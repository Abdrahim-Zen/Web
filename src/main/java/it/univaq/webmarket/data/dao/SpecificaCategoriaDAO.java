package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.SpecificaCategoria;
import it.univaq.webmarket.framework.data.DataException;
import java.util.List;

/**
 *
 * @author abdrahimzeno
 */
public interface SpecificaCategoriaDAO {

    /**
     * restituisce lista delle specifiche associate alla categoria
     *
     * @param idCategoria id della categoria di riferimento
     * @return lista di specifiche
     * @throws DataException
     */
    List<SpecificaCategoria> getListSpecificaCategoria(int idCategoria) throws DataException;

    /**
     * restituisce specifica in base all' id
     *
     * @param id id della specifica
     * @return specifica
     * @throws DataException
     */
    SpecificaCategoria getSpecificaCategoria(int id) throws DataException;

    /**
     * elimina specifica dalla categoria associata
     *
     * @param idCategoria id della categoria di riferimento
     * @throws DataException
     */
    void deleteSpecificheByCategoria(int idCategoria) throws DataException;
     /**
     * elimina specifica dall'id specificato
     *
     * @param idSpecifica  id della specifica
     * @throws DataException
     */
    void deleteSpecifica(int idSpecifica) throws DataException;
    
     /**
     * aggiorna specifica 
     *
     * @param idSpecifica  id della specifica
     * @param  nomeSpecifica nome della specifica da aggiornare
     * @throws DataException
     */
    void updateSpecifica(int idSpecifica, String nomeSpecifica) throws DataException;
    
     /**
     * aggiunge nuova specifica
     *
     * @param idCategoria id della categoria di riferimento
     * @param  nomeSpecifica  nome della nuova specifica
     * @throws DataException
     */
    int insertSpecifica(int idCategoria, String nomeSpecifica) throws DataException;
}
