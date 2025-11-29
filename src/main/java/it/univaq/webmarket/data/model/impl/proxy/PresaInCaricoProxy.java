/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.model.impl.PresaInCaricoImpl;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;
import java.sql.Timestamp;

public class PresaInCaricoProxy extends PresaInCaricoImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;

    public PresaInCaricoProxy(DataLayer d) {
        super();
        this.modified = false;
        this.dataLayer = d;
    }

    @Override
    public void setDataIncarico(Timestamp dataIncarico) {
        super.setDataIncarico(dataIncarico);
        setModified(true);
    }

    @Override
    public void setStato(String stato) {
        super.setStato(stato);
        setModified(true);
    }

    @Override
    public void setCompletato(boolean completato) {
        super.setCompletato(completato);
        setModified(true);
    }

    @Override
    public void setTecnicoKey(int tecnicoKey) {
        super.setTecnicoKey(tecnicoKey);
        setModified(true);
    }

    @Override
    public void setRichiestaKey(int richiestaKey) {
        super.setRichiestaKey(richiestaKey);
        setModified(true);
    }

    // NOTA: I metodi per i dati aggiuntivi (noteRichiesta, importoTotale, etc.)
    // non settano modified=true perché non sono campi persistenti nel database
    // ma vengono calcolati tramite JOIN
    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }
}
