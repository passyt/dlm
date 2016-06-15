package com.derby.nuke.dlm.server.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.derby.nuke.dlm.server.dispatcher.HandlerDispatcher;
import com.derby.nuke.dlm.server.domain.Request;
import com.derby.nuke.dlm.server.domain.RequestType;
import com.derby.nuke.dlm.server.utils.HttpUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author Passyt
 *
 */
public class ServerAdapter extends SimpleChannelInboundHandler<Object> {

	private static final String WEBSOCKET_PATH = "/websocket";

	private static final Logger log = LoggerFactory.getLogger(ServerAdapter.class);

	private WebSocketServerHandshaker handshaker;
	private HandlerDispatcher handlerDispatcher;

	public ServerAdapter() {
	}

	public ServerAdapter(HandlerDispatcher handlerDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
	}

	public void setHandshaker(WebSocketServerHandshaker handshaker) {
		this.handshaker = handshaker;
	}

	public void setHandlerDispatcher(HandlerDispatcher handlerDispatcher) {
		this.handlerDispatcher = handlerDispatcher;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if ((msg instanceof ByteBuf)) {
			socketRequest(ctx, msg);
		} else if ((msg instanceof FullHttpRequest)) {
			httpFullRequest(ctx, msg);
		} else if ((msg instanceof WebSocketFrame)) {
			handleWebSocketFrame(ctx, msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	private void socketRequest(ChannelHandlerContext ctx, Object msg) throws Exception {
		this.handlerDispatcher.dispatch(new Request(RequestType.Socket, ctx, msg));
	}

	private void httpFullRequest(ChannelHandlerContext ctx, Object msg) throws Exception {
		FullHttpRequest request = (FullHttpRequest) msg;
		if (!request.getDecoderResult().isSuccess()) {
			HttpUtils.sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}

		if (request.getMethod() == HttpMethod.POST) {
			this.handlerDispatcher.dispatch(new Request(RequestType.HTTP, ctx, msg));
		} else if (request.getMethod() == HttpMethod.GET) {
			if (WEBSOCKET_PATH.equals(request.getUri())) {
				WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(request), null, false);

				this.handshaker = wsFactory.newHandshaker(request);
				if (this.handshaker == null) {
					WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
				} else {
					this.handshaker.handshake(ctx.channel(), request);
				}
			} else {
				ByteBuf content = null;
				if ("/test".equals(request.getUri())) {
					content = Unpooled.copiedBuffer(
							"<html><head><title>Web Socket Test</title></head>\r\n<body>\r\n<script type=\"text/javascript\">\r\nvar socket;\r\nif (!window.WebSocket) {\r\n  window.WebSocket = window.MozWebSocket;\r\n}\r\nif (window.WebSocket) {\r\n  socket = new WebSocket(\""
									+ getWebSocketLocation(request) + "\");" + "\r\n" + "  socket.onmessage = function(event) {" + "\r\n"
									+ "    var ta = document.getElementById('responseText');" + "\r\n" + "    ta.value = event.data + '\\n' + ta.value" + "\r\n"
									+ "  };" + "\r\n" + "  socket.onopen = function(event) {" + "\r\n" + "    var ta = document.getElementById('responseText');"
									+ "\r\n" + "    ta.value = \"Web Socket opened!\";" + "\r\n" + "  };" + "\r\n" + "  socket.onclose = function(event) {"
									+ "\r\n" + "    var ta = document.getElementById('responseText');" + "\r\n"
									+ "    ta.value = \"Web Socket closed\" + '\\n'+ ta.value; " + "\r\n" + "  };" + "\r\n" + "} else {" + "\r\n"
									+ "  alert(\"Your browser does not support Web Socket.\");" + "\r\n" + '}' + "\r\n" + "\r\n" + "function send(message) {"
									+ "\r\n" + "  if (!window.WebSocket) { return; }" + "\r\n" + "  if (socket.readyState == WebSocket.OPEN) {" + "\r\n"
									+ "    socket.send(message);" + "\r\n" + "  } else {" + "\r\n" + "    alert(\"The socket is not open.\");" + "\r\n" + "  }"
									+ "\r\n" + '}' + "\r\n" + "</script>" + "\r\n" + "<form onsubmit=\"return false;\">" + "\r\n"
									+ "<input type=\"text\" style=\"width:100%;height:22px;\" name=\"message\" value=\"999,are you ok?\"/>"
									+ "<input type=\"button\" value=\"Send Web Socket Data\"" + "\r\n" + "       onclick=\"send(this.form.message.value)\" />"
									+ "\r\n" + "<h3>Output</h3>" + "\r\n" + "<textarea id=\"responseText\" style=\"width: 1348px; height:599px;\"></textarea>"
									+ "\r\n" + "</form>" + "\r\n" + "</body>" + "\r\n" + "</html>" + "\r\n",
							CharsetUtil.US_ASCII);
				} else {
					content = Unpooled.copiedBuffer("DLM is alive", CharsetUtil.US_ASCII);
				}
				FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
				response.headers().set("Content-Type", "text/html; charset=UTF-8");
				HttpHeaders.setContentLength(response, content.readableBytes());
				HttpUtils.sendHttpResponse(ctx, request, response);
			}
		} else {
			HttpUtils.sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
			return;
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, Object msg) {
		WebSocketFrame frame = (WebSocketFrame) msg;
		if ((frame instanceof CloseWebSocketFrame)) {
			this.handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());

			return;
		}
		if ((frame instanceof PingWebSocketFrame)) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));

			return;
		}
		if ((!(frame instanceof TextWebSocketFrame)) || ((frame instanceof BinaryWebSocketFrame))) {
			throw new UnsupportedOperationException(String.format("%s frame types not supported", new Object[] { frame.getClass().getName() }));
		}

		try {
			if ((frame instanceof TextWebSocketFrame)) {
				this.handlerDispatcher.dispatch(new Request(RequestType.WebSocketText, ctx, msg));
			} else
				this.handlerDispatcher.dispatch(new Request(RequestType.WebSocketBinary, ctx, msg));
		} catch (Exception e) {
			log.error("Failed", e);
		}
	}

	private static String getWebSocketLocation(FullHttpRequest req) {
		return "ws://" + req.headers().get("Host") + WEBSOCKET_PATH;
	}

}
