/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.webedu.models.*;
import nl.webedu.dao.*;

/**
 *
 * @author Robert
 */
public class EntryService {
    private EntryDAO entryDao = new EntryDAO();
    private UserStoryDAO userstoryDao = new UserStoryDAO();
    private ProjectDAO projectDao =  new ProjectDAO();
    private SprintDAO sprintDao = new SprintDAO();
    private EmployeeDAO employeeDao = new EmployeeDAO();
    
    public ArrayList<EntryModel> getEntries(EmployeeModel loggedInEmployee){
        ArrayList<EntryModel> entries = entryDao.getEntriesFull();
        
        System.out.println("enployee id: "+loggedInEmployee.getEmployeeId());
        if(loggedInEmployee.getEmployeeRole().equals("employee")){
            ArrayList<EntryModel> entriesDelete = new ArrayList<EntryModel>();
            // Verwijder alle entries die niet van de employees zijn en die niet 
            // up-to-date zijn
            for (int i=0; i<entries.size();i++) {
                EntryModel entry = entries.get(i);
                System.out.println("employeefk"+entry.getEmployeeFk()+"iscurrent: "+entry.getIsCurrent()+" isdeleted: "+entry.getIsDeleted());
                if(entry.getEmployeeFk()!=loggedInEmployee.getEmployeeId()||!entry.getIsCurrent()||entry.getIsDeleted()){
//                    entries.remove(entry);
                    entriesDelete.add(entry);
                }
            }
            entries.removeAll(entriesDelete);
        }
        //deze lijst wordt straks doorzocht om te kijken of er employees bij zitten die ook in de 
//        ArrayList<SprintModel> sprints = sprintDao.allSprints();
//        ArrayList<ProjectModel> projects = projectDao.getAllProjects();
        for(EntryModel entry: entries){
            
            //als de entry een userstoryb heeft dan is de waarde boven 0. 
            //Dan vraag je userstoryDao om een enkele instantie van de userstory en dan om zijn naam
            if(entry.getEntryUserstoryFk() > 0){
                try {
                    entry.setEntryUserstoryName(userstoryDao.getUserstory(entry.getEntryUserstoryFk()).getUserStoryName());
                } catch (java.lang.Exception ex) {
                    Logger.getLogger(EntryService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //als de entry een project heeft dan is de waarde boven 0. 
            //Dan vraag je projectDao om een enkele instantie van de userstorye n dan om zijn naam
            if(entry.getEntryProjectFk()> 0){
                try {
                    entry.setEntryProjectName(projectDao.getProject(entry.getEntryProjectFk()).getProjectName());
                } catch (java.lang.Exception ex) {
                    Logger.getLogger(EntryService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //als de entry een project heeft dan is de waarde boven 0. 
            //Dan vraag je projectDao om een enkele instantie van de userstorye n dan om zijn naam
            if(entry.getEntrySprintFk()> 0){
                try {
                    entry.setEntrySprintName(sprintDao.getSprint(entry.getEntrySprintFk()).getSprintName());
                } catch (java.lang.Exception ex) {
                    Logger.getLogger(EntryService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
//            if(entry.getEmployeeFk()==loggedInEmployee.getEmployeeId()){
//                entry.setEntryEmployeeName(loggedInEmployee.getEmployeeFirstname()+" "+loggedInEmployee.getEmployeeLastName());
//            }else{
//                for(EmployeeModel employee:employees){
//                    if(employee.getEmployeeId()==entry.getEmployeeFk()){
//                        entry.setEntryEmployeeName(employee.getEmployeeFirstname()+" "+employee.getEmployeeLastName());
//                    }
//                }
//            }
        }
        
        return entries;
    }
}