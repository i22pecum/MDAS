package dao;

import aux.SQLProperties;
import aux.DBConnection;
import dto.Usuario;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {
    
    private SQLProperties sqlProperties;

    public UsuarioDAO() {
        sqlProperties = new SQLProperties();
    }  
    
    public Boolean insertarUsuario(Usuario usuario) {
        Boolean registrado = false;
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        // Para encriptar la contraseña
        String hashedPassword = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());

        try {
            Connection connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("insertar_usuario");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario.getCorreo());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, usuario.getNombreCompleto());
            preparedStatement.setString(4, usuario.getDni());
            preparedStatement.setInt(5, usuario.getTelefono());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                registrado = true;
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            dbConnection.closeConnection();
        } catch (SQLException e) {
            registrado = false;
        } 
        return registrado;
    }

    public Boolean validarUsuario(Usuario usuario) {
        Boolean permitirAcceso = false;
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("validar_usuario");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario.getCorreo());

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("contrasena");
                if(BCrypt.checkpw(usuario.getContrasena(), hashedPassword)) {
                    permitirAcceso = true;
                } else {
                    permitirAcceso = false;
                }
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            dbConnection.closeConnection();
        } catch (SQLException e) {
            permitirAcceso = false;
        } 
        return permitirAcceso;
    }

}
