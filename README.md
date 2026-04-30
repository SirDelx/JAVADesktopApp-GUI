# JAVADesktopApp-GUI

A Java-based desktop GUI application with user authentication, role-based dashboards, and MySQL database integration.

## Features

- User authentication (Login/Signup)
- Role-based access control (Student, Instructor, Staff)
- MySQL database integration
- JDBC connection management
- Dashboard interfaces for different user roles

## Prerequisites

Before running this application, ensure you have:

- **Java Development Kit (JDK)** 8 or higher
- **NetBeans IDE** or any Java IDE
- **MySQL Server** installed and running
- **MySQL JDBC Driver** (mysql-connector-j)
- **phpMyAdmin** (for database management)

## Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/SirDelx/JAVADesktopApp-GUI.git
cd JAVADesktopApp-GUI
```

### 2. Add MySQL JDBC Driver

The MySQL JDBC driver is already included in the project at:
```
dist/lib/mysql-connector-j-9.7.0.jar
```

If you need to update or add it manually:

1. Download the latest MySQL Connector/J from: https://dev.mysql.com/downloads/connector/j/
2. Extract the `.jar` file
3. Copy it to the `dist/lib/` folder in your project
4. In NetBeans, right-click on your project → Properties → Libraries → Add JAR/Folder
5. Select the MySQL JDBC jar file

### 3. Create and Import the Database

#### Using phpMyAdmin:

1. **Open phpMyAdmin**
   - Go to `http://localhost/phpmyadmin` in your browser
   - Login with your MySQL credentials

2. **Create a New Database**
   - Click "New" in the left sidebar
   - Database name: `user_system`
   - Collation: `utf8mb4_unicode_ci`
   - Click "Create"

3. **Import the Database**
   - Select the `user_system` database from the left sidebar
   - Click the "Import" tab
   - Click "Choose File" and select `user_system.sql` from the project root
   - Click "Import"

#### Using MySQL Command Line:

```bash
mysql -u root -p
CREATE DATABASE user_system;
USE user_system;
SOURCE /path/to/user_system.sql;
```

### 4. Update Database Connection

Edit `src/guipackage/DBConnection.java` and update the connection details:

```java
String dbURL = "jdbc:mysql://localhost:3306/user_system";
String user = "root";  // Your MySQL username
String password = "";  // Your MySQL password
```

### 5. Build and Run the Application

#### In NetBeans:

1. Open the project in NetBeans
2. Right-click on the project → Clean and Build
3. Right-click on the project → Run

#### From Command Line:

```bash
javac -cp dist/lib/mysql-connector-j-9.7.0.jar src/guipackage/*.java
java -cp .:dist/lib/mysql-connector-j-9.7.0.jar guipackage.Main
```

## Default Login Credentials

After importing the database, use the following test credentials:

| Role | Username | Password |
|------|----------|----------|
| Student | student1 | password123 |
| Instructor | instructor1 | password123 |
| Staff | staff1 | password123 |

*Note: These are default test credentials. Change them in phpMyAdmin after first login.*

## Project Structure

```
GUIApp/
├── src/
│   └── guipackage/
│       ├── Main.java                 # Application entry point
│       ├── LoginFrame.java           # Login interface
│       ├── SignupFrame.java          # User registration
│       ├── DBConnection.java         # Database connection handler
│       ├── DashboardFrame.java       # Main dashboard
│       ├── StudentDashboard.java     # Student interface
│       ├── InstructorDashboard.java  # Instructor interface
│       ├── StaffDashboard.java       # Staff interface
│       └── WelcomeFrame.java         # Welcome screen
├── dist/
│   └── lib/
│       └── mysql-connector-j-9.7.0.jar  # MySQL JDBC driver
├── user_system.sql                   # Database schema and data
├── build.xml                         # Build configuration
└── README.md                         # This file
```

## Database Schema

The `user_system` database includes the following tables:

- **users** - User account information
- **roles** - User role definitions
- **user_roles** - User-role mappings

## Troubleshooting

### Connection Refused
```
Error: java.sql.SQLException: (conn=0) Server returned extended info-string: "Unknown character set index '255' for field 'email'"
```
**Solution:** Update MySQL JDBC driver to the latest version.

### Class Not Found Exception
```
Error: java.lang.ClassNotFoundException: com.mysql.cj.jdbc.Driver
```
**Solution:** Ensure the MySQL JDBC driver JAR is added to the classpath.

### Database Not Found
```
Error: Unknown database 'user_system'
```
**Solution:** Make sure you've imported the `user_system.sql` file into phpMyAdmin.

### Connection Timeout
```
Error: java.sql.SQLException: Timeout waiting for idle object
```
**Solution:** Verify MySQL Server is running on your system.

## Support & Contact

For issues or questions:
- Check the GitHub Issues page
- Review the source code comments
- Ensure all prerequisites are installed correctly

## License

This project is open source and available for educational purposes.

---

**Last Updated:** April 30, 2026
