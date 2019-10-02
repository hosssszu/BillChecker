package bills;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JList;

public class Bills {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private static DefaultListModel<String> listM = new DefaultListModel<>();
	private JButton btnExit;

	static Connection c = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	Statement stmt2 = null;
	
	public static void main(String[] args) throws Exception {
		
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dbname",
					"user", "password");
		       c.setAutoCommit(true);
   	 } catch (Exception e) {
		       e.printStackTrace();
		       System.err.println(e.getClass().getName()+": "+e.getMessage());
	           JOptionPane.showMessageDialog(null, "Connection failed!");
	           System.exit(0);
	   }		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Bills window = new Bills();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Bills() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 444, 320);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("BillChecker");
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mntmExit.setMnemonic(KeyEvent.VK_X);
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setMnemonic(KeyEvent.VK_E);
		menuBar.add(mnEdit);
		
		JMenu mnOptions = new JMenu("Options");
		mnOptions.setMnemonic(KeyEvent.VK_O);
		menuBar.add(mnOptions);
		
		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
		String data = spf.format(new Date());
		
		JLabel labelb = new JLabel("");
		labelb.setBounds(80, 231, 58, 14);		
		frame.getContentPane().add(labelb);
		
		JMenuItem mntmAddAmount = new JMenuItem("Add amount");
		mntmAddAmount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fn = JOptionPane.showInputDialog(null, "Amount: ", "Input", JOptionPane.PLAIN_MESSAGE);
				int num1 = Integer.parseInt(fn);
				
				try {
				
					java.sql.Statement stat = c.createStatement();
					String sql = "INSERT INTO BILLSB" +
					"(Data, Money) VALUES('"+data+"', '"+num1+"')";
					((java.sql.Statement) stat).executeUpdate(sql);
				
					String g = labelb.getText();
				
					int i = Integer.parseInt(g);
					int h = num1+i;
					String str = String.valueOf(h);
					labelb.setText(str);		
				
				}catch (SQLException e2){
					
				}
			}
		});
		mntmAddAmount.setMnemonic(KeyEvent.VK_A);
		mnOptions.add(mntmAddAmount);
		
		menuBar.add(Box.createHorizontalGlue());
		JMenu menuhelp = new JMenu("Help");
		menuhelp.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menuhelp);
		 
		JMenuItem about = new JMenuItem("About");
		about.setMnemonic(KeyEvent.VK_A);
		about.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {		   		
		 		JOptionPane.showMessageDialog(null, "Created by Szegedi Loránd!" ,"Message", JOptionPane.INFORMATION_MESSAGE);
		   	}
		});
		menuhelp.add(about);
		
		JLabel lblBalance = new JLabel("Balance:");
		lblBalance.setBounds(19, 231, 61, 14);
		frame.getContentPane().add(lblBalance);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(247, 253, 61, 14);
		lblNewLabel.setVisible(false);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblLei = new JLabel("lei");
		lblLei.setBounds(112, 231, 46, 14);
		frame.getContentPane().add(lblLei);
		
		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(34, 11, 46, 14);
		frame.getContentPane().add(lblDate);
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(129, 11, 46, 14);
		frame.getContentPane().add(lblPrice);
		
		JLabel label = new JLabel("");
		label.setVisible(false);
		label.setBounds(160, 253, 61, 14);
		frame.getContentPane().add(label);
		
		String[] files = {"Provider 1", "Provider 2", "Provider 3", "Provider 4"};
		JComboBox comboBox = new JComboBox(files);
		comboBox.setSelectedIndex(-1);
		comboBox.setBounds(211, 27, 113, 21);
		
		textField = new JTextField();
		textField.setBounds(19, 27, 85, 22);
		textField.setColumns(10);
		frame.getContentPane().add(textField);
		
		textField_1 = new JTextField();
		textField_1.setBounds(115, 27, 85, 22);
		textField_1.setColumns(10);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(comboBox);
		frame.getContentPane().add(textField_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(19, 58, 402, 162);
		frame.getContentPane().add(scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
		
		JMenuItem mntmShowAll = new JMenuItem("Show all");
		mntmShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listM.clear();
			try {	
				java.sql.Statement stat = c.createStatement();
				String sqlQuery = "select * from <dbname>; ";
				rs = stat.executeQuery(sqlQuery);
				
				while(rs.next()) {
					StringBuilder builder = new StringBuilder();
					
					String categorynameDba = rs.getString("ID");
					String categorynameDb0 = rs.getString("Data");
					String categorynameDb = rs.getString("Suma");
					String categorynameDb2 = rs.getString("Firma");

					builder.append("<html><pre>");
					builder.append(String.format("%s, \t %s, \t %s lei, \t %s", categorynameDba, categorynameDb0, categorynameDb, categorynameDb2));
					builder.append("</pre></html>");
					
					listM.addElement(builder.toString()); 	
				}
			}catch(Exception ef){
				}
			}
		});
		mntmShowAll.setMnemonic(KeyEvent.VK_S);
		mnEdit.add(mntmShowAll);
		
		JMenuItem mntmProvider1 = new JMenuItem("Provider 1");
		mntmElectrica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listM.clear();
			try {	
				java.sql.Statement stat = c.createStatement();
				String sqlQuery = "select * from <dbname> where firma like 'E%'; ";
				rs = stat.executeQuery(sqlQuery);
				
				while(rs.next()) {
					StringBuilder builder = new StringBuilder();
					
					String categorynameDba = rs.getString("ID");
					String categorynameDb0 = rs.getString("Data");
					String categorynameDb = rs.getString("Suma");
					String categorynameDb2 = rs.getString("Firma");

					builder.append("<html><pre>");
					builder.append(String.format("%s, \t %s, \t %s lei, \t %s", categorynameDba, categorynameDb0, categorynameDb, categorynameDb2));
					builder.append("</pre></html>");
					
					listM.addElement(builder.toString()); 	
				}
			}catch(Exception ef){
				}
			}
		});
		mnEdit.add(mntmProvider1);
		
		JMenuItem mntmProvider2 = new JMenuItem("Provider 2");
		mntmRcsrds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listM.clear();
			try {	
				java.sql.Statement stat = c.createStatement();
				String sqlQuery = "select * from <dbname> where firma like 'R%'; ";
				rs = stat.executeQuery(sqlQuery);
				
				while(rs.next()) {
					StringBuilder builder = new StringBuilder();
					
					String categorynameDba = rs.getString("ID");
					String categorynameDb0 = rs.getString("Data");
					String categorynameDb = rs.getString("Suma");
					String categorynameDb2 = rs.getString("Firma");

					builder.append("<html><pre>");
					builder.append(String.format("%s, \t %s, \t %s lei, \t %s", categorynameDba, categorynameDb0, categorynameDb, categorynameDb2));
					builder.append("</pre></html>");
					
					listM.addElement(builder.toString()); 	
				}
			}catch(Exception ef){
				}
			}
		});
		mnEdit.add(mntmProvider2);
		
		JMenuItem mntmProvider3 = new JMenuItem("Provider 3");
		mntmUpc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listM.clear();
			try {	
				java.sql.Statement stat = c.createStatement();
				String sqlQuery = "select * from <dbname> where firma like 'U%'; ";
				rs = stat.executeQuery(sqlQuery);
				
				while(rs.next()) {
					StringBuilder builder = new StringBuilder();
					
					String categorynameDba = rs.getString("ID");
					String categorynameDb0 = rs.getString("Data");
					String categorynameDb = rs.getString("Suma");
					String categorynameDb2 = rs.getString("Firma");

					builder.append("<html><pre>");
					builder.append(String.format("%s, \t %s, \t %s lei, \t %s", categorynameDba, categorynameDb0, categorynameDb, categorynameDb2));
					builder.append("</pre></html>");
					
					listM.addElement(builder.toString()); 	
				}
			}catch(Exception ef){
				}
			}
		});
		mnEdit.add(mntmProvider3);
		
		JMenuItem mntmProvider4 = new JMenuItem("Provider 4");
		mntmVodafone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listM.clear();
			try {	
				java.sql.Statement stat = c.createStatement();
				String sqlQuery = "select * from <dbname> where firma like 'V%'; ";
				rs = stat.executeQuery(sqlQuery);
				
				while(rs.next()) {
					
					StringBuilder builder = new StringBuilder();
					
					String categorynameDba = rs.getString("ID");
					String categorynameDb0 = rs.getString("Data");
					String categorynameDb = rs.getString("Suma");
					String categorynameDb2 = rs.getString("Firma");

					builder.append("<html><pre>");
					builder.append(String.format("%s, \t %s, \t %s lei, \t %s", categorynameDba, categorynameDb0, categorynameDb, categorynameDb2));
					builder.append("</pre></html>");
					
					listM.addElement(builder.toString()); 	
				}
			}catch(Exception ef){
				}
			}
		});
		mnEdit.add(mntmProvider4);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				listM.clear();
				
				try {
					java.sql.Statement stat = c.createStatement();
					String sql = "INSERT INTO BILLS" +
					"(Data, Suma, Firma) VALUES('"+textField.getText()+"','"+textField_1.getText()+"','"+comboBox.getSelectedItem()+"')";
					
				 ((java.sql.Statement) stat).executeUpdate(sql);
					
				String sqlQuery = "select * from <dbname>; ";

					rs = stat.executeQuery(sqlQuery);

					while(rs.next()) {
						
						String categorynameDba = rs.getString("ID");
						String categorynameDb0 = rs.getString("Data");
						String categorynameDb = rs.getString("Suma");
						String categorynameDb2 = rs.getString("Firma");
						
						StringBuilder builder = new StringBuilder();

						builder.append("<html><pre>");
						builder.append(String.format("%s, \t %s, \t %s lei, \t %s", categorynameDba, categorynameDb0, categorynameDb, categorynameDb2));
						builder.append("</pre></html>");
						
						listM.addElement(builder.toString()); 
					}
					
					list.setModel(listM);
					
					String sql2 = "select sum(suma) from <dbname>;";
					rs2 = stat.executeQuery(sql2);
				 
					while(rs2.next()) {
						StringBuilder builder2 = new StringBuilder();
						builder2.append(String.format("%d", rs2.getInt(1)));				
						lblNewLabel.setText(builder2.toString()); 
					}
					
					String sql3 = "select sum(money) from <dbname>;";
					rs3 = stat.executeQuery(sql3);
				 
					while(rs3.next()) {
						StringBuilder builder3 = new StringBuilder();
						builder3.append(String.format("%d", rs3.getInt(1)));			
						label.setText(builder3.toString()); 
					} 
						String a = label.getText();
						String b = lblNewLabel.getText();		
						int a1 = Integer.parseInt(a);
						int b1 = Integer.parseInt(b);	
						int d = a1-b1;
						String strI = String.valueOf(d);	
						labelb.setText(strI);	
				}catch(Exception ex) {
					System.err.println(ex.getMessage());
				}
			}
		});
		btnNewButton.setBounds(336, 27, 84, 21);
		frame.getContentPane().add(btnNewButton);
		
		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.setMnemonic(KeyEvent.VK_D);
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String fm = JOptionPane.showInputDialog(null, "Enter ID", "Input", JOptionPane.PLAIN_MESSAGE);
				
				listM.clear();
				
				try {
					java.sql.Statement stmt = c.createStatement();
					String delete = "DELETE FROM <dbname> WHERE id = " + fm + ";";
					stmt.executeUpdate(delete);
				    
				    ResultSet rs = stmt.executeQuery( "SELECT * FROM <dbname>;" );
			         while ( rs.next() ) {
			        String categorynameDba = rs.getString("ID");
							String categorynameDb0 = rs.getString("Data");
							String categorynameDb = rs.getString("Suma");
							String categorynameDb2 = rs.getString("Firma");
							
							StringBuilder builder = new StringBuilder();

							builder.append("<html><pre>");
							builder.append(String.format("%s, \t %s, \t %s lei, \t %s", categorynameDba, categorynameDb0, categorynameDb, categorynameDb2));
							builder.append("</pre></html>");
							
							listM.addElement(builder.toString()); 
						}
						
						list.setModel(listM);
						
						String sql2 = "select sum(suma) from <dbname>;";
						ResultSet rs2 = stmt.executeQuery(sql2);
					 
						while(rs2.next()) {
							
							StringBuilder builder2 = new StringBuilder();
							builder2.append(String.format("%d", rs2.getInt(1)));
							lblNewLabel.setText(builder2.toString()); 
						}
			        	 
						java.sql.Statement stat = c.createStatement();
						String sql3 = "select sum(money) from <dbname>;";
						rs3 = stat.executeQuery(sql3);
						
						StringBuilder builder3 = new StringBuilder();
						
						while(rs3.next()) {
							builder3.append(String.format("%d", rs3.getInt(1)));
							label.setText(builder3.toString()); 
						}

						String a = label.getText();
						String b = lblNewLabel.getText();		
						int a1 = Integer.parseInt(a);
						int b1 = Integer.parseInt(b);
						int d = a1-b1;
						String strI = String.valueOf(d);	
						labelb.setText(strI);
						 
				} catch (SQLException e1) {

				}					
			}
		});
		mnOptions.add(mntmDelete);
			
		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setBounds(336, 229, 84, 21);
		frame.getContentPane().add(btnExit);
		
		try {
			String sqlQuery = "select * from <dbname>; " ;
			String sql2 = "select sum(suma) from <dbname>;";
			
			java.sql.Statement stat = c.createStatement();
			java.sql.Statement stat2 = c.createStatement();
			
			rs = stat.executeQuery(sqlQuery);
			rs2 = stat2.executeQuery(sql2);
			
			StringBuilder builder2 = new StringBuilder();
			
			while(rs2.next()) {
			
				builder2.append(String.format("%d", rs2.getInt(1)));
				lblNewLabel.setText(builder2.toString()); 
			}
			
			while(rs.next()) {
				
				StringBuilder builder = new StringBuilder();
				
				String categorynameDba = rs.getString("ID");
				String categorynameDb0 = rs.getString("Data");
				String categorynameDb = rs.getString("Suma");
				String categorynameDb2 = rs.getString("Firma");

				builder.append("<html><pre>");
				builder.append(String.format("%s, \t %s, \t %s lei, \t %s", categorynameDba, categorynameDb0, categorynameDb, categorynameDb2));
				builder.append("</pre></html>");
				
				listM.addElement(builder.toString()); 				
			}

			list.setModel(listM);			
			
			String sql3 = "select sum(money) from <dbname>;";
			rs3 = stat.executeQuery(sql3);
			
			StringBuilder builder3 = new StringBuilder();
			
			while(rs3.next()) {
				builder3.append(String.format("%d", rs3.getInt(1)));
				label.setText(builder3.toString()); 
			}

			String a = label.getText();
			String b = lblNewLabel.getText();	
			int a1 = Integer.parseInt(a);
			int b1 = Integer.parseInt(b);
			int d = a1-b1;
			String strI = String.valueOf(d);
			labelb.setText(strI);
			
		} catch (Exception ex) {

		}	
	}
}
