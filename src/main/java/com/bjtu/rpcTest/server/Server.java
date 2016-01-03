package com.bjtu.rpcTest.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by lids on 15/12/4.
 */
public class Server extends UnicastRemoteObject implements RMIQueryStatus {

    public Server() throws RemoteException{
        super ();
    }


    public static final String RMI_URL = "rmi://localhost:12308/query";
    ResultStatus resultStatus = new ResultStatus() {
        public int i = 0;
        public int getI() {
            i ++;
            return i;
        }
    };
    int i = 0;

    @Override
    public int getFileStatus(String filename) throws RemoteException {
        System.out.println(resultStatus.getI());
        i++;
        return i;
//        return resultStatus;
    }
    public static void main(String[] args) throws Exception {

        Server server = new Server();
        LocateRegistry.createRegistry(12308);

        Naming.rebind(RMI_URL, server);
        System.out.println("READY");

    }
}

