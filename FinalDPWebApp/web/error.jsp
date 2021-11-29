<%@page import="general.FunctionsController"%>
<%@page import="com.webservices.TranslationModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.webservices.UserModel"%>
<%@page language="java" isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    Cookie[] cookies = null;
    UserModel user_info = null;
    String language = request.getParameter("language")!=null?request.getParameter("language"):null;
    String session_language,webServicesStringUrl,responseString;
    boolean is_language_ok=false;
    HashMap<String,String> translation = null;
    // Get an array of Cookies associated with this domain
    cookies = request.getCookies();
    if(cookies!=null){
        for(int i=0; i<cookies.length; i++){
            if(cookies[i].getName().equals("user_json")){
                user_info = new UserModel(URLDecoder.decode(cookies[i].getValue(), "UTF-8")); break;
            }
        }
    }
    if(user_info!=null){        
        if(FunctionsController.checkUserExpiry(user_info.getUser_auth_key())==true){
            response.sendRedirect("logout.jsp?logout=2");
        }
    }
    if(session.getAttribute("language")==null){
        session.setAttribute("language", "gr");
    }else{
        if(language!=null && !session.getAttribute("language").toString().equals(language)){
            session.setAttribute("language", language);
        }
    }
    session_language=session.getAttribute("language").toString();
    for(int i=0; i<TranslationModel.getAVAILABLE_LANGUAGES().length; i++){
        if(session_language.equals(TranslationModel.getAVAILABLE_LANGUAGES()[i])){
            is_language_ok = true;  break;
        }
    }
    if(!is_language_ok){
        session_language = TranslationModel.getAVAILABLE_LANGUAGES()[0];
        session.setAttribute("language", session_language);
    }
    webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Translation/getAllTranslation";
    responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
    if(responseString!=null && !responseString.contains("Error")){
        //Translation found.
        translation = FunctionsController.createTranslationModelHashMapFromJSON(responseString);
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= translation.get("head_title_"+session_language) %></title>
        <link href="CSS_Files/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css"/>
        <link href="CSS_Files/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="CSS_Files/jquery/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="CSS_Files/main.css" type="text/css"/>
        <link rel="shortcut icon" type="image/png" href="Images/favicon.png"/>
        <script src="JS_Files/jquery.min.js"></script>
        <script src="JS_Files/jquery-ui.js"></script>
        <script src="JS_Files/bootstrap.min.js"></script>
    </head>
    <body class="main">        
        <nav class="navbar navbar-default">
            <div class="container-fluid my-navbar">
              <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>                        
                </button>
                  <p class="title"><%= translation.get("head_title_"+session_language) %></p>
              </div>
              <div class="collapse navbar-collapse" id="myNavbar">
                <ul class="nav navbar-nav">
                  <li><a href="index.jsp"><%=translation.get("homepage_label_"+session_language)%></a></li>
                  <li>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#"><%=translation.get("courses_label_"+session_language)%>
                    <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                      <li><a href="courses.jsp?edu=0"><%=translation.get("undergraduate_label_"+session_language)%></a></li>
                      <li><a href="courses.jsp?edu=1"><%=translation.get("postgraduate_label_"+session_language)%></a></li>
                    </ul>
                  </li>
                </ul>                
                    <ul class="nav navbar-nav navbar-right">
                        <% if(user_info != null){ %>
                            <li><a><%=translation.get("welcome_label_"+session_language)+","%> <%=user_info.getName()%> <%=user_info.getSurname()%></a></li>
                        <% } %>
                        <li class="dropdown">
                            <a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-globe"></span>
                            <span class="caret"></span></a>
                            <ul class="dropdown-menu my-dropdown-menu">
                                <li><a href="?language=gr"><img class="flag_icon" src="Images/greek_flag_icon.ico"></a></li>                            
                                <li><a href="?language=eng"><img class="flag_icon" src="Images/english_flag_icon.ico"></a></li>
                            </ul>
                        </li>
                        <% if(user_info != null){ %>
                            <li><a href="logout.jsp?logout=1"><span class="glyphicon glyphicon-off"></span> <%=translation.get("logout_label_"+session_language)%></a></li>
                        <% } %>
                    </ul>                
              </div>
            </div>
        </nav>
        <div class="container-fluid">
            <div class="container title-container">                
                Error 404: Page Not Found
                <hr class="my-hr">
            </div>
            <div class="container main-container">
                <p><b>Error code:</b> ${pageContext.errorData.statusCode}</p>
                <p><b>Request URI:</b> ${pageContext.request.scheme}://${header.host}${pageContext.errorData.requestURI}</p>
                <br />
                <div class="row">
                    <div class="col-lg-4 col-md-3 col-sm-2"></div>
                    <button class="col-lg-4 col-md-6 col-sm-8 col-xs-11 btn btn-primary btn-lg" onclick="history.back()"><%=translation.get("previous_page_button_label_"+session_language)%></button>
                </div>
                
            </div>    
            <div class="container bottom-container"></div>
            <footer>
                <div class="my-copyrights"><%="© "+translation.get("copyrights_content_"+session_language)+" | 2017"%></div>
            </footer>
        </div>
    </body>
</html>
