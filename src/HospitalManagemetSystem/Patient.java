package HospitalManagemetSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        System.out.println("Enter the name of the patient");
        String name = scanner.nextLine();
        System.out.println("Enter the age of the patient");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter the gender of the patient");
        String gender = scanner.nextLine();

        String sql = "INSERT INTO patient (name, age, gender) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Patient added successfully");
            } else {
                System.out.println("Failed to add patient");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAllPatients() {
        String sql = "SELECT * FROM patient";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                System.out.println("-------------------------------------");
                System.out.println(String.format("%d %s %d %s", id, name, age, gender));
                System.out.println("-------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatientById(int id) {
        String sql = "SELECT * FROM patient WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int patientId = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    System.out.println("-------------------------------------");
                    System.out.println(String.format("%d %s %d %s", patientId, name, age, gender));
                    System.out.println("-------------------------------------");
                } else {
                    System.out.println("No patient found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatient(int id, String name, int age, String gender) {
        String sql="UPDATE patient SET name = ?, age = ?, gender = ? WHERE id = ?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setInt(4, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient updated successfully");
            }else{
                System.out.println("Failed to update patient");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deletePatient(int id) {
        String sql = "DELETE FROM patient WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient deleted successfully.");
            } else {
                System.out.println("No patient found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
