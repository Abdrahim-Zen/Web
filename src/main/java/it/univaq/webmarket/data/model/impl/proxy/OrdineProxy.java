package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.model.ProdottoCandidato;
import it.univaq.webmarket.data.model.UtenteRegistrato;
import it.univaq.webmarket.data.model.impl.OrdineImpl;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;
import java.time.LocalDateTime;

/**
 *
 * @author abdrahimzeno
 */
public class OrdineProxy extends OrdineImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;

    public OrdineProxy(DataLayer d) {
        super();
        this.modified = false;
        this.dataLayer = d;
    }

    @Override
    public void setDataOrdine(LocalDateTime x) {
        super.setDataOrdine(x);
        setModified(true);
    }

    @Override
    public void setEmailTecnico(String emailTecnico) {
        super.setEmailTecnico(emailTecnico);
        setModified(true);
    }

    @Override
    public void setStato(String x) {
        super.setStato(x);
        setModified(true);
    }

    @Override
    public void setProdottoCandidato(ProdottoCandidato x) {
        super.setProdottoCandidato(x);
        setModified(true);
    }

    public void setMotivazioneRifiuto(String x) {
        super.setMotivaziooneRifiuto(x);
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

    @Override
    public void setNomeUtente(String x) {
        super.setNomeUtente(x);
        setModified(true);
    }

    @Override
    public void setCognomeUtente(String x) {
        super.setCognomeUtente(x);
        setModified(true);
    }

    @Override
    public void setEmailUtente(String x) {
        super.setEmailUtente(x);
        setModified(true);
    }

    @Override
    public void setNomeProdotto(String x) {
        super.setNomeProdotto(x);
        setModified(true);
    }

    @Override
    public void setPrezzoProdotto(double x) {
        super.setPrezzoProdotto(x);
        setModified(true);
    }

    @Override
    public void setIdUtente(int idUtente) {
        super.setIdUtente(idUtente);
        setModified(true);
    }

    @Override
    public void setIdProdottoCandidato(int idProdottoCandidato) {
        super.setIdProdottoCandidato(idProdottoCandidato);
        setModified(true);
    }

}
