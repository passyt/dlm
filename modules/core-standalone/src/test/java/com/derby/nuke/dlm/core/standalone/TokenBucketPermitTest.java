package com.derby.nuke.dlm.core.standalone;

import com.derby.nuke.dlm.IPermit;

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
