package com.webservices;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    public Connection getConnection() throws FileNotFoundException,
            IOException, ClassNotFoundException, SQLException{
        Properties props = new Properties();
        FileInputStream fis = null;
        Connection con = null;
        URL url = getClass().getResource("database.properties");
        fis = new FileInputStream(url.getPath());        
        props.load(fis);
        // load the Driver Class
        Class.forName(props.getProperty("DB_DRIVER_CLASS"));
        // create the connection now
        con = DriverManager.getConnection(props.getProperty("DB_URL"),
                props.getProperty("DB_USERNAME"),
                props.getProperty("DB_PASSWORD"));
        return con;
    }
}