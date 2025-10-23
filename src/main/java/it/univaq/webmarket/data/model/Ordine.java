/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.webmarket.data.model;

import it.univaq.webmarket.framework.data.DataItem;
import java.time.LocalDateTime;

/**
 *
 * @author abdrahimzeno
 */
public interface Ordine extends DataItem<Integer> {

    LocalDateTime getDataOrdine();

    void setDataOrdine(LocalDateTime x);

    String getMotivazioneRifiuto();

    void setMotivaziooneRifiuto(String x);

    String getStato();

    void setStato(String x);

    void setIdUtente(int idUtente);

    int getIdUtente();
    
    void setIdProdottoCandidato(int idProdottoCandidato);
    
    int getIdProdottoCandidato();

    ProdottoCandidato getProdottoCandidato();

    void setProdottoCandidato(ProdottoCandidato x);

    String getNomeUtente();

    void setNomeUtente(String nome);

    String getCognomeUtente();

    void setCognomeUtente(String cognome);

    String getEmailUtente();

    void setEmailUtente(String email);

    String getNomeProdotto();

    void setNomeProdotto(String nome);

    double getPrezzoProdotto();

    void setPrezzoProdotto(double prezzo);
    

    String getEmailTecnico();
    void setEmailTecnico(String emailTecnico);

}
