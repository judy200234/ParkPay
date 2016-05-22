import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class EncryptorGUICheckIn extends Thread implements ActionListener{
	JFrame frame;
	static JLabel label1;
	JLabel label2;
	JLabel label3;
	JLabel label4;
	JLabel label5;
	JLabel label6;
	JLabel label7;
	JTextArea TextArea1;
	TextField input;
	
	int NumberOfTime;
	int Credit;
	int whichmachine;

	static Calendar calendar = new GregorianCalendar();
	
	public void runMain(){

		
		frame = new JFrame("Parking Check In Screen");//視窗名稱
		frame.addWindowListener(new AdapterDemo());
		frame.setLayout(new FlowLayout());
		frame.setSize(900,600);
		frame.setBackground(Color.blue);

		
		//新增panel容器和label.在lable設定文字
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		label1 = new JLabel("",SwingConstants.CENTER);
		label2 = new JLabel("Enter Number" ,SwingConstants.LEFT);
		label3 = new JLabel("Parking Fee" ,SwingConstants.LEFT);
		label4 = new JLabel("Credit" ,SwingConstants.LEFT);
		label5 = new JLabel("$"+ "0" ,SwingConstants.CENTER);
		label6 = new JLabel("$"+ Credit ,SwingConstants.CENTER);
		label7 = new JLabel("Rule: 30 sec = $1 NTD, under 30 sec for free." ,SwingConstants.CENTER);
		TextArea1 = new JTextArea(4,20);
		
		input = new TextField(40);
		input.setFont(new Font("Serif",Font.BOLD,20));
		//設定label大小
		
		label1.setPreferredSize(new Dimension(800,100));
		label1.setFont(new Font("Serif",Font.BOLD,80));
		label2.setPreferredSize(new Dimension(200,30));
		label2.setFont(new Font("Serif",Font.BOLD,30));
		label3.setPreferredSize(new Dimension(200,30));
		label3.setFont(new Font("Serif",Font.BOLD,30));
		label4.setPreferredSize(new Dimension(200,30));
		label4.setFont(new Font("Serif",Font.BOLD,30));
		label5.setPreferredSize(new Dimension(425,30));
		label5.setFont(new Font("Serif",Font.BOLD,30));
		label6.setPreferredSize(new Dimension(425,30));
		label6.setFont(new Font("Serif",Font.BOLD,30));
		label7.setPreferredSize(new Dimension(600,30));
		label7.setFont(new Font("Serif",Font.BOLD,30));
		TextArea1.setFont(new Font("Serif",Font.BOLD,30));
		TextArea1.setBackground(SystemColor.control);
		
		//設定panel顏色
		panel1.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.blue));
		panel5.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.blue));		

		//設定label邊框顏色
		label5.setBorder(BorderFactory.createLineBorder(Color.blue));
		label6.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		input.addActionListener(this);
		
		panel1.add(label1);
		panel2.add(label2);
		panel2.add(input);
		panel3.add(label3);
		panel3.add(label5);
		panel4.add(label4);
		panel4.add(label6);
		panel5.add(TextArea1);
		panel6.add(label7);
		
		frame.add(panel1);
		frame.add(panel2);
		frame.add(panel3);
		frame.add(panel4);
		frame.add(panel5);
		frame.add(panel6);
		
		frame.setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent k) {
      
	  System.out.println("input: " + input.getText());
		
	  String val;
	  
	  val = input.getText();
	  
	  jdbc123CheckIn jdbc = new jdbc123CheckIn();
	  
	  jdbc.jdbc(val);
	  
	  Credit = jdbc.getCredit();
	  whichmachine = jdbc.getWhichMachine();
	  
	  label6.setText("$" + Credit);
	  if(whichmachine==1){
		  TextArea1.setText("Start: " + jdbc.getsGMT());
	  }
	  else{
		  TextArea1.setText("Error! Double Check In");
	  }
	  reset();
	}
	
	private void setTime()  //設定系統時間
    {
        calendar.setTimeInMillis( System.currentTimeMillis() );
    }
    
    public int getYear()  //取得年
    {
        return calendar.get(Calendar.YEAR);
    }
    
    public int getMonth()  //取得月
    {
        return (calendar.get(Calendar.MONTH)+1);
    }
    
    public int getDate()  //取得日
    {
        return calendar.get(Calendar.DATE);
    }
    
    public int getHour()  //取得小時
    {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    
    public int getMinute()  //取得分鐘
    {
		if(calendar.get(Calendar.MINUTE)<10){
			return calendar.get(Calendar.MINUTE);
		}
		else
			return calendar.get(Calendar.MINUTE);
    }
        
    public int getSecond()  //取得秒數
    {
		if(calendar.get(Calendar.SECOND)<10){
			return calendar.get(Calendar.SECOND);
		}
		else
			return calendar.get(Calendar.SECOND);
    }
    
    public void run()  //顯示目前時間
    {
        while(true)
        {
			try{
				setTime();
				label1.setText(""+getYear()+"/"+getMonth()+"/"+getDate()+"    "+
				               getHour()+":"+getMinute()+":"+getSecond());
			}catch(Exception q){
				q.printStackTrace();
			}finally{
				try{
					Thread.sleep(1*1000);
				}catch(Exception q){
				}
			}
		}
	}
	
	
	public void reset()
	{
				input.setText("");
	}
}

class AdapterDemo extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}