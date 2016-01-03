package com.bjtu.LeanSocket;

import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lids on 15/12/30.
 */
public class UdpSocket {

    public DatagramSocket resciveSocket;
    public InetAddress address;
    public int port;

    public static Map<Integer, DatagramSocket> socketCache = new HashMap<Integer, DatagramSocket>();

    public UdpSocket(int port, DatagramSocket datagramSocket)  {
        this.port = port;
        this.resciveSocket = datagramSocket;
        init();
    }
    public UdpSocket(int port) {
        this.port = port;

        init();
    }

    private void init() {
        try {
            if (address == null) {
                address = InetAddress.getByName("localhost");
            }

            //socket = new DatagramSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public synchronized DLC receive( ) throws Exception {

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        resciveSocket.receive(receivePacket);

        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();
        DLC dlc = new DLC(IPAddress, port, receivePacket.getData());
        return dlc;
    }

    public synchronized void send(DLC dlc) throws Exception {
        if (socketCache == null)
            socketCache = new HashMap<Integer, DatagramSocket>();

        if (socketCache.containsKey(dlc.port) == false)
            socketCache.put(dlc.port, new DatagramSocket());

        //System.out.println(dlc.port + " " + socketCache.size());
        DatagramSocket sendSocket = socketCache.get(dlc.port) ;
        //DatagramSocket sendSocket = new DatagramSocket();

        byte[] sendData = dlc.getMessage();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, dlc.getPort());
        sendSocket.send(sendPacket);
//        sendSocket.close();
//        socketCache.remove(port);
        //sendSocket.close();
        Thread.sleep(DLCManager.SENDTIME*1000);
    }
}
