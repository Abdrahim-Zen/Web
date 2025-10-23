package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.Categoria;
import it.univaq.webmarket.data.model.SpecificaCategoria;
import it.univaq.webmarket.framework.data.DataItemImpl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abdrahimzeno
 */
public class CategoriaImpl extends DataItemImpl<Integer> implements Categoria {
    private String nome;
    private List<SpecificaCategoria> specifiche;

    public CategoriaImpl() {
        super();
        nome = null;
        specifiche = new ArrayList<>();
    }
    
    @Override
    public String getNomeCategoria() {
        return nome; 
    }

    @Override
    public void setNomeCategoria(String x) {
        this.nome = x; 
    }
    
    @Override
    public List<SpecificaCategoria> getSpecifiche() {
        return specifiche;
    }
    
    @Override
    public void setSpecifiche(List<SpecificaCategoria> specifiche) {
        this.specifiche = specifiche;
    }
    
    @Override
    public void addSpecifica(SpecificaCategoria specifica) {
        if (this.specifiche == null) {
            this.specifiche = new ArrayList<>();
        }
        this.specifiche.add(specifica);
    }
    
    @Override
    public void removeSpecifica(SpecificaCategoria specifica) {
        if (this.specifiche != null) {
            this.specifiche.remove(specifica);
        }
    }
    
    @Override
    public void clearSpecifiche() {
        if (this.specifiche != null) {
            this.specifiche.clear();
        }
    }
}