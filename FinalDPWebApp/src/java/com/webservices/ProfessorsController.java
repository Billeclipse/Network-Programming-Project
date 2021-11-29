package com.webservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("Professors")
public class ProfessorsController {

    @Context
    private UriInfo context;
    
    public ProfessorsController() {
    }

    @GET
    @Path("/getAllProfessors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public ArrayList<ProfessorsModel> getAllProfessorsInJSON(){
        ArrayList<ProfessorsModel> professors_model_arraylist = new ArrayList<>();        
        Statement database_statement; 
        try { 
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT * FROM Professors;";
            ResultSet database_result_set = database_statement.executeQuery(database_query);                
            while(database_result_set.next()){
                professors_model_arraylist.add(new ProfessorsModel(database_result_set.getInt("professor_id"),database_result_set.getString("professor_username")
                        ,database_result_set.getString("professor_name"),database_result_set.getString("professor_surname")
                        ,database_result_set.getString("professor_grade"),database_result_set.getInt("professor_valid")==0));
            }
            database_statement.close();
            return professors_model_arraylist.isEmpty()?null:professors_model_arraylist;        
        } catch (Exception ex) {
            return null;
        }
    }
    
    @GET
    @Path("/getProfessor")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ProfessorsModel getProfessorInfoInJSON(@QueryParam ("id") int professor_id)
    {        
        ProfessorsModel temp_professor_model = null;
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT * FROM Professors WHERE professor_id="+String.valueOf(professor_id)+";";
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            if(database_result_set.next()){
                temp_professor_model = new ProfessorsModel(database_result_set.getInt("professor_id"),database_result_set.getString("professor_username")
                        ,database_result_set.getString("professor_name"),database_result_set.getString("professor_surname")
                        ,database_result_set.getString("professor_grade"),database_result_set.getInt("professor_valid")==0);
            }
            database_statement.close();
            return temp_professor_model!=null?temp_professor_model:null;
        } catch (Exception ex) {
            return null;
        }
    }
    
    @GET
    @Path("/getCourseProfessors")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public ArrayList<ProfessorsModel> getCourseProfessorsInJSON(@QueryParam ("course_id") int course_id)
    {        
        ArrayList<ProfessorsModel> temp_professor_model_arraylist = new ArrayList<>();
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT Professors.professor_id,professor_username,professor_name,professor_surname,professor_grade,professor_valid "
                    + "FROM Professors,Professors_Courses WHERE Professors_Courses.professor_id=Professors.professor_id AND "
                    + "Professors_Courses.course_id="+String.valueOf(course_id)+";";
            ResultSet database_result_set = database_statement.executeQuery(database_query);            
            while(database_result_set.next()){
                temp_professor_model_arraylist.add(new ProfessorsModel(database_result_set.getInt("Professors.professor_id"),database_result_set.getString("professor_username")
                        ,database_result_set.getString("professor_name"),database_result_set.getString("professor_surname")
                        ,database_result_set.getString("professor_grade"),database_result_set.getInt("professor_valid")==0));
            }
            database_statement.close();
            return temp_professor_model_arraylist.isEmpty()?null:temp_professor_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    }
    
    @PUT
    @Path("/invalidateProfessor")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")    
    public Response invalidateProfessor(@QueryParam ("professor_id") int professor_id)
    {        
        Connection database_connection;
        PreparedStatement database_prepared_statement;
        String database_query;
        try {
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }
            database_query = "UPDATE Professors SET professor_valid = 1 WHERE professor_id = ?;";
            database_prepared_statement = database_connection.prepareStatement(database_query);
            database_prepared_statement.setInt(1, professor_id);
            database_prepared_statement.executeUpdate();
            database_prepared_statement.close();
            return Response.status(200).entity("Professor invalidated on Database").build();
        } catch (Exception ex) {
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
    
    @PUT
    @Path("/validateProfessor")
    @Produces(MediaType.TEXT_PLAIN + ";charset=utf-8")    
    public Response validateProfessor(@QueryParam ("professor_id") int professor_id)
    {        
        Connection database_connection;
        PreparedStatement database_prepared_statement;
        String database_query;
        try {
            if (new DBConnection().getConnection()!=null){
                database_connection = new DBConnection().getConnection();
            }else{
                return Response.status(400).entity("Error: Couldn't connect to SQL Database").build();
            }
            database_query = "UPDATE Professors SET professor_valid = 0 WHERE professor_id = ?;";
            database_prepared_statement = database_connection.prepareStatement(database_query);
            database_prepared_statement.setInt(1, professor_id);
            database_prepared_statement.executeUpdate();
            database_prepared_statement.close();
            return Response.status(200).entity("Professor validated on Database").build();
        } catch (Exception ex) {
            return Response.status(400).entity("Error: Database Exception -> " + ex.toString()).build();
        }
    }
}