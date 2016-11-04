/**
 * Author:  Fernando
 * Created: Aug 10, 2016
 */
l
ALTER TABLE legacy.predio
  ADD CONSTRAINT fk_predio_ciudadano FOREIGN KEY (ciu_replegal) REFERENCES legacy.ciudadano (ciu_cedula)
   ON UPDATE CASCADE ON DELETE NO ACTION;
CREATE INDEX fki_predio_ciudadano
  ON legacy.predio(ciu_replegal);

ALTER TABLE legacy.predio
  ADD CONSTRAINT fk_predio_divisionpol FOREIGN KEY (divpo_codigo) REFERENCES legacy.division_politica (divpo_codigo)
   ON UPDATE CASCADE ON DELETE NO ACTION;
CREATE INDEX fki_predio_divisionpol
  ON legacy.predio(divpo_codigo);

ALTER TABLE legacy.predio
  ADD CONSTRAINT fk_predio_ubicacion FOREIGN KEY (ubi_codigo) REFERENCES legacy.ubicacion (ubi_codigo)
   ON UPDATE CASCADE ON DELETE NO ACTION;
CREATE INDEX fki_predio_ubicacion
  ON legacy.predio(ubi_codigo);

ALTER TABLE legacy.bloques_predio
  ADD CONSTRAINT fk_bloque_predio FOREIGN KEY (pre_codigocatastral) REFERENCES legacy.predio (pre_codigocatastral)
   ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE legacy.ubicacion
  ADD CONSTRAINT fk_ubicacion_division FOREIGN KEY (divpo_codigo) REFERENCES legacy.division_politica (divpo_codigo)
   ON UPDATE CASCADE ON DELETE CASCADE;
CREATE INDEX fki_ubicacion_division
  ON legacy.ubicacion(divpo_codigo);


ALTER TABLE legacy.desc_edif_bloque
  ADD CONSTRAINT fk_edif_desc FOREIGN KEY (pre_codigocatastral, blopr_numero) REFERENCES legacy.bloques_predio (pre_codigocatastral, blopr_numero)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_edif_desc
  ON legacy.desc_edif_bloque(pre_codigocatastral, blopr_numero);

ALTER TABLE legacy.desc_edif_bloque
  ADD CONSTRAINT fk_edif_descedif FOREIGN KEY (desed_codigo) REFERENCES legacy.desc_edificacion (desed_codigo)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_edif_descedif
  ON legacy.desc_edif_bloque(desed_codigo);


ALTER TABLE legacy.desc_terreno_predio
  ADD CONSTRAINT fk_terreno_predio FOREIGN KEY ("Pre_CodigoCatastral") REFERENCES legacy.predio (pre_codigocatastral)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_terreno_predio
  ON legacy.desc_terreno_predio("Pre_CodigoCatastral");

ALTER TABLE legacy.desc_terreno_predio
  ADD CONSTRAINT fk_terreno_descterreno FOREIGN KEY ("DesTe_Codigo") REFERENCES legacy.desc_terreno ("DesTe_Codigo")
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_terreno_descterreno
  ON legacy.desc_terreno_predio("DesTe_Codigo");


ALTER TABLE legacy.infra_serv_predio
  ADD CONSTRAINT fk_infraserv_predio FOREIGN KEY ("Pre_CodigoCatastral") REFERENCES legacy.predio (pre_codigocatastral)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_infraserv_predio
  ON legacy.infra_serv_predio("Pre_CodigoCatastral");

ALTER TABLE legacy.infra_serv
  ADD CONSTRAINT fk_infraserv_infra FOREIGN KEY ("InfSe_Codigo") REFERENCES legacy.infra_serv ("InfSe_Codigo")
   ON UPDATE NO ACTION ON DELETE NO ACTION;


ALTER TABLE legacy.uso_suelo_predio
  ADD CONSTRAINT fk_uso_predio FOREIGN KEY (pre_codigocatastral) REFERENCES legacy.predio (pre_codigocatastral)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_uso_predio
  ON legacy.uso_suelo_predio(pre_codigocatastral);

ALTER TABLE legacy.uso_suelo_predio
  ADD CONSTRAINT fk_usosuelo_usp FOREIGN KEY (usosu_codigo) REFERENCES legacy.uso_suelo (usosu_codigo)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_usosuelo_usp
  ON legacy.uso_suelo_predio(usosu_codigo);






