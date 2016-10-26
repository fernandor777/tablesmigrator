/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.libertad.migracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.postgresql.core.BaseConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Fernando
 */
@Named
public class PostgresqlJdbcFactory {
    
    @Inject
    private Parametros parametros;
    
    public BaseConnection getConnection(){
        Connection conn = null;
        
        try {
            
            Class.forName("org.postgresql.Driver");
            
            Properties props = new Properties();
            props.setProperty("user", parametros.getPgUser());
            props.setProperty("password", parametros.getPgPass());
            //props.setProperty("ssl","true");
            conn = DriverManager.getConnection(parametros.getPgConnectionUrl(), props);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PostgresqlJdbcFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PostgresqlJdbcFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return (BaseConnection)conn;
    }
    
}
