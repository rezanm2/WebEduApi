package nl.webedu.dao;

import nl.webedu.models.ProjectModel;
import nl.webedu.models.EntryModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EntryDAO {
    /**
     * Get all entries in database
     */
    public ArrayList<EntryModel> getAllEntries(){
        ArrayList<EntryModel> sprintList = new ArrayList<>();
        try {
            Connection connect = new ConnectDAO().makeConnection();
            String getAllEntriesQuery = "SELECT * FROM entry";
            PreparedStatement allEntriesStatement = connect.prepareStatement(getAllEntriesQuery);
            ResultSet entrySet = allEntriesStatement.executeQuery();

            while(entrySet.next()){
                EntryModel entryPlaceholder = new EntryModel(
                        entrySet.getInt("entry_id"),
                        entrySet.getString("entry_description"),
                        entrySet.getBoolean("entry_status"),
                        entrySet.getBoolean("entry_isLocked"),
                        entrySet.getBoolean("entry_isDeleted"),
                        entrySet.getBoolean("entry_current"),
                        entrySet.getString("entry_startdate"),
                        entrySet.getString("entry_enddate"));
                sprintList.add(entryPlaceholder);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return sprintList;
    }
}
