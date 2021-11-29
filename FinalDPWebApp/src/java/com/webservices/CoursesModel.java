package com.webservices;

public class CoursesModel {
    private int id;
    private String title_gr;
    private String title_eng;
    private int education_level;
    private int semester;

    public CoursesModel() {
    }
    
    public CoursesModel(int id, String title_gr, String title_eng, int education_level, int semester) {
        this.id = id;
        this.title_gr = title_gr;
        this.title_eng = title_eng;
        this.education_level = education_level;
        this.semester = semester;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle_gr() {
        return title_gr;
    }

    public void setTitle_gr(String title_gr) {
        this.title_gr = title_gr;
    }

    public String getTitle_eng() {
        return title_eng;
    }

    public void setTitle_eng(String title_eng) {
        this.title_eng = title_eng;
    }

    public int getEducation_level() {
        return education_level;
    }

    public void setEducation_level(int education_level) {
        this.education_level = education_level;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "CoursesModel{" + "id=" + id + ", title_gr=" + title_gr + ", title_eng=" + title_eng + ", education_level=" + education_level + ", semester=" + semester + '}';
    }
    
}
