/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.UtenteRegistrato;
import it.univaq.webmarket.framework.data.DataException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author abdrahimzeno
 */
public interface UtenteRegistratoDAO {
    UtenteRegistrato getUtenteRegistrato(String n)throws DataException;
    UtenteRegistrato getUtente(Integer id) throws DataException;
    void deliteUtenteByEmail(String n) throws DataException, SQLException;
    List<UtenteRegistrato> getUtentiRegistratiCreatiDa(int idAmministratore) throws DataException;
    void addUtentebyAdmin(int id_utente) throws SQLException;
    UtenteRegistrato createUtente();
}
