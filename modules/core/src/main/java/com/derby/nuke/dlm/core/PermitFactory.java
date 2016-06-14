package com.derby.nuke.dlm.core;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 
 * @author Passyt
 *
 */
public class PermitFactory implements IPermitFactory {

	private Map<String, IPermit> permits = new HashMap<>();

	@Override
	public IPermit getPermit(String key) {
		IPermit permit = permits.get(key);
		if (permit == null) {
			throw new IllegalArgumentException("Permit not found by [" + key + "]");
		}

		return permit;
	}

	public PermitFactory register(String key, IPermit permit) {
		permits.put(key, permit);
		return this;
	}

	public Map<String, IPermit> getPermits() {
		return Maps.newHashMap(permits);
	}

}
