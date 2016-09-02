package com.derbysoft.nuke.dlm.core.standalone;

import com.derbysoft.nuke.dlm.IPermit;

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
