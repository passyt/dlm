package com.derby.nuke.dlm.core.standalone;

import com.derby.nuke.dlm.IPermit;

public class SemaphorePermitTest extends PermitTest {

    @Override
    protected IPermit getPermit() {
        return new SemaphorePermit(50);
    }

}
