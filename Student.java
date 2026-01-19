public class Student {
    String studentID;
    String name;
    Subject enrolledSubjectsHead; // Head of Subject Linked List
    Student next; // Pointer to next Stud for hashtable collisions handling

    public Student(String id, String name) {
        this.studentID = id;
        this.name = name;
        this.enrolledSubjectsHead = null;
        this.next = null;
    }

    public void addSubject(String code, String name) {
        Subject newSubject = new Subject(code, name);
        if (enrolledSubjectsHead == null) {
            enrolledSubjectsHead = newSubject;
        } else {
            Subject current = enrolledSubjectsHead;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newSubject;
        }
    }
}