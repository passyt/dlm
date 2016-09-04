package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.server.initializer.PermitServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by passyt on 16-9-2.
 */
@Component
public class PermitServer {

    private static final Logger log = LoggerFactory.getLogger(PermitServer.class);

    private final int port;
    private final PermitServerInitializer initializer;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    private final ServerBootstrap bootstrap;

    @Autowired
    public PermitServer(@Value("${server.tcp.port}") int port, @Qualifier("permitServerTcpInitializer") PermitServerInitializer initializer, @Qualifier("bossGroup") EventLoopGroup bossGroup, @Qualifier("workerGroup") EventLoopGroup workerGroup) {
        this.port = port;
        this.initializer = initializer;
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;

        bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024)
                .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(initializer);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
        }));
    }

    @PostConstruct
    public void startup() throws Exception {
        log.info("Startup server on {} port {}", initializer.getType(), port);
        bootstrap.bind(port).sync().channel().closeFuture().sync();
    }

    @PreDestroy
    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
