/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.libertad.migracion.model;

import java.util.Comparator;

/**
 * Comparator by Key Sequence for Primary Keys List Sorting
 * @author Fernando
 */
public class PkComparator<PK extends PrimaryKey> implements Comparator<PrimaryKey>{

    @Override
    public int compare(PrimaryKey o1, PrimaryKey o2) {
        return o1.getKeySeq() - o2.getKeySeq();
    }
    
}
