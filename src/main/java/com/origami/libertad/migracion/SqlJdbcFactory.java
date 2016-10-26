/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.libertad.migracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Fernando
 */
@Named
public class SqlJdbcFactory {
    private static final Logger LOG = Logger.getLogger(SqlJdbcFactory.class.getName());
    
    @Inject
    private Parametros parametros;
    
    /**
     * https://msdn.microsoft.com/en-us/library/ms378526(v=sql.110).aspx
     * @return 
     */
    public Connection getConnection(){
        Connection conn = null;
        
        try{
            Class.forName(parametros.getOrigenDriver());
            conn = DriverManager.getConnection(parametros.getOrigenConnectionUrl(), parametros.getOrigenUser(),
					parametros.getOrigenPass());
        }catch(ClassNotFoundException | SQLException ex){
            LOG.log(Level.SEVERE, "", ex);
        }
         return conn;
    }
    
}
