/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.PresaInCarico;
import it.univaq.webmarket.framework.data.DataException;
import java.util.List;

public interface PresaInCaricoDAO {
    
    /**
     * Ottiene tutte le richieste in carico di un tecnico specifico
     * @param idTecnico l'ID del tecnico
     * @return lista di prese in carico
     * @throws DataException
     */
    List<PresaInCarico> getRichiesteInCaricoByTecnico(int idTecnico) throws DataException;
    
    /**
     * Ottiene una presa in carico per ID
     * @param id l'ID della presa in carico
     * @return la presa in carico
     * @throws DataException
     */
    PresaInCarico getPresaInCaricoByID(int id) throws DataException;
    
    /**
     * Ottiene una presa in carico per ID richiesta
     * @param idRichiesta l'ID della richiesta
     * @return la presa in carico
     * @throws DataException
     */
    PresaInCarico getPresaInCaricoByRichiestaID(int idRichiesta) throws DataException;
    
    /**
     * Inserisce una nuova presa in carico
     * @param presa la presa in carico da inserire
     * @return l'ID generato
     * @throws DataException
     */
    int insertPresaInCarico(PresaInCarico presa) throws DataException;
    
    /**
     * Aggiorna una presa in carico esistente
     * @param presa la presa in carico da aggiornare
     * @throws DataException
     */
    void updatePresaInCarico(PresaInCarico presa) throws DataException;
    
    /**
     * Elimina una presa in carico
     * @param id l'ID della presa in carico da eliminare
     * @throws DataException
     */
    void deletePresaInCarico(int id) throws DataException;
    
    /**
     * Crea un nuovo oggetto PresaInCarico 
     * @return una nuova presa in carico
     */
    PresaInCarico createPresaInCarico();
    
    public void segnaComeCompletato(int idRichiestaInCarico) throws DataException;
    
     List<PresaInCarico> getRichiesteRifiutateByTecnico(int idTecnico) throws DataException;
    
    
}
