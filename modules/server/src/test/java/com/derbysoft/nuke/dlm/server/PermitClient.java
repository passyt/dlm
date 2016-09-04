package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.model.IPermitRequest;
import com.derbysoft.nuke.dlm.model.RegisterRequest;
import com.derbysoft.nuke.dlm.server.codec.PermitRequest2ProtoBufEncoder;
import com.derbysoft.nuke.dlm.server.codec.ProtoBuf2PermitResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by passyt on 16-9-2.
 */
public class PermitClient {

    private static Logger log = LoggerFactory.getLogger(PermitClient.class);

    private final Channel channel;
    private final Bootstrap bootstrap;
    private final String host;
    private final int port;
    private final EventLoopGroup group = new NioEventLoopGroup();

    public PermitClient(String host, int port) throws Exception {
        this.host = host;
        this.port = port;

        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024)
                .option(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast("logger", new LoggingHandler(LogLevel.DEBUG))
                                .addLast("frameDecoder", new ProtobufVarint32FrameDecoder())
                                .addLast("protobufDecoder", new ProtobufDecoder(com.derbysoft.nuke.dlm.model.Protobuf.Response.getDefaultInstance()))
                                .addLast("permitDecoder", new ProtoBuf2PermitResponseDecoder())

                                .addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender())
                                .addLast("protobufEncoder", new ProtobufEncoder())
                                .addLast("permitEncoder", new PermitRequest2ProtoBufEncoder())
                                .addLast("handler", new PermitClientHandler());
                    }
                });
        channel = bootstrap.connect(host, port).sync().channel().closeFuture().sync().channel();
//        channel = bootstrap.connect(host, port).sync().channel();
    }

    public void sendMessage(IPermitRequest request) throws InterruptedException {
        log.info("Send request >>| {}", request);
        channel.writeAndFlush(request).sync();
    }

    public void shutdown() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        PermitClient client = new PermitClient("127.0.0.1", 8081);
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            tasks.add(() -> {
                client.sendMessage(new RegisterRequest(new Random().nextInt(100) + "-" + finalI, "SemaphorePermit", "total=100"));
                return null;
            });
        }

        ExecutorService pool = Executors.newFixedThreadPool(50);
        try {
            long start = System.currentTimeMillis();
            pool.invokeAll(tasks);
            System.out.println("Cost " + (System.currentTimeMillis() - start) + " ms");
            pool.shutdown();
        } finally {
            client.shutdown();
        }
    }

}