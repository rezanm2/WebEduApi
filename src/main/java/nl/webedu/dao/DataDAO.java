package nl.webedu.dao;

import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import nl.webedu.models.CSVModel;

public class DataDAO {
	private ConnectDAO connect;

	public DataDAO(){
		this.connect = new ConnectDAO();
	}

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
	        Statement stmt = this.connect.makeConnection().createStatement();
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
                System.out.println(fw.toString());
	        System.out.println("CSV File is created successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
        
        public CSVModel getCsvData(){
                String result = "";
	    try {
	        String query = "SELECT entry_version_date, entry_version_starttime, entry_version_endtime, entry_version_description, project_version_name, (entry_version_endtime - entry_version_starttime) AS Uren "
	        		+ "FROM entry_version, project_version "
	        		+ "WHERE entry_version_project_fk = project_version_project_fk "
	        		+"AND entry_version_current = true";
	        Statement stmt = this.connect.makeConnection().createStatement();
	        result += "Datum";
            result += ';';
            result += "BeginTijd";
            result += ';';
            result += "EindTijd";
            result += ';';
            result += "Project";
            result += ';';
            result += "Werkzaamheden";
            result += ';';
            result += "Uren";
            result += ';';
            result += "Algemeen";
            result += ';';
            result += "Praktijkbeoordelen";
            result += ';';
            result += "EduCourse";
            result += ';';
            result += "Overige";
            result += ';';
            result += "Actorius";
            result += ';';
            result += "Totaal";
            result += ';';
            result += "Check";
            result += '\n';
	        ResultSet rs = stmt.executeQuery(query);
	        while (rs.next()) {
	            result += rs.getString(1);
	            result += ';';
	            result += rs.getString(2).substring(0, 5);
	            result += ';';
	            result += rs.getString(3).substring(0, 5);
	            result += ';';
	            result += rs.getString(5);
	            result += ';';
	            result += rs.getString(4);
	            result += ';';
	            result += rs.getString(6).substring(0, 5);
	            result += ';';
	            if(rs.getString(5).equals("Algemeen"))
	            	{
	            		result += rs.getString(6).substring(0, 5);
	            	};
	            if(rs.getString(5).equals("Praktijkbeoordelen"))
	            	{
	            		result += ';';
	            		result += rs.getString(6).substring(0, 5);
	            	}
	            if(rs.getString(5).equals("EduCourse"))
            	{
	            	result += ';';
	            	result += ';';
            		result += rs.getString(6).substring(0, 5);
            	}
	            if(rs.getString(5).equals("Overige"))
            	{
	            	result += ';';
	            	result += ';';
	            	result += ';';
            		result += rs.getString(6).substring(0, 5);
            	}
	            if(rs.getString(5).equals("Actorius"))
            	{
	            	result += ';';
	            	result += ';';
	            	result += ';';
	            	result += ';';
            		result += rs.getString(6).substring(0, 5);
            	}
	            result += '\n';
	           }
                
	        System.out.println("CSV File is created successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
            return new CSVModel(result);
	}
	
}
