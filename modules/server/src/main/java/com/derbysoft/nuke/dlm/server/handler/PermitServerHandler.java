package com.derbysoft.nuke.dlm.server.handler;

import com.alibaba.fastjson.JSON;
import com.derbysoft.nuke.dlm.IPermitService;
import com.derbysoft.nuke.dlm.model.IPermitRequest;
import com.derbysoft.nuke.dlm.model.IPermitResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by passyt on 16-9-2.
 */
@ChannelHandler.Sharable
@Component
public class PermitServerHandler extends ChannelHandlerAdapter {

    private static Logger log = LoggerFactory.getLogger(PermitServerHandler.class);
    private IPermitService permitService;
    private Executor executor = Executors.newCachedThreadPool();

    @Autowired
    public PermitServerHandler(IPermitService permitService) {
        this.permitService = permitService;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Logger streamLog = LoggerFactory.getLogger("http.StreamLog");
        IPermitRequest request = (IPermitRequest) msg;
        executor.execute(() -> {
            streamLog.info("Receive request <<| {} [{}] from {}", request, request.getClass().getSimpleName(), ctx.channel().remoteAddress().toString());
            IPermitResponse response = null;
            try {
                response = permitService.execute(request);
            } catch (Exception e) {
                log.error("Catch exception by request [" + request + "]", e);
                response = request.newResponse();
                response.setResourceId(request.getResourceId());
                response.setHeader(request.getHeader());
                response.setErrorMessage(e.getMessage());
            }
            streamLog.info("Return response >>| {} [{}] to {}", response, response.getClass().getSimpleName(), ctx.channel().remoteAddress().toString());
            ctx.writeAndFlush(response);
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Catch exception", cause);
        //TODO return back error message
    }

}
