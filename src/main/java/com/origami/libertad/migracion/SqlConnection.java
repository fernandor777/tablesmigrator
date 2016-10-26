/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.libertad.migracion;

import java.sql.Connection;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Fernando
 */
@Named
@Scope("prototype")
public class SqlConnection {
    
    private Connection connection = null;
    @Inject
    private SqlJdbcFactory oraFac;
    
    public Connection getConnection(){
        if(connection==null){
            connection = oraFac.getConnection();
        }
        return connection;
    }
    
}
