package com.derbysoft.nuke.dlm.core.standalone;

import com.derbysoft.nuke.dlm.IPermit;

public class TokenBucketPermitTest extends PermitTest {

    @Override
    protected IPermit getPermit() {
        return new TokenBucketPermit(20);
    }

    @Override
    protected long getTaskCostTime() {
        return 0L;
    }

}
