/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.libertad.migracion.model;

import java.io.Serializable;

/**
 *
 * @author Fernando
 */
public class PrimaryKey implements Serializable{
    
    private String colName;
    private Short keySeq;

    public PrimaryKey() {
    }

    public PrimaryKey(String colName, Short keySeq) {
        this.colName = colName;
        this.keySeq = keySeq;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }
 
    public Short getKeySeq() {
        return keySeq;
    }

    public void setKeySeq(Short keySeq) {
        this.keySeq = keySeq;
    }
    
    
    
}
