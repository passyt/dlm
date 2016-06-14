package com.derby.nuke.dlm.server.domain;

public enum RequestType {

	Socket("socket"), HTTP("http"), WebSocketText("websocket-text"), WebSocketBinary("websocket-binary");

	private final String alias;

	private RequestType(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}

	public static RequestType aliasOf(String value) {
		for (RequestType o : values()) {
			if (o.getAlias().equals(value)) {
				return o;
			}
		}

		throw new IllegalArgumentException("Unknown value [" + value + "]");
	}

}
