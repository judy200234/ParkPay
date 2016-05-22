import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class jdbc123CheckOut{
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://parkingfee.noip.me:3306/idtime";
   //  Database credentials
   static final String USER = "parkingfee";
   static final String PASS = "0000";
   
   int NumberOfTime = 0;
   int Credit = 0;
   int ParkingFee;
   int whichmachine = 2;
   int checkwhichmachine;
   int checkdiscount;
   
   long carparkingtime;
   long hour;
   long min;
   long sec;
   long showsec;
   long discountTime;
   long showdiscountTime;
   
   String starttime;
   String endtime;
   String sGMT;
   String discountDate;
   String discountNowdate;
   String discountTimeReal;
   String discountCutDateString;
   
   public void jdbc(String val) {
	  
	  Connection conn = null;
	  Statement stmt = null;
	  ResultSet rs = null;
	  ResultSet querynum = null;
	  ResultSet counttime = null;
	  ResultSet getdiscount = null;
	  ResultSet whichmachinenum = null;
	  PreparedStatement checkinstmt;
	  PreparedStatement qstmt;
	  PreparedStatement parkingstmt;
	  PreparedStatement discountstmt;
	  PreparedStatement pstmt;
	  PreparedStatement cstmt;
	  PreparedStatement nstmt;
	  PreparedStatement ct;
	  PreparedStatement checkdiscountstmt;
	  
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);


      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();	  
	  
	  
	  //STEP 5: Extract data from result set
	  
	  //導出DB裡的卡片次數
	  qstmt = conn.prepareStatement("SELECT number_of_time FROM `administrator` WHERE User=?");
	  qstmt.setString(1, val);
	  querynum = qstmt.executeQuery();
	  querynum.next();
	  NumberOfTime = querynum.getInt("number_of_time");
	  System.out.println(NumberOfTime);
	  this.NumberOfTime = NumberOfTime;
	  
	  checkinstmt = conn.prepareStatement("SELECT whichmachine FROM `timetable` WHERE User = ? ORDER BY timein DESC limit 1");
	  checkinstmt.setString(1, val);
	  whichmachinenum = checkinstmt.executeQuery();
	  whichmachinenum.next();
	  checkwhichmachine = whichmachinenum.getInt("whichmachine");
	  
	  if((NumberOfTime%2)!=0){
		  if(checkwhichmachine==1){
			  //登記出場時間回傳至mysql
    		  java.util.Date now = new java.util.Date(); //取得現在時間
    		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		  sGMT = sf.format(now);
    		  java.sql.Timestamp createDate = Timestamp.valueOf(sGMT); // 要轉換成 java.sql.Date 的物件才可以寫入資料庫
    		  System.out.println(sGMT);
    		  pstmt = conn.prepareStatement("UPDATE timetable SET timeout = ? , whichmachine = ? WHERE user=? ORDER BY timein DESC limit 1");
    		  pstmt.setTimestamp(1, createDate);	
			  pstmt.setInt(2, whichmachine);
    		  pstmt.setString(3, val);
    		  pstmt.execute();
			  
			  this.whichmachine = whichmachine;
			  
    		  //計算停車時數
    		  ct = conn.prepareStatement("SELECT timein, timeout FROM `timetable` WHERE user=? ORDER BY timein DESC limit 1");
    		  ct.setString(1,val);
   		  
    		  counttime = ct.executeQuery();
    		  counttime.next();
		  
    		  java.sql.Timestamp beforetime = counttime.getTimestamp("timein");
    		  java.sql.Timestamp nowtime = counttime.getTimestamp("timeout");
    		  starttime = sf.format(beforetime);
    		  endtime = sf.format(nowtime);
		  		  
    		  carparkingtime = nowtime.getTime() - beforetime.getTime();
    		  hour = carparkingtime/(3600*1000);
    		  min = (carparkingtime-hour*3600*1000)/(60*1000);
    		  sec = (carparkingtime-hour*3600*1000-min*60*1000)/1000;
    		  showsec = carparkingtime/1000;

    		  this.showsec = showsec;
    		  this.starttime = starttime;
    		  this.endtime = endtime;
		  
    		  //讀取購物discountTime
    		  discountstmt = conn.prepareStatement("SELECT confirmtime,discountTime,checkdiscount FROM timetable WHERE user= ? ORDER BY timein DESC limit 1");
    		  discountstmt.setString(1,val);
    		  getdiscount = discountstmt.executeQuery();
    		  getdiscount.next();
			  
			  
    		  java.sql.Timestamp discountDatesql = getdiscount.getTimestamp("confirmtime");
    		  java.sql.Timestamp discountTimesql = getdiscount.getTimestamp("discountTime");
			  checkdiscount = getdiscount.getInt("checkdiscount");
			  System.out.println(discountTimesql);
			  
			  if(discountDatesql!=null){
				  if(checkdiscount==0){
					  SimpleDateFormat discountTimesf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            		  discountCutDateString = discountTimesf.format(now);
            		  Date discountCutDate = discountTimesf.parse(discountCutDateString);
		  
            		  SimpleDateFormat discountDatesf = new SimpleDateFormat("yyyy-MM-dd");
            		  discountDate = discountDatesf.format(discountDatesql);
            		  discountNowdate = discountDatesf.format(now);
				  
            		  discountTime = discountTimesql.getTime()-discountCutDate.getTime();
    				  if(discountTime>0){
    					  if(discountDate.compareTo(discountNowdate)==0){
    						  carparkingtime = carparkingtime-discountTime;
    						  if(carparkingtime<0){
    							  carparkingtime = 0;
    							  showdiscountTime = discountTime/(60*1000);
    							  this.showdiscountTime = showdiscountTime;
							  }
							  else{
    							  showdiscountTime = discountTime/(60*1000);
    							  this.showdiscountTime = showdiscountTime;
							  }
						  }
					  }
					  else{
						  showdiscountTime = 0;
						  this.showdiscountTime = showdiscountTime;
					  }
					  checkdiscount = 1;
					  checkdiscountstmt = conn.prepareStatement("UPDATE timetable SET checkdiscount = ? WHERE User = ? ORDER BY timein DESC LIMIT 1");
					  checkdiscountstmt.setInt(1,checkdiscount);
					  checkdiscountstmt.setString(2, val);
				  }
			  }
			  else{
				  showdiscountTime = 0;
				  this.showdiscountTime = showdiscountTime;
			  }
    		  //計算停車價錢
    		  ParkingFee = 1*((int)carparkingtime/(30*1000));
    		  this.ParkingFee = ParkingFee;
		  
    		  //存入停車價錢至mysql
    		  parkingstmt = conn.prepareStatement("UPDATE timetable SET Parkingfee = ? WHERE User = ? AND timein =?");
    		  parkingstmt.setInt(1,ParkingFee);
    		  parkingstmt.setString(2, val);
    		  parkingstmt.setTimestamp(3, beforetime);
    		  parkingstmt.execute();
		  
    		  //讀出餘額並扣款	  
    		  pstmt = conn.prepareStatement("SELECT credit FROM administrator where User = ?");
    		  pstmt.setString(1, val);
    		  rs = pstmt.executeQuery();
    		  rs.next();
    		  Credit = rs.getInt("credit");
    		  Credit = Credit - ParkingFee;
    		  System.out.println("Credit:"+Credit);
    		  this.Credit = Credit;
		  
	    	  //回傳扣款後餘額至mysql
    		  cstmt = conn.prepareStatement("UPDATE administrator SET credit = ? WHERE User = ?");
    		  cstmt.setInt(1,Credit);
    		  cstmt.setString(2, val);
    		  cstmt.execute();
		  
    		  //增加停車次數並回傳至mysql
    		  NumberOfTime++;
    		  nstmt = conn.prepareStatement("UPDATE administrator SET number_of_time = ? WHERE User = ?");
    		  nstmt.setInt(1,NumberOfTime);
    		  nstmt.setString(2, val);
    		  nstmt.execute();
		  }
		  rs.close();
	  }
	  else{
	  
		  whichmachine = 0;
		  this.whichmachine = whichmachine;
		  System.out.println("Error! Double Check Out");
	  }
	  //STEP 6: Clean-up environment
	  querynum.close();
	  stmt.close();
	  conn.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
   }
   
   public int getParkingFee(){
	   return ParkingFee;
   }
   
   public int getCredit(){
	   return Credit;
   }
   
   public int getWhichMachine(){
	   return whichmachine;
   }
   
   public long getShowsec(){
	   return showsec;
   }
   
   public long getShowDiscountTime(){
	   return showdiscountTime;
   }
   
   public String getstarttime(){
	   return starttime;
   }
   
   public String getendtime(){
	   return endtime;
   }
}