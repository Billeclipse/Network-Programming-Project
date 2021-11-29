package main;

import Models.CoursesModel;
import Models.FunctionsController;
import Models.UserModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements WindowListener,ActionListener{
    
    private ArrayList<CoursesModel> professor_courses;
    private UserModel professor_info;
    private Dictionary main_dictionary;
    private HashMap<String,String> translation;
    private JPanel header_panel;
    private JPanel main_panel;
    private JLabel welcome_label;
    private JLabel error_label;
    private JButton greek_language_button;
    private JButton english_language_button;
    private JButton create_course_button;
    private JButton create_objective_button;
    
    public MainFrame(){
        main_dictionary = LoginFrame.main_dictionary;
        error_label = new javax.swing.JLabel();
        translation = LoginFrame.initTranslation(main_dictionary);
        professor_info = null;
        try{ 
            professor_info = new UserModel(main_dictionary.get("professor_info").toString());
        } catch (IOException ex) {
            error_label.setText(translation.get("error_message_failed_to_login_"+main_dictionary.get("language")));
        }        
        if(professor_info!=null){
            getProfessorCourses();
            //Panels and Layouts
            header_panel = new JPanel();
            main_panel = new JPanel();
            //Labels
            welcome_label = new javax.swing.JLabel(translation.get("welcome_label_"+main_dictionary.get("language"))
                    +", "+professor_info.getName()+" "+professor_info.getSurname());
            //Buttons  
            create_course_button = new JButton(translation.get("create_course_form_title_"+main_dictionary.get("language")));
            create_course_button.addActionListener(this); 
            if(professor_courses!=null){
                create_objective_button = new JButton(translation.get("create_objective_form_title_"+main_dictionary.get("language")));
                create_objective_button.addActionListener(this);
            }            
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
            main_panel.add(create_course_button);
            if(professor_courses!=null){
                main_panel.add(create_objective_button);
            }
            if(professor_info!=null)header_panel.add(welcome_label);
            this.addWindowListener(this);
            this.setLayout(new BorderLayout(1,2));
            this.addWindowListener(this);
            this.add(header_panel,BorderLayout.PAGE_START);
            this.add(main_panel,BorderLayout.CENTER);            
        }else{
            goBack();
        }        
    }
        
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        //System.out.println(cmd);  //Debug
        if(cmd.equals(translation.get("create_course_form_title_"+main_dictionary.get("language")))){
            CreateNewCourseFrame create_new_course_frame = new CreateNewCourseFrame();
            create_new_course_frame.setTitle(translation.get("create_course_form_title_"+main_dictionary.get("language")));
            create_new_course_frame.setSize(new Dimension(600,600));
            create_new_course_frame.setMinimumSize(new Dimension(600,600));
            create_new_course_frame.setVisible(true);
            create_new_course_frame.setLocationRelativeTo(null);
            this.dispose();
        }else if(cmd.equals(translation.get("create_objective_form_title_"+main_dictionary.get("language")))){
            CreateNewObjectiveFrame create_new_objective_frame = new CreateNewObjectiveFrame();
            create_new_objective_frame.setTitle(translation.get("create_objective_form_title_"+main_dictionary.get("language")));
            create_new_objective_frame.setSize(new Dimension(650,600));
            create_new_objective_frame.setMinimumSize(new Dimension(650,600));
            create_new_objective_frame.setVisible(true);
            create_new_objective_frame.setLocationRelativeTo(null);
            this.dispose();
        }else if(cmd.equals("gr")){
            changeTranslation("gr");
        }else if(cmd.equals("eng")){
            changeTranslation("eng");
        }
    }
    
    @Override
    public void windowClosing(WindowEvent e) {
        logout();
        System.exit(0);
    }

    private void logout(){
        if(professor_info!=null){
            String webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Controller/Logout?auth_key="
                    +professor_info.getUser_auth_key().getAuth_key(); 
            FunctionsController.getResponse(webServicesStringUrl,"DELETE");
        }        
    }
    
    private void goBack() {
        LoginFrame login_frame = new LoginFrame();
        login_frame.setTitle("Login");
        login_frame.setSize(new Dimension(650,300));
        login_frame.setMinimumSize(new Dimension(650,300));
        login_frame.setVisible(true);
        login_frame.setLocationRelativeTo(null);
        this.dispose();
    }
    
    private void changeTranslation(String language){        
        main_dictionary.put("language", language);     
        welcome_label.setText(translation.get("welcome_label_"+main_dictionary.get("language")) +", "+professor_info.getName()+" "+professor_info.getSurname());
        create_course_button.setText(translation.get("create_course_form_title_"+main_dictionary.get("language")));
        if(professor_courses!=null) create_objective_button.setText(translation.get("create_objective_form_title_"+main_dictionary.get("language")));
    }
    
    private void getProfessorCourses(){
        professor_courses = null;
        String webServicesStringUrl,responseString;
        if(professor_info!=null){
            webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/getProfessorCourses?professor_id="
                +String.valueOf(professor_info.getUser_auth_key().getId());
            responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
            if(responseString!=null && !responseString.contains("Error")){
                //Courses found.
                professor_courses = FunctionsController.createCoursesModelArrayListFromJSON(responseString);
            }
        }
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
