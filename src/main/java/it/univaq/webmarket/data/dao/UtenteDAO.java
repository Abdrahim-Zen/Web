package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.Utente;
import it.univaq.webmarket.framework.data.DataException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author abdrahimzeno
 */
public interface UtenteDAO {

    /**
     * restituisce utente dall'id
     *
     * @param id id dell'utente
     * @return Utente
     * @throws DataException
     */
    Utente getUtente(int id) throws DataException;

    /**
     * restituisce lista di utenti creati dallo stesso amministratore
     *
     * @param idAmministratore id dell' amministratore
     * @return lista di utenti
     * @throws DataException
     */
    List<Utente> getUtentiCreatiDa(int idAmministratore) throws DataException;

    /**
     * aggiunge utente al db
     *
     * @param idAdmin id dell' amministratore che lo ha creato
     * @param cognome
     * @param email
     * @param nome
     * @param password
     * @return lista di utenti
     * @throws SQLException
     */
    void addUtente(String email, String nome, String cognome, String password, Integer idAdmin) throws SQLException;

    /**
     * restituisce utente dato come parametro la sua email
     *
     * @param n email
     * @return utente
     * @throws DataException
     */
    Utente getUtentebyEmail(String n) throws DataException;

}
