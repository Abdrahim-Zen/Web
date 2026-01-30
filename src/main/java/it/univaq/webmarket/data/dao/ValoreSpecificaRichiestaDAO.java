
package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.ValoreSpecificaRichiesta;
import it.univaq.webmarket.framework.data.DataException;

/**
 *
 * @author abdrahimzeno
 */
public interface ValoreSpecificaRichiestaDAO {
     /**
     * salvo valori dediderati dall'utente duramte la richiesta
     *
     * @param x valori delle specifica
     * @throws DataException
     */
    public void storeValoreSpecificaRichiesta(ValoreSpecificaRichiesta x) throws DataException;
    public ValoreSpecificaRichiesta creaValoreRichiesta();
}
