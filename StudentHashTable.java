import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class StudentHashTable {
    private Student[] table;
    private int size;
    private int studentCount; // Tracks number of students

    public StudentHashTable(int tableSize) {
        table = new Student[tableSize];
        size = tableSize;
        studentCount = 0;
    }

    //  HELPER -- Custom Hash Function 
    private int getHashIndex(String key) {
        int sum = 0;
        for (int i = 0; i < key.length(); i++) {
            sum += key.charAt(i);
        }
        return Math.abs(sum % size);
    }
    
      // Helper -- Sorting
    private Student[] getAllStudentsArray() {
        Student[] allStudents = new Student[studentCount];
        int count = 0;
        for (int i = 0; i < size; i++) {
            Student current = table[i];
            while (current != null) {
                allStudents[count++] = current;
                current = current.next;
            }
        }
        return allStudents;
    }

    // REQ 1 -- Add Student
    public boolean addStudent(String id, String name) {
        int index = getHashIndex(id);
        Student newStudent = new Student(id, name);

        if (table[index] == null) {
            table[index] = newStudent;
        } else {
            // Coll handling ~ Add to end of chain
            Student current = table[index];
            while (current != null) {
                if (current.studentID.equals(id)) {
                    System.out.println("Error: Duplicate ID not allowed.");
                    return false; // Duplicate found
                }
                if (current.next == null) break;
                current = current.next;
            }
            current.next = newStudent;
        }
        studentCount++;
        return true;
    }

    // REQ 4 -- Search by ID
    public Student searchByID(String id) {
        int index = getHashIndex(id);
        Student current = table[index];

        while (current != null) {
            if (current.studentID.equals(id)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    // REQ 5 -- Search by Name 
    public void searchByName(String searchName) {
        boolean found = false;
        // Linear search required since table is hashed by ID
        for (int i = 0; i < size; i++) {
            Student current = table[i];
            while (current != null) {
                if (current.name.equalsIgnoreCase(searchName)) {
                    System.out.println("Found: " + current.studentID + " - " + current.name);
                    found = true;
                }
                current = current.next;
            }
        }
        if (!found) System.out.println("Student not found.");
    }

    //  REQ 6: Display All (Unsorted)
    public void displayAllUnsorted() {
        if (studentCount == 0) {
            System.out.println("No students registered.");
            return;
        }
        System.out.println("\n--- All Students (Unsorted) ---");
        for (int i = 0; i < size; i++) {
            Student current = table[i];
            while (current != null) {
                System.out.println("ID: " + current.studentID + " | Name: " + current.name);
                current = current.next;
            }
        }
    }

    // REQ 7: Display Sorted by Name (Bubble Sort)
    public void displaySortedByName() {
        if (studentCount == 0) {
            System.out.println("No students to display.");
            return;
        }

        // 1. Extract to array
        Student[] list = getAllStudentsArray();

        // 2. Bubble Sort
        for (int i = 0; i < list.length - 1; i++) {
            for (int j = 0; j < list.length - 1 - i; j++) {
                if (list[j].name.compareToIgnoreCase(list[j + 1].name) > 0) {
                    Student temp = list[j];
                    list[j] = list[j + 1];
                    list[j + 1] = temp;
                }
            }
        }

        // 3. Print
        System.out.println("\n--- Students Sorted by Name ---");
        for (Student s : list) {
            System.out.println("ID: " + s.studentID + " | Name: " + s.name);
        }
    }
    
    // REQ 9 -- Delete Student 
    public boolean deleteStudent(String id) {
        int index = getHashIndex(id);
        Student current = table[index];
        Student prev = null;

        while (current != null) {
            if (current.studentID.equals(id)) {
                if (prev == null) {
                    table[index] = current.next; // Head of chain
                } else {
                    prev.next = current.next; // Middle/End
                }
                studentCount--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    // --- REQ 10: File I/O (Save & Load) ---
    public void saveData(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < size; i++) {
                Student current = table[i];
                while (current != null) {
                    // Save Student
                    writer.write("STU|" + current.studentID + "|" + current.name + "\n");
                    
                    // Save Subjects
                    Subject sub = current.enrolledSubjectsHead;
                    while (sub != null) {
                        writer.write("SUB|" + sub.subjectCode + "|" + sub.subjectName + "\n");
                        sub = sub.next;
                    }
                    current = current.next;
                }
            }
            System.out.println("Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void loadData(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("No save file found. Starting fresh.");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            Student currentStudent = null;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length < 3) continue;

                if (parts[0].equals("STU")) {
                    String id = parts[1];
                    String name = parts[2];
                    this.addStudent(id, name); // Use 'this'
                    currentStudent = this.searchByID(id); 
                } else if (parts[0].equals("SUB") && currentStudent != null) {
                    String code = parts[1];
                    String subName = parts[2];
                    currentStudent.addSubject(code, subName);
                }
            }
            System.out.println("Data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Error loading file.");
        }
    }
}