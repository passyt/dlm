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
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by passyt on 16-9-2.
 */
public class PermitClient {

    private final Channel channel;
    private final Bootstrap bootstrap;
    private final String host;
    private final int port;

    public PermitClient(String host, int port) throws Exception {
        this.host = host;
        this.port = port;

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                        .addLast("decoder", new StringDecoder())
                        .addLast("frameEncoder",
                                new ProtobufVarint32LengthFieldPrepender())
                        .addLast("protobufEncoder", new ProtobufEncoder())
                        .addLast("handler", new PermitClientHandler());
            }
        });
        channel = bootstrap.connect(host, port).sync().channel();
    }

    public void sendMessage(Protobuf.AcquireRequest request) throws InterruptedException {
        System.out.println(request);
        channel.writeAndFlush(request);
    }

    public void shutdown() {
        channel.close();
//        channel.parent().close();
    }

    public static void main(String[] args) throws Exception {
        PermitClient client = new PermitClient("127.0.0.1", 8081);
        try {
            Protobuf.AcquireRequest.Builder builder = Protobuf.AcquireRequest.newBuilder();
            builder.setPermitId("123");
            client.sendMessage(builder.build());
        } finally {
            client.shutdown();
        }
    }

}
