package com.derbysoft.nuke.dlm.client.tcp;

import com.derbysoft.nuke.dlm.model.IPermitRequest;
import com.derbysoft.nuke.dlm.model.IPermitResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
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

/**
 * Created by DT219 on 2016-09-14.
 */
public class AbstractTcpPermitService {

    private Logger log = LoggerFactory.getLogger(AbstractTcpPermitService.class);

    public AbstractTcpPermitService() {
    }

    private Bootstrap bootstrap;
    private final EventLoopGroup group = new NioEventLoopGroup();
    private ChannelFuture channelFuture;

    public AbstractTcpPermitService(String host, int port) throws InterruptedException {
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
                                .addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender())
                                .addLast("protobufEncoder", new ProtobufEncoder());
                    }
                });
        channelFuture = bootstrap.connect(host, port).sync();


    }


    public <RS extends IPermitResponse> RS sendMessage(IPermitRequest<RS> request) throws Exception {

        return null;
    }

}
