/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.webmarket.data.dao.impl;

import it.univaq.webmarket.data.dao.UtenteDAO;
import it.univaq.webmarket.data.model.Utente;
import it.univaq.webmarket.data.model.impl.proxy.UtenteProxy;
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
public class UtenteDAO_MySQL extends DAO implements UtenteDAO{
    private PreparedStatement sUserByID;
    private PreparedStatement sUserByAdminID;
    private PreparedStatement sUserByEmail;
    private PreparedStatement iUser;

    
    
    public UtenteDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();
            sUserByID = connection.prepareStatement("SELECT * FROM utente WHERE ID=?"); 
            sUserByAdminID=connection.prepareStatement("SELECT u.*, ur.budget_disponibile FROM utente u JOIN utenteRegistrato "+
                "ur ON u.ID = ur.ID WHERE u.creato_da = ? ");
            sUserByEmail=connection.prepareStatement("SELECT * FROM utente WHERE email = ?");
            iUser=connection.prepareStatement("INSERT INTO utente (email, nome, cognome,password, creato_da) VALUES (?, ?, ?, ?, ?) ");
          
        } catch (SQLException ex) {
            throw new DataException("Error initializing market data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {

        try {
            sUserByID.close();

        } catch (SQLException ex) {
           
        }
        super.destroy();
    }
    
    public Utente createUtente() {
        return new UtenteProxy(getDataLayer());
    }
    
    private UtenteProxy createUtente(ResultSet rs) throws DataException {
        try {
            UtenteProxy a = (UtenteProxy) createUtente();
            a.setKey(rs.getInt("ID"));
            a.setCognome(rs.getString("cognome"));
            a.setNome(rs.getString("nome"));
            a.setAmministratore_key(rs.getInt("creato_da"));
            a.setEmail(rs.getString("email"));
            return a;
        } catch (SQLException ex) {
            throw new DataException("Unable to create user object form ResultSet", ex);
        }
    }
    

    @Override
    public Utente getUtente(int id) throws DataException {
        Utente u = null;
       
        if (dataLayer.getCache().has(Utente.class, id)) {
            u = dataLayer.getCache().get(Utente.class, id);
        } else {
     
            try {
                sUserByID.setInt(1, id);
                try ( ResultSet rs = sUserByID.executeQuery()) {
                    if (rs.next()) {
                

                        u = createUtente(rs);
        
                        dataLayer.getCache().add(Utente.class, u);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load user by ID", ex);
            }
        }
        return u;
    }
    
    
    @Override
    public Utente getUtentebyEmail(String n) throws DataException {
        Utente u= null;
        try {
            sUserByEmail.setString(1, n);
            ResultSet rs = sUserByEmail.executeQuery();
            
            if(rs.next()){
                 return getUtente(rs.getInt("ID"));
            } 
            
        } catch (SQLException ex) {
            Logger.getLogger(UtenteRegistratoDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
     
    }
    
    
    @Override
    public List<Utente> getUtentiCreatiDa(int idAmministratore) throws DataException {
        List<Utente> utenti = new ArrayList<>();
        try {
            sUserByAdminID.setInt(1, idAmministratore);
            ResultSet rs = sUserByAdminID.executeQuery();
            while(rs.next()){
                Utente u = createUtente(rs);
                utenti.add(u);
                 dataLayer.getCache().add(Utente.class, u);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UtenteDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return utenti;
    }
    
 
    

    @Override 
    public void addUtente(String email, String nome, String cognome, String password, Integer idAdmin) {
        try {
            iUser.setString(1, email);
            iUser.setString(2, nome);
            iUser.setString(3, cognome);
            iUser.setString(4, password);
            iUser.setInt(5, idAdmin);
            iUser.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UtenteDAO_MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    
    
    
}
