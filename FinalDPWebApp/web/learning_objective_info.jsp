<%@page import="com.webservices.TranslationModel"%>
<%@page import="general.FunctionsController"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.webservices.LearningObjectivesModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.webservices.UserModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    Cookie[] cookies = null;
    UserModel user_info = null;
    String learning_objective_code = request.getParameter("code")!=null?request.getParameter("code"):null;
    String language = request.getParameter("language")!=null?request.getParameter("language"):null;
    if(learning_objective_code==null) response.sendRedirect("index.jsp");
    String title = "",webServicesStringUrl,responseString,learning_objective_category_title="",learning_objective_course_title="",session_language;
    boolean is_language_ok=false;
    HashMap<String,String> translation = null;
    ArrayList<LearningObjectivesModel> prerequisites_learning_objectives = null;
    LearningObjectivesModel learning_objective = null;
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
    webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/LearningObjectives/getLearningObjective?code="+learning_objective_code;
    responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
    if(responseString!=null && !responseString.contains("Error")){
        //Course found.        
        learning_objective = FunctionsController.createLearningObjectivesModelFromJSON(responseString);        
        if(session_language.equals("gr")){
            title = learning_objective.getLearning_objective_title_gr();
            learning_objective_category_title = learning_objective.getLearning_objective_category_title_gr();
            learning_objective_course_title = learning_objective.getLearning_objective_course_title_gr();
        }else if(session_language.equals("eng")){
            title = learning_objective.getLearning_objective_title_eng();
            learning_objective_category_title = learning_objective.getLearning_objective_category_title_eng();
            learning_objective_course_title = learning_objective.getLearning_objective_course_title_eng();
        }
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/LearningObjectives/getPrerequisitesLearningObjectives?code="+learning_objective_code;
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //LearningObjectives found.
            prerequisites_learning_objectives = FunctionsController.createLearningObjectivesModelArrayListFromJSON(responseString);
            for(int i=0; i<prerequisites_learning_objectives.size(); i++){
                if(session_language.equals("gr")){
                    
                }else if(session_language.equals("eng")){
                    
                }
            }
        }
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
                                <li><a href="?code=<%=learning_objective_code%>&language=gr"><img class="flag_icon" src="Images/greek_flag_icon.ico"></a></li>                            
                                <li><a href="?code=<%=learning_objective_code%>&language=eng"><img class="flag_icon" src="Images/english_flag_icon.ico"></a></li>
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
                <%if(learning_objective!=null){%>
                    <table>
                        <tr>
                            <th><%=translation.get("password_label_"+session_language)+":"%></th><td><%= learning_objective.getLearning_objective_code() %></td>
                        </tr>
                        <tr>
                            <th><%=translation.get("category_label_"+session_language)+":"%></th><td><%= learning_objective_category_title %></td>                            
                        </tr>
                        <tr>
                            <th><%=translation.get("course_label_"+session_language)+":"%></th><td><%= learning_objective_course_title %></td>
                        </tr>                        
                        <%if(prerequisites_learning_objectives!=null && !prerequisites_learning_objectives.isEmpty()){
                        %>                            
                            <th><%=translation.get("prerequisites_form_label_"+session_language)+" "+translation.get("learning_objectives_label_"+session_language)%></th>
                        <%
                            String learning_objectives_title="",learning_objectives_code="",learning_objectives_category_title="",learning_objectives_course_title="";
                            for(int i=0; i<prerequisites_learning_objectives.size(); i++){
                                learning_objectives_code=prerequisites_learning_objectives.get(i).getLearning_objective_code();
                                if(session_language.equals("gr")){
                                    learning_objectives_title=prerequisites_learning_objectives.get(i).getLearning_objective_title_gr();                                    
                                    learning_objectives_category_title=prerequisites_learning_objectives.get(i).getLearning_objective_category_title_gr();
                                    learning_objectives_course_title=prerequisites_learning_objectives.get(i).getLearning_objective_course_title_gr();
                                }else if(session_language.equals("eng")){
                                    learning_objectives_title=prerequisites_learning_objectives.get(i).getLearning_objective_title_eng();
                                    learning_objectives_category_title=prerequisites_learning_objectives.get(i).getLearning_objective_category_title_eng();
                                    learning_objectives_course_title=prerequisites_learning_objectives.get(i).getLearning_objective_course_title_eng();
                                }                            
                            %>
                            <table>
                                <tr>
                                    <td>• <%= learning_objectives_title %></td>
                                </tr></table>
                                <table class="learning-objective-table"> 
                                    <tr>
                                        <th><%=translation.get("password_label_"+session_language)+":"%></th><td><%= learning_objectives_code %></td>
                                    </tr>                                    
                                    <tr>
                                        <th><%=translation.get("category_label_"+session_language)+":"%></th><td><%= learning_objectives_category_title %></td>
                                    </tr>
                                    <tr>
                                        <th><%=translation.get("course_label_"+session_language)+":"%></th><td><%= learning_objectives_course_title %></td>
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
