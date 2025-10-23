
package it.univaq.webmarket.data.dao.impl;

import it.univaq.webmarket.data.dao.UtenteRegistratoDAO;
import it.univaq.webmarket.data.model.UtenteRegistrato;
import it.univaq.webmarket.data.model.impl.proxy.UtenteRegistratoProxy;
import it.univaq.webmarket.framework.data.DAO;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataLayer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abdrahimzeno
 */
public class UtenteRegistratoDAO_MySQL extends DAO implements UtenteRegistratoDAO{
    private PreparedStatement sRegistedUserbyEmail;
    private PreparedStatement sUserByID;
    private PreparedStatement dUserByEmail;
    private PreparedStatement sRegistedUserbyAdmin;
    private PreparedStatement iRegistedUser;
    public UtenteRegistratoDAO_MySQL(DataLayer d){
        super(d);
    }
    
    @Override
    public void init() throws DataException{
        super.init();
        try {
              sUserByID = connection.prepareStatement(
            "SELECT u.email, u.ID, u.nome, u.cognome, u.password, u.creato_da, ur.budget_disponibile " +
            "FROM utente u JOIN utenteRegistrato ur ON u.ID = ur.ID " +
            "WHERE u.ID = ?"
        );
        
        sRegistedUserbyEmail = connection.prepareStatement(
            "SELECT ur.ID FROM utenteRegistrato ur "+ 
            "JOIN utente u ON ur.ID = u.ID WHERE u.email = ? "
        );
        
        sRegistedUserbyAdmin=connection.prepareStatement(" SELECT u.*, ur.budget_disponibile FROM utente u JOIN utenteRegistrato "+
                "ur ON u.ID = ur.ID WHERE u.creato_da = ? ");
        
        iRegistedUser=connection.prepareStatement("INSERT INTO utenteRegistrato (ID) VALUES (?)");
        
        dUserByEmail=connection.prepareStatement("DELETE FROM utente WHERE email = ?");
        } catch (SQLException ex) {
            Logger.getLogger(UtenteRegistratoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    //ottengo utente tramite email
    @Override
    public UtenteRegistrato getUtenteRegistrato(String n) throws DataException {
        UtenteRegistrato u= null;
        try {
            sRegistedUserbyEmail.setString(1, n);
            ResultSet rs = sRegistedUserbyEmail.executeQuery();
            
            if(rs.next()){
                 return getUtente(rs.getInt("ID"));
            } 
            
        } catch (SQLException ex) {
            Logger.getLogger(UtenteRegistratoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
     
    }
    
    //ottengo utente tramite id
    @Override
    public UtenteRegistrato getUtente(Integer id) throws DataException {
        UtenteRegistrato u = null;
     
        if (dataLayer.getCache().has(UtenteRegistrato.class, id)) {
            u = dataLayer.getCache().get(UtenteRegistrato.class, id);
        } else {
           
            try {
                sUserByID.setInt(1, id);
                ResultSet rs = sUserByID.executeQuery();
                    if (rs.next()) {

                        u = createUtente(rs);
          
                        dataLayer.getCache().add(UtenteRegistrato.class, u);
                    }
                
            } catch (SQLException ex) {
                throw new DataException("Unable to load user by ID", ex);
            }
        }
        return u;
    }
    
    @Override
    public void deliteUtenteByEmail(String email) throws DataException, SQLException{
        UtenteRegistrato u = getUtenteRegistrato(email);
        dUserByEmail.setString(1, email);
        int rs = dUserByEmail.executeUpdate();
    }
    
    @Override
    public UtenteRegistrato createUtente() {
        return new UtenteRegistratoProxy(getDataLayer());
    }
    
    @Override
    public void addUtentebyAdmin(int id_utente) throws SQLException{
        iRegistedUser.setInt(1, id_utente);
        iRegistedUser.executeUpdate();
    }
    @Override
    public List<UtenteRegistrato> getUtentiRegistratiCreatiDa(int idAmministratore) throws DataException {
        List<UtenteRegistrato> utenti = new ArrayList<>();
        try {
            sRegistedUserbyAdmin.setInt(1, idAmministratore);
            ResultSet rs = sRegistedUserbyAdmin.executeQuery();
            while(rs.next()){
                UtenteRegistrato u = createUtente(rs);
                utenti.add(u);
                 dataLayer.getCache().add(UtenteRegistrato.class, u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UtenteDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return utenti;
    }
    
    private UtenteRegistratoProxy createUtente(ResultSet rs) throws DataException {
        try {
            UtenteRegistratoProxy a = (UtenteRegistratoProxy) createUtente();
            a.setEmail(rs.getString("email"));
            a.setKey(rs.getInt("ID"));
            a.setCognome(rs.getString("cognome"));
            a.setNome(rs.getString("nome"));
            a.setAmministratore_key(rs.getInt("creato_da"));
            a.setBudgetDisponibile(rs.getDouble("budget_disponibile"));
            a.setPassword(rs.getString("password"));
            
            return a;
        } catch (SQLException ex) {
            throw new DataException("Unable to create user object form ResultSet", ex);
        }
    }
    
}
