# Hotel Reservation System

## Overview

The **Hotel Reservation System** is a Java-based application designed to streamline the process of booking, managing, and canceling hotel reservations. This project demonstrates the integration of Java with a MySQL database to manage hotel bookings effectively and efficiently. It features a console-based user interface for both guests and administrators.

## Features

- **Room Reservation:** Book rooms by providing guest details and room numbers.
- **View Reservations:** Display all current reservations in a tabular format.
- **Search Reservations:** Search for reservations by guest name, room number, or reservation date.
- **Update Reservations:** Modify existing bookings.
- **Delete Reservations:** Cancel reservations securely.
- **Export Search Results:** Save search results to a file (e.g., CSV format).

## Technologies Used

- **Programming Language:** Java
- **Database:** MySQL
- **Database Connector:** JDBC
- **IDE:** IntelliJ IDEA / Eclipse
- **Version Control:** Git

## Prerequisites

1. **Java Development Kit (JDK):** Ensure JDK 8 or above is installed.
2. **MySQL Database:** Set up a MySQL server and create a database named `HotelReservationSystem`.
3. **JDBC Driver:** Download and add the MySQL JDBC driver to your project.

## Database Setup

1. Create the `reservations` table using the following SQL script:

```sql
CREATE TABLE reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(255) NOT NULL,
    room_number INT NOT NULL,
    contact_number VARCHAR(15) NOT NULL,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

2. Update the database credentials in the code:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/HotelReservationSystem";
   private static final String USERNAME = "your_username";
   private static final String PASSWORD = "your_password";
   ```

## Installation and Usage

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/kishukumar091/Hotel_Reservation_System.git
   ```
2. **Open the Project:** Import the project into your preferred Java IDE.
3. **Configure Database:** Ensure the database connection details in the code match your MySQL setup.
4. **Run the Application:** Execute the `HotelReservationSystem` class to start the application.
5. **Interact with the System:** Use the menu-driven interface to perform operations like booking, updating, and deleting reservations.

## Application Menu

- **1. Reserve a Room:** Enter guest details to book a room.
- **2. View Reservations:** View all current reservations.
- **3. Get Room Number:** Retrieve the room number using a reservation ID and guest name.
- **4. Update Reservation:** Modify guest details or room information for an existing reservation.
- **5. Delete Reservation:** Cancel a reservation using its ID.
- **6. Search Reservations:** Find reservations using guest name, room number, or date.
- **7. Exit:** Close the application.

## Screenshots

(Include screenshots of the console interface and sample outputs if possible.)

## Future Enhancements

- Integration with online payment gateways.
- Mobile-friendly user interface.
- Multi-language support.
- Enhanced search with combined criteria.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments

- Thanks to online resources and tutorials for guidance on JDBC and MySQL integration.
- Special thanks to the contributors and reviewers for their valuable feedback.

