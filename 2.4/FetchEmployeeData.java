import java.sql.*;
import java.util.*;

// =============================
// PART A: CONNECTING TO MYSQL & FETCHING DATA
// =============================
class FetchEmployeeData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/nimbusdb"; // change database name if needed
        String user = "root"; // change if your MySQL username differs
        String password = "root"; // change if your MySQL password differs

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
            Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Employee");

            System.out.println("\n--- Employee Table Data ---");
            while (rs.next()) {
                int id = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");
                System.out.printf("EmpID: %d | Name: %-10s | Salary: %.2f%n", id, name, salary);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// =============================
// PART B: CRUD OPERATIONS ON PRODUCT TABLE
// =============================
class ProductCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/nimbusdb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n====== PRODUCT MANAGEMENT SYSTEM ======");
            System.out.println("1. Add Product");
            System.out.println("2. Display All Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> insertProduct(sc);
                case 2 -> displayProducts();
                case 3 -> updateProduct(sc);
                case 4 -> deleteProduct(sc);
                case 5 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // CREATE
    private static void insertProduct(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            con.setAutoCommit(false);
            String sql = "INSERT INTO Product (ProductID, ProductName, Price, Quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            System.out.print("Enter Product ID: ");
            ps.setInt(1, sc.nextInt());
            sc.nextLine();
            System.out.print("Enter Product Name: ");
            ps.setString(2, sc.nextLine());
            System.out.print("Enter Price: ");
            ps.setDouble(3, sc.nextDouble());
            System.out.print("Enter Quantity: ");
            ps.setInt(4, sc.nextInt());

            ps.executeUpdate();
            con.commit();
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ
    private static void displayProducts() {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Product")) {

            System.out.println("\n--- Product Table ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %-10s | Price: %.2f | Quantity: %d%n",
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    private static void updateProduct(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            con.setAutoCommit(false);
            String sql = "UPDATE Product SET Price=?, Quantity=? WHERE ProductID=?";
            PreparedStatement ps = con.prepareStatement(sql);

            System.out.print("Enter Product ID to Update: ");
            int id = sc.nextInt();
            System.out.print("Enter New Price: ");
            double price = sc.nextDouble();
            System.out.print("Enter New Quantity: ");
            int qty = sc.nextInt();

            ps.setDouble(1, price);
            ps.setInt(2, qty);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                con.commit();
                System.out.println("Product updated successfully!");
            } else {
                con.rollback();
                System.out.println("Product not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    private static void deleteProduct(Scanner sc) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            con.setAutoCommit(false);
            String sql = "DELETE FROM Product WHERE ProductID=?";
            PreparedStatement ps = con.prepareStatement(sql);

            System.out.print("Enter Product ID to Delete: ");
            ps.setInt(1, sc.nextInt());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                con.commit();
                System.out.println("Product deleted successfully!");
            } else {
                con.rollback();
                System.out.println("Product not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// =============================
// PART C: STUDENT MANAGEMENT APP (MVC ARCHITECTURE)
// =============================

// ----- MODEL -----
class Student {
    int studentID;
    String name;
    String department;
    double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }
}

// ----- CONTROLLER -----
class StudentController {
    private static final String URL = "jdbc:mysql://localhost:3306/nimbusdb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public void addStudent(Student s) {
        String sql = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, s.studentID);
            ps.setString(2, s.name);
            ps.setString(3, s.department);
            ps.setDouble(4, s.marks);
            ps.executeUpdate();
            System.out.println("Student added successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewAllStudents() {
        String sql = "SELECT * FROM Student";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Student Records ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %-10s | Dept: %-10s | Marks: %.2f%n",
                        rs.getInt("StudentID"),
                        rs.getString("Name"),
                        rs.getString("Department"),
                        rs.getDouble("Marks"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudent(int id, double marks) {
        String sql = "UPDATE Student SET Marks=? WHERE StudentID=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, marks);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Student record updated!");
            else
                System.out.println("Student not found!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {
        String sql = "DELETE FROM Student WHERE StudentID=?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Student record deleted!");
            else
                System.out.println("Student not found!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// ----- VIEW -----
class StudentView {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentController controller = new StudentController();

        while (true) {
            System.out.println("\n====== STUDENT MANAGEMENT SYSTEM ======");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student Marks");
            System.out.println("4. Delete Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Student ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Department: ");
                    String dept = sc.nextLine();
                    System.out.print("Enter Marks: ");
                    double marks = sc.nextDouble();
                    controller.addStudent(new Student(id, name, dept, marks));
                }
                case 2 -> controller.viewAllStudents();
                case 3 -> {
                    System.out.print("Enter Student ID to Update: ");
                    int id = sc.nextInt();
                    System.out.print("Enter New Marks: ");
                    double marks = sc.nextDouble();
                    controller.updateStudent(id, marks);
                }
                case 4 -> {
                    System.out.print("Enter Student ID to Delete: ");
                    int id = sc.nextInt();
                    controller.deleteStudent(id);
                }
                case 5 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
