/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.services;
import nl.webedu.dao.ProjectDAO;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.webedu.helpers.DateHelper;
import nl.webedu.models.*;
import nl.webedu.dao.*;
import nl.webedu.models.entrymodels.DayModel;
import nl.webedu.models.entrymodels.WeekModel;

/**
 *
 * @author Robert
 */
public class EntryService {
    private EntryDAO entryDao = new EntryDAO();
    private TaskDAO userstoryDao = new TaskDAO();
    private ProjectDAO projectDao =  new ProjectDAO();
    private SprintDAO sprintDao = new SprintDAO();
    private EmployeeDAO employeeDao = new EmployeeDAO();
    
    public ArrayList<EntryModel> getEntries(EmployeeModel loggedInEmployee){
        ArrayList<EntryModel> entries = entryDao.getEntriesFull();
        
        if(loggedInEmployee.getEmployeeRole().equals("employee")){
            ArrayList<EntryModel> entriesDelete = new ArrayList<EntryModel>();
            // Verwijder alle entries die niet van de employees zijn en die niet 
            // up-to-date zijn
            for (int i=0; i<entries.size();i++) {
                EntryModel entry = entries.get(i);
                if(entry.getEmployeeFk()!=loggedInEmployee.getEmployeeId()||!entry.getIsCurrent()||entry.getIsDeleted()){
//                    entries.remove(entry);
                    entriesDelete.add(entry);
                }
            }
            entries.removeAll(entriesDelete);
        }
        //deze lijst wordt straks doorzocht om te kijken of er employees bij zitten die ook in de 
        for(EntryModel entry: entries){
            fillEntryWithTaskName(entry);            
            fillEntryWithProjectName(entry);
            fillEntryWithCategoryName(entry);
        }
        
        return entries;
    }

    public WeekModel getEntriesWeek(EmployeeModel loggedInEmployee, Date startDate){
        DateHelper dateHelper = new DateHelper();
        Date endDate = dateHelper.incrementDays(startDate, 6);
        ArrayList<EntryModel> entries = entryDao.getEntriesWithinTimePeriod(startDate, endDate);
        
        if(loggedInEmployee.getEmployeeRole().equals("employee")){
            entries=removeWrongEntries(entries, loggedInEmployee);
        }
        //deze lijst wordt straks doorzocht om te kijken of er employees bij zitten die ook in de
        for(EntryModel entry: entries){
            fillEntryWithTaskName(entry);            
            fillEntryWithProjectName(entry);
            fillEntryWithCategoryName(entry);
        }

        //Stop de entries in de juiste Days en die in de juiste weeks.
        Date dayDate = new Date(startDate.getTime());
        WeekModel weekModel = new WeekModel(startDate,endDate);
        while (dayDate.compareTo(endDate)<=0){
            ArrayList<EntryModel> entriesDelete = new ArrayList<EntryModel>();
            //maak de dag
            DayModel dayModel = new DayModel(dayDate);
            for (EntryModel entry: entries) {
                if(entry.getEntryDate().compareTo(dayDate)==0){
                    dayModel.getEntryModels().add(entry);
                    entriesDelete.add(entry);
                }
            }
            entries.removeAll(entriesDelete);
            weekModel.getDayModels().add(dayModel);
            dayDate=dateHelper.incrementDays(dayDate,1);
        }
        return weekModel;
    }
    
    public ArrayList<EntryModel> getEntriesQueued(EmployeeModel loggedInEmployee){
        if(loggedInEmployee.getEmployeeRole().equals("employee")){
            //Normale medewerkers mogen deze data sowiezo niet hebben, 
            // dus breek je de operatie af met null
            return null;
        }
        //Haal de data uit de DAO
        ArrayList<EntryModel> entries = entryDao.entry_queued_list();
        
        
        //deze lijst wordt straks doorzocht om te kijken of er employees bij zitten die ook in de 
        for(EntryModel entry: entries){
            fillEntryWithTaskName(entry);            
            fillEntryWithProjectName(entry);
            fillEntryWithCategoryName(entry);
        }
        
        return entries;
    }
    
    private EntryModel fillEntryWithTaskName(EntryModel entry){
        //als de entry een task heeft dan is de waarde boven 0.
            //Dan vraag je userstoryDao om een enkele instantie van de userstory en dan om zijn naam
        if(entry.getEntryUserstoryFk() > 0){
                try {
                    entry.setEntryUserstoryName(userstoryDao.getUserstory(entry.getEntryUserstoryFk()).getUserStoryName());
                } catch (java.lang.Exception ex) {
                    Logger.getLogger(EntryService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        return entry;
    }
    
    private EntryModel fillEntryWithProjectName(EntryModel entry){
        //als de entry een project heeft dan is de waarde boven 0.
            //Dan vraag je projectDao om een enkele instantie van de userstorye n dan om zijn naam
        if(entry.getEntryProjectFk()> 0){
            try {
                entry.setEntryProjectName(projectDao.getProject(entry.getEntryProjectFk()).getProjectName());
            } catch (java.lang.Exception ex) {
                Logger.getLogger(EntryService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entry;
    }
    
    private EntryModel fillEntryWithCategoryName(EntryModel entry){
        //als de entry een category heeft dan is de waarde boven 0. 
            //Dan vraag je SprintDao om een enkele instantie van de userstorye n dan om zijn naam
        if(entry.getEntrySprintFk()> 0){
                try {
                    entry.setEntrySprintName(sprintDao.getSprint(entry.getEntrySprintFk()).getCategoryName());
                } catch (java.lang.Exception ex) {
                    Logger.getLogger(EntryService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        return entry;
    }
    /**
     * Verwijder alle entries die niet van de employees zijn en die niet up-to-date zijn.
     * 
     * @author  Robert
     * @param entries           De lijst van entries.
     * @param loggedInEmployee  De employee die nu ingelogd is.
     * @return entries      de lijst van entries nadat foute entries zijn verwijderd.
     */
    private ArrayList<EntryModel> removeWrongEntries(ArrayList<EntryModel> entries, EmployeeModel loggedInEmployee){
        ArrayList<EntryModel> entriesDelete = new ArrayList<EntryModel>();
            for (int i=0; i<entries.size();i++) {
                EntryModel entry = entries.get(i);
                if(entry.getEmployeeFk()!=loggedInEmployee.getEmployeeId()||!entry.getIsCurrent()||entry.getIsDeleted()){
                    entriesDelete.add(entry);
                }
            }
            entries.removeAll(entriesDelete);
            return entries;
    }
    
    public boolean approveEntries(int[] entryIds, EmployeeModel loggedInEmployee){
        if(loggedInEmployee.getEmployeeRole().equals("employee")){
            return false;
        }
        try {
            for(int i:entryIds){
                    this.entryDao.approveHours(i);
            }
            return true;
        } catch (java.lang.Exception ex) {
            Logger.getLogger(EntryService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    public boolean rejectEntries(int[] entryIds, EmployeeModel loggedInEmployee){
        if(loggedInEmployee.getEmployeeRole().equals("employee")){
            return false;
        }
        try {
            for(int i:entryIds){
                    this.entryDao.rejectHours(i);
            }
            return true;
        } catch (java.lang.Exception ex) {
            Logger.getLogger(EntryService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean createEntry(EntryModel entryModel){
        DateHelper dateHelper = new DateHelper();
        System.out.println(this.getClass().toString()
                +": time before parsing raw: "+entryModel.getEntryStartTime());
        Time parsedStartTime = dateHelper.parseTime(entryModel.getEntryStartTime(), "HH:mm");
        Time parsedEndTime = dateHelper.parseTime(entryModel.getEntryEndTime(), "HH:mm");
       
        try {
            entryDao.addEntry(entryModel.getEmployeeFk(), 
                    entryModel.getEntryProjectFk(), 
                    entryModel.getEntrySprintFk(), 
                    entryModel.getEntryDate(), 
                    entryModel.getEntryDescription(), 
                    parsedStartTime, 
                    parsedEndTime, 
                    entryModel.getEntryUserstoryFk());
            return true;
        } catch (NumberFormatException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean updateEntry(EntryModel entryModel){
        DateHelper dateHelper = new DateHelper();
        Time parsedStartTime = dateHelper.parseTime(entryModel.getEntryStartTime(), "HH:mm");
        Time parsedEndTime = dateHelper.parseTime(entryModel.getEntryEndTime(), "HH:mm");
        try {
            entryDao.modifyEntry(entryModel.getEntryId(), 
                    entryModel.getEntryProjectFk(), 
                    entryModel.getEntrySprintFk(), 
                    entryModel.getEntryDate(), 
                    entryModel.getEntryDescription(), 
                    parsedStartTime, 
                    parsedEndTime, 
                    entryModel.getEntryUserstoryFk());
            System.out.println(this.getClass().toString()+": UPDATEENTRY--------------------------------");
        } catch (NumberFormatException ex) {
            Logger.getLogger(this.getClass().toString()+": ").log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public boolean deleteEntry(int entryId){
        try {
            entryDao.deleteEntry(entryId);
            return true;
        } catch (NumberFormatException ex) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
