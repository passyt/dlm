package com.derbysoft.nuke.dlm.server.codec;

import com.alibaba.fastjson.JSON;
import com.derbysoft.nuke.dlm.model.IPermitResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by passyt on 16-9-5.
 */
public class PermitResponse2HttpEncoder extends MessageToMessageEncoder<IPermitResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, IPermitResponse response, List<Object> out) throws Exception {
        DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(JSON.toJSONBytes(response)));
        httpResponse.headers().add("Content-Type", "application/json; charset=utf-8");
        httpResponse.headers().add("Server", "Netty-5.0");
        ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        StringWriter writer = new StringWriter();
        cause.printStackTrace(new PrintWriter(writer));

        DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.copiedBuffer(writer.toString().getBytes("UTF-8")));
        httpResponse.headers().add("Content-Type", "application/json; charset=utf-8");
        httpResponse.headers().add("Server", "Netty-5.0");
        ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
    }
}
