<%@page import="com.webservices.AuthKey"%>
<%@page import="general.FunctionsController"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.webservices.UserModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");    
    Cookie[] cookies = null;
    UserModel user_info = null; 
    String professor = request.getParameter("professor")!=null?request.getParameter("professor"):null;
    String [] user_types = AuthKey.getArrayWithUserTypes();
    final String ADMIN= user_types[2];
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
    if(user_info!=null && user_info.getUser_auth_key().getUser_type().equals(ADMIN)){
        if(professor!=null){
            webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Professors/validateProfessor?professor_id="+professor;            
        }
        responseString = FunctionsController.getResponse(webServicesStringUrl,"PUT");
        if(responseString!=null && responseString.contains("Error")){            
            //Found an Error.
            session.setAttribute("error", "validate_fail");            
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
        <title>Validate</title>
    </head>
    <body>
    </body>
</html>