/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.libertad.migracion;

import javax.inject.Inject;
import javax.inject.Named;
import org.postgresql.core.BaseConnection;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Fernando
 */
@Named
@Scope("prototype")
public class PgConnection {
    
    private BaseConnection connection;
    @Inject
    private PostgresqlJdbcFactory pgFac;
    
    public BaseConnection getConnection(){
        if(connection==null){
            connection = pgFac.getConnection();
        }
        return connection;
    }
    
}
