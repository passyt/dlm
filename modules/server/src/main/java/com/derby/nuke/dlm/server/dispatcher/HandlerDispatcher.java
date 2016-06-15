package com.derby.nuke.dlm.server.dispatcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.derby.nuke.dlm.server.domain.Request;
import com.derby.nuke.dlm.server.domain.Response;
import com.derby.nuke.dlm.server.handler.IHandler;
import com.derby.nuke.dlm.server.utils.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 
 * @author Passyt
 *
 */
@Service
public class HandlerDispatcher implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(HandlerDispatcher.class);

	private final BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();
	private final Executor executor;
	private IHandler handler;
	private boolean running = true;

	public HandlerDispatcher() {
		this(Executors.newFixedThreadPool(10));
	}

	public HandlerDispatcher(Executor messageExecutor) {
		super();
		this.executor = messageExecutor;
	}

	public void dispatch(Request request) {
		this.queue.add(request);
	}

	public void clear(Channel channel) {
		queue.clear();
	}

	@Required
	public void setHandler(IHandler handler) {
		this.handler = handler;
	}

	public void run() {
		while (running) {
			try {
				Request request = queue.take();
				this.executor.execute(new MessageWorker(request));
			} catch (Exception e) {
				logger.warn("Execute request failed", e);
			}
		}
	}

	public void stop() {
		this.running = false;
	}

	private final class MessageWorker implements Runnable {

		private final Request request;

		private MessageWorker(Request request) {
			this.request = request;
		}

		public void run() {
			try {
				handMessageQueue();
			} catch (Exception e) {
				logger.error("Execute request failed", e);
			}
		}

		private void handMessageQueue() {
			Response response = new Response(request.getType());
			handler.execute(request, response);

			switch (request.getType()) {
			case HTTP:
				HttpUtils.sendHttpResponse(this.request.getCtx(), (FullHttpRequest) this.request.getMessage(), response.getResp());
				break;
			case WebSocketText:
				this.request.getCtx().channel().write(new TextWebSocketFrame(response.getWebSocketRespone()));
				break;
			case Socket:
			case WebSocketBinary:
				this.request.getCtx().channel().writeAndFlush((ByteBuf) response.getMessage());
				break;
			}
		}
	}
}
