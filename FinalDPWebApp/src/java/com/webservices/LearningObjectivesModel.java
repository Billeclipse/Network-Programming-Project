package com.webservices;

public class LearningObjectivesModel {
    private String learning_objective_code;
    private String learning_objective_title_gr;
    private String learning_objective_title_eng;
    private int learning_objective_category;
    private String learning_objective_category_title_gr;
    private String learning_objective_category_title_eng;
    private int learning_objective_course;
    private String learning_objective_course_title_gr;
    private String learning_objective_course_title_eng;

    public LearningObjectivesModel() {
    }

    public LearningObjectivesModel(String learning_objective_code, String learning_objective_title_gr,
            String learning_objective_title_eng, int learning_objective_category, int learning_objective_course) {
        this.learning_objective_code = learning_objective_code;
        this.learning_objective_title_gr = learning_objective_title_gr;
        this.learning_objective_title_eng = learning_objective_title_eng;
        this.learning_objective_category = learning_objective_category;
        this.learning_objective_category_title_gr = null;
        this.learning_objective_category_title_eng = null;
        this.learning_objective_course = learning_objective_course;
        this.learning_objective_course_title_gr = null;
        this.learning_objective_course_title_eng = null;
    }

    public LearningObjectivesModel(String learning_objective_code, String learning_objective_title_gr, String learning_objective_title_eng, int learning_objective_category, int learning_objective_course, String learning_objective_course_title_gr, String learning_objective_course_title_eng) {
        this.learning_objective_code = learning_objective_code;
        this.learning_objective_title_gr = learning_objective_title_gr;
        this.learning_objective_title_eng = learning_objective_title_eng;
        this.learning_objective_category = learning_objective_category;
        this.learning_objective_course = learning_objective_course;
        this.learning_objective_course_title_gr = learning_objective_course_title_gr;
        this.learning_objective_course_title_eng = learning_objective_course_title_eng;
    }
    
    public LearningObjectivesModel(String learning_objective_code, String learning_objective_title_gr,
            String learning_objective_title_eng, int learning_objective_category, String learning_objective_category_title_gr,
            String learning_objective_category_title_eng, int learning_objective_course, String learning_objective_course_title_gr,
            String learning_objective_course_title_eng) {
        this.learning_objective_code = learning_objective_code;
        this.learning_objective_title_gr = learning_objective_title_gr;
        this.learning_objective_title_eng = learning_objective_title_eng;
        this.learning_objective_category = learning_objective_category;
        this.learning_objective_category_title_gr = learning_objective_category_title_gr;
        this.learning_objective_category_title_eng = learning_objective_category_title_eng;
        this.learning_objective_course = learning_objective_course;
        this.learning_objective_course_title_gr = learning_objective_course_title_gr;
        this.learning_objective_course_title_eng = learning_objective_course_title_eng;
    }

    public String getLearning_objective_code() {
        return learning_objective_code;
    }

    public void setLearning_objective_code(String learning_objective_code) {
        this.learning_objective_code = learning_objective_code;
    }

    public String getLearning_objective_title_gr() {
        return learning_objective_title_gr;
    }

    public void setLearning_objective_title_gr(String learning_objective_title_gr) {
        this.learning_objective_title_gr = learning_objective_title_gr;
    }

    public String getLearning_objective_title_eng() {
        return learning_objective_title_eng;
    }

    public void setLearning_objective_title_eng(String learning_objective_title_eng) {
        this.learning_objective_title_eng = learning_objective_title_eng;
    }

    public int getLearning_objective_category() {
        return learning_objective_category;
    }

    public void setLearning_objective_category(int learning_objective_category) {
        this.learning_objective_category = learning_objective_category;
    }

    public String getLearning_objective_category_title_gr() {
        return learning_objective_category_title_gr;
    }

    public void setLearning_objective_category_title_gr(String learning_objective_category_title_gr) {
        this.learning_objective_category_title_gr = learning_objective_category_title_gr;
    }

    public String getLearning_objective_category_title_eng() {
        return learning_objective_category_title_eng;
    }

    public void setLearning_objective_category_title_eng(String learning_objective_category_title_eng) {
        this.learning_objective_category_title_eng = learning_objective_category_title_eng;
    }

    public int getLearning_objective_course() {
        return learning_objective_course;
    }

    public void setLearning_objective_course(int learning_objective_course) {
        this.learning_objective_course = learning_objective_course;
    }

    public String getLearning_objective_course_title_gr() {
        return learning_objective_course_title_gr;
    }

    public void setLearning_objective_course_title_gr(String learning_objective_course_title_gr) {
        this.learning_objective_course_title_gr = learning_objective_course_title_gr;
    }

    public String getLearning_objective_course_title_eng() {
        return learning_objective_course_title_eng;
    }

    public void setLearning_objective_course_title_eng(String learning_objective_course_title_eng) {
        this.learning_objective_course_title_eng = learning_objective_course_title_eng;
    }

    @Override
    public String toString() {
        return "LearningObjectivesModel{" + "learning_objective_code=" + learning_objective_code 
                + ", learning_objective_title_gr=" + learning_objective_title_gr + ", learning_objective_title_eng="
                + learning_objective_title_eng + ", learning_objective_category=" + learning_objective_category 
                + ", learning_objective_category_title_gr=" + learning_objective_category_title_gr + ", learning_objective_category_title_eng=" 
                + learning_objective_category_title_eng + ", learning_objective_course=" + learning_objective_course 
                + ", learning_objective_course_title_gr=" + learning_objective_course_title_gr + ", learning_objective_course_title_eng=" 
                + learning_objective_course_title_eng + '}';
    }
    
}

