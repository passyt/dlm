package com.derbysoft.nuke.dlm.client.utils;

import net.sourceforge.plastosome.json.JSON;
import net.sourceforge.plastosome.json.JSONParseException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

/**
 * Created by suny on 2016-09-07.
 */
public class DataUtil {

    public static Map<String,String> transformerResponse(String res) {
        try {
            return (Map<String, String>) JSON.deserialize(new StringReader(res));
        } catch (JSONParseException e) {
            throw new IllegalArgumentException();
        } catch (IOException e1) {
            throw new IllegalArgumentException();
        }
    }
}
