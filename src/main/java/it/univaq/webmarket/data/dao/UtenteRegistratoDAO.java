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

    /**
     * restituisce utente registrato in base all'email
     *
     * @param n email
     * @return utente regitrato
     * @throws DataException
     */
    UtenteRegistrato getUtenteRegistrato(String n) throws DataException;

    /**
     * restituisce utente registrato in base all'id
     *
     * @param id id utente
     * @return utente regitrato
     * @throws DataException
     */
    UtenteRegistrato getUtente(Integer id) throws DataException;

    /**
     * elimina utente registrato in base all'email
     *
     * @param n email
     * @throws DataException
     * @throws SQLException
     */
    void deliteUtenteByEmail(String n) throws DataException, SQLException;

    /**
     * restituisce lista di utenti registrati in base all'amministratore che li
     * ha creati
     *
     * @param idAmministratore id dell'amministratore
     * @return lista di utenti regitrati
     * @throws DataException
     */
    List<UtenteRegistrato> getUtentiRegistratiCreatiDa(int idAmministratore) throws DataException;

    /**
     * aggiungo unte registrato in base ad id_utente
     *
     * @param id
     * @throws SQLException
     */
    void addUtentebyAdmin(int id_utente) throws SQLException;

    UtenteRegistrato createUtente();
}
