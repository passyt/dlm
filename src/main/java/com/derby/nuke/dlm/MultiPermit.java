package com.derby.nuke.dlm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.derby.nuke.dlm.local.LocalPermit;

public class MultiPermit extends LocalPermit {

	final List<IPermit> permits = new ArrayList<IPermit>();

	public MultiPermit(IPermit... permits) {
		if (permits.length == 0) {
			throw new IllegalArgumentException("permits are required");
		}

		this.permits.addAll(Arrays.asList(permits));
	}

	@Override
	public void acquire() {
		for (;;) {
			if (tryAcquire()) {
				return;
			}
		}
	}

	@Override
	public boolean tryAcquire() {
		List<IPermit> acquiredPermits = new ArrayList<IPermit>();
		boolean isAcquired = true;
		for (IPermit each : permits) {
			if (each.tryAcquire()) {
				acquiredPermits.add(each);
			} else {
				isAcquired = false;
				break;
			}
		}

		if (isAcquired) {
			return true;
		}

		for (IPermit each : acquiredPermits) {
			each.release();
		}

		return false;
	}

	@Override
	public boolean tryAcquire(long timeout, TimeUnit unit) {
		List<IPermit> acquiredPermits = new ArrayList<IPermit>();
		boolean isAcquired = true;
		for (IPermit each : permits) {
			if (each.tryAcquire(timeout, unit)) {
				acquiredPermits.add(each);
			} else {
				isAcquired = false;
				break;
			}
		}

		if (isAcquired) {
			return true;
		}

		for (IPermit each : acquiredPermits) {
			each.release();
		}

		return false;
	}

	@Override
	public void release() {
		for (IPermit each : permits) {
			each.release();
		}
	}

}
