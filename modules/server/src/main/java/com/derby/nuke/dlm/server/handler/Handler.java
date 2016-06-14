package com.derby.nuke.dlm.server.handler;

import com.derby.nuke.dlm.server.domain.Request;
import com.derby.nuke.dlm.server.domain.Response;

/**
 * 
 * @author Passyt
 *
 */
public interface Handler {

	void execute(Request request, Response response);

}
