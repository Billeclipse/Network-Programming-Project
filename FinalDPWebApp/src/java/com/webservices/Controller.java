package com.webservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("Controller")
public class Controller {
    
    @Context
    private UriInfo context; 
    
    @GET
    @Path("/getAllCoursesWithProfessors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<ProfessorsCourses> getAllCoursesWithProfessorsInJSON()
    {
        ArrayList<ProfessorsCourses> professors_courses_arraylist = new ArrayList<>();        
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }       
            String database_query = "SELECT CONCAT(Professors.professor_name, \" \", Professors.professor_surname) AS professor_fullname"
                    + ",Courses.course_title_gr,Courses.course_title_eng,Courses.course_semester "
                    + "FROM Courses,Professors,Professors_Courses WHERE Professors_Courses.course_id=Courses.course_id "
                    + "AND Professors_Courses.professor_id=Professors.professor_id;";        
            ResultSet database_result_set = database_statement.executeQuery(database_query);        
            while(database_result_set.next()){      
                professors_courses_arraylist.add(new ProfessorsCourses(database_result_set.getString("professor_fullname")
                        ,database_result_set.getString("Courses.course_title_gr"),database_result_set.getString("Courses.course_title_eng")
                        ,database_result_set.getInt("Courses.course_semester")));
            }
            database_statement.close();
            return professors_courses_arraylist;
        } catch (Exception ex) {
            return null;
        }
    }
        
    @GET
    @Path("/searchByProfessor/{professor}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<CoursesModel> getProfessorCoursesInJSON(@PathParam ("professor") String professor_name)
    {
        ArrayList<CoursesModel> courses_model_arraylist = new ArrayList<>();        
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }        
            String database_query = "SELECT Courses.course_id,Courses.course_title_gr,Courses.course_title_eng,Courses.education_level,Courses.course_semester "
                    + "FROM Courses,Professors,Professors_Courses WHERE Professors_Courses.course_id=Courses.course_id "
                    + "AND Professors_Courses.professor_id=Professors.professor_id AND (Professors.professor_name LIKE '%"+professor_name+"%' "
                    + "OR Professors.professor_surname LIKE '%"+professor_name+"%');";
            ResultSet database_result_set = database_statement.executeQuery(database_query);        
            while(database_result_set.next()){
                courses_model_arraylist.add(new CoursesModel(database_result_set.getInt("Courses.course_id"),database_result_set.getString("Courses.course_title_gr")
                    ,database_result_set.getString("Courses.course_title_eng"),database_result_set.getInt("Courses.education_level")
                    ,database_result_set.getInt("Courses.course_semester")));
            }
            database_statement.close();
            return courses_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    } 
    
    @GET
    @Path("/searchByLearningObjective/{learningObjective}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<CoursesModel> getLearingObjectiveCoursesInJSON(@PathParam ("learningObjective") String learing_objective_title)
    {
        ArrayList<CoursesModel> courses_model_arraylist = new ArrayList<>();
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String query = "SELECT Courses.course_id,Courses.course_title_gr,Courses.course_title_eng,Courses.education_level,Courses.course_semester "
                    + "FROM Courses,Learning_Objectives WHERE Courses.course_id=Learning_Objectives.learning_objective_course "
                    + "AND Learning_Objectives.learning_objective_title_gr LIKE '%"+learing_objective_title+"%' "
                    + "OR Learning_Objectives.learning_objective_title_eng LIKE '%"+learing_objective_title+"%';";
            ResultSet database_result_set = database_statement.executeQuery(query);        
            System.out.println(query);
            while(database_result_set.next()){
                courses_model_arraylist.add(new CoursesModel(database_result_set.getInt("Courses.course_id"),database_result_set.getString("Courses.course_title_gr")
                        ,database_result_set.getString("Courses.course_title_eng"),database_result_set.getInt("Courses.education_level")
                        ,database_result_set.getInt("Courses.course_semester")));
            }
            database_statement.close();
            return courses_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    } 
    
    @GET
    @Path("Login")
    @Produces (MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response userLoginJSON(@QueryParam (value = "username") String username, @QueryParam ("password") String password)
    {
        AuthKey new_key = null,old_key=null;
        UserModel user_model = null;
        Connection database_connection;
        Statement database_statement;
        PreparedStatement database_prepared_statement;
        PasswordAuthentication password_authentication = new PasswordAuthentication();
        boolean already_exists=false;
        int response_code,user_type_id=-1;
        String response_string,database_query,user_type=null,stored_password=null;
        Date user_type_datetime=null;
        ResultSet database_result_set;
        String [] user_types= AuthKey.getArrayWithUserTypes();   
        try{
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
                database_statement = database_connection.createStatement();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }                    
            database_query = "SELECT professor_id,professor_username,professor_password,professor_valid FROM Professors "
                    + "WHERE professor_username ='" + username + "';";
            database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                user_type_id = database_result_set.getInt("professor_id");
                user_model = new UserModel(new ProfessorsModel(user_type_id,database_result_set.getString("professor_username")
                        ,null,null,null,database_result_set.getInt("professor_valid")==0),null); 
                stored_password = database_result_set.getString("professor_password");
                user_type=user_types[0];
            }
            database_query = "SELECT student_id,student_username,student_password,student_valid FROM Students WHERE student_username='"+username+"';";
            database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                user_type_id = database_result_set.getInt("student_id");
                user_model = new UserModel(new StudentsModel(user_type_id,database_result_set.getString("student_username")
                        ,null,null,database_result_set.getInt("student_valid")==0),null);    
                stored_password = database_result_set.getString("student_password");
                user_type=user_types[1];
            }
            database_query = "SELECT admin_id,admin_username,admin_password FROM Admin WHERE admin_username='"+username+"';";
            database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                user_type_id = database_result_set.getInt("admin_id");
                user_model = new UserModel(new AdminModel(user_type_id,database_result_set.getString("admin_username")),null);
                stored_password = database_result_set.getString("admin_password");
                user_type=user_types[2];
            }
            if(user_model!=null && user_model.isValid() && stored_password!=null){
                try{
                    if(user_model.getUsername().equals(username) && password_authentication.authenticate(password.toCharArray(), stored_password)){
                        new_key = new AuthKey(user_type_id,user_type);                
                    }
                }catch(Exception ex){}                
            }
            if (new_key!=null && new_key.isValid()){
                database_query = "SELECT * FROM Login_Instances WHERE id="+String.valueOf(new_key.getId())
                        +" AND user_type='"+new_key.getUser_type()+"';";
                database_result_set = database_statement.executeQuery(database_query);
                if(database_result_set.next()){
                    old_key = new AuthKey(database_result_set.getInt("id"),database_result_set.getString("user_type"),
                            database_result_set.getString("authentication_key"),database_result_set.getString("expires"));
                    user_type_datetime = database_result_set.getTimestamp("expires");
                    already_exists=true;
                }
                if(!already_exists){                    
                    database_query = "INSERT INTO Login_Instances VALUES (?, ?, ?, ?);";
                    database_prepared_statement = database_connection.prepareStatement(database_query);
                    database_prepared_statement.setString(1, String.valueOf(new_key.getId()));
                    database_prepared_statement.setString(2, new_key.getUser_type());
                    database_prepared_statement.setString(3, new_key.getAuth_key());
                    database_prepared_statement.setString(4, new_key.getExpires());
                    database_prepared_statement.executeUpdate();
                    response_code = 200;
                    try{
                        response_string = new_key.toJSON();
                    }catch(JsonProcessingException jpe){
                        response_string = "Error: AuthKey JSON Exception -> "+jpe.toString();
                        response_code = 400;
                    }   
                    database_prepared_statement.close();
                }else{
                    if(user_type_datetime.compareTo(new Date())<=0){
                        database_query = "DELETE FROM Login_Instances WHERE id="+String.valueOf(new_key.getId())
                                +" AND user_type='"+new_key.getUser_type()+"';";
                        database_statement.executeUpdate(database_query);
                        database_query = "INSERT INTO Login_Instances VALUES (?, ?, ?, ?);";
                        database_prepared_statement = database_connection.prepareStatement(database_query);
                        database_prepared_statement.setString(1, String.valueOf(new_key.getId()));
                        database_prepared_statement.setString(2, new_key.getUser_type());
                        database_prepared_statement.setString(3, new_key.getAuth_key());
                        database_prepared_statement.setString(4, new_key.getExpires());
                        database_prepared_statement.executeUpdate();
                        response_code = 200;
                        try{
                            response_string = new_key.toJSON();
                        }catch(JsonProcessingException jpe){
                            response_string = "Error: AuthKey JSON Exception -> "+jpe.toString();
                            response_code = 400;
                        }
                        database_prepared_statement.close();
                    }else{
                        //Already logged in
                        response_code = 200;
                        try{
                            response_string = old_key.toJSON();
                        }catch(JsonProcessingException jpe){
                            response_string = "Error: AuthKey JSON Exception -> "+jpe.toString();
                            response_code = 400;
                        }
                    } 
                }                
            }else if (new_key==null && user_model==null){
                response_string = "Error: Wrong username or password";
                response_code = 400;
            }else if (new_key==null && user_model!=null && !user_model.isValid()) {
                response_string = "Error: Invalid user";
                response_code = 400;
            }else{
                response_string = "Error: Password exception";
                response_code = 400;
            }
            database_statement.close();
            return Response.status(response_code).entity(response_string).build();
        }catch(Exception ex){
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
    
    @DELETE
    @Path("Logout")
    @Produces (MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response userLogout(@QueryParam ("auth_key") String auth_key)
    {
        Statement database_statement;
        int response_code,id=-1;
        String response_string,database_query,user_type=null;
        ResultSet database_result_set;
        try{
            if (new DBConnection().getConnection()!=null){                
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            } 
            database_query = "SELECT id,user_type FROM Login_Instances WHERE authentication_key='"+auth_key+"';";
            database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                id = database_result_set.getInt("id");
                user_type = database_result_set.getString("user_type");
            }
            if(id>=0 && user_type!=null){
                database_query = "DELETE FROM Login_Instances WHERE id="+String.valueOf(id)+" AND user_type='"+user_type+"';";
                database_statement.executeUpdate(database_query);
                response_string = "User logged out successfully";
                response_code = 200;
            }else{
                response_string = "Error: Authentication key not found in database";
                response_code = 400;
            }
            database_statement.close();
            return Response.status(response_code).entity(response_string).build();
        }catch(Exception ex){
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }        
    }
        
    @POST
    @Path("Register")
    @Produces (MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response userRegister(@FormParam ("username") String username, @FormParam ("password") String password
            , @FormParam ("name") String name, @FormParam ("surname") String surname, @FormParam ("grade") String grade)
    {
        Connection database_connection;
        Statement database_statement;
        PreparedStatement database_prepared_statement;
        ResultSet database_result_set;
        String database_query,response_string;
        int response_code;
        boolean foundProfessor=false,foundStudent=false;
        PasswordAuthentication password_authentication = new PasswordAuthentication();        
        try {
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
                database_statement = database_connection.createStatement();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }
            database_query = "SELECT professor_username FROM Professors WHERE professor_username='"+username+"';";
            database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                foundProfessor = true;
            }
            database_query = "SELECT student_username FROM Students WHERE student_username='"+username+"';";
            database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                foundStudent = true;
            }
            if(!foundProfessor && !foundStudent){
                if(grade!=null){
                    database_query = "INSERT INTO Professors (professor_username,professor_password,professor_name,professor_surname,professor_grade) "
                            + "VALUES (?, ?, ?, ?, ?);";
                    database_prepared_statement = database_connection.prepareStatement(database_query);
                    database_prepared_statement.setString(1, username);
                    database_prepared_statement.setString(2, password_authentication.hash(password.toCharArray()));
                    database_prepared_statement.setString(3, name);
                    database_prepared_statement.setString(4, surname);
                    database_prepared_statement.setString(5, grade);
                    database_prepared_statement.executeUpdate();
                    response_code = 201;
                    response_string = "Professor inserted into database, waiting admin approval";
                }else{                    
                    database_query = "INSERT INTO Students (student_username,student_password,student_name,student_surname) "
                            + "VALUES (?, ?, ?, ?);";
                    database_prepared_statement = database_connection.prepareStatement(database_query);
                    database_prepared_statement.setString(1, username);
                    database_prepared_statement.setString(2, password_authentication.hash(password.toCharArray()));
                    database_prepared_statement.setString(3, name);
                    database_prepared_statement.setString(4, surname);
                    database_prepared_statement.executeUpdate();
                    response_code = 201;
                    response_string = "Student inserted into database";
                }
                database_prepared_statement.close();
            }else{
                response_code = 400;
                response_string = "Error: Username has already been taken";
            }
            database_statement.close();
            return Response.status(response_code).entity(response_string).build();
        } catch (Exception ex) {
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
}