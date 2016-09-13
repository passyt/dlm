package com.derbysoft.nuke.dlm.client.utils;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

/**
 * Created by suny on 2016-09-07.
 */
public class DataUtil {

    public static Map<String, String> transformerResponse(String text) {
        return JSON.parseObject(text, Map.class);
    }
}
