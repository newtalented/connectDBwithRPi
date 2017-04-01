/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import bean.Datasensors;
import controller.DatasensorsJpaController;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author huy-lap
 */
public class DatasensorDAO {

    public DatasensorDAO() {
    }
    
    public boolean insertIntoTable(Datasensors tbl){
        EntityManagerFactory emf= Persistence.createEntityManagerFactory("connectDBwithRPiPU");
        DatasensorsJpaController con= new DatasensorsJpaController(emf);
        
        try{
        con.create(tbl);
        return true;
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
}
