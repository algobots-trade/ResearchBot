package Forms;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.h2.jdbcx.JdbcDataSource;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.FileWriter;

import ApiConsumer.consumeIB;
import GUICommons.ClockLabel;
import GUICommons.DateValidator;
import commons.db_commons;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.awt.Dialog.ModalExclusionType;



public class Home {

	protected static final SwingUtilities SwingUtilties = null;
	
	PLTableModel model=null;
    JTable table=null;
	private JFrame frmOvviMarketBot;
	private JRadioButton rdbtnStocks, rdbtnfutures;
	private File logfile;
	private Connection con;private JTextField txtdd;
	private JTextField txtMm;
	private JTextField txtYy;
	private JProgressBar progressInResearch;
	public static String url = "jdbc:h2:"+System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
	public static String USER="admin", PASS="test123";
    public static String dbName= System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
   
    int completedcount=0, totalscripscount=0;
   
	

	private Label f1PL,f2PL,f3PL,f4PL,f5PL;
	private JLabel lblStockTitle;
	private ClockLabel timeLable;
	private ClockLabel dateLable;
	private ClockLabel dayLable;
	private JButton btnClear;
	private JButton btnDcsv;
	private JButton btnRun;
	private JPanel totalpanel;
	private Label lbltotal;

   
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			 	Home ovviMain =new	Home();
			}
		});
	}


	public Home() 
	{
		try 
		{
		    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
			//KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			//manager.addKeyEventDispatcher( new KeyDispatcher() );
			JdbcDataSource ds = new JdbcDataSource();
			
            ds.setURL(url);
	        con = ds.getConnection(USER,PASS);
	        mainframe();
	        designMenuBar();
		    loadAllStaticObject();
			loadAllDynamicObject();
			createTotalPanel();
			
			db_commons dbobj=new db_commons();
			if (dbobj.getRowCount("Select * from TBL_SYMBOL_MST") == 0 )
			{
				String[] dropCreate= new String [1];
			    dropCreate[0] = "drop table tbl_pldatatable;create table tbl_pldatatable (id bigint auto_increment,symbol varchar(255) not null,f1pl double null,f1precent double null,f1tradecount double null,f2pl double null,f2precent double null,f2tradecount double null,f3pl double null, " + 
			    		"f3precent double null,f3tradecount double null,f4pl double null,f4precent double null,f4tradecount double null,f5pl double null,f5precent double null,f5tradecount double null, first double null, last double null );drop table tbl_F1_TradeInfo;create table tbl_F1_TradeInfo (id bigint auto_increment,   symbol varchar(255) not null,  entrytime varchar(255) null, buyprice  double null,    sellprice double null, exittime varchar(255) null,  isShotsell varchar(255) null, high double null,    low double null, " + 
			    		"isBought varchar(255) null, isSell varchar(255) null, Mpoint double null,exitcondition varchar(255) null,Tcount int null);drop table tbl_F2_TradeInfo;create table tbl_F2_TradeInfo (id bigint auto_increment,   symbol varchar(255) not null,  entrytime varchar(255) null, buyprice  double null,    sellprice double null, exittime varchar(255) null,  isShotsell varchar(255) null, high double null,    low double null, " + 
			    		"isBought varchar(255) null, isSell varchar(255) null, Mpoint double null,exitcondition varchar(255) null,Tcount int null);drop table tbl_F3_TradeInfo;create table tbl_F3_TradeInfo (id bigint auto_increment,   symbol varchar(255) not null,  entrytime varchar(255) null, buyprice  double null,    sellprice double null, exittime varchar(255) null,  isShotsell varchar(255) null, high double null,    low double null, " + 
			    		"isBought varchar(255) null, isSell varchar(255) null, Mpoint double null,exitcondition varchar(255) null,Tcount int null);drop table tbl_F4_TradeInfo;create table tbl_F4_TradeInfo (id bigint auto_increment,   symbol varchar(255) not null,  entrytime varchar(255) null, buyprice  double null,    sellprice double null, exittime varchar(255) null,  isShotsell varchar(255) null, high double null,    low double null, " + 
			    		"isBought varchar(255) null, isSell varchar(255) null, Mpoint double null, Bpoint double null ,exitcondition varchar(255) null,Tcount int null);drop table tbl_F5_TradeInfo;create table tbl_F5_TradeInfo (id bigint auto_increment,   symbol varchar(255) not null,  entrytime varchar(255) null, buyprice  double null,    sellprice double null, exittime varchar(255) null,  isShotsell varchar(255) null, high double null,    low double null, " + 
			    		"isBought varchar(255) null, isSell varchar(255) null, Mpoint double null, Bpoint double null,exitcondition varchar(255) null,Tcount int null);";

			    dbobj.executeBatchStatement(dropCreate);
				String dir = System.getProperty("user.dir");
				String Sep= System.getProperty("file.separator");
				BufferedReader br = null; 
				File myFile = new File(dir+Sep+"T1.txt"); 
		        try
		        {
		        		if((myFile.exists()))
					 { 
		        			    FileReader reader  = new FileReader(myFile);  
		        				br = new BufferedReader(reader); 
		                    String line = null;  
		                    int count = 0;
		                   
		                    while ((line = br.readLine()) != null) { 
		                    	   dbobj.executeNonQuery("insert into TBL_SYMBOL_MST (SCRIB,QTY) values ('"+line.split(",")[0]+"','"+line.split(",")[1]+"');");
		                        count = count +1; 
		                    } 
		                    if (count > 0)
		                    {
			                    	String [][] scribs = dbobj.getMultiColumnRecords("Select SCRIB,QTY from TBL_SYMBOL_MST;");
	    							if (scribs !=null)
	    							{
	    						    String[] plInsert=new String[scribs.length];
	    						    String[] F1Insert=new String[scribs.length];
	    						    String[] F2Insert=new String[scribs.length];
	    						    String[] F3Insert=new String[scribs.length];
	    						    String[] F4Insert=new String[scribs.length];
	    						    String[] F5Insert=new String[scribs.length];
	    						    for(int i=0; i< scribs.length; i++)
	    						    {
	    						    		plInsert[i] = "insert into TBL_PLDATATABLE (SYMBOL) values ('"+scribs[i][0]+"');";
	    						    		F1Insert[i] = "insert into TBL_F1_TRADEINFO (SYMBOL) values ('"+scribs[i][0]+"');";
	    						    		F2Insert[i] = "insert into TBL_F2_TRADEINFO (SYMBOL) values ('"+scribs[i][0]+"');";
	    						    		F3Insert[i] = "insert into TBL_F3_TRADEINFO (SYMBOL) values ('"+scribs[i][0]+"');";
	    						    		F4Insert[i] = "insert into TBL_F4_TRADEINFO (SYMBOL) values ('"+scribs[i][0]+"');";
	    						    		F5Insert[i] = "insert into TBL_F5_TRADEINFO (SYMBOL) values ('"+scribs[i][0]+"');";
	    						    	 
	    						    }
	    						   
	    						    dbobj.executeBatchStatement(plInsert);
	    						    dbobj.executeBatchStatement(F1Insert);
	    						    dbobj.executeBatchStatement(F2Insert);
	    						    dbobj.executeBatchStatement(F3Insert);
	    						    dbobj.executeBatchStatement(F4Insert);
	    						    dbobj.executeBatchStatement(F5Insert);
	    							}
		                    }
		                    else
		                    {
		                    		JOptionPane.showMessageDialog(frmOvviMarketBot,"No Symbols are added  & No Data in backup file. Kinldy add !!", "Empty Record",JOptionPane.WARNING_MESSAGE);
		                    		SymbolMgmt sybmgmt=new SymbolMgmt();
		                    }
				     }
		        		else
		        		{
		        			JOptionPane.showMessageDialog(frmOvviMarketBot,"No Data in Scrib back file. Kinldy add !!", "Empty Record",JOptionPane.WARNING_MESSAGE);
                    		SymbolMgmt sybmgmt=new SymbolMgmt();
		        		}
		        }
		        catch(Exception ex)
		        {
		        	
		        }
		        finally
		        {
		        	
		        }
			}
			  
			  EventQueue.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                    try {
	                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
	                        ex.printStackTrace();
	                    }
	                    Outerpane grid = new Outerpane();	                    
			    	    		grid.setBounds(10,50, 1664, 780);
			    	    		grid.setBorder(null);
		        		    grid.setBackground(new Color(80,75,78));
		        		    grid.setLayout(null);
			    	    		frmOvviMarketBot.getContentPane().add(grid);
		    	    	
	             }
	            });
			
			
		 }
		catch (Exception e){e.printStackTrace();}		
	}

    private void mainframe()
    {
	    	try
	    	{
	    		
	    		frmOvviMarketBot = new JFrame();
	    		frmOvviMarketBot.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
			frmOvviMarketBot.setTitle("Ovvi Market Bot - Engine");
			frmOvviMarketBot.getContentPane().setBackground(new Color(36,34,29));
			frmOvviMarketBot.setBackground(new Color(36,34,29));
			//frmOvviMarketBot.setSize(500, 500);
			frmOvviMarketBot.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frmOvviMarketBot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frmOvviMarketBot.getContentPane().setLayout(null);
			frmOvviMarketBot.setVisible(true);
			
			

			rdbtnStocks = new JRadioButton("STOCKS", true);
    			rdbtnStocks.setBounds(8, 2, 141, 23);
    			rdbtnStocks.setFont(new Font("Tahoma", Font.PLAIN, 16));
    			rdbtnStocks.setForeground(Color.WHITE);
    			rdbtnStocks.setBackground(new Color(36,34,29));
    			rdbtnStocks.setBorder(null);
    			
    			frmOvviMarketBot.getContentPane().add(rdbtnStocks);
    		
    			 rdbtnfutures = new JRadioButton("FUTURES");
    			 rdbtnfutures.setBounds(8, 26, 141, 23);
    			 rdbtnfutures.setFont(new Font("Tahoma", Font.PLAIN, 16));
    			 rdbtnfutures.setForeground(Color.WHITE);
    			 frmOvviMarketBot.getContentPane().add(rdbtnfutures);
    			
    			ButtonGroup btngroup=new ButtonGroup();  

    			 btngroup.add(rdbtnStocks);  
    			 btngroup.add(rdbtnfutures);
    			 rdbtnfutures.setVisible(false);
    			 Logger.info("Ovvi Main Form Loaded properly");
    			
	    	}
		catch(Exception ex)
		{
			Logger.error(ex);	
		}
    	}

    private void loadAllStaticObject()
    {
    		/*
         * all lablel and static object are defined and designed here
         */
    		try
		{
    			//Stock Market Label
	    	    lblStockTitle = new JLabel("MARKET RESEARCH");
	    	    lblStockTitle.setHorizontalAlignment(SwingConstants.CENTER);
	    	    lblStockTitle.setFont(new Font("Tsukushi A Round Gothic", Font.PLAIN, 24));
	    	    lblStockTitle.setForeground(new Color(240,159,108));
	    	    lblStockTitle.setBounds(730, 0, 250, 51);
			frmOvviMarketBot.getContentPane().add(lblStockTitle);
			

    		}
    		catch(Exception ex)
    		{
    			Logger.error(ex);	
    		}
    	
    }
    private void dCSV(String [][] recs)
    {
	    	String dir = System.getProperty("user.dir");
			String Sep= System.getProperty("file.separator");
	        BufferedWriter bWrite1 = null;
	        String timeStamp = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss", Locale.ENGLISH).format(new Date());
	        File myFile = new File(dir+Sep+timeStamp+".csv"); 
			try
			{
				 if(!(myFile.exists()))
				 { 
			            myFile.createNewFile(); 
			            System.out.println("New File created...."); 
			     }
				 else
				 {
					 myFile.delete();
					 myFile.createNewFile();
				 }
				 bWrite1 = new BufferedWriter(new java.io.FileWriter(myFile));
				 for(int i=0; i < recs.length; i++)
				 {
					 if (i==0)
					 {
						 bWrite1.write("SYMBOL,F1PL,F1PRECENT,F1TRADECOUNT,F2PL,F2PRECENT,F2TRADECOUNT,F3PL,F3PRECENT,F3TRADECOUNT,F4PL,F4PRECENT,F4TRADECOUNT,F5PL,F5PRECENT,F5TRADECOUNT,FIRST,LAST");
						 bWrite1.newLine();
					 }
					 bWrite1.write(recs[i][1]+","+recs[i][2]+","+recs[i][3]+","+recs[i][4]+","+recs[i][5]+","+recs[i][6]+","+recs[i][7]+","+recs[i][8]+","+recs[i][9]+","+recs[i][10]+","+recs[i][11]+","+recs[i][12]+","+recs[i][13]+","+recs[i][14]+","+recs[i][15]+","+recs[i][16]+","+recs[i][17]+","+recs[i][8]);
					 bWrite1.newLine();
				 }
				 bWrite1.flush();
				 
			}
			catch(Exception ex)
			{
				if (bWrite1 != null) try {
					bWrite1.close();
					
				 } catch (IOException ioe2) {
				    // just ignore it
				 }
			}
			finally
			{
				
			}
    }
    private void loadAllDynamicObject()
    {
    		/*
         * all lablel and dynamic object are defined and designed here
         */
    		try
    		{
    			
    			
    			dateLable = new ClockLabel("date");
    			dateLable.setForeground(new Color(210, 180, 140));
    			dateLable.setSize(130, 26);
    			dateLable.setLocation(1567, 0);
    		    timeLable = new ClockLabel("time");
    		    timeLable.setFont(new Font("American Typewriter", Font.PLAIN, 24));
    		    timeLable.setBounds(1388, 1, 181, 48);
    		    
    		    dayLable = new ClockLabel("day");
    		    dayLable.setForeground(new Color(210, 180, 140));
    		    dayLable.setSize(99, 26);
    		    dayLable.setLocation(1567, 25);
    			//timer ticks
    			
    			frmOvviMarketBot.getContentPane().add(timeLable);	
    			frmOvviMarketBot.getContentPane().add(dateLable);	
    			frmOvviMarketBot.getContentPane().add(dayLable);	
    			db_commons dbobj=new db_commons();
    			int progresscount = dbobj.getRowCount("select * from tbl_pldatatable;");
    			progressInResearch = new JProgressBar(0,progresscount);
    			progressInResearch.setBackground(Color.WHITE);
    			//progressInResearch.setStringPainted(true);
    			progressInResearch.setBounds(636, 878, 453, 10);
    			progressInResearch.setForeground(Color.GREEN);
	    		frmOvviMarketBot.getContentPane().add(progressInResearch);
    			
    				
    			
    			btnClear = new JButton("Clear");
    			btnClear.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					try
    					{
    						int opcion = JOptionPane.showConfirmDialog(null, "Are You Sure, Want to Clean ?", "Clear Confirmation", JOptionPane.YES_NO_OPTION);
    						if (opcion == 0) 
    						{ //The ISSUE is here
    							//RunScript.execute(con, new FileReader("/Users/sivaamur/Desktop/Clean_Market_bot.sql"));
    							db_commons db=new db_commons();
    						    String[] dropCreate= new String [1];
    						    dropCreate[0] = "drop table tbl_pldatatable;create table tbl_pldatatable (id bigint auto_increment,symbol varchar(255) not null,f1pl double null,f1precent double null,f1tradecount double null,f2pl double null,f2precent double null,f2tradecount double null,f3pl double null, " + 
    						    		"f3precent double null,f3tradecount double null,f4pl double null,f4precent double null,f4tradecount double null,f5pl double null,f5precent double null,f5tradecount double null, first double null, last double null );drop table tbl_F1_TradeInfo;create table tbl_F1_TradeInfo (id bigint auto_increment,   symbol varchar(255) not null,  entrytime varchar(255) null, buyprice  double null,    sellprice double null, exittime varchar(255) null,  isShotsell varchar(255) null, high double null,    low double null, " + 
    						    		"isBought varchar(255) null, isSell varchar(255) null, Mpoint double null,exitcondition varchar(255) null,Tcount int null);drop table tbl_F2_TradeInfo;create table tbl_F2_TradeInfo (id bigint auto_increment,   symbol varchar(255) not null,  entrytime varchar(255) null, buyprice  double null,    sellprice double null, exittime varchar(255) null,  isShotsell varchar(255) null, high double null,    low double null, " + 
    						    		"isBought varchar(255) null, isSell varchar(255) null, Mpoint double null,exitcondition varchar(255) null,Tcount int null);drop table tbl_F3_TradeInfo;create table tbl_F3_TradeInfo (id bigint auto_increment,   symbol varchar(255) not null,  entrytime varchar(255) null, buyprice  double null,    sellprice double null, exittime varchar(255) null,  isShotsell varchar(255) null, high double null,    low double null, " + 
    						    		"isBought varchar(255) null, isSell varchar(255) null, Mpoint double null,exitcondition varchar(255) null,Tcount int null);drop table tbl_F4_TradeInfo;create table tbl_F4_TradeInfo (id bigint auto_increment,   symbol varchar(255) not null,  entrytime varchar(255) null, buyprice  double null,    sellprice double null, exittime varchar(255) null,  isShotsell varchar(255) null, high double null,    low double null, " + 
    						    		"isBought varchar(255) null, isSell varchar(255) null, Mpoint double null, Bpoint double null ,exitcondition varchar(255) null,Tcount int null);drop table tbl_F5_TradeInfo;create table tbl_F5_TradeInfo (id bigint auto_increment,   symbol varchar(255) not null,  entrytime varchar(255) null, buyprice  double null,    sellprice double null, exittime varchar(255) null,  isShotsell varchar(255) null, high double null,    low double null, " + 
    						    		"isBought varchar(255) null, isSell varchar(255) null, Mpoint double null, Bpoint double null,exitcondition varchar(255) null,Tcount int null);";

    						    db.executeBatchStatement(dropCreate);
    							String [][] scribs = db.getMultiColumnRecords("Select SCRIB,QTY from TBL_SYMBOL_MST;");
    							if (scribs !=null)
    							{
    						    String[] plInsert=new String[scribs.length];
    						    String[] F1Insert=new String[scribs.length];
    						    String[] F2Insert=new String[scribs.length];
    						    String[] F3Insert=new String[scribs.length];
    						    String[] F4Insert=new String[scribs.length];
    						    String[] F5Insert=new String[scribs.length];
    						    for(int i=0; i< scribs.length; i++)
    						    {
    						    		plInsert[i] = "insert into TBL_PLDATATABLE (SYMBOL) values ('"+scribs[i][0]+"');";
    						    		F1Insert[i] = "insert into TBL_F1_TRADEINFO (SYMBOL) values ('"+scribs[i][0]+"');";
    						    		F2Insert[i] = "insert into TBL_F2_TRADEINFO (SYMBOL) values ('"+scribs[i][0]+"');";
    						    		F3Insert[i] = "insert into TBL_F3_TRADEINFO (SYMBOL) values ('"+scribs[i][0]+"');";
    						    		F4Insert[i] = "insert into TBL_F4_TRADEINFO (SYMBOL) values ('"+scribs[i][0]+"');";
    						    		F5Insert[i] = "insert into TBL_F5_TRADEINFO (SYMBOL) values ('"+scribs[i][0]+"');";
    						    	 
    						    }
    						   
    						    db.executeBatchStatement(plInsert);
    						    db.executeBatchStatement(F1Insert);
    						    db.executeBatchStatement(F2Insert);
    						    db.executeBatchStatement(F3Insert);
    						    db.executeBatchStatement(F4Insert);
    						    db.executeBatchStatement(F5Insert);
    							}
    						    
    						} 
    						}
    					catch(Exception ex)
    					{
    						
    					}
    				}
    			});
    			btnClear.setBounds(1567, 886, 87, 47);
    			frmOvviMarketBot.getContentPane().add(btnClear);
    			btnClear.setBackground(new Color(25,162,170));
    			
    			btnDcsv = new JButton("D-CSV");
    			btnDcsv.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					
    					try
    					{
    						db_commons dbobj=new db_commons();
    						String [][] recs = dbobj.getMultiColumnRecords("Select * from TBL_PLDATATABLE");
    						dCSV(recs);
    						
    					}
    					catch(Exception ex)
    					{
    						
    					}
    					finally
    					{
    						
    					}
    					
    					
    				}
    			});
    			btnDcsv.setBounds(926, 900, 163, 35);
    			frmOvviMarketBot.getContentPane().add(btnDcsv);
    			
    			btnRun = new JButton("RUN");
    			btnRun.setBounds(636, 898, 181, 35);
    			frmOvviMarketBot.getContentPane().add(btnRun);
    			
    			txtdd = new JTextField();
    			txtdd.setForeground(new Color(255, 255, 255));
    			txtdd.setBackground(new Color(80, 75, 78));
    			txtdd.setHorizontalAlignment(SwingConstants.CENTER);
    			txtdd.setText("DD");
    			txtdd.setBounds(10, 886, 47, 47);
    			txtdd.setCaretColor(Color.WHITE);
    			frmOvviMarketBot.getContentPane().add(txtdd);
    			txtdd.setColumns(10);
    			txtdd.addFocusListener(new FocusListener() {
    			    public void focusGained(FocusEvent e) {
    			    	txtdd.setText("");
    			    }

    			    public void focusLost(FocusEvent e) {
    			    	    if(txtdd.getText().equals(""))
    			    	    {
    			    	    		txtdd.setText("DD");
    			    	    }
    			        // nothing
    			    }
    			});
    			
    			txtMm = new JTextField();
    			txtMm.setText("MM");
    			txtMm.setHorizontalAlignment(SwingConstants.CENTER);
    			txtMm.setForeground(Color.WHITE);
    			txtMm.setColumns(10);
    			txtMm.setBackground(new Color(80, 75, 78));
    			txtMm.setBounds(60, 886, 47, 47);
    			txtMm.setCaretColor(Color.WHITE);
    			frmOvviMarketBot.getContentPane().add(txtMm);
    			txtMm.addFocusListener(new FocusListener() {
    			    public void focusGained(FocusEvent e) {
    			    	txtMm.setText("");
    			    }

    			    public void focusLost(FocusEvent e) {
    			    	    if(txtMm.getText().equals(""))
    			    	    {
    			    	    		txtMm.setText("MM");
    			    	    }
    			        // nothing
    			    }
    			});
    			
    			txtYy = new JTextField();
    			txtYy.setText("YYYY");
    			txtYy.setHorizontalAlignment(SwingConstants.CENTER);
    			txtYy.setForeground(Color.WHITE);
    			txtYy.setColumns(10);
    			txtYy.setBackground(new Color(80, 75, 78));
    			txtYy.setBounds(110, 886, 67, 47);
    			txtYy.setCaretColor(Color.WHITE);
    			txtYy.addFocusListener(new FocusListener() {
    			    public void focusGained(FocusEvent e) {
    			    	txtYy.setText("");
    			    }

    			    public void focusLost(FocusEvent e) {
    			    	    if(txtYy.getText().equals(""))
    			    	    {
    			    	    	txtYy.setText("YYYY");
    			    	    }
    			        // nothing
    			    }
    			});
    			frmOvviMarketBot.getContentPane().add(txtYy);
    			btnRun.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) 
    				{    					
    					try
    					{
    						
    						
    					    String strMarkettype="STK";
    						String strStockExchange="NSE";
    						String strStockCurrency="INR";
    						String whichData = "TRADES";
    						//After implementation on stock will enable futures
    						//if (rdbtnStocks.isSelected()) { strWhichdata ="FUT";};
    						//if (rdbtnStocks.isSelected()) { strMarkettype ="STK";}
    						DateValidator dv=new DateValidator();
    						String strStartDate =txtYy.getText()+txtMm.getText()+txtdd.getText();
    						Boolean isValidDate = dv.isThisDateValid(strStartDate, "yyyyMMdd");
    						Boolean isNotWeekend = dv.isThisDateWeekend(strStartDate, "yyyyMMdd");
    						Thread mainengine = null;
    						if ((!isValidDate)||(!isNotWeekend))
    						{
    							JOptionPane.showMessageDialog(frmOvviMarketBot,"Invalid Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
    							txtdd.requestFocus();
    						}
    						else
    						{
    					   // progressInResearch = new JProgressBar(15);
    							btnRun.setText("RESEARCHING");
    							btnDcsv.setEnabled(false);
    							btnClear.setEnabled(false);
    							btnRun.setEnabled(false);
    							mainengine = new Thread(){
    						    public void run(){
    						    		System.out.println("Thread Running");
    						    		db_commons dbobj =new db_commons();
    						    		ArrayList<String> symbol=dbobj.getSingleColumnRecords("select SYMBOL from TBL_PLDATATABLE order by ID ASC");
    						    		totalscripscount=symbol.size();
    						    		String startDateTime =  strStartDate+" 09:18:00";
	    	    						for(int i=0; i<symbol.size(); i++)
	    	        					{
	    	    							
	    	    							Logger.info("==============RESEARCH STARTED=============");
	    	        						String instrument = symbol.get(i).toString();
	    	        						consumeIB ibconnect=new consumeIB();
	    	        						Logger.info("Research Started For ------------> "+instrument);
	    	        						String [] contract = {instrument,strMarkettype, strStockCurrency,strStockExchange};
	    	        						try 
	    	        						{
	    	        							String [] f1results =  ibconnect.calculateF1PL(contract,startDateTime,whichData,"STK");
	    	        							completedcount = i;
									} 
	    	        						catch (ParseException e) 
	    	        						{
	    	        							Logger.error(e);
									}
	    	        						progressInResearch.setValue(i+1);
	    	        						double t= i*(symbol.size()/100);
	    	        						progressInResearch.setString(String.valueOf(t)+"%");
	    	        					}
	    	    						btnRun.setText("Run");
	        						btnRun.setEnabled(true);
	        						btnDcsv.setEnabled(true);
	    							btnClear.setEnabled(true);
	    							JOptionPane.showMessageDialog(frmOvviMarketBot,"Research Completed !!","Task Completed",JOptionPane.INFORMATION_MESSAGE);
    						    }
    						  };

    						  mainengine.start();
    						
    						}
//    						if (completedcount == (totalscripscount -1))
//    						{
//    							btnRun.setText("Run");
//    							btnRun.setEnabled(true);
//    						}

    						
    					}
    					catch(Exception ex)
    					{
    						Logger.error(ex);
    					}
    				}
    			});
    			
    		}
    		catch(Exception ex)
    		{
    			Logger.error(ex);	
    		}
    	
    }
    
    
    private void createTotalPanel()
    {
    		/*
         * Creates Team panel
         */
    		try
    		{
    			
    			
    			totalpanel = new JPanel();
    			totalpanel.setBounds(10, 834, 1687, 35);
    			frmOvviMarketBot.getContentPane().add(totalpanel);
    			totalpanel.setBackground(new Color(61, 57, 54));
    			totalpanel.setLayout(null);
    			
    			lbltotal = new Label("TOTAL");
    			lbltotal.setAlignment(Label.CENTER);
    			lbltotal.setForeground(Color.WHITE);
    			lbltotal.setBounds(0, 10, 61, 16);
    			totalpanel.add(lbltotal);
    			
    			f1PL = new Label();
    			f1PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
    			f1PL.setForeground(new Color(255,220,135));
    			f1PL.setAlignment(Label.CENTER);
    			f1PL.setBounds(113, 10, 85, 16);
    			totalpanel.add(f1PL);
    			
    			f2PL = new Label();
    			f2PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
    			f2PL.setForeground(new Color(255,220,135));
    			f2PL.setAlignment(Label.CENTER);
    			f2PL.setBounds(390, 10, 85, 16);
    			totalpanel.add(f2PL);
    			
    			f3PL = new Label();
    			f3PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
    			f3PL.setForeground(new Color(255,220,135));
    			f3PL.setAlignment(Label.CENTER);
    			f3PL.setBounds(660, 10, 85, 16);
    			totalpanel.add(f3PL);
    			
    			f4PL = new Label();
    			f4PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
    			f4PL.setForeground(new Color(255,220,135));
    			f4PL.setAlignment(Label.CENTER);
    			f4PL.setBounds(937, 10, 85, 16);
    			totalpanel.add(f4PL);
    			
    			f5PL = new Label();
    			f5PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
    			f5PL.setForeground(new Color(255,220,135));
    			f5PL.setAlignment(Label.CENTER);
    			f5PL.setBounds(1210, 10, 61, 16);
    			totalpanel.add(f5PL);

    		    
    		    
    		}
    		catch(Exception ex)
    		{
    			Logger.error(ex);	
    		}
    		finally
    		{
    			
    		}
    	
    	
    }
	
	private void designMenuBar(){
		
		try
		{
	      //create a menu bar
	      final JMenuBar menuBar = new JMenuBar();

	      //create menus
	      JMenu fileMenu = new JMenu("File");
	      JMenu editMenu = new JMenu("Team"); 
	      final JMenu aboutMenu = new JMenu("Formula");
	      final JMenu linkMenu = new JMenu("Trace");
	     
	      //create menu items
	      JMenuItem newMenuItem = new JMenuItem("New");
	      newMenuItem.setMnemonic(KeyEvent.VK_N);
	      newMenuItem.setActionCommand("New");

	      JMenuItem openMenuItem = new JMenuItem("Open");
	      openMenuItem.setActionCommand("Open");

	      JMenuItem saveMenuItem = new JMenuItem("Save");
	      saveMenuItem.setActionCommand("Save");

	      JMenuItem exitMenuItem = new JMenuItem("Exit");
	      exitMenuItem.setActionCommand("Exit");

	      JMenuItem createTeam = new JMenuItem("Create");
	      createTeam.setActionCommand("Create");

	      JMenuItem viewTeam = new JMenuItem("View");
	      viewTeam.setActionCommand("View");

	      JMenuItem editTeam = new JMenuItem("Edit");
	      editTeam.setActionCommand("Edit");
	      
	      JMenuItem deleteteam = new JMenuItem("Delete");
	      deleteteam.setActionCommand("Edit");

	      final JCheckBoxMenuItem showWindowMenu = new JCheckBoxMenuItem("Show About", true);
	      showWindowMenu.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {
	            
	            if(showWindowMenu.getState()){
	               menuBar.add(aboutMenu);
	            } else {
	               menuBar.remove(aboutMenu);
	            }
	         }
	      });
	      final JRadioButtonMenuItem showLinksMenu = new JRadioButtonMenuItem(
	         "Show Links", true);
	      showLinksMenu.addItemListener(new ItemListener() {
	         public void itemStateChanged(ItemEvent e) {
	            
	            if(menuBar.getMenu(3)!= null){
	               menuBar.remove(linkMenu);
	               frmOvviMarketBot.repaint();
	            } else {                   
	               menuBar.add(linkMenu);
	               frmOvviMarketBot.repaint();
	            }
	         }
	      });
	      //add menu items to menus
	      fileMenu.add(newMenuItem);
	      fileMenu.add(openMenuItem);
	      fileMenu.add(saveMenuItem);
	      fileMenu.addSeparator();
	      fileMenu.add(showWindowMenu);
	      fileMenu.addSeparator();
	      fileMenu.add(showLinksMenu);       
	      fileMenu.addSeparator();
	      fileMenu.add(exitMenuItem);        
	      
	      editMenu.add(createTeam);
	      editMenu.add(editTeam);
	      editMenu.add(viewTeam);
	      editMenu.add(deleteteam);

	      //add menu to menubar
	      menuBar.add(fileMenu);
	      menuBar.add(editMenu);
	      menuBar.add(aboutMenu);       
	      menuBar.add(linkMenu);

	      //add menubar to the frame
	      frmOvviMarketBot.setJMenuBar(menuBar);
	      frmOvviMarketBot.setVisible(true);     
		}
		catch(Exception ex)
		{
			Logger.error(ex);	
		}
		
	}

	private void logInsane()
	{
		try
		{
			String logname = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
			logfile = new File("/tmp/ovvi_"+logname+".log");
			Configurator.defaultConfig().writer(new FileWriter(logfile.getPath())).level(Level.DEBUG).level(Level.WARNING).level(Level.INFO).level(Level.ERROR).level(Level.TRACE).activate();
		    Logger.info("Logged In Sucesfully !! Log file are created in path :"+logfile);
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		finally
		{
			
		}
	}


	public class Outerpane extends JPanel implements KeyListener{
		JTable table;
		PLTableModel model;
        public Outerpane() {
        	 model = new PLTableModel();
        	 table = new JTable(model) {
    				public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
    				{
    					Component c = super.prepareRenderer(renderer, row, column);
    					c.setSize(5,100);
    					
    					if ( (column % 3 != 0) && (column > 0))
    					{
    						if (model.getValueAt(row, column).toString() !="")
    						{
	    						double value = Double.parseDouble(model.getValueAt(row, column).toString());
		    						
		    						if (value > 0)
		    						{
		    						c.setForeground(new Color(255,220,135));
		    						c.setFont(new Font("Tahoma", Font.BOLD, 12));
		    						}
		    						else if (value == 0)
		    						{
		    							c.setFont(new Font("Tahoma", Font.PLAIN, 0));
		    						}
		    						else
		    						{
		    						c.setForeground(new Color(103,186,233));
		    						c.setFont(new Font("Tahoma", Font.BOLD, 12));
		    						}
    						}
    						
    					}
    					else
    					{
    						c.setForeground(Color.WHITE);
    						c.setFont(new Font("Tahoma", Font.BOLD, 12));
    						if (model.getValueAt(row, column).toString().equals("0"))
    						{
    							c.setFont(new Font("Tahoma", Font.PLAIN, 0));
    						}
    					}
    					
    					if (column==0)
    					{
    					((JLabel) c).setHorizontalAlignment(JLabel.LEFT);
    					
    					}
    					else
    					{
    						((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
    					}
    					if (!isRowSelected(row))
    						if (row % 2 == 0)
    						{
    						c.setBackground(new Color(58,54,51));
    						}
    						else
    						{
    						c.setBackground(new Color(79,75,72));
    						}
    					return c;
    				}    
    			};
            
            
			table.changeSelection(0, 0, false, false);
			table.setBorder(null);
			table.setGridColor(new Color(90,86,83));
			table.setForeground(Color.WHITE);
			table.setDefaultEditor(Object.class, null);
			table.addKeyListener(this);
			
			
			JTableHeader header = table.getTableHeader();
		    header.setBackground(new Color(36,34,29));
		    header.setForeground(new Color(255,220,135));
		    header.setFont(new Font("Tahoma", Font.PLAIN, 14));
		    //header.addKeyListener(this);
            		
		    JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setEnabled(false);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setViewportBorder(null);
			scrollPane.setBorder(null);
			scrollPane.setBounds(6, 6, 1650, 765);
			//scrollPane.addKeyListener(this);
            
            
            add(scrollPane);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                	try {
                    	
                    	Timer timer = new Timer(0, new ActionListener() {

                    		   @Override
                    		   public void actionPerformed(ActionEvent e) {
                    			   try {
                    				   int selectedrow = table.getSelectedRow();
                    		            if (selectedrow != -1)
                    		            {
                    		            		selectedrow = table.getSelectedRow();
                    		            }
                    				   model.clear();
								   model.refresh();
								   model.SumPL();
								    if (selectedrow != -1)
						            {
								    	 if (model.getRowCount() > 0)
								    	 {
						            		table.setRowSelectionInterval(selectedrow, selectedrow);
								    	 }
						            } 
								} catch (SQLException ex) {
									// TODO Auto-generated catch block
									//ex.printStackTrace();
								}
                    		   }
                    		});

                    		timer.setDelay(8000); // delay for 30 seconds
                    		timer.start();
                    	
                       
                    }  catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            
        }
        


    	@Override
    	public void keyTyped(KeyEvent e) {
    		// TODO Auto-generated method stub

    	}


    	@Override
    	public void keyPressed(KeyEvent e) {
    		// TODO Auto-generated method stub
    		if (e.getKeyCode() == 112)// Check F1 Key press
    		{
    			FormulaInputs formula =new FormulaInputs("FORMULA 1");
    		}
    		else if (e.getKeyCode() == 113)// Check F2 Key press
    		{
    			FormulaInputs formula =new FormulaInputs("FORMULA 2");
    		}
    		else if (e.getKeyCode() == 114)// Check F3 Key press
    		{
    			FormulaInputs formula =new FormulaInputs("FORMULA 3");
    		}
    		else if (e.getKeyCode() == 115)// Check F4 Key press
    		{
    			FormulaInputs formula =new FormulaInputs("FORMULA 4");
    		}
    		else if (e.getKeyCode() == 116)// Check F5 Key press
    		{
    			FormulaInputs formula =new FormulaInputs("FORMULA 5");
    		}
    		else if (e.getKeyCode() == 49)// Check 1 Key press
    		{
    			TradeInfo tiobj=new TradeInfo(table.getValueAt(table.getSelectedRow(), 0).toString(),"F1");
    			
    		}
    		else if (e.getKeyCode() == 50)// Check 2 Key press
    		{
    			TradeInfo tiobj=new TradeInfo(table.getValueAt(table.getSelectedRow(), 0).toString(),"F2");
    		}
    		else if (e.getKeyCode() == 51)// Check 3 Key press
    		{
    			TradeInfo tiobj=new TradeInfo(table.getValueAt(table.getSelectedRow(), 0).toString(),"F3");
    		}
    		else if (e.getKeyCode() == 52)// Check 4 Key press
    		{
    			TradeInfo tiobj=new TradeInfo(table.getValueAt(table.getSelectedRow(), 0).toString(),"F4");
    		}
    		else if (e.getKeyCode() == 53)// Check 5 Key press
    		{
    			TradeInfo tiobj=new TradeInfo(table.getValueAt(table.getSelectedRow(), 0).toString(),"F5");
    		}
    		else if (e.getKeyCode() == 84)// Check "T" Key press
    		{
    			SymbolMgmt symmgmt=new SymbolMgmt();
 
    		}
    		
    	}
    
    	public void tradeinfo(String tblname)
    	{
    		

			Connection conn = null; 
		    Statement stmt = null;
		int row = table.getSelectedRow();
		String selectedsymbol = table.getValueAt(row, 0).toString();

			
			try
			{
				JdbcDataSource ds = new JdbcDataSource();
				String to_print="";
		        ds.setURL("jdbc:h2:"+dbName);
		         conn = ds.getConnection(USER,PASS);
		         stmt = conn.createStatement();
		         stmt.execute("select ISSHOTSELL ,ENTRYTIME ,EXITTIME ,BUYPRICE ,SELLPRICE ,EXITCONDITION  from "+tblname+"  where SYMBOL ='"+selectedsymbol+"'");
		         ResultSet rs =stmt.getResultSet(); 
		         while (rs.next()) {
		    			to_print="IS SHOT SELL : " + rs.getBoolean("ISSHOTSELL")+"\n"+
 					"ENTRY FST : "+ rs.getString("ENTRYTIME")+"\n"+
 					"EXIT FST : "+ rs.getString("EXITTIME")+"\n"+
 					"EXIT CONDITION : "+ rs.getString("EXITCONDITION")+"\n"+
 					"BUY PRICE : "+ rs.getString("BUYPRICE") + ",  "+"SELL PRICE : "+ rs.getString("SELLPRICE")+"\n\n\n"+
 					"Details copied to clipboard !! ";   //printing text
		        	 	break;
		        
		         }
		         
		         if (rs != null) {
		                rs.close();
		            }
		         JOptionPane.showMessageDialog(frmOvviMarketBot,to_print,"Trade Info",JOptionPane.ERROR_MESSAGE);
			}
			catch(Exception ex)
			{
				
			}
			finally {
				try {
		            
		            if (stmt != null) {
		                stmt.close();
		            }
		            if (conn != null) {
		                conn.close();
		            }
		        } catch (SQLException ex) {
		            Logger.error("Ignored", ex);
		        }	
			}
    		
    		
    	}

    	@Override
    	public void keyReleased(KeyEvent e) {
    		
    	}

    }
	

	public class PLTableModel extends AbstractTableModel {

	        private List<Pldata> Pldata = new ArrayList<>(100);
	        private List<String> columnNames = new ArrayList<>(100);
	        
	        public void clear()
            {
            	Pldata.clear();
            }
	        
	        public void SumPL()
            {
	        		Connection conn = null; 
	        		Statement stmt = null;
	        		DecimalFormat f = new DecimalFormat("##.00");
	        		try
	        		{
	        			 JdbcDataSource ds = new JdbcDataSource();
	        	         ds.setURL(url);
	        	         conn = ds.getConnection(USER,PASS);
	       	         stmt = conn.createStatement();
	       	         stmt.execute("SELECT SUM(F1PL) as F1Sum, SUM(F2PL) as F2Sum, SUM(F3PL) as F3Sum, SUM(F4PL) as F4Sum, SUM(F5PL) as F5Sum\n" + 
	       	         		"FROM TBL_PLDATATABLE; ");
	       	         ResultSet rs =stmt.getResultSet(); 
	       	         rs.next();
	       	         if (rs !=null)
	       	         {
	       	        	 	f1PL.setText(Double.valueOf(f.format(rs.getDouble("F1Sum"))).toString());
                         if (Double.valueOf(f.format(rs.getDouble("F1Sum"))) < 0) 
                         {
                        	 	f1PL.setForeground(new Color(103,186,233));
                        	}       
                         else
                         {
                        	 f1PL.setForeground(new Color(255,220,135));
                         }
	       	        	 	f2PL.setText(Double.valueOf(f.format(rs.getDouble("F2Sum"))).toString());
	       	        	 	if (Double.valueOf(f.format(rs.getDouble("F2Sum")))<0)
                         {
	       	        	 		f2PL.setForeground(new Color(103,186,233));
                        	}  
	       	        	 	else
                         {
	       	        	 	f2PL.setForeground(new Color(255,220,135));
                         }
	       	        	 	f3PL.setText(Double.valueOf(f.format(rs.getDouble("F3Sum"))).toString());
	       	        	 	if (Double.valueOf(f.format(rs.getDouble("F3Sum")))<0)
                         {
	       	        	 		f3PL.setForeground(new Color(103,186,233));
                        	}  
	       	        	 	else
                         {
	       	        	 	f3PL.setForeground(new Color(255,220,135));
                         }
	       	        	 	f4PL.setText(Double.valueOf(f.format(rs.getDouble("F4Sum"))).toString());
	       	        	 	if (Double.valueOf(f.format(rs.getDouble("F4Sum")))<0)
                         {
	       	        	 		f4PL.setForeground(new Color(103,186,233));
                        	}  
	       	        	 	else
                         {
	       	        	 	f4PL.setForeground(new Color(255,220,135));
                         }
	       	        	 	f5PL.setText(Double.valueOf(f.format(rs.getDouble("F5Sum"))).toString());
	       	        	 	if (Double.valueOf(f.format(rs.getDouble("F5Sum")))<0)
                         {
	       	        	 		f5PL.setForeground(new Color(103,186,233));
                        	}  
	       	        	 	else
                         {
	       	        	 	f5PL.setForeground(new Color(255,220,135));
                         }
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
	        

	        @Override
	        public int getRowCount() {
	            return Pldata.size();
	        }

	        @Override
	        public int getColumnCount() {
	            return columnNames.size();
	        }

	        @Override
	        public String getColumnName(int column) {
	            return columnNames.get(column);
	        }

	        @Override
	        public Object getValueAt(int rowIndex, int columnIndex) {
	            Pldata rowValue = Pldata.get(rowIndex);
	            Object value = null;
	            switch (columnIndex) {
	                case 0:
	                    value = rowValue.getSymbol();
	                    break;
	                case 1:
	                    value = rowValue.getf1PL();
	                    break;
	                case 2:
	                    value = rowValue.getf1Percent();
	                    break;
	                case 3:
	                    value = rowValue.getf1TradeCount();
	                    break;
	                case 4:
	                    value = rowValue.getf2PL();
	                    break;
	                case 5:
	                    value = rowValue.getf2Percent();
	                    break;
	                case 6:
	                    value = rowValue.getf2TradeCount();
	                    break;
	                case 7:
	                    value = rowValue.getf3PL();
	                    break;
	                case 8:
	                    value = rowValue.getf3Percent();
	                    break;
	                case 9:
	                    value = rowValue.getf3TradeCount();
	                    break;
	                case 10:
	                    value = rowValue.getf4PL();
	                    break;
	                case 11:
	                    value = rowValue.getf4Percent();
	                    break;
	                case 12:
	                    value = rowValue.getf4TradeCount();
	                    break;
	                case 13:
	                    value = rowValue.getf5PL();
	                    break;
	                case 14:
	                    value = rowValue.getf5Percent();
	                    break;
	                case 15:
	                    value = rowValue.getf5TradeCount();
	                    break;
	                case 16:
	                    value = rowValue.getFirst();
	                    break;
	                case 17:
	                    value = rowValue.getLast();
	                    break;
	                
	                
	            }
	            return value;
	        }

	        public void refresh() throws SQLException {

	            List<String> values = new ArrayList<>(100);
	            
	            try (PreparedStatement ps = con.prepareStatement("select SYMBOL,F1PL as \"F1 P&L\"  ,F1PRECENT as \"F1 %\", F1TRADECOUNT as \"F1 COUNT\",F2PL as \"F2 P&L\" ,F2PRECENT as \"F2 %\",F2TRADECOUNT as \"F2 COUNT\",F3PL as \"F3 P&L\",F3PRECENT as \"F3 %\",F3TRADECOUNT as \"F3 COUNT\",F4PL as \"F4 P&L\",F4PRECENT as \"F4 %\",F4TRADECOUNT as \"F4 COUNT\",F5PL as \"F5 P&L\",F5PRECENT as \"F5 %\",F5TRADECOUNT as \"F5 COUNT\",  FIRST, LAST from TBL_PLDATATABLE order by ID ASC")) {
	                try (ResultSet rs = ps.executeQuery()) {
	                    ResultSetMetaData md = rs.getMetaData();
	                    for (int col = 0; col < md.getColumnCount(); col++) {
	                        values.add(md.getColumnLabel(col + 1));
	                    }
	                    while (rs.next()) {
	                    		Pldata list = new Pldata( rs.getString("SYMBOL"), rs.getDouble("F1PL"), rs.getDouble("F1PRECENT"),rs.getInt("F1TRADECOUNT")
	                    					  ,rs.getDouble("F2PL"), rs.getDouble("F2PRECENT"),rs.getInt("F2TRADECOUNT"), rs.getDouble("F3PL"), rs.getDouble("F3PRECENT"),rs.getInt("F3TRADECOUNT")
	                    					  , rs.getDouble("F4PL"), rs.getDouble("F4PRECENT"),rs.getInt("F4TRADECOUNT"), rs.getDouble("F5PL"), rs.getDouble("F5PRECENT"),rs.getInt("F5TRADECOUNT")
	                    					  , rs.getDouble("FIRST"), rs.getDouble("LAST"));
	                        Pldata.add(list);
	                    }
	                }
	            } finally {
	                if (columnNames.size() != values.size()) {
	                    columnNames = values;
	                    fireTableStructureChanged();
	                } else {
	                    fireTableDataChanged();
	                }
	            }

	        }

	        public class Pldata {

	            private long id;
	            private String symbol;
	            private int f1Trade,f2Trade,f3Trade,f4Trade, f5Trade;
	            private double f1PL,f1Percent,f2PL,f2Percent,f3PL,f3Percent,f4PL,f4Percent,f5PL,f5Percent,first,last;

	            public Pldata(String symbol, double f1PL, double f1Percent, int f1Trade, double f2PL, double f2Percent, int f2Trade, double f3PL, double f3Percent, int f3Trade, 
						double f4PL, double f4Percent, int f4Trade, double f5PL, double f5Percent, int f5Trade, double first,double last) {
	                //this.id = id;
	                this.symbol = symbol;
	                
	                this.f1PL = f1PL;
	                this.f1Percent = f1Percent;
	                this.f1Trade = f1Trade;
	                
	                this.f2PL = f2PL;
	                this.f2Percent = f2Percent;
	                this.f2Trade = f2Trade;
	                
	                this.f3PL = f3PL;
	                this.f3Percent = f3Percent;
	                this.f3Trade = f3Trade;
	                
	                this.f4PL = f4PL;
	                this.f4Percent = f4Percent;
	                this.f4Trade = f4Trade;
	                
	                this.f5PL = f5PL;
	                this.f5Percent = f5Percent;
	                this.f5Trade = f5Trade;
	                
	                this.first = first;
	                this.last = last;
	                		
	                		
	            }



			//public long getId() {return id;}
	           
	           public String getSymbol() {return symbol;}

	           public double getf1PL() {return f1PL;}
	           public double getf1Percent() {return f1Percent;}
	           public int getf1TradeCount() {return f1Trade;}
	           
	           public double getf2PL() {return f2PL;}
	           public double getf2Percent() {return f2Percent;}
	           public int getf2TradeCount() {return f2Trade;}
	           
	           public double getf3PL() {return f3PL;}
	           public double getf3Percent() {return f3Percent;}
	           public int getf3TradeCount() {return f3Trade;}
	           
	           public double getf4PL() {return f4PL;}
	           public double getf4Percent() {return f4Percent;}
	           public int getf4TradeCount() {return f4Trade;}
	           
	           public double getf5PL() {return f5PL;}
	           public double getf5Percent() {return f5Percent;}
	           public int getf5TradeCount() {return f5Trade;}
	           
	           public double getFirst() {return first;}
	           public double getLast() {return last;}
	    	
	    	}

	    }
}
