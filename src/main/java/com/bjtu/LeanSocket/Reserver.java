package com.bjtu.LeanSocket;

import java.net.DatagramSocket;

/**
 * Created by lids on 15/12/28.
 */
public class Reserver extends Thread{

    DLCManager dlcManager;
    int port;

    public Reserver(int port) {
        this.port = port;
    }
    public Reserver(DLCManager dlcManager, int port) {
        this.dlcManager = dlcManager;
        this.port = port;
    }
    @Override
    public void run()  {
        try {
//            System.out.println(port);
            UdpSocket  udpSocket = new UdpSocket(port, new DatagramSocket(port));

            while (true) {
                DLC dlc = udpSocket.receive();
                dlcManager.getReceiveDlc().add(dlc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread thread = new Reserver(38438);
        thread.start();
    }
}
