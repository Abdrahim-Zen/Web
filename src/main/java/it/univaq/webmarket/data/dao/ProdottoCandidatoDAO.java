/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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
      List<ProdottoCandidato> getProdottiCandidatiByUserID(int userId);
    
    List<ProdottoCandidato> getProdottiCandidatiByRichiestaID(int richiestaId);
    
    ProdottoCandidato getProdottoCandidatoByID(int id) throws DataException;
    
    int insertProdottoCandidato(ProdottoCandidato prodotto) throws DataException;
    
    void sceltaProdottoCandidato(int id,String scelta,String motivazione) throws DataException;
    
    void updateProdotti(String descrizione, double prezzo, String nome, int idProdotto) throws DataException;
    ProdottoCandidato createProdottoCandidato();
}
