package BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe de connexion à la base de données Oracle
 */
public class SessionOracle {

    /**
     * Connexion à la base de données
     */
    static Connection conn;


    /**
     * Constructeur de la classe
     * @param user
     * @param passwd
     */
    public SessionOracle(String user, String passwd)  {
        try {

            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.26.82.31:1521:pdb1",user, passwd);



            System.out.println("connexion réussi");
        }
        catch(SQLException e)
        {
            System.out.println("connexion échoué");

            conn = null;
            System.out.println(new ErreurSQL(e.getErrorCode(),e.getMessage()));
        }
    }

    /**
     * Getter de la connexion
     * @return conn
     */
    public Connection getConnection()
    {
        return conn;
    }

}