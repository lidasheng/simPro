package com.bjtu.LeanSocket;

/**
 * Created by lids on 15/12/30.
 */
public class CMD {

    private byte message;
    public static final byte P =  (byte)0x08;


    public static final byte RR = (byte)0x00;
    public static final byte RNR = (byte)0x01;
    public static final byte SNRM = (byte)0xC0| 0x03;
    public static final byte UA = (byte)0xC0| 0x01;
    public static final byte DISC = (byte)0xC0| 0x02;


    public static final byte NULL = (byte)0xC0;//无信息桢
    public static final byte MS = (byte)0x00;// message
    public static final byte CL = (byte)0x80; // 控制
    /**
     *
     * @param type 信息桢位：0x00 监控桢位：0x80 无编号桢位：0xC0
     * @param sn
     * @param rn
     * @param p
     */
    public CMD(byte type, int sn, int rn, boolean p) {
        if (p) {
            this.message = (byte) (type | (sn | rn));
        } else {
            this.message = (byte) (type | (sn | rn) | P);
        }
    }



    public CMD(byte b) {
        this.message = b;
    }
    public byte getByte() {
        return message;
    }
}
