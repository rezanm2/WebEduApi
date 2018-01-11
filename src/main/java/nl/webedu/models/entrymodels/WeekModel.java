/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.models.entrymodels;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Dit model is gemaakt om dagen te bevatten, die entries bevatten. Ht doel is om entries op een convenient manier te ordenen.
 * @author Robert
 */
public class WeekModel {
    private Date beginDate;
    private Date endDate;


    private ArrayList<DayModel> dayModels = new ArrayList<>();

    public WeekModel(Date beginDate, Date endDate) {
        this.beginDate = beginDate;
        this.endDate=endDate;
    }
    /**
     * @return the beginDate
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * @param beginDate the beginDate to set
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public ArrayList<DayModel> getDayModels() {
        return dayModels;
    }

    public void setDayModels(ArrayList<DayModel> dayModels) {
        this.dayModels = dayModels;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
