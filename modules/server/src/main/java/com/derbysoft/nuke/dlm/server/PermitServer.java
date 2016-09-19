package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.server.initializer.PermitServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

/**
 * Created by passyt on 16-9-2.
 */
@Component
public class PermitServer {

    private static final Logger log = LoggerFactory.getLogger(PermitServer.class);

    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final Map<Integer, PermitServerInitializer> initializers;

    @Autowired
    public PermitServer(@Qualifier("bossGroup") EventLoopGroup bossGroup, @Qualifier("workerGroup") EventLoopGroup workerGroup, @Qualifier("permServerInitializers") Map<Integer, PermitServerInitializer> initializers) {
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.initializers = initializers;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
        }, "Netty-Shutdown"));
    }

    @PostConstruct
    public void startup() {
        for (Map.Entry<Integer, PermitServerInitializer> each : initializers.entrySet()) {
            startup(each.getKey(), each.getValue());
        }
    }

    protected PermitServer startup(int port, PermitServerInitializer initializer) {
        log.info("Startup server on {} port {}", initializer.getType(), port);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_RCVBUF, 10 * 1024)
                .option(ChannelOption.SO_SNDBUF, 10 * 1024)
                .option(EpollChannelOption.SO_REUSEPORT, true)
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024)
                .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(initializer);

        Thread thread = new Thread(() -> {
            try {
                bootstrap.bind(port).sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                shutdown();
            }
        }, "Server-" + initializer.getType() + "@" + port);
        thread.setDaemon(false);
        thread.start();
        return this;
    }

    @PreDestroy
    public void shutdown() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

}
