<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.webservices.AuthKey"%>
<%@page import="com.webservices.CoursesModel"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="general.FunctionsController"%>
<%@page import="com.webservices.UserModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");    
    Cookie[] cookies = null;
    UserModel user_info = null; 
    String learning_objective_title_gr = request.getParameter("learning_objective_title_gr")!=null?request.getParameter("learning_objective_title_gr"):null;
    String learning_objective_title_eng = request.getParameter("learning_objective_title_eng")!=null?request.getParameter("learning_objective_title_eng"):null;
    int learning_objective_category = request.getParameter("learning_objective_category")!=null?Integer.valueOf(request.getParameter("learning_objective_category")):-1;
    int learning_objective_course = request.getParameter("learning_objective_course")!=null?Integer.valueOf(request.getParameter("learning_objective_course")):-1;
    String[] prerequisites_learning_objectives_tmp = request.getParameterValues("prerequisites_learning_objectives")!=null?request.getParameterValues("prerequisites_learning_objectives"):null;
    List<String> prerequisites_learning_objectives = prerequisites_learning_objectives_tmp!=null?Arrays.asList(prerequisites_learning_objectives_tmp):null;
    String [] user_types = AuthKey.getArrayWithUserTypes();
    final String PROFESSOR = user_types[0];
    String webServicesStringUrl="",responseString,learning_objective_code;
    CoursesModel course = null;
    cookies = request.getCookies();
    if(cookies!=null){        
        for(int i=0; i<cookies.length; i++){
            if(cookies[i].getName().equals("user_json")){
                user_info = new UserModel(URLDecoder.decode(cookies[i].getValue(), "UTF-8")); break;
            }
        }
    }else{
        response.sendRedirect("../index.jsp");
    }
    if(user_info!=null && user_info.getUser_auth_key().getUser_type().equals(PROFESSOR)){
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/getCourse?id="+learning_objective_course;
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            course = FunctionsController.createCoursesModelFromJSON(responseString);
        }    
        if (course!=null){
            String[] course_title_parts = course.getTitle_eng().split(" ");
            String course_title_anagram="";
            for(int i=0; i<course_title_parts.length; i++){
                if(Character.isUpperCase(course_title_parts[i].charAt(0))){
                    course_title_anagram += course_title_parts[i].charAt(0);
                }
            }
            learning_objective_code = "CS."+course_title_anagram+".1";
            if(learning_objective_title_gr!=null && learning_objective_title_eng!=null && learning_objective_category!=-1 && learning_objective_course!=-1){
                webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/LearningObjectives/createLearningObjective?learning_objective_code="
                    +learning_objective_code+"&learning_objective_title_gr="+learning_objective_title_gr+"&learning_objective_title_eng="+learning_objective_title_eng
                    +"&learning_objective_category="+learning_objective_category+"&learning_objective_course="+learning_objective_course
                    +"&prerequisites_learning_objectives="+prerequisites_learning_objectives;
                responseString = FunctionsController.getResponse(webServicesStringUrl,"POST");
                if(responseString!=null && responseString.contains("Error")){            
                    //Found an Error.
                    session.setAttribute("error", "create_objective_fail");            
                }else{
                    //Success
                    session.setAttribute("message", "create_objective_success");  
                }
                response.sendRedirect("index.jsp");
            }else{
                response.sendRedirect("index.jsp");
            }
        }else{
            response.sendRedirect("index.jsp");
        }        
    }else{
        if(cookies!=null)
            response.sendRedirect("../index.jsp");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Learning Objective</title>
    </head>
    <body>
    </body>
</html>
