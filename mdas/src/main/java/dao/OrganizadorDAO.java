package dao;

import aux.SQLProperties;
import aux.DBConnection;
import dto.Organizador;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class OrganizadorDAO {
    
    private SQLProperties sqlProperties;

    public OrganizadorDAO() {
        sqlProperties = new SQLProperties();
    }  
    
    public Boolean insertarOrganizador(Organizador organizador) {
        Boolean registrado = false;
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;

        // Para encriptar la contraseña
        String hashedPassword = BCrypt.hashpw(organizador.getContrasena(), BCrypt.gensalt());

        try {
            Connection connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("insertar_organizador");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, organizador.getCorreo());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, organizador.getNombreCompleto());
            preparedStatement.setString(4, organizador.getDni());
            preparedStatement.setInt(5, organizador.getTelefono());

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

    public Boolean validarOrganizador(Organizador organizador) {
        Boolean permitirAcceso = false;
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("validar_organizador");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, organizador.getCorreo());

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("contrasena");
                if(BCrypt.checkpw(organizador.getContrasena(), hashedPassword)) {
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