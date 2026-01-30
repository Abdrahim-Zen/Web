package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author abdrahimzeno
 */
public interface TecnicoDAO {

    /**
     * aggiunge tecnico al db
     *
     * @param id id che fa riferimento ad utente
     * @param data data assunzione
     * @throws SQLException
     */
    void addTecnicobyAdmin(int id, Date data) throws SQLException;

    /**
     * restituisce tecnico dall'id
     *
     * @param id id del tecnico
     * @return tecnico
     * @throws DataException
     */
    Tecnico getTecnicoByID(int id) throws DataException;

    /**
     * restituisce tecnico dal nome
     *
     * @param n nome del tecnico
     * @return tecnico
     * @throws DataException
     */
    Tecnico getTecnicoByName(String n) throws DataException;

    /**
     * restituisce lista di tecnici creati dallo stesso amministratore
     *
     * @param idAmministratore id dell'Amministratore
     * @return lista di tecnici
     * @throws DataException
     */
    List<Tecnico> getTecnicoCreatiDa(int idAmministratore) throws DataException;
}
