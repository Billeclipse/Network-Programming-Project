package com.webservices;

public class ProfessorsCourses {
    private String professor_fullname;
    private String course_title_gr;
    private String course_title_eng;
    private int semester;

    public ProfessorsCourses() {
    }

    public ProfessorsCourses(String professor_fullname, String course_name_gr, String course_name_eng, int semester) {
        this.professor_fullname = professor_fullname;
        this.course_title_gr = course_name_gr;
        this.course_title_eng = course_name_eng;
        this.semester = semester;
    }

    public String getProfessor_fullname() {
        return professor_fullname;
    }

    public void setProfessor_fullname(String professor_fullname) {
        this.professor_fullname = professor_fullname;
    }

    public String getCourse_name_gr() {
        return course_title_gr;
    }

    public void setCourse_name_gr(String course_name_gr) {
        this.course_title_gr = course_name_gr;
    }

    public String getCourse_name_eng() {
        return course_title_eng;
    }

    public void setCourse_name_eng(String course_name_eng) {
        this.course_title_eng = course_name_eng;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "ProfessorsCourses{" + "professor_fullname=" + professor_fullname + ", course_title_gr=" + course_title_gr + ", course_title_eng=" + course_title_eng + ", semester=" + semester + '}';
    }
        
}
