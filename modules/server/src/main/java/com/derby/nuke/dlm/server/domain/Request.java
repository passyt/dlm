package com.derby.nuke.dlm.server.domain;

import io.netty.channel.ChannelHandlerContext;

public class Request {

	private final RequestType type;
	private final ChannelHandlerContext ctx;
	private final Object msg;

	public Request(RequestType type, ChannelHandlerContext ctx, Object msg) {
		super();
		this.type = type;
		this.ctx = ctx;
		this.msg = msg;
	}

	public RequestType getType() {
		return type;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public Object getMsg() {
		return msg;
	}

}
