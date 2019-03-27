package pl.com.ttpsc.data;

public class Guardian extends User {

    private String assignStudent = "";

    public String getAssignStudent () {
        return assignStudent;
    }

    public void setAssignStudent(String assignStudent) {
        this.assignStudent = assignStudent;
    }

    public String toString () {
        return "" +getName()+" "+getSurname()+" "+getWhoIs()+" "+getAssignStudent();

    }
}
