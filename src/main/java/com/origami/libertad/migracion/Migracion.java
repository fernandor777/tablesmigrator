/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.libertad.migracion;

import com.origami.libertad.migracion.tablas.TableMigrator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.beans.factory.ObjectFactory;

/**
 *
 * @author Fernando
 */
@Named
public class Migracion {
    static final Logger LOG = Logger.getLogger(Migracion.class.getName());
    
    @Inject
    private ObjectFactory<TableMigrator> tmf;
    
    public void startMigracion(){
        LOG.info((new Date()).toString());
        
        /**
         * Con lowercase = true
         */
        
//        tmf.getObject().start("PREDIO", "legacy.predio");
//        tmf.getObject().start("UBICACION", "legacy.ubicacion");
//        tmf.getObject().start("PROPIETARIO", "legacy.propietario");
//        
//        tmf.getObject().start("USO_SUELO", "legacy.uso_suelo");
//        tmf.getObject().start("USO_SUELO_PREDIO", "legacy.uso_suelo_predio");
//        tmf.getObject().start("CIUDADANO", "legacy.ciudadano");
//        tmf.getObject().start("DIVISION_POLITICA", "legacy.division_politica");
//        
//        tmf.getObject().start("BLOQUES_PREDIO", "legacy.bloques_predio");
//        
//        tmf.getObject().start("DESC_EDIF_BLOQUE", "legacy.desc_edif_bloque");
//        tmf.getObject().start("DESC_EDIFICACION", "legacy.desc_edificacion");


        /**
         * desde aqui sin lowercase ( = false )
         */
        tmf.getObject().start("DESC_TERRENO", "legacy.desc_terreno");
        tmf.getObject().start("DESC_TERRENO_PREDIO", "legacy.desc_terreno_predio");
        
        tmf.getObject().start("INFRA_SERV", "legacy.infra_serv");
        tmf.getObject().start("INFRA_SERV_PREDIO", "legacy.infra_serv_predio");
        
        
        
        
        LOG.info((new Date()).toString());
        
        
        
    }
    
}
