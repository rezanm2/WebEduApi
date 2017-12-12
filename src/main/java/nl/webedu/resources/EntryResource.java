/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;

import nl.webedu.dao.EntryDAO;

import javax.ws.rs.Path;

/**
 *
 * @author rezanaser
 */
@Path("/entries")
public class EntryResource {
    private EntryDAO entryDao;

    public EntryResource(){
        entryDao = new EntryDAO();
    }

    
}
