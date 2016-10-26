/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.libertad.migracion.tablas;

import com.origami.libertad.migracion.OracleConnection;
import com.origami.libertad.migracion.OracleJdbcFactory;
import com.origami.libertad.migracion.Parametros;
import com.origami.libertad.migracion.PgConnection;
import com.origami.libertad.migracion.PostgresqlJdbcFactory;
import com.origami.libertad.migracion.SqlConnection;
import com.origami.libertad.migracion.model.ColumnaDb;
import com.origami.libertad.migracion.model.PkComparator;
import com.origami.libertad.migracion.model.PrimaryKey;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author Fernando
 */
@Named
@Scope("prototype")
public class TableMigrator {
    
    @Inject
    private SqlConnection sqlConn;
    @Inject
    private PgConnection pgConn;
    
    @Inject
    private Parametros parametros;
    
    private Connection oc;
    private BaseConnection pgc;
        
    private String tablaOrigen;
    private String tablaDestino;
    
    private List<ColumnaDb> columnas;
    private List<PrimaryKey> pkeys;
    
    private String delimiter = "\t";
    
    @PostConstruct
    private void init(){
        columnas = new LinkedList<>();
        pkeys = new LinkedList<>();
    }
    
    public void set(String tablaOrigen, String tablaDestino){
        this.tablaDestino = tablaDestino;
        this.tablaOrigen = tablaOrigen;
    }
    
    public void start(String tablaOrigen, String tablaDestino ){
        this.set(tablaOrigen, tablaDestino);
        this.start();
    }
    
    /**
     * Ejecuta la migracion de tabla a postgresql
     */
    public void start(){
        conectar();
        genMetadata();
        
        createTable();
        
        Statement oraStatement = null;
        CopyManager cm = null;
        try {
            oraStatement = oc.createStatement();
            oraStatement.setFetchSize(10000);
            cm = new CopyManager(pgc);
            CopyIn cin= cm.copyIn(genCopyString());
            
            Integer counter = 0;
            ResultSet rs = oraStatement.executeQuery(genQuery());
            while (rs.next()) {
                byte[] insert_values = getBytes(rs);
                if(insert_values!=null){
                    cin.writeToCopy(insert_values, 0, insert_values.length);
                }
                counter++;
                if(counter % 5000 == 0){
                    LOG.info(counter + "registros");
                }
            }
            
            rs.close();
            cin.endCopy();
            
            // generar PKs
            this.genPks();
            
        } catch (SQLException ex) {
            Logger.getLogger(TableMigrator.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            cerrarStatement(oraStatement);
            desconectar();
        }
    }
    
    protected byte[] getBytes(ResultSet rs){
        StringBuilder builder = new StringBuilder();
        try {
            
            boolean inited = false;
            for(ColumnaDb cadaCol :columnas){
                if(!inited) inited = true;
                    else builder.append(delimiter);
                
                Object colForNull = rs.getObject(cadaCol.getColumnName());
                if(colForNull == null){
                    builder.append("null");
                    continue;
                }
                
                if(cadaCol.getTypeInt() == Types.NVARCHAR || cadaCol.getTypeInt() == Types.VARCHAR
                        || cadaCol.getTypeInt() == Types.NCHAR || cadaCol.getTypeInt() == Types.CHAR){
                    String colStr = rs.getString(cadaCol.getColumnName());
                    //LOG.info(colStr.);
                     //InputStream is = rs.getUnicodeStream(cadaCol.getColumnName());
                     
                    if(colStr!=null && colStr.length()>0){
//                        String utf8 = new String(colStr.trim().getBytes(Charset.forName("AL32UTF8")), 
//                                Charset.forName("UTF-8"));
                        String utf8 = colStr;
                        utf8 = utf8.replaceAll("[\\\\]", "\\\\\\\\");
                        utf8 = utf8.replaceAll("[\\n]+", "\\\\n");
                        utf8 = utf8.replaceAll("[\\r]+", "\\\\r");
                        utf8 = utf8.replaceAll("[\\t]", "\\\\t");
                        builder//.append("\"")
                                .append( utf8 )
                                /*.append("\"")*/;
                    }
//                    else{
//                        builder.append("NULL");
//                    }
                }
                
                if(cadaCol.getTypeInt() == Types.BIGINT){
                    Long dataLong = rs.getLong(cadaCol.getColumnName());
                    builder.append( dataLong.toString() );
                }
                
                if(cadaCol.getTypeInt() == Types.INTEGER){
                    Integer dataint = rs.getInt(cadaCol.getColumnName());
                    builder.append( dataint.toString() );
                }
                if(cadaCol.getTypeInt() == Types.SMALLINT){
                    Short dataShort = rs.getShort(cadaCol.getColumnName());
                    builder.append( dataShort.toString() );
                }
                
                if(cadaCol.getTypeInt() == Types.NUMERIC || cadaCol.getTypeInt() == Types.DECIMAL){
                    BigDecimal data = rs.getBigDecimal(cadaCol.getColumnName());
                    builder.append( data.toString() );
                }
                
                if(cadaCol.getTypeInt() == Types.DATE){
                    Date data = rs.getDate(cadaCol.getColumnName());
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String text = df.format(data);
                    builder.append( text );
                }
                
                if(cadaCol.getTypeInt() == Types.TIMESTAMP){
                    Timestamp data = rs.getTimestamp(cadaCol.getColumnName());
//                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss");
//                    String text = df.format(data);
                    builder.append( data.toString() );
                }
                
            }
            builder.append("\n");
            
            String data = builder.toString();
            //LOG.info(data);
            
            return data.getBytes("UTF-8");
        } catch (SQLException ex) {
            Logger.getLogger(TableMigrator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TableMigrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    protected void cerrarStatement(Statement sta){
        try {
            sta.close();
        } catch (SQLException ex) {
            Logger.getLogger(TableMigrator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void desconectar(){
        try {
            oc.close();
        } catch (SQLException ex) {
            Logger.getLogger(TableMigrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            pgc.close();
        } catch (SQLException ex) {
            Logger.getLogger(TableMigrator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void conectar(){
        oc = sqlConn.getConnection();
        pgc = pgConn.getConnection();
    }
    
    public String getMundo(){
        
        return "Mundo";
    }
    
    protected void genMetadata(){
        try {
            
            DatabaseMetaData dbm = oc.getMetaData();
            ResultSet columnasRs = dbm.getColumns(parametros.getOrigenDbName(), parametros.getOrigenSchema(), tablaOrigen, null);
            while (columnasRs.next()) {
                String columnName = columnasRs.getString(4);
                int columnType = columnasRs.getInt(5);
                int sizeNum = columnasRs.getInt(7);
                int decimalDigitsNum = columnasRs.getInt(9);
                ColumnaDb colModel = new ColumnaDb(columnName, columnType, sizeNum, decimalDigitsNum);
                
                if(columnType == Types.NVARCHAR || columnType == Types.VARCHAR 
                        || columnType == Types.CHAR || columnType == Types.NCHAR
                        || columnType == Types.BIGINT || columnType == Types.INTEGER || columnType == Types.SMALLINT
                        || columnType == Types.NUMERIC || columnType == Types.DECIMAL
                        || columnType == Types.DATE || columnType == Types.TIMESTAMP)
                    columnas.add(colModel);
                
                LOG.log(Level.INFO, "Columna: {0} {1}", new Object[]{ columnName, columnType });
            }
            columnasRs.close();
            // generar primary keys:
            iniPkList();
            
        } catch (SQLException ex) {
            Logger.getLogger(TableMigrator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void iniPkList() throws SQLException{
        DatabaseMetaData dbm = oc.getMetaData();
        ResultSet pksRs = dbm.getPrimaryKeys(null, null, tablaOrigen);
        while (pksRs.next()) {
            String colName = pksRs.getString(4);
            Short keySeq = pksRs.getShort(5);
            PrimaryKey pk1 = new PrimaryKey(colName, keySeq);
            pkeys.add(pk1);
        }
        // order list by sequence:
        this.orderPkList();
    }
    
    protected String ddlPk(){
        /* Ejemplo:
        ALTER TABLE legacy.manzana
        ADD PRIMARY KEY (zona, sector, codigo);
        */
        StringBuilder sb1 = new StringBuilder("ALTER TABLE ");
        sb1.append(this.tablaDestino)
                .append(" ADD PRIMARY KEY (");
        
        boolean inited = false;
        for(PrimaryKey cadaPk : pkeys){
            if(inited) sb1.append(", ");
            else inited = true;
            // add key column:
            sb1.append("\"");
            sb1.append(cadaPk.getColName());
            sb1.append("\"");
        }
        sb1.append(");");
        
        return sb1.toString();
    }
    
    protected void genPks() throws SQLException{
        if(pkeys.isEmpty()) return;
        Statement st1 = pgc.createStatement();
        st1.execute(this.ddlPk());
        st1.close();
    }
    
    protected void orderPkList(){
        Collections.sort(pkeys, new PkComparator<>());
    }
    
    protected String genQuery(){
        StringBuilder sb = new StringBuilder("SELECT ");
        
        boolean inited = false;
        for(ColumnaDb cadaCol : columnas){
            if(!inited) inited = true;
            else sb.append(", ");
            sb.append(cadaCol.getColumnName());
        }
        sb.append(" FROM ")
                .append(tablaOrigen);
        
        return sb.toString();
    }
    
    protected String genCopyString(){
        StringBuilder sb = new StringBuilder("COPY ")
                .append(tablaDestino)
                .append("(");
        
        boolean inited = false;
        for(ColumnaDb cadaCol : columnas){
            if(!inited) inited = true;
            else sb.append(", ");
            
            sb.append("\"");
            if(parametros.getLowercaseColumns()){
                sb.append(cadaCol.getColumnName().toLowerCase());
            }
            else{
                sb.append(cadaCol.getColumnName());
            }
            sb.append("\"");
        }
        sb.append(") FROM STDIN WITH DELIMITER '" + delimiter +"' NULL 'null' ENCODING 'UTF-8'");
        
        return sb.toString();
    }
    
    protected String genCreateTable(){
        StringBuilder sb = new StringBuilder("CREATE TABLE ")
                .append(tablaDestino)
                .append(" ( \n");
        
        boolean inited = false;
        for(ColumnaDb cadaCol : columnas){
            if(!inited) inited = true;
            else sb.append(", \n");
            sb.append("\"");
            
            if(parametros.getLowercaseColumns()){
                sb.append(cadaCol.getColumnName().toLowerCase());
            }else{
                sb.append(cadaCol.getColumnName());
            }
            
            sb.append("\" ")
                    .append(getCreateType(cadaCol));
        }
        sb.append("\n ) \n WITH ( OIDS=FALSE );");
        
        return sb.toString();
    }
    
    protected String getCreateType(ColumnaDb col){
        if(col.getTypeInt() == Types.NVARCHAR || col.getTypeInt() == Types.VARCHAR
                || col.getTypeInt() == Types.CHAR || col.getTypeInt() == Types.NCHAR){
            if(col.getSizeNum()==0) return "text";
            return "character varying(" + col.getSizeNum() + ")";
        }
        if(col.getTypeInt() == Types.BIGINT){
            return "bigint";
        }
        if(col.getTypeInt() == Types.INTEGER){
            return "integer";
        }
        if(col.getTypeInt() == Types.SMALLINT){
            return "smallint";
        }
        if(col.getTypeInt() == Types.NUMERIC || col.getTypeInt() == Types.DECIMAL){
            if(col.getSizeNum()==0) return "numeric";
            return "numeric (" + col.getSizeNum() + "," + col.getDecimalDigitsNum() + ")";
        }
        if(col.getTypeInt() == Types.DATE){
            return "date";
        }
        if(col.getTypeInt() == Types.TIMESTAMP){
            return "timestamp without time zone";
        }
        
        return null;
    }
    
    protected void createTable(){
        try {
            String ddl = genCreateTable();
            LOG.info(ddl);
            
            PreparedStatement ps = pgc.prepareStatement(ddl);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(TableMigrator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    /***************************************************************************
     * Logger, Getters & Setters:
     ***************************************************************************/
    
    private static final Logger LOG = Logger.getLogger(TableMigrator.class.getName());    
    
    public String getTablaOrigen() {
        return tablaOrigen;
    }

    public void setTablaOrigen(String tablaOrigen) {
        this.tablaOrigen = tablaOrigen;
    }

    public String getTablaDestino() {
        return tablaDestino;
    }

    public void setTablaDestino(String tablaDestino) {
        this.tablaDestino = tablaDestino;
    }
    
    
    
}
