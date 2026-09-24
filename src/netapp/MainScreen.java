package netapp;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends JFrame implements ActionListener{
    private Connection conn;

    private User user;
    
    private JPanel personalDetailsPanel;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField dobField;
    private JTextField countryField;
    private JButton updateButton;
    
    private JList<Member> networkList;
    private DefaultListModel<Member> networkListModel;
    
    

    public MainScreen(Connection conn, User user) {
        this.conn = conn;
        this.user = user;

        setTitle("Home");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        
        personalDetailsPanel = new JPanel(new GridLayout(5, 2,10,10));
        personalDetailsPanel.setMaximumSize(new Dimension(600,200));
        personalDetailsPanel.setBorder(new EmptyBorder(10,10,10,10));
        personalDetailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        personalDetailsPanel.add(new JPanel(new FlowLayout(FlowLayout.TRAILING)).add(new JLabel("First Name:")));
        firstNameField = new JTextField();
        personalDetailsPanel.add(firstNameField);

        personalDetailsPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        personalDetailsPanel.add(lastNameField);

        personalDetailsPanel.add(new JLabel("Date of Birth:"));
        dobField = new JTextField();
        personalDetailsPanel.add(dobField);
        
        personalDetailsPanel.add(new JLabel("Country:"));
        countryField = new JTextField();
        personalDetailsPanel.add(countryField);

        personalDetailsPanel.add(new JLabel());
        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        personalDetailsPanel.add(updateButton);

        networkListModel = new DefaultListModel<>();
        networkList = new JList<>(networkListModel);
        networkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        networkList.addListSelectionListener(new ListSelectionListener() {  // 
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    new MemberDetailScreen(conn, networkList.getSelectedValue(), user).setVisible(true);
                }
            }
        });
        
        add(personalDetailsPanel);
        JLabel nusers = new JLabel("My Network");
        nusers.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(nusers);
        JScrollPane jpane = new JScrollPane(networkList);
        jpane.setAlignmentX(Component.LEFT_ALIGNMENT);
        jpane.setBorder(new EmptyBorder(10,10,10,10));
        add(jpane);

        fetchPersonalDetails();
        fetchNetwork();
    }
    
    public void showMessage(String msg) {
    	JOptionPane.showMessageDialog(null, msg);
    }
    
    private void fetchPersonalDetails() {
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getSecondName());
        dobField.setText(user.getDateOfBirth().toString());
        countryField.setText(user.getCountry());
    }
    
    private void updatePersonalDetails() {
        String sql = "UPDATE public.member SET \"firstName\" = ?, \"secondName\" = ?, \"dateOfBirth\" = ?, country = ? WHERE email = ?";
        
        String formatPattern = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatPattern);
        
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            java.util.Date dateOfBirth = dateFormat.parse(dobField.getText());

            String newFirstName = firstNameField.getText(); // update Member instance from JTextField
            String newSecondName = lastNameField.getText();
            Date newDateOfBirth = new java.sql.Date(dateOfBirth.getTime());
            String newCountry = countryField.getText();

            st.setString(1, newFirstName);
            st.setString(2, newSecondName);
            st.setDate(3, newDateOfBirth);
            st.setString(4, newCountry);
            st.setString(5, user.getEmail());
            
            int rowsUpdated = st.executeUpdate();
            
            if (rowsUpdated > 0) {
                showMessage("Updated personal details!");
            } else {
                showMessage("Failed to update personal details!");
            }
            
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
        }
    }
    

    private void fetchNetwork() {
    	String sql = "SELECT * FROM public.connects LEFT JOIN public.member ON member.email = \"connectedWithEmail\" WHERE connects.email = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
			st.setString(1, user.getEmail());
	
			ResultSet result = st.executeQuery();
            List<Member> memberList = new ArrayList<>();

			while (result.next()){
                String connectedEmail = result.getString("connectedWithEmail");
                String connectedName = result.getString("firstName");
                String connectedSurname = result.getString("secondName");

                Member member = new Member(connectedName, connectedSurname, connectedEmail);
                memberList.add(member);
			}
            Member[] membersList = memberList.toArray(new Member[0]);
            networkList.setListData(membersList);

		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		updatePersonalDetails();
	}
}
