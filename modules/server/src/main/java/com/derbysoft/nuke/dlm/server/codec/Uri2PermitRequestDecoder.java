package com.derbysoft.nuke.dlm.server.codec;

import com.alibaba.fastjson.JSON;
import com.derbysoft.nuke.dlm.model.*;
import com.google.common.base.Splitter;
import com.google.common.base.Utf8;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <ul>
 * <li>/register/${permitId}/resourcename/${resourcename}/spec/${spec}</li>
 * <li>/unregister/${permitId}</li>
 * <li>/existing/${permitId}</li>
 * <li>/permit/${permitId}/action/acquire</li>
 * <li>/permit/${permitId}/action/tryacquire</li>
 * <li>/permit/${permitId}/action/tryacquire/timeout/10/timeunit/seconds</li>
 * </ul>
 * Created by passyt on 16-9-4.
 */
public class Uri2PermitRequestDecoder extends MessageToMessageDecoder<FullHttpRequest> {

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest request, List<Object> out) throws Exception {
        String uri = request.uri();
        Map<String, String> parameters = toParameters(uri.substring(1));
        if (parameters.containsKey("register")) {
            String permitId = parameters.get("register");
            out.add(new RegisterRequest(permitId, parameters.get("resourcename"), parameters.get("spec")));
        } else if (parameters.containsKey("unregister")) {
            String permitId = parameters.get("unregister");
            out.add(new UnRegisterRequest(permitId));
        } else if (parameters.containsKey("existing")) {
            String permitId = parameters.get("existing");
            out.add(new ExistingRequest(permitId));
        } else if (parameters.containsKey("permit")) {
            String permitId = parameters.get("permit");
            String action = parameters.get("action");
            if ("acquire".equals(action)) {
                out.add(new AcquireRequest(permitId));
            } else if ("tryacquire".equals(action)) {
                if (parameters.containsKey("timeout") && parameters.containsKey("timeunit")) {
                    //TODO
                } else {

                }
            } else if ("release".equals(action)) {
                //TODO
            } else {
                //TODO
            }

        }
    }

    private static Map<String, String> toParameters(String string) {
        String key = null;
        Map<String, String> paramters = new HashMap<>();
        for (Iterator<String> it = Splitter.on("/").split(string).iterator(); it.hasNext(); ) {
            String value = it.next();
            if (key == null) {
                key = value;
            } else {
                paramters.put(key, value);
                key = null;
            }
        }
        return paramters;
    }

}
