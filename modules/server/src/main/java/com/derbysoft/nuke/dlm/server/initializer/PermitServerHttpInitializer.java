package com.derbysoft.nuke.dlm.server.initializer;

import com.derbysoft.nuke.dlm.server.codec.Uri2PermitRequestDecoder;
import com.derbysoft.nuke.dlm.server.handler.PermitServerHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by passyt on 16-9-4.
 */
@Component("permitServerHttpInitializer")
public class PermitServerHttpInitializer extends PermitServerInitializer {

    @Autowired
    public PermitServerHttpInitializer(PermitServerHandler handler) {
        super(handler);
    }

    @Override
    public String getType() {
        return TYPE_HTTP;
    }

    @Override
    protected void beforeInitChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast("logger", new LoggingHandler(LogLevel.DEBUG))
                .addLast("http-decoder", new HttpRequestDecoder())
                .addLast("http-aggregator", new HttpObjectAggregator(65536))
                .addLast("json-decoder", new Uri2PermitRequestDecoder())
                .addLast("http-encoder", new HttpResponseEncoder())
                //TODO
                .addLast("http-chunked", new ChunkedWriteHandler());
    }

    @Override
    protected void afterInitChannel(SocketChannel socketChannel) throws Exception {
    }

}
