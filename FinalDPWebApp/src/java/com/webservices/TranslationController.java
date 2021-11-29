package com.webservices;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("Translation")
public class TranslationController {
    
    @Context
    private UriInfo context;
    
    public TranslationController() {
    }
    
    @GET
    @Path("/getAllTranslation")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public ArrayList<TranslationModel> getAllTranslationInJSON(){
        ArrayList<TranslationModel> translation_model_arraylist = new ArrayList<>();        
        Statement database_statement; 
        try { 
            if (new DBConnection().getConnection()!=null){
                database_statement = new DBConnection().getConnection().createStatement();
            }else{
                return null;
            }
            String database_query = "SELECT * FROM Translation;";
            ResultSet database_result_set = database_statement.executeQuery(database_query);                
            while(database_result_set.next()){
                translation_model_arraylist.add(new TranslationModel(database_result_set.getString("translation_key"),
                        database_result_set.getString("translation_value")));
            }
            database_statement.close();
            return translation_model_arraylist.isEmpty()?null:translation_model_arraylist;        
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
}
