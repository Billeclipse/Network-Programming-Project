package Models;

public class ProfessorsModel {
    private int id;
    private String username;
    private String name;
    private String surname;
    private String grade;
    private boolean valid;

    public ProfessorsModel() {
    }
    
    public ProfessorsModel(int id, String username, String name, String surname, String grade, boolean valid) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.grade = grade;
        this.valid = valid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }    
    
    public boolean isNotNull(){
        return this.id>0 && this.username!=null && this.username.trim().length()>0;
    }

    @Override
    public String toString() {
        return "ProfessorsModel{" + "id=" + id + ", username=" + username + ", name=" + name + ", surname=" + surname + ", grade=" + grade + ", valid=" + valid + '}';
    }
    
}
