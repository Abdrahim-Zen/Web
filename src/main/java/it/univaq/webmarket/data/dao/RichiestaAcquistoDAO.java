/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.webmarket.data.dao;


import it.univaq.webmarket.data.model.RichiestaAcquisto;
import it.univaq.webmarket.framework.data.DataException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author abdrahimzeno
 */
public interface RichiestaAcquistoDAO {
 public void storeRichiestaAcquisto(RichiestaAcquisto richiesta) throws DataException;
 RichiestaAcquisto createRichiesta();
 public List<RichiestaAcquisto> getRichiesteByUtenteId(int utenteid);
 public List<RichiestaAcquisto> getRichiesteNonAssociate() throws SQLException;
 /**
 * Ottiene le richieste non ancora assegnate a nessun tecnico
 */
RichiestaAcquisto getRichiestaAcquistoByID(int id) throws DataException;

/**
 * Aggiorna una richiesta acquisto esistente
 */
void updateRichiestaAcquisto(RichiestaAcquisto richiesta) throws DataException;

public List<Map<String, String>> getSpecificheByRichiestaId(int idRichiesta) throws DataException;

public void updateRichiestabyProdotto(int idProdotto,String scelta) throws SQLException;
}
