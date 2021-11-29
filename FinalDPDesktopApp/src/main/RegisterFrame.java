package main;

import Models.FunctionsController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class RegisterFrame extends JFrame implements WindowListener,ActionListener{

    private Dictionary main_dictionary;
    private HashMap<String,String> translation;
    private JTextField username_field;
    private JPasswordField password_field;
    private JTextField name_field;
    private JTextField surname_field;
    private JTextField grade_field;
    private JPanel register_panel;
    private JPanel header_panel;
    private JLabel username_label;
    private JLabel password_label;
    private JLabel name_label;
    private JLabel surname_label;
    private JLabel grade_label;
    private JLabel error_label;
    private JButton register_button;
    private JButton back_button;
    private JButton greek_language_button;
    private JButton english_language_button;
    
    public RegisterFrame() {
        main_dictionary = LoginFrame.main_dictionary;
        error_label = new javax.swing.JLabel();
        translation = LoginFrame.initTranslation(main_dictionary);
        //Panels and Layouts
        header_panel = new JPanel();
        register_panel = new JPanel();
        GroupLayout layout = new GroupLayout(register_panel);
        register_panel.setLayout(layout);  
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        //Dimensions Objects
        Dimension txt_field_dimension = new Dimension(150,25);
        //Border
        Border blackLine;
        blackLine = BorderFactory.createLineBorder(Color.black);
        //Fields
        username_field = new javax.swing.JTextField();
        username_field.setPreferredSize(txt_field_dimension);
        username_field.setMinimumSize(txt_field_dimension);        
        username_field.setBorder(blackLine);
        password_field = new javax.swing.JPasswordField();
        password_field.setPreferredSize(txt_field_dimension);
        password_field.setMinimumSize(txt_field_dimension);        
        password_field.setBorder(blackLine);
        name_field = new javax.swing.JTextField();
        name_field.setPreferredSize(txt_field_dimension);
        name_field.setMinimumSize(txt_field_dimension);        
        name_field.setBorder(blackLine);
        surname_field = new javax.swing.JTextField();
        surname_field.setPreferredSize(txt_field_dimension);
        surname_field.setMinimumSize(txt_field_dimension);        
        surname_field.setBorder(blackLine);
        grade_field = new javax.swing.JTextField();
        grade_field.setPreferredSize(txt_field_dimension);
        grade_field.setMinimumSize(txt_field_dimension);        
        grade_field.setBorder(blackLine);
        //Labels        
        username_label = new javax.swing.JLabel(translation.get("username_label_"+main_dictionary.get("language")));
        password_label = new javax.swing.JLabel(translation.get("password_label_"+main_dictionary.get("language"))+":");        
        name_label = new javax.swing.JLabel(translation.get("name_label_"+main_dictionary.get("language"))+":");  
        surname_label = new javax.swing.JLabel(translation.get("surname_label_"+main_dictionary.get("language"))+":");
        grade_label = new javax.swing.JLabel(translation.get("grade_label_"+main_dictionary.get("language"))+":");
        //Buttons
        back_button = new javax.swing.JButton(translation.get("previous_button_label_"+main_dictionary.get("language")));
        back_button.addActionListener(this);
        register_button = new javax.swing.JButton(translation.get("register_label_"+main_dictionary.get("language")));
        register_button.addActionListener(this);        
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
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)                                            
                    .addComponent(username_label) 
                    .addComponent(password_label)
                    .addComponent(name_label)
                    .addComponent(surname_label) 
                    .addComponent(grade_label)
                    .addComponent(back_button)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(error_label)
                    .addComponent(username_field)
                    .addComponent(password_field)
                    .addComponent(name_field)
                    .addComponent(surname_field)
                    .addComponent(grade_field)
                    .addComponent(register_button)
                )                
         );
        layout.setVerticalGroup(
            layout.createSequentialGroup() 
                .addComponent(error_label)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)   
                    .addComponent(username_label)                    
                    .addComponent(username_field)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(password_label)
                    .addComponent(password_field)                    
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(name_label)
                    .addComponent(name_field)                    
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(surname_label)
                    .addComponent(surname_field)                    
                )  
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(grade_label)
                    .addComponent(grade_field)                    
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(back_button) 
                    .addComponent(register_button) 
                )               
         );
        this.addWindowListener(this);  
        this.setLayout(new BorderLayout(1,2));
        this.addWindowListener(this);
        this.add(header_panel,BorderLayout.PAGE_START);
        this.add(register_panel,BorderLayout.CENTER);              
    }
            
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        //System.out.println(cmd);  //Debug
        if(cmd.equals(translation.get("register_label_"+main_dictionary.get("language")))){
            register();
        }else if(cmd.equals("gr")){
            changeTranslation("gr");
        }else if(cmd.equals("eng")){
            changeTranslation("eng");
        }else if(cmd.equals(translation.get("previous_button_label_"+main_dictionary.get("language")))){
            goBack("");
        }
    }

    private void goBack(String back_message) {
        LoginFrame login_frame = new LoginFrame();
        login_frame.setError_label_text(back_message);
        login_frame.setTitle("Login");
        login_frame.setSize(new Dimension(650,300));
        login_frame.setMinimumSize(new Dimension(650,300));
        login_frame.setVisible(true);
        login_frame.setLocationRelativeTo(null);
        this.dispose();
    }
    
    private void register(){
        String webServicesStringUrl,responseString;
        String REGEX = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}";
        String username=!(username_field.getText().equals(""))?username_field.getText():null;
        String login_password = "";
        for(int i=0; i<password_field.getPassword().length; i++){
            login_password += password_field.getPassword()[i];
        }
        String name=!(name_field.getText().equals(""))?name_field.getText():null;
        String surname=!(surname_field.getText().equals(""))?surname_field.getText():null;
        String grade=!(grade_field.getText().equals(""))?grade_field.getText():null;
        if(username!=null && name!=null && surname!=null && grade_field!=null){
            if(login_password.matches(REGEX)){
                webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Controller/Register?username="+
                    username+"&password="+login_password+"&name="+name+"&surname="+surname+"&grade="+grade;
                responseString = FunctionsController.getResponse(webServicesStringUrl,"POST");
                if(responseString!=null && !responseString.contains("Error")){
                    //Professor Registered.
                    goBack(translation.get("message_register_done_"+main_dictionary.get("language")));
                }else{
                    error_label.setText(translation.get("error_message_register_fail_"+main_dictionary.get("language")));
                }
            }else{
                error_label.setText(translation.get("invalid_password_control_"+main_dictionary.get("language")));
            }
        }else{
            error_label.setText(translation.get("invalid_empty_fields_"+main_dictionary.get("language")));
        }
    }
    
    private void changeTranslation(String language){
        main_dictionary.put("language", language);
        username_label.setText(translation.get("username_label_"+language)+":");
        password_label.setText(translation.get("password_label_"+language)+":");
        name_label.setText(translation.get("name_label_"+language)+":");  
        surname_label.setText(translation.get("surname_label_"+language)+":");
        grade_label.setText(translation.get("grade_label_"+language)+":");
        if(!error_label.getText().isEmpty()) error_label.setText(translation.get("error_message_register_fail_"+language));
        register_button.setText(translation.get("register_label_"+language));      
        back_button.setText(translation.get("previous_button_label_"+language));
    }
    
    @Override
    public void windowClosing(WindowEvent e) {
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
