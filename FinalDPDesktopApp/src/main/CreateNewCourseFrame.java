package main;

import Models.CoursesModel;
import Models.FunctionsController;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

public class CreateNewCourseFrame extends JFrame implements WindowListener,ActionListener{
    
    private ArrayList<CoursesModel> courses;
    private UserModel professor_info;
    private Dictionary main_dictionary;
    private HashMap<String,String> translation;
    private JTextField course_title_gr_field;
    private JTextField course_title_eng_field;
    private JList education_list;
    private JList semester_list;
    private JList prerequisites_courses_list;
    private JScrollPane semester_pane;
    private JScrollPane prerequisites_courses_pane;
    private JPanel create_course_panel;
    private JPanel header_panel;
    private JLabel course_title_gr_label;
    private JLabel course_title_eng_label;
    private JLabel education_level_label;
    private JLabel semester_label;
    private JLabel prerequisites_courses_label;
    private JLabel error_label;
    private JButton create_new_course_button;
    private JButton greek_language_button;
    private JButton english_language_button;
    private JButton back_button;

    public CreateNewCourseFrame() {
        main_dictionary = LoginFrame.main_dictionary;
        error_label = new javax.swing.JLabel();
        translation = LoginFrame.initTranslation(main_dictionary);
        professor_info = null;
        try{ 
            professor_info = new UserModel(main_dictionary.get("professor_info").toString());
        } catch (IOException ex) {
            error_label.setText(translation.get("error_message_failed_to_login_"+main_dictionary.get("language")));
        }
        //Panels and Layouts
        header_panel = new JPanel();
        create_course_panel = new JPanel();
        GroupLayout create_course_layout = new GroupLayout(create_course_panel);
        create_course_panel.setLayout(create_course_layout);  
        create_course_layout.setAutoCreateGaps(true);
        create_course_layout.setAutoCreateContainerGaps(true);
        //Dimensions Objects
        Dimension txt_field_dimension = new Dimension(150,25);
        //Border
        Border blackLine;
        blackLine = BorderFactory.createLineBorder(Color.black);
        //Fields and Lists
        course_title_gr_field = new javax.swing.JTextField();
        course_title_gr_field.setPreferredSize(txt_field_dimension);
        course_title_gr_field.setMinimumSize(txt_field_dimension);        
        course_title_gr_field.setBorder(blackLine);
        course_title_eng_field = new javax.swing.JTextField();
        course_title_eng_field.setPreferredSize(txt_field_dimension);
        course_title_eng_field.setMinimumSize(txt_field_dimension);        
        course_title_eng_field.setBorder(blackLine);
        String[] education_tmp_list = {translation.get("undergraduate_form_label_"+main_dictionary.get("language")),
            translation.get("postgraduate_form_label_"+main_dictionary.get("language"))};
        education_list = new JList(education_tmp_list);
        DefaultListSelectionModel m = new DefaultListSelectionModel();
        m.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m.setLeadAnchorNotificationEnabled(false);
        education_list.setSelectionModel(m);        
        String[] semester_tmp_list = new String[8];
        for(int i=0; i<8; i++){
            semester_tmp_list[i] = translation.get("semester_label_"+main_dictionary.get("language")) + " " + String.valueOf(i+1);
        }
        semester_list = new JList(semester_tmp_list); 
        DefaultListSelectionModel m2 = new DefaultListSelectionModel();
        m2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m2.setLeadAnchorNotificationEnabled(false);
        semester_list.setSelectionModel(m2);
        semester_pane = new JScrollPane(semester_list);        
        prerequisites_courses_list = new JList(getPrerequisitesCourses()); 
        DefaultListSelectionModel m3 = new DefaultListSelectionModel();
        m3.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        m3.setLeadAnchorNotificationEnabled(false);
        prerequisites_courses_list.setSelectionModel(m3);
        prerequisites_courses_pane = new JScrollPane(prerequisites_courses_list);
        //Labels 
        course_title_gr_label = new javax.swing.JLabel(translation.get("create_form_greek_title_label_"+main_dictionary.get("language"))+":");        
        course_title_eng_label = new javax.swing.JLabel(translation.get("create_form_english_title_label_"+main_dictionary.get("language"))+":");  
        education_level_label = new javax.swing.JLabel(translation.get("education_level_title_"+main_dictionary.get("language"))+":"); 
        semester_label = new javax.swing.JLabel(translation.get("semester_label_"+main_dictionary.get("language"))+":"); 
        prerequisites_courses_label = new javax.swing.JLabel(translation.get("prerequisites_label_"+main_dictionary.get("language"))+" "
                +translation.get("courses_label_"+main_dictionary.get("language"))+":");
        //Buttons
        back_button = new javax.swing.JButton(translation.get("previous_button_label_"+main_dictionary.get("language")));
        back_button.addActionListener(this);
        create_new_course_button = new javax.swing.JButton(translation.get("submit_button_label_"+main_dictionary.get("language")));
        create_new_course_button.addActionListener(this);
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
        create_course_layout.setHorizontalGroup(
               create_course_layout.createSequentialGroup()
                   .addGroup(create_course_layout.createParallelGroup(GroupLayout.Alignment.LEADING)                                            
                       .addComponent(course_title_gr_label) 
                       .addComponent(course_title_eng_label)
                       .addComponent(education_level_label)
                       .addComponent(semester_label)
                       .addComponent(prerequisites_courses_label)
                       .addComponent(back_button)
                   )
                   .addGroup(create_course_layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                       .addComponent(error_label)
                       .addComponent(course_title_gr_field)
                       .addComponent(course_title_eng_field)
                       .addComponent(education_list)
                       .addComponent(semester_pane)
                       .addComponent(prerequisites_courses_pane)
                       .addComponent(create_new_course_button)
                   )                
           );
           create_course_layout.setVerticalGroup(
                create_course_layout.createSequentialGroup() 
                    .addComponent(error_label)
                    .addGroup(create_course_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)   
                        .addComponent(course_title_gr_label)                    
                        .addComponent(course_title_gr_field)
                    )
                    .addGroup(create_course_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(course_title_eng_label)
                        .addComponent(course_title_eng_field)                    
                    )
                    .addGroup(create_course_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(education_level_label)
                        .addComponent(education_list)                    
                    )
                    .addGroup(create_course_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(semester_label)
                        .addComponent(semester_pane)                    
                    )
                    .addGroup(create_course_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(prerequisites_courses_label)
                        .addComponent(prerequisites_courses_pane)                    
                    )           
                    .addGroup(create_course_layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(back_button)
                        .addComponent(create_new_course_button)
                    )
        );
        this.addWindowListener(this);
        this.getRootPane().setDefaultButton(create_new_course_button);
        this.setLayout(new BorderLayout(1,2));
        this.addWindowListener(this);
        this.add(header_panel,BorderLayout.PAGE_START);
        this.add(create_course_panel,BorderLayout.CENTER);        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        //System.out.println(cmd);  //Debug
        if(cmd.equals(translation.get("submit_button_label_"+main_dictionary.get("language")))){
            createNewCourse();
        }else if(cmd.equals("gr")){
            changeTranslation("gr");
        }else if(cmd.equals("eng")){
            changeTranslation("eng");
        }else if(cmd.equals(translation.get("previous_button_label_"+main_dictionary.get("language")))){
            goBack();
        }
    }
    
    private void createNewCourse(){           
        String webServicesStringUrl,responseString;
        String course_title_gr=!(course_title_gr_field.getText().equals(""))?course_title_gr_field.getText():null;
        String course_title_eng=!(course_title_eng_field.getText().equals(""))?course_title_eng_field.getText():null;
        if(course_title_eng!=null && Character.isLowerCase(course_title_eng.charAt(0))){
            error_label.setText(translation.get("error_message_course_title_"+main_dictionary.get("language")));
            return;
        }
        String education_level=education_list.getSelectedValue()!=null?education_list.getSelectedValue().toString():null;
        int education_level_id;        
        int semester=semester_list.getSelectedValue()!=null?Integer.valueOf(semester_list.getSelectedValue().toString().trim().split(" ")[1]):-1;;
        List<String> prerequisites_courses_string=prerequisites_courses_list!=null?prerequisites_courses_list.getSelectedValuesList():null;
        if(course_title_gr!=null && course_title_eng!=null && education_level!=null && semester!=-1){
            String[] prerequisites_courses_tmp = null;
            if(education_level.equals(translation.get("undergraduate_form_label_"+main_dictionary.get("language")))){
                education_level_id=0;
            }else{
                education_level_id=1;
            }            
            if(prerequisites_courses_string.size()>0){                
                prerequisites_courses_tmp = new String[prerequisites_courses_string.size()];
                for(int i=0; i<prerequisites_courses_string.size(); i++){
                   for(CoursesModel course : courses){ 
                        if(main_dictionary.get("language").equals("gr")){
                            if(course.getTitle_gr().equals(prerequisites_courses_string.get(i))){
                                prerequisites_courses_tmp[i] = String.valueOf(course.getId());
                            }
                        }else if(main_dictionary.get("language").equals("eng")){
                            if(course.getTitle_eng().equals(prerequisites_courses_string.get(i))){
                                prerequisites_courses_tmp[i] = String.valueOf(course.getId());
                            }
                        }
                    }
                }                            
            }
            List<String> prerequisites_courses = prerequisites_courses_tmp!=null?Arrays.asList(prerequisites_courses_tmp):null;
            if(professor_info!=null){                
                webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/createCourse?course_title_gr="
                    +course_title_gr+"&course_title_eng="+course_title_eng+"&education_level="+education_level_id+"&course_semester="
                    +semester+"&prerequisites_courses="+prerequisites_courses+"&professor_id="+String.valueOf(professor_info.getUser_auth_key().getId());
                responseString = FunctionsController.getResponse(webServicesStringUrl,"POST");
                if(responseString!=null && responseString.contains("Error")){            
                    //Found an Error.                
                    error_label.setText(translation.get("error_message_create_course_fail_"+main_dictionary.get("language")));
                }else{
                    clear();
                    error_label.setText(translation.get("message_create_course_success_"+main_dictionary.get("language")));
                }    
            }else{
                error_label.setText(translation.get("error_message_failed_to_login_"+main_dictionary.get("language")));
            }                 
        }else{
            error_label.setText(translation.get("invalid_empty_fields_"+main_dictionary.get("language")));
        }        
    }
    
    private String[] getPrerequisitesCourses(){
        courses = null;
        String webServicesStringUrl,responseString;        
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Courses/getAllCourses";
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //Courses found.
            courses = FunctionsController.createCoursesModelArrayListFromJSON(responseString);
            String[] temp = new String[courses.size()];
            for(int i=0; i<courses.size(); i++){
                if(main_dictionary.get("language").equals("gr")){
                    temp[i] = courses.get(i).getTitle_gr();
                }else if(main_dictionary.get("language").equals("eng")){
                    temp[i] = courses.get(i).getTitle_eng();
                }
            }
            return temp;
        }
        return null;
    }
    
    private void changeTranslation(String language){        
        main_dictionary.put("language", language);        
        CreateNewCourseFrame create_new_course_frame = new CreateNewCourseFrame();
        create_new_course_frame.setTitle(translation.get("create_course_form_title_"+main_dictionary.get("language")));
        create_new_course_frame.setSize(new Dimension(600,600));
        create_new_course_frame.setMinimumSize(new Dimension(600,600));
        create_new_course_frame.setVisible(true);
        create_new_course_frame.setLocationRelativeTo(null);
        this.dispose();
    }
    
    private void clear() {
        if(!course_title_gr_field.getText().isEmpty())   course_title_gr_field.setText("");
        if(!course_title_eng_field.getText().isEmpty())   course_title_eng_field.setText("");
        if(education_list.getSelectedValue()!=null)     education_list.clearSelection();
        if(semester_list.getSelectedValue()!=null)     semester_list.clearSelection();
        if(prerequisites_courses_list!=null)    prerequisites_courses_list.clearSelection();
    }
    
    private void logout(){
        if(professor_info!=null){
            String webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Controller/Logout?auth_key="
                    +professor_info.getUser_auth_key().getAuth_key(); 
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
