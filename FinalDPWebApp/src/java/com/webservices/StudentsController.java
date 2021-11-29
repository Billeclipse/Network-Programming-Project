package com.webservices;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("Students")
public class StudentsController {

    @Context
    private UriInfo context;

    public StudentsController() {
    }
    
    @GET
    @Path("/getAllStudents")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public ArrayList<StudentsModel> getAllStudentsInJSON(){
        ArrayList<StudentsModel> students_model_arraylist = new ArrayList<>();
        Statement database_statement;
        try {  
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT * FROM Students;";
            ResultSet database_result_set = database_statement.executeQuery(database_query);
            while(database_result_set.next()){
                students_model_arraylist.add(new StudentsModel(database_result_set.getInt("student_id"),database_result_set.getString("student_username")
                        ,database_result_set.getString("student_name"),database_result_set.getString("student_surname")
                        ,database_result_set.getInt("student_valid")==0));
            }
            database_statement.close();
            return students_model_arraylist.isEmpty()?null:students_model_arraylist;
        } catch (Exception ex) {
            return null;
        }
    }
    
    @GET
    @Path("/getStudent")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")    
    public StudentsModel getCourseInformationInJSON(@QueryParam ("id") int student_id)
    {
        StudentsModel temp_students_model = null;        
        Statement database_statement;
        try {
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT * FROM Students WHERE student_id="+String.valueOf(student_id)+";";
            ResultSet database_result_set = database_statement.executeQuery(database_query);        
            if(database_result_set.next()){
                temp_students_model = new StudentsModel(database_result_set.getInt("student_id"),database_result_set.getString("student_username")
                        ,database_result_set.getString("student_name"),database_result_set.getString("student_surname"),database_result_set.getBoolean("student_valid"));
            }
            database_statement.close();
            return temp_students_model!=null?temp_students_model:null;
        } catch (Exception ex) {
            return null;
        }
    } 

}
