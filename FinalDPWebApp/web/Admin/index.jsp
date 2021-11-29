<%@page import="com.webservices.LearningObjectivesModel"%>
<%@page import="com.webservices.ProfessorsModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.webservices.TranslationModel"%>
<%@page import="general.FunctionsController"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.webservices.AuthKey"%>
<%@page import="com.webservices.UserModel"%>
<%@page import="java.util.HashMap"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    String language = request.getParameter("language")!=null?request.getParameter("language"):null;
    String error_message = session.getAttribute("error")!=null?session.getAttribute("error").toString():null;
    String other_message = session.getAttribute("message")!=null?session.getAttribute("message").toString():null;
    Cookie[] cookies = null;
    UserModel user_info = null;    
    String [] user_types = AuthKey.getArrayWithUserTypes();
    final String PROFESSOR = user_types[0];
    final String STUDENT= user_types[1];
    final String ADMIN= user_types[2];
    String webServicesStringUrl,responseString,session_language,professors_by_tabs="",learning_objectives_string="";
    boolean is_language_ok=false;
    HashMap<String,String> translation = null;
    ArrayList<ProfessorsModel> professors = null;
    ArrayList<LearningObjectivesModel> learning_objectives = null;
    // Get an array of Cookies associated with this domain
    cookies = request.getCookies();
    if(cookies!=null){        
        for(int i=0; i<cookies.length; i++){
            if(cookies[i].getName().equals("user_json")){
                user_info = new UserModel(URLDecoder.decode(cookies[i].getValue(), "UTF-8")); break;
            }
        }
    }else{
        session.setAttribute("message", "auto_logout");
        response.sendRedirect("../index.jsp");
    }
    if(user_info!=null){ 
        if(user_info.getUser_auth_key().getUser_type().equals(STUDENT)){
            response.sendRedirect("../Student/index.jsp");
        }else if(user_info.getUser_auth_key().getUser_type().equals(PROFESSOR)){
            response.sendRedirect("../Professor/index.jsp");
        }
        if(FunctionsController.checkUserExpiry(user_info.getUser_auth_key())==true){
            response.sendRedirect("../logout.jsp?logout=2");
        }
    }else{
        if(cookies!=null)   response.sendRedirect("../index.jsp");
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
    if(user_info!=null && user_info.getUser_auth_key().getUser_type().equals(ADMIN)){
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/LearningObjectives/getAllLearningObjectives";
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //LearningObjectives found.
            learning_objectives = FunctionsController.createLearningObjectivesModelArrayListFromJSON(responseString);
            for(int i=0; i<learning_objectives.size(); i++){
                if(session_language.equals("gr")){
                    learning_objectives_string+= "<option value=\""+learning_objectives.get(i).getLearning_objective_code()+"\">"
                        + learning_objectives.get(i).getLearning_objective_title_gr() + "</option>";
                }else if(session_language.equals("eng")){
                    learning_objectives_string+= "<option value=\""+learning_objectives.get(i).getLearning_objective_code()+"\">"
                        + learning_objectives.get(i).getLearning_objective_title_eng() + "</option>";
                }
            }
        }
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Professors/getAllProfessors";
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //Courses found.
            professors = FunctionsController.createProfessorsModelArrayListFromJSON(responseString);
            for(int i=0; i<professors.size(); i++){
                if(professors.get(i).isValid()){
                    professors_by_tabs += "<tr class=\"tab-tr\"><td class=\"tab-td\">"+professors.get(i).getName()+" "
                    +professors.get(i).getSurname()+"</td>"+"<td class=\"tab-td\"><a href=\"invalidate.jsp?professor="
                    +String.valueOf(professors.get(i).getId())+"\">"+ "<center><span style=\"font-size:30px;\" class=\"closeX glyphicon glyphicon-remove-circle\"></span></center></a></td>"
                    + "</tr>";
                }else{
                    professors_by_tabs += "<tr class=\"tab-tr\"><td class=\"tab-td\">"+professors.get(i).getName()+" "
                    +professors.get(i).getSurname()+"</td>"+"<td class=\"tab-td\"><a href=\"validate.jsp?professor="
                    +String.valueOf(professors.get(i).getId())+"\">"+ "<center><span style=\"font-size:30px;\" class=\"check glyphicon glyphicon-ok-circle\"></span></center></a></td>"
                    + "</tr>";
                }            
            }
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title><%= translation.get("head_title_"+session_language) %></title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <link href="../CSS_Files/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css"/>
        <link href="../CSS_Files/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="../CSS_Files/jquery/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <link href="../CSS_Files/select2.min.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="../CSS_Files/main.css" type="text/css"/>
        <link rel="shortcut icon" type="image/png" href="../Images/favicon.png"/>
        <script src="../JS_Files/jquery.min.js"></script>
        <script src="../JS_Files/jquery-ui.js"></script>
        <script src="../JS_Files/bootstrap.min.js"></script>
        <script src="../JS_Files/select2.min.js" type="text/javascript"></script>
        <script>
            $( function() {
              $(".sin").select2();
            } );
        </script>
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
                  <li class="active"><a href=""><%=translation.get("homepage_label_"+session_language)%></a></li>
                  <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#"><%=translation.get("courses_label_"+session_language)%>
                    <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                      <li><a href="../courses.jsp?edu=0"><%=translation.get("undergraduate_label_"+session_language)%></a></li>
                      <li><a href="../courses.jsp?edu=1"><%=translation.get("postgraduate_label_"+session_language)%></a></li>
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
                                <li><a href="?language=gr"><img class="flag_icon" src="../Images/greek_flag_icon.ico"></a></li>                            
                                <li><a href="?language=eng"><img class="flag_icon" src="../Images/english_flag_icon.ico"></a></li>
                            </ul>
                        </li>
                        <% if(user_info != null){ %>
                            <li><a href="../logout.jsp?logout=1"><span class="glyphicon glyphicon-off"></span> <%=translation.get("logout_label_"+session_language)%></a></li>
                        <% } %>
                    </ul>
              </div>
            </div>
        </nav>
        <div class="container-fluid">
            <%if(user_info!=null && user_info.getUser_auth_key().getUser_type().equals(ADMIN)){%>
                <div class="container title-container">
                    <%=translation.get("admin_control_panel_title_"+session_language)%>
                    <hr class="my-hr">
                </div>
                <div class="container main-container">
                    <%if(error_message!=null){session.removeAttribute("error");%>
                        <%if(error_message.equals("validate_fail") || error_message.equals("invalidate_fail")){%>
                            <p class="alert alert-danger"><%=translation.get("error_message_server_"+session_language)%></p>
                        <%}%>
                    <%}%>
                    <div id="professor_courses" class="collapse-in">
                        <%if(professors!=null){%>
                            <table class="tab-table">
                                <th class="tab-th"><%=translation.get("professor_label_"+session_language)%></th>
                                <th class="tab-th"><%=translation.get("validate_invalidate_label_"+session_language)%></th>
                                <%=professors_by_tabs%>
                            </table>
                        <%}%>
                        <%if(professors==null){%>
                            <p class="alert alert-info"><%=translation.get("message_professor_courses_not_found_"+session_language)%></p>
                        <%}%>
                        <hr class="main-hr">
                    </div>
                    <div class="courses-search-container">
                        <div class="row">
                            <div class="courses-search-title col-lg-3 col-md-4 col-sm-5 col-xs-12">
                                <%=translation.get("search_courses_label_"+session_language)%>                            
                            </div>
                            <div class="all-courses-box col-lg-3 col-md-3 col-sm-4 col-xs-12">
                                <a class="btn btn-default" href="../courses.jsp"><%=translation.get("search_all_courses_button_"+session_language)%></a>
                            </div>
                        </div>
                        <div class="row">
                            <hr class="col-lg-6 col-md-7 col-sm-9 col-xs-9 courses-search-hr">
                        </div>
                        <div class="row">
                            <div class="learning-objectives-box">
                                <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12"><%=translation.get("by_learning_objectives_label_"+session_language)+":"%></div>
                                <form action="../courses.jsp">
                                    <select name="learning_objective_search" class="sin select2-container col-lg-6 col-md-5 col-sm-5 col-xs-11" required>
                                        <%=learning_objectives_string%>
                                    </select>
                                    <hr class="visible-xs learning-objective-search-hr">
                                    <input class="learning-objective-search-button btn btn-default col-lg-2 col-md-2 col-sm-2 col-xs-10"
                                           type="submit" value="<%=translation.get("search_label_"+session_language)%>">
                                </form>
                            </div>
                        </div>
                    </div>
                </div>                
            <%}%>
            <div class="container bottom-container"></div>
            <div class="my-copyrights"><%="© "+translation.get("copyrights_content_"+session_language)+" | 2017"%></div>
        </div>
    </body>
</html>
