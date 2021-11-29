<%@page import="com.webservices.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="general.FunctionsController"%>
<%@page import="java.net.URLDecoder"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%    
    request.setCharacterEncoding("UTF-8");
    String error_message = session.getAttribute("error")!=null?session.getAttribute("error").toString():null;
    String other_message = session.getAttribute("message")!=null?session.getAttribute("message").toString():null;
    String language = request.getParameter("language")!=null?request.getParameter("language"):null;
    String webServicesStringUrl,responseString,learning_objectives_string="",session_language="";
    boolean is_language_ok=false;
    HashMap<String,String> translation = null;
    ArrayList<LearningObjectivesModel> learning_objectives = null;
    UserModel user_info = null;
    String [] user_types = AuthKey.getArrayWithUserTypes();
    final String PROFESSOR = user_types[0];
    final String STUDENT= user_types[1];
    final String ADMIN= user_types[2];
    Cookie[] cookies = null;
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
        if(user_info.getUser_auth_key().getUser_type().equals(STUDENT)){
            response.sendRedirect("Student/index.jsp");
        }else if(user_info.getUser_auth_key().getUser_type().equals(PROFESSOR)){
            response.sendRedirect("Professor/index.jsp");
        }else if(user_info.getUser_auth_key().getUser_type().equals(ADMIN)){
            response.sendRedirect("Admin/index.jsp");
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
%>

<!DOCTYPE html>
<html>
    <head>
        <title><%= translation.get("head_title_"+session_language) %></title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
        <link href="CSS_Files/bootstrap/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css"/>
        <link href="CSS_Files/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="CSS_Files/pop-up-box.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="CSS_Files/main.css" type="text/css"/>
        <link href="CSS_Files/select2.min.css" rel="stylesheet" type="text/css"/>
        <link rel="shortcut icon" type="image/png" href="Images/favicon.png"/>
        <script src="JS_Files/jquery.min.js"></script>
        <script src="JS_Files/bootstrap.min.js"></script>       
        <script src="JS_Files/select2.min.js" type="text/javascript"></script>
    </head>
    <body class="main" onload="javascript:user_typeCheck();">
        <div id="popupBoxRegister">
            <div class="popupBoxWrapper">
                <div class="popupBoxContent container-fluid">
                    <div class="row">
                        <div class="col-lg-11 col-md-11 col-sm-11"></div>
                        <p><a class="col-lg-1 col-md-1 col-sm-1 col-xs-12" href="javascript:void(0)" onclick="toggle_visibility('popupBoxRegister');">
                                <span class="closeX glyphicon glyphicon-remove"></span></a></p>
                    </div>
                    <div class="row" style="padding:10px;"></div>
                    <div class="row" style="text-align: center;">
                        <form name="register" method="post" action="register.jsp">
                            <div class="form-group row">
                                <div class="col-lg-5 col-md-5 col-sm-5 col-xs-4">
                                    <label for="register_user"><%=translation.get("username_label_"+session_language)+":"%></label>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-4">
                                    <input class="form-control" type="text" name="register_user" pattern="[a-z]*[A-Z]*[0-9]*"
                                           oninvalid="setCustomValidity(' <%=translation.get("invalid_username_control_"+session_language)%> ')"
                                           onchange="try{setCustomValidity('')}catch(e){}" maxlength="30" required/>
                                </div>
                            </div>
                            <div class="form-group row">
                                <div class="col-lg-5 col-md-5 col-sm-5 col-xs-4">
                                    <label for="register_pass"><%=translation.get("password_label_"+session_language)+":"%></label>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-4">
                                    <input class="form-control" type="password" name="register_pass" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
                                           oninvalid="setCustomValidity(' <%=translation.get("invalid_password_control_"+session_language)%> ')"
                                           onchange="try{setCustomValidity('')}catch(e){}" maxlength="30" required/>
                                </div>
                            </div>
                            <div class="form-group row">
                                <div class="col-lg-5 col-md-5 col-sm-5 col-xs-4">
                                    <label for="register_name"> <%=translation.get("name_label_"+session_language)+":"%> </label>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-4">
                                    <input class="form-control" type="text" name="register_name" pattern="[^\x20]+" 
                                           oninvalid="setCustomValidity(' <%=translation.get("invalid_space_control_"+session_language)%> ')" onchange="try{setCustomValidity('')}catch(e){}"
                                           maxlength="40" required/>
                                </div>
                            </div>
                            <div class="form-group row">
                                <div class="col-lg-5 col-md-5 col-sm-5 col-xs-4">
                                    <label for="register_name"> <%=translation.get("surname_label_"+session_language)+":"%> </label>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-4">
                                    <input class="form-control" type="text" name="register_surname" pattern="[^\x20]+" 
                                           oninvalid="setCustomValidity(' <%=translation.get("invalid_space_control_"+session_language)%> ')" onchange="try{setCustomValidity('')}catch(e){}"
                                           maxlength="40" required/>
                                </div>
                            </div>
                            <div class="form-group row">
                                <div class="col-lg-5 col-md-5 col-sm-5 col-xs-4">
                                    <label for="register_name"> <%=translation.get("user_type_label_"+session_language)+":"%> </label>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-4">
                                    <p class="form-control-static" style="font-size:20px;">
                                        <%=translation.get("professor_label_"+session_language)%> <input type="radio" name="register_type" value="professor" id="professor" onclick="javascript:user_typeCheck();" checked/>
                                        <%=translation.get("student_label_"+session_language)%>  <input type="radio" name="register_type" value="student" id="student" onclick="javascript:user_typeCheck();"/>
                                    </p>
                                </div>
                            </div>
                            <div class="form-group row" id="ifProfessor">
                                <div class="col-lg-5 col-md-5 col-sm-5 col-xs-4">
                                    <label for="register_name"> <%=translation.get("grade_label_"+session_language)+":"%> </label>
                                </div>
                                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-4">
                                    <input class="form-control" type="text" id="register_grade" name="register_grade" maxlength="50" />
                                </div>
                            </div>
                            <div class="row" style="padding:20px;"></div>
                            <div class="form-group row">
                                <div class="col-lg-5 col-md-5 col-sm-5"></div>
                                <div class="col-lg-3 col-md-3 col-sm-3 col-xs-12">
                                    <input class="btn btn-primary btn-lg" type="submit" value="<%=translation.get("register_label_"+session_language)%>">
                                </div>
                            </div>
                            <div class="row" style="padding:20px;"></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        
        <nav class="navbar navbar-default">
            <div class="container-fluid my-navbar">
              <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>
                  <span class="icon-bar"></span>                        
                </button>
                  <p class="title"><%=translation.get("head_title_"+session_language)%></p>
              </div>
              <div class="collapse navbar-collapse" id="myNavbar">
                <ul class="nav navbar-nav">
                  <li class="active"><a href=""><%=translation.get("homepage_label_"+session_language)%></a></li>
                  <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#"><%=translation.get("courses_label_"+session_language)%>
                    <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                      <li><a href="courses.jsp?edu=0"><%=translation.get("undergraduate_label_"+session_language)%></a></li>
                      <li><a href="courses.jsp?edu=1"><%=translation.get("postgraduate_label_"+session_language)%></a></li>
                    </ul>
                  </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-globe"></span>
                        <span class="caret"></span></a>
                        <ul class="dropdown-menu my-dropdown-menu">
                            <li><a href="?language=gr"><img class="flag_icon" src="Images/greek_flag_icon.ico"></a></li>                            
                            <li><a href="?language=eng"><img class="flag_icon" src="Images/english_flag_icon.ico"></a></li>
                        </ul>
                    </li>
                  <li><a href="javascript:void(0)" onclick="toggle_visibility('popupBoxRegister');"><span class="glyphicon glyphicon-user"></span> <%=translation.get("register_label_"+session_language)%></a></li>                  
                </ul>
              </div>
            </div>
        </nav>
        <div class="container-fluid">
            <div class="container title-container">
                <%=translation.get("homepage_title_"+session_language)%>
            </div>
            <div class="container login-container">                
                <div class="col-lg-5 col-md-5 col-sm-5 col-xs-11 pull-right login-form">
                    <div class="row">                    
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <center><h3><span class="glyphicon glyphicon-log-in"></span> <%=translation.get("user_login_title_"+session_language)%></h3></center>
                        </div>
                    </div>
                    <%if(error_message!=null){session.removeAttribute("error");%>
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <%if(error_message.equals("login_user_fail")){%>
                                <center><div class="alert alert-info message"><%=translation.get("error_message_login_fail_"+session_language)%></div></center>
                            <%}else if(error_message.equals("login_user_invalid")){%>
                                <center><div class="alert alert-warning message"><%=translation.get("error_message_invalid_"+session_language)%></div></center>
                            <%}else if(error_message.equals("register_user_fail")){%>
                                <center><div class="alert alert-info message"><%=translation.get("error_message_register_fail_"+session_language)%></div></center>
                            <%}else if(error_message.equals("login_server_error") || error_message.equals("register_server_error")){%>
                                <center><div class="alert alert-danger message"><%=translation.get("error_message_server_"+session_language)%></div></center>
                            <%}%>
                        </div>
                    <%}%>
                    <%if(other_message!=null){session.removeAttribute("message");%>
                        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
                            <%if(other_message.equals("professor_register_done")){%>
                                <center><div class="alert alert-success message"><%=translation.get("message_register_done_"+session_language)%></div></center>
                            <%}else if(other_message.equals("auto_logout")){%>
                                <center><div class="alert alert-warning message"><%=translation.get("auto_logout_message_"+session_language)%></div></center>
                            <%}%>
                        </div>
                    <%}%>
                    <form name="login" method="post" action="login.jsp">
                        <div class="row" style="padding:10px;"></div>
                        <div class="form-group row">
                            <div class="col-lg-5 col-md-6 col-sm-11 col-xs-11">
                                <label for="login_user"> <%=translation.get("username_label_"+session_language)+":"%></label>
                            </div>
                            <div class="col-lg-6 col-md-6 col-sm-11 col-xs-11">
                                <input class="form-control" type="text" name="login_user" maxlength="20" placeholder="Username" 
                                    pattern="[a-z]*[A-Z]*[0-9]*" oninvalid="setCustomValidity(' <%=translation.get("invalid_username_control_"+session_language)%> ')"
                                    onchange="try{setCustomValidity('')}catch(e){}" required/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-lg-5 col-md-6 col-sm-11 col-xs-11">
                                <label for="login_pass"> <%=translation.get("password_label_"+session_language)+":"%> </label>
                            </div>
                            <div class="col-lg-6 col-md-6 col-sm-11 col-xs-11">
                                <input class="form-control" type="password" name="login_pass" maxlength="20" 
                                       oninvalid="setCustomValidity(' <%=translation.get("invalid_empty_control_"+session_language)%> ')" onchange="try{setCustomValidity('')}catch(e){}" required/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-lg-1 col-md-1 col-sm-1"></div>
                            <div class="col-lg-10 col-md-10 col-sm-10 col-xs-12">
                                <input class="btn btn-primary btn-lg btn-login" type="submit" value="<%=translation.get("login_label_"+session_language)%>">
                            </div>
                        </div>
                    </form>
                </div>
            </div>  
            <div class="container main-container">
                <div class="courses-search-container">
                    <div class="row">
                        <div class="courses-search-title col-lg-3 col-md-4 col-sm-5 col-xs-12">
                            <%=translation.get("search_courses_label_"+session_language)%>                           
                        </div>
                        <div class="all-courses-box col-lg-3 col-md-3 col-sm-4 col-xs-12">
                            <a class="btn btn-default" href="courses.jsp"><%=translation.get("search_all_courses_button_"+session_language)%></a>
                        </div>
                    </div>
                    <div class="row">
                        <hr class="col-lg-6 col-md-7 col-sm-9 col-xs-9 courses-search-hr">
                    </div>
                    <div class="row">
                        <div class="learning-objectives-box">
                            <div class="col-lg-3 col-md-4 col-sm-4 col-xs-12"><%=translation.get("by_learning_objectives_label_"+session_language)+":"%></div>
                            <form action="courses.jsp">
                                <select name="learning_objective_search" class="sin select2-container col-lg-6 col-md-5 col-sm-5 col-xs-11" required>
                                    <%=learning_objectives_string%>
                                </select>
                                <hr class="visible-xs learning-objective-search-hr">
                                <input class="learning-objective-search-button btn btn-default col-lg-2 col-md-2 col-sm-2 col-xs-10" type="submit" value="<%=translation.get("search_label_"+session_language)%>">
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="container bottom-container"></div>
            <div class="my-copyrights"><%="© "+translation.get("copyrights_content_"+session_language)+" | 2017"%></div>
        </div>
        <script type="text/javascript">            
            function toggle_visibility(id) {
               var e = document.getElementById(id);
               if(e.style.display === 'block')
                  e.style.display = 'none';
               else
                  e.style.display = 'block';
            }
            function user_typeCheck() {
                if (document.getElementById('professor').checked) {
                    document.getElementById('ifProfessor').style.display = 'block';
                }
                else{
                    document.getElementById('ifProfessor').style.display = 'none';
                    document.getElementById('register_grade').value = null;
                }
            }
            $(".sin").select2();
        </script>
    </body>
</html>