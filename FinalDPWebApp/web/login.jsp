<%@page import="java.net.URLDecoder"%>
<%@page import="com.fasterxml.jackson.databind.ObjectMapper"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.webservices.AuthKey"%>
<%@page import="com.webservices.AdminModel"%>
<%@page import="com.webservices.StudentsModel"%>
<%@page import="com.webservices.ProfessorsModel"%>
<%@page import="com.webservices.UserModel"%>
<%@page import="general.FunctionsController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("UTF-8");
    final int COOKIES_DEFAULT_EXPIRY = 60*60*AuthKey.getDEFAULT_EXPIRY(); //Set cookies default expiry to 2 hours.
    String [] user_types = AuthKey.getArrayWithUserTypes();
    final String PROFESSOR = user_types[0];
    final String STUDENT= user_types[1];
    final String ADMIN= user_types[2];
    UserModel user_info = null;
    String login_username = request.getParameter("login_user");
    String login_password = request.getParameter("login_pass");    
    
    if(login_username!=null && login_password!=null){
        //User trying to login.
        AuthKey user_auth_key = null;
        String webServicesStringUrl,responseString;
        login_username=URLEncoder.encode(login_username, "UTF-8");
        login_password=URLEncoder.encode(login_password, "UTF-8");
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Controller/Login?username="+
                login_username+"&password="+login_password;
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //LoggedIn.
            Cookie user_info_cookie;
            ObjectMapper mapper = new ObjectMapper();         
            user_auth_key = mapper.readValue(responseString, AuthKey.class);
            
            user_info_cookie = FunctionsController.createUserCookieFromAuthKey(user_auth_key);
            
            if(user_info_cookie!=null && user_info_cookie.getName().equals("user_json")){
                user_info = new UserModel(URLDecoder.decode(user_info_cookie.getValue(), "UTF-8"));
                user_info_cookie.setMaxAge(COOKIES_DEFAULT_EXPIRY);
                response.addCookie(user_info_cookie);          
                if(user_info!=null){
                    if(user_info.getUser_auth_key().getUser_type().equals(STUDENT)){
                        response.sendRedirect("Student/index.jsp");
                    }else if(user_info.getUser_auth_key().getUser_type().equals(PROFESSOR)){
                        response.sendRedirect("Professor/index.jsp");
                    }else if(user_info.getUser_auth_key().getUser_type().equals(ADMIN)){
                        response.sendRedirect("Admin/index.jsp");
                    }
                }
            }else{
                session.setAttribute("error", "login_server_error");
                response.sendRedirect("index.jsp");
            }
        }else{
            //Found an Error.
            String error_message;
            //out.println(responseString); //Debug
            if(responseString.contains("Wrong")){
                error_message = "login_user_fail";
            }else if(responseString.contains("Invalid")){
                error_message = "login_user_invalid";
            }else if(responseString.contains("Password")){
                error_message = "login_user_fail";
            }
            else{
                error_message = "login_server_error";
            }
            session.setAttribute("error", error_message);
            response.sendRedirect("index.jsp");
        }
    }else{
        response.sendRedirect("index.jsp");
    }    
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
    </head>
    <body>
    </body>
</html>