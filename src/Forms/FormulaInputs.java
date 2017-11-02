package Forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.pmw.tinylog.Logger;

import commons.FormulaData;
import commons.db_commons;

import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class FormulaInputs {

	private JFrame contentPane;
	private JTextField txtX;
	private JTextField txtY;
	private JTextField txtZ;
	private JTextField txtT1H;
	private JTextField txtT2H;
	private JTextField txtT3H;
	private JTextField txtT4H;
	private JTextField txtT1M;
	private JTextField txtT1S;
	private JTextField txtT2M;
	private JTextField txtT2S;
	private JTextField txtT3M;
	private JTextField txtT3S;
	private JTextField txtT4M;
	private JTextField txtT4S;
	private JTextField txtLcount;
	private JTextField txtStopL;
	private JLabel lblFTitle;
	private db_commons dbObj;
	public static String dbName= System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
    public static String USER="admin", PASS="test123";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormulaInputs window = new FormulaInputs("Invalid");
					window.contentPane.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FormulaInputs(String Fname) {
		initialize(Fname);
		List<FormulaData> fdata=new ArrayList<FormulaData>();  
		try {
			dbObj=new db_commons();
			int count = dbObj.getRowCount("select * from TBL_FORMULA where  fname='"+lblFTitle.getText().toString()+"'");
			if (count !=0)
			{
				fdata = dbObj.getFormulaData("select * from TBL_FORMULA where fname='"+lblFTitle.getText().toString()+"'");
				for(FormulaData f : fdata){
					txtX.setText(String.valueOf(((f.getX() == 0.0) ? "" : f.getX())));
					txtY.setText(String.valueOf(((f.getY() == 0.0) ? "" : f.getY())));
					txtZ.setText(String.valueOf(((f.getZ() == 0.0) ? "" : f.getZ())));
					txtLcount.setText(String.valueOf(((f.getLcount() == 0.0) ? "" : f.getLcount())));
					txtStopL.setText(String.valueOf(((f.getStopL() == 0.0) ? "" : f.getStopL())));
					
					txtT1H.setText(String.valueOf(((f.getT1() == null) ? "" : f.getT1().split(":")[0])));
					txtT1M.setText(String.valueOf(((f.getT1() == null) ? "" : f.getT1().split(":")[1])));
					txtT1S.setText(String.valueOf(((f.getT1() == null) ? "" : f.getT1().split(":")[2])));
					
					txtT2H.setText(String.valueOf(((f.getT2() == null) ? "" : f.getT2().split(":")[0])));
					txtT2M.setText(String.valueOf(((f.getT2() == null) ? "" : f.getT2().split(":")[1])));
					txtT2S.setText(String.valueOf(((f.getT2() == null) ? "" : f.getT2().split(":")[2])));
					
					txtT3H.setText(String.valueOf(((f.getT3() == null) ? "" : f.getT3().split(":")[0])));
					txtT3M.setText(String.valueOf(((f.getT3() == null) ? "" : f.getT3().split(":")[1])));
					txtT3S.setText(String.valueOf(((f.getT3() == null) ? "" : f.getT3().split(":")[2])));
					
					txtT4H.setText(String.valueOf(((f.getT4() == null) ? "" : f.getT4().split(":")[0])));
					txtT4M.setText(String.valueOf(((f.getT4() == null) ? "" : f.getT4().split(":")[1])));
					txtT4S.setText(String.valueOf(((f.getT4() == null) ? "" : f.getT4().split(":")[2])));
					
					break;
		        }
				
			}
		}
		catch(Exception ex) {
		}
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String Fname) {
		InputHandler keyhand = new InputHandler();
		contentPane = new JFrame();
		contentPane.getContentPane().setBackground(new Color(51, 51, 51));
		contentPane.setVisible(true);
		contentPane.setTitle("Ovvi - Formula Input");
		contentPane.setBounds(100, 100, 410, 635);
		contentPane.setBackground(new Color(36,34,29));
		contentPane.getContentPane().setLayout(null);
		contentPane.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane.addKeyListener(keyhand);
		

		lblFTitle = new JLabel(Fname);
		lblFTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblFTitle.setFont(new Font("Verdana", Font.BOLD, 22));
		lblFTitle.setForeground(new Color(255, 220, 135));
		lblFTitle.setBounds(6, 6, 398, 43);
		contentPane.getContentPane().add(lblFTitle);
		
		JPanel pnlInput = new JPanel();
		pnlInput.setForeground(Color.WHITE);
		pnlInput.setBounds(28, 47, 347, 549);
		pnlInput.setBackground(new Color(80,75,78));
		contentPane.getContentPane().add(pnlInput);
		pnlInput.setLayout(null);
		
		JLabel lblX = new JLabel("X  =");
		lblX.setBounds(99, 26, 73, 49);
		lblX.setHorizontalAlignment(SwingConstants.LEFT);
		lblX.setForeground(Color.WHITE);
		lblX.setFont(new Font("Verdana", Font.PLAIN, 22));
		pnlInput.add(lblX);
		
		JLabel lblX_1 = new JLabel("Y  =");
		lblX_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblX_1.setForeground(Color.WHITE);
		lblX_1.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblX_1.setBounds(99, 76, 65, 49);
		pnlInput.add(lblX_1);
		
		JLabel lblZ = new JLabel("Z  =");
		lblZ.setHorizontalAlignment(SwingConstants.LEFT);
		lblZ.setForeground(Color.WHITE);
		lblZ.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblZ.setBounds(97, 126, 59, 49);
		pnlInput.add(lblZ);
		
		JLabel lblT = new JLabel("T1  =");
		lblT.setHorizontalAlignment(SwingConstants.LEFT);
		lblT.setForeground(Color.WHITE);
		lblT.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT.setBounds(83, 181, 81, 49);
		pnlInput.add(lblT);
		
		JLabel lblT_1 = new JLabel("T2  =");
		lblT_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblT_1.setForeground(Color.WHITE);
		lblT_1.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT_1.setBounds(83, 235, 81, 45);
		pnlInput.add(lblT_1);
		
		JLabel lblT_2 = new JLabel("T3  =");
		lblT_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblT_2.setForeground(Color.WHITE);
		lblT_2.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT_2.setBounds(83, 284, 81, 49);
		pnlInput.add(lblT_2);
		
		JLabel lblT_3 = new JLabel("T4  =");
		lblT_3.setHorizontalAlignment(SwingConstants.LEFT);
		lblT_3.setForeground(Color.WHITE);
		lblT_3.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT_3.setBounds(83, 331, 81, 49);
		pnlInput.add(lblT_3);
		
		JLabel lblLcount = new JLabel("LCOUNT  =");
		lblLcount.setHorizontalAlignment(SwingConstants.LEFT);
		lblLcount.setForeground(Color.WHITE);
		lblLcount.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblLcount.setBounds(22, 392, 140, 49);
		pnlInput.add(lblLcount);
		
		JLabel lblStopl = new JLabel("STOPL  =");
		lblStopl.setHorizontalAlignment(SwingConstants.LEFT);
		lblStopl.setForeground(Color.WHITE);
		lblStopl.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblStopl.setBounds(41, 444, 131, 49);
		pnlInput.add(lblStopl);
		
		txtX = new JTextField();
		txtX.setHorizontalAlignment(SwingConstants.RIGHT);
		txtX.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtX.setBounds(171, 27, 104, 49);
		txtX.setBackground(new Color(36,34,29));
		txtX.setForeground(new Color(255,220,135));
		txtX.setCaretColor(Color.WHITE);
		pnlInput.add(txtX);
		txtX.setColumns(10);
		txtX.addKeyListener(keyhand);
		
		txtY = new JTextField();
		txtY.setHorizontalAlignment(SwingConstants.RIGHT);
		txtY.setForeground(new Color(255, 220, 135));
		txtY.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtY.setColumns(10);
		txtY.setBackground(new Color(36, 34, 29));
		txtY.setBounds(171, 77, 104, 48);
		txtY.setCaretColor(Color.WHITE);
		txtY.addKeyListener(keyhand);
		pnlInput.add(txtY);
		
		txtZ = new JTextField();
		txtZ.setHorizontalAlignment(SwingConstants.RIGHT);
		txtZ.setForeground(new Color(255, 220, 135));
		txtZ.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtZ.setColumns(10);
		txtZ.setBackground(new Color(36, 34, 29));
		txtZ.setBounds(171, 126, 104, 49);
		txtZ.setCaretColor(Color.WHITE);
		txtZ.addKeyListener(keyhand);
		pnlInput.add(txtZ);
		
		txtT1H = new JTextField();
		txtT1H.setHorizontalAlignment(SwingConstants.CENTER);
		txtT1H.setForeground(new Color(255, 220, 135));
		txtT1H.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT1H.setColumns(10);
		txtT1H.setBackground(new Color(36, 34, 29));
		txtT1H.setBounds(171, 188, 48, 42);
		txtT1H.setCaretColor(Color.WHITE);
		txtT1H.addKeyListener(keyhand);
		pnlInput.add(txtT1H);
		
		txtT2H = new JTextField();
		txtT2H.setHorizontalAlignment(SwingConstants.CENTER);
		txtT2H.setForeground(new Color(255, 220, 135));
		txtT2H.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT2H.setColumns(10);
		txtT2H.setBackground(new Color(36, 34, 29));
		txtT2H.setBounds(171, 237, 48, 42);
		txtT2H.addKeyListener(keyhand);
		txtT2H.setCaretColor(Color.WHITE);
		pnlInput.add(txtT2H);
		
		txtT3H = new JTextField();
		txtT3H.setHorizontalAlignment(SwingConstants.CENTER);
		txtT3H.setForeground(new Color(255, 220, 135));
		txtT3H.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT3H.setColumns(10);
		txtT3H.setBackground(new Color(36, 34, 29));
		txtT3H.setBounds(171, 288, 48, 42);
		txtT3H.addKeyListener(keyhand);
		txtT3H.setCaretColor(Color.WHITE);
		pnlInput.add(txtT3H);
		
		txtT4H = new JTextField();
		txtT4H.setHorizontalAlignment(SwingConstants.CENTER);
		txtT4H.setForeground(new Color(255, 220, 135));
		txtT4H.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT4H.setColumns(10);
		txtT4H.setBackground(new Color(36, 34, 29));
		txtT4H.setBounds(171, 338, 48, 42);
		txtT4H.addKeyListener(keyhand);
		txtT4H.setCaretColor(Color.WHITE);
		pnlInput.add(txtT4H);
		
		JLabel label = new JLabel(":");
		label.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label.setForeground(Color.WHITE);
		label.setBounds(220, 182, 7, 49);
		pnlInput.add(label);
		
		txtT1M = new JTextField();
		txtT1M.setHorizontalAlignment(SwingConstants.CENTER);
		txtT1M.setForeground(new Color(255, 220, 135));
		txtT1M.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT1M.setColumns(10);
		txtT1M.setBackground(new Color(36, 34, 29));
		txtT1M.setBounds(227, 188, 48, 42);
		txtT1M.setCaretColor(Color.WHITE);
		txtT1M.addKeyListener(keyhand);
		pnlInput.add(txtT1M);
		
		txtT1S = new JTextField();
		txtT1S.setHorizontalAlignment(SwingConstants.CENTER);
		txtT1S.setForeground(new Color(255, 220, 135));
		txtT1S.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT1S.setColumns(10);
		txtT1S.setCaretColor(Color.WHITE);
		txtT1S.setBackground(new Color(36, 34, 29));
		txtT1S.setBounds(282, 188, 48, 42);
		txtT1S.addKeyListener(keyhand);
		pnlInput.add(txtT1S);
		
		JLabel label_1 = new JLabel(":");
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_1.setBounds(275, 181, 7, 49);
		pnlInput.add(label_1);
		
		JLabel label_2 = new JLabel(":");
		label_2.setForeground(Color.WHITE);
		label_2.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_2.setBounds(220, 231, 7, 49);
		pnlInput.add(label_2);
		
		txtT2M = new JTextField();
		txtT2M.setHorizontalAlignment(SwingConstants.CENTER);
		txtT2M.setForeground(new Color(255, 220, 135));
		txtT2M.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT2M.setColumns(10);
		txtT2M.setBackground(new Color(36, 34, 29));
		txtT2M.setCaretColor(Color.WHITE);
		txtT2M.addKeyListener(keyhand);
		txtT2M.setBounds(227, 237, 48, 42);
		pnlInput.add(txtT2M);
		
		JLabel label_3 = new JLabel(":");
		label_3.setForeground(Color.WHITE);
		label_3.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_3.setBounds(275, 230, 7, 49);
		pnlInput.add(label_3);
		
		txtT2S = new JTextField();
		txtT2S.setHorizontalAlignment(SwingConstants.CENTER);
		txtT2S.setForeground(new Color(255, 220, 135));
		txtT2S.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT2S.setColumns(10);
		txtT2S.setBackground(new Color(36, 34, 29));
		txtT1S.setCaretColor(Color.WHITE);
		txtT1S.addKeyListener(keyhand);
		txtT2S.setBounds(282, 237, 48, 42);
		pnlInput.add(txtT2S);
		
		JLabel label_4 = new JLabel(":");
		label_4.setForeground(Color.WHITE);
		label_4.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_4.setBounds(220, 285, 7, 49);
		pnlInput.add(label_4);
		
		txtT3M = new JTextField();
		txtT3M.setHorizontalAlignment(SwingConstants.CENTER);
		txtT3M.setForeground(new Color(255, 220, 135));
		txtT3M.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT3M.setColumns(10);
		txtT3M.setBackground(new Color(36, 34, 29));
		txtT3M.setCaretColor(Color.WHITE);
		txtT3M.addKeyListener(keyhand);
		txtT3M.setBounds(227, 291, 48, 42);
		pnlInput.add(txtT3M);
		
		JLabel label_5 = new JLabel(":");
		label_5.setForeground(Color.WHITE);
		label_5.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_5.setBounds(275, 284, 7, 49);
		pnlInput.add(label_5);
		
		txtT3S = new JTextField();
		txtT3S.setHorizontalAlignment(SwingConstants.CENTER);
		txtT3S.setForeground(new Color(255, 220, 135));
		txtT3S.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT3S.setColumns(10);
		txtT3S.setBackground(new Color(36, 34, 29));
		txtT3S.setCaretColor(Color.WHITE);
		txtT3S.addKeyListener(keyhand);
		txtT3S.setBounds(282, 291, 48, 42);
		pnlInput.add(txtT3S);
		
		JLabel label_6 = new JLabel(":");
		label_6.setForeground(Color.WHITE);
		label_6.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_6.setBounds(220, 332, 7, 49);
		pnlInput.add(label_6);
		
		txtT4M = new JTextField();
		txtT4M.setHorizontalAlignment(SwingConstants.CENTER);
		txtT4M.setForeground(new Color(255, 220, 135));
		txtT4M.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT4M.setColumns(10);
		txtT4M.setBackground(new Color(36, 34, 29));
		txtT4M.setBounds(227, 338, 48, 42);
		txtT4M.addKeyListener(keyhand);
		txtT4M.setCaretColor(Color.WHITE);
		pnlInput.add(txtT4M);
		
		JLabel label_7 = new JLabel(":");
		label_7.setForeground(Color.WHITE);
		label_7.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_7.setBounds(275, 331, 7, 49);
		pnlInput.add(label_7);
		
		txtT4S = new JTextField();
		txtT4S.setHorizontalAlignment(SwingConstants.CENTER);
		txtT4S.setForeground(new Color(255, 220, 135));
		txtT4S.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT4S.setColumns(10);
		txtT4S.setBackground(new Color(36, 34, 29));
		txtT4S.setBounds(282, 338, 48, 42);
		txtT4S.setCaretColor(Color.WHITE);
		txtT4S.addKeyListener(keyhand);
		pnlInput.add(txtT4S);
		
		txtLcount = new JTextField();
		txtLcount.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLcount.setForeground(new Color(255, 220, 135));
		txtLcount.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtLcount.setColumns(10);
		txtLcount.setBackground(new Color(36, 34, 29));
		txtLcount.setBounds(171, 392, 104, 49);
		txtLcount.setCaretColor(Color.WHITE);
		txtLcount.addKeyListener(keyhand);
		pnlInput.add(txtLcount);
		
		txtStopL = new JTextField();
		txtStopL.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStopL.setForeground(new Color(255, 220, 135));
		txtStopL.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtStopL.setColumns(10);
		txtStopL.setBackground(new Color(36, 34, 29));
		txtStopL.setCaretColor(Color.WHITE);
		txtStopL.setBounds(171, 445, 104, 49);
		txtStopL.addKeyListener(keyhand);
		pnlInput.add(txtStopL);
		
		JLabel label_8 = new JLabel("%");
		label_8.setHorizontalAlignment(SwingConstants.LEFT);
		label_8.setForeground(Color.WHITE);
		label_8.setFont(new Font("Verdana", Font.PLAIN, 22));
		label_8.setBounds(275, 26, 31, 49);
		pnlInput.add(label_8);
		
		JLabel label_9 = new JLabel("%");
		label_9.setHorizontalAlignment(SwingConstants.LEFT);
		label_9.setForeground(Color.WHITE);
		label_9.setFont(new Font("Verdana", Font.PLAIN, 22));
		label_9.setBounds(275, 76, 31, 49);
		pnlInput.add(label_9);
		
		JLabel label_10 = new JLabel("%");
		label_10.setHorizontalAlignment(SwingConstants.LEFT);
		label_10.setForeground(Color.WHITE);
		label_10.setFont(new Font("Verdana", Font.PLAIN, 22));
		label_10.setBounds(275, 126, 31, 49);
		pnlInput.add(label_10);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFormula();
			}
		});
		btnSave.setBounds(69, 508, 206, 35);
		pnlInput.add(btnSave);
	}
	
	public void saveFormula()
	{
		dbObj=new db_commons();
		String[] colvalue = new String[2];
		colvalue = validateFormulaData();
		try	{
			if (colvalue[0] == null)
			{
				JOptionPane.showMessageDialog(contentPane,"No Value Entered, Empty Record !!", "Empty Record Alert",JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				int count = dbObj.getRowCount("select * from TBL_FORMULA where  fname='"+lblFTitle.getText().toString()+"'");
				if (count == 0)
				{
					boolean isSucess = dbObj.executeNonQuery("insert into TBL_FORMULA ("+colvalue[0]+") values ("+colvalue[1]+")");
					if (isSucess ==true)
					{
						JOptionPane.showMessageDialog(contentPane,"Formula Added Successfully !!", "Message",JOptionPane.INFORMATION_MESSAGE);
						contentPane.dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(contentPane,"Unsuccessfull find the log", "Message",JOptionPane.ERROR_MESSAGE);	
					}
				}
				else 
				{
					dbObj.executeNonQuery("DELETE FROM TBL_FORMULA WHERE FNAME ='"+lblFTitle.getText().toString()+"'");
					boolean isSucess = dbObj.executeNonQuery("insert into TBL_FORMULA ("+colvalue[0]+") values ("+colvalue[1]+")");
					if (isSucess ==true)
					{
						JOptionPane.showMessageDialog(contentPane,"Formula Updated Successfully !!", "Message",JOptionPane.INFORMATION_MESSAGE);
						contentPane.dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(contentPane,"Unsuccessfull find the log", "Message",JOptionPane.ERROR_MESSAGE);	
					}
				}
			}			
		}
		catch(Exception ex){
			Logger.error(ex);
		}
		finally {
			
		}
	}
	
	public String [] validateFormulaData()
	{
		String [] colval =new String[2];
		try {
			String colbuilder="", valueBuilder="";
			if (!txtX.getText().equals(""))
			{
				colbuilder=colbuilder+"X,";
				valueBuilder = valueBuilder + txtX.getText()+",";
			}
			if (!txtY.getText().equals(""))
			{
				colbuilder=colbuilder+"Y,";
				valueBuilder = valueBuilder + txtY.getText()+",";
			}
			if (!txtZ.getText().equals(""))
			{
				colbuilder=colbuilder+"Z,";
				valueBuilder = valueBuilder + txtZ.getText()+",";
			}
			if ((!txtT1H.getText().equals("")) && (!txtT1M.getText().equals("")) && (!txtT1S.getText().equals("")))
			{
				colbuilder=colbuilder+"T1,";
				valueBuilder = valueBuilder +"'"+txtT1H.getText()+":"+txtT1M.getText()+":"+txtT1S.getText()+"',";
			}
			if ((!txtT2H.getText().equals("")) && (!txtT2M.getText().equals("")) && (!txtT2S.getText().equals("")))
			{
				colbuilder=colbuilder+"T2,";
				valueBuilder = valueBuilder +"'"+txtT2H.getText()+":"+txtT2M.getText()+":"+txtT2S.getText()+"',";
			}
			if ((!txtT3H.getText().equals("")) && (!txtT3M.getText().equals("")) && (!txtT3S.getText().equals("")))
			{
				colbuilder=colbuilder+"T3,";
				valueBuilder = valueBuilder +"'"+txtT3H.getText()+":"+txtT3M.getText()+":"+txtT3S.getText()+"',";
			}
			if ((!txtT4H.getText().equals("")) && (!txtT4M.getText().equals("")) && (!txtT4S.getText().equals("")))
			{
				colbuilder=colbuilder+"T4,";
				valueBuilder = valueBuilder +"'"+txtT4H.getText()+":"+txtT4M.getText()+":"+txtT4S.getText()+"',";
			}
			if (!txtLcount.getText().equals(""))
			{
				colbuilder=colbuilder+"LCOUNT,";
				valueBuilder = valueBuilder + txtLcount.getText()+",";
			}
			if (!txtStopL.getText().equals(""))
			{
				colbuilder=colbuilder+"STOPL,";
				valueBuilder = valueBuilder + txtStopL.getText()+",";
			}
			if (!colbuilder.equals(""))
			{
				colbuilder = colbuilder + "FNAME";
				valueBuilder= valueBuilder + "'"+lblFTitle.getText()+"'";
				colval[0]=colbuilder;//.substring(0, colbuilder.length() - 1);
				colval[1]=valueBuilder;//.substring(0, valueBuilder.length() - 1);
			}
			
			
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		finally {
			
		}
		return colval;
	}
	
	public class InputHandler implements KeyListener
	{
	        public void keyTyped(KeyEvent kt)
	        {
	        }
	        
 	        public void keyPressed(java.awt.event.KeyEvent evt)
 	        {
 	        		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE)
 	        		{
 	        			contentPane.dispose();
 	            }
 	        }
	        
	        public void keyReleased(KeyEvent kr)
	        {
	        }
	  }


}

