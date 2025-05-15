package dao;

import aux.SQLProperties;
import java.util.ArrayList;
import dto.Transaccion;
import aux.DBConnection;
import java.sql.*;
import dto.TipoTransaccion;

public class TransaccionDAO {
    
    private SQLProperties sqlProperties;

    public TransaccionDAO() {
        sqlProperties = new SQLProperties();
    }  

    public ArrayList<Transaccion> getTransaccionesByNombreEvento(String nombreEvento) {
        ArrayList<Transaccion> transacciones = new ArrayList<>();
        String sql = sqlProperties.getSQLQuery("ver_transacciones");
        DBConnection dbConnection = new DBConnection();
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            pstmt.setString(1, nombreEvento);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaccion transaccion = new Transaccion();
                transaccion.setCorreoComprador(rs.getString("correoComprador"));
                transaccion.setCorreoVendedor(rs.getString("correoVendedor"));
                transaccion.setPrecio(rs.getFloat("precio"));
                transaccion.setTipo(TipoTransaccion.valueOf(rs.getString("tipo")));
                transacciones.add(transaccion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transacciones;
    }

    public Boolean eliminarTransaccionesByNombreEvento(String nombreEvento) {
        Boolean eliminado = false;
        String sql = sqlProperties.getSQLQuery("eliminar_transacciones");
        DBConnection dbConnection = new DBConnection();
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            pstmt.setString(1, nombreEvento);
            pstmt.executeUpdate();
            eliminado = true;
            if (pstmt != null) {
                pstmt.close();
            }
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eliminado;
    }

}
