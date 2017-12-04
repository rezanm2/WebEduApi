package nl.webedu.dao;

import nl.webedu.models.SprintModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SprintDAO {
    /**
     * Get all sprints in database
     */
    public ArrayList<SprintModel> getAllSprints(){
        ArrayList<SprintModel> sprintList = new ArrayList<>();
        try {
            Connection connect = new ConnectDAO().makeConnection();
            String getAllSprintQuery = "SELECT * FROM sprints";
            PreparedStatement allSprintsStatement = connect.prepareStatement(getAllSprintQuery);
            ResultSet sprintSet = allSprintsStatement.executeQuery();

            while(sprintSet.next()){
                SprintModel sprintPlaceholder = new SprintModel(
                        sprintSet.getInt("sprint_id"),
                        sprintSet.getString("sprint_name"),
                        sprintSet.getString("sprint_description"),
                        sprintSet.getString("sprint_startdate"),
                        sprintSet.getString("sprint_enddate"),
                        sprintSet.getBoolean("sprint_current"));
                sprintList.add(sprintPlaceholder);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return sprintList;
    }
}
