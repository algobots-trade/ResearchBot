package Formulas;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.h2.jdbcx.JdbcDataSource;
import org.pmw.tinylog.Logger;


public class F2CoreCalculation {
	public double x,y,z;
	public Date t1,t2,t4;
	public int tradecount =0, TCount=0;
	public boolean isBought =false, isSell = false, isShotsell=false;
	public double buyPrice,sellPrice,low=0.0,high=0.0,Mpoint;
	public Date fst1, fst2;
	public static String url = "jdbc:h2:"+System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
	public static String USER="admin", PASS="test123";
    public static String dbName= System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
	
	public static SimpleDateFormat fmt;
	
	public F2CoreCalculation() 
	{
		try
		{
			
		}
		catch(Exception Ex)
		{
			
		}
		finally
		{
			
		}
	}
	public void assignF2Variable(String StDate)
	{
		
		Connection conn = null; 
	    Statement stmt = null;
		try
		{
			JdbcDataSource ds = new JdbcDataSource();
	        ds.setURL("jdbc:h2:"+dbName);
	         conn = ds.getConnection(USER,PASS);
	         stmt = conn.createStatement();
	         stmt.execute("SELECT X,Y,Z,T1,T2,T4 FROM TBL_FORMULA where fname ='FORMULA 2'");
	         ResultSet rs =stmt.getResultSet(); 
	         rs.next();
	         if (rs !=null)
	         {
				x=Double.valueOf(rs.getDouble("X"));
				y=Double.valueOf(rs.getDouble("Y"));
				z=Double.valueOf(rs.getDouble("Z"));
				fmt = new SimpleDateFormat("yyyyMMdd HH:mm:ss z");
				t1 = fmt.parse(StDate+" "+rs.getString("T1")+" IST");
				t2 = fmt.parse(StDate+" "+rs.getString("T2")+" IST");
				t4 = fmt.parse(StDate+" "+rs.getString("T4")+" IST");
	         }
	         rs.close();
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		finally
		{
			try { 
	            if(stmt!=null) stmt.close(); 
	         } catch(SQLException se2) { 
	         } 
	         try { 
	            if(conn!=null) conn.close(); 
	         } catch(SQLException se) { 
	        	 	Logger.error(se);
	         } 	
		}
	}
	public void assignF2TradeInfo(String scripcode)
	{
		
		Connection conn = null; 
	    Statement stmt = null;
		try
		{
			if (scripcode !=null)
			{
				JdbcDataSource ds = new JdbcDataSource();
		        ds.setURL("jdbc:h2:"+dbName);
		         conn = ds.getConnection(USER,PASS);
		         stmt = conn.createStatement();
		         stmt.execute("Select * from TBL_F2_TRADEINFO where symbol = '"+scripcode+"'");
		         ResultSet rs =stmt.getResultSet(); 
		         rs.next();
		         if (rs !=null)
		         {
		        	 
		        	 	if (rs.getString("isBought") != null) {isBought =Boolean.valueOf(rs.getString("isBought"));};
		        	 	if (rs.getString("isSell") != null) {isSell =Boolean.valueOf(rs.getString("isSell"));};
		        	 	if (rs.getString("isShotsell") != null) {isShotsell =Boolean.valueOf(rs.getString("isShotsell"));};
		        	 	if (rs.getString("buyPrice") != null) {buyPrice =Double.valueOf(rs.getDouble("buyPrice"));};
		        	 	if (rs.getString("sellPrice") != null) {sellPrice =Double.valueOf(rs.getDouble("sellPrice"));};
		        	 	if (rs.getString("low") != null) {low =Double.valueOf(rs.getDouble("low"));};
		        	 	if (rs.getString("high") != null) {high =Double.valueOf(rs.getDouble("high"));};
		        	 	if (rs.getString("Mpoint") != null) {Mpoint =Double.valueOf(rs.getDouble("Mpoint"));};
		        	 	if (rs.getString("Tcount") != null) {TCount =rs.getInt("Tcount");};
		        	    
		         }
		         rs.close();
			}
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		finally
		{
			try { 
	            if(stmt!=null) stmt.close(); 
	         } catch(SQLException se2) { 
	         } // nothing we can do 
	         try { 
	            if(conn!=null) conn.close(); 
	         } catch(SQLException se) { 
	        	 	Logger.error(se);
	         } // end finally try 	
		}
	}
	
	public void Calculate(int iteration, String dateStartTime, String strsymbol, String [][] tradeData)
	{	
		assignF2Variable(dateStartTime.split(" ")[0]);
		assignF2TradeInfo(strsymbol);
		try
		{
			if (TCount <2)
			{
				
				String instrument = strsymbol;
				for (int k=1; k<tradeData.length; k++)
				{
					Date ticktime=fmt.parse(tradeData[k][0]);
					double tickprice=Double.valueOf(tradeData[k][1]);
					double ticksize=Double.valueOf(tradeData[k][2]);
					if (ticktime.after(t1))
					{
							if ((low == 0.0) && (high == 0.0))
					    	    {
					    	    		low = tickprice;
					    	    		high =tickprice;
					    	    		iteration = iteration+1;
					    	    		executequery("update TBL_F2_TRADEINFO  set low="+low+", high="+high+" where symbol='"+instrument+"'");
					    	    }
							if ((isBought==false)&&(isSell==false))
							{
						    	    if ( tickprice > high) {high = tickprice;executequery("update TBL_F2_TRADEINFO  set high="+high+" where symbol='"+instrument+"'");}
						    	    if ( tickprice < low) {low = tickprice;executequery("update TBL_F2_TRADEINFO  set low="+low+" where symbol='"+instrument+"'");}
							}
					    	    if (isBought == true)
					    	    {
					    	    		if (tickprice > Mpoint + (Mpoint*(y/100)))
					    	    		{
					    	    			//sell command
					    	    			sellPrice = Double.valueOf(tradeData[k+1][1]);				    	    			
					    	    			fst2 = ticktime;
					    	    			System.out.println("long buy and sell Condition1 :"+ticktime);
					    	    			TCount =TCount +1; 
					    	    			executequery("update TBL_F2_TRADEINFO set EXITCONDITION='PROFIT',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+",Tcount="+TCount+" where symbol='"+instrument+"'");
					    	    			calculatefigure(strsymbol);
					    	    			break;
					    	    		}
					    	    		else if(tickprice < Mpoint - (Mpoint*(z/100)))
					    	    		{
					    	    			//sell command
					    	    			sellPrice = Double.valueOf(tradeData[k+1][1]);
					    	    			fst2 = ticktime;
					    	    			System.out.println("long buy and sell Condition2 :"+ticktime);
					    	    			TCount =TCount +1;
					    	    			executequery("update TBL_F2_TRADEINFO set EXITCONDITION='LOSS',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+",Tcount="+TCount+" where symbol='"+instrument+"'");
					    	    			calculatefigure(strsymbol);
					    	    			break;
					    	    		}//ticktime > t4
					    	    		else if(ticktime.after(t4))
					    	    		{				    	    			
					    	    			//sell command
					    	    			sellPrice = Double.valueOf(tradeData[k+1][1]);				    	    			
					    	    			fst2 = ticktime;
					    	    			System.out.println("long buy and sell Condition3:"+ticktime);
					    	    			TCount =TCount +1;
					    	    			executequery("update TBL_F2_TRADEINFO set EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+",Tcount="+TCount+" where symbol='"+instrument+"'");
					    	    			calculatefigure(strsymbol);
					    	    			break;	
					    	    		}
					    	    		
					    	    }
					    	    else if (isSell == true)
					    	    {
					        	    	if (tickprice < Mpoint - (Mpoint*(y/100)))
					    	    		{
					        	    		//buy command
					        	    		buyPrice = Double.valueOf(tradeData[k+1][1]); 				        	    		
					        	    		fst2 = ticktime;
					        	    		TCount =TCount +1;
					        	    		System.out.println("short sell and buy Condition1 :"+ticktime);
					        	    		executequery("update TBL_F2_TRADEINFO set EXITCONDITION='PROFIT',EXITTIME='"+ticktime.toString()+"',BUYPRICE="+buyPrice+",Tcount="+TCount+" where symbol='"+instrument+"'");
					        	    		calculatefigure(strsymbol);
					        	    		break;
					    	    		}
					    	    		else if(tickprice > Mpoint + (Mpoint*(z/100)))
					    	    		{
					    	    			//buy command
					    	    			buyPrice = Double.valueOf(tradeData[k+1][1]);				        	    		
					        	    		fst2 = ticktime;
					        	    		System.out.println("short sell and buy Condition2 :"+ticktime);
					        	    		TCount =TCount +1;
					        	    		executequery("update TBL_F2_TRADEINFO set EXITCONDITION='LOSS',EXITTIME='"+ticktime.toString()+"',BUYPRICE="+buyPrice+",Tcount="+TCount+" where symbol='"+instrument+"'");
					        	    		calculatefigure(strsymbol);
					        	    		break;
					    	    		}
					    	    		else if(ticktime.after(t4))
					    	    		{
					    	    			//buy command
					    	    			buyPrice = Double.valueOf(tradeData[k+1][1]);				        	    		
					        	    		fst2 = ticktime;
					        	    		System.out.println("short sell and buy Condition3 :"+ticktime);
					        	    		TCount =TCount +1;
					        	    		executequery("update TBL_F2_TRADEINFO set EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',BUYPRICE="+buyPrice+",Tcount="+TCount+" where symbol='"+instrument+"'");
					        	    		calculatefigure(strsymbol);
					        	    		break;
					    	    		}
					    	    		
					    	    }
					    	    
					    	    else if ((isBought==false)&&(isSell==false))
					    	    {
					        	    double buylegprice = low + (low*(x/100)), sellegprice = high - (high*(x/100));
					        	    
						        	    if (tickprice > buylegprice)
						        	    {
						        	    	   
						        	    		fst1 = ticktime;
						        	    		if (fst1.after(t2))
						        	    		{
						        	    			break;
						        	    			//goto end;
						        	    		}
						        	    		else {
						        	    			
						        	    			isShotsell = false;
						        	    			//buy command later
						        	    			buyPrice = Double.valueOf(tradeData[k+1][1]);
						        	    			Mpoint = low + (low*(x/100));
						        	    			isBought=true;
						        	    			System.out.println("Long buy :"+ticktime);
						        	    			System.out.println("HIGH : "+high+", LOW : "+low);
						        	    			TCount=1;
						        	    			executequery("update TBL_F2_TRADEINFO  set ISSHOTSELL='"+isShotsell+"', ENTRYTIME = '"+ticktime.toString()+"',BUYPRICE="+buyPrice+", Mpoint="+Mpoint+", isBought='"+isBought+"',Tcount="+TCount+" where symbol='"+instrument+"'");
						        	    			
						        	    		}
						        	    		
						        	    }
						        	    else if (tickprice < sellegprice)
						        	    {
						        	    		
						        	    		fst1 = ticktime;
						        	    		if (fst1.after(t2))
						        	    		{
						        	    			//isDataGot=true;
						        	    			break;
						        	    			//goto end;
						        	    		}
						        	    		else {
						        	    			//sell command later
						        	    			isShotsell = true;
						        	    			sellPrice = Double.valueOf(tradeData[k+1][1]);
						        	    			Mpoint = high - (high*(x/100));
						        	    			isSell=true;
						        	    			TCount=1;
						        	    			System.out.println("Short Sell :"+ticktime);
						        	    			System.out.println("HIGH : "+high+", LOW : "+low);
						        	    			executequery("update TBL_F2_TRADEINFO set ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+",Mpoint="+Mpoint+", isSell='"+isSell+"',Tcount="+TCount+"  where symbol='"+instrument+"'");
						        	    			
						        	    		}
						        	    }
					    	    }
					    	   // j++;
			    	    }
			        
				}
			}
			
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			
		}
		//return tradecount;
	}
	public void calculatefigure(String scrip)
	{
		double fpl=0.00, fpercent=0,flTradecount=0;
		DecimalFormat f = new DecimalFormat("##.00");
		try
		{
	        if (isShotsell == true)
	        {
	         	
		        	fpl=sellPrice-buyPrice;
		        	double avg =(sellPrice+buyPrice)/2;
		        	fpercent = (fpl*100)/avg;
		        	System.out.println("Selling Price : "+sellPrice );
			    System.out.println("Buying Price : "+buyPrice );
		        	System.out.println("Trade End of the Dau P&L : "+fpl +", Percentage % :"+fpercent +", Trade Count : "+tradecount);
		        	InsertPL(scrip, Double.valueOf(f.format(fpl)), Double.valueOf(f.format(fpercent)), TCount);
	        }
	        else
	        {
	        		
		        	fpl=sellPrice-buyPrice;
		        	double avg =(sellPrice+buyPrice)/2;
		        	fpercent = (fpl*100)/avg;
		        System.out.println("Selling Price : "+sellPrice );
		        System.out.println("Buying Price : "+buyPrice );
		        	System.out.println("Trade End of the Dau P&L : "+fpl +", Percentage % :"+fpercent +", Trade Count : "+tradecount);
		        	InsertPL(scrip, Double.valueOf(f.format(fpl)), Double.valueOf(f.format(fpercent)), TCount);
	        }
		}
		catch(Exception ex)
		{
			
		}
	}
	public void executequery(String Query)
	{
		Connection conn = null; 
	    Statement stmt = null;
		try
		{
			JdbcDataSource ds = new JdbcDataSource();
	        ds.setURL("jdbc:h2:"+dbName);
	         conn = ds.getConnection(USER,PASS);
	         stmt = conn.createStatement();
	         stmt.executeUpdate(Query);
	        
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		finally
		{
			try { 
	            if(stmt!=null) stmt.close(); 
	         } catch(SQLException se2) { 
	         } // nothing we can do 
	         try { 
	            if(conn!=null) conn.close(); 
	         } catch(SQLException se) { 
	        	 	Logger.error(se);
	         } // end finally try 	
		}
	} 
	
	public void InsertPL(String sym, double pl, double percent, int tcount)
	{
		Connection conn = null; 
	    Statement stmt = null;
		try
		{
			JdbcDataSource ds = new JdbcDataSource();
	        ds.setURL("jdbc:h2:"+dbName);
	         conn = ds.getConnection(USER,PASS);
	         stmt = conn.createStatement();
	         stmt.executeUpdate("update TBL_PLDATATABLE  set  F2PL="+pl+" , F2PRECENT = "+percent+" , F2TRADECOUNT = "+tcount+" where symbol ='"+sym+"'");
	        
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		finally
		{
			try { 
	            if(stmt!=null) stmt.close(); 
	         } catch(SQLException se2) { 
	         } // nothing we can do 
	         try { 
	            if(conn!=null) conn.close(); 
	         } catch(SQLException se) { 
	        	 	Logger.error(se);
	         } // end finally try 	
		}
	}

}
