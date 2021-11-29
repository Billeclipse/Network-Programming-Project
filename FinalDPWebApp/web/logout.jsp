<%@page import="java.net.URLDecoder"%>
<%@page import="com.webservices.UserModel"%>
<%@page import="general.FunctionsController"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    int logout_code = request.getParameter("logout")!=null?Integer.valueOf(request.getParameter("logout")):-1;
    UserModel user_info = null;        
    if(logout_code!=-1){        
        //User trying to logout.
        Cookie[] cookies = request.getCookies();
        Cookie user_cookie=null;
        String webServicesStringUrl;
        if(cookies != null){
            for (int i = 0; i < cookies.length; i++){
               if(cookies[i].getName().equals("user_json")){
                   user_cookie = cookies[i];
                   user_info = new UserModel(URLDecoder.decode(user_cookie.getValue(), "UTF-8"));
                   break;
               }
            } 
        }
        if(user_info!=null){                
            webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Controller/Logout?auth_key="+user_info.getUser_auth_key().getAuth_key(); 
            FunctionsController.getResponse(webServicesStringUrl,"DELETE");
            if(user_cookie!=null){
                user_cookie.setMaxAge(0); //Delete cookie.
                response.addCookie(user_cookie); 
            }
            if(logout_code==2){
                session.setAttribute("message", "auto_logout");
            }
            //System.out.println("User logged out"); //Debug
        }
        response.sendRedirect("index.jsp"); 
    }
%>    
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Logout Page</title>
    </head>
    <body>
    </body>
</html>
