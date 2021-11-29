<%@page import="com.webservices.AuthKey"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.webservices.UserModel"%>
<%@page import="general.FunctionsController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");    
    Cookie[] cookies = null;
    UserModel user_info = null; 
    String add_course = request.getParameter("add_course")!=null?request.getParameter("add_course"):null;
    String add_learning_objective = request.getParameter("add_learning_objective")!=null?request.getParameter("add_learning_objective"):null;
    String [] user_types = AuthKey.getArrayWithUserTypes();
    final String STUDENT= user_types[1];
    String webServicesStringUrl="",responseString;
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
    if(user_info!=null && user_info.getUser_auth_key().getUser_type().equals(STUDENT)){
        if(add_course!=null){
            webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/addFavoriteCourse?course_id="+
                add_course+"&student_id="+String.valueOf(user_info.getUser_auth_key().getId());            
        }else if(add_learning_objective!=null){
            webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/LearningObjectives/addFavoriteLearningObjective?learning_objective_code="+
                add_learning_objective+"&student_id="+String.valueOf(user_info.getUser_auth_key().getId()); 
        }
        responseString = FunctionsController.getResponse(webServicesStringUrl,"POST");
        if(responseString!=null && responseString.contains("Error")){            
            //Found an Error.
            String error_message;
            if(add_course!=null){
                error_message = "add_favorite_course_fail";
            }else{
                error_message = "add_favorite_objective_fail";
            }
            session.setAttribute("error", error_message);            
        }
        response.sendRedirect("index.jsp");
    }else{
        if(cookies!=null)
            response.sendRedirect("../index.jsp");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Favorite</title>
    </head>
    <body>
    </body>
</html>
