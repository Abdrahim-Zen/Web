/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.PresaInCarico;
import it.univaq.webmarket.data.model.RichiestaAcquisto;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataItemImpl;
import java.sql.Timestamp;

public class PresaInCaricoImpl extends DataItemImpl<Integer> implements PresaInCarico {

    private boolean completato;
    private int key;
    private Timestamp dataIncarico;
    private String stato;
    private int tecnicoKey;
    private int richiestaKey;

    // Dati aggiuntivi da JOIN
    private String noteRichiesta;
    private Double importoTotale;
    private String utenteNome;
    private String utenteCognome;
    private String categoriaNome;
    private String motivazione;
    private String idProdotto;
    

    // Relazioni
    private Tecnico tecnico;
    private RichiestaAcquisto richiesta;

    public PresaInCaricoImpl() {
        this.key = 0;
        this.dataIncarico = null;
        this.stato = null;
        this.tecnicoKey = 0;
        this.richiestaKey = 0;
        this.noteRichiesta = null;
        this.importoTotale = null;
        this.utenteNome = null;
        this.utenteCognome = null;
        this.categoriaNome = null;
        this.tecnico = null;
        this.richiesta = null;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public void setKey(Integer key) {
        this.key = key;
    }

    @Override
    public Timestamp getDataIncarico() {
        return dataIncarico;
    }

    @Override
    public void setDataIncarico(Timestamp dataIncarico) {
        this.dataIncarico = dataIncarico;
    }

    @Override
    public String getStato() {
        return stato;
    }

    @Override
    public void setStato(String stato) {
        this.stato = stato;
    }

    @Override
    public int getTecnicoKey() {
        return tecnicoKey;
    }

    @Override
    public void setTecnicoKey(int tecnicoKey) {
        this.tecnicoKey = tecnicoKey;
    }

    @Override
    public int getRichiestaKey() {
        return richiestaKey;
    }

    @Override
    public void setRichiestaKey(int richiestaKey) {
        this.richiestaKey = richiestaKey;
    }

    @Override
    public String getNoteRichiesta() {
        return noteRichiesta;
    }

    @Override
    public void setNoteRichiesta(String noteRichiesta) {
        this.noteRichiesta = noteRichiesta;
    }

    @Override
    public Double getImportoTotale() {
        return importoTotale;
    }

    @Override
    public void setImportoTotale(Double importoTotale) {
        this.importoTotale = importoTotale;
    }

    @Override
    public String getUtenteNome() {
        return utenteNome;
    }

    @Override
    public void setUtenteNome(String utenteNome) {
        this.utenteNome = utenteNome;
    }

    @Override
    public String getUtenteCognome() {
        return utenteCognome;
    }

    @Override
    public void setUtenteCognome(String utenteCognome) {
        this.utenteCognome = utenteCognome;
    }

    @Override
    public String getCategoriaNome() {
        return categoriaNome;
    }

    @Override
    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    @Override
    public Tecnico getTecnico() {
        return tecnico;
    }

    @Override
    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
        if (tecnico != null) {
            this.tecnicoKey = tecnico.getKey();
        }
    }

    @Override
    public RichiestaAcquisto getRichiesta() {
        return richiesta;
    }

    @Override
    public void setRichiesta(RichiestaAcquisto richiesta) {
        this.richiesta = richiesta;
        if (richiesta != null) {
            this.richiestaKey = richiesta.getKey();
        }
    }

    @Override
    public String toString() {
        return "PresaInCaricoImpl{"
                + "key=" + key
                + ", dataIncarico=" + dataIncarico
                + ", stato='" + stato + '\''
                + ", tecnicoKey=" + tecnicoKey
                + ", richiestaKey=" + richiestaKey
                + ", noteRichiesta='" + noteRichiesta + '\''
                + ", importoTotale=" + importoTotale
                + ", utenteNome='" + utenteNome + '\''
                + ", utenteCognome='" + utenteCognome + '\''
                + ", categoriaNome='" + categoriaNome + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PresaInCaricoImpl that = (PresaInCaricoImpl) o;
        return key == that.key;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(key);
    }

    @Override
    public boolean isCompletato() {
        return completato;
    }

    @Override
    public void setCompletato(boolean completato) {
        this.completato = completato;
    }

    @Override
    public String getMotivazione() {
        return motivazione; 
    }

    @Override
    public void setMotivazione(String x) {
        this.motivazione=x;
    }

    @Override
    public String getidProdotto() {
        return idProdotto; 
    }

    @Override
    public void seIdProdotto(String x) {
        this.idProdotto=x; 
    }
}
