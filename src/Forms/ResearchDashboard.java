package Forms;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.h2.jdbcx.JdbcDataSource;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.FileWriter;

import com.jgoodies.forms.factories.Borders;

import ApiConsumer.consumeIB;
import Forms.Home.Outerpane;
import Forms.Home.PLTableModel;
import Forms.Home.PLTableModel.Pldata;
import GUICommons.ClockLabel;
import GUICommons.DateValidator;
import commons.db_commons;

import java.awt.Font;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import java.awt.FlowLayout;
import javax.swing.BoxLayout;

public class ResearchDashboard {


	protected static final SwingUtilities SwingUtilties = null;
	PLTableModel model=null;
    JTable table=null;
	private File logfile;
	private Connection con;
	public static String url = "jdbc:h2:"+System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
	public static String USER="admin", PASS="test123";
    public static String dbName= System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
    int completedcount=0, totalscripscount=0;
    
	private JFrame frmOvviMarketBot;
	//private JRadioButton rdbtnStocks, rdbtnfutures;
	private JLabel lblStockTitle,lblrt,lblstrike,lblright;
	private ClockLabel timeLable;
	private ClockLabel dateLable;
	private ClockLabel dayLable;
	private JPanel gridpanel;
	private JPanel bottompanel;
	private JTextField txtdd, txtddFUT, txtddOPT,txtstrike;
	private JTextField txtMm, txtMmFUT,txtMmOPT ;
	private JTextField txtYy, txtYyFUT, txtYyOPT ;
	private JButton btnDcsv;
	private JButton btnRun;
	private JProgressBar progressInResearch;
	private JButton btnClear;
	private JPanel totalpanel;
	private Label lbltotal;
	private Label f1PL,f2PL,f3PL,f4PL,f5PL;
	private JPanel mainTable;
	private JScrollPane scrollPane; 
	private static String strResearchType; 
	private JComboBox<String> drpright;
	String strMarkettype="STK";
	String strStockExchange="NSE";
	String strStockCurrency="INR";
	String whichData = "TRADES";
	String strExpiridate="", strRightval="";
	String	strStrikeVal="0.0"; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResearchDashboard window = new ResearchDashboard("IND");
					window.frmOvviMarketBot.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ResearchDashboard(String researchType) 
	{
		try {
			strResearchType = researchType;
			initialize();
			frmOvviMarketBot.setVisible(true);
			Logger.info("==============RESEARCH FOR :"+researchType+"=============");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	private void initialize() throws SQLException {
		 try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			
			e1.printStackTrace();
		}
		
			//KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
			//manager.addKeyEventDispatcher( new KeyDispatcher() );
			JdbcDataSource ds = new JdbcDataSource();
			
         ds.setURL(url);
	        con = ds.getConnection(USER,PASS);
		 
		frmOvviMarketBot = new JFrame();
		
		frmOvviMarketBot.getContentPane().setForeground(Color.GREEN);
		frmOvviMarketBot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmOvviMarketBot.setBackground(new Color(36,34,29));
		//frmOvviMarketBot.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frmOvviMarketBot.setSize(500, 600);
		//frmOvviMarketBot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frmOvviMarketBot.pack();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    double width = screenSize.getWidth();
	    double height = screenSize.getHeight();
	    frmOvviMarketBot.setSize((int)width, (int)height);
	    
		JPanel toppanel = new JPanel();
		frmOvviMarketBot.getContentPane().add(toppanel, BorderLayout.NORTH);
		toppanel.setLayout(new BorderLayout(0, 0));
		toppanel.setBackground(new Color(36,34,29));
		toppanel.setOpaque(false);
		
		JPanel topleft = new JPanel();
		toppanel.add(topleft, BorderLayout.WEST);
		topleft.setBackground(new Color(36,34,29));
		lblStockTitle = new JLabel();
		if (strResearchType =="FUT")
		{
			JPanel innertopleft = new JPanel();
			innertopleft.setLayout(new BorderLayout(0, 0));
			innertopleft.setBackground(new Color(36,34,29));
			innertopleft.setOpaque(false);
			
			lblStockTitle.setText("FUTURE RESEARCH");
			lblrt = new JLabel("Expiration Date");
			lblrt.setHorizontalAlignment(SwingConstants.CENTER);
	 	    //lblStockTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblrt.setFont(new Font("Tsukushi A Round Gothic", Font.BOLD, 18));
			lblrt.setForeground(Color.WHITE);
			innertopleft.add(lblrt, BorderLayout.NORTH);
			
			txtddFUT = new JTextField();
			//txtdd.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
			txtddFUT.setForeground(new Color(255, 255, 255));
			txtddFUT.setBackground(new Color(80, 75, 78));
			txtddFUT.setHorizontalAlignment(SwingConstants.CENTER);
			txtddFUT.setPreferredSize(new Dimension(60,30));
			txtddFUT.setText("DD");
			//txtdd.setBounds(10, 886, 47, 47);
			txtddFUT.setCaretColor(Color.WHITE);
			innertopleft.add(txtddFUT, BorderLayout.WEST);
			txtddFUT.setColumns(4);
			txtddFUT.addFocusListener(new FocusListener() {
			    public void focusGained(FocusEvent e) {
			    	txtddFUT.setText("");
			    }

			    public void focusLost(FocusEvent e) {
			    	    if(txtddFUT.getText().equals(""))
			    	    {
			    	    	txtddFUT.setText("DD");
			    	    }
			        // nothing
			    }
			});
			
			txtMmFUT = new JTextField();
			//txtMm.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
			txtMmFUT.setText("MM");
			txtMmFUT.setHorizontalAlignment(SwingConstants.CENTER);
			txtMmFUT.setForeground(Color.WHITE);
			txtMmFUT.setColumns(4);
			txtMmFUT.setPreferredSize(new Dimension(60,30));
			txtMmFUT.setBackground(new Color(80, 75, 78));
			//txtMm.setBounds(60, 886, 47, 47);
			txtMmFUT.setCaretColor(Color.WHITE);
			innertopleft.add(txtMmFUT, BorderLayout.CENTER);
			txtMmFUT.addFocusListener(new FocusListener() {
			    public void focusGained(FocusEvent e) {
			    	txtMmFUT.setText("");
			    }

			    public void focusLost(FocusEvent e) {
			    	    if(txtMmFUT.getText().equals(""))
			    	    {
			    	    		txtMmFUT.setText("MM");
			    	    }
			        // nothing
			    }
			});
			
			
			
			txtYyFUT = new JTextField();
			txtYyFUT.setText("YYYY");
			txtYyFUT.setHorizontalAlignment(SwingConstants.CENTER);
			txtYyFUT.setForeground(Color.WHITE);
			txtYyFUT.setColumns(6);
			txtYyFUT.setPreferredSize(new Dimension(80,30));
			
			txtYyFUT.setBackground(new Color(80, 75, 78));
			//txtYy.setBounds(110, 886, 67, 47);
			txtYyFUT.setCaretColor(Color.WHITE);
			innertopleft.add(txtYyFUT,BorderLayout.EAST);
			txtYyFUT.addFocusListener(new FocusListener() {
			    public void focusGained(FocusEvent e) {
			    	txtYyFUT.setText("");
			    }

			    public void focusLost(FocusEvent e) {
			    	    if(txtYyFUT.getText().equals(""))
			    	    {
			    	    	txtYyFUT.setText("YYYY");
			    	    }
			        // nothing
			    }
			});
			topleft.add(innertopleft);
		}
		else if (strResearchType =="OPT")
		{
			lblStockTitle.setText("OPTION'S RESEARCH");
			JPanel innertopleftfull = new JPanel();
			innertopleftfull.setLayout(new BorderLayout(0, 0));
			innertopleftfull.setBackground(new Color(36,34,29));
			innertopleftfull.setOpaque(false);
			
			JPanel innertopleft = new JPanel();
			innertopleft.setLayout(new BorderLayout(0, 0));
			innertopleft.setBackground(new Color(36,34,29));
			innertopleft.setOpaque(false);
			
			lblrt = new JLabel("Expiration Date");
			lblrt.setHorizontalAlignment(SwingConstants.CENTER);
	 	    //lblStockTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lblrt.setFont(new Font("Tsukushi A Round Gothic", Font.BOLD, 18));
			lblrt.setForeground(Color.WHITE);
			innertopleft.add(lblrt, BorderLayout.NORTH);
			
			txtddOPT = new JTextField();
			//txtdd.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
			txtddOPT.setForeground(new Color(255, 255, 255));
			txtddOPT.setBackground(new Color(80, 75, 78));
			txtddOPT.setHorizontalAlignment(SwingConstants.CENTER);
			txtddOPT.setPreferredSize(new Dimension(60,30));
			txtddOPT.setText("DD");
			//txtdd.setBounds(10, 886, 47, 47);
			txtddOPT.setCaretColor(Color.WHITE);
			innertopleft.add(txtddOPT, BorderLayout.WEST);
			txtddOPT.setColumns(4);
			txtddOPT.addFocusListener(new FocusListener() {
			    public void focusGained(FocusEvent e) {
			    	txtddOPT.setText("");
			    }

			    public void focusLost(FocusEvent e) {
			    	    if(txtddOPT.getText().equals(""))
			    	    {
			    	    	txtddOPT.setText("DD");
			    	    }
			        // nothing
			    }
			});
			
			txtMmOPT = new JTextField();
			//txtMm.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
			txtMmOPT.setText("MM");
			txtMmOPT.setHorizontalAlignment(SwingConstants.CENTER);
			txtMmOPT.setForeground(Color.WHITE);
			txtMmOPT.setColumns(4);
			txtMmOPT.setPreferredSize(new Dimension(60,30));
			txtMmOPT.setBackground(new Color(80, 75, 78));
			//txtMm.setBounds(60, 886, 47, 47);
			txtMmOPT.setCaretColor(Color.WHITE);
			innertopleft.add(txtMmOPT, BorderLayout.CENTER);
			txtMmOPT.addFocusListener(new FocusListener() {
			    public void focusGained(FocusEvent e) {
			    	txtMmOPT.setText("");
			    }

			    public void focusLost(FocusEvent e) {
			    	    if(txtMmOPT.getText().equals(""))
			    	    {
			    	    	txtMmOPT.setText("MM");
			    	    }
			        // nothing
			    }
			});
			
			
			
			txtYyOPT = new JTextField();
			txtYyOPT.setText("YYYY");
			txtYyOPT.setHorizontalAlignment(SwingConstants.CENTER);
			txtYyOPT.setForeground(Color.WHITE);
			txtYyOPT.setColumns(6);
			txtYyOPT.setPreferredSize(new Dimension(80,30));
			
			txtYyOPT.setBackground(new Color(80, 75, 78));
			//txtYy.setBounds(110, 886, 67, 47);
			txtYyOPT.setCaretColor(Color.WHITE);
			innertopleft.add(txtYyOPT,BorderLayout.EAST);
			txtYyOPT.addFocusListener(new FocusListener() {
			    public void focusGained(FocusEvent e) {
			    	txtYyOPT.setText("");
			    }

			    public void focusLost(FocusEvent e) {
			    	    if(txtYyOPT.getText().equals(""))
			    	    {
			    	    	txtYyOPT.setText("YYYY");
			    	    }
			        // nothing
			    }
			});
			
			innertopleftfull.add(innertopleft,BorderLayout.WEST);
			
			JPanel innertopcenter = new JPanel();
			innertopcenter.setLayout(new BorderLayout(0, 0));
			innertopcenter.setBackground(new Color(36,34,29));
			innertopcenter.setOpaque(false);
			
			
			lblstrike = new JLabel("     Strike Rate  ");
			lblstrike.setHorizontalAlignment(SwingConstants.LEFT);
			lblstrike.setFont(new Font("Tsukushi A Round Gothic", Font.BOLD, 18));
			lblstrike.setForeground(Color.WHITE);
			innertopcenter.add(lblstrike,BorderLayout.NORTH);
			
			txtstrike = new JTextField();
			txtstrike.setText("0");
			txtstrike.setHorizontalAlignment(SwingConstants.CENTER);
			txtstrike.setForeground(Color.WHITE);		
			txtstrike.setColumns(5);
			txtstrike.setBackground(new Color(80, 75, 78));
			txtstrike.setPreferredSize(new Dimension(50,30));
			
			txtstrike.addFocusListener(new FocusListener() {
			    public void focusGained(FocusEvent e) {
			    	txtstrike.setText("");
			    }

			    public void focusLost(FocusEvent e) {
			    	    if(txtstrike.getText().equals(""))
			    	    {
			    	    	txtstrike.setText("0");
			    	    }
			        // nothing
			    }
			});
			
			JLabel lbldummy = new JLabel("    ");
			innertopcenter.add(lbldummy,BorderLayout.WEST);
			innertopcenter.add(txtstrike,BorderLayout.CENTER);
			innertopleftfull.add(innertopcenter,BorderLayout.CENTER);
			topleft.add(innertopleftfull);
			
		
			JPanel innertopeast = new JPanel();
			innertopeast.setLayout(new BorderLayout(0, 0));
			innertopeast.setBackground(new Color(36,34,29));
			innertopeast.setOpaque(false);
			
			lblright = new JLabel("     Right  ");
			lblright.setHorizontalAlignment(SwingConstants.LEFT);
			lblright.setFont(new Font("Tsukushi A Round Gothic", Font.BOLD, 18));
			lblright.setForeground(Color.WHITE);
			innertopeast.add(lblright,BorderLayout.NORTH);
			
			JLabel lbldummy1 = new JLabel("    ");
			innertopeast.add(lbldummy1,BorderLayout.WEST);
			
			drpright=new JComboBox<String>();
			drpright.setFont(new Font("Verdana", Font.PLAIN, 16));
			drpright.setBounds(18, 114, 211, 40);
			drpright.addItem("CALL");
			drpright.addItem("PUT");
			innertopeast.add(drpright,BorderLayout.CENTER);
			
			
			topleft.add(innertopeast,BorderLayout.EAST);
			
		}
		else if (strResearchType =="IND")
		{
			lblStockTitle.setText("                        INDEX RESEARCH");
		}
		else if (strResearchType == "STK")
		{
			lblStockTitle.setText("                        STOCK RESEARCH");
		}
		 
		JPanel topcenter = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) topcenter.getLayout();
	    toppanel.add(topcenter, BorderLayout.CENTER);
	    topcenter.setBackground(new Color(36,34,29));
		 
		
		
		lblStockTitle.setHorizontalAlignment(SwingConstants.CENTER);
 	    //lblStockTitle.setHorizontalAlignment(SwingConstants.CENTER);
 	    lblStockTitle.setFont(new Font("Tsukushi A Round Gothic", Font.BOLD, 26));
 	    lblStockTitle.setForeground(new Color(240,159,108));
 	    
 	    
 	    topcenter.add(lblStockTitle);
 	    
 	    JPanel topright = new JPanel();
 	    topright.setLayout(new BorderLayout(5, 0));
		toppanel.add(topright, BorderLayout.EAST);
		topright.setBackground(new Color(36,34,29));
 	    
 	    dateLable = new ClockLabel("date");
		dateLable.setForeground(new Color(210, 180, 140));
	    timeLable = new ClockLabel("time");
	    timeLable.setFont(new Font("American Typewriter", Font.PLAIN, 24));
	    
	    dayLable = new ClockLabel("day");
	    dayLable.setForeground(new Color(210, 180, 140));
	    
	    JPanel toprightcenter = new JPanel();
	    toprightcenter.setBackground(new Color(36,34,29));
	    toprightcenter.setLayout(new BorderLayout(0, 0));
	    topright.add(toprightcenter, BorderLayout.CENTER);
	    toprightcenter.add(timeLable,BorderLayout.CENTER);
	    
	    
 	    JPanel toprightright = new JPanel();
 	    toprightright.setBackground(new Color(36,34,29));
	    toprightright.setLayout(new BorderLayout(2, 2));
	    topright.add(toprightright, BorderLayout.EAST);	    
	    toprightright.add(dateLable,BorderLayout.EAST);	
	    toprightright.add(dayLable,BorderLayout.SOUTH);	
	    
	    gridpanel = new JPanel();
	    gridpanel.setBackground(new Color(36,34,29));
	    frmOvviMarketBot.getContentPane().add(gridpanel, BorderLayout.CENTER);
	    
	    bottompanel = new JPanel();
	    bottompanel.setLayout(new BorderLayout(2, 2));
	    bottompanel.setBackground(new Color(36,34,29));
	    frmOvviMarketBot.getContentPane().add(bottompanel, BorderLayout.SOUTH);
	    
	    JPanel datepanel = new JPanel();
	    bottompanel.add(datepanel, BorderLayout.WEST);
		datepanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		datepanel.setBackground(new Color(36,34,29));
	    

		txtdd = new JTextField();
		//txtdd.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		txtdd.setForeground(new Color(255, 255, 255));
		txtdd.setBackground(new Color(80, 75, 78));
		txtdd.setHorizontalAlignment(SwingConstants.CENTER);
		txtdd.setPreferredSize(new Dimension(60,50));
		txtdd.setText("DD");
		//txtdd.setBounds(10, 886, 47, 47);
		txtdd.setCaretColor(Color.WHITE);
		datepanel.add(txtdd);
		txtdd.setColumns(4);
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
		//txtMm.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		txtMm.setText("MM");
		txtMm.setHorizontalAlignment(SwingConstants.CENTER);
		txtMm.setForeground(Color.WHITE);
		txtMm.setColumns(4);
		txtMm.setPreferredSize(new Dimension(60,50));
		txtMm.setBackground(new Color(80, 75, 78));
		//txtMm.setBounds(60, 886, 47, 47);
		txtMm.setCaretColor(Color.WHITE);
		datepanel.add(txtMm);
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
		txtYy.setColumns(6);
		txtYy.setPreferredSize(new Dimension(80,50));
		
		txtYy.setBackground(new Color(80, 75, 78));
		//txtYy.setBounds(110, 886, 67, 47);
		txtYy.setCaretColor(Color.WHITE);
		datepanel.add(txtYy);
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
	    
		JPanel buttonpanel = new JPanel();
	    bottompanel.add(buttonpanel, BorderLayout.CENTER);
	    buttonpanel.setBackground(new Color(36,34,29));
	    
	    db_commons dbobj=new db_commons();
	    int progresscount = dbobj.getRowCount("select * from tbl_pldatatable;");
		progressInResearch = new JProgressBar(0,progresscount);
		progressInResearch.setBackground(Color.WHITE);
		//progressInResearch.setStringPainted(true);
		progressInResearch.setBounds(636, 878, 453, 10);
		progressInResearch.setForeground(Color.GREEN);
		bottompanel.add(progressInResearch,BorderLayout.SOUTH);
		buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

		
		
		btnRun = new JButton("RUN");
		//btnRun.setBounds(636, 898, 181, 35);
		btnRun.setPreferredSize(new Dimension(180, 50));
		buttonpanel.add(btnRun);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{    					
				try
				{
					DateValidator dv=new DateValidator();
					String strStartDate =txtYy.getText()+txtMm.getText()+txtdd.getText();					
					Boolean isvalidationpassed = false;
					if (strResearchType=="STK")
					{
						 strMarkettype="STK";
						 Boolean isValidDate = dv.isThisDateValid(strStartDate, "yyyyMMdd");
						 Boolean isNotWeekend = dv.isThisDateWeekend(strStartDate, "yyyyMMdd");
						 if ((isValidDate)&&(isNotWeekend))
						 {
							 isvalidationpassed = true;
						 }
						 else 
						 {
							 JOptionPane.showMessageDialog(frmOvviMarketBot,"Invalid Feed Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
							  txtdd.requestFocus();
							  isvalidationpassed = false;
						 }
						 
					}
					else if (strResearchType=="FUT")
					{
						 strMarkettype="FUT";
						 int validation =0;
						 Boolean isValidDate = dv.isThisDateValid(strStartDate, "yyyyMMdd");
						 Boolean isNotWeekend = dv.isThisDateWeekend(strStartDate, "yyyyMMdd");						 
						 if ((isValidDate)&&(isNotWeekend))
						 {
							 validation = validation+1;
						 }
						 else 
						 {
							 JOptionPane.showMessageDialog(frmOvviMarketBot,"Invalid Feed Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
							  txtdd.requestFocus();
							  isvalidationpassed = false;
						 }
						 strExpiridate = txtYyFUT.getText()+txtMmFUT.getText()+txtddFUT.getText();
						 Boolean isValidDateexp = dv.isThisDateValid(strExpiridate, "yyyyMMdd");
						 Boolean isNotWeekendexp = dv.isThisDateWeekend(strExpiridate, "yyyyMMdd");
						 if ((isValidDateexp)&&(isNotWeekendexp))
						 {
							 validation = validation+1;
						 }
						 else 
						 {
							 JOptionPane.showMessageDialog(frmOvviMarketBot,"Invalid Expiration Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
							  txtddFUT.requestFocus();
							  isvalidationpassed = false;
						 }
						 if (validation==2)
						 {
							 isvalidationpassed = true;
						 }
						 else
						 {
							 isvalidationpassed = false;
						 }
							 						 
					}
					else if (strResearchType=="OPT")
					{
						 strMarkettype="OPT";
						 int validation =0;
						 Boolean isValidDate = dv.isThisDateValid(strStartDate, "yyyyMMdd");
						 Boolean isNotWeekend = dv.isThisDateWeekend(strStartDate, "yyyyMMdd");						 
						 if ((isValidDate)&&(isNotWeekend))
						 {
							 validation = validation+1;
						 }
						 else 
						 {
							 JOptionPane.showMessageDialog(frmOvviMarketBot,"Invalid Feed Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
							  txtdd.requestFocus();
							  isvalidationpassed = false;
						 }
						 strExpiridate = txtYyOPT.getText()+txtMmOPT.getText()+txtddOPT.getText();
						 Boolean isValidDateexp = dv.isThisDateValid(strExpiridate, "yyyyMMdd");
						 Boolean isNotWeekendexp = dv.isThisDateWeekend(strExpiridate, "yyyyMMdd");
						 if ((isValidDateexp)&&(isNotWeekendexp))
						 {
							 validation = validation+1;
						 }
						 else 
						 {
							 JOptionPane.showMessageDialog(frmOvviMarketBot,"Invalid Expiration Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
							  txtddFUT.requestFocus();
							  isvalidationpassed = false;
						 }
						 if ((Double.parseDouble(txtstrike.getText()) > 0))
						 {
							 validation = validation+1;
						 }
						 else
						 {
							 JOptionPane.showMessageDialog(frmOvviMarketBot,"Invalid Strike Data !!","Alert",JOptionPane.ERROR_MESSAGE);
							  txtstrike.requestFocus();
							  isvalidationpassed = false;
						 }
						 if (validation==3)
						 {
							 strStrikeVal = txtstrike.getText();
							 strRightval = drpright.getSelectedItem().toString();
							 isvalidationpassed = true;
						 }
						 else
						 {
							 isvalidationpassed = false;
						 }
						 
					}
					else if (strResearchType=="IND")
					{
						 strMarkettype="IND";
						 Boolean isValidDate = dv.isThisDateValid(strStartDate, "yyyyMMdd");
						 Boolean isNotWeekend = dv.isThisDateWeekend(strStartDate, "yyyyMMdd");
						 if ((isValidDate)&&(isNotWeekend))
						 {
							 isvalidationpassed = true;
						 }
						 else 
						 {
							 JOptionPane.showMessageDialog(frmOvviMarketBot,"Invalid Feed Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
							  txtdd.requestFocus();
							  isvalidationpassed = false;
						 }
					}
				   
					
					
					Thread mainengine = null;
					if ((!isvalidationpassed))
					{
						JOptionPane.showMessageDialog(frmOvviMarketBot,"Invalid Inputs !!","Alert",JOptionPane.ERROR_MESSAGE);
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
	        						String [] contract ={instrument,strMarkettype, strStockCurrency,strStockExchange};
	        						if (strResearchType=="STK")
	        						{
	        							 contract = new String[]{instrument,strMarkettype, strStockCurrency,strStockExchange};
	        						}
	        						else if (strResearchType=="FUT")
	        						{
	        							contract = new String[]{instrument,strMarkettype, strStockCurrency,strStockExchange,strExpiridate};
	        						}
	        						else if (strResearchType=="OPT")
	        						{
	        							contract = new String[]{instrument,strMarkettype, strStockCurrency,strStockExchange,strExpiridate,strStrikeVal,strRightval};
	        						}
	        						else if (strResearchType=="IND")
	        						{
	        							contract = new String[]{instrument,strMarkettype, strStockCurrency,strStockExchange};
	        						}
	        						try 
	        						{
	        							String [] f1results =  ibconnect.calculateF1PL(contract,startDateTime,whichData,strResearchType);
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
//					if (completedcount == (totalscripscount -1))
//					{
//						btnRun.setText("Run");
//						btnRun.setEnabled(true);
//					}

					
				}
				catch(Exception ex)
				{
					Logger.error(ex);
				}
			}
		});
		
		
	    
		JPanel clearpanel = new JPanel();
		clearpanel.setBackground(new Color(36,34,29));
		clearpanel.setLayout(new BorderLayout(0, 0));
	    bottompanel.add(clearpanel, BorderLayout.EAST);
	    
	    btnDcsv = new JButton("D-CSV");
		btnDcsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try
				{
					db_commons dbobj=new db_commons();
					String [][] recs = dbobj.getMultiColumnRecords("Select * from TBL_PLDATATABLE");
					dCSV(recs);	
					JOptionPane.showMessageDialog(frmOvviMarketBot,"Research Data Exported Successfully !!","Date Exported",JOptionPane.INFORMATION_MESSAGE);
					
				}
				catch(Exception ex)
				{
					
				}
				finally
				{
					
				}
								
			}
		});
		
		//btnDcsv.setBounds(926, 900, 163, 35);
		clearpanel.add(btnDcsv,BorderLayout.WEST);
	    
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
		//btnClear.setBounds(1567, 886, 87, 47);
		clearpanel.add(btnClear);
		gridpanel.setLayout(new BorderLayout(0, 0));
        
		
		/////////////////TOTAL PANEL
		
		totalpanel = new JPanel();
		totalpanel.setBounds(10, 830, 1680, 35);
		gridpanel.add(totalpanel,BorderLayout.SOUTH);
		totalpanel.setBackground(new Color(61, 57, 54));
		totalpanel.setPreferredSize(new Dimension((int)width, 40));
		totalpanel.setLayout(null);
		
		
		lbltotal = new Label("TOTAL");
		lbltotal.setBounds(0, 0, 42, 40);
		lbltotal.setAlignment(Label.LEFT);
		lbltotal.setForeground(Color.WHITE);
		totalpanel.add(lbltotal);
		
		f1PL = new Label("200.00");
		f1PL.setSize(119, 30);
		f1PL.setLocation(200, 5);
		//f1PL.setBounds(0, 0, 0, 0);
		f1PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f1PL.setForeground(new Color(255,220,135));
		f1PL.setAlignment(Label.CENTER);
		totalpanel.add(f1PL);
		
		f2PL = new Label("200.00");
		f2PL.setSize(119, 30);
		f2PL.setLocation(400, 5);
		//f2PL.setBounds(0, 0, 0, 0);
		f2PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f2PL.setForeground(new Color(255,220,135));
		f2PL.setAlignment(Label.CENTER);
		totalpanel.add(f2PL);
		
		f3PL = new Label("200.00");
		//f3PL.setBounds(0, 0, 0, 0);
		f3PL.setSize(119, 30);
		f3PL.setLocation(660, 5);
		f3PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f3PL.setForeground(new Color(255,220,135));
		f3PL.setAlignment(Label.CENTER);
		totalpanel.add(f3PL);
		
		f4PL = new Label("200.00");
		f4PL.setSize(119, 30);
		f4PL.setLocation(950, 5);
		f4PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f4PL.setForeground(new Color(255,220,135));
		f4PL.setAlignment(Label.CENTER);
		totalpanel.add(f4PL);
		
		f5PL = new Label("200.00");
		f5PL.setSize(119, 30);
		f5PL.setLocation(1280, 5);
		f5PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f5PL.setForeground(new Color(255,220,135));
		f5PL.setAlignment(Label.CENTER);
		totalpanel.add(f5PL);
		
		mainTable = new JPanel();
		gridpanel.add(mainTable);
		mainTable.setBackground(new Color(36,34,29));
		
		
		//db_commons 
		dbobj=new db_commons();
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
		    	    		//grid.setBounds(10,50, 1664, 780);
		    	    		//grid.setBorder(null);
	        		   // grid.setBackground(new Color(80,75,78));
	        		    grid.setLayout(new FlowLayout());
	        		    grid.setBackground(new Color(36,34,29));
	        		    //grid.setPreferredSize(new Dimension(1670, 790));
	        		    
	        		    mainTable.add(grid);
	        		    mainTable.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		    	    		//frmOvviMarketBot.getContentPane().add(grid);
	    	    	
             }
            });
		
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
				table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
				//table.setBackground(new Color(36,34,29));
				
				
				JTableHeader header = table.getTableHeader();
			    header.setBackground(new Color(36,34,29));
			    header.setForeground(new Color(255,220,135));
			    header.setFont(new Font("Tahoma", Font.PLAIN, 14));
			    
			    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			    double width = frmOvviMarketBot.getWidth();
			    double height = frmOvviMarketBot.getHeight();
			    
			    scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setEnabled(false);
				scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
				//scrollPane.setViewportBorder(null);
				//scrollPane.setBorder(null);
				scrollPane.setPreferredSize(new Dimension((int)width-25, (int)height-220));
				scrollPane.setBackground(new Color(36,34,29));
				
				//scrollPane.setPreferredSize(new Dimension((int)width-500, (int)height-500));
	            
	            add(scrollPane,BorderLayout.CENTER);

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
