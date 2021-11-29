package com.webservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("Courses")
public class CoursesController {

    @Context
    private UriInfo context;
    
    public CoursesController() {
    }

    @GET
    @Path("/getAllCourses")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<CoursesModel> getAllCoursesInJSON()
    {        
        ArrayList<CoursesModel> courses_model_arraylist = new ArrayList<>();
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT * FROM Courses;";
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                courses_model_arraylist.add(new CoursesModel(database_result_set.getInt("course_id"),database_result_set.getString("course_title_gr")
                        ,database_result_set.getString("course_title_eng"),database_result_set.getInt("education_level"),database_result_set.getInt("course_semester")));
            }
            database_statement.close();
            return courses_model_arraylist.isEmpty()?null:courses_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    }
    
    @GET
    @Path("/getProfessorCourses")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<CoursesModel> getProfessorCoursesInJSON(@QueryParam ("professor_id") int professor_id)
    {        
        ArrayList<CoursesModel> courses_model_arraylist = new ArrayList<>();
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT Courses.course_id,course_title_gr,course_title_eng,education_level,course_semester FROM Courses,Professors_Courses "
                    + "WHERE Courses.course_id=Professors_Courses.course_id AND Professors_Courses.professor_id="+professor_id+";";
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                courses_model_arraylist.add(new CoursesModel(database_result_set.getInt("course_id"),database_result_set.getString("course_title_gr")
                        ,database_result_set.getString("course_title_eng"),database_result_set.getInt("education_level"),database_result_set.getInt("course_semester")));
            }
            database_statement.close();
            return courses_model_arraylist.isEmpty()?null:courses_model_arraylist;
        } catch (Exception ex) {            
            return null;
        }
    }
    
    @GET
    @Path("/getCourse")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public CoursesModel getCourseInfoInJSON(@QueryParam ("id") int course_id)
    {        
        CoursesModel temp_course_model = null;
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT * FROM Courses WHERE course_id="+String.valueOf(course_id)+";";
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                temp_course_model = new CoursesModel(database_result_set.getInt("course_id"),database_result_set.getString("course_title_gr")
                        ,database_result_set.getString("course_title_eng"),database_result_set.getInt("education_level"),database_result_set.getInt("course_semester"));
            }
            database_statement.close();
            return temp_course_model!=null?temp_course_model:null;
        } catch (Exception ex) {
            return null;
        }
    }  
    
    @GET
    @Path("/getPrerequisitesCourses")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<CoursesModel> getPrerequisitesCoursesInJSON(@QueryParam ("course_id") int course_id)
    {        
        ArrayList<CoursesModel> temp_course_model_arraylist = new ArrayList<>();
        ArrayList<Integer> prerequisites_courses_id = new ArrayList<>();
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT Prerequisites_Courses.prerequisite_course_id"
                    + " FROM Courses,Prerequisites_Courses WHERE Courses.course_id=Prerequisites_Courses.course_id AND Prerequisites_Courses.course_id="+String.valueOf(course_id)+";";            
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                prerequisites_courses_id.add(database_result_set.getInt("Prerequisites_Courses.prerequisite_course_id"));
            }
            database_result_set.close();
            String database_temp_query;    
            ResultSet database_temp_result_set;
            for(int i=0; i<prerequisites_courses_id.size(); i++){
                database_temp_query = "SELECT * FROM Courses WHERE course_id = "+prerequisites_courses_id.get(i);
                database_temp_result_set = database_statement.executeQuery(database_temp_query);                
                if(database_temp_result_set.next()){                    
                    temp_course_model_arraylist.add(new CoursesModel(database_temp_result_set.getInt("course_id"),database_temp_result_set.getString("course_title_gr")
                        ,database_temp_result_set.getString("course_title_eng"),database_temp_result_set.getInt("education_level"),database_temp_result_set.getInt("course_semester")));
                }
            }
            database_statement.close();
            return temp_course_model_arraylist.isEmpty()?null:temp_course_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    }
    
    @GET
    @Path("/searchByCourse/{course}")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public CoursesModel getCourseInfoInJSON(@PathParam ("course") String course_title)
    {
        CoursesModel temp_courses_model = null;
        Statement database_statement; 
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT * FROM Courses WHERE course_title_gr LIKE '%"+course_title+"%' OR course_title_eng LIKE '%"+course_title+"%';";
            ResultSet database_result_set = database_statement.executeQuery(database_query);        
            if(database_result_set.next()){
                temp_courses_model = new CoursesModel(database_result_set.getInt("course_id"),database_result_set.getString("course_title_gr")
                        ,database_result_set.getString("course_title_eng"),database_result_set.getInt("education_level"),database_result_set.getInt("course_semester"));
            }
            database_statement.close();
            return temp_courses_model!=null?temp_courses_model:null;
        } catch (Exception ex) {
            return null;
        }
    }
    
    @GET
    @Path("/getLearningObjectiveCourses")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<CoursesModel> getLearingObjectiveCoursesInJSON(@QueryParam ("learning_objective_code") String learning_objective_code)
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
                    + "AND Learning_Objectives.learning_objective_code ='"+learning_objective_code+"';";
            ResultSet database_result_set = database_statement.executeQuery(query);
            while(database_result_set.next()){
                courses_model_arraylist.add(new CoursesModel(database_result_set.getInt("Courses.course_id"),database_result_set.getString("Courses.course_title_gr")
                        ,database_result_set.getString("Courses.course_title_eng"),database_result_set.getInt("Courses.education_level")
                        ,database_result_set.getInt("Courses.course_semester")));
            }
            database_statement.close();
            return courses_model_arraylist.isEmpty()?null:courses_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    } 
    
    @GET
    @Path("/getAllFavoriteCourses")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<CoursesModel> getAllFavoriteCoursesInJSON(@QueryParam ("student_id") String student_id)
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
                    + "FROM Courses,Favorite_Courses WHERE Courses.course_id=Favorite_Courses.course_id "
                    + "AND Favorite_Courses.student_id ='"+student_id+"';";
            ResultSet database_result_set = database_statement.executeQuery(query);
            while(database_result_set.next()){
                courses_model_arraylist.add(new CoursesModel(database_result_set.getInt("Courses.course_id"),database_result_set.getString("Courses.course_title_gr")
                        ,database_result_set.getString("Courses.course_title_eng"),database_result_set.getInt("Courses.education_level")
                        ,database_result_set.getInt("Courses.course_semester")));
            }
            database_statement.close();
            return courses_model_arraylist.isEmpty()?null:courses_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    } 
    
    @POST
    @Path("/createCourse")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response createCourse(@FormParam("course_title_gr") String course_title_gr, @FormParam("course_title_eng") String course_title_eng
            ,@FormParam("education_level") int education_level, @FormParam("course_semester") int course_semester
            ,@FormParam("prerequisites_courses") List<String> prerequisites_courses, @FormParam("professor_id") int professor_id){
        Connection database_connection;
        Statement database_statement;
        PreparedStatement database_prepared_statement;
        ResultSet database_result_set;
        String database_query,response_string;
        int response_code;
        boolean course_found=false;
        try {
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
                database_statement = database_connection.createStatement();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }
            database_query = "SELECT course_id FROM Courses WHERE course_title_gr='"+course_title_gr+"' OR course_title_eng='"+course_title_eng+"';";
            database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                course_found = true;
            }
            if(!course_found){
                database_query = "INSERT INTO Courses (course_title_gr,course_title_eng,education_level,course_semester) "
                            + "VALUES (?, ?, ?, ?);";
                database_prepared_statement = database_connection.prepareStatement(database_query);
                database_prepared_statement.setString(1, course_title_gr);
                database_prepared_statement.setString(2, course_title_eng);
                database_prepared_statement.setInt(3, education_level);
                database_prepared_statement.setInt(4, course_semester);
                database_prepared_statement.executeUpdate();
                response_code = 201;
                response_string = "Course inserted into database";
                
                int course_id=-1;
                database_query = "SELECT course_id FROM Courses WHERE course_title_gr='"+course_title_gr+"' OR course_title_eng='"+course_title_eng+"';";
                database_result_set = database_statement.executeQuery(database_query);
                if(database_result_set.next()){
                    course_id = database_result_set.getInt("course_id");
                    course_found = true;
                }
                if(course_found){
                    if(professor_id>0){
                        database_query = "INSERT INTO Professors_Courses VALUES (?, ?);";
                        database_prepared_statement = database_connection.prepareStatement(database_query);
                        database_prepared_statement.setInt(1, course_id);
                        database_prepared_statement.setInt(2, professor_id);
                        database_prepared_statement.executeUpdate();
                    }
                    if(prerequisites_courses!=null && !prerequisites_courses.get(0).replace("[", "").replace("]", "").replace("null", "").equals("")){                    
                        String[] prerequisites_courses_tmp = prerequisites_courses.get(0).replace("[", "").replace("]", "").split(",");
                        database_query = "INSERT INTO Prerequisites_Courses VALUES (?, ?);";
                        database_prepared_statement = database_connection.prepareStatement(database_query);
                        for(int i=0; i<prerequisites_courses_tmp.length; i++){
                            database_prepared_statement.setInt(1, Integer.valueOf(prerequisites_courses_tmp[i].replace(" ", "")));
                            database_prepared_statement.setInt(2, course_id);
                            database_prepared_statement.addBatch();
                        }
                        database_prepared_statement.executeBatch();
                    }                   
                }            
                database_prepared_statement.close();
            }else{
                response_code = 400;
                response_string = "Error: Course already exists in database";
            }
            database_statement.close();
            return Response.status(response_code).entity(response_string).build();
        } catch (Exception ex) {
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
    
    @POST
    @Path("/addFavoriteCourse")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response addFavoriteCourse(@FormParam("course_id") int course_id, @FormParam("student_id") int student_id){
        Connection database_connection;
        Statement database_statement;
        PreparedStatement database_prepared_statement;
        ResultSet database_result_set;
        String database_query,response_string;
        int response_code;
        boolean course_found=false;
        try {
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
                database_statement = database_connection.createStatement();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }
            database_query = "SELECT course_id FROM Courses WHERE course_id="+course_id+";";
            database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                course_found = true;
            }
            if(course_found){
                database_query = "INSERT INTO Favorite_Courses (course_id,student_id) VALUES (?, ?);";
                database_prepared_statement = database_connection.prepareStatement(database_query);
                database_prepared_statement.setInt(1, course_id);
                database_prepared_statement.setInt(2, student_id);
                database_prepared_statement.executeUpdate();
                response_code = 201;
                response_string = "Favorite Course inserted into database";
                database_prepared_statement.close();
            }else{
                response_code = 400;
                response_string = "Error: Course not found";
            }
            database_statement.close();
            return Response.status(response_code).entity(response_string).build();
        } catch (Exception ex) {
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
    
    @DELETE
    @Path("/removeFavoriteCourse")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response removeFavoriteCourse(@QueryParam("course_id") int course_id, @QueryParam("student_id") int student_id){
        Connection database_connection;
        Statement database_statement;
        PreparedStatement database_prepared_statement;
        ResultSet database_result_set;
        String database_query,response_string;
        int response_code;
        boolean course_found=false;
        try {
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
                database_statement = database_connection.createStatement();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }
            database_query = "SELECT course_id FROM Courses WHERE course_id="+course_id+";";
            database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                course_found = true;
            }
            if(course_found){
                database_query = "DELETE FROM Favorite_Courses WHERE course_id = ? AND student_id = ?;";
                database_prepared_statement = database_connection.prepareStatement(database_query);
                database_prepared_statement.setInt(1, course_id);
                database_prepared_statement.setInt(2, student_id);
                database_prepared_statement.executeUpdate();
                response_code = 200;
                response_string = "Favorite Course deleted from database";
                database_prepared_statement.close();
            }else{
                response_code = 400;
                response_string = "Error: Course not found";
            }
            database_statement.close();
            return Response.status(response_code).entity(response_string).build();
        } catch (Exception ex) {
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
}