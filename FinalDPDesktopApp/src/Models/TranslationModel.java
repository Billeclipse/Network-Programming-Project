package Models;

public class TranslationModel {
    private String key;
    private String value;
    
    private final static String[] AVAILABLE_LANGUAGES = {"gr","eng"};
    
    public TranslationModel() {
    }

    public TranslationModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String[] getAVAILABLE_LANGUAGES() {
        return AVAILABLE_LANGUAGES;
    }
    
    @Override
    public String toString() {
        return "TranslationModel{" + "key=" + key + ", value=" + value + '}';
    }
}