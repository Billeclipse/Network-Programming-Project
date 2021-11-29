package general;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webservices.AdminModel;
import com.webservices.AuthKey;
import com.webservices.CoursesModel;
import com.webservices.LearningObjectiveCategoriesModel;
import com.webservices.LearningObjectivesModel;
import com.webservices.ProfessorsModel;
import com.webservices.StudentsModel;
import com.webservices.TranslationModel;
import com.webservices.UserModel;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.http.Cookie;

public class FunctionsController {    
    public static String getResponse(String web_services_url_string, String method_type){        
        try {
            String response_string;  
            URL web_services_url;
            HttpURLConnection connection;            
            if(method_type.equals("POST")){
                if (web_services_url_string.contains("?")){
                    String[] new_web_services_url_string = web_services_url_string.split("\\?",2);
                    new_web_services_url_string[0].replace("?", " ");
                    web_services_url = new URL(new_web_services_url_string[0]);
                    byte[] postData = new_web_services_url_string[1].getBytes(StandardCharsets.UTF_8);
                    connection = (HttpURLConnection) web_services_url.openConnection();
                    connection.setRequestMethod(method_type);
                    connection.setDoOutput(true);
                    connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty( "charset", "utf-8");
                    DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                    output.write(postData);
                    output.flush();
                    output.close();
                }else{
                    return null;
                }
            }else{
                web_services_url = new URL(web_services_url_string);
                connection = (HttpURLConnection) web_services_url.openConnection();
                connection.setRequestMethod(method_type);
            }
            response_string = getResponseString(connection);
            if(response_string.toString().trim().length()<=0)
                return null;
            return response_string.toString();
        } catch (Exception ex) {
            //System.out.println(ex.toString()); //Debug
            return null;
        }
    }
    
    private static String getResponseString(HttpURLConnection connection){        
        try{
            int responseCode = connection.getResponseCode();
            BufferedReader in_reader;
            if(responseCode>=400){
                in_reader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(),"UTF-8"));
            }else{
                in_reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(),"UTF-8"));
            }                
            String inputLine;
            StringBuffer response_buffer = new StringBuffer();
            while ((inputLine = in_reader.readLine()) != null) {
                    response_buffer.append(inputLine);
            }
            in_reader.close();
            return response_buffer.toString();
        }catch(IOException ioe){
            return null;
        }
    }
    
    public static Cookie createUserCookieFromAuthKey(AuthKey user_auth_key){
        try{ 
            String [] user_types = AuthKey.getArrayWithUserTypes();
            final String PROFESSOR = user_types[0];
            final String STUDENT= user_types[1];
            final String ADMIN= user_types[2]; 
            ObjectMapper mapper = new ObjectMapper();
            String webServicesStringUrl,responseString;
            String user_type = user_auth_key.getUser_type();
            Cookie user_info = null;
            UserModel um;
            if(user_type.equals(PROFESSOR)){                
                //Professor
                ProfessorsModel pm;
                webServicesStringUrl= "http://localhost:8080/FinalDPWebApp/webresources/Professors/getProfessor?id="
                    +String.valueOf(user_auth_key.getId());
                responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");      
                pm = mapper.readValue(responseString, ProfessorsModel.class);
                um = new UserModel(pm, user_auth_key);
                // Create cookie for professor info.
                user_info = new Cookie("user_json",URLEncoder.encode(um.toJSON(), "UTF-8"));

                //System.out.println("Logged in as Professor with id: " + String.valueOf(um.getUser_auth_key().getId())); //Debug
            }else if(user_type.equals(STUDENT)){
                //Student
                StudentsModel sm;
                webServicesStringUrl= "http://localhost:8080/FinalDPWebApp/webresources/Students/getStudent?id="
                    +String.valueOf(user_auth_key.getId());
                responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");        
                sm = mapper.readValue(responseString, StudentsModel.class);
                um = new UserModel(sm, user_auth_key);
                // Create cookie for student info.
                user_info = new Cookie("user_json",URLEncoder.encode(um.toJSON(), "UTF-8"));

                //System.out.println("Logged in as Student with id: " + String.valueOf(um.getUser_auth_key().getId())); //Debug
            }else if(user_type.equals(ADMIN)){
                //Admin
                um = new UserModel(new AdminModel(user_auth_key.getId(),"admin"), user_auth_key);
                // Create cookie for admin info.
                user_info = new Cookie("user_json",URLEncoder.encode(um.toJSON(), "UTF-8"));
                //System.out.println("Logged in as Admin"); //Debug
            }
            return user_info;
        }catch(IOException e){            
            //System.out.println("Login Failed: couldn't traslate json string into object"); //Debug
            return null;
        }
    }
    
    public static boolean checkUserExpiry(AuthKey user_auth_key){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Date expire_date = dateFormat.parse(user_auth_key.getExpires());
            if(date.compareTo(expire_date)>=0){
                return true;
            }
        } catch (ParseException ex) {
            return false;
        }
        return false;
    }
    
    public static ArrayList<CoursesModel> createCoursesModelArrayListFromJSON(String json){
        ArrayList<CoursesModel> tmp;        
        ObjectMapper mapper = new ObjectMapper();
        try {
            tmp = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, CoursesModel.class));
            return tmp;
        } catch (IOException ex) {
            return null;
        }        
    }
    
    public static CoursesModel createCoursesModelFromJSON(String json){
        CoursesModel tmp; 
        ObjectMapper mapper = new ObjectMapper();
        try {
            tmp = mapper.readValue(json, CoursesModel.class);
            return tmp;
        } catch (IOException ex) {
            return null;
        } 
    }
    
    public static ArrayList<LearningObjectivesModel> createLearningObjectivesModelArrayListFromJSON(String json){
        ArrayList<LearningObjectivesModel> tmp;        
        ObjectMapper mapper = new ObjectMapper();
        try {
            tmp = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, LearningObjectivesModel.class));
            return tmp;
        } catch (IOException ex) {
            return null;
        }        
    }
    
    public static LearningObjectivesModel createLearningObjectivesModelFromJSON(String json){
        LearningObjectivesModel tmp;        
        ObjectMapper mapper = new ObjectMapper();
        try {
            tmp = mapper.readValue(json, LearningObjectivesModel.class);
            return tmp;
        } catch (IOException ex) {
            return null;
        }        
    }
    
    public static ArrayList<LearningObjectiveCategoriesModel> createLearningObjectiveCategoriesModelArrayListFromJSON(String json){
        ArrayList<LearningObjectiveCategoriesModel> tmp;        
        ObjectMapper mapper = new ObjectMapper();
        try {
            tmp = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, LearningObjectiveCategoriesModel.class));
            return tmp;
        } catch (IOException ex) {
            return null;
        }        
    }
    
    public static ArrayList<ProfessorsModel> createProfessorsModelArrayListFromJSON(String json){
        ArrayList<ProfessorsModel> tmp;        
        ObjectMapper mapper = new ObjectMapper();
        try {
            tmp = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, ProfessorsModel.class));
            return tmp;
        } catch (IOException ex) {
            return null;
        }        
    }
    
    public static HashMap<String,String> createTranslationModelHashMapFromJSON(String json){
        ArrayList<TranslationModel> tmp_arraylist; 
        HashMap<String,String> tmp_hashmap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            tmp_arraylist = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(ArrayList.class, TranslationModel.class));
            for(int i=0; i<tmp_arraylist.size(); i++){
                tmp_hashmap.put(tmp_arraylist.get(i).getKey(), tmp_arraylist.get(i).getValue());
            }
            return tmp_hashmap;
        } catch (IOException ex) {
            return null;
        }        
    }
}
