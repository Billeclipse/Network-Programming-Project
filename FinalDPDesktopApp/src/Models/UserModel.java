package Models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class UserModel {
    private String username;
    private String name;
    private String surname;
    private String grade;
    private boolean valid;
    private AuthKey user_auth_key;

    public UserModel() {
    }
    public UserModel(ProfessorsModel pm, AuthKey user_auth_key) {
        this.username = pm.getUsername();
        this.name = pm.getName();
        this.surname = pm.getSurname();
        this.grade = pm.getGrade();
        this.valid = pm.isValid();
        this.user_auth_key = user_auth_key;
    }
    
    public UserModel(String json) throws IOException {
        UserModel tmp;
        ObjectMapper mapper = new ObjectMapper();
        tmp = mapper.readValue(json, UserModel.class);  
        this.username = tmp.getUsername();
        this.name = tmp.getName();
        this.surname = tmp.getSurname();
        this.grade = tmp.getGrade();
        this.valid = tmp.isValid();
        this.user_auth_key = tmp.getUser_auth_key();
    }

    public String getUsername() {
        return username;
    }
    
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getGrade() {
        return grade;
    }

    public boolean isValid() {
        return valid;
    }

    public AuthKey getUser_auth_key() {
        return user_auth_key;
    }
        
    public String toJSON() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
