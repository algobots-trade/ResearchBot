package Forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import commons.db_commons;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.apache.commons.lang3.ArrayUtils;

import javax.swing.JSeparator;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionEvent;

public class SymbolMgmt {

	private JFrame teamframe;
	private JTable table;
	private JTextField txtsymbol;
	private JSeparator separator;
	private JLabel lblQty;
	private JTextField txtQty;
	private JSeparator separator_1;
	String [] col = {"SYMBOL","QTY"};
	String [][] records;
	db_commons dbobj = new db_commons();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SymbolMgmt window = new SymbolMgmt();
					window.teamframe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SymbolMgmt() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		teamframe = new JFrame();
		teamframe.setTitle("Scrib Management");
		teamframe.setBounds(100, 100, 503, 654);
		teamframe.setBackground(new Color(36,34,29));
		teamframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		teamframe.getContentPane().setLayout(null);
		teamframe.setVisible(true);
		teamframe.addKeyListener(new KeyAdapter()
	    {
	        @Override
	        public void keyPressed(java.awt.event.KeyEvent evt)
	        {
	        		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE){
	        			teamframe.dispose();
	            }
	        }
	    });
		
		JPanel cntpanel = new JPanel();
		cntpanel.setBounds(0, 0, 503, 632);
		cntpanel.setBackground(new Color(36,34,29));
		teamframe.getContentPane().add(cntpanel);
		cntpanel.setLayout(null);
		
		
		
		JLabel lblSymbol = new JLabel("SYMBOL");
		//lblSymbol.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblSymbol.setForeground(Color.WHITE);
		lblSymbol.setForeground(new Color(255, 220, 135));
		lblSymbol.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblSymbol.setBounds(51, 69, 92, 21);
		cntpanel.add(lblSymbol);
		
		txtsymbol = new JTextField();
		txtsymbol.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtsymbol.setBounds(143, 64, 148, 26);
		txtsymbol.setForeground(Color.WHITE);
		txtsymbol.setBackground(new Color(36,34,29));
		txtsymbol.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		txtsymbol.setCaretColor(Color.WHITE);
		cntpanel.add(txtsymbol);
		txtsymbol.setColumns(10);
		txtsymbol.addKeyListener(new KeyAdapter() {

			  public void keyTyped(KeyEvent e) {
			    char keyChar = e.getKeyChar();
			    if (Character.isLowerCase(keyChar)) {
			      e.setKeyChar(Character.toUpperCase(keyChar));
			    }
			  }

			});
		
		separator = new JSeparator();
		separator.setBounds(143, 88, 148, 21);
		cntpanel.add(separator);
		
		lblQty = new JLabel("QTY");
		lblQty.setForeground(Color.WHITE);
		lblQty.setForeground(new Color(255, 220, 135));
		lblQty.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblQty.setBounds(51, 108, 92, 21);
		cntpanel.add(lblQty);
		
		txtQty = new JTextField("0");
		txtQty.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtQty.setForeground(Color.WHITE);
		txtQty.setColumns(10);
		txtQty.setBackground(new Color(36, 34, 29));
		txtQty.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		txtQty.setBounds(143, 103, 148, 26);
		txtQty.setCaretColor(Color.WHITE);
		cntpanel.add(txtQty);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(143, 127, 148, 13);
		cntpanel.add(separator_1);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					if ((txtsymbol.getText().length() > 0) && (txtQty.getText().length() > 0))
					{
						
						dbobj = new db_commons();
						if (dbobj.getRowCount("Select * from TBL_SYMBOL_MST where SCRIB='"+txtsymbol.getText()+"';")==0)
						{
						dbobj.executeNonQuery("insert into TBL_SYMBOL_MST (SCRIB,QTY) values ('"+txtsymbol.getText()+"','"+txtQty.getText()+"');");
						records = dbobj.getMultiColumnRecords("Select SCRIB,QTY from TBL_SYMBOL_MST;");
						TableModel newmodel = new DefaultTableModel(records, col);
						table.setModel(newmodel);
						String [] stmts=new String[6];
					    stmts[0] ="insert into TBL_PLDATATABLE  (SYMBOL) values ('"+txtsymbol.getText()+"');";
					    stmts[1] ="insert into TBL_F1_TRADEINFO  (SYMBOL) values ('"+txtsymbol.getText()+"');";
					    stmts[2] ="insert into TBL_F2_TRADEINFO  (SYMBOL) values ('"+txtsymbol.getText()+"');";
					    stmts[3] ="insert into TBL_F3_TRADEINFO  (SYMBOL) values ('"+txtsymbol.getText()+"');";
					    stmts[4] ="insert into TBL_F4_TRADEINFO  (SYMBOL) values ('"+txtsymbol.getText()+"');";
					    stmts[5] ="insert into TBL_F5_TRADEINFO  (SYMBOL) values ('"+txtsymbol.getText()+"');";
					    dbobj.executeBatchStatement(stmts);
					    createSymbolBackUp(records);
						}
						else
						{
							JOptionPane.showMessageDialog(teamframe,"Duplicate Entry !!", "Duplicate Violation",JOptionPane.WARNING_MESSAGE);
						}
					}
				}
				catch (Exception ex)
				{
					
				}
				finally
				{
					
				}
				
			}
		});
		btnSave.setBounds(331, 87, 106, 42);
		cntpanel.add(btnSave);
		records =dbobj.getMultiColumnRecords("Select SCRIB,QTY from TBL_SYMBOL_MST;");
		TableModel model = new DefaultTableModel(records, col);
		table = new JTable(model){
		    public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
		        Component returnComp = super.prepareRenderer(renderer, row, column);
		        Color alternateColor = new Color(58,54,51);
		        Color whiteColor = new Color(79,75,72);
		        if (!returnComp.getBackground().equals(getSelectionBackground())){
		            Color bg = (row % 2 == 0 ? alternateColor : whiteColor);
		            returnComp .setBackground(bg);
		            returnComp.setForeground(Color.WHITE);
		            bg = null;
		        }
		        return returnComp;
		    }
		};
				
		table.setBounds(43, 106, 417, 502);
		cntpanel.add(table);
		JTableHeader header = table.getTableHeader();
	    header.setBackground(new Color(36,34,29));
	    header.setForeground(new Color(255,220,135));
	    header.setFont(new Font("Tahoma", Font.PLAIN, 14));
	    JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setEnabled(false);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportBorder(null);
		scrollPane.setBorder(null);
		scrollPane.setBounds(43, 152, 417, 456);
		
		cntpanel.add(scrollPane);
		
		JLabel lblScribManagement = new JLabel("SCRIB MANAGEMENT");
		lblScribManagement.setForeground(Color.WHITE);
		lblScribManagement.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblScribManagement.setBounds(152, 12, 238, 21);
		cntpanel.add(lblScribManagement);
		
		table.addKeyListener(new KeyAdapter() {
		      @Override
		      public void keyPressed(KeyEvent e){
		        if(e.getKeyCode()==68)
		          dbobj = new db_commons();
		        	  String [] deletestmts = new String[6];
				  deletestmts[0] = "delete from TBL_PLDATATABLE where symbol='"+records[table.getSelectedRow()][0]+"';";
		          deletestmts[1] = "delete from TBL_F1_TRADEINFO where symbol='"+records[table.getSelectedRow()][0]+"';";
		          deletestmts[2] = "delete from TBL_F2_TRADEINFO where symbol='"+records[table.getSelectedRow()][0]+"';";
		          deletestmts[3] = "delete from TBL_F3_TRADEINFO where symbol='"+records[table.getSelectedRow()][0]+"';";
		          deletestmts[4] = "delete from TBL_F4_TRADEINFO where symbol='"+records[table.getSelectedRow()][0]+"';";
		          deletestmts[5] = "delete from TBL_F5_TRADEINFO where symbol='"+records[table.getSelectedRow()][0]+"';";
		          dbobj.executeBatchStatement(deletestmts);
		        
				  dbobj.executeNonQuery("delete from TBL_SYMBOL_MST where scrib='"+table.getValueAt(table.getSelectedRow(), 0)+"';");
				  records = dbobj.getMultiColumnRecords("Select SCRIB,QTY from TBL_SYMBOL_MST;");
		          
		          TableModel newmodel = new DefaultTableModel(records, col);
				  table.setModel(newmodel);
				  createSymbolBackUp(records);
		      }
		    });

			
		
	}
	public void createSymbolBackUp(String [][] records)
	{
		String dir = System.getProperty("user.dir");
		String Sep= System.getProperty("file.separator");
        BufferedWriter bWrite = null; 
        File myFile = new File(dir+Sep+"T1.txt"); 
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
			 bWrite = new BufferedWriter(new FileWriter(myFile, true));
			 for(int i=0; i < records.length; i++)
			 {
				 bWrite.write(records[i][0]+","+records[i][1]);
				 bWrite.newLine();
			 }
			 bWrite.flush();
			 
		}
		catch(Exception ex)
		{
			if (bWrite != null) try {
				bWrite.close();
				
			 } catch (IOException ioe2) {
			    // just ignore it
			 }
		}
		finally
		{
			
		}
	}
	
}
