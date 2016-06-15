package com.derby.nuke.dlm.server.domain;

import java.nio.charset.Charset;
import java.util.List;

import com.google.common.base.Splitter;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class Command {

	private List<String> dataFrames;
	private ByteBuf byteBuf;
	private RequestType type;
	private int readIndex = 0;

	public Command(RequestType type, Object message) {
		this.type = type;
		switch (type) {
		case HTTP:
			FullHttpRequest request = (FullHttpRequest) message;
			String content = request.content().toString(Charset.forName("UTF-8"));
			this.dataFrames = Splitter.on(",").trimResults().splitToList(content);
			break;
		case WebSocketText:
			content = ((TextWebSocketFrame) message).text();
			this.dataFrames = Splitter.on(",").trimResults().splitToList(content);
			break;
		case Socket:
			this.byteBuf = ((ByteBuf) message).copy();
			break;
		case WebSocketBinary:
			BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) message;
			this.byteBuf = binaryFrame.content();
			break;
		}
	}

	String readString() {
		switch (type) {
		case HTTP:
		case WebSocketText:
			return (String) this.dataFrames.get(this.readIndex++);
		case Socket:
		case WebSocketBinary:
			int length = this.byteBuf.readInt();
			if (length < 0) {
				return null;
			}

			byte[] c = new byte[length];
			this.byteBuf.readBytes(c);
			return new String(c);
		}

		return null;
	}

	int readInt() {
		switch (type) {
		case HTTP:
		case WebSocketText:
			return Integer.parseInt((String) this.dataFrames.get(this.readIndex++));
		case Socket:
		case WebSocketBinary:
			return this.byteBuf.readInt();
		}

		return -1;
	}

	short readShort() {
		switch (type) {
		case HTTP:
		case WebSocketText:
			return Short.parseShort((String) this.dataFrames.get(this.readIndex++));
		case Socket:
		case WebSocketBinary:
			return this.byteBuf.readShort();
		}

		return -1;
	}

	long readLong() {
		switch (type) {
		case HTTP:
		case WebSocketText:
			return Long.parseLong((String) this.dataFrames.get(this.readIndex++));
		case Socket:
		case WebSocketBinary:
			return this.byteBuf.readLong();
		}

		return -1L;
	}

	float readFloat() {
		switch (type) {
		case HTTP:
		case WebSocketText:
			return Float.parseFloat((String) this.dataFrames.get(this.readIndex++));
		case Socket:
		case WebSocketBinary:
			return this.byteBuf.readFloat();
		}

		return -1.0F;
	}
}
