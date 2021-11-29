package com.webservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.UUID;
import static java.util.UUID.randomUUID;

public class AuthKey {
    private int id;
    private String user_type;
    private String auth_key;
    private String expires;
    private boolean valid;
    private final static String PROFESSOR="Professor";
    private final static String STUDENT="Student";
    private final static String ADMIN="Admin";   
    private static final int DEFAULT_EXPIRY = 2;

    public AuthKey() {
    }
    
    public AuthKey(int id, String user_type) {        
        this.id = id;
        this.user_type = user_type;  
        this.auth_key=setAuthKey();        
        this.expires=setExpireDate(DEFAULT_EXPIRY);
        this.valid=setValid();
    }

    public AuthKey(int id, String user_type, String auth_key, String expires) {
        this.id = id;
        this.user_type = user_type;
        this.auth_key = auth_key;
        this.expires = expires;
        this.valid = setValid();
    }
    
    public int getId() {
        return id;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getAuth_key() {
        return auth_key;
    }

    public String getExpires() {
        return expires;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public static String[] getArrayWithUserTypes(){
        final String [] aut = {PROFESSOR,STUDENT,ADMIN};
        return aut;
    }       

    public static int getDEFAULT_EXPIRY() {
        return DEFAULT_EXPIRY;
    }

    @Override
    public String toString() {
        return "AuthKey{" + "id=" + id + ", user_type=\"" + user_type + "\", auth_key=\"" 
                + auth_key + "\", expires=\"" + expires + "\", valid=" + valid + '}';
    }
    
    public String toJSON() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
    
    private String setExpireDate(int after_hours){
        Date dNow = new Date( );
        Calendar cal = Calendar.getInstance();
        cal.setTime(dNow);
        cal.add(Calendar.HOUR_OF_DAY, after_hours); // adds n(after_hours) hours
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        return ft.format(cal.getTime());
    }
        
    private String setAuthKey(){
        UUID uid = randomUUID();
        return uid.toString();
    }

    private boolean setValid(){
        boolean user_type_ok=false;
        switch(this.user_type){
            case PROFESSOR: user_type_ok = true; break;
            case STUDENT: user_type_ok = true; break;
            case ADMIN: user_type_ok = true; break;
        }
        if(this.id>=0 && user_type_ok)
            return true;
        return false;
    }
}
