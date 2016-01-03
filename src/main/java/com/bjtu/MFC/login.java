package com.bjtu.MFC;
import com.bjtu.LeanSocket.Log;
import com.bjtu.LeanSocket.main;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class login extends JFrame implements ActionListener, Log {             //Jframe   actionlistener
	private main m ;


	JPanel p[]=new JPanel[8];//左侧八行 分为编辑框和按钮 
    JTextField text1=new JTextField("从站名称",15);                                              //two textfields
    JButton button1=new JButton("确定");
    
    JTextField text2=new JTextField("从站名称",15);                                              //two textfields
    JButton button2=new JButton("确定");
    
    JTextField text31=new JTextField("输入数量",10);
    JTextField text32=new JTextField("从站名称",10);
    JTextField text33=new JTextField("数据内容",20);
    JButton button3=new JButton("确定");
    
	JTextArea jta=new JTextArea(25,30);//右侧文本区大小
	JPanel q[]=new JPanel[2];
	JTextField textr1=new JTextField(20);
	
	public login(int port)
	{
		m = new main(port, this);


//		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
	
		JPanel left=new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel right=new JPanel(new FlowLayout(FlowLayout.CENTER));
				
		Container con= getContentPane();
		//con.add(new JPanel(new FlowLayout(FlowLayout.CENTER)));    // set position
		con.setLayout(new GridLayout(1,2));                         //set layout

		left.setLayout(new GridLayout(8,1));
		//right.setLayout(new GridLayout(2,1));

		for(int i=0;i<8;i++)
			p[i]=new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		for(int i=0;i<2;i++)
			q[i]=new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		//左边各部分
		p[0].add(new JLabel("建立连接"));
		p[1].add(text1);
		p[1].add(button1);
		///////////////////////////////////////
		p[2].add(new JLabel("拆除连接"));
		p[3].add(text2);
		p[3].add(button2);		
		
		///////////////////////////////
		p[4].add(new JLabel("发送数据"));
		p[5].add(text31);
		p[5].add(text32);
		p[6].add(text33);
		p[7].add(button3);
		/////////////////////////////
		//右侧部分
		q[0].add(new JLabel("命令显示窗口"));
		q[1].add(jta);
		/////////////////////////////
		for(int i=0;i<8;i++)
			left.add(p[i]);
		right.add(q[0],"North");
		right.add(q[1],"Center");
		//right.add(jta);
		con.add(left);
		con.add(right);

		///////////////////////////
		button1.addActionListener(this);           //add actionlistener
		button2.addActionListener(this);
		button3.addActionListener(this);

		
		
		this.setTitle("主站A");                  //set jframe info
		this.setSize(900, 500);
		this.setVisible(true);
		

	
	}


//	JTextField text1=new JTextField("从站名称",15);                                              //two textfields
//	JButton button1=new JButton("确定");
//
//	JTextField text2=new JTextField("从站名称",15);                                              //two textfields
//	JButton button2=new JButton("确定");
//
//	JTextField text31=new JTextField("输入数量",10);
//	JTextField text32=new JTextField("从站名称",10);
//	JTextField text33=new JTextField("数据内容",20);
//
	@Override
	public void actionPerformed(ActionEvent e)          //add button info
	{
		if(e.getSource()==button1)
		{//调用连接方法,显示连接成功

			String mes = text1.getText();
			if (mes.equals("A") || mes.equals("B") || mes.equals("C")) {
				m.createLink(mes);
			} else {
				jta.append( "链接机器只能是：A,B,C\n");
				this.setVisible(true);
			}
		}
		if(e.getSource()==button2){
			String mes = text2.getText();
			if (mes.equals("A") || mes.equals("B") || mes.equals("C")) {
				//断开连接  显示断开连接
				m.chai(mes);
			} else {
				jta.append( "断开机器只能是：A,B,C\n");
				this.setVisible(true);
			}

		}
		if(e.getSource() == button3){
			//发送数据 显示数据
			//	JTextField text31=new JTextField("输入数量",10);
//	JTextField text32=new JTextField("从站名称",10);
//	JTextField text33=new JTextField("数据内容",20);
			try {

				int num = Integer.parseInt(text31.getText());
				String mes = text32.getText();
				String message = text33.getText();
				if (mes.equals("A") || mes.equals("B") || mes.equals("C")) {
					if (message == null) {
						jta.append( "发送内容不能为空\n");
						this.setVisible(true);
					} else {
						for (int i = 0; i < num; i++) {
							m.seadMessage(new byte[][]{message.getBytes()}, mes);
						}
					}
				} else {
					jta.append( "断开机器只能是：A,B,C\n");
					this.setVisible(true);
				}

			} catch (Exception ee) {
				jta.append( ee.getMessage() + "\n");
				this.setVisible(true);
			}

		}
	}


	@Override
	public void log(String s) {
		jta.append(s + "\n");
		this.setVisible(true);
	}
}