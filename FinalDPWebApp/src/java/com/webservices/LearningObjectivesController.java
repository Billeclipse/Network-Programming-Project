package com.webservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("LearningObjectives")
public class LearningObjectivesController {
    @Context
    private UriInfo context;
    
    public LearningObjectivesController() {
    }
    
    @GET
    @Path("/getAllLearningObjectives")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<LearningObjectivesModel> getAllLearningObjectivesInJSON()
    {        
        ArrayList<LearningObjectivesModel> learning_objectives_model_arraylist = new ArrayList<>();
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT * FROM Learning_Objectives;";
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                learning_objectives_model_arraylist.add(new LearningObjectivesModel(database_result_set.getString("learning_objective_code"),
                        database_result_set.getString("learning_objective_title_gr"),database_result_set.getString("learning_objective_title_eng"),
                        database_result_set.getInt("learning_objective_category"),database_result_set.getInt("learning_objective_course")));
            }
            database_statement.close();
            return learning_objectives_model_arraylist.isEmpty()?null:learning_objectives_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    }
    
    @GET
    @Path("/getLearningObjective")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public LearningObjectivesModel getLearningObjectiveInfoInJSON(@QueryParam ("code") String learning_objective_code)
    {        
        LearningObjectivesModel temp_learning_objective_model = null;        
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT learning_objective_code,learning_objective_title_gr,learning_objective_title_eng,"
                    + "learning_objective_category,learning_objective_course,learning_objective_category_title_gr,"
                    + "learning_objective_category_title_eng,course_title_gr,course_title_eng FROM Learning_Objectives,Courses,Learning_Objective_Categories WHERE "
                    + "Learning_Objectives.learning_objective_course=Courses.course_id AND "
                    + "Learning_Objectives.learning_objective_category=Learning_Objective_Categories.learning_objective_category_id AND "
                    + "learning_objective_code='"+learning_objective_code+"';";
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                temp_learning_objective_model = new LearningObjectivesModel(database_result_set.getString("learning_objective_code"),
                        database_result_set.getString("learning_objective_title_gr"),database_result_set.getString("learning_objective_title_eng"),                        
                        database_result_set.getInt("learning_objective_category"),database_result_set.getString("learning_objective_category_title_gr"),
                        database_result_set.getString("learning_objective_category_title_eng"),database_result_set.getInt("learning_objective_course"),
                        database_result_set.getString("course_title_gr"),database_result_set.getString("course_title_eng"));
            }
            database_statement.close();
            return temp_learning_objective_model!=null?temp_learning_objective_model:null;
        } catch (Exception ex) {
            return null;
        }
    }
    
    @GET
    @Path("/getCourseLearningObjectives")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<LearningObjectivesModel> getCourseLearningObjectivesInJSON(@QueryParam ("course_id") int course_id)
    {        
        ArrayList<LearningObjectivesModel> temp_learning_objective_model_arraylist = new ArrayList<>();
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT learning_objective_code,learning_objective_title_gr,learning_objective_title_eng,"
                    + "learning_objective_category,learning_objective_course,learning_objective_category_title_gr,"
                    + "learning_objective_category_title_eng,course_title_gr,course_title_eng FROM Learning_Objectives,Courses,Learning_Objective_Categories WHERE "
                    + "Learning_Objectives.learning_objective_course=Courses.course_id AND "
                    + "Learning_Objectives.learning_objective_category=Learning_Objective_Categories.learning_objective_category_id AND "
                    + "course_id="+String.valueOf(course_id)+";";
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                temp_learning_objective_model_arraylist.add(new LearningObjectivesModel(database_result_set.getString("learning_objective_code"),
                        database_result_set.getString("learning_objective_title_gr"),database_result_set.getString("learning_objective_title_eng"),                        
                        database_result_set.getInt("learning_objective_category"),database_result_set.getString("learning_objective_category_title_gr"),
                        database_result_set.getString("learning_objective_category_title_eng"),database_result_set.getInt("learning_objective_course"),
                        database_result_set.getString("course_title_gr"),database_result_set.getString("course_title_eng")));
            }
            database_statement.close();
            return temp_learning_objective_model_arraylist.isEmpty()?null:temp_learning_objective_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    } 
    
    @GET
    @Path("/getAllFavoriteLearningObjectives")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<LearningObjectivesModel> getAllFavoriteLearningObjectivesInJSON(@QueryParam ("student_id") int student_id)
    {        
        ArrayList<LearningObjectivesModel> temp_learning_objective_model_arraylist = new ArrayList<>();
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT Learning_Objectives.learning_objective_code,learning_objective_title_gr,learning_objective_title_eng,"
                    + "learning_objective_category,learning_objective_course,course_title_gr,course_title_eng "
                    + "FROM Learning_Objectives,Courses,Favorite_Learning_Objectives WHERE "
                    + "Learning_Objectives.learning_objective_course=Courses.course_id AND "
                    + "Learning_Objectives.learning_objective_code=Favorite_Learning_Objectives.learning_objective_code AND "
                    + "Favorite_Learning_Objectives.student_id="+String.valueOf(student_id)+";";
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                temp_learning_objective_model_arraylist.add(new LearningObjectivesModel(database_result_set.getString("Learning_Objectives.learning_objective_code"),
                        database_result_set.getString("learning_objective_title_gr"),database_result_set.getString("learning_objective_title_eng"),                        
                        database_result_set.getInt("learning_objective_category"),database_result_set.getInt("learning_objective_course"),
                        database_result_set.getString("course_title_gr"),database_result_set.getString("course_title_eng")));
            }
            database_statement.close();
            return temp_learning_objective_model_arraylist.isEmpty()?null:temp_learning_objective_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    }
    
    @POST
    @Path("/addFavoriteLearningObjective")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response addFavoriteLearningObjective(@FormParam("learning_objective_code") String learning_objective_code, @FormParam("student_id") int student_id){
        Connection database_connection;
        Statement database_statement;
        PreparedStatement database_prepared_statement;
        ResultSet database_result_set;
        String database_query,response_string;
        int response_code;
        boolean learning_objective_found=false;
        try {
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
                database_statement = database_connection.createStatement();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }
            database_query = "SELECT learning_objective_code FROM Learning_Objectives WHERE learning_objective_code='"+learning_objective_code+"';";
            database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                learning_objective_found = true;
            }
            if(learning_objective_found){
                database_query = "INSERT INTO Favorite_Learning_Objectives (learning_objective_code,student_id) VALUES (?, ?);";
                database_prepared_statement = database_connection.prepareStatement(database_query);
                database_prepared_statement.setString(1, learning_objective_code);
                database_prepared_statement.setInt(2, student_id);
                database_prepared_statement.executeUpdate();
                response_code = 201;
                response_string = "Favorite Learning Objective inserted into database";
                database_prepared_statement.close();
            }else{
                response_code = 400;
                response_string = "Error: Learning Objective not found";
            }
            database_statement.close();
            return Response.status(response_code).entity(response_string).build();
        } catch (Exception ex) {
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
    
    @DELETE
    @Path("/removeFavoriteLearningObjective")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response removeFavoriteLearningObjective(@QueryParam("learning_objective_code") String learning_objective_code, @QueryParam("student_id") int student_id){
        Connection database_connection;
        Statement database_statement;
        PreparedStatement database_prepared_statement;
        ResultSet database_result_set;
        String database_query,response_string;
        int response_code;
        boolean learning_objective_found=false;
        try {
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
                database_statement = database_connection.createStatement();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }
            database_query = "SELECT learning_objective_code FROM Learning_Objectives WHERE learning_objective_code='"+learning_objective_code+"';";
            database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                learning_objective_found = true;
            }
            if(learning_objective_found){
                database_query = "DELETE FROM Favorite_Learning_Objectives WHERE learning_objective_code= ? AND student_id = ?;";                
                database_prepared_statement = database_connection.prepareStatement(database_query);
                database_prepared_statement.setString(1, learning_objective_code);
                database_prepared_statement.setInt(2, student_id);
                database_prepared_statement.executeUpdate();
                response_code = 200;
                response_string = "Favorite Learning Objective deleted from database";
                database_prepared_statement.close();
            }else{
                response_code = 400;
                response_string = "Error: Learning Objective not found";
            }
            database_statement.close();
            return Response.status(response_code).entity(response_string).build();
        } catch (Exception ex) {
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
    
    @GET
    @Path("/getAllLearningObjectiveCategories")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<LearningObjectiveCategoriesModel> getAllLearningObjectiveCategories()
    {        
        ArrayList<LearningObjectiveCategoriesModel> learning_objective_categories_model_arraylist = new ArrayList<>();
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT * FROM Learning_Objective_Categories;";
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                learning_objective_categories_model_arraylist.add(new LearningObjectiveCategoriesModel(database_result_set.getInt("learning_objective_category_id"),
                        database_result_set.getString("learning_objective_category_title_gr"),database_result_set.getString("learning_objective_category_title_eng")));
            }
            database_statement.close();
            return learning_objective_categories_model_arraylist.isEmpty()?null:learning_objective_categories_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    }
    
    @POST
    @Path("/createLearningObjective")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response createLearningObjective(@FormParam("learning_objective_code") String learning_objective_code, @FormParam("learning_objective_title_gr") String learning_objective_title_gr
                ,@FormParam("learning_objective_title_eng") String learning_objective_title_eng, @FormParam("learning_objective_category") int learning_objective_category
                ,@FormParam("learning_objective_course") int learning_objective_course, @FormParam("prerequisites_learning_objectives") List<String> prerequisites_learning_objectives){
        Connection database_connection;
        Statement database_statement;
        PreparedStatement database_prepared_statement;
        ResultSet database_result_set;
        String database_query,response_string;
        boolean learning_objective_found=false;
        int response_code,max=-1;
        try {
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
                database_statement = database_connection.createStatement();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }
            database_query = "SELECT learning_objective_code FROM Learning_Objectives WHERE learning_objective_code LIKE '%"+(learning_objective_code.split("\\.",3)[1])+"%';";
            database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                String[] learning_objective_code_parts = database_result_set.getString("learning_objective_code").split("\\.",3);
                int last_part = Integer.valueOf(learning_objective_code_parts[2]);
                if(max<last_part){
                    max=last_part;
                }
                if(!learning_objective_found) learning_objective_found = true;
            }
            if(learning_objective_found){
                learning_objective_code = learning_objective_code.replace("1", String.valueOf(max+1));
            }
            database_query = "INSERT INTO Learning_Objectives VALUES (?, ?, ? , ?, ?);";
            database_prepared_statement = database_connection.prepareStatement(database_query);
            database_prepared_statement.setString(1, learning_objective_code);
            database_prepared_statement.setString(2, learning_objective_title_gr);
            database_prepared_statement.setString(3, learning_objective_title_eng);
            database_prepared_statement.setInt(4, learning_objective_category);
            database_prepared_statement.setInt(5, learning_objective_course);
            database_prepared_statement.executeUpdate();
            database_prepared_statement.close();
            response_code = 201;
            response_string = "Learning Objective inserted into database";
            if(prerequisites_learning_objectives!=null && !prerequisites_learning_objectives.get(0).replace("[", "").replace("]", "").replace("null", "").equals("")){                    
                String[] prerequisites_learning_objectives_tmp = prerequisites_learning_objectives.get(0).replace("[", "").replace("]", "").split(",");
                database_query = "INSERT INTO Prerequisites_Learning_Objectives VALUES (?, ?);";
                database_prepared_statement = database_connection.prepareStatement(database_query);
                for(int i=0; i<prerequisites_learning_objectives_tmp.length; i++){
                    database_prepared_statement.setString(1, prerequisites_learning_objectives_tmp[i].replace(" ", ""));
                    database_prepared_statement.setString(2, learning_objective_code);
                    database_prepared_statement.addBatch();
                }
                database_prepared_statement.executeBatch();
            }
            database_prepared_statement.close();            
            database_statement.close();
            return Response.status(response_code).entity(response_string).build();
        } catch (Exception ex) {
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
    
    @POST
    @Path("/createLearningObjectiveByFile")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")
    public Response createLearningObjectiveByFile(@FormParam("local_file_path") String local_file_path){
        Connection database_connection;
        Statement database_statement;
        PreparedStatement database_prepared_statement;
        String database_query,response_string;
        int response_code,database_update_result;
        try {
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
                database_statement = database_connection.createStatement();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }
            database_query = "LOAD DATA LOCAL INFILE ? INTO TABLE Learning_Objectives FIELDS TERMINATED BY ','" 
                    +"OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\\n' (learning_objective_code, learning_objective_title_gr,"
                    + " learning_objective_title_eng, learning_objective_category, learning_objective_course);";            
            database_prepared_statement = database_connection.prepareStatement(database_query);
            database_prepared_statement.setString(1, local_file_path);
            database_update_result = database_prepared_statement.executeUpdate();    
            if(database_update_result!=0){
                response_code = 201;
                response_string = "Learning Objectives inserted from file into database";
            }else{
                response_code = 400;
                response_string = "No new Learning Objective was inserted from file into database";
            }      
            database_prepared_statement.close();            
            database_statement.close();
            return Response.status(response_code).entity(response_string).build();
        } catch (Exception ex) {
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
    
    @GET
    @Path("/getPrerequisitesLearningObjectives")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<LearningObjectivesModel> getPrerequisitesLearningObjectivesInJSON(@QueryParam ("code") String learning_objective_code)
    {        
        ArrayList<LearningObjectivesModel> temp_learning_objective_model_arraylist = new ArrayList<>();
        ArrayList<String> prerequisites_learning_objective_code = new ArrayList<>();
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT prerequisite_learning_objective_code"
                    + " FROM Prerequisites_Learning_Objectives WHERE learning_objective_code='"+learning_objective_code+"';";            
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                prerequisites_learning_objective_code.add(database_result_set.getString("prerequisite_learning_objective_code"));
            }
            database_result_set.close();
            String database_temp_query;    
            ResultSet database_temp_result_set;
            for(int i=0; i<prerequisites_learning_objective_code.size(); i++){
                database_temp_query = "SELECT learning_objective_code,learning_objective_title_gr,learning_objective_title_eng,"
                    + "learning_objective_category,learning_objective_course,learning_objective_category_title_gr,"
                    + "learning_objective_category_title_eng,course_title_gr,course_title_eng FROM Learning_Objectives,Courses,Learning_Objective_Categories WHERE "
                    + "Learning_Objectives.learning_objective_course=Courses.course_id AND "
                    + "Learning_Objectives.learning_objective_category=Learning_Objective_Categories.learning_objective_category_id AND "
                    + "learning_objective_code='"+prerequisites_learning_objective_code.get(i)+"';";
                database_temp_result_set = database_statement.executeQuery(database_temp_query);
                if(database_temp_result_set.next()){
                    temp_learning_objective_model_arraylist.add(new LearningObjectivesModel(database_temp_result_set.getString("learning_objective_code"),
                            database_temp_result_set.getString("learning_objective_title_gr"),database_temp_result_set.getString("learning_objective_title_eng"),                        
                            database_temp_result_set.getInt("learning_objective_category"),database_temp_result_set.getString("learning_objective_category_title_gr"),
                            database_temp_result_set.getString("learning_objective_category_title_eng"),database_temp_result_set.getInt("learning_objective_course"),
                            database_temp_result_set.getString("course_title_gr"),database_temp_result_set.getString("course_title_eng")));
                }
            }            
            database_statement.close();
            return temp_learning_objective_model_arraylist.isEmpty()?null:temp_learning_objective_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    } 
}