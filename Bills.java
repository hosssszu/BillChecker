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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JList;

public class Bills {

	static Connection connection = null;
	int biggestID = 0;
	JFrame frame;

	public static void main(String[] args) throws Exception {

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "hyundai444");
			connection.setAutoCommit(true);
//		       JOptionPane.showMessageDialog(null, "Connected successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
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

	public updateBills() {
		initialize();

	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 480, 320);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Bill Tracker");

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

		JLabel balanceLabel = new JLabel("");
		balanceLabel.setBounds(80, 231, 58, 14);
		frame.getContentPane().add(balanceLabel);

		JMenuItem mntmAddAmount = new JMenuItem("Add amount");
		mntmAddAmount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String amount = JOptionPane.showInputDialog(null, "Amount: ", "Input", JOptionPane.PLAIN_MESSAGE);
				int amountInt = Integer.parseInt(amount);
				try {
					Statement stat = connection.createStatement();
					String sqlInsert = "INSERT INTO BILLSB" + "(Data, Money) VALUES('" + data + "', '" + amountInt
							+ "')";
					stat.executeUpdate(sqlInsert);
					String balanceValue = balanceLabel.getText();
					int balanceValueInt = Integer.parseInt(balanceValue);
					int totalInt = amountInt + balanceValueInt;
					String total = String.valueOf(totalInt);
					balanceLabel.setText(total);
				} catch (SQLException exceptionAdd) {
					exceptionAdd.printStackTrace();
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
				JOptionPane.showMessageDialog(null, "Created by Szegedi Loránd!\nVersion: 1.0", "Message",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menuhelp.add(about);

		JLabel lblBalance = new JLabel("Balance:");
		lblBalance.setBounds(19, 231, 61, 14);
		frame.getContentPane().add(lblBalance);

		JLabel totalMoneySpentLabel = new JLabel("");
		totalMoneySpentLabel.setBounds(263, 246, 61, 14);
		totalMoneySpentLabel.setVisible(false);
		frame.getContentPane().add(totalMoneySpentLabel);

		JLabel lblLei = new JLabel("lei");
		lblLei.setBounds(112, 231, 46, 14);
		frame.getContentPane().add(lblLei);

		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(34, 11, 46, 14);
		frame.getContentPane().add(lblDate);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(137, 11, 46, 14);
		frame.getContentPane().add(lblPrice);

		JLabel lblProvider = new JLabel("Provider");
		lblProvider.setBounds(239, 11, 58, 14);
		frame.getContentPane().add(lblProvider);

		JLabel totalMoney = new JLabel("");
		totalMoney.setVisible(false);
		totalMoney.setBounds(151, 246, 61, 14);
		frame.getContentPane().add(totalMoney);

		String[] company = { "Compania de apa", "Electrica", "RCS&RDS", "UPC", "Vodafone" };
		JComboBox comboBox = new JComboBox(company);
		comboBox.setSelectedIndex(-1);
		comboBox.setBounds(220, 27, 143, 21);

		JTextField textFieldData = new JTextField();
		textFieldData.setBounds(19, 27, 90, 22);
		textFieldData.setColumns(10);
		frame.getContentPane().add(textFieldData);

		JTextField textFieldPrice = new JTextField();
		textFieldPrice.setBounds(119, 27, 91, 22);
		textFieldPrice.setColumns(10);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(comboBox);
		frame.getContentPane().add(textFieldPrice);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(19, 58, 439, 162);
		frame.getContentPane().add(scrollPane);

		JList list = new JList();
		scrollPane.setViewportView(list);
		DefaultListModel<String> defaultListMedel = new DefaultListModel<>();

		try {

			String sqlBills = "select * from bills order by id desc;";
			String sqlBillsSum = "select sum(suma) from bills;";

			Statement stat1 = connection.createStatement();
			Statement stat2 = connection.createStatement();

			ResultSet rs1 = stat1.executeQuery(sqlBillsSum);
			ResultSet rs2 = stat2.executeQuery(sqlBills);

			StringBuilder builder1 = new StringBuilder();

			while (rs1.next()) {
				builder1.append(String.format("%d", rs1.getInt(1)));
				totalMoneySpentLabel.setText(builder1.toString());
			}

			while (rs2.next()) {

				StringBuilder builder2 = new StringBuilder();

				String getID = rs2.getString("ID");
				String getData = rs2.getString("Data");
				String getSuma = rs2.getString("Suma");
				String getFirma = rs2.getString("Firma");

				builder2.append("<html><pre>");
				builder2.append(String.format("%s, \t %s, \t %s lei, \t %s", getID, getData, getSuma, getFirma));
				builder2.append("</pre></html>");

				defaultListMedel.addElement(builder2.toString());

				int id = Integer.parseInt(getID);
				biggestID = Math.max(id, biggestID);
			}

			list.setModel(defaultListMedel);

			String sqlBillsBSum = "select sum(money) from billsb;";
			ResultSet rs3 = stat2.executeQuery(sqlBillsBSum);

			StringBuilder builder3 = new StringBuilder();

			while (rs3.next()) {
				builder3.append(String.format("%d", rs3.getInt(1)));
				totalMoney.setText(builder3.toString());
			}

			String totalMoneyString = totalMoney.getText();
			String totalMoneySpentString = totalMoneySpentLabel.getText();
			int totalMoneyInt = Integer.parseInt(totalMoneyString);
			int totalMoneySpentInt = Integer.parseInt(totalMoneySpentString);
			int moneyDifference = totalMoneyInt - totalMoneySpentInt;
			String moneyDifferenceInt = String.valueOf(moneyDifference);
			balanceLabel.setText(moneyDifferenceInt);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		JMenuItem mntmShowAll = new JMenuItem("Show all");
		mntmShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				defaultListMedel.clear();
				try {
					Statement stat = connection.createStatement();
					String sqlQuery = "select * from bills order by id desc;";
					ResultSet rs2 = stat.executeQuery(sqlQuery);

					while (rs2.next()) {
						StringBuilder builder = new StringBuilder();
						String getID = rs2.getString("ID");
						String getData = rs2.getString("Data");
						String getSuma = rs2.getString("Suma");
						String getFirma = rs2.getString("Firma");
						builder.append("<html><pre>");
						builder.append(String.format("%s, \t %s, \t %s lei, \t %s", getID, getData, getSuma, getFirma));
						builder.append("</pre></html>");
						defaultListMedel.addElement(builder.toString());
					}
				} catch (Exception ef) {
					ef.printStackTrace();
				}
			}
		});
		mntmShowAll.setMnemonic(KeyEvent.VK_S);
		mnEdit.add(mntmShowAll);

		JMenuItem mntmSOMES = new JMenuItem("Compania de apa");
		mntmSOMES.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultListMedel.clear();
				try {
					Statement stat = connection.createStatement();
					String sqlQuery = "select * from bills where firma like 'C%';";
					ResultSet rs2 = stat.executeQuery(sqlQuery);

					while (rs2.next()) {
						StringBuilder builder = new StringBuilder();
						String getID = rs2.getString("ID");
						String getData = rs2.getString("Data");
						String getSuma = rs2.getString("Suma");
						String getFirma = rs2.getString("Firma");
						builder.append("<html><pre>");
						builder.append(String.format("%s, \t %s, \t %s lei, \t %s", getID, getData, getSuma, getFirma));
						builder.append("</pre></html>");
						defaultListMedel.addElement(builder.toString());
					}
				} catch (Exception ef) {
					ef.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmSOMES);

		JMenuItem mntmElectrica = new JMenuItem("Electrica");
		mntmElectrica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultListMedel.clear();
				try {
					Statement stat = connection.createStatement();
					String sqlQuery = "select * from bills where firma like 'E%';";
					ResultSet rs2 = stat.executeQuery(sqlQuery);

					while (rs2.next()) {
						StringBuilder builder = new StringBuilder();
						String getID = rs2.getString("ID");
						String getData = rs2.getString("Data");
						String getSuma = rs2.getString("Suma");
						String getFirma = rs2.getString("Firma");
						builder.append("<html><pre>");
						builder.append(String.format("%s, \t %s, \t %s lei, \t %s", getID, getData, getSuma, getFirma));
						builder.append("</pre></html>");
						defaultListMedel.addElement(builder.toString());
					}
				} catch (Exception ef) {
					ef.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmElectrica);

		JMenuItem mntmRcsrds = new JMenuItem("RCS&RDS");
		mntmRcsrds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultListMedel.clear();
				try {
					Statement stat = connection.createStatement();
					String sqlQuery = "select * from bills where firma like 'R%';";
					ResultSet rs2 = stat.executeQuery(sqlQuery);

					while (rs2.next()) {
						StringBuilder builder = new StringBuilder();
						String getID = rs2.getString("ID");
						String getData = rs2.getString("Data");
						String getSuma = rs2.getString("Suma");
						String getFirma = rs2.getString("Firma");
						builder.append("<html><pre>");
						builder.append(String.format("%s, \t %s, \t %s lei, \t %s", getID, getData, getSuma, getFirma));
						builder.append("</pre></html>");
						defaultListMedel.addElement(builder.toString());
					}
				} catch (Exception ef) {
					ef.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmRcsrds);

		JMenuItem mntmUpc = new JMenuItem("UPC");
		mntmUpc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultListMedel.clear();
				try {
					Statement stat = connection.createStatement();
					String sqlQuery = "select * from bills where firma like 'U%';";
					ResultSet rs2 = stat.executeQuery(sqlQuery);

					while (rs2.next()) {
						StringBuilder builder = new StringBuilder();
						String getID = rs2.getString("ID");
						String getData = rs2.getString("Data");
						String getSuma = rs2.getString("Suma");
						String getFirma = rs2.getString("Firma");
						builder.append("<html><pre>");
						builder.append(String.format("%s, \t %s, \t %s lei, \t %s", getID, getData, getSuma, getFirma));
						builder.append("</pre></html>");
						defaultListMedel.addElement(builder.toString());
					}
				} catch (Exception ef) {
					ef.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmUpc);

		JMenuItem mntmVodafone = new JMenuItem("Vodafone");
		mntmVodafone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultListMedel.clear();
				try {
					Statement stat = connection.createStatement();
					String sqlQuery = "select * from bills where firma like 'V%';";
					ResultSet rs2 = stat.executeQuery(sqlQuery);

					while (rs2.next()) {
						StringBuilder builder = new StringBuilder();
						String getID = rs2.getString("ID");
						String getData = rs2.getString("Data");
						String getSuma = rs2.getString("Suma");
						String getFirma = rs2.getString("Firma");
						builder.append("<html><pre>");
						builder.append(String.format("%s, \t %s, \t %s lei, \t %s", getID, getData, getSuma, getFirma));
						builder.append("</pre></html>");
						defaultListMedel.addElement(builder.toString());
					}
				} catch (Exception ef) {
					ef.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmVodafone);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					
					DataTest dataTest = new DataTest();
					String data = textFieldData.getText();

					if (dataTest.input(data) == false) {
						throw new IllegalArgumentException();
					}

					if (Integer.parseInt(textFieldPrice.getText()) < 0) {
						textFieldPrice.setText("");
						throw new IllegalArgumentException();
					}

					String providerSelected = comboBox.getSelectedItem().toString();
					if (providerSelected == null) {
						throw new IllegalArgumentException();
					}

					defaultListMedel.clear();
					biggestID++;
					Statement stat = connection.createStatement();

					String sqlInsert = "INSERT INTO BILLS" + "(Data, Suma, Firma) VALUES('" + textFieldData.getText()
							+ "','" + textFieldPrice.getText() + "','" + comboBox.getSelectedItem() + "')";

					stat.executeUpdate(sqlInsert);

					String sqlQuery = "select * from bills order by id desc;";

					ResultSet rs2 = stat.executeQuery(sqlQuery);

					while (rs2.next()) {
						StringBuilder builder = new StringBuilder();
						String getID = rs2.getString("ID");
						String getData = rs2.getString("Data");
						String getSuma = rs2.getString("Suma");
						String getFirma = rs2.getString("Firma");
						builder.append("<html><pre>");
						builder.append(String.format("%s, \t %s, \t %s lei, \t %s", getID, getData, getSuma, getFirma));
						builder.append("</pre></html>");
						defaultListMedel.addElement(builder.toString());
					}

					list.setModel(defaultListMedel);

					String sqlBillsSum = "select sum(suma) from bills;";
					ResultSet rs1 = stat.executeQuery(sqlBillsSum);

					while (rs1.next()) {
						StringBuilder builder2 = new StringBuilder();
						builder2.append(String.format("%d", rs1.getInt(1)));
						totalMoneySpentLabel.setText(builder2.toString());
					}

					String sqlBillsBSum = "select sum(money) from billsb;";
					ResultSet rs3 = stat.executeQuery(sqlBillsBSum);

					while (rs3.next()) {
						StringBuilder builder3 = new StringBuilder();
						builder3.append(String.format("%d", rs3.getInt(1)));
						totalMoney.setText(builder3.toString());
					}
					String totalMoneyString = totalMoney.getText();
					String totalMoneySpentString = totalMoneySpentLabel.getText();
					int totalMoneyInt = Integer.parseInt(totalMoneyString);
					int totalMoneySpentInt = Integer.parseInt(totalMoneySpentString);
					int moneyDifference = totalMoneyInt - totalMoneySpentInt;
					String moneyDifferenceString = String.valueOf(moneyDifference);
					balanceLabel.setText(moneyDifferenceString);
					textFieldPrice.setText("");
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
					JOptionPane.showMessageDialog(null, "Invalid input!");
					textFieldData.setText("");
					textFieldPrice.setText("");
				}
			}
		});
		btnAdd.setBounds(373, 27, 84, 21);
		frame.getContentPane().add(btnAdd);

		JMenuItem mntmDelete = new JMenuItem("Delete");
		mntmDelete.setMnemonic(KeyEvent.VK_D);
		mntmDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete the last input?",
						"DELETE", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					defaultListMedel.clear();
					try {
						Statement stat = connection.createStatement();
						Statement stmt = connection.createStatement();

						String sqlDelete = "DELETE FROM bills WHERE id = " + biggestID + ";";
						stmt.executeUpdate(sqlDelete);

						ResultSet rs = stmt.executeQuery("SELECT * FROM BILLS order by id desc;");
						while (rs.next()) {

							StringBuilder builder = new StringBuilder();
							String getID = rs.getString("ID");
							String getData = rs.getString("Data");
							String getSuma = rs.getString("Suma");
							String getFirma = rs.getString("Firma");
							builder.append("<html><pre>");
							builder.append(
									String.format("%s, \t %s, \t %s lei, \t %s", getID, getData, getSuma, getFirma));
							builder.append("</pre></html>");

							defaultListMedel.addElement(builder.toString());
						}

						list.setModel(defaultListMedel);

						String sqlBillsSum = "select sum(suma) from bills;";
						ResultSet rs2 = stmt.executeQuery(sqlBillsSum);
						while (rs2.next()) {

							StringBuilder builder2 = new StringBuilder();
							builder2.append(String.format("%d", rs2.getInt(1)));
							totalMoneySpentLabel.setText(builder2.toString());
						}

						String sqlBillsBSum = "select sum(money) from billsb;";
						ResultSet rs3 = stat.executeQuery(sqlBillsBSum);

						StringBuilder builder3 = new StringBuilder();

						while (rs3.next()) {
							builder3.append(String.format("%d", rs3.getInt(1)));
							totalMoney.setText(builder3.toString());
						}

						String totalMoneyString = totalMoney.getText();
						String totalMoneySpentString = totalMoneySpentLabel.getText();
						int totalMoneyInt = Integer.parseInt(totalMoneyString);
						int totalMoneySpentInt = Integer.parseInt(totalMoneySpentString);
						int moneyDifference = totalMoneyInt - totalMoneySpentInt;
						String moneyDifferenceString = String.valueOf(moneyDifference);
						balanceLabel.setText(moneyDifferenceString);

						String sqlAlterSequence = "ALTER SEQUENCE bills_id_seq RESTART WITH " + biggestID + ";";
						ResultSet rs4 = stat.executeQuery(sqlAlterSequence);

					} catch (SQLException ed) {
						System.out.println(ed.getMessage());
					}
					biggestID--;
				}
			}
		});
		mnOptions.add(mntmDelete);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setBounds(373, 228, 84, 21);
		frame.getContentPane().add(btnExit);
	}
}
