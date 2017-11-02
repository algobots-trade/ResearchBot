package Forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Dialog.ModalExclusionType;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.pmw.tinylog.*;

import commons.db_commons;
import javax.swing.JComboBox;




public class LogIn {

	private JFrame frmOvviResearchBot;
	private JPasswordField passwordField;
	public static String url = "jdbc:h2:"+System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
	public static String USER="admin", PASS="test123";
    public static String dbName= System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
    public JComboBox<String> researchtype;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogIn window = new LogIn();
					window.frmOvviResearchBot.setVisible(true);
				} catch (Exception ex) {
					Logger.error(ex);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LogIn() {
		Logger.info("Loding Login GUI..");
		initialize();
		Logger.info("Login GUI Loaded Completely..");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmOvviResearchBot = new JFrame();
		frmOvviResearchBot.setResizable(false);
		frmOvviResearchBot.setBackground(Color.BLACK);
		frmOvviResearchBot.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frmOvviResearchBot.setTitle("Ovvi Market Bot - Log In");
		frmOvviResearchBot.getContentPane().setBackground(Color.BLACK);
		frmOvviResearchBot.setBounds(100, 100, 558, 343);
		frmOvviResearchBot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmOvviResearchBot.getContentPane().setLayout(null);
		frmOvviResearchBot.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(System.getProperty("user.dir")+File.separator+"img"+File.separator+"logo.jpg"));
		lblNewLabel.setBounds(18, 52, 204, 229);
		frmOvviResearchBot.getContentPane().add(lblNewLabel);
		
		JLabel lblCopyrightInteractiveBrokers = new JLabel("©  2017 Copyright Ovvi Traders - Beta V.0.1");
		lblCopyrightInteractiveBrokers.setFont(new Font("Verdana", Font.PLAIN, 11));
		lblCopyrightInteractiveBrokers.setForeground(Color.WHITE);
		lblCopyrightInteractiveBrokers.setBounds(146, 293, 294, 16);
		frmOvviResearchBot.getContentPane().add(lblCopyrightInteractiveBrokers);
		
		JLabel lblOvviResearchBot = new JLabel("OVVI MARKET RESEARCH BOT");
		lblOvviResearchBot.setHorizontalAlignment(SwingConstants.LEFT);
		lblOvviResearchBot.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOvviResearchBot.setForeground(Color.WHITE);
		lblOvviResearchBot.setBounds(21, 6, 269, 40);
		frmOvviResearchBot.getContentPane().add(lblOvviResearchBot);
		
		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(285, 58, 238, 212);
		frmOvviResearchBot.getContentPane().add(panel);
		panel.setLayout(null);
		
		passwordField = new JPasswordField();
		passwordField.setForeground(Color.WHITE);
		passwordField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		passwordField.setBackground(Color.DARK_GRAY);
		passwordField.setBounds(21, 30, 199, 33);
		passwordField.setCaretColor(Color.WHITE);
		panel.add(passwordField);
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) 
			{
				if (e.getKeyChar()==KeyEvent.VK_ENTER)
				{
					validateLogin(new String(passwordField.getPassword()));
				}		
			}
		});
		passwordField.setToolTipText("Enter Piasscode & hit Enter");
		passwordField.setEchoChar('*');
		passwordField.setFont(new Font("Verdana", Font.BOLD, 20));
		
		JLabel lblPassCode = new JLabel("Enter The Passcode");
		lblPassCode.setBounds(21, 0, 184, 40);
		panel.add(lblPassCode);
		lblPassCode.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassCode.setForeground(Color.WHITE);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(21, 61, 211, 16);
		panel.add(separator);
		separator.setForeground(Color.WHITE);
		
		JButton btnGetIn = new JButton("Get In");
		btnGetIn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					validateLogin(new String(passwordField.getPassword()));
				}
				catch(Exception ex)
				{
					Logger.error(ex);
				}
			    finally
				{
				   
				}
			}	
		});
		btnGetIn.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnGetIn.setBackground(Color.BLACK);
		btnGetIn.setBounds(67, 166, 98, 40);
		panel.add(btnGetIn);
		
		JLabel lblResearchType = new JLabel("RESEARCH TYPE");
		lblResearchType.setForeground(Color.WHITE);
		lblResearchType.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblResearchType.setBounds(21, 79, 184, 40);
		panel.add(lblResearchType);
		
		researchtype = new JComboBox<String>();
		researchtype.setFont(new Font("Verdana", Font.PLAIN, 16));
		researchtype.setBounds(18, 114, 211, 40);
		researchtype.addItem("STK");
		researchtype.addItem("FUT");
		researchtype.addItem("OPT");
		researchtype.addItem("IND");
		//comboBox.setForeground(Color.WHITE);
//		researchtype.setBorder(javax.swing.BorderFactory.createEmptyBorder());
//		researchtype.setBackground(Color.DARK_GRAY);
		panel.add(researchtype);
		
//		JSeparator separator_1 = new JSeparator();
//		separator_1.setForeground(Color.WHITE);
//		separator_1.setBounds(16, 36, 507, 16);
//		frmOvviResearchBot.getContentPane().add(separator_1);
	}
	private void validateLogin(String passcode)
	{
		try
		{
			db_commons dbObj=new db_commons();
			int count = dbObj.getRowCount("select * from tbl_passcode where passcode='"+passcode.toString()+"'");
			if (count == 1)
			{
				Logger.info("Logged in Sucessfully !!");
				frmOvviResearchBot.dispose();
				ResearchDashboard mainwin=new ResearchDashboard(researchtype.getSelectedItem().toString());
			}
			else
			{
				Logger.warn("Wrong Passcode Attempt..");
			    JOptionPane.showMessageDialog(frmOvviResearchBot,"Invalid Passcode !!", "Authentication Violation",JOptionPane.WARNING_MESSAGE);
			}	
		}
		catch(Exception Ex)
		{
			Logger.error(Ex);
		}
		finally {
			
		}
	}
}
