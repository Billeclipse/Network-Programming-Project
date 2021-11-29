package com.webservices;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.webservices.Controller.class);
        resources.add(com.webservices.CoursesController.class);
        resources.add(com.webservices.LearningObjectivesController.class);
        resources.add(com.webservices.ProfessorsController.class);
        resources.add(com.webservices.StudentsController.class);
        resources.add(com.webservices.TranslationController.class);
    }
    
}
