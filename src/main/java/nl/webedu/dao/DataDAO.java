package nl.webedu.dao;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import nl.webedu.dao.ConnectDAO;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import javafx.scene.control.TextField;

public class DataDAO {
	ConnectDAO connect = new ConnectDAO();	
	/**
	 * Deze methode maakt een csv bestand van de database.
	 * @author rezanaser
	 */
	public void exportCsv()
	{
		String time = new String();
		String filename ="factuur.csv";
	    try {
	        FileWriter fw = new FileWriter(filename);
	        String query = "SELECT entry_version_date, entry_version_starttime, entry_version_endtime, entry_version_description, project_version_name, (entry_version_endtime - entry_version_starttime) AS Uren "
	        		+ "FROM entry_version, project_version "
	        		+ "WHERE entry_version_project_fk = project_version_project_fk "
	        		+"AND entry_version_current = true";
	        Statement stmt = connect.makeConnection().createStatement();
	        fw.append("Datum");
            fw.append(';');
            fw.append("BeginTijd");
            fw.append(';');
            fw.append("EindTijd");
            fw.append(';');
            fw.append("Project");
            fw.append(';');
            fw.append("Werkzaamheden");
            fw.append(';');
            fw.append("Uren");
            fw.append(';');
            fw.append("Algemeen");
            fw.append(';');
            fw.append("Praktijkbeoordelen");
            fw.append(';');
            fw.append("EduCourse");
            fw.append(';');
            fw.append("Overige");
            fw.append(';');
            fw.append("Actorius");
            fw.append(';');
            fw.append("Totaal");
            fw.append(';');
            fw.append("Check");
            fw.append('\n');
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            fw.append(rs.getString(1));
	            fw.append(';');
	            fw.append(rs.getString(2).substring(0, 5));
	            fw.append(';');
	            fw.append(rs.getString(3).substring(0, 5));
	            fw.append(';');
	            fw.append(rs.getString(5));
	            fw.append(';');
	            fw.append(rs.getString(4));
	            fw.append(';');
	            fw.append(rs.getString(6).substring(0, 5));
	            fw.append(';');
	            if(rs.getString(5).equals("Algemeen"))
	            	{
	            		fw.append(rs.getString(6).substring(0, 5));
	            	};
	            if(rs.getString(5).equals("Praktijkbeoordelen"))
	            	{
	            		fw.append(';');
	            		fw.append(rs.getString(6).substring(0, 5));
	            	}
	            if(rs.getString(5).equals("EduCourse"))
            	{
	            	fw.append(';');
	            	fw.append(';');
            		fw.append(rs.getString(6).substring(0, 5));
            	}
	            if(rs.getString(5).equals("Overige"))
            	{
	            	fw.append(';');
	            	fw.append(';');
	            	fw.append(';');
            		fw.append(rs.getString(6).substring(0, 5));
            	}
	            if(rs.getString(5).equals("Actorius"))
            	{
	            	fw.append(';');
	            	fw.append(';');
	            	fw.append(';');
	            	fw.append(';');
            		fw.append(rs.getString(6).substring(0, 5));
            	}
	            fw.append('\n');
	           }
	        fw.flush();
	        fw.close();
	        System.out.println("CSV File is created successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
