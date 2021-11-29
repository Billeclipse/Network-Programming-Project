package main;

import Models.CoursesModel;
import Models.FunctionsController;
import Models.LearningObjectiveCategoriesModel;
import Models.LearningObjectivesModel;
import Models.UserModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

public class CreateNewObjectiveFrame extends JFrame implements WindowListener,ActionListener{

    private ArrayList<CoursesModel> professor_courses;
    private ArrayList<LearningObjectiveCategoriesModel> objective_categories;
    private ArrayList<LearningObjectivesModel> prerequisites_objectives;
    private UserModel professor_info;
    private Dictionary main_dictionary;
    private HashMap<String,String> translation;
    private JTextArea objective_title_gr_area;
    private JTextArea objective_title_eng_area;
    private JList professor_course_list;
    private JList category_list;
    private JList prerequisites_objective_list;
    private JScrollPane professor_course_pane;
    private JScrollPane category_pane;
    private JScrollPane prerequisites_objective_pane;
    private JScrollPane objective_title_gr_pane;
    private JScrollPane objective_title_eng_pane;
    private JPanel create_objective_panel;
    private JPanel header_panel;
    private JLabel error_label;
    private JLabel professor_course_label;
    private JLabel category_label;
    private JLabel prerequisites_objective_label;
    private JLabel objective_title_gr_label;
    private JLabel objective_title_eng_label;
    private JButton create_new_objective_button;
    private JButton greek_language_button;
    private JButton english_language_button;
    private JButton back_button;

    public CreateNewObjectiveFrame() {
        main_dictionary = LoginFrame.main_dictionary;
        error_label = new javax.swing.JLabel();
        translation = LoginFrame.initTranslation(main_dictionary);
        professor_info = null;
        try{ 
            professor_info = new UserModel(main_dictionary.get("professor_info").toString());
        } catch (IOException ex) {
            error_label.setText(translation.get("error_message_failed_to_login_"+main_dictionary.get("language")));
        }        
        String [] professor_courses_string = getProfessorCourses();
        //Panels and Layouts
        header_panel = new JPanel();
        GroupLayout create_objective_layout = null;
        create_objective_panel = new JPanel();
        create_objective_layout = new GroupLayout(create_objective_panel);
        create_objective_panel.setLayout(create_objective_layout);  
        create_objective_layout.setAutoCreateGaps(true);
        create_objective_layout.setAutoCreateContainerGaps(true);
        //Border
        Border blackLine;
        blackLine = BorderFactory.createLineBorder(Color.black);
        //Fields
        objective_title_gr_area = new javax.swing.JTextArea();
        objective_title_gr_area.setRows(6);
        objective_title_gr_pane = new JScrollPane(objective_title_gr_area);
        objective_title_eng_area = new javax.swing.JTextArea();
        objective_title_eng_area.setRows(6);
        objective_title_eng_pane = new JScrollPane(objective_title_eng_area);
        professor_course_list = new JList(professor_courses_string);
        DefaultListSelectionModel m4 = new DefaultListSelectionModel();
        m4.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m4.setLeadAnchorNotificationEnabled(false);
        professor_course_list.setSelectionModel(m4);
        professor_course_pane = new JScrollPane(professor_course_list);        
        category_list = new JList(getObjectivesCategories()); 
        DefaultListSelectionModel m5 = new DefaultListSelectionModel();
        m5.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m5.setLeadAnchorNotificationEnabled(false);
        category_list.setSelectionModel(m5);
        category_pane = new JScrollPane(category_list);        
        prerequisites_objective_list = new JList(getPrerequisitesObjectives()); 
        DefaultListSelectionModel m6 = new DefaultListSelectionModel();
        m6.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        m6.setLeadAnchorNotificationEnabled(false);
        prerequisites_objective_list.setSelectionModel(m6);
        prerequisites_objective_pane = new JScrollPane(prerequisites_objective_list);
        //Labels
        professor_course_label = new javax.swing.JLabel(translation.get("course_label_"+main_dictionary.get("language")));
        category_label = new javax.swing.JLabel(translation.get("category_label_"+main_dictionary.get("language")));
        prerequisites_objective_label = new javax.swing.JLabel(translation.get("prerequisites_label_"+main_dictionary.get("language"))
                +" "+translation.get("learning_objectives_label_"+main_dictionary.get("language")));
        objective_title_gr_label = new javax.swing.JLabel(translation.get("create_form_greek_title_label_"+main_dictionary.get("language")));
        objective_title_eng_label = new javax.swing.JLabel(translation.get("create_form_english_title_label_"+main_dictionary.get("language")));
        //Buttons
        back_button = new javax.swing.JButton(translation.get("previous_button_label_"+main_dictionary.get("language")));
        back_button.addActionListener(this);
        create_new_objective_button = new javax.swing.JButton(translation.get("submit_button_label_"+main_dictionary.get("language")));
        create_new_objective_button.addActionListener(this);           
        greek_language_button = new JButton(new ImageIcon("src/images/greek_flag.png"));
        greek_language_button.setActionCommand("gr");
        greek_language_button.setBorder(BorderFactory.createEmptyBorder());
        greek_language_button.setContentAreaFilled(false);
        greek_language_button.addActionListener(this);        
        english_language_button = new JButton(new ImageIcon("src/images/english_flag.png"));
        english_language_button.setActionCommand("eng");
        english_language_button.setBorder(BorderFactory.createEmptyBorder());
        english_language_button.setContentAreaFilled(false);
        english_language_button.addActionListener(this);        
        //Add order on Panels        
        header_panel.add(greek_language_button);
        header_panel.add(english_language_button);
        create_objective_layout.setHorizontalGroup(
            create_objective_layout.createSequentialGroup()                                     
                .addGroup(create_objective_layout.createParallelGroup(GroupLayout.Alignment.LEADING)                                            
                    .addComponent(professor_course_label) 
                    .addComponent(category_label)
                    .addComponent(objective_title_gr_label)
                    .addComponent(objective_title_eng_label)
                    .addComponent(prerequisites_objective_label)   
                    .addComponent(back_button)
                )
                .addGroup(create_objective_layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(error_label)
                    .addComponent(professor_course_pane)
                    .addComponent(category_pane)
                    .addComponent(objective_title_gr_pane)
                    .addComponent(objective_title_eng_pane)
                    .addComponent(prerequisites_objective_pane)
                    .addComponent(create_new_objective_button)
                )                   
        );
        create_objective_layout.setVerticalGroup(
            create_objective_layout.createSequentialGroup() 
                .addComponent(error_label)
                .addGroup(create_objective_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)   
                    .addComponent(professor_course_label)                    
                    .addComponent(professor_course_pane)
                )
                .addGroup(create_objective_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(category_label)
                    .addComponent(category_pane)                    
                )
                .addGroup(create_objective_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(objective_title_gr_label)
                    .addComponent(objective_title_gr_pane)                    
                )
                .addGroup(create_objective_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(objective_title_eng_label)
                    .addComponent(objective_title_eng_pane)                    
                )
                .addGroup(create_objective_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(prerequisites_objective_label)
                    .addComponent(prerequisites_objective_pane)                    
                )   
                .addGroup(create_objective_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(back_button)
                    .addComponent(create_new_objective_button)
                )
        );
        this.addWindowListener(this);
        this.getRootPane().setDefaultButton(create_new_objective_button);
        this.setLayout(new BorderLayout(1,2));
        this.addWindowListener(this);
        this.add(header_panel,BorderLayout.PAGE_START);
        this.add(create_objective_panel,BorderLayout.CENTER);        
    }    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        //System.out.println(cmd);  //Debug
        if(cmd.equals(translation.get("submit_button_label_"+main_dictionary.get("language")))){
            createNewObjective();
        }else if(cmd.equals("gr")){
            changeTranslation("gr");
        }else if(cmd.equals("eng")){
            changeTranslation("eng");
        }else if(cmd.equals(translation.get("previous_button_label_"+main_dictionary.get("language")))){
            goBack();
        }
    }    
    
    private void createNewObjective(){
        String webServicesStringUrl,responseString,learning_objective_code=null;
        String objective_title_gr=!(objective_title_gr_area.getText().equals(""))?objective_title_gr_area.getText():null;
        String objective_title_eng=!(objective_title_eng_area.getText().equals(""))?objective_title_eng_area.getText():null;
        String professor_course=professor_course_list.getSelectedValue()!=null?professor_course_list.getSelectedValue().toString():null;
        String objective_category=category_list.getSelectedValue()!=null?category_list.getSelectedValue().toString():null;  
        List<String> prerequisites_objectives_string=prerequisites_objective_list!=null?prerequisites_objective_list.getSelectedValuesList():null;
        if(objective_title_gr!=null && objective_title_eng!=null && professor_course!=null && objective_category!=null){
            int professor_course_id=-1,objective_category_id=-1;
            for(CoursesModel course : professor_courses){ 
                if(main_dictionary.get("language").equals("gr")){
                    if(course.getTitle_gr().equals(professor_course)){
                        professor_course_id = course.getId();
                        professor_course = course.getTitle_eng();
                        break;
                    }else if(main_dictionary.get("language").equals("eng")){
                        professor_course_id = course.getId();
                        break;
                    }
                }
            }
            String[] course_title_parts = professor_course.split(" ");
            String course_title_anagram="";
            for(int i=0; i<course_title_parts.length; i++){
                if(Character.isUpperCase(course_title_parts[i].charAt(0))){
                    course_title_anagram += course_title_parts[i].charAt(0);
                }
            }
            learning_objective_code = "CS."+course_title_anagram+".1";
            for(LearningObjectiveCategoriesModel category : objective_categories){ 
                if(main_dictionary.get("language").equals("gr")){
                    if(category.getTitle_gr().equals(objective_category)){
                        objective_category_id = category.getId();
                        break;
                    }
                }else if(main_dictionary.get("language").equals("eng")){
                    if(category.getTitle_eng().equals(objective_category)){
                        objective_category_id = category.getId();
                        break;
                    }
                }
            }
            String[] prerequisites_objectives_tmp = null;        
            if(prerequisites_objectives_string.size()>0){                
                prerequisites_objectives_tmp = new String[prerequisites_objectives_string.size()];
                for(int i=0; i<prerequisites_objectives_string.size(); i++){
                   for(LearningObjectivesModel objective : prerequisites_objectives){ 
                        if(main_dictionary.get("language").equals("gr")){
                            if(objective.getLearning_objective_title_gr().equals(prerequisites_objectives_string.get(i))){
                                prerequisites_objectives_tmp[i] = String.valueOf(objective.getLearning_objective_code());
                            }
                        }else if(main_dictionary.get("language").equals("eng")){
                            if(objective.getLearning_objective_title_eng().equals(prerequisites_objectives_string.get(i))){
                                prerequisites_objectives_tmp[i] = String.valueOf(objective.getLearning_objective_code());
                            }
                        }
                    }
                }                            
            }
            List<String> prerequisites_objectives = prerequisites_objectives_tmp!=null?Arrays.asList(prerequisites_objectives_tmp):null;
            if(learning_objective_code!=null && professor_course_id!=-1 && objective_category_id!=-1){                
                webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/LearningObjectives/createLearningObjective?learning_objective_code="
                    +learning_objective_code+"&learning_objective_title_gr="+objective_title_gr+"&learning_objective_title_eng="+objective_title_eng
                    +"&learning_objective_category="+objective_category_id+"&learning_objective_course="+professor_course_id
                    +"&prerequisites_learning_objectives="+prerequisites_objectives;
                responseString = FunctionsController.getResponse(webServicesStringUrl,"POST");
                if(responseString!=null && responseString.contains("Error")){            
                    //Found an Error.                
                    error_label.setText(translation.get("error_message_create_objective_fail_"+main_dictionary.get("language")));
                }else{
                    clear();
                    error_label.setText(translation.get("message_create_objective_success_"+main_dictionary.get("language")));
                }    
            }else{
                error_label.setText(translation.get("error_message_create_objective_fail_"+main_dictionary.get("language")));
            }
        }else{
            error_label.setText(translation.get("invalid_empty_fields_"+main_dictionary.get("language")));
        }
    }
    
    private void clear() {
        if(!objective_title_gr_area.getText().isEmpty())   objective_title_gr_area.setText("");
        if(!objective_title_eng_area.getText().isEmpty())   objective_title_eng_area.setText("");
        if(professor_course_list.getSelectedValue()!=null)     professor_course_list.clearSelection();
        if(category_list.getSelectedValue()!=null)     category_list.clearSelection();
        if(prerequisites_objective_list!=null)    prerequisites_objective_list.clearSelection();
    }
           
    private String[] getObjectivesCategories(){
        objective_categories = null;
        String webServicesStringUrl,responseString;
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/LearningObjectives/getAllLearningObjectiveCategories";
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //Courses found.
            objective_categories = FunctionsController.createLearningObjectiveCategoriesModelArrayListFromJSON(responseString);
            String[] temp = new String[objective_categories.size()];
            for(int i=0; i<objective_categories.size(); i++){
                if(main_dictionary.get("language").equals("gr")){
                    temp[i] = objective_categories.get(i).getTitle_gr();
                }else if(main_dictionary.get("language").equals("eng")){
                    temp[i] = objective_categories.get(i).getTitle_eng();
                }
            }
            return temp;
        }    
        return null;
    }
    
    private String[] getPrerequisitesObjectives(){
        prerequisites_objectives = null;
        String webServicesStringUrl,responseString;
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/LearningObjectives/getAllLearningObjectives";
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //Courses found.
            prerequisites_objectives = FunctionsController.createLearningObjectivesModelArrayListFromJSON(responseString);
            String[] temp = new String[prerequisites_objectives.size()];
            for(int i=0; i<prerequisites_objectives.size(); i++){
                if(main_dictionary.get("language").equals("gr")){
                    temp[i] = prerequisites_objectives.get(i).getLearning_objective_title_gr();
                }else if(main_dictionary.get("language").equals("eng")){
                    temp[i] = prerequisites_objectives.get(i).getLearning_objective_title_eng();
                }
            }
            return temp;
        }    
        return null;
    }
    
    private String[] getProfessorCourses(){
        professor_courses = null;
        String webServicesStringUrl,responseString;
        if(professor_info!=null){
            webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/getProfessorCourses?professor_id="
                +String.valueOf(professor_info.getUser_auth_key().getId());
            responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
            if(responseString!=null && !responseString.contains("Error")){
                //Courses found.
                professor_courses = FunctionsController.createCoursesModelArrayListFromJSON(responseString);
                String[] temp = new String[professor_courses.size()];
                for(int i=0; i<professor_courses.size(); i++){
                    if(main_dictionary.get("language").equals("gr")){
                        temp[i] = professor_courses.get(i).getTitle_gr();
                    }else if(main_dictionary.get("language").equals("eng")){
                        temp[i] = professor_courses.get(i).getTitle_eng();
                    }
                }
                return temp;
            }
        }        
        return null;
    }
    
    private void changeTranslation(String language){        
        main_dictionary.put("language", language);        
        CreateNewObjectiveFrame create_new_objective_frame = new CreateNewObjectiveFrame();
        create_new_objective_frame.setTitle(translation.get("create_objective_form_title_"+main_dictionary.get("language")));
        create_new_objective_frame.setSize(new Dimension(650,600));
        create_new_objective_frame.setMinimumSize(new Dimension(650,600));
        create_new_objective_frame.setVisible(true);
        create_new_objective_frame.setLocationRelativeTo(null);
        this.dispose();
    }
    
    private void logout(){
        if(professor_info!=null){
            String webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Controller/Logout?auth_key="+professor_info.getUser_auth_key().getAuth_key(); 
            FunctionsController.getResponse(webServicesStringUrl,"DELETE");
        }        
    }
    
    private void goBack() {
        MainFrame main_frame = new MainFrame();
        main_frame.setTitle("Main");
        main_frame.setSize(new Dimension(400,200));
        main_frame.setMinimumSize(new Dimension(400,200));
        main_frame.setVisible(true);
        main_frame.setLocationRelativeTo(null);
        this.dispose();
    }
    
    @Override
    public void windowClosing(WindowEvent e) {
        logout();
        System.exit(0);
    }
    
    @Override
    public void windowActivated(WindowEvent e) {              
    }
        
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }
    
    @Override
    public void windowDeactivated(WindowEvent e) {
    }    
}
