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

    /**
     * salva richiesta di acquisto
     *
     * @param richiesta richiesta da salvare
     *@throws DataException
     */
    public void storeRichiestaAcquisto(RichiestaAcquisto richiesta) throws DataException;

    RichiestaAcquisto createRichiesta();

    /**
     * restituisce richieste in base all'id utente
     *
     * @param id id dell'utente 
     * @return lista di richieste associate all'utente
     */
    public List<RichiestaAcquisto> getRichiesteByUtenteId(int utenteid);
    
     /**
     * restituisce richieste non associate a nessun tecnico
     *
     * 
     * @return lista di richieste non associate
     * @throws SQLException
     */
    public List<RichiestaAcquisto> getRichiesteNonAssociate() throws SQLException;

    /**
     * restituisce richiesta in base all'id
     *
     * @param id id della richiesta 
     * @return richiesta
     * @throws DataException
     */
    RichiestaAcquisto getRichiestaAcquistoByID(int id) throws DataException;

    /**
     * aggiorna la richiesta
     *
     * @param richiesta richiesta che viene aggiornata 
     * @throws DataException
     */
    void updateRichiestaAcquisto(RichiestaAcquisto richiesta) throws DataException;
     /**
     * restituisce le specifiche dell prodotto associato alla richiesta
     *
     * @param idRichiesta  id della richiesta
     * @return lista di mappe 
     * @throws DataException
     */
    public List<Map<String, String>> getSpecificheByRichiestaId(int idRichiesta) throws DataException;
    
     /**
     * aggiorna la richiesta dove si aggiorna il prodottoCandidato scelto e lo stato della richiesta
     *
     * @param idProdotto  id dell prodottoCandidato scelto
     * @param  scelta indica se il prodotto scelto è ok
     * @throws SQLException
     */
    public void updateRichiestabyProdotto(int idProdotto, String scelta) throws SQLException;
}
