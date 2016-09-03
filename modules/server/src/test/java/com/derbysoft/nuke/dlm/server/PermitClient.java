package com.derbysoft.nuke.dlm.server;

import com.derbysoft.nuke.dlm.model.Protobuf;
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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by passyt on 16-9-2.
 */
public class PermitClient {

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
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast("logger", new LoggingHandler(LogLevel.TRACE))
                                .addLast("frameDecoder", new ProtobufVarint32FrameDecoder())
                                .addLast("protobufDecoder", new ProtobufDecoder(com.derbysoft.nuke.dlm.model.Protobuf.AcquireRequest.getDefaultInstance()))
                                .addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender())
                                .addLast("protobufEncoder", new ProtobufEncoder())
                                .addLast("handler", new PermitClientHandler());
                    }
                });
//        channel = bootstrap.connect(host, port).sync().channel().closeFuture().sync().channel();
        channel = bootstrap.connect(host, port).sync().channel();
    }

    public void sendMessage(Protobuf.AcquireRequest request) throws InterruptedException {
        System.out.println(request);
        channel.writeAndFlush(request).sync();
    }

    public void shutdown() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        PermitClient client = new PermitClient("127.0.0.1", 8081);
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            tasks.add(() -> {
                Protobuf.AcquireRequest.Builder builder = Protobuf.AcquireRequest.newBuilder();
                builder.setPermitId(new Random().nextInt(100) + "-" + finalI);
                client.sendMessage(builder.build());
                return null;
            });
        }

        ExecutorService pool = Executors.newFixedThreadPool(5);
        try {
            long start = System.currentTimeMillis();
            pool.invokeAll(tasks);
            pool.shutdown();
            System.out.println("Cost " + (System.currentTimeMillis() - start) + " ms");
        } finally {
            client.shutdown();
        }
    }

//    public static void main(String[] args) throws Exception {
//        PermitClient client = new PermitClient("127.0.0.1", 8081);
//        List<Protobuf.AcquireRequest> messages = new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            Protobuf.AcquireRequest.Builder builder = Protobuf.AcquireRequest.newBuilder();
//            builder.setPermitId(new Random().nextInt(100) + "-" + i);
//            messages.add(builder.build());
//        }
//
//        try {
//            long start = System.currentTimeMillis();
//            for(Protobuf.AcquireRequest message : messages){
//                client.sendMessage(message);
//            }
//            System.out.println("Cost " + (System.currentTimeMillis() - start) + " ms");
//        } finally {
//            client.shutdown();
//        }
//    }

}