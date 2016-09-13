package com.derbysoft.nuke.dlm.client.http.impl;

import com.derby.nuke.common.adapter.exception.BusinessException;
import com.derbysoft.nuke.dlm.IPermit;
import com.derbysoft.nuke.dlm.IPermitManager;
import com.derbysoft.nuke.dlm.PermitSpec;
import com.derbysoft.nuke.dlm.client.utils.DataUtil;

import java.util.Map;

/**
 * Created by suny on 2016-09-06.
 */
public class PermitManagerImpl extends HttpPermitClient implements IPermitManager {

    @Override
    public boolean register(String resourceId, String permitName, PermitSpec spec) {
        Map<String, String> response = DataUtil.transformerResponse(client.get(serverUrl + "/register/" + resourceId + "/permitname/" + permitName + "/spec/" + spec));
        if (response.get("errorMessage") != null) {
            throw new BusinessException(response.get("permitId"), (String) response.get("errorMessage"));

        }
        return Boolean.valueOf(String.valueOf(response.get("successful")));
    }

    @Override
    public boolean unregister(String resourceId) {
        Map<String, String> response = DataUtil.transformerResponse(client.get(serverUrl + "/unregister/" + resourceId));
        if (response.get("errorMessage") != null) {
            throw new BusinessException(response.get("permitId"), (String) response.get("errorMessage"));
        }
        return Boolean.valueOf(String.valueOf(response.get("successful")));
    }

    @Override
    public boolean isExisting(String resourceId) {
        Map<String, String> response = DataUtil.transformerResponse(client.get(serverUrl + "/existing/" + resourceId));
        if (response.get("errorMessage") != null) {
            throw new BusinessException(response.get("permitId"), (String) response.get("errorMessage"));
        }
        return Boolean.valueOf(String.valueOf(response.get("existing")));
    }

    @Override
    public IPermit getPermit(String resourceId) {
        return new PermitImpl(resourceId);
    }
}
