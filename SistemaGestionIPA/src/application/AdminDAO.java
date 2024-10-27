package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

	//Clase para manejar todo lo relacionado con Admins, será utilizado cuando se conecte con la BD.
public class AdminDAO {

    // Método para insertar un nuevo admin en la BD
    public boolean insertarAdmin(String nombreCompleto, String dni, String password) {
        String sql = "INSERT INTO Admin (nombreCompleto, dni, password) VALUES (?, ?, ?)";
        String hashedPassword = hashPassword(password);

        // Verificar si la contraseña fue cifrada correctamente.
        if (hashedPassword == null) {
            System.out.println("Error al cifrar la contraseña.");
            return false;
        }

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, nombreCompleto);
            pstmt.setString(2, dni);
            pstmt.setString(3, hashedPassword);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar el administrador: " + e.getMessage());
            return false;
        }
    }

        // Método para cifrar la contraseña utilizando SHA-256.
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: Algoritmo de hash no encontrado.");
            return null;
        }
    }
}

