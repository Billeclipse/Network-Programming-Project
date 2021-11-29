package main;

import Models.AuthKey;
import Models.FunctionsController;
import Models.TranslationModel;
import Models.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.border.Border;

public class LoginFrame extends JFrame implements WindowListener,ActionListener{
    
    public static Dictionary main_dictionary;
    private HashMap<String,String> translation;
    private JPanel login_panel;
    private JPanel header_panel;
    private JTextField username_field;
    private JPasswordField password_field;
    private JLabel username_label;;
    private JLabel password_label;    
    private JLabel error_label;
    private JButton login_button;
    private JButton register_button;
    private JButton greek_language_button;
    private JButton english_language_button;
    
    public LoginFrame() {
        main_dictionary = new Hashtable();
        translation = initTranslation(main_dictionary);
        //Dimensions Objects
        Dimension txt_field_dimension = new Dimension(100,25);
        //Border
        Border blackLine;
        blackLine = BorderFactory.createLineBorder(Color.black); 
        //Panels and Layouts
        header_panel = new JPanel();
        login_panel = new JPanel();
        GroupLayout layout = new GroupLayout(login_panel);
        login_panel.setLayout(layout);  
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);        
        //Fields
        username_field = new javax.swing.JTextField();
        username_field.setPreferredSize(txt_field_dimension);
        username_field.setMinimumSize(txt_field_dimension);        
        username_field.setBorder(blackLine);        
        password_field = new javax.swing.JPasswordField();
        password_field.setPreferredSize(txt_field_dimension);
        password_field.setMinimumSize(txt_field_dimension);        
        password_field.setBorder(blackLine);     
        //Labels
        
        username_label = new javax.swing.JLabel(translation!=null?(translation.get("username_label_"+main_dictionary.get("language"))+":"):"Όνομα Χρήστη:");        
        password_label = new javax.swing.JLabel(translation!=null?(translation.get("password_label_"+main_dictionary.get("language"))+":"):"Κωδικός:");        
        error_label = new javax.swing.JLabel();
        //Buttons
        login_button = new javax.swing.JButton(translation!=null?(translation.get("login_label_"+main_dictionary.get("language"))):"Είσοδος");
        register_button = new javax.swing.JButton(translation!=null?(translation.get("register_label_"+main_dictionary.get("language"))):"Εγγραφή");
        if(translation!=null){
            login_button.addActionListener(this);
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
        }else{
            error_label.setText("Δεν υπάρχει σύνδεση με τον WebServer");
            greek_language_button= new JButton(new ImageIcon("src/images/greek_flag.png"));
            greek_language_button.setBorder(BorderFactory.createEmptyBorder());
            greek_language_button.setContentAreaFilled(false);
            english_language_button = new JButton(new ImageIcon("src/images/english_flag.png"));
            english_language_button.setBorder(BorderFactory.createEmptyBorder());
            english_language_button.setContentAreaFilled(false);
        }
        //Add order on Panels
        header_panel.add(greek_language_button);
        header_panel.add(english_language_button);
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(username_label)                        
                    .addComponent(password_label)    
                    .addComponent(register_button)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(error_label)
                    .addComponent(username_field)
                    .addComponent(password_field)
                    .addComponent(login_button)
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
                    .addComponent(register_button)
                    .addComponent(login_button) 
                )
         );
        this.getRootPane().setDefaultButton(login_button);
        this.setLayout(new BorderLayout(1,2));
        this.addWindowListener(this);
        this.add(header_panel,BorderLayout.PAGE_START);
        this.add(login_panel,BorderLayout.CENTER);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        //System.out.println(cmd);  //Debug
        if(cmd.equals(translation.get("login_label_"+main_dictionary.get("language")))){
            login();
        }else if(cmd.equals("gr")){
            changeTranslation("gr");
        }else if(cmd.equals("eng")){
            changeTranslation("eng");
        }else if(cmd.equals(translation.get("register_label_"+main_dictionary.get("language")))){
            RegisterFrame register_frame = new RegisterFrame();
            register_frame.setTitle("Register");
            register_frame.setSize(new Dimension(800,400));
            register_frame.setMinimumSize(new Dimension(800,400));
            register_frame.setVisible(true);
            register_frame.setLocationRelativeTo(null);
            this.dispose();
        }
    }
    
    private void login() {        
        String webServicesStringUrl,responseString;        
        String login_username=username_field.getText();
        String login_password = "";
        for(int i=0; i<password_field.getPassword().length; i++){
            login_password += password_field.getPassword()[i];
        }
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Controller/Login?username="+
                login_username+"&password="+login_password;
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //LoggedIn.
            AuthKey user_auth_key = null;
            UserModel user_info = null;
            ObjectMapper mapper = new ObjectMapper();         
            try {
                user_auth_key = mapper.readValue(responseString, AuthKey.class);
                user_info = FunctionsController.createUserModelFromAuthKey(user_auth_key);
                main_dictionary.put("professor_info", user_info.toJSON());
                MainFrame main_frame = new MainFrame();
                main_frame.setTitle("Main");
                main_frame.setSize(new Dimension(400,200));
                main_frame.setMinimumSize(new Dimension(400,200));                
                main_frame.setVisible(true);
                main_frame.setLocationRelativeTo(null);
                this.dispose();
            } catch (IOException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }           
        }else{
            if(responseString.contains("Invalid")){
                error_label.setText(translation!=null?(translation.get("error_message_invalid_"+main_dictionary.get("language"))):"Δεν υπάρχει σύνδεση με τον WebServer");
            }else{
                error_label.setText(translation!=null?(translation.get("error_message_login_fail_"+main_dictionary.get("language"))):"Δεν υπάρχει σύνδεση με τον WebServer");
            }
        }        
    }
    
    private void changeTranslation(String language){
        main_dictionary.put("language", language);
        username_label.setText(translation.get("username_label_"+language)+":");
        password_label.setText(translation.get("password_label_"+language)+":");
        if(!error_label.getText().isEmpty()) error_label.setText(translation.get("error_message_login_fail_"+language));
        login_button.setText(translation.get("login_label_"+language));
        register_button.setText(translation.get("register_label_"+language));         
    }
    
    public static HashMap<String,String> initTranslation(Dictionary main_dictionary){
        boolean is_language_ok=false;
        String webServicesStringUrl,responseString;
        String main_language;
        
        if(main_dictionary.isEmpty()){
            main_dictionary.put("language", "gr");
        }
        main_language = main_dictionary.get("language").toString();
        for(int i=0; i<TranslationModel.getAVAILABLE_LANGUAGES().length; i++){
            if(main_language.equals(TranslationModel.getAVAILABLE_LANGUAGES()[i])){
                is_language_ok = true;  break;
            }
        }
        if(!is_language_ok){
            main_language = TranslationModel.getAVAILABLE_LANGUAGES()[0];
            main_dictionary.put("language", main_language);
        }
        
        webServicesStringUrl = "http://localhost:8080/FinalDPWebApp/webresources/Translation/getAllTranslation";
        responseString = FunctionsController.getResponse(webServicesStringUrl,"GET");
        if(responseString!=null && !responseString.contains("Error")){
            //Translation found.
            return FunctionsController.createTranslationModelHashMapFromJSON(responseString);
        }
        return null;
    }

    public void setError_label_text(String error_label_text) {
        this.error_label.setText(error_label_text);
    }
    
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
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
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
