
package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.Categoria;
import it.univaq.webmarket.framework.data.DataException;
import java.util.List;

/**
 *
 * @author abdrahimzeno
 */
public interface CategoriaDAO {
     /**
     * restituisce tutte le categorie disponibili nel database 
     * 
     * @return Lista di categorie
     * @throws DataException  
     */
    List<Categoria> getAllCategorie() throws DataException;
    
     /**
     * restituisce una categoria 
     * @param id che identifica la categoria
     * @return  categoria
     * @throws DataException 
     */
    Categoria getCategoriabyID(int id)throws DataException;
    
     /**
     * elimina categoria dal db
     * @param id che identifica la categoria
     * @return  categoria
     * @throws DataException 
     */
    void deleteCategoria(int id) throws DataException;
    
     /**
     * aggiorna nome della categoria
     * @param id che identifica la categoria
     * @param nome nuovo nome per la categoria
     * @throws DataException 
     */
    void updateCategoria(int id, String nome) throws DataException;
    
     /**
     * aggiunge una nuova categoria al db
     * @param nome identifica il nome della nuova categoria
     * @throws DataException 
     */
    int insertCategoria(String nome) throws DataException;
    
     /**
     * mi restituisce lista di categorie con relative specifiche
     * 
     * @return  lista di categorie
     * @throws DataException 
     */
    List<Categoria> getAllCategorieWithSpecifiche() throws DataException;
}
