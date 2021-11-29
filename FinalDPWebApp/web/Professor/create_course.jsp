<%@page import="com.webservices.AuthKey"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.webservices.UserModel"%>
<%@page import="general.FunctionsController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");    
    Cookie[] cookies = null;
    UserModel user_info = null; 
    boolean isCourseTitleOk=false;
    String course_title_gr = request.getParameter("course_title_gr")!=null?request.getParameter("course_title_gr"):null;
    String course_title_eng = request.getParameter("course_title_eng")!=null?request.getParameter("course_title_eng"):null;
    String education_level = request.getParameter("education_level")!=null?request.getParameter("education_level"):null;
    String semester = request.getParameter("semester")!=null?request.getParameter("semester"):null;
    String[] prerequisites_courses_tmp = request.getParameterValues("prerequisites_courses")!=null?request.getParameterValues("prerequisites_courses"):null;    
    List<String> prerequisites_courses = prerequisites_courses_tmp!=null?Arrays.asList(prerequisites_courses_tmp):null;    
    String [] user_types = AuthKey.getArrayWithUserTypes();
    final String PROFESSOR = user_types[0];
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
    if(Character.isUpperCase(course_title_eng.charAt(0))){
        isCourseTitleOk=true;
    }
    if(user_info!=null && user_info.getUser_auth_key().getUser_type().equals(PROFESSOR)){
        if(isCourseTitleOk){
            if(course_title_gr!=null && course_title_eng!=null && education_level!=null && semester!=null){
                webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/createCourse?course_title_gr="
                    +course_title_gr+"&course_title_eng="+course_title_eng+"&education_level="+education_level+"&course_semester="
                    +semester+"&prerequisites_courses="+prerequisites_courses+"&professor_id="+String.valueOf(user_info.getUser_auth_key().getId());
                responseString = FunctionsController.getResponse(webServicesStringUrl,"POST");
                if(responseString!=null && responseString.contains("Error")){            
                    //Found an Error.
                    session.setAttribute("error", "create_course_fail");            
                }
                response.sendRedirect("index.jsp");
            }else{
                response.sendRedirect("index.jsp");
            }
        }else{
            session.setAttribute("error", "course_title_fail"); 
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
        <title>Create Course</title>
    </head>
    <body>
    </body>
</html>
