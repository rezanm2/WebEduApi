/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.services;

import nl.webedu.dao.DataDAO;

/**
 *
 * @author RdenBlaauwen
 */
public class ExportService {
    private DataDAO dataDao = new DataDAO();
    
    public String read(){
        return dataDao.getCsvData();
//        return "test";
    }
}
