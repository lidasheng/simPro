package com.bjtu.LeanSocket;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by lids on 15/12/30.
 */
public class DLCManager extends Thread {

    public boolean isZHU = true;
    public static final int SENDTIME= 1; //发送时间间隔
    public static final int TIMEOUT = 3; //发送超时时间3s

    public static final int LEN = 7;
    public static final int MOD = 8;

    public Log log;
    int sendport;

    // log * 监听端口个
    public DLCManager(Log log, int port, boolean b) {
        this.log = log;
        this.sendport = port;
        isZHU = b;
        new Reserver(this, port).start();
    }

    public ConcurrentLinkedQueue neadSend = new ConcurrentLinkedQueue();
    public ConcurrentLinkedQueue receiveDlc = new ConcurrentLinkedQueue();

    //seading
    private DLC[] seading = new DLC[MOD];
    private long[] time = new long[MOD];
    //seading
    private DLC[] reciveing = new DLC[MOD];
    int sn = 0;
    int rn = 0;
    int remotesn = 0;

    public void reset() {
        sn = rn = 0;
        reciveing = new DLC[MOD];
        neadSend = new ConcurrentLinkedQueue();
        receiveDlc = new ConcurrentLinkedQueue();
    }
    public void sendNow(DLC dlc) {
        if (Math.random() > 0.15) {
            try {
                new UdpSocket(sendport).send(dlc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void deal(DLC dlc) {
        //如果当前不是主，发给a，否则，发个b or c
        int port = 0;
        if (isZHU == false) port = main.portA;
        else if (dlc.getName().equals("B")) port = main.portB;
        else port = main.portC;

        System.out.println(Integer.toBinaryString(dlc.getCmd().getByte()));

        if ((byte) (dlc.getCmd().getByte() ) == CMD.SNRM) {
            sendNow(new DLC(dlc.getRegion(), new CMD(CMD.UA), "".getBytes(), port));
            log.log(getRLog(dlc));

        } else if ((byte) (dlc.getCmd().getByte()) == CMD.DISC) {
            sendNow(new DLC(dlc.getRegion(), new CMD(CMD.UA), "".getBytes(), port));
            log.log(getRLog(dlc));

        } else if ((byte) (dlc.getCmd().getByte()) == CMD.UA) {
            sendNow(new DLC(dlc.getRegion(), new CMD(CMD.UA), "".getBytes(), port));
            log.log(getRLog(dlc));
            log.log("成功");
            reset();
        } else if((byte)(dlc.getCmd().getByte() & CMD.CL) == CMD.CL) {
            int rnn = (dlc.getCmd().getByte() & 0x07);
            rn = rnn;

        } else {
            System.out.println("shoudao erjinzhi " + Integer.toBinaryString(dlc.getCmd().getByte()));
            int snn = (dlc.getCmd().getByte() & 0x70) >>> 4;
            int rnn = (dlc.getCmd().getByte() & 0x07);

            System.out.println("res" + snn + " " + rnn + " " + dlc.port);
            reciveing[snn] = dlc;

            rn = rnn;

            while (reciveing[remotesn] != null) {
                System.out.println(sn + " " + rn + " " + snn + " " + rnn);

                log.log( getRLog(reciveing[remotesn]));
                reciveing[remotesn] = null;
                remotesn = (remotesn + 1) % MOD;
            }
            dlc = new DLC(dlc.getRegion(), new CMD(CMD.CL), "收到数据确认".getBytes(), port);
            dlc.setCmd(new CMD((byte) (dlc.getCmd().getByte() | remotesn)));
            sendNow(dlc);
            log.log("确认收到桢" + dlc.getName() + "(" + sn + " "  + remotesn +  ")P");
        }
    }

    public String getRLog(DLC dlc) {
        if ((byte)(dlc.getCmd().getByte()) == CMD.SNRM) {
            return "收到 " + dlc.getName() + "(SNRM    )P";
        }
        if ((byte)(dlc.getCmd().getByte()) == CMD.DISC) {
            return "收到 " +dlc.getName() + "(DISC    )P";
        }
        if ((byte)(dlc.getCmd().getByte()) == CMD.UA) {
            return "收到 " +dlc.getName() + "(UA    )P";
        }

        String p = "P";
        if ((byte)(dlc.getCmd().getByte() | CMD.P) != 0x00)
            p = "F";
        int sn = (dlc.getCmd().getByte()& 0x70)>>>4;
        int rn = (dlc.getCmd().getByte()& 0x07);
        System.out.println(dlc.getOnlyMessage().length + "lenlenlen");
        return "收到 " + dlc.getName() + "(" + sn + " "+ rn  + ")" + p + " message = " + new String(dlc.getOnlyMessage());
    }
    public String getSLog(DLC dlc, int sn, int rn) {
        if ((byte)(dlc.getCmd().getByte()) == CMD.SNRM) {
            return dlc.getName() + "(SNRM    )P";
        }
        if ((byte)(dlc.getCmd().getByte()) == CMD.DISC) {
            return dlc.getName() + "(DISC    )P";
        }
        if ((byte)(dlc.getCmd().getByte()) == CMD.UA) {
            return dlc.getName() + "(UA    )P";
        }

        String p = "P";
        if ((byte)(dlc.getCmd().getByte() | CMD.P) != 0x00)
            p = "F";

        return dlc.getName() + "(" + sn + " "+ rn  + ")" + p;
    }
    @ Override
    public void run() {
        while (true) {

//            System.out.println(" " + sn + " " + rn);

            while (receiveDlc.size() > 0) {
                DLC dlc = (DLC) receiveDlc.poll();
                deal(dlc);
                System.out.println("shoudao");
            }
            if (sn != rn) {
                if (System.currentTimeMillis() - time[rn] > TIMEOUT * 1000) {
                    DLC dlc = seading[rn];

                    if (seading[rn] == null) {
//                        System.out.println("retry null");
                    } else {
                        System.out.println("retru snrn " + sn + " " + rn);
                        log.log("重新发送 " + getSLog(dlc, sn, remotesn));

                        System.out.println(dlc.port);
                        try {
                            new UdpSocket(sendport).send(dlc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //sn = (sn + 1) % MOD;
                }
            }

            if (neadSend.size() != 0 && (sn - rn) % MOD < LEN) {

                DLC dlc = (DLC) neadSend.poll();
                System.out.println("len" + neadSend.size());
                //System.out.println(dlc.port);

                if ((byte) (dlc.getCmd().getByte()) == CMD.UA) {
                    //neadSend.add(new DLC(dlc.getRegion(), new CMD(CMD.UA), "".getBytes(), port));
                    try {
                        new UdpSocket(sendport).send(dlc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    reset();
                    log.log("发送 " + getSLog(dlc, sn, remotesn));
                    log.log("成功");
                    continue;
                }
                boolean isData = false;
                if (dlc.getCmd().getByte() == CMD.RR) isData = true;
                if (isData) {
//                    System.out.println("erjinzhi" +Integer.toBinaryString(dlc.getCmd().getByte()));
                    dlc.setCmd(new CMD((byte) (dlc.getCmd().getByte() | ((byte) sn << 4) | remotesn)));
                }
//                System.out.println("erjinzhi" + Integer.toBinaryString(dlc.getCmd().getByte()));

                log.log("发送 " + getSLog(dlc, sn, remotesn));

                if (Math.random() > 0.15) {
                    try {
                        new UdpSocket(sendport).send(dlc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                seading[sn] = dlc;
                time[sn] = System.currentTimeMillis();
                sn = (sn + 1) % MOD;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ConcurrentLinkedQueue getNeadSend() {
        return neadSend;
    }

    public void setNeadSend(ConcurrentLinkedQueue neadSend) {
        this.neadSend = neadSend;
    }

    public ConcurrentLinkedQueue getReceiveDlc() {
        return receiveDlc;
    }

    public void setReceiveDlc(ConcurrentLinkedQueue receiveDlc) {
        this.receiveDlc = receiveDlc;
    }

    // test
    public static void main(String[] args) {
        DLCManager d1 = new DLCManager(new Log() {
            @Override
            public void log(String s) {
//                System.out.println("test_ log " + s);
            }
        }, main.portA, true);
        for (int i = 0;i < 10;i++) {
            d1.getNeadSend().add(new DLC(DLC.ADDRESS_B, new CMD((byte)0XFF), ("TEST d1" +i ) .getBytes(), main.portB));
            //d2.getNeadSend().add(new DLC(DLC.ADDRESS_A, new CMD((byte)0XFF), ("TEST d2" + i).getBytes(), 38438));
        }
        d1.start();


        DLCManager d2 = new DLCManager(new Log() {
            @Override
            public void log(String s) {
//                System.out.println("test_ log " + s);
            }
        }, main.portB, false);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0;i < 10;i++) {
            //d1.getNeadSend().add(new DLC(DLC.ADDRESS_A, new CMD((byte)0XFF), ("TEST d1" +i ) .getBytes(), 38439));
            d2.getNeadSend().add(new DLC(DLC.ADDRESS_A, new CMD((byte)0XFF), ("TEST d2" + i).getBytes(), main.portA));
        }
        d2.start();

        System.out.println("start");
    }
}
