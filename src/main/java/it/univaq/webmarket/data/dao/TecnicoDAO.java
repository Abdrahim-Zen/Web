/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.webmarket.data.dao;

import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author abdrahimzeno
 */
public interface TecnicoDAO {
    void addTecnicobyAdmin(int id,Date data) throws SQLException;
    Tecnico getTecnicoByID(int id) throws DataException;
    Tecnico getTecnicoByName(String n)throws DataException;
    List<Tecnico> getTecnicoCreatiDa(int idAmministratore) throws DataException;
}
