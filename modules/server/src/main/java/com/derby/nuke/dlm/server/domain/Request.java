package com.derby.nuke.dlm.server.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author Passyt
 *
 */
public class Request {

	private final RequestType type;
	private final ChannelHandlerContext ctx;
	private final Object message;

	private String resourceKey;
	private String methodName;
	private List<String> argurments;

	public Request(RequestType type, ChannelHandlerContext ctx, Object message) {
		super();
		this.type = type;
		this.ctx = ctx;
		this.message = message;
		parse(message);
	}

	private void parse(Object message) {
		Command command = new Command(this.type, message);

		this.resourceKey = command.readString();
		this.methodName = command.readString();
		String next = command.readString();
		if (next == null) {
			this.argurments = new ArrayList<>();
		} else {
			this.argurments = Splitter.on("|").trimResults().splitToList(next);
		}
	}

	public RequestType getType() {
		return type;
	}

	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public String getMethodName() {
		return methodName;
	}

	public List<String> getArgurments() {
		return argurments;
	}

	public Object getMessage() {
		return message;
	}

}
