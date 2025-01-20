package HospitalManagemetSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection connection;
    private Scanner scanner;

    public Doctor(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addDoctor() {
        System.out.println("Enter the name of the doctor");
        String name = scanner.next();
        System.out.println("Enter the specialization of the patient");
        String specialization = scanner.next();

        scanner.nextLine();

        String sql = "INSERT INTO doctors (name,specialization) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, specialization);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Doctor added successfully");
            } else {
                System.out.println("Failed to add doctor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAllDoctors() {
        String sql = "SELECT * FROM doctors";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String specialization = rs.getString("specialization");
                System.out.println("-------------------------------------");
                System.out.println(String.format("%d %s %s", id, name, specialization));
                System.out.println("-------------------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewDoctorById() {
        System.out.println("Enter the ID of the doctor to view:");
        int id = scanner.nextInt();

        String sql = "SELECT * FROM doctors WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idDoctor = rs.getInt("id");
                    String name = rs.getString("name");
                    String specialization = rs.getString("specialization");
                    System.out.println("-------------------------------------");
                    System.out.println(String.format("%d %s %s", idDoctor, name, specialization));
                    System.out.println("-------------------------------------");
                } else {
                    System.out.println("No doctors found with ID: " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctor() {
        System.out.println("Enter the ID of the patient to update:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter the new name of the doctor:");
        String name = scanner.nextLine();

        System.out.println("Enter the new specialization of the doctor:");
        String specialization = scanner.nextLine();

        String sql="UPDATE doctors SET name = ?, specialization = ? WHERE id = ?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.setInt(3, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Doctor updated successfully");
            }else{
                System.out.println("Failed to update doctor");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteDoctor() {
        System.out.println("Enter the ID of the doctor to delete:");
        int id = scanner.nextInt();
        String sql = "DELETE FROM doctors WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Doctor deleted successfully.");
            } else {
                System.out.println("No doctor found with ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isDoctorExist(int id) {
        String sql = "SELECT * FROM doctors WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
