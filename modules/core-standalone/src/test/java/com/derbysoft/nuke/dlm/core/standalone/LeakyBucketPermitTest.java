package com.derbysoft.nuke.dlm.core.standalone;

import com.derbysoft.nuke.dlm.IPermit;

public class LeakyBucketPermitTest extends PermitTest {

    @Override
    protected IPermit getPermit() {
        return new LeakyBucketPermit(50);
    }

    @Override
    protected long getTaskCostTime() {
        return 0L;
    }

}