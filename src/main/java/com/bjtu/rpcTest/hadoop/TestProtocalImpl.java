package com.bjtu.rpcTest.hadoop;

import org.apache.hadoop.ipc.ProtocolSignature;

import java.io.IOException;

/**
 * Created by lids on 15/12/9.
 */
public class TestProtocalImpl implements TestProtocal {
    public static final int port = 12308;
    public static final int version = 110;

    public String send(String message) {
        System.out.println(message);
        return message;
    }

    @Override
    public long getProtocolVersion(String s, long l) throws IOException {
        return version;
    }

    @Override
    public ProtocolSignature getProtocolSignature(String s, long l, int i) throws IOException {
        return new ProtocolSignature(TestProtocalImpl.version, null);
    }
}
