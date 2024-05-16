package com.extraction;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.model.UserDataModel;
import com.util.HelperFile;
import com.util.JFilePicker;
import com.util.LogConsole;

@SuppressWarnings({ "serial", "unused" })
public class ExtractForm extends JFrame implements ActionListener, PropertyChangeListener {

	private JLabel lblUserId;
	private JLabel lblName;
	private JLabel lblEmail;
	public JButton submitButton;
	private JButton logout;
	private JLabel lblNewLabel_2;
	private JLabel lblPleaseWait;
	private JTextArea textArea;

	private PrintStream standardOut;
	private String sessionIdValue;
	private String userId;
	private String password;
	private String sesssionIdValueLogin;
	private String urlValueLogin;

	private String apiVersion;
	private Logger logger;
	// private String delValue = "";
	private String envName;
	private String productIdVal = null;

	private int docLenIn = 0;
	private String[] documentLen = new String[docLenIn];
	private String envAllValue[] = documentLen;
	private String[] envLen = new String[docLenIn];
	List<UserDataModel> mainList;
	private Map<String, String> vaultName;
	private Map<String, String> securitypoliciesName;
	private JFilePicker filePicker;

	/**
	 * Create the frame.
	 */
	public ExtractForm() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginForm.class.getResource("/img/logo.png")));
		setForeground(Color.WHITE);
		setBackground(new Color(112, 128, 144));
		setTitle("Active User Report Generation Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 719, 534);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(ExtractForm.this, "Are you sure you want to close ?",
						"Close", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION) {
					System.exit(0);
				} else if (result == JOptionPane.NO_OPTION) {

				} else {

				}
			}
		});

		profileUi();
		extractUi();

	}

	/**
	 * ProfileUi - Contains components related to profiles.
	 */
	private void profileUi() {

		JPanel panel = new JPanel();
		panel.setBackground(new Color(245, 255, 250));
		panel.setBounds(2, 0, 713, 499);
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(ExtractForm.class.getResource("/img/profile_big.png")));
		lblNewLabel.setBounds(112, 27, 115, 100);
		panel.add(lblNewLabel);

		lblUserId = new JLabel();
		lblUserId.setForeground(new Color(0, 0, 128));
		lblUserId.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblUserId.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserId.setBounds(12, 134, 326, 29);
		panel.add(lblUserId);

		lblName = new JLabel();
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		lblName.setForeground(new Color(0, 0, 128));
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblName.setBounds(12, 164, 326, 22);
		panel.add(lblName);

		lblEmail = new JLabel();
		lblEmail.setHorizontalAlignment(SwingConstants.CENTER);
		lblEmail.setForeground(new Color(0, 0, 128));
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEmail.setBounds(12, 187, 326, 22);
		panel.add(lblEmail);

		logout = new JButton("LOGOUT");
		logout.setForeground(new Color(255, 255, 255));
		logout.setBackground(new Color(255, 0, 0));
		logout.setBounds(122, 242, 100, 35);
		logout.addActionListener(this);
		panel.add(logout);

		submitButton = new JButton("EXTRACT REPORT");
		submitButton.setBounds(467, 242, 150, 40);
		panel.add(submitButton);
		submitButton.setForeground(new Color(255, 255, 255));
		submitButton.setBackground(new Color(255, 165, 0));

		lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBounds(388, 295, 313, 16);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBackground(new Color(0, 0, 128));
		lblNewLabel_2.setIcon(new ImageIcon(LoginForm.class.getResource("/img/2.gif")));

		lblPleaseWait = new JLabel();
		lblPleaseWait.setText("Please Wait.....");
		lblPleaseWait.setHorizontalAlignment(SwingConstants.CENTER);
		lblPleaseWait.setForeground(new Color(0, 0, 128));
		lblPleaseWait.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPleaseWait.setBounds(375, 315, 326, 22);
		panel.add(lblPleaseWait);
		lblNewLabel_2.setVisible(false);
		lblPleaseWait.setVisible(false);
		submitButton.addActionListener(this);
		
		textArea = new JTextArea();
		textArea.setBackground(new Color(192, 192, 192));
		textArea.setEditable(false);
		textArea.setMargin(new Insets(0, 5, 0, 5));
		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(0, 345, 712, 155);
		panel.add(scroll);
		PrintStream printStream = new PrintStream(new LogConsole(textArea));
		
		standardOut = System.out;
		System.setOut(printStream);
		
		
		filePicker = new JFilePicker("Save in directory: ",
	            "Browse...", "C:\\Sanofi_Active_Users_List");
		filePicker.setBounds(375, 125, 326, 100);
		filePicker.setMode(JFilePicker.MODE_SAVE);	
		JFileChooser fileChooser = filePicker.getFileChooser();
	    fileChooser.setCurrentDirectory(new File("C:\\Sanofi_Active_Users_List"));
	    filePicker.getFileChooser().setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		panel.add(filePicker);
		
		
	}

	/**
	 * ExtractUi - Contains components related to extraction.
	 */
	private void extractUi() {
	}

	/**
	 * @param sessionId
	 * @param userName
	 * @param urlvalue
	 */

	public void sesUserUrl(String sessionId, String userName, String pwdValue, String urlvalue, Logger log, String name,
			String id, String apiV, Map<String, String> vaultName, Map<String, String> securitypoliciesName) {

		sesssionIdValueLogin = sessionId;
		urlValueLogin = urlvalue;
		userId = userName;
		password = pwdValue;
		this.logger = log;
		this.apiVersion = apiV;
		this.vaultName = vaultName;
		this.securitypoliciesName = securitypoliciesName;
		
		System.out.println("urlvalue = "+urlvalue);

		HelperFile helper = new HelperFile();
		boolean sessionAlive = helper.isSessionAlive(urlvalue, sessionId, logger, apiVersion);

		if (sessionAlive) {
			lblEmail.setText("Email- " + userName);
			lblUserId.setText("WELCOME - " + id);
			lblName.setText("Name- " + name);

		} else {
			Map<String, String> map = helper.userValidation(userName, pwdValue, urlvalue, logger, apiVersion);
			sessionId = map.get("sessionId");
			if (sessionId != null) {
				sessionIdValue = sessionId;

				lblEmail.setText("Email- " + userName);
				lblUserId.setText("WELCOME - " + id);
				lblName.setText("Name- " + name);
			} else {
				JOptionPane.showMessageDialog(this, "Invalid Username or Password");
			}
		}

	}

	/**
	 * Action Performed Task
	 */

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == submitButton) {

			String sessionIdValue = sesssionIdValueLogin;
			String urlValue = urlValueLogin;

			HelperFile helper = new HelperFile();
			boolean sessionAlive = helper.isSessionAlive(urlValue, sessionIdValue, logger, apiVersion);

			lblNewLabel_2.setVisible(true);
			lblPleaseWait.setVisible(true);
			
			String fileDir = filePicker.getSelectedFilePath();
			
			if (sessionAlive) {
				ExtractionTask task = new ExtractionTask(this, sessionIdValue, urlValue, logger, apiVersion, vaultName,
						securitypoliciesName,fileDir);
				task.addPropertyChangeListener(this);
				task.execute();
			} else {

				Map<String, String> map = helper.userValidation(userId, password, urlValue, logger, apiVersion);
				String sessionId = map.get("sessionId");
				if (sessionId != null) {
					sessionIdValue = sessionId;

					ExtractionTask task = new ExtractionTask(this, sessionIdValue, urlValue, logger, apiVersion,
							vaultName, securitypoliciesName,fileDir);
					task.addPropertyChangeListener(this);
					task.execute();

				} else {
					JOptionPane.showMessageDialog(this, "Session is expired. Please login agin.");
					LoginForm lfm1 = new LoginForm();
					lfm1.setVisible(true);
					this.dispose();
				}
			}

		}

		if (e.getSource() == logout) {

			int result = JOptionPane.showConfirmDialog(this, "Are you sure? Do you want to exit?", "Exit",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (result == JOptionPane.YES_OPTION) {
				LoginForm lfm = new LoginForm();
				lfm.setVisible(true);
				this.dispose();
			} else if (result == JOptionPane.NO_OPTION) {

			} else {

			}

		}
	}

	public void hide() {
		lblNewLabel_2.setVisible(false);
		lblPleaseWait.setVisible(false);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}
}
