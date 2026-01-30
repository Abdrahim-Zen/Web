
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
    public void setNomeProdottoCandidato(String x){
        super.setNomeProdottoCandidato(x);
        setModified(true);
    }

    @Override
    public void setStato(String stato) {
        super.setStato(stato);
        setModified(true);
    }
    
    @Override
    public void setMotivazione(String x){
        super.setMotivazione(x);
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

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public void setIdProdotto(String string) {
       super.seIdProdotto(string);
        setModified(true);
    }
}
