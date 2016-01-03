package com.bjtu.rpcTest.client;

import com.bjtu.rpcTest.server.RMIQueryStatus;
import com.bjtu.rpcTest.server.Server;

import java.rmi.Naming;

/**
 * Created by lids on 15/12/4.
 */
public class Client {

    public static void main(String[] argv) throws Exception {
        for (int i = 0;i < 10;i++) {
            RMIQueryStatus rmiQueryStatus = (RMIQueryStatus) Naming.lookup(Server.RMI_URL);
            int value = rmiQueryStatus.getFileStatus("s");
            System.out.println(value);
        }

    }
}
