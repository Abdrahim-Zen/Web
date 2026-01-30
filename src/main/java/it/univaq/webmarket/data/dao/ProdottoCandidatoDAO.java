
package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.ProdottoCandidato;
import it.univaq.webmarket.framework.data.DataException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author abdrahimzeno
 */
public interface ProdottoCandidatoDAO {
     /**
     * Restituisce lista di prodottti candidati associati all'utente
     * @param userId  indica id utente
     * @return lista dei prodotti candidati associati all'utente
     */
    List<ProdottoCandidato> getProdottiCandidatiByUserID(int userId);
     /**
     * Restituisce lista di prodottti candidati associati alla richiesta
     * @param richiestaId   indica id della richiesta
     * @return lista dei prodotti candidati associati alla richiesta
     */
    List<ProdottoCandidato> getProdottiCandidatiByRichiestaID(int richiestaId);
    
     /**
     * restituisce prodotto candidato dal suo id 
     * @param id   indica id della del prodotto candidato
     * @return prodotto candidato 
     */
    ProdottoCandidato getProdottoCandidatoByID(int id) throws DataException;
    
     /**
     * inserisce prodotto candidato nel db
     * @param prodotto indica il prodotto da inserire
     *  
     */
    int insertProdottoCandidato(ProdottoCandidato prodotto) throws DataException;
    
     /**
     * accettazione o rifuto del prodotto candidato
     * @param id indica il prodotto per cui decidere
     * @param scelta indica la scelta presa
     * @param motivazione in caso di rifiuto si puo inserire motivazione
     */
    void sceltaProdottoCandidato(int id, String scelta, String motivazione) throws DataException;
     /**
     * aggiornamento/modifica del prodotto candidato
     * @param descrizione  descrizione del prodotto candidato
     * @param prezzo indica prezzo del prodotto
     * @param nome nome del prodotto
     * @param idProdotto indica prodotto da modificare
     */
    void updateProdotti(String descrizione, double prezzo, String nome, int idProdotto) throws DataException;

    ProdottoCandidato createProdottoCandidato();
}
