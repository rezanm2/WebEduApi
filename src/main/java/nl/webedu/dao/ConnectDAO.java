package nl.webedu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDAO {
    /**
     * Open een connectie met de database.
     * In de URL van .getConnection moet je de correcte database informatie toevoegen.
     * @return Connection - die ook te gebruiken is in andere klasses.
     * @throws Exception - SQLException
     * @author IIPSEN - Groep 10
     */
    public Connection makeConnection() throws Exception {
        Connection connect = null;
        try {
            // This will load the postgre's driver
            Class.forName("org.postgresql.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/webedu",
                            "postgres", "postgres");
        } catch(SQLException e){
            e.printStackTrace();
        }
        return connect;
    }
}
