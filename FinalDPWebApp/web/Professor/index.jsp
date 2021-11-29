<%@page import="com.webservices.LearningObjectivesModel"%>
<%@page import="com.webservices.AuthKey"%>
<%@page import="com.webservices.TranslationModel"%>
<%@page import="general.FunctionsController"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webservices.LearningObjectiveCategoriesModel"%>
<%@page import="com.webservices.CoursesModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.webservices.UserModel"%>
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
    String webServicesStringUrl,responseString,learning_objectives_string="",courses_string=""
            ,session_language,learning_objective_categories_string="",professor_courses_string="",professor_courses_by_tabs="";
    boolean is_language_ok=false;
    ArrayList<CoursesModel> professor_courses = null;    
    ArrayList<CoursesModel> courses = null;
    ArrayList<LearningObjectivesModel> learning_objectives = null;
    ArrayList<LearningObjectiveCategoriesModel> learning_objective_categories = null;
    HashMap<String,String> translation = null;
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
        }else if(user_info.getUser_auth_key().getUser_type().equals(ADMIN)){
            response.sendRedirect("../Admin/index.jsp");
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
    if(user_info!=null && user_info.getUser_auth_key().getUser_type().equals(PROFESSOR)){
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
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/getAllCourses";
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //Courses found.
            courses = FunctionsController.createCoursesModelArrayListFromJSON(responseString);
            for(int i=0; i<courses.size(); i++){
                if(session_language.equals("gr")){
                    courses_string+= "<option value=\""+courses.get(i).getId()+"\">"
                        + courses.get(i).getTitle_gr() + "</option>";
                }else if(session_language.equals("eng")){
                    courses_string+= "<option value=\""+courses.get(i).getId()+"\">"
                        + courses.get(i).getTitle_eng() + "</option>";
                }
            }
        }
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/LearningObjectives/getAllLearningObjectiveCategories";
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //Courses found.
            learning_objective_categories = FunctionsController.createLearningObjectiveCategoriesModelArrayListFromJSON(responseString);
            for(int i=0; i<learning_objective_categories.size(); i++){
                if(session_language.equals("gr")){
                    learning_objective_categories_string+= "<option value=\""+learning_objective_categories.get(i).getId()+"\">"
                        + learning_objective_categories.get(i).getTitle_gr() + "</option>";
                }else if(session_language.equals("eng")){
                    learning_objective_categories_string+= "<option value=\""+learning_objective_categories.get(i).getId()+"\">"
                        + learning_objective_categories.get(i).getTitle_eng() + "</option>";
                }
            }
        }
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/getProfessorCourses?professor_id="
                +String.valueOf(user_info.getUser_auth_key().getId());
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //Courses found.
            professor_courses = FunctionsController.createCoursesModelArrayListFromJSON(responseString);
            for(int i=0; i<professor_courses.size(); i++){
                if(session_language.equals("gr")){
                    professor_courses_string+= "<option value=\""+professor_courses.get(i).getId()+"\">"
                        + professor_courses.get(i).getTitle_gr() + "</option>";
                    professor_courses_by_tabs += "<tr class=\"tab-tr\"><td class=\"tab-td\"><a href=\"../course_info.jsp?course="
                            +String.valueOf(professor_courses.get(i).getId())+"\">"+professor_courses.get(i).getTitle_gr()+"</a></td>"
                            +"<td class=\"tab-td\">"+String.valueOf(professor_courses.get(i).getSemester())+"</td>" + "</tr>";
                }else if(session_language.equals("eng")){
                    professor_courses_string+= "<option value=\""+professor_courses.get(i).getId()+"\">"
                        + professor_courses.get(i).getTitle_eng() + "</option>";
                    professor_courses_by_tabs += "<tr class=\"tab-tr\"><td class=\"tab-td\"><a href=\"../course_info.jsp?course="
                            +String.valueOf(professor_courses.get(i).getId())+"\">"+professor_courses.get(i).getTitle_eng()+"</a></td>"
                            +"<td class=\"tab-td\">"+String.valueOf(professor_courses.get(i).getSemester())+"</td>" + "</tr>";
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
            <%if(user_info!=null && user_info.getUser_auth_key().getUser_type().equals(PROFESSOR)){%>
                <div class="container title-container">
                    <%=translation.get("professor_control_panel_title_"+session_language)%>
                    <hr class="my-hr">
                </div>
                <div class="container main-container">
                    <%if(error_message!=null){session.removeAttribute("error");%>
                        <%if(error_message.equals("create_course_fail")){%>
                            <p class="alert alert-danger"><%=translation.get("error_message_add_favorite_course_fail_"+session_language)%></p>
                        <%}else if(error_message.equals("course_title_fail")){%>
                            <p class="alert alert-danger"><%=translation.get("error_message_course_title_"+session_language)%></p>
                        <%}else if(error_message.equals("create_objective_fail")){%>
                            <p class="alert alert-danger"><%=translation.get("error_message_create_objective_fail_"+session_language)%></p>
                        <%}%>
                    <%}%>
                    <%if(other_message!=null){session.removeAttribute("message");%>
                        <%if(other_message.equals("create_objective_success")){%>
                            <p class="alert alert-success"><%=translation.get("message_create_objective_success_"+session_language)%></p>
                        <%}%>
                    <%}%>
                    <div class="panel-group" id="accordion">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-lg-6 col-md-7 col-sm-9 col-xs-12">
                                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#professor_courses">
                                            <%=translation.get("professor_courses_title_"+session_language)%> <span style="font-size:20px;" class="glyphicon glyphicon-collapse-down"</span>
                                        </a>                           
                                    </div>
                                </div>
                            </div>
                            <div id="professor_courses" class="accordion-body collapse">
                                <%if(professor_courses!=null){%>
                                    <table class="tab-table">
                                        <th class="tab-th"><%=translation.get("course_name_title_"+session_language)%></th>
                                        <th class="tab-th"><%=translation.get("semester_label_"+session_language)%></th>
                                        <%=professor_courses_by_tabs%>
                                    </table>
                                <%}%>
                                <%if(professor_courses==null){%>
                                    <p class="alert alert-info"><%=translation.get("message_professor_courses_not_found_"+session_language)%></p>
                                <%}%>
                                <hr class="main-hr">
                            </div>
                        </div>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="row">
                                    <div class="col-lg-5 col-md-6 col-sm-7 col-xs-12">
                                        <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#create_course_form">
                                            <%=translation.get("create_course_form_title_"+session_language)%> <span style="font-size:20px;" class="glyphicon glyphicon-collapse-down"</span>
                                        </a>                           
                                    </div>
                                </div>
                            </div>
                            <form id="create_course_form" class="accordion-body collapse in" action="create_course.jsp" method="POST">
                                <div class="row form-group">
                                    <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
                                        <label class="create_course_label" for="course_title_gr"> <%=translation.get("create_form_greek_title_label_"+session_language)+":"%> </label>
                                    </div>
                                    <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12" style="margin-left: -15px;">
                                    <input type="text" class="form-control" name="course_title_gr" 
                                           oninvalid="setCustomValidity(' <%=translation.get("invalid_empty_control_"+session_language)%> ')"
                                           onchange="try{setCustomValidity('')}catch(e){}" maxlength="60" required>
                                    </div>
                                </div> 
                                <div class="row form-group">
                                    <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
                                        <label class="create_course_label" for="course_title_eng"> <%=translation.get("create_form_english_title_label_"+session_language)+":"%> </label>
                                    </div>
                                    <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12" style="margin-left: -15px;">
                                    <input type="text" class="form-control" name="course_title_eng" 
                                           oninvalid="setCustomValidity(' <%=translation.get("invalid_empty_control_"+session_language)%> ')"
                                           onchange="try{setCustomValidity('')}catch(e){}" maxlength="60" required>
                                    </div>
                                </div>
                                <div class="row form-group">
                                    <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
                                        <label class="create_course_label" for="education_level"> <%=translation.get("education_level_title_"+session_language)+":"%> </label>
                                    </div>
                                    <div class="visible-xs col-xs-1"></div>
                                    <select name="education_level" class="sin select2-container col-lg-3 col-md-5 col-sm-5 col-xs-9" 
                                            oninvalid="setCustomValidity(' <%=translation.get("invalid_empty_control_"+session_language)%> ')"
                                            onchange="try{setCustomValidity('')}catch(e){}" required>
                                        <option value="0"><%=translation.get("undergraduate_form_label_"+session_language)%></option>
                                        <option value="1"><%=translation.get("postgraduate_form_label_"+session_language)%></option>
                                    </select>
                                </div>
                                <div class="row form-group">
                                    <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
                                        <label class="create_course_label" for="semester"> <%=translation.get("semester_label_"+session_language)+":"%> </label>
                                    </div>
                                    <select name="semester" class="sin select2-container col-lg-3 col-md-5 col-sm-5 col-xs-5" 
                                            oninvalid="setCustomValidity(' <%=translation.get("invalid_empty_control_"+session_language)%> ')"
                                            onchange="try{setCustomValidity('')}catch(e){}" required>
                                        <%for(int i=1; i<9; i++){%>
                                            <option value="<%=i%>"><%=translation.get("semester_label_"+session_language)+" "+i%></option>
                                        <%}%>
                                    </select>
                                </div>
                                <div class="row form-group">
                                    <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
                                        <label class="create_course_label" for="prerequisites_courses"> <%=translation.get("prerequisites_label_"+session_language)+" "+
                                                translation.get("courses_label_"+session_language)+":"%> </label>
                                    </div>
                                    <select name="prerequisites_courses" class="sin select2-container col-lg-6 col-md-7 col-sm-7 col-xs-12" multiple="">
                                        <%=courses_string%>
                                    </select>
                                </div>                                
                                <div class="row form-group"></div>
                                <div class="row form-group">
                                    <div class="col-lg-4 col-md-4 col-sm-4"></div>
                                    <input class="btn btn-primary btn-lg col-lg-4 col-md-4 col-sm-4 col-xs-12" type="submit" value="<%=translation.get("submit_button_label_"+session_language)%>">
                                </div>
                                <hr class="main-hr">
                            </form>  
                        </div>
                        <%if(professor_courses!=null){%>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <div class="row">
                                        <div class="col-lg-6 col-md-7 col-sm-9 col-xs-12">
                                            <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#create_objective_form">
                                                <%=translation.get("create_objective_form_title_"+session_language)%> <span style="font-size:20px;" class="glyphicon glyphicon-collapse-down"</span>
                                            </a>                           
                                        </div>
                                    </div>
                                </div>                            
                                <form id="create_objective_form" class="accordion-body collapse in" action="create_objective.jsp" method="POST">
                                    <div class="row form-group">
                                        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
                                            <label class="create_course_label" for="learning_objective_course"> <%=translation.get("course_label_"+session_language)+":"%> </label>
                                        </div>                                    
                                        <select name="learning_objective_course" class="sin select2-container col-lg-5 col-md-6 col-sm-6 col-xs-12" 
                                                oninvalid="setCustomValidity(' <%=translation.get("invalid_empty_control_"+session_language)%> ')"
                                                onchange="try{setCustomValidity('')}catch(e){}" required>
                                            <%=professor_courses_string%>
                                        </select>                                    
                                    </div>
                                    <div class="row form-group">
                                        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
                                            <label class="create_course_label" for="learning_objective_category"> <%=translation.get("category_label_"+session_language)+":"%> </label>
                                        </div>
                                        <select name="learning_objective_category" class="sin select2-container col-lg-3 col-md-3 col-sm-3 col-xs-6" 
                                                oninvalid="setCustomValidity(' <%=translation.get("invalid_empty_control_"+session_language)%> ')"
                                                onchange="try{setCustomValidity('')}catch(e){}" required>
                                            <%=learning_objective_categories_string%>
                                        </select>
                                    </div>
                                    <div class="row form-group">
                                        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
                                            <label class="create_course_label" for="learning_objective_title_gr"> <%=translation.get("create_form_greek_title_label_"+session_language)+":"%> </label>
                                        </div>
                                        <div class="col-lg-6 col-md-6 col-sm-7 col-xs-12" style="margin-left: -15px;">
                                        <textarea type="text" class="form-control" name="learning_objective_title_gr" 
                                               oninvalid="setCustomValidity(' <%=translation.get("invalid_empty_control_"+session_language)%> ')"
                                               onchange="try{setCustomValidity('')}catch(e){}" rows="2" maxlength="150" required></textarea>
                                        </div>
                                    </div>
                                    <div class="row form-group">
                                        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
                                            <label class="create_course_label" for="learning_objective_title_eng"> <%=translation.get("create_form_english_title_label_"+session_language)+":"%> </label>
                                        </div>
                                        <div class="col-lg-6 col-md-6 col-sm-7 col-xs-12" style="margin-left: -15px;">
                                        <textarea type="text" class="form-control" name="learning_objective_title_eng" 
                                               oninvalid="setCustomValidity(' <%=translation.get("invalid_empty_control_"+session_language)%> ')"
                                               onchange="try{setCustomValidity('')}catch(e){}" rows="2" maxlength="150" required></textarea>
                                        </div>
                                    </div>    
                                    <div class="row form-group">
                                        <div class="col-lg-5 col-md-5 col-sm-5 col-xs-12">
                                            <label class="create_course_label" for="prerequisites_learning_objectives"> <%=translation.get("prerequisites_form_label_"+session_language)+" "+
                                                    translation.get("learning_objectives_label_"+session_language)+":"%> </label>
                                        </div>
                                        <select name="prerequisites_learning_objectives" class="sin select2-container col-lg-6 col-md-7 col-sm-7 col-xs-12" multiple="">
                                            <%=learning_objectives_string%>
                                        </select>
                                    </div>
                                    <div class="row form-group">
                                        <div class="col-lg-4 col-md-4 col-sm-4"></div>
                                        <input class="btn btn-primary btn-lg col-lg-4 col-md-4 col-sm-4 col-xs-12" type="submit" value="<%=translation.get("submit_button_label_"+session_language)%>">
                                    </div>
                                    <hr class="main-hr">
                                </form> 
                            </div>
                        <%}%>
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
