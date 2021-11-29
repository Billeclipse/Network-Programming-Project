<%@page import="general.FunctionsController"%>
<%@page import="java.net.URLEncoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    request.setCharacterEncoding("UTF-8");
    String register_username = request.getParameter("register_user");
    String register_password = request.getParameter("register_pass");
    String register_name = request.getParameter("register_name");
    String register_surname = request.getParameter("register_surname");
    String register_type = request.getParameter("register_type");
    String register_grade = request.getParameter("register_grade");    
    if(register_username!=null && register_password!=null && register_name!=null && register_surname!=null && register_type!=null){
        //User trying to login.
        String webServicesStringUrl,responseString;
        register_username=URLEncoder.encode(register_username, "UTF-8");
        register_password=URLEncoder.encode(register_password, "UTF-8");
        register_name=URLEncoder.encode(register_name, "UTF-8");
        register_surname=URLEncoder.encode(register_surname, "UTF-8");
        if(register_grade.equals("")){
            webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Controller/Register?username="+
                register_username+"&password="+register_password+"&name="+register_name+"&surname="+register_surname;
        }else{
            register_grade=URLEncoder.encode(register_grade, "UTF-8");
            webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Controller/Register?username="+
                register_username+"&password="+register_password+"&name="+register_name+"&surname="+register_surname+"&grade="+register_grade;
        }
        responseString = FunctionsController.getResponse(webServicesStringUrl,"POST");
        if(responseString!=null && !responseString.contains("Error") && register_grade.equals("")){
            //Student Registered.
        %>
            <form id="login_form" method="post" action="login.jsp">
                <input type="hidden" name="login_user" value="<%=register_username%>"/>
                <input type="hidden" name="login_pass" value="<%=register_password%>"/>
                <input type="submit"/>
            </form>
        <%
        }else if(responseString!=null && !responseString.contains("Error")){
            //Professor Registered.
            String done_message;
            done_message = "professor_register_done";
            session.setAttribute("message", done_message);
            response.sendRedirect("index.jsp");
        }else if(responseString!=null && responseString.contains("Error")){
            //Found an Error.
            String error_message;
            //out.println(responseString); //Debug
            if(responseString.contains("Username has already been taken")){
                error_message = "register_user_fail";
            }else{
                error_message = "register_server_error";
            }
            session.setAttribute("error", error_message);
            response.sendRedirect("index.jsp");
        }else{            
            response.sendRedirect("index.jsp");
        }
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register Page</title>
    </head>
    <body onload="autoRedirect()">
        <script type="text/javascript">
            function autoRedirect() {
                document.getElementById('login_form').submit();
            }                        
        </script>
    </body>
</html>
