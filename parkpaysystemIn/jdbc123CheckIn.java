import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class jdbc123CheckIn{
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://parkingfee.noip.me:3306/idtime";
   //  Database credentials
   static final String USER = "parkingfee";
   static final String PASS = "0000";
   
   int NumberOfTime = 0;
   int Credit = 0;
   int whichmachine = 1;
   
   String sGMT;
   
   public void jdbc(String val) {
	  
	  Connection conn = null;
	  Statement stmt = null;
	  ResultSet rs = null;
	  ResultSet querynum = null;
	  PreparedStatement checkinstmt;
	  PreparedStatement qstmt;
	  PreparedStatement pstmt;
	  PreparedStatement nstmt;
	  
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
	  
	  if((NumberOfTime==0)||(NumberOfTime%2)==0){
		  //登記進場時間回傳至mysql
		  Date now = new Date(); //取得現在時間
		  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  sGMT = sf.format(now);
		  java.sql.Timestamp createDate = Timestamp.valueOf(sGMT); // 要轉換成 java.sql.Date 的物件才可以寫入資料庫
		  System.out.println(sGMT);
		  pstmt = conn.prepareStatement("INSERT INTO timetable (User, timein, whichmachine) VALUES (?,?,?)");
		  pstmt.setString(1, val);
		  pstmt.setTimestamp(2, createDate);	
		  pstmt.setInt(3, whichmachine);
		  pstmt.execute();
		  this.sGMT = sGMT;
		  this.whichmachine = whichmachine;
		  //讀出餘額 
		  pstmt = conn.prepareStatement("SELECT credit FROM administrator where User = ?");
		  pstmt.setString(1, val);
		  rs = pstmt.executeQuery();
		  rs.next();
		  Credit = rs.getInt("credit");
		  this.Credit = Credit;
		  
		  //增加停車次數並回傳至mysql
		  NumberOfTime++;
		  nstmt = conn.prepareStatement("UPDATE administrator SET number_of_time = ? WHERE User = ?");
		  nstmt.setInt(1,NumberOfTime);
		  nstmt.setString(2, val);
		  nstmt.execute();
		  rs.close();
	  }
	  else{
		  whichmachine = 0;
		  this.whichmachine = whichmachine;
		  System.out.println("Error! Double Check In");
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
   
   public int getCredit(){
	   return Credit;
   }
   
   public int getWhichMachine(){
	   return whichmachine;
   }
   
   public String getsGMT(){
	   return sGMT;
   }
}