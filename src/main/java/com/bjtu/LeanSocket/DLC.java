package com.bjtu.LeanSocket;

import java.net.InetAddress;

/**
 * 格式为：
 * ｜flag,8|地址，8｜控制，8｜分组，？｜CRC,16|FLAG,8|
 * Flag=0(1*6)0
 * CRC校验采用CRC-CCITT, 其生成多项式为D^16+D^12+D^5+1。在具
 体计算CRC校验码时,将地址域和控制域取反后参与CRC计算,将
 求得的余数取反后, 形成CRC(或者说在传输时将CRC取反进行传
 输)。
 */
public class DLC {
    public static final byte ADDRESS_A = (byte)0xFF;
    public static final byte ADDRESS_B = (byte)0xFE;
    public static final byte ADDRESS_C = (byte)0xFD;


    public static final byte CRC = (byte) 0xFF;

    public String getName() {
        if (region == ADDRESS_B) return "B";
        if (region == ADDRESS_A) return "A";
        if (region == ADDRESS_C) return "C";
        return "B";
    }

    public static final byte FLAG = (byte)0x7E;

    //无crc校验

    public InetAddress ipAddress;
    public int port;

    public CMD cmd;
    public byte region;
    public byte[] message;

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public CMD getCmd() {
        return cmd;
    }

    public void setCmd(CMD cmd) {
        this.cmd = cmd;
    }

    public byte getRegion() {
        return region;
    }

    public void setRegion(byte region) {
        this.region = region;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public DLC(byte region, CMD cmd, byte[] message, int port) {
        this.region = region;
        this.cmd = cmd;
        this.message = message;
        this.port = port;
    }
    public DLC(byte region, CMD cmd, byte[] message) {
        this (region, cmd, message, 0);

    }
    public int getPort() {
        return port;
    }

    public DLC(InetAddress ipAddress, int port, byte[] data) {
        this.ipAddress = ipAddress;
        this.port = port;

        this.region = data[1];
        this.cmd = new CMD(data[2]);

        message = new byte[data.length - 7];
        for (int i = 0; i < data.length - 7;i++) {
            message[i] = data[i+3];
        }
        // TODO  this
    }
    public byte[] getOnlyMessage() {
        return message;
    }

    public byte[] getMessage() {
        byte[] re = new byte[message.length + 6];
        re[0] = FLAG;
        re[re.length-1] = FLAG;

        re[re.length-2] = CRC;
        re[re.length-3] = CRC;

        re[1] = region;
        re[2] = cmd.getByte();
        for (int i = 0; i < message.length;i++) {
            re[i + 3] = message[i];
        }
        return re;
    }
    public static void main(String[] args) {
        System.out.println(new String(new DLC(ADDRESS_B, new CMD(ADDRESS_B, 1, 1, true), "test".getBytes()).getOnlyMessage()));
    }
}
