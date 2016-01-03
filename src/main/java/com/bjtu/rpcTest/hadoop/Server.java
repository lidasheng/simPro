package com.bjtu.rpcTest.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

/**
 * Created by lids on 15/12/9.
 */
public class Server {

    public static void main(String[] args) {
        try {
            RPC.Server service = new RPC.Builder(new Configuration())
                    .setProtocol(TestProtocal.class)
                    .setInstance(new TestProtocalImpl())
                    //.setBindAddress("rmi://localhost:12308/query").setPort(TestProtocalImpl.port)
                    .setBindAddress("localhost").setPort(TestProtocalImpl.port)
                    .setnumReaders(1).setVerbose(true).build();

            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
