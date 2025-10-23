/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.webmarket.data.model;

import it.univaq.webmarket.framework.data.DataItem;
import java.util.List;

/**
 *
 * @author abdrahimzeno
 */
public interface Categoria extends DataItem<Integer>{
    String getNomeCategoria();
    void setNomeCategoria(String x);
    
        // Nuovi metodi per gestire le specifiche
    List<SpecificaCategoria> getSpecifiche();
    void setSpecifiche(List<SpecificaCategoria> specifiche);
    void addSpecifica(SpecificaCategoria specifica);
    void removeSpecifica(SpecificaCategoria specifica);
    void clearSpecifiche();
    
}
