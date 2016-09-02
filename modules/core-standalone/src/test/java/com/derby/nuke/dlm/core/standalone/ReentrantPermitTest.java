package com.derby.nuke.dlm.core.standalone;

import com.derby.nuke.dlm.IPermit;

public class ReentrantPermitTest extends PermitTest {

    @Override
    protected IPermit getPermit() {
        return new ReentrantPermit();
    }

    @Override
    protected long getTaskCostTime() {
        return 0L;
    }

}
