/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.Amministratore;
import it.univaq.webmarket.framework.data.DataException;

/**
 *
 * @author abrah
 */
public interface AmministratoreDAO {
     /**
     * Restituisce l'oggetto Amministratore con l'email passata come parametro
     *
     * @param x email dell'Amministratore
     * @return l'oggetto Amministratore con l'email passata come parametro
     */
    Amministratore getAmmistratorebyEmail(String x)throws DataException;
    
     /**
     * Restituisce l'oggetto Amministratore con l'id passato come parametro
     *
     * @param x id dell'Amministratore
     * @return l'oggetto Amministratore con l'id passato come parametro
     */
    Amministratore getAmministratorebyID(Integer x) throws DataException;
    
    
}
