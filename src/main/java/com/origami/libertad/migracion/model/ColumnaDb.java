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
public class ColumnaDb implements Serializable{
    
    private String columnName;
    private Integer typeInt;
    
    private Integer sizeNum;
    private Integer decimalDigitsNum;

    public ColumnaDb() {
    }

 

    public ColumnaDb(String columnName, Integer typeInt, Integer sizeNum, Integer decimalDigitsNum) {
        this.columnName = columnName;
        this.typeInt = typeInt;
        this.sizeNum = sizeNum;
        this.decimalDigitsNum = decimalDigitsNum;
    }
    
    

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getTypeInt() {
        return typeInt;
    }

    public void setTypeInt(Integer typeInt) {
        this.typeInt = typeInt;
    }

    public Integer getSizeNum() {
        return sizeNum;
    }

    public void setSizeNum(Integer sizeNum) {
        this.sizeNum = sizeNum;
    }

    public Integer getDecimalDigitsNum() {
        return decimalDigitsNum;
    }

    public void setDecimalDigitsNum(Integer decimalDigitsNum) {
        this.decimalDigitsNum = decimalDigitsNum;
    }
    
    
}
