package com.derby.nuke.dlm.server.dispatcher;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.derby.nuke.dlm.server.domain.Request;
import com.derby.nuke.dlm.server.domain.Response;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 
 * @author Passyt
 *
 */
public class HandlerDispatcher implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(HandlerDispatcher.class);

	private final BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();
	private final Executor executor;
	private boolean running = true;

	public HandlerDispatcher(Executor messageExecutor) {
		super();
		this.executor = messageExecutor;
	}

	public void addMessage(Request request) {
		this.queue.add(request);
	}

	public void clear(Channel channel) {
		queue.clear();
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
			GameHandler handler = (GameHandler) HandlerDispatcher.this.handlerMap.get(Integer.valueOf(messageId));
			if (handler != null) {
				handler.execute(request, response);
			} else {
				HandlerDispatcher.logger.warn("指令 [{}]找不到", messageId);
			}

			switch (request.getType()) {
			case HTTP:
				HttpUtils.sendHttpResponse(this.request.getCtx(), (FullHttpRequest) this.request.getMsg(), response.getResp());
				break;
			case WebSocketText:
				this.request.getCtx().channel().write(new TextWebSocketFrame(response.getWebSocketRespone()));
				break;
			case Socket:
			case WebSocketBinary:
				response.getChannel().writeAndFlush((ByteBuf) response.getRtMessage());
				break;
			}
		}
	}
}
