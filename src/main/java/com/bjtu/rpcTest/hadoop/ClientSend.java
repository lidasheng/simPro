package com.bjtu.rpcTest.hadoop;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by lids on 15/12/9.
 */
public class ClientSend {

    public static void main(String[] args) {


        InetSocketAddress addr=new InetSocketAddress("localhost", TestProtocalImpl.port);

        TestProtocal query= null;
        try {
            query = RPC.getProxy(TestProtocal.class, TestProtocalImpl.version, addr, new Configuration());

            String status = query.send("test send");
            System.out.println(status);
            RPC.stopProxy(query);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
