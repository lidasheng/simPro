package com.bjtu.LeanSocket;

/**
 * Created by lids on 15/12/28.
 */
public class Sender {


    public void send(Byte[] messages) {

    }
    public static void main(String[] args) {
        for (int i = 0; i < 10;i++ ){
            try {
                System.out.println(i);
                new UdpSocket(38438).send(new DLC(DLC.ADDRESS_A, new CMD((byte)0xFF), ("test" + i).getBytes(), 38433));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
