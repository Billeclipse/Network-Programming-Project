package main;

import java.awt.Dimension;

public class Main {
    public static void main(String[] args) {    
        LoginFrame app = new LoginFrame();
        app.setTitle("Login");        
        app.setSize(new Dimension(650,300));
        app.setMinimumSize(new Dimension(650,300));
        app.setLocationRelativeTo(null); 
        app.setVisible(true);
    } 
}
