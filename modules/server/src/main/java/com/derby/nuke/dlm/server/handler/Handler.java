package com.derby.nuke.dlm.server.handler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

import com.derby.nuke.dlm.core.IPermit;
import com.derby.nuke.dlm.core.IPermitFactory;
import com.derby.nuke.dlm.server.domain.Request;
import com.derby.nuke.dlm.server.domain.Response;

@Service
public class Handler implements IHandler {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private IPermitFactory permitFactory;

	@Override
	public void execute(Request request, Response response) {
		IPermit permit = permitFactory.getPermit(request.getResourceKey());
		if (permit == null) {
			response.setSuccess(false);
			response.write("No permit found by [" + request.getResourceKey() + "]");
			return;
		}

		if ("acquire".equals(request.getMethodName())) {
			permit.acquire();
			response.write(true);
		} else if ("tryAcquire".equals(request.getMethodName())) {
			if (request.getArgurments().isEmpty()) {
				response.write(permit.tryAcquire());
			} else {
				Long timeout = Long.parseLong(request.getArgurments().get(0));
				TimeUnit unit = TimeUnit.valueOf(request.getArgurments().get(1));
				response.write(permit.tryAcquire(timeout, unit));
			}
		} else if ("release".equals(request.getMethodName())) {
			permit.release();
			response.write(true);
		} else {
			response.setSuccess(false);
			response.write("No method [" + request.getMethodName() + "] found by [" + request.getResourceKey() + "]");
		}
	}

	@Required
	public void setPermitFactory(IPermitFactory permitFactory) {
		this.permitFactory = permitFactory;
	}

}
