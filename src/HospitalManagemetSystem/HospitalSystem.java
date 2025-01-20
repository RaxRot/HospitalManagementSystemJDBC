package HospitalManagemetSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String user = "root";
    private static final String password = "19649072Sever";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection= DriverManager.getConnection(url,user,password);

            Patient patient=new Patient(connection,scanner);
            Doctor doctor=new Doctor(connection,scanner);

            boolean isWorking=true;
            while(isWorking){

                showOptions();

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                    patient.addPatient();
                        break;
                    case 2:
                    patient.viewAllPatients();
                        break;
                    case 3:
                    patient.viewPatientById();
                        break;
                    case 4:
                    patient.updatePatient();
                        break;
                    case 5:
                    patient.deletePatient();
                        break;
                    case 6:
                    doctor.addDoctor();
                        break;
                    case 7:
                    doctor.viewAllDoctors();
                        break;
                    case 8:
                    doctor.viewDoctorById();
                        break;
                    case 9:
                    doctor.updateDoctor();
                        break;
                    case 10:
                    doctor.deleteDoctor();
                        break;
                    case 11:
                    bookAppointment(patient,doctor,connection,scanner);
                        break;
                        case 0:
                            isWorking=false;
                            System.out.println("Exit");
                            break;
                    default:
                        System.out.println("Invalid choice, please try again.");
                        break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private static void showOptions() {
        System.out.println("Hospital Management System");
        System.out.println();
        System.out.println("------PATIENTS-----");
        System.out.println("1. Add patient");
        System.out.println("2. View all patients");
        System.out.println("3. View patient by id");
        System.out.println("4. Update patient");
        System.out.println("5. Delete patient");
        System.out.println("-------------");
        System.out.println();

        System.out.println("---DOCTORS---");
        System.out.println("6. Add doctor");
        System.out.println("7. View all doctors");
        System.out.println("8. View doctor by id");
        System.out.println("9. Update doctor");
        System.out.println("10. Delete doctor");
        System.out.println("-------------");
        System.out.println();

        System.out.println("---APPOINTMENTS----");
        System.out.println("11. Book Appointment");
        System.out.println("-------------");
        System.out.println();

        System.out.println("---OTHER---");
        System.out.println("0. Exit");
        System.out.println("-------------");
    }

    public static void bookAppointment(Patient patient,Doctor doctor,
                                       Connection connection,Scanner scanner){

        System.out.println("Enter patient ID");
        int patientID=scanner.nextInt();
        System.out.println("Enter doctor ID");
        int doctorID=scanner.nextInt();

        System.out.println("Enter appointment Date (YYYY-MM-DD)");
        String appointmentDate=scanner.next();

        if (patient.isPatientExist(patientID) && doctor.isDoctorExist(doctorID)){
            if (checkDoorAvailability(doctorID,appointmentDate,connection)){
                String appointmentQuery="INSERT INTO APPOINTMENTS (patient_id,doctor_id,appointment_date) VALUES (?,?,?)";
                try(PreparedStatement preparedStatement=connection.prepareStatement(appointmentQuery)){
                    preparedStatement.setInt(1,patientID);
                    preparedStatement.setInt(2,doctorID);
                    preparedStatement.setString(3,appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected>0){
                        System.out.println("Appointment Booked");
                    }else{
                        System.out.println("Appointment Not Booked");
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else{
                System.out.println("Doctor not available on this date.");
            }
        }else{
            System.out.println("Patient does not exist or doctor does not exist");
        }
    }

    private static boolean checkDoorAvailability(int doctorID, String appointmentDate,Connection connection) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setInt(1,doctorID);
            ps.setString(2,appointmentDate);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                int count=rs.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
