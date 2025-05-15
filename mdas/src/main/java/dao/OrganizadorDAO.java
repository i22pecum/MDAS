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

    public boolean restarMonedero(String correo, float cantidad) {
        DBConnection dbConnection = new DBConnection();
        boolean exito = false;
        String sql = sqlProperties.getSQLQuery("restar_monedero_organizador");
        int filas;

        try {
            Connection conn = dbConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, cantidad);
            ps.setString(2, correo);

            filas = ps.executeUpdate();
            exito = filas > 0;

            ps.close();
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exito;
    }
}