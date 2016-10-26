/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.libertad.migracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Fernando
 */
@Named
public class OracleJdbcFactory {
    private static final Logger LOG = Logger.getLogger(OracleJdbcFactory.class.getName());
    
    private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_CONNECTION = "jdbc:oracle:thin:@localhost:1521:ORCL";
    private static final String DB_USER = "PRODUCCION";
    private static final String DB_PASSWORD = "12345";
    // ?useUnicode=true&characterEncoding=utf8
    
    public void testOracleSolar() {

		Connection dbConnection = null;
		      Statement statement = null;

		String selectTableSQL = "SELECT ZONA, SECTOR from SOLAR";

		try {
			dbConnection = getConnection();
			statement = dbConnection.createStatement();

			System.out.println(selectTableSQL);

			// execute select SQL stetement
                        ResultSet rs = statement.executeQuery(selectTableSQL);
                        
			while (rs.next()) {

                            String userid = rs.getString("ZONA");
                            String username = rs.getString("SECTOR");

                            System.out.println("zona : " + userid);
                            System.out.println("sector : " + username);

			}
                }catch (SQLException e) {

                    System.out.println(e.getMessage());

		} finally {

                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(OracleJdbcFactory.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    if (dbConnection != null) {
                        try {
                            dbConnection.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(OracleJdbcFactory.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

		}
    }
    
    
    public Connection getConnection(){
        Connection conn = null;
        
        try{
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(DB_CONNECTION, DB_USER,
					DB_PASSWORD);
        }catch(ClassNotFoundException | SQLException ex){
            LOG.log(Level.SEVERE, "", ex);
        }
         return conn;
    }
    
    @PostConstruct
    public void init(){
        
    }
    
}
