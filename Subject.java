public class Subject {
    String subjectCode;
    String subjectName;
    Subject next;

    public Subject(String code, String name) {
        this.subjectCode = code;
        this.subjectName = name;
        this.next = null;
    }
}