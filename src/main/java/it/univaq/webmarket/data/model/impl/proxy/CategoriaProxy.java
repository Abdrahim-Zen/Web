
package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.model.impl.CategoriaImpl;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;

/**
 *
 * @author abdrahimzeno
 */
public class CategoriaProxy extends CategoriaImpl implements DataItemProxy {

    protected boolean modified;
    protected DataLayer dataLayer;
    
    public CategoriaProxy(DataLayer d){
        super();
        this.modified=false;
        this.dataLayer=d;
    }
    
    @Override
    public void setNomeCategoria(String x){
        super.setNomeCategoria(x);
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
