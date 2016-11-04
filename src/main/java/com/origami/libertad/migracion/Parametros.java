/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.libertad.migracion;

import javax.inject.Named;

/**
 *
 * @author Fernando
 */
@Named
public class Parametros {
    
    private Boolean lowercaseColumns = false;
    
    /**
     * Conexion Postgresql destino:
     */
    private String pgConnectionUrl = "jdbc:postgresql://192.168.1.72/sanvicente_censo";
    private String pgUser = "sisapp";
    private String pgPass = "sis98";
    
    /**
     * Conexión de origen:
     */
    private String origenDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private String origenConnectionUrl = "jdbc:sqlserver://127.0.0.1:1433;databaseName=sanvicente;";
    private String origenUser = "sa";
    private String origenPass = "sis98";
    
    private String origenDbName = "sanvicente";
    private String origenSchema = "dbo";
    
    
    

    public Boolean getLowercaseColumns() {
        return lowercaseColumns;
    }

    public void setLowercaseColumns(Boolean lowercaseColumns) {
        this.lowercaseColumns = lowercaseColumns;
    }

    public String getOrigenDbName() {
        return origenDbName;
    }

    public void setOrigenDbName(String origenDbName) {
        this.origenDbName = origenDbName;
    }

    public String getOrigenSchema() {
        return origenSchema;
    }

    public void setOrigenSchema(String origenSchema) {
        this.origenSchema = origenSchema;
    }

    public String getOrigenDriver() {
        return origenDriver;
    }

    public void setOrigenDriver(String origenDriver) {
        this.origenDriver = origenDriver;
    }

    public String getOrigenConnectionUrl() {
        return origenConnectionUrl;
    }

    public void setOrigenConnectionUrl(String origenConnectionUrl) {
        this.origenConnectionUrl = origenConnectionUrl;
    }

    public String getOrigenUser() {
        return origenUser;
    }

    public void setOrigenUser(String origenUser) {
        this.origenUser = origenUser;
    }

    public String getOrigenPass() {
        return origenPass;
    }

    public void setOrigenPass(String origenPass) {
        this.origenPass = origenPass;
    }

    public String getPgConnectionUrl() {
        return pgConnectionUrl;
    }

    public void setPgConnectionUrl(String pgConnectionUrl) {
        this.pgConnectionUrl = pgConnectionUrl;
    }

    public String getPgUser() {
        return pgUser;
    }

    public void setPgUser(String pgUser) {
        this.pgUser = pgUser;
    }

    public String getPgPass() {
        return pgPass;
    }

    public void setPgPass(String pgPass) {
        this.pgPass = pgPass;
    }
    
    
    
}
