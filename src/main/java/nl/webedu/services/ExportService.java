/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.services;

import nl.webedu.dao.DataDAO;
import nl.webedu.models.CSVModel;

/**
 *
 * @author RdenBlaauwen
 */
public class ExportService {
    private DataDAO dataDao = new DataDAO();
    
    public CSVModel read(){
        return dataDao.getCsvData();
    }
}
