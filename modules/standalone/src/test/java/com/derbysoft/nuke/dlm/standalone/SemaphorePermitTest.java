package com.derbysoft.nuke.dlm.standalone;

import com.derbysoft.nuke.dlm.IPermit;

public class SemaphorePermitTest extends PermitTest {

    @Override
    protected IPermit getPermit() {
        return new SemaphorePermit(50);
    }

}
