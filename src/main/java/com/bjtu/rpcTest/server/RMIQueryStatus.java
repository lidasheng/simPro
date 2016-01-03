package com.bjtu.rpcTest.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by lids on 15/12/4.
 */
public interface RMIQueryStatus extends Remote {
        int getFileStatus (String filename) throws RemoteException;
}
