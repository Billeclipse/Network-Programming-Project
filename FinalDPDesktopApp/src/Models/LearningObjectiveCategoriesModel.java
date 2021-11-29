package Models;

public class LearningObjectiveCategoriesModel {
    private int id;
    private String title_gr;
    private String title_eng;

    public LearningObjectiveCategoriesModel() {
    }

    public LearningObjectiveCategoriesModel(int id, String title_gr, String title_eng) {
        this.id = id;
        this.title_gr = title_gr;
        this.title_eng = title_eng;
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

    @Override
    public String toString() {
        return "LearningObjectiveCategoriesModel{" + "id=" + id + ", title_gr=" + title_gr + ", title_eng=" + title_eng + '}';
    }
    
}
