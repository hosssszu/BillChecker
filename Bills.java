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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JList;

public class BillTracker {

	static Connection connection = null;
	int biggestID = 0;
	JFrame frame;

	public static void main(String[] args) throws Exception {

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "password");
			connection.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Connection failed!");
			System.exit(0);
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BillTracker window = new BillTracker();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public BillTracker() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 480, 339);
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
				try {
					connection.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
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

		JLabel balanceLabel = new JLabel("");
		balanceLabel.setBounds(70, 251, 58, 14);
		frame.getContentPane().add(balanceLabel);

		JMenuItem mntmAddAmount = new JMenuItem("Add amount");
		mntmAddAmount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
				String amount = JOptionPane.showInputDialog(null, "Amount: ", "Input", JOptionPane.PLAIN_MESSAGE);
				try {
					int amountInt = Integer.parseInt(amount);
					String sqlInsert = "INSERT INTO BILLSB" + "(Date, Money) VALUES (?, ?)";
					PreparedStatement statement = connection.prepareStatement(sqlInsert);
					statement.setString(1, date);
					statement.setInt(2, amountInt);
					statement.executeUpdate();
					int balanceValue = Integer.parseInt(balanceLabel.getText());
					int totalInt = amountInt + balanceValue;
					String total = String.valueOf(totalInt);
					balanceLabel.setText(total);
				} catch (SQLException | NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid input! Try again!", "Message",
							JOptionPane.INFORMATION_MESSAGE);
					ex.printStackTrace();
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
		lblBalance.setBounds(9, 251, 61, 14);
		frame.getContentPane().add(lblBalance);

		JLabel totalMoneySpentLabel = new JLabel("");
		totalMoneySpentLabel.setBounds(263, 266, 61, 14);
		totalMoneySpentLabel.setVisible(false);
		frame.getContentPane().add(totalMoneySpentLabel);

		JLabel lblLei = new JLabel("lei");
		lblLei.setBounds(102, 251, 46, 14);
		frame.getContentPane().add(lblLei);

		JLabel lblDate = new JLabel("Date");
		lblDate.setBounds(24, 11, 46, 14);
		frame.getContentPane().add(lblDate);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setBounds(131, 11, 46, 14);
		frame.getContentPane().add(lblPrice);

		JLabel lblProvider = new JLabel("Provider");
		lblProvider.setBounds(236, 11, 58, 14);
		frame.getContentPane().add(lblProvider);

		JLabel totalMoney = new JLabel("");
		totalMoney.setVisible(false);
		totalMoney.setBounds(151, 246, 61, 14);
		frame.getContentPane().add(totalMoney);

		String[] company = { "Provider1", "Provider2", "Provider3", "Provider4", "Provider5" };
		JComboBox comboBox = new JComboBox(company);
		comboBox.setSelectedIndex(-1);
		comboBox.setBounds(217, 27, 143, 21);

		JTextField textFieldDate = new JTextField();
		textFieldDate.setBounds(9, 27, 90, 22);
		textFieldDate.setColumns(10);
		frame.getContentPane().add(textFieldDate);

		JTextField textFieldPrice = new JTextField();
		textFieldPrice.setBounds(113, 27, 91, 22);
		textFieldPrice.setColumns(10);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(comboBox);
		frame.getContentPane().add(textFieldPrice);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(9, 58, 449, 182);
		frame.getContentPane().add(scrollPane);

		JList list = new JList();
		scrollPane.setViewportView(list);
		DefaultListModel<String> defaultListModel = new DefaultListModel<>();

		try {
			String sqlBillsSum = "select sum(suma) from bills;";
			PreparedStatement stat = connection.prepareStatement(sqlBillsSum);
			ResultSet rs = stat.executeQuery();

			while (rs.next()) {
				StringBuilder builder = new StringBuilder();
				builder.append(String.format("%d", rs.getInt(1)));
				totalMoneySpentLabel.setText(builder.toString());
			}

			String sqlBills = "select * from bills order by id desc;";
			extractedList(defaultListModel, sqlBills);
			
			stat = connection.prepareStatement(sqlBills);
			rs = stat.executeQuery();
			rs.next();
			biggestID = Integer.parseInt(rs.getString("ID"));

			list.setModel(defaultListModel);

			String sqlBillsBSum = "select sum(money) from billsb;";
			stat = connection.prepareStatement(sqlBillsBSum);
			rs = stat.executeQuery();

			while (rs.next()) {
				StringBuilder builder = new StringBuilder();
				builder.append(String.format("%d", rs.getInt(1)));
				totalMoney.setText(builder.toString());
			}
			extractedValue(balanceLabel, totalMoneySpentLabel, totalMoney);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		JMenuItem mntmShowAll = new JMenuItem("Show all");
		mntmShowAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				defaultListModel.clear();
				try {
					String sqlQuery = "select * from bills order by id desc;";
					extractedList(defaultListModel, sqlQuery);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mntmShowAll.setMnemonic(KeyEvent.VK_S);
		mnEdit.add(mntmShowAll);

		JMenuItem mntmSOMES = new JMenuItem("Provider1");
		mntmSOMES.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultListModel.clear();
				try {
					String sqlQuery = "select * from bills where firma like 'C%' order by id desc;";
					extractedList(defaultListModel, sqlQuery);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmSOMES);

		JMenuItem mntmElectrica = new JMenuItem("Provider2");
		mntmElectrica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultListModel.clear();
				try {
					String sqlQuery = "select * from bills where firma like 'E%' order by id desc;";
					extractedList(defaultListModel, sqlQuery);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmElectrica);

		JMenuItem mntmRcsrds = new JMenuItem("Provider3");
		mntmRcsrds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultListModel.clear();
				try {
					String sqlQuery = "select * from bills where firma like 'R%' order by id desc;";
					extractedList(defaultListModel, sqlQuery);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmRcsrds);

		JMenuItem mntmUpc = new JMenuItem("Provider4");
		mntmUpc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultListModel.clear();
				try {
					String sqlQuery = "select * from bills where firma like 'U%' order by id desc;";
					extractedList(defaultListModel, sqlQuery);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmUpc);

		JMenuItem mntmVodafone = new JMenuItem("Provider5");
		mntmVodafone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				defaultListModel.clear();
				try {
					String sqlQuery = "select * from bills where firma like 'V%' order by id desc;";
					extractedList(defaultListModel, sqlQuery);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		mnEdit.add(mntmVodafone);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					DataValidator dateValidator = new DataValidator();
					String date = textFieldDate.getText();
					int price = Integer.parseInt(textFieldPrice.getText());
					String provider = comboBox.getSelectedItem().toString();					

					if (dateValidator.input(date) == false) {
						JOptionPane.showMessageDialog(null, "Invalid date!", "Message",	JOptionPane.INFORMATION_MESSAGE);
						textFieldDate.setText("");
						return;
					}

					if (Integer.parseInt(textFieldPrice.getText()) < 0) {
						JOptionPane.showMessageDialog(null, "Invalid price!", "Message", JOptionPane.INFORMATION_MESSAGE);
						textFieldPrice.setText("");
						return;
					}

					defaultListModel.clear();
					biggestID++;

					String sqlInsert = "INSERT INTO BILLS" + "(Date, Suma, Firma) VALUES(?, ? ,?)";
					PreparedStatement preparedStat = connection.prepareStatement(sqlInsert);
					preparedStat.setString(1, date);
					preparedStat.setInt(2, price);
					preparedStat.setString(3, provider);
					preparedStat.executeUpdate();

					String sqlQuery = "select * from bills order by id desc;";
					extractedList(defaultListModel, sqlQuery);
					
					list.setModel(defaultListModel);

					String sqlBillsSum = "select sum(suma) from bills;";
					preparedStat = connection.prepareStatement(sqlBillsSum);
					ResultSet rs1 = preparedStat.executeQuery();
					while (rs1.next()) {
						StringBuilder builder2 = new StringBuilder();
						builder2.append(String.format("%d", rs1.getInt(1)));
						totalMoneySpentLabel.setText(builder2.toString());
					}

					String sqlBillsBSum = "select sum(money) from billsb;";
					preparedStat = connection.prepareStatement(sqlBillsBSum);
					ResultSet rs3 = preparedStat.executeQuery();
					while (rs3.next()) {
						StringBuilder builder3 = new StringBuilder();
						builder3.append(String.format("%d", rs3.getInt(1)));
						totalMoney.setText(builder3.toString());
					}

					extractedValue(balanceLabel, totalMoneySpentLabel, totalMoney);
					textFieldPrice.setText("");
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Invalid input!");
					textFieldDate.setText("");
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
					defaultListModel.clear();
					try {
						String sqlDelete = "DELETE FROM bills WHERE id = " + biggestID + ";";
						PreparedStatement statement = connection.prepareStatement(sqlDelete);
						statement.executeUpdate();

						String sqlSelect = "SELECT * FROM BILLS order by id desc;";
						extractedList(defaultListModel, sqlSelect);

						list.setModel(defaultListModel);

						String sqlBillsSum = "select sum(suma) from bills;";
						statement = connection.prepareStatement(sqlBillsSum);
						ResultSet rs = statement.executeQuery();
						while (rs.next()) {
							StringBuilder builder = new StringBuilder();
							builder.append(String.format("%d", rs.getInt(1)));
							totalMoneySpentLabel.setText(builder.toString());
						}

						String sqlBillsBSum = "select sum(money) from billsb;";
						statement = connection.prepareStatement(sqlBillsBSum);
						rs = statement.executeQuery();
						while (rs.next()) {
							StringBuilder builder = new StringBuilder();
							builder.append(String.format("%d", rs.getInt(1)));
							totalMoney.setText(builder.toString());
						}

						extractedValue(balanceLabel, totalMoneySpentLabel, totalMoney);

						String sqlAlterSequence = "ALTER SEQUENCE bills_id_seq RESTART WITH " + biggestID + ";";
						statement = connection.prepareStatement(sqlAlterSequence);
						rs = statement.executeQuery();

					} catch (SQLException ex) {
						ex.getMessage();
					}
					biggestID--;
				}
			}
		});
		mnOptions.add(mntmDelete);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		btnExit.setBounds(373, 248, 84, 21);
		frame.getContentPane().add(btnExit);
	}

	private void extractedValue(JLabel balanceLabel, JLabel totalMoneySpentLabel, JLabel totalMoney) {
		int totalMoneyValue = Integer.parseInt(totalMoney.getText());
		int totalMoneySpentValue = Integer.parseInt(totalMoneySpentLabel.getText());
		int moneyDifference = totalMoneyValue - totalMoneySpentValue;
		String moneyDifferenceInt = String.valueOf(moneyDifference);
		balanceLabel.setText(moneyDifferenceInt);
	}

	private void extractedList(DefaultListModel<String> defaultListMedel, String sqlQuery) throws SQLException {
		PreparedStatement stat = connection.prepareStatement(sqlQuery);
		ResultSet rs = stat.executeQuery();

		while (rs.next()) {
			StringBuilder builder = new StringBuilder();
			String getID = rs.getString("ID");
			String getDate = rs.getString("Date");
			String getSuma = rs.getString("Suma");
			String getFirma = rs.getString("Firma");
			builder.append("<html><pre>" + String.format("%s, \t %s, \t %s lei, \t %s", getID, getDate, getSuma, getFirma) + "</pre></html>");
			defaultListMedel.addElement(builder.toString());
		}
	}
}
