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

 
    List<Ordine> getAllOrdiniWithDetails() throws DataException;
    
    Ordine getOrdineById(int id) throws DataException;
    
    List<Ordine> getOrdiniByStato(String stato) throws DataException;
    
    List<Ordine> getOrdiniByUtente(int idUtente) throws DataException;
    
    int insertOrdine(int idUtente, int idProdottoCandidato) throws DataException;
    
    void updateStatoOrdine(int idOrdine, String stato) throws DataException;
    
    void updateStatoOrdine(int idOrdine, String stato, String motivazione) throws DataException;
    
    void deleteOrdine(int idOrdine) throws DataException;
    
    Ordine createOrdine();
}
