package com.derbysoft.nuke.dlm.client.http.impl;

import com.derby.nuke.common.ws.client.RetryExecutor;
import com.derby.nuke.common.ws.client.SimpleClient;
import com.derby.nuke.common.ws.client.SimpleClientImpl;


/**
 * Created by suny on 2016-09-06.
 */
public class HttpPermitClient {

    protected SimpleClient client;
    protected String serverUrl;

    public HttpPermitClient() {
        client = new SimpleClientImpl();
        client.setEncoding("UTF-8");
        client.setContentType("text/json");
        client.setExecutor(new RetryExecutor(3, 1));
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
