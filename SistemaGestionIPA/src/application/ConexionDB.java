package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
   
    //Clase que se conecta con la Base de Datos.
    public class ConexionDB {
        private static final String URL = "jdbc:mysql://localhost:3306/DB_IPASOFT";
        private static final String USER = "root"; 
        private static final String PASSWORD = "Cartasnomienten"; 
    
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
   
