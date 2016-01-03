package com.bjtu;


import java.util.ArrayList;
import java.util.Random;

import com.admaster.data.metrics.core.AdMonitor;
import com.admaster.data.metrics.core.Scene;
import com.codahale.metrics.Timer;

public class TestScene {
//你有一个需要度量的方法，他运行会消耗一定时间，并且返回一个值

    public static int myFunction() {

        try { Thread.sleep(50); } catch (InterruptedException e) {}

        return new Random().nextInt();

    }

    public static void main(String[] args) {

        Scene scene = AdMonitor.getScene("3"); //获取应用场景实例，参数18是邮件中所得App ID。
        AdMonitor.setHostName("myInstName");//用户可以自定义hostname, 如果不设置则取本机ip

        ArrayList<String> myQueue = new ArrayList<String>();

        myQueue.add("ele");//你需要度量一个队列，他有一定的元素，你很关注他有多大
        scene.gauge("XSystem.queue", myQueue);//这行代码可以监听队列大小，它产生的指标名字是“XSystem.queue”

        while(true) {

            Timer.Context c = scene.timer("XSystem.qps");//度量时间

            System.out.println("start");
            int result = myFunction();

            c.stop();//注意度量时间要有结尾

            //System.out.println(scene.SceneFormatMetric());

            scene.meter("XSystem.qps");//度量速率

            scene.histogram("XSystem.qps",result);//度量数值分布

        }
    }
}