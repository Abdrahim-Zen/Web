/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.webmarket.data.model;

/**
 *
 * @author abdrahimzeno
 */



import it.univaq.webmarket.framework.data.DataItem;
import java.sql.Timestamp;

public interface PresaInCarico extends DataItem<Integer> {
    
    // Getter e Setter per i campi base
    Timestamp getDataIncarico();
    void setDataIncarico(Timestamp dataIncarico);
    
    String getStato();
    void setStato(String stato);
    
    String getNomeProdottoCandidato();
    void setNomeProdottoCandidato(String stato);
    
    int getTecnicoKey();
    void setTecnicoKey(int tecnicoKey);
    
    String getidProdotto();
    void seIdProdotto(String x);
    
    int getRichiestaKey();
    void setRichiestaKey(int richiestaKey);
    
    // Getter e Setter per i dati aggiuntivi (da JOIN)
    String getNoteRichiesta();
    void setNoteRichiesta(String note);
    
    Double getImportoTotale();
    void setImportoTotale(Double importoTotale);
    
    String getUtenteNome();
    void setUtenteNome(String utenteNome);
    
    String getUtenteCognome();
    void setUtenteCognome(String utenteCognome);
    
    String getCategoriaNome();
    void setCategoriaNome(String categoriaNome);
    
    // Metodi per le relazioni
    Tecnico getTecnico();
    void setTecnico(Tecnico tecnico);
    
    String getMotivazione();
    void setMotivazione(String x);
    
    RichiestaAcquisto getRichiesta();
    void setRichiesta(RichiestaAcquisto richiesta);
    
    
boolean isCompletato();
void setCompletato(boolean completato);

}
