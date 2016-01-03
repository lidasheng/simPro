package com.bjtu;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class test {

    //歪度系数
    public double wd(double[] a) {
        return u3(a) / Math.pow(dx(a), 3.0/2);
    }

    // 自相关系数
    public double zixiangguanxishu(double[] a) {
        double agv = agv(a);
        double[] b = new double[a.length];
        for (int i = 0;i < a.length;i++) b[i] = a[i] * a[i];
        return agv(b)/agv;
    }

    //三阶中心距
    public double u3 (double[] a) {
        double re = 0;
        double agv = agv(a);
        for (double i : a) {
            re += Math.pow(i - agv, 3);
        }
        return re;
    }
    //方差系数  方差／平均数的平房
    public double c3(double[] a) {
        return dx(a) / (agv(a) * agv(a));
    }
    //方差
    public double dx(double[] a) {
        double agv = agv(a);
        double re = 0;
        for (double i : a) {
            re += (agv - i) * (agv - i);
        }
        return re / a.length;
    }
    //平均数 所有数求和／n
    public double agv(double[] a) {
        double re = 0;
        for (double i : a) {
            re += i;
        }
        return re / a.length;
    }


    public static void main(String[] argv) throws IOException {
        System.out.println(System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis());
        List<Double> list = new ArrayList<Double>();
        BufferedReader br = new BufferedReader(new FileReader(new File("/Users/lids/Downloads/000001.csv")));
        String line;
        while ((line = br.readLine()) != null ) {
            String[] data = line.trim().split(";");
            try {
                double value = Double.parseDouble(data[3]);
                list.add(value);

            } catch (Exception e) {
                System.out.println("wrong value =" + data[3]);
            }
        }
        double[] a = new double[list.size()];
        int i = 0;
        for (double v : list) {
            a[i] = v;
            i++;
        }

        test t = new test();
        System.out.println("平均数为：" + t.agv(a));
        System.out.println("方差为：" + t.dx(a));
        System.out.println("方差系数为：" + t.c3(a));
        System.out.println("自相关系数: " + t.zixiangguanxishu(a));
        System.out.println("歪度系数为： " + t.wd(a));

    }
}
