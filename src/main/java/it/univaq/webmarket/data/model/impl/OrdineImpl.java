/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.Ordine;
import it.univaq.webmarket.data.model.ProdottoCandidato;
import it.univaq.webmarket.data.model.UtenteRegistrato;
import it.univaq.webmarket.framework.data.DataItemImpl;
import java.time.LocalDateTime;

/**
 *
 * @author abdrahimzeno
 */
public class OrdineImpl extends DataItemImpl<Integer> implements Ordine {

    private LocalDateTime dataOrdine;
    private String stato;
    private String emailTecnico;
    private ProdottoCandidato prodottoCandidato;
    private String motivazioneRifiuto;
    private int idUtente;
    private int idProdottiCandiato;
    private String nomeUtente;
    private String cognomeUtente;
    private String emailUtente;
    private String nomeProdotto;
    private double prezzoProdotto;

    @Override
    public LocalDateTime getDataOrdine() {
        return dataOrdine;
    }

    @Override
    public void setDataOrdine(LocalDateTime x) {
        this.dataOrdine = x;
    }

    @Override
    public String getStato() {
        return stato;
    }

    @Override
    public void setStato(String x) {
        this.stato = x;
    }

    @Override
    public ProdottoCandidato getProdottoCandidato() {
        return prodottoCandidato;
    }

    @Override
    public void setProdottoCandidato(ProdottoCandidato x) {
        this.prodottoCandidato = x;
    }

    @Override
    public String getMotivazioneRifiuto() {
        return motivazioneRifiuto;
    }

    @Override
    public void setMotivaziooneRifiuto(String x) {
        this.motivazioneRifiuto = x;
    }

    @Override
    public String getNomeUtente() {
        return nomeUtente;
    }

    @Override
    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    @Override
    public String getCognomeUtente() {
        return cognomeUtente;
    }

    @Override
    public void setCognomeUtente(String cognomeUtente) {
        this.cognomeUtente = cognomeUtente;
    }

    @Override
    public String getEmailUtente() {
        return emailUtente;
    }

    @Override
    public void setEmailUtente(String emailUtente) {
        this.emailUtente = emailUtente;
    }

    @Override
    public String getNomeProdotto() {
        return nomeProdotto;
    }

    @Override
    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    @Override
    public double getPrezzoProdotto() {
        return prezzoProdotto;
    }

    @Override
    public void setPrezzoProdotto(double prezzoProdotto) {
        this.prezzoProdotto = prezzoProdotto;
    }

    @Override
    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    @Override
    public int getIdUtente() {
        return this.idUtente;
    }

    @Override
    public void setIdProdottoCandidato(int idProdottoCandidato) {
        this.idProdottiCandiato = idProdottoCandidato;
    }

    @Override
    public int getIdProdottoCandidato() {
        return this.idProdottiCandiato;
    }

    @Override
    public String getEmailTecnico() {
        return emailTecnico;
    }

    @Override
    public void setEmailTecnico(String emailTecnico) {
        this.emailTecnico = emailTecnico;
    }

}
