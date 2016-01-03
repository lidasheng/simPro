package com.bjtu.rpcTest.hadoop;

import org.apache.hadoop.ipc.VersionedProtocol;

/**
 * Created by lids on 15/12/9.
 */
public interface TestProtocal extends VersionedProtocol {
    public String send(String message);
}
