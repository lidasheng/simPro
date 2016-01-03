package com.bjtu.LeanSocket;

/**
 * Created by lids on 15/12/27.
 */
public class main {

    public static final int portA = 38438;
    public static final int portB = 38439;
    public static final int portC = 38440;

    public DLCManager dlcManager;
    public main(int port, Log log) {
        boolean iszhu = false;
        if (portA == port) iszhu = true;
        dlcManager = new DLCManager(log, port, iszhu);
        dlcManager.start();
    }

    public void createLink(String a) {
        int port = 0;
        byte region = 0x00;
        if (a.equals("A")) {port = portA; region = DLC.ADDRESS_A;}
        else if (a.equals("B")) {port = portB;region = DLC.ADDRESS_B;}
        else {port = portC;region = DLC.ADDRESS_C;}

//        System.out.println(port + " " + region);
        dlcManager.getNeadSend().add(new DLC(region, new CMD(CMD.SNRM), "建立链接".getBytes(), port));
    }

    public void seadMessage(byte[][] message, String a) {
        int port = 0;
        byte region = 0x00;
        if (a.equals("A")) {port = portA; region = DLC.ADDRESS_A;}
        else if (a.equals("B")) {port = portB;region = DLC.ADDRESS_B;}
        else {port = portC;region = DLC.ADDRESS_C;}

//        System.out.println(port + " " + region);
        for (int i = 0;i < message.length;i++) {
            dlcManager.getNeadSend().add(new DLC(region, new CMD(CMD.RR), message[i], port));
        }
    }

    public void chai(String a) {
        int port = 0;
        byte region = 0x00;
        if (a.equals("A")) {port = portA; region = DLC.ADDRESS_A;}
        else if (a.equals("B")) {port = portB;region = DLC.ADDRESS_B;}
        else {port = portC;region = DLC.ADDRESS_C;}

//        System.out.println(port + " " + region);
        dlcManager.getNeadSend().add(new DLC(region, new CMD(CMD.DISC), "拆除链接".getBytes(), port));
    }

    public static void main(String[] args) throws Exception{
        Log log = new Log() {
            @Override
            public void log(String s) {
                System.out.println("========" + s);
            }
        };
        main a = new main(main.portA, log);
        main b = new main(main.portB, log);
        main c = new main(main.portC, log);
        //a.createLink("A");
        Thread.sleep(4000);
        //a.createLink("B");
        for (int i = 0; i < 10;i++) {
            a.seadMessage(new byte[][]{"a".getBytes()}, "B");
        }

        a.chai("B");
    }

}
