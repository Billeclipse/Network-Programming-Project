<%@page import="com.webservices.TranslationModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webservices.ProfessorsModel"%>
<%@page import="com.webservices.LearningObjectivesModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.webservices.CoursesModel"%>
<%@page import="general.FunctionsController"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.webservices.UserModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    Cookie[] cookies = null;
    UserModel user_info = null;
    String course_id = request.getParameter("course")!=null?request.getParameter("course"):null;
    String language = request.getParameter("language")!=null?request.getParameter("language"):null;
    if(course_id==null) response.sendRedirect("index.jsp");
    String title = "",webServicesStringUrl,responseString,prerequisites_courses_string="",professors_courses_string="",session_language,education_level="";
    boolean is_language_ok=false;
    HashMap<String,String> translation = null;
    CoursesModel course_info = null;
    ArrayList<LearningObjectivesModel> learning_objectives = null;
    ArrayList<CoursesModel> prerequisites_courses = null;
    ArrayList<ProfessorsModel> professors = null;
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
    webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/getCourse?id="+course_id;
    responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
    if(responseString!=null && !responseString.contains("Error")){
        //Course found.        
        course_info = FunctionsController.createCoursesModelFromJSON(responseString);
        if(session_language.equals("gr")){
            title = course_info.getTitle_gr();
            education_level = course_info.getEducation_level()==0?"Προπτυχιακό":"Μεταπτυχιακό";
        }else if(session_language.equals("eng")){
            title = course_info.getTitle_eng();
            education_level = course_info.getEducation_level()==0?"Undergraduate":"Postgraduate";
        }
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Professors/getCourseProfessors?course_id="+course_id;
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //Professors found.
            professors = FunctionsController.createProfessorsModelArrayListFromJSON(responseString);
            for(int i=0; i<professors.size(); i++){
                professors_courses_string += professors.get(i).getName() + " " + professors.get(i).getSurname();
                if(i!=professors.size()-1){
                    professors_courses_string += ", ";
                }
            }
        }
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/getPrerequisitesCourses?course_id="+course_id;
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //LearningObjectives found.
            prerequisites_courses = FunctionsController.createCoursesModelArrayListFromJSON(responseString);
            for(int i=0; i<prerequisites_courses.size(); i++){
                if(session_language.equals("gr")){
                    prerequisites_courses_string += prerequisites_courses.get(i).getTitle_gr();
                }else if(session_language.equals("eng")){
                    prerequisites_courses_string += prerequisites_courses.get(i).getTitle_eng();
                }
                if(i!=prerequisites_courses.size()-1){
                    prerequisites_courses_string += ", ";
                }
            }
        }
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/LearningObjectives/getCourseLearningObjectives?course_id="+course_id;
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //LearningObjectives found.
            learning_objectives = FunctionsController.createLearningObjectivesModelArrayListFromJSON(responseString);
        }      
    }else{
        //Course not found.
        response.sendRedirect("index.jsp");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= translation.get("head_title_"+session_language) %></title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
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
                  <li class="dropdown active">
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
                                <li><a href="?course=<%=course_id%>&language=gr"><img class="flag_icon" src="Images/greek_flag_icon.ico"></a></li>                            
                                <li><a href="?course=<%=course_id%>&language=eng"><img class="flag_icon" src="Images/english_flag_icon.ico"></a></li>
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
                <%=title%>
                <hr class="my-hr">
            </div>
            <div class="container main-container">
                <%if(course_info!=null){%>
                    <table>
                        <tr>
                            <th><%=translation.get("semester_label_"+session_language)+":"%></th><td><%= String.valueOf(course_info.getSemester()) %></td>                            
                        </tr>
                        <tr>
                            <th><%=translation.get("education_level_title_"+session_language)+":"%></th><td><%= education_level %></td>
                        </tr>
                        <%if(!professors_courses_string.equals("")){%>
                            <tr>
                                <th><%=translation.get("professors_label_"+session_language)+":"%></th><td><%= professors_courses_string %></td>
                            </tr>
                        <%}%>
                        <%if(!prerequisites_courses_string.equals("")){%>
                            <tr>
                                <th><%=translation.get("prerequisites_label_"+session_language)+" "+translation.get("courses_label_"+session_language)+":"%></th><td><%= prerequisites_courses_string %></td>
                            </tr>
                        <%}%>
                        <%if(learning_objectives!=null && !learning_objectives.isEmpty()){
                        %>                            
                            <th><%=translation.get("learning_objectives_label_"+session_language)%></th>
                        <%
                            String learning_objectives_title="",learning_objectives_code="";
                            for(int i=0; i<learning_objectives.size(); i++){
                                learning_objectives_code = learning_objectives.get(i).getLearning_objective_code();
                                if(session_language.equals("gr")){
                                    learning_objectives_title=learning_objectives.get(i).getLearning_objective_title_gr();
                                }else if(session_language.equals("eng")){
                                    learning_objectives_title=learning_objectives.get(i).getLearning_objective_title_eng();
                                }                            
                            %>
                            <table>
                                <tr>
                                    <td>• <a href="learning_objective_info.jsp?code=<%=learning_objectives_code%>"><%= learning_objectives_title %></a></td>
                                </tr>
                            </table>
                            <%}
                        }%>
                            
                    </table>
                <%}%>
                <div class="row">
                    <button style="margin: 20px 0 0 25px;" class="col-lg-3 col-md-4 col-sm-5 col-xs-10 btn btn-primary" onclick="history.back()">
                        <%=translation.get("previous_page_button_label_"+session_language)%>
                    </button>
                </div>
            </div>    
            <div class="container bottom-container"></div>
            <footer>
                <div class="my-copyrights"><%="© "+translation.get("copyrights_content_"+session_language)+" | 2017"%></div>
            </footer>
        </div>
    </body>
</html>
