package netapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginScreen extends JFrame implements ActionListener {
	private static final String DB_URL = "";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";
    
    private JTextField emailField;
    private JTextField passField;
    private JButton loginButton;
    
    private Connection conn;

    public LoginScreen() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        emailField = new JTextField(20);
        
        passField = new JPasswordField();
        
        loginButton = new JButton("Login");

        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        panel.add(loginButton);

        add(panel, BorderLayout.CENTER);

        loginButton.addActionListener(this);

        checkJDBCdriver();
        dbConnect();
    }
    
    public void showMessage(String msg) {
    	JOptionPane.showMessageDialog(null, msg);
    }
    
    private void checkJDBCdriver() {
    	try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver Found!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found!");
        }
    }
    
    private void dbConnect() {
    	try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connection established : "+conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private User authenticateUser(String email) { // changed return type to User so we can parse the query info to other classes
        String sql = "SELECT * FROM public.member WHERE email = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
			st.setString(1, email);
	
			ResultSet result = st.executeQuery();
			if (result.next()){
                String password = result.getString("thePassword");
                if (password.equals(passField.getText())) {

                    String name = result.getString("firstName");
                    String surname = result.getString("secondName");
                    String gender = result.getString("gender");
                    String country = result.getString("country");
                    Date dateOfBirth = result.getDate("dateOfBirth");
                    User user = new User(name, surname, email, country, gender, dateOfBirth);

                    result.close();
                    return user;
                }
                System.out.println("Incorrect password!");
                return null;
			}
			else{
                result.close();
                System.out.println("Email does not exist!");
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
            return null;
		}
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
        String email = emailField.getText();
        User user = authenticateUser(email);
        if (user != null) {
            dispose();
            new MainScreen(conn, user).setVisible(true);
        } else {
            showMessage("Login failed!");
            emailField.setText("");
            passField.setText("");
        }
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginScreen().setVisible(true);
            }
        });
    }
}