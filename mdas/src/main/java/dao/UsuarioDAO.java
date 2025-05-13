package dao;

import aux.SQLProperties;
import aux.DBConnection;
import dto.Usuario;
import java.sql.*;

public class UsuarioDAO {
    
    private SQLProperties sqlProperties;

    public UsuarioDAO() {
        sqlProperties = new SQLProperties();
    }  
    
    public Boolean insertarUsuario(Usuario usuario) {
        Boolean registrado = false;
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;

        try {
            Connection connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("insertar_usuario");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usuario.getCorreo());
            preparedStatement.setString(2, usuario.getContrasena());
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
            e.printStackTrace();
        } 
        return registrado;
    }

}
