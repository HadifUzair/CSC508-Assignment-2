import java.util.Scanner;

public class StudentSystem {

    // The custom Data Structure
    static StudentHashTable db = new StudentHashTable(100); 
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // FIXED: Call loadData using the db object
        db.loadData("student_data.txt");
        
        int choice = -1;
        while (choice != 0) {
            printMenu();
            System.out.print("Enter choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addStudentUI();
                    break;
                case 2:
                    enrollSubjectUI();
                    break;
                case 3:
                    dropSubjectUI();
                    break;
                case 4:
                    searchByIDUI();
                    break;
                case 5:
                    searchByNameUI();
                    break;
                case 6:
                    db.displayAllUnsorted(); 
                    break;
                case 7:
                    db.displaySortedByName();
                    break;
                case 8:
                    displaySubjectsUI();
                    break;
                case 9:
                    deleteStudentUI();
                    break;
                case 10:
                    // FIXED: Call saveData using the db object
                    db.saveData("student_data.txt");
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Student Enrollment Management System ===");
        System.out.println("1. Add New Student");
        System.out.println("2. Enroll Subject");
        System.out.println("3. Drop Subject");
        System.out.println("4. Search Student by ID");
        System.out.println("5. Search Student by Name");
        System.out.println("6. Display All Students (Unsorted)");
        System.out.println("7. Display All Students (Sorted by Name)");
        System.out.println("8. Display Subjects for a Student");
        System.out.println("9. Delete Student");
        System.out.println("10. Save & Exit");
    }

    // --- UI Helper Methods ---

    private static void addStudentUI() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        
        if (db.addStudent(id, name)) {
            System.out.println("Student added successfully.");
        }
    }

    private static void enrollSubjectUI() {
        System.out.print("Enter Student ID to enroll: ");
        String id = scanner.nextLine();
        Student s = db.searchByID(id);
        
        if (s != null) {
            System.out.print("Enter Subject Code: ");
            String code = scanner.nextLine();
            System.out.print("Enter Subject Name: ");
            String name = scanner.nextLine();
            s.addSubject(code, name); 
            System.out.println("Subject enrolled.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void dropSubjectUI() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        Student s = db.searchByID(id);
        
        if (s != null) {
            System.out.print("Enter Subject Code to drop: ");
            String code = scanner.nextLine();
            
            if (s.enrolledSubjectsHead == null) {
                System.out.println("No subjects to drop.");
                return;
            }

            // Case 1: Head needs removal
            if (s.enrolledSubjectsHead.subjectCode.equals(code)) {
                s.enrolledSubjectsHead = s.enrolledSubjectsHead.next;
                System.out.println("Subject dropped.");
                return;
            }

            // Case 2: Middle or End
            Subject current = s.enrolledSubjectsHead;
            while (current.next != null) {
                if (current.next.subjectCode.equals(code)) {
                    current.next = current.next.next; // Bypass the node
                    System.out.println("Subject dropped.");
                    return;
                }
                current = current.next;
            }
            System.out.println("Subject code not found.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void searchByIDUI() {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        Student s = db.searchByID(id);
        if (s != null) {
            System.out.println("Found: " + s.name);
        } else {
            System.out.println("Not found.");
        }
    }

    private static void searchByNameUI() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        db.searchByName(name);
    }

    private static void displaySubjectsUI() {
        System.out.print("Enter Student ID: ");
        String id = scanner.nextLine();
        Student s = db.searchByID(id);
        if (s != null) {
            System.out.println("Subjects for " + s.name + ":");
            Subject current = s.enrolledSubjectsHead;
            while (current != null) {
                System.out.println("- " + current.subjectCode + ": " + current.subjectName);
                current = current.next;
            }
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void deleteStudentUI() {
        System.out.print("Enter Student ID to delete: ");
        String id = scanner.nextLine();
        if (db.deleteStudent(id)) {
            System.out.println("Student deleted.");
        } else {
            System.out.println("Student not found or could not be deleted.");
        }
    }
}