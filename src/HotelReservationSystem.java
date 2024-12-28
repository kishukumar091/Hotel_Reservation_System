import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/HotelReservationSystem";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "7545079980";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {

            Class.forName("com.mysql.cj.jdbc.Driver");

            while (true) {
                System.out.println("\n    -:HOTEL MANAGEMENT SYSTEM:- ");
                System.out.println("1. Reserve a Room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("6. Search Reservations");
                System.out.println("7. Exit");
                System.out.print("Choose an Option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> reserveRoom(scanner, connection);
                    case 2 -> viewReservations(connection);
                    case 3 -> getRoomNumber(scanner, connection);
                    case 4 -> updateReservation(scanner, connection);
                    case 5 -> deleteReservation(scanner, connection);
                    case 6 -> searchReservations(scanner, connection);
                    case 7 -> {
                        exit();
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try Again.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reserveRoom(Scanner scanner, Connection connection) {
        System.out.print("Enter Guest Name: ");
        String guestName = scanner.nextLine();
        System.out.print("Enter Room Number: ");
        int roomNumber = scanner.nextInt();
        System.out.print("Enter Contact Number: ");
        String contactNumber = scanner.next();

        String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, guestName);
            pstmt.setInt(2, roomNumber);
            pstmt.setString(3, contactNumber);
            int rowsInserted = pstmt.executeUpdate();
            System.out.println(rowsInserted > 0 ? "Reservation Successful!" : "Reservation Failed.");
        } catch (SQLException e) {
            System.err.println("Error reserving room: " + e.getMessage());
        }
    }

    public static void viewReservations(Connection connection) {
        String sql = "SELECT * FROM reservations";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("                                  -:Current Reservations:-");
            System.out.println("*----------------*-----------------*--------------*---------------------*--------------------------*");
            System.out.println("| Reservation Id | Guest Name      | Room Number  | Contact Number      | Reservation Date         |");
            System.out.println("*----------------*-----------------*--------------*---------------------*--------------------------*");

            while (rs.next()) {
                System.out.printf("| %-14d | %-15s | %-12d | %-19s | %-24s |%n",
                        rs.getInt("reservation_id"),
                        rs.getString("guest_name"),
                        rs.getInt("room_number"),
                        rs.getString("contact_number"),
                        rs.getTimestamp("reservation_date"));
                System.out.println("*----------------*-----------------*--------------*---------------------*--------------------------*");
            }
        } catch (SQLException e) {
            System.err.println("Error viewing reservations: " + e.getMessage());
        }
    }

    public static void getRoomNumber(Scanner scanner, Connection connection) {
        System.out.print("Enter Reservation ID: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Guest Name: ");
        String guestName = scanner.nextLine();

        String sql = "SELECT room_number FROM reservations WHERE reservation_id = ? AND guest_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setString(2, guestName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Room Number: " + rs.getInt("room_number"));
                } else {
                    System.out.println("No reservation found for the given details.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching room number: " + e.getMessage());
        }
    }

    public static void updateReservation(Scanner scanner, Connection connection) {
        System.out.print("Enter Reservation ID to update: ");
        int reservationId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (!reservationExists(reservationId, connection)) {
            System.out.println("Reservation not found.");
            return;
        }

        System.out.print("Enter new Guest Name: ");
        String guestName = scanner.nextLine();
        System.out.print("Enter new Room Number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new Contact Number: ");
        String contactNumber = scanner.nextLine();

        String sql = "UPDATE reservations SET guest_name = ?, room_number = ?, contact_number = ? WHERE reservation_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, guestName);
            pstmt.setInt(2, roomNumber);
            pstmt.setString(3, contactNumber);
            pstmt.setInt(4, reservationId);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "Reservation updated successfully." : "Reservation update failed.");
        } catch (SQLException e) {
            System.err.println("Error updating reservation: " + e.getMessage());
        }
    }

    public static void deleteReservation(Scanner scanner, Connection connection) {
        System.out.print("Enter Reservation ID: ");
        int reservationId = scanner.nextInt();

        if (!reservationExists(reservationId, connection)) {
            System.out.println("Reservation not found.");
            return;
        }

        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted > 0 ? "Reservation deleted successfully." : "Reservation deletion failed.");
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
        }
    }

    public static void searchReservations(Scanner scanner, Connection connection) {
        try {
            System.out.println("Search Reservations by:");
            System.out.println("1. Guest Name");
            System.out.println("2. Room Number");
            System.out.println("3. Reservation Date");
            System.out.print("Choose an option: ");
            int searchChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String sqlQuery = "";
            switch (searchChoice) {
                case 1:
                    System.out.print("Enter Guest Name (or part of it): ");
                    String guestName = scanner.nextLine();
                    sqlQuery = "SELECT * FROM reservations WHERE guest_name LIKE '%" + guestName + "%'";
                    break;

                case 2:
                    System.out.print("Enter Room Number: ");
                    int roomNumber = scanner.nextInt();
                    sqlQuery = "SELECT * FROM reservations WHERE room_number = " + roomNumber;
                    break;

                case 3:
                    System.out.print("Enter Reservation Date (YYYY-MM-DD): ");
                    String reservationDate = scanner.nextLine();
                    sqlQuery = "SELECT * FROM reservations WHERE DATE(reservation_date) = '" + reservationDate + "'";
                    break;

                default:
                    System.out.println("Invalid choice. Returning to main menu.");
                    return;
            }

            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery(sqlQuery);
                StringBuilder table = new StringBuilder();
                String separator = "*----------------*-----------------*--------------*---------------------*--------------------------*\n";
                String header = "| Reservation Id | Guest Name      | Room Number  | Contact Number      | Reservation Date         |\n";

                table.append(separator);
                table.append(header);
                table.append(separator);
                System.out.println("                                  -:Search Results:-                                        ");
                System.out.println("*----------------*-----------------*--------------*---------------------*--------------------------*");
                System.out.println("| Reservation Id | Guest Name      | Room Number  | Contact Number      | Reservation Date         |");
                System.out.println("*----------------*-----------------*--------------*---------------------*--------------------------*");

                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    int reservationId = rs.getInt("reservation_id");
                    String resultGuestName = rs.getString("guest_name");
                    int resultRoomNumber = rs.getInt("room_number");
                    String contactNumber = rs.getString("contact_number");
                    String reservationDateResult = rs.getTimestamp("reservation_date").toString();

                    System.out.printf("| %-14d | %-15s | %-12d | %-19s | %-24s |\n",
                            reservationId, resultGuestName, resultRoomNumber, contactNumber, reservationDateResult);
                    System.out.println("*----------------*-----------------*--------------*---------------------*--------------------------*");

                    String row = String.format("| %-14d | %-15s | %-12d | %-19s | %-24s |\n",
                            reservationId, resultGuestName, resultRoomNumber, contactNumber, reservationDateResult);
                    table.append(row);
                    table.append(separator);
                }

                if (!hasResults) {
                    System.out.println("No reservations match your search criteria.");
                    return;
                }
                System.out.print("Do you want to save the results to a file? (yes/no): ");
                String saveToFile = scanner.nextLine();
                if (saveToFile.equalsIgnoreCase("yes")) {
                    System.out.print("Enter file name (with extension, e.g., results.csv): ");
                    String fileName = scanner.nextLine();
                    saveResultsToFile(table.toString(), fileName);
                }
            } catch (SQLException e) {
                System.out.println("Error executing search: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static void saveResultsToFile(String data, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(data);
            System.out.println("Results saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static boolean reservationExists(int reservationId, Connection connection) {
        String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking reservation: " + e.getMessage());
            return false;
        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        for (int i = 0; i < 5; i++) {
            System.out.print(".");
            Thread.sleep(450);
        }
        System.out.println("\nThank you for using the Hotel Reservation System!");
    }
}
