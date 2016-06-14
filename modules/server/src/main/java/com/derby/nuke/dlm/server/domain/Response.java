package com.derby.nuke.dlm.server.domain;

import java.util.LinkedList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 
 * @author Passyt
 *
 */
public class Response {

	private final RequestType type;
	private Object message;

	public Response(RequestType type) {
		this.type = type;
		switch (type) {
		case HTTP:
		case WebSocketText:
			this.message = new LinkedList<>();
			break;
		case Socket:
		case WebSocketBinary:
			this.message = Unpooled.buffer();
			break;
		}
	}

	@SuppressWarnings("unchecked")
	public void write(Object obj) {
		switch (type) {
		case HTTP:
		case WebSocketText:
			((List<Object>) this.message).add(obj);
			break;
		case Socket:
		case WebSocketBinary:
			ByteBuf buf = (ByteBuf) this.message;
			if (obj == null)
				return;
			if ((obj instanceof String)) {
				String tmp = (String) obj;
				buf.writeInt(tmp.getBytes().length);
				buf.writeBytes(tmp.getBytes());
				return;
			}
			if ((obj instanceof Short))
				buf.writeShort(((Short) obj).shortValue());
			else if ((obj instanceof Integer))
				buf.writeInt(((Integer) obj).intValue());
			else if ((obj instanceof Long))
				buf.writeLong(((Long) obj).longValue());
			else if ((obj instanceof Float))
				buf.writeFloat(((Float) obj).floatValue());
			else if ((obj instanceof Byte))
				buf.writeByte(((Byte) obj).byteValue());
			break;
		}
	}

	public Object getMessage() {
		return message;
	}

}