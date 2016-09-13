package com.derbysoft.nuke.dlm.client.http;

import com.alibaba.fastjson.JSON;
import com.derby.nuke.common.ws.client.RetryExecutor;
import com.derby.nuke.common.ws.client.SimpleClient;
import com.derby.nuke.common.ws.client.SimpleClientImpl;
import com.derbysoft.nuke.dlm.exception.PermitException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;


/**
 * Created by suny on 2016-09-06.
 */
public abstract class AbstractHttpPermitClient {

    protected SimpleClient client = new SimpleClientImpl();
    protected String serverUrl;

    public AbstractHttpPermitClient() {
        client = new SimpleClientImpl();
        client.setEncoding("UTF-8");
        client.setContentType("text/json");
        client.setExecutor(new RetryExecutor(3, 1));
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    protected Map<String, Object> execute(String uri) {
        String url = null;
        if (uri.startsWith("/")) {
            url = serverUrl + uri;
        } else {
            url = serverUrl + "/" + uri;
        }
        String text = client.get(url);
        Map<String, Object> response = JSON.parseObject(text, Map.class);
        if (response.get("errorMessage") != null) {
            throw new PermitException((String) response.get("errorMessage"));
        }

        return response;
    }

    protected String toUri(String template, Object... args) {
        String[] strings = new String[args.length];
        int i = 0;
        for (Object each : args) {
            try {
                strings[i++] = URLEncoder.encode(String.valueOf(each), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }
        return String.format(template, strings);
    }

}
