<%@page import="com.webservices.TranslationModel"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.webservices.UserModel"%>
<%@page import="com.fasterxml.jackson.core.type.TypeReference"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.webservices.CoursesModel"%>
<%@page import="general.FunctionsController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    Cookie[] cookies = null;
    UserModel user_info = null;
    // Get an array of Cookies associated with this domain
    cookies = request.getCookies();    
    int requested_education_level = request.getParameter("edu")!=null?Integer.parseInt(request.getParameter("edu")):-1;
    String requested_learning_objective = request.getParameter("learning_objective_search")!=null?request.getParameter("learning_objective_search"):null;    
    String language = request.getParameter("language")!=null?request.getParameter("language"):null;
    int max_semester=-1;
    boolean is_language_ok=false;
    HashMap<String,String> translation = null;
    ArrayList<CoursesModel> courses = null;
    ArrayList<CoursesModel> learning_objective_courses = null;
    String webServicesStringUrl,responseString,session_language;
    String title;
    String[] courses_titles_by_tabs = new String[8];
    for(int i=0; i<8; i++){
        courses_titles_by_tabs[i]="";
    }
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
        session.setAttribute("language", "greek");
    }else{
        if(language!=null && !session.getAttribute("language").toString().equals(language)){
            session.setAttribute("language", language);
        }
    }
    session_language=session.getAttribute("language").toString();
    for(int i=0; i<TranslationModel.getAVAILABLE_LANGUAGES().length; i++){
        if(session_language.equals(TranslationModel.getAVAILABLE_LANGUAGES()[i])){
            is_language_ok = true; break;
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
    title = translation.get("courses_label_"+session_language);
    if(requested_education_level==0){
        title = translation.get("undergraduate_label_"+session_language)+" "+translation.get("courses_label_"+session_language);
    }else if(requested_education_level==1){
        title = translation.get("postgraduate_label_"+session_language)+" "+translation.get("courses_label_"+session_language);
    }
    if(requested_learning_objective!=null){
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/getLearningObjectiveCourses?learning_objective_code="+requested_learning_objective;
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            max_semester=0;
            title = translation.get("learning_objective_courses_title_"+session_language)+" -> "+requested_learning_objective;
            //LearningObjectives found.
            learning_objective_courses = FunctionsController.createCoursesModelArrayListFromJSON(responseString);            
            for(int i=0; i<learning_objective_courses.size(); i++){
                if(session_language.equals("gr")){
                    courses_titles_by_tabs[0] += "<tr class=\"tab-tr\"><td class=\"tab-td\"><a href=\"course_info.jsp?course="
                        +String.valueOf(learning_objective_courses.get(i).getId())+"\">"+learning_objective_courses.get(i).getTitle_gr()+"</a></td>"+"<td class=\"tab-td\">"
                        +(learning_objective_courses.get(i).getEducation_level()==0?"Προπτυχιακό":"Μεταπτυχιακό")+"</td>"+"<td class=\"visible-lg visible-md visible-sm tab-td\">"
                        +String.valueOf(learning_objective_courses.get(i).getSemester())+"</td></tr>";
                }else if(session_language.equals("eng")){
                    courses_titles_by_tabs[0] += "<tr class=\"tab-tr\"><td class=\"tab-td\"><a href=\"course_info.jsp?course="
                        +String.valueOf(learning_objective_courses.get(i).getId())+"\">"+learning_objective_courses.get(i).getTitle_eng()+"</a></td>"+"<td class=\"tab-td\">"
                        +(learning_objective_courses.get(i).getEducation_level()==0?"Undergraduate":"Postgraduate")+"</td>"+"<td class=\"visible-lg visible-md visible-sm tab-td\">"
                        +String.valueOf(learning_objective_courses.get(i).getSemester())+"</td></tr>";
                }
            }
        }
    }else{
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/getAllCourses";
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //Courses found.        
            courses = FunctionsController.createCoursesModelArrayListFromJSON(responseString);
        }else{
            //No courses found.
            session.setAttribute("error", "login_server_error");
            response.sendRedirect("index.jsp");
        }    
        if(requested_education_level!=-1 && courses!=null){
            for(int i=0; i<courses.size(); i++){                      
                if(courses.get(i).getEducation_level()==requested_education_level){
                    if(max_semester<courses.get(i).getSemester()) max_semester=courses.get(i).getSemester();
                    if(session_language.equals("gr")){
                        courses_titles_by_tabs[(courses.get(i).getSemester()-1)] += "<tr class=\"tab-tr\"><td class=\"tab-td\"><a href=\"course_info.jsp?course="+String.valueOf(courses.get(i).getId())
                                +"\">"+courses.get(i).getTitle_gr()+"</a></td></tr>";  
                    }else if(session_language.equals("eng")){
                        courses_titles_by_tabs[(courses.get(i).getSemester()-1)] += "<tr class=\"tab-tr\"><td class=\"tab-td\"><a href=\"course_info.jsp?course="+String.valueOf(courses.get(i).getId())
                            +"\">"+courses.get(i).getTitle_eng()+"</a></td></tr>";  
                    }
                }
            }
        }else{
            for(int i=0; i<courses.size(); i++){
                if(max_semester<courses.get(i).getSemester()) max_semester=courses.get(i).getSemester();
                if(session_language.equals("gr")){
                    courses_titles_by_tabs[(courses.get(i).getSemester()-1)] += "<tr class=\"tab-tr\"><td class=\"tab-td\"><a href=\"course_info.jsp?course="+String.valueOf(courses.get(i).getId())
                                +"\">"+courses.get(i).getTitle_gr()+"</a></td>"+"<td class=\"tab-td\">"+(courses.get(i).getEducation_level()==0?"Προπτυχιακό":"Μεταπτυχιακό")
                                +"</td></tr>";
                }else if(session_language.equals("eng")){
                    courses_titles_by_tabs[(courses.get(i).getSemester()-1)] += "<tr class=\"tab-tr\"><td class=\"tab-td\"><a href=\"course_info.jsp?course="+String.valueOf(courses.get(i).getId())
                                +"\">"+courses.get(i).getTitle_eng()+"</a></td>"+"<td class=\"tab-td\">"+(courses.get(i).getEducation_level()==0?"Undergraduate":"Postgraduate")
                                +"</td></tr>";
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
        <link href="CSS_Files/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css"/>
        <link href="CSS_Files/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="CSS_Files/jquery/jquery-ui.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="CSS_Files/main.css" type="text/css"/>
        <link rel="shortcut icon" type="image/png" href="Images/favicon.png"/>
        <script src="JS_Files/jquery.min.js"></script>
        <script src="JS_Files/jquery-ui.js"></script>
        <script src="JS_Files/bootstrap.min.js"></script>
        <script>
            $( function() {
              $( "#tabs" ).tabs();
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
                                <% if(requested_learning_objective!=null){ %>
                                    <li><a href="?learning_objective_search=<%=requested_learning_objective%>&language=greek"><img class="flag_icon" src="Images/greek_flag_icon.ico"></a></li>                            
                                    <li><a href="?learning_objective_search=<%=requested_learning_objective%>&language=english"><img class="flag_icon" src="Images/english_flag_icon.ico"></a></li>
                                <%}else{%>
                                    <li><a href="?edu=<%=requested_education_level%>&language=gr"><img class="flag_icon" src="Images/greek_flag_icon.ico"></a></li>                            
                                    <li><a href="?edu=<%=requested_education_level%>&language=eng"><img class="flag_icon" src="Images/english_flag_icon.ico"></a></li>
                                <%}%>
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
                <div id="tabs" class="my-tab">
                    <ul class="my-tab-bar">
                      <% for(int i=1; i<max_semester+1; i++){ %>
                        <li><a href="#tabs-<%=i%>"><%=translation.get("semester_label_"+session_language)+" "%><%=i%></a></li>
                      <%}%>
                    </ul>
                    <% for(int i=1; i<max_semester+1; i++){ %>
                        <div id="tabs-<%=i%>">     
                            <table class="tab-table">
                                <th class="tab-th"><%=translation.get("course_name_title_"+session_language)%></th><%if(requested_education_level==-1){%><th class="tab-th"><%=translation.get("education_level_title_"+session_language)%></th><%}%>
                                <%if(requested_learning_objective!=null){%><th class="tab-th"><%=translation.get("semester_label_"+session_language)%></th><%}%>
                                <%=courses_titles_by_tabs[i-1]%>
                            </table>                        
                        </div>
                    <%}%>
                    <%if(requested_learning_objective!=null){%>
                        <table class="tab-table">
                            <th class="visible-lg visible-md visible-sm tab-th"><%=translation.get("course_name_title_"+session_language)%></th><th class="visible-lg visible-md visible-sm tab-th"><%=translation.get("education_level_title_"+session_language)%></th>
                            <th class="visible-lg visible-md visible-sm tab-th"><%=translation.get("semester_label_"+session_language)%></th>
                            <%=courses_titles_by_tabs[0]%>
                        </table>
                    <%}%>
                </div>
            </div>
            <div class="container bottom-container"></div>
            <footer>
                <div class="my-copyrights"><%="© "+translation.get("copyrights_content_"+session_language)+" | 2017"%></div>
            </footer>
        </div>
    </body>
</html>
