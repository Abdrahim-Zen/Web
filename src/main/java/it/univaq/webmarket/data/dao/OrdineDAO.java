/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.Ordine;
import it.univaq.webmarket.framework.data.DataException;
import java.util.List;

/**
 *
 * @author abdrahimzeno
 */
public interface OrdineDAO {

     /**
     * restituisce gli ordini con relativi dettagli tecnico,ordinante e prodotto
     * 
     * @return  lista di ordinni
     */
    List<Ordine> getAllOrdiniWithDetails(int id) throws DataException;
    
     /**
     * restituisce un ordine dall suo id associato
     * @param id specifica ordine
     * @return  ordine
     */
    Ordine getOrdineById(int id) throws DataException;
    
     /**
     * restituisce gli ordini in base allo stato dato come parametro
     * @param stato stato che si vuole verificare
     * @return  lista di ordini
     */
    List<Ordine> getOrdiniByStato(String stato) throws DataException;
    
     /**
     * restituisce gli ordini in base all'idUtente passato come parametro
     * @param idUtente idUtente dell'utente
     * @return  lista di ordini associati all'utente
     */
    List<Ordine> getOrdiniByUtente(int idUtente) throws DataException;
    
     /**
     * inserisce un nuovo ordine nel db
     * @param idUtente idUtente dell'utente
     * @param  idProdottoCandidato prodotto che l'utente ha selezionato
     *
     */
    int insertOrdine(int idUtente, int idProdottoCandidato) throws DataException;
    
     /**
     * aggiorna stato dell'ordine
     * @param idOrdine  id dell'ordine 
     * @param  stato indica il nuovo stato
     *
     */
    void updateStatoOrdine(int idOrdine, String stato) throws DataException;
    
     /**
     * aggiorna stato dell'ordine + motivazione
     * @param idOrdine  id dell'ordine 
     * @param  stato indica il nuovo stato
     * @param  motivazione  indica motivazione
     *
     */
    void updateStatoOrdine(int idOrdine, String stato, String motivazione) throws DataException;
    
     /**
     * cancella ordine dal db
     * @param idOrdine  id dell'ordine 
     *
     */
    void deleteOrdine(int idOrdine) throws DataException;
    
    Ordine createOrdine();
}
