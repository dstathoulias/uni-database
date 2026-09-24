package netapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDetailScreen extends JFrame {
    private Connection conn;
    private User user;
    private Member member;
    private JTextArea detailsArea;
    private JTextArea messagesArea;
    private JTextArea educationArea;
    private JTextArea experienceArea;

    public MemberDetailScreen(Connection conn, Member member, User user) {
        this.conn = conn;
        this.member = member;
        this.user = user;

        setTitle("Details for :"+member);
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        detailsArea = new JTextArea(5, 40);
        detailsArea.setEditable(false);
        messagesArea = new JTextArea(10, 40);
        messagesArea.setEditable(false);
        educationArea = new JTextArea(10, 40);
        educationArea.setEditable(false);
        experienceArea = new JTextArea(10, 40);
        experienceArea.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBorder(new EmptyBorder(20,20,20,20));
        
        panel.add(new JLabel("Personal Details"));
        panel.add(new JScrollPane(detailsArea));
        panel.add(new JLabel("Messages"));
        panel.add(new JScrollPane(messagesArea));
        panel.add(new JLabel("Education"));
        panel.add(new JScrollPane(educationArea));
        panel.add(new JLabel("Experience"));
        panel.add(new JScrollPane(experienceArea));

        add(panel);

        fetchMemberDetails();
        fetchMessages();
        fetchEducation();
        fetchExperience();
    }

    private void fetchMemberDetails() {
        String sql = "SELECT * FROM public.member WHERE email = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
			st.setString(1, member.getEmail());
	
			ResultSet result = st.executeQuery();
			if (result.next()){
                String name = result.getString("firstName");
                String surname = result.getString("secondName");
                String gender = result.getString("gender");
                String country = result.getString("country");
                Date dateOfBirth = result.getDate("dateOfBirth");
                User connectedUser = new User(name, surname, member.getEmail(), country, gender, dateOfBirth);

                String memberName = "Name: " + connectedUser.getFirstName() + " " + connectedUser.getSecondName();
                String memberGender = "Gender: " + connectedUser.getGender();
                String memberCountry = "Country: " + connectedUser.getCountry();
                String memberDateOfBirth = "Date of Birth: " + connectedUser.getDateOfBirth();
                String memberEmail = "Email: " + member.getEmail();

                detailsArea.append(memberEmail);
                detailsArea.append("\n");
                detailsArea.append(memberName);
                detailsArea.append("\n");
                detailsArea.append(memberGender);
                detailsArea.append("\n");
                detailsArea.append(memberCountry);
                detailsArea.append("\n");
                detailsArea.append(memberDateOfBirth);

                result.close();
            } else {
                System.out.println("There was an error fetching the member!");
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }

    private void fetchMessages() {
    	String sql = "SELECT \"theText\" FROM public.msg WHERE \"senderEmail\" = ? AND \"receiverEmail\" = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
			st.setString(1, member.getEmail());
            st.setString(2, user.getEmail());
	
			ResultSet result = st.executeQuery();
			while (result.next()) {
                messagesArea.append(result.getString("theText"));
                messagesArea.append("\n");
            }

            result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    private void fetchEducation() {
    	String sql = "SELECT * FROM public.education WHERE email = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
			st.setString(1, member.getEmail());
	
			ResultSet result = st.executeQuery();
			while (result.next()) {
                String educationCountry = "Country: " + result.getString("country");
                String educationSchool = "School: " + result.getString("school");
                String educationLevel= "Education Level: " + result.getString("eduLevel");
                String educationCategory = "Category: " + result.getInt("categoryID");
                String educationFromDate = "From: " + result.getDate("fromYear");
                String educationToDate = "To: " + result.getDate("toYear");

                educationArea.append(educationCountry);
                educationArea.append("\n");
                educationArea.append(educationSchool);
                educationArea.append("\n");
                educationArea.append(educationLevel);
                educationArea.append("\n");
                educationArea.append(educationCategory);
                educationArea.append("\n");
                educationArea.append(educationFromDate);
                educationArea.append("\n");
                educationArea.append(educationToDate);
                educationArea.append("\n\n");
            }

            result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    private void fetchExperience() {
    	String sql = "SELECT * FROM public.experience WHERE email = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
			st.setString(1, member.getEmail());
	
			ResultSet result = st.executeQuery();
			while (result.next()) {
                String experienceCompany = "Company: " + result.getString("company");
                String experienceWorkStatus = "Work Status: " + result.getString("workStatus");
                String experienceTitle = "Title: " + result.getString("title");
                String experienceDescription = "Description: " + result.getString("description");
                String experienceFromDate = "From: " + result.getDate("fromYear");
                String experienceToDate = "To: " + result.getDate("toYear");

                experienceArea.append(experienceCompany);
                experienceArea.append("\n");
                experienceArea.append(experienceWorkStatus);
                experienceArea.append("\n");
                experienceArea.append(experienceTitle);
                experienceArea.append("\n");
                experienceArea.append(experienceDescription);
                experienceArea.append("\n");
                experienceArea.append(experienceFromDate);
                experienceArea.append("\n");
                experienceArea.append(experienceToDate);
                experienceArea.append("\n");
            }

            result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}

