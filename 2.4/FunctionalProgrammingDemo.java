import java.util.*;
import java.util.stream.*;

public class FunctionalProgrammingDemo {

    // ======== Part A: Employee Sorting ========
    static class Employee {
        String name;
        int age;
        double salary;

        Employee(String name, int age, double salary) {
            this.name = name;
            this.age = age;
            this.salary = salary;
        }

        @Override
        public String toString() {
            return name + " (Age: " + age + ", Salary: " + salary + ")";
        }
    }

    // ======== Part B: Student Filtering & Sorting ========
    static class Student {
        String name;
        double marks;

        Student(String name, double marks) {
            this.name = name;
            this.marks = marks;
        }
    }

    // ======== Part C: Product Stream Operations ========
    static class Product {
        String name;
        double price;
        String category;

        Product(String name, double price, String category) {
            this.name = name;
            this.price = price;
            this.category = category;
        }

        @Override
        public String toString() {
            return name + " (" + category + ", $" + price + ")";
        }
    }

    public static void main(String[] args) {

        // ================================
        // Part A: Sorting Employee Objects
        // ================================
        System.out.println("=== Part A: Employee Sorting ===");
        List<Employee> employees = new ArrayList<>(Arrays.asList(
            new Employee("Alice", 30, 60000),
            new Employee("Bob", 25, 50000),
            new Employee("Charlie", 28, 70000),
            new Employee("David", 35, 55000)
        ));

        // Sort by name
        employees.sort(Comparator.comparing(e -> e.name));
        System.out.println("Sorted by name:");
        employees.forEach(System.out::println);

        // Sort by age
        employees.sort(Comparator.comparingInt(e -> e.age));
        System.out.println("\nSorted by age:");
        employees.forEach(System.out::println);

        // Sort by salary descending
        employees.sort(Comparator.comparingDouble((Employee e) -> e.salary).reversed());
        System.out.println("\nSorted by salary (descending):");
        employees.forEach(System.out::println);

        // ================================
        // Part B: Student Filtering & Sorting
        // ================================
        System.out.println("\n=== Part B: Student Filtering and Sorting ===");
        List<Student> students = Arrays.asList(
            new Student("Emily", 82),
            new Student("Frank", 65),
            new Student("Grace", 90),
            new Student("Hannah", 74),
            new Student("Ian", 88)
        );

        List<String> topStudents = students.stream()
            .filter(s -> s.marks > 75)
            .sorted(Comparator.comparingDouble(s -> s.marks))
            .map(s -> s.name)
            .collect(Collectors.toList());

        System.out.println("Students scoring above 75%, sorted by marks:");
        topStudents.forEach(System.out::println);

        // ================================
        // Part C: Product Stream Operations
        // ================================
        System.out.println("\n=== Part C: Product Stream Operations ===");
        List<Product> products = Arrays.asList(
            new Product("Laptop", 1200, "Electronics"),
            new Product("TV", 800, "Electronics"),
            new Product("Smartphone", 1000, "Electronics"),
            new Product("Chair", 150, "Furniture"),
            new Product("Table", 250, "Furniture"),
            new Product("Sofa", 500, "Furniture"),
            new Product("Pen", 2.5, "Stationery"),
            new Product("Notebook", 5.0, "Stationery")
        );

        // Group by category
        Map<String, List<Product>> productsByCategory = products.stream()
            .collect(Collectors.groupingBy(p -> p.category));

        System.out.println("Products grouped by category:");
        productsByCategory.forEach((category, prodList) -> {
            System.out.println(category + ": " + prodList);
        });

        // Most expensive product in each category
        Map<String, Optional<Product>> maxProductByCategory = products.stream()
            .collect(Collectors.groupingBy(
                p -> p.category,
                Collectors.maxBy(Comparator.comparingDouble(p -> p.price))
            ));

        System.out.println("\nMost expensive product in each category:");
        maxProductByCategory.forEach((category, productOpt) -> {
            System.out.println(category + ": " + productOpt.orElse(null));
        });

        // Average price of all products
        double averagePrice = products.stream()
            .collect(Collectors.averagingDouble(p -> p.price));

        System.out.println("\nAverage price of all products: $" + averagePrice);
    }
}
