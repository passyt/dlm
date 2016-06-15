package com.derby.nuke.dlm.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.derby.nuke.dlm.server.domain.Request;
import com.derby.nuke.dlm.server.domain.Response;

@Service
public class Handler implements IHandler {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void execute(Request request, Response response) {
		response.write("Hello world!");
	}

}
