package nl.webedu.models.entrymodels;

import nl.webedu.models.EntryModel;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Robert on 12/22/2017.
 */
public class DayModel {

    private Date date;

    private ArrayList<EntryModel> entryModels = new ArrayList<>();

    public DayModel(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<EntryModel> getEntryModels() {
        return entryModels;
    }

    public void setEntryModels(ArrayList<EntryModel> entryModels) {
        this.entryModels = entryModels;
    }
}
