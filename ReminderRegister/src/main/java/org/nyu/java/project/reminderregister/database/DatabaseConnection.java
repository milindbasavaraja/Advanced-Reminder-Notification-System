package org.nyu.java.project.reminderregister.database;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseConnection {

    @Autowired
    private DataSource dataSource;

    /*@PostConstruct
    public void testConnection(){
        try{
            Connection conn = dataSource.getConnection();
            System.out.println("Connection successful!");
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
}
