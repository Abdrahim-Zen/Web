
package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.ProdottoCandidato;
import it.univaq.webmarket.data.model.RichiestaAcquisto;
import it.univaq.webmarket.data.model.TecnicoIncaricato;
import it.univaq.webmarket.framework.data.DataItemImpl;
import java.sql.Timestamp;

/**
 *
 * @author abdrahimzeno
 */
public class ProdottoCandidatoImpl extends DataItemImpl<Integer> implements ProdottoCandidato{
    private String nome;
    private String descrizione;
    private Double prezzo;
    private Timestamp dataProposta;
    private int tecnicoKey;
    private int richiestaKey;
    private TecnicoIncaricato tecnicoIncaricato;
    private RichiestaAcquisto richiestaAcquisto;
    
    public ProdottoCandidatoImpl(){
        super();
        nome = null;
        descrizione = null;
        prezzo = null;
        dataProposta = null;
        tecnicoKey = 0;
        richiestaKey = 0;
        tecnicoIncaricato = null;
        richiestaAcquisto = null;
    }
    
    @Override
    public String getNome() {
        return nome; 
    }

    @Override
    public void setNome(String x) {
        this.nome = x; 
    }

    @Override
    public String getDescrizione() { 
        return descrizione; 
    }

    @Override
    public void setDescrizione(String x) {
        this.descrizione = x; 
    }

    @Override
    public Double getPrezzo() {
        return prezzo; 
    }

    @Override
    public void setPrezzo(Double x) {
        this.prezzo = x; 
    }

    @Override
    public Timestamp getDataProposta() {
        return dataProposta; 
    }

    @Override
    public void setDataProposta(Timestamp x) {
        this.dataProposta = x; 
    }

    @Override
    public int getTecnicoKey() {
        return tecnicoKey;
    }

    @Override
    public void setTecnicoKey(int key) {
        this.tecnicoKey = key;
    }

    @Override
    public int getRichiestaKey() {
        return richiestaKey;
    }

    @Override
    public void setRichiestaKey(int key) {
        this.richiestaKey = key;
    }

    @Override
    public TecnicoIncaricato getTecnicoIncaricato() {
        return tecnicoIncaricato; 
    }

    @Override
    public void setTecnicoIncaricato(TecnicoIncaricato x) {
        this.tecnicoIncaricato = x;
        if (x != null) {
            this.tecnicoKey = x.getKey();
        }
    }

    @Override
    public RichiestaAcquisto getRichiestaAcquisto() {
        return richiestaAcquisto;
    }

    @Override
    public void setRichiestaAcquisto(RichiestaAcquisto x) {
        this.richiestaAcquisto = x;
        if (x != null) {
            this.richiestaKey = x.getKey();
        }
    }
    
}