/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.helpers;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert
 */
public class DateHelper {
    /**
     * Maakt een sql.Date object van een dateString en een formatString
     * 
     * @author Robert den Blaauwen
     * @param  dateString   string met datum
     * @param  formatString string met format
     * @return sqlDate      sql.Date object 
     */
    public Date parseDate(String dateString, String formatString){
        SimpleDateFormat sdf1 = new SimpleDateFormat(formatString);
        java.util.Date date;
        try {
            date = sdf1.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            return sqlDate;
        } catch (ParseException ex) {
            Logger.getLogger(DateHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * @author  Robert den Blaauwen
     * @param   timeString      string met tijd
     * @param   formatString    string met format
     * @return sqlTime          sql.Time object
     */
    public Time parseTime(String timeString, String formatString){
        SimpleDateFormat sdf = new SimpleDateFormat(formatString);
        long ms;
        try {
            ms = sdf.parse(timeString).getTime();
            Time sqlTime = new Time(ms);
            return sqlTime;
        } catch (ParseException ex) {
            Logger.getLogger(DateHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Dagen erbij of eraf. Spreekt voor zich tbqh fam
     *
     * @author  Robert
     * @param startDate datum waar je mee begint
     * @param change    dagen die erbij (of eraf) moeten
     * @return new Date
     */
    public Date incrementDays(Date startDate, int change) {
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, change);  // number of days to add
        return new Date(c.getTime().getTime());
    }
}
