/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.services;

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
                    entry.setEntrySprintName(sprintDao.getSprint(entry.getEntrySprintFk()).getCategoryName());
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

    public WeekModel getEntriesWeek(EmployeeModel loggedInEmployee, Date startDate){
        DateHelper dateHelper = new DateHelper();
        Date endDate = dateHelper.incrementDays(startDate, 6);
        ArrayList<EntryModel> entries = entryDao.getEntriesWithinTimePeriod(startDate, endDate);
        System.out.println(this.getClass().toString()+": entries size: "+entries.size());

        System.out.println(this.getClass().toString()+": employee id: "+loggedInEmployee.getEmployeeId());
        if(loggedInEmployee.getEmployeeRole().equals("employee")){
            entries=removeWrongEntries(entries, loggedInEmployee);
        }
        //deze lijst wordt straks doorzocht om te kijken of er employees bij zitten die ook in de
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
                    entry.setEntrySprintName(sprintDao.getSprint(entry.getEntrySprintFk()).getCategoryName());
                } catch (java.lang.Exception ex) {
                    Logger.getLogger(EntryService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        System.out.println(this.getClass().toString()+": entries size: "+entries.size());
        Date dayDate = new Date(startDate.getTime());
        WeekModel weekModel = new WeekModel(startDate,endDate);
        while (dayDate.compareTo(endDate)<=0){
            System.out.println(this.getClass().toString()+": weekModel creation");
            ArrayList<EntryModel> entriesDelete = new ArrayList<EntryModel>();
            //maak de dag
            DayModel dayModel = new DayModel(dayDate);
            for (EntryModel entry: entries) {
                if(entry.getEntryDate().compareTo(dayDate)==0){
                    System.out.println(this.getClass().toString()+": add entry!");
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
                System.out.println("employeefk"+entry.getEmployeeFk()+"iscurrent: "+entry.getIsCurrent()+" isdeleted: "+entry.getIsDeleted());
                if(entry.getEmployeeFk()!=loggedInEmployee.getEmployeeId()||!entry.getIsCurrent()||entry.getIsDeleted()){
                    entriesDelete.add(entry);
                }
            }
            entries.removeAll(entriesDelete);
            return entries;
    }
    
    public boolean createEntry(EntryModel entryModel){
        DateHelper dateHelper = new DateHelper();
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
}
