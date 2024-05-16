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
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings({ "serial","rawtypes", "unchecked" })
public class LoginForm extends JFrame implements ActionListener, PropertyChangeListener {

	static FileHandler fh;
	static Logger logger = Logger.getLogger("MyLog");

	Properties pathAllInformation;
	private JComboBox enviValue;
	private JButton loginButton;
	private JButton resetButton;
	private JCheckBox showPassword;
	private JTextField userTextField;
	private JPasswordField passwordField;

	private JProgressBar progressBar = new JProgressBar(0, 100);
	private JLabel lblPleaseWait = new JLabel("Please Wait....");
	private JLabel lblNewLabel_2;
	private String apiVersion = null;

	{
		try {

			File rootFolder = new File("C://Sanofi_Active_Users_List");
			if (!rootFolder.exists()) {
				rootFolder.mkdir();
			}

			File logFolder = new File("C://Sanofi_Active_Users_Log");
			if (!logFolder.exists()) {
				logFolder.mkdir();
			}

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			InputStream filePath = getClass().getResourceAsStream("/properties/layer7url.properties");

			if (filePath != null) {
				pathAllInformation = new Properties();
				pathAllInformation.load(filePath);
				String logFilePath = pathAllInformation.getProperty("logFilePath");

				Date date = new Date();
				int lastvalue = logFilePath.lastIndexOf("/");
				String folderPath = logFilePath.substring(0, lastvalue);
				String fileNameexist = logFilePath.substring(lastvalue + 1, logFilePath.length());

				String fileName = format.format(date);

				fileNameexist = folderPath + "/" + fileName + "_" + fileNameexist;
				File file = new File(fileNameexist);
				if (file.createNewFile()) {
					logFilePath = fileNameexist;
				} else {
					logger.info("Already exist");
				}
				fh = new FileHandler(logFilePath);
				logger.addHandler(fh);
				fh.setFormatter(new Formatter() {
					@Override
					public String format(LogRecord record) {
						StringBuilder sb = new StringBuilder();
						sb.append(record.getLevel()).append(':');
						sb.append(record.getMessage()).append('\n');
						return sb.toString();
					}
				});
			} else {
				logger.info("Please Enter Valid Path");
			}
		} catch (Exception e) {
			logger.info("LoginFrame" + e.toString());
		}
	}
	
	
	{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
		logger.info("*********************Extraction Start********************" + format.format(date));
		String environmentName = pathAllInformation.getProperty("environmentName");
		if (!environmentName.equalsIgnoreCase("")) {
			String environmentValuee = environmentName;
			String envAllValue[] = environmentValuee.split(",");
			String[] envLen = new String[envAllValue.length];
			{
				for (int i = 0; i < envAllValue.length; i++) {
					int tildPos = envAllValue[i].indexOf("~");
					envLen[i] = envAllValue[i].substring(0, tildPos);
				}
			}

			enviValue = new JComboBox(envLen);

		} else {
			logger.info("Please Enter Valid Path");
		}
	}
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getLookAndFeel());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					new LoginForm().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * Create the frame.
	 */
	public LoginForm() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginForm.class.getResource("/img/logo.png")));
		setForeground(Color.WHITE);
		setBackground(new Color(112, 128, 144));
		setTitle("Active User Report Generation Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 719, 534);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().setLayout(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(LoginForm.this, "Are you sure you want to close ?", "Close",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (result == JOptionPane.YES_OPTION) {
					System.exit(0);
				} else if (result == JOptionPane.NO_OPTION) {

				} else {

				}
			}
		});

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(0, 0, 713, 495);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		userTextField = new JTextField();
		userTextField.setBounds(194, 98, 302, 36);
		userTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		userTextField.setHorizontalAlignment(SwingConstants.LEFT);
		userTextField.setMargin(new Insets(0, 5, 0, 5));
		panel_1.add(userTextField);
		userTextField.setColumns(10);

		JLabel lblUserName = new JLabel("USER NAME");
		lblUserName.setBounds(194, 80, 90, 16);
		lblUserName.setForeground(Color.BLACK);
		panel_1.add(lblUserName);

		JLabel lblPassword = new JLabel("PASSWORD");
		lblPassword.setBounds(194, 140, 90, 16);
		lblPassword.setForeground(Color.BLACK);
		panel_1.add(lblPassword);

		showPassword = new JCheckBox("Show Password");
		showPassword.setBounds(381, 200, 126, 25);
		showPassword.setForeground(Color.BLACK);
		showPassword.setBackground(Color.LIGHT_GRAY);
		panel_1.add(showPassword);
		showPassword.addActionListener(this);
		
		
		JLabel lblEnvir = new JLabel("ENVIRONMENT NAME");
		lblEnvir.setHorizontalAlignment(SwingConstants.LEFT);
		lblEnvir.setBounds(194, 220, 148, 16);
		lblEnvir.setForeground(Color.BLACK);
		panel_1.add(lblEnvir);

		// enviValue = new JComboBox();
		enviValue.setBackground(new Color(255, 255, 255));
		enviValue.setBounds(194, 238, 302, 36);
		panel_1.add(enviValue);
		

		passwordField = new JPasswordField();
		passwordField.setBounds(194, 156, 302, 36);
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordField.setMargin(new Insets(0, 5, 0, 5));
		panel_1.add(passwordField);

		loginButton = new JButton("LOGIN");
		loginButton.setBounds(194, 330, 148, 36);
		loginButton.setForeground(new Color(255, 255, 255));
		loginButton.setBackground(new Color(255, 153, 51));
		panel_1.add(loginButton);
		loginButton.addActionListener(this);

		resetButton = new JButton("RESET");
		resetButton.setBounds(348, 330, 148, 36);
		resetButton.setForeground(new Color(255, 255, 255));
		resetButton.setBackground(new Color(255, 0, 51));
		panel_1.add(resetButton);
		resetButton.addActionListener(this);

		progressBar.setBounds(194, 420, 302, 25);
		progressBar.setStringPainted(true);
		panel_1.add(progressBar);
		lblPleaseWait.setHorizontalAlignment(SwingConstants.CENTER);

		lblPleaseWait.setForeground(Color.BLACK);
		lblPleaseWait.setBounds(194, 370, 302, 16);
		panel_1.add(lblPleaseWait);

		lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBackground(new Color(0, 0, 128));
		lblNewLabel_2.setIcon(new ImageIcon(LoginForm.class.getResource("/img/2.gif")));
		lblNewLabel_2.setBounds(266, 400, 150, 16);
		panel_1.add(lblNewLabel_2);

		progressBar.setVisible(false);
		lblPleaseWait.setVisible(false);
		lblNewLabel_2.setVisible(false);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == loginButton) {
			logger.info("*********************Login Process Start*******************");
			String userName;
			String pwdValue;
			String envSelectedValue;
			userName = userTextField.getText();
			pwdValue = new String(passwordField.getPassword());
			
			envSelectedValue = (String) enviValue.getItemAt(enviValue.getSelectedIndex());

			logger.info("*********************Environment Name**************" + envSelectedValue);

			if (userName.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter user name!", "Error", JOptionPane.ERROR_MESSAGE);
				logger.info(
						"*********************Login Process Vailidation Failed - Please enter user name **************");

			} else if (pwdValue.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please enter password!", "Error", JOptionPane.ERROR_MESSAGE);
				logger.info(
						"*********************Login Process Vailidation Failed - Please enter password **************");

			} else {

				progressBar.setVisible(false);
				lblPleaseWait.setVisible(true);
				lblNewLabel_2.setVisible(true);
				progressBar.setValue(0);

				//String urlvalue = pathAllInformation.getProperty("environmentName");
				apiVersion = pathAllInformation.getProperty("versionUrl");
				
				String environmentValuee = pathAllInformation.getProperty("environmentName");
				String envAllValue[] = environmentValuee.split(",");

				String[] strAr3 = new String[envAllValue.length];
				String urlvalue = "";

				for (int i = 0; i < envAllValue.length; i++) {
					int tildPos = envAllValue[i].indexOf("~");
					strAr3[i] = envAllValue[i].substring(0, tildPos);
					if (strAr3[i].equalsIgnoreCase(envSelectedValue)) {
						urlvalue = envAllValue[i].substring(tildPos + 1, envAllValue[i].length());
						break;
					}
				}
				

				try {
					LoginTask task = new LoginTask(this, userName, pwdValue, urlvalue, apiVersion, logger);
					task.addPropertyChangeListener(this);
					task.execute();
				} catch (Exception ex) {
					progressBar.setVisible(false);
					lblPleaseWait.setVisible(false);
					lblNewLabel_2.setVisible(false);
					JOptionPane.showMessageDialog(this, "Error executing login task: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}

		}

		// Coding Part of RESET button
		if (e.getSource() == resetButton) {
			userTextField.setText("");
			passwordField.setText("");
		}

		// Coding Part of showPassword JCheckBox
		if (e.getSource() == showPassword) {
			if (showPassword.isSelected()) {
				passwordField.setEchoChar((char) 0);
			} else {
				passwordField.setEchoChar('*');
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("progress")) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	public void hide() {
		progressBar.setVisible(false);
		lblPleaseWait.setVisible(false);
		lblNewLabel_2.setVisible(false);
	}

}
