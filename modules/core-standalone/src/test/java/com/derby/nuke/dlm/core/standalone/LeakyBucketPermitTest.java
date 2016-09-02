package com.derby.nuke.dlm.core.standalone;

import com.derby.nuke.dlm.IPermit;

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