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
            preparedStatement.setFloat(6, usuario.getMonedero());
            

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

    public boolean recargarMonedero(String correo, float cantidad) {
        DBConnection dbConnection = new DBConnection();
        boolean exito = false;
        String sql;
        int filas;

        try {
            Connection conn = dbConnection.getConnection();
            sql = "UPDATE usuarios SET monedero = monedero + ? WHERE correo = ?";
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

    public float consultarMonedero(String correo) {
    DBConnection dbConnection = new DBConnection();
    float saldo = 0.0f;
    String sql;

    try {
        Connection conn = dbConnection.getConnection();
        sql = "SELECT monedero FROM usuarios WHERE correo = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, correo);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            saldo = rs.getFloat("monedero");
        }

        rs.close();
        ps.close();
        dbConnection.closeConnection();
    } catch (SQLException e) {
        e.printStackTrace();
    }

        return saldo;
    }


}
