/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.model.RichiestaAcquisto;
import it.univaq.webmarket.data.model.TecnicoIncaricato;
import it.univaq.webmarket.data.model.impl.ProdottoCandidatoImpl;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;
import java.sql.Timestamp;

/**
 *
 * @author abdrahimzeno
 */
public class ProdottoCandidatoProxy extends ProdottoCandidatoImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;
    
    public ProdottoCandidatoProxy(DataLayer d) {
        super();
        this.modified = false;
        this.dataLayer = d;
    }

    @Override
    public void setDescrizione(String x) {
        super.setDescrizione(x);
        setModified(true);
    }

    @Override
    public void setPrezzo(Double x) {
        super.setPrezzo(x);
        setModified(true);
    }
    
    @Override
    public void setDataProposta(Timestamp x){
        super.setDataProposta(x);
        setModified(true);
    }
    
    @Override
    public void setTecnicoKey(int x){
        super.setTecnicoKey(x);
        setModified(true);
    }
    
    @Override
    public void setRichiestaKey(int x){
        super.setRichiestaKey(x);
        setModified(true);
    }
    
    @Override
    public void setNome(String x){
        super.setNome(x);
        setModified(true);
    }
    
    @Override
    public void setTecnicoIncaricato(TecnicoIncaricato x) {
        super.setTecnicoIncaricato(x);
        setModified(true);
    }
    
    @Override
    public void setRichiestaAcquisto(RichiestaAcquisto x) {
        super.setRichiestaAcquisto(x);
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
}