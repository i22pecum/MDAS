package dao;

import dto.Entrada;
import dto.TipoEntrada;
import java.util.ArrayList;
import aux.DBConnection;
import java.sql.*;

public class EntradaDAO {
    
    public ArrayList<Integer> getEntradasByCorreo(String correo) {
        ArrayList<Integer> entradas = new ArrayList<>();
        String sql = "ver_entradas";
        DBConnection dbConnection = new DBConnection();
        
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Integer idEntrada = rs.getInt("idEntrada");
                entradas.add(idEntrada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return entradas;
    }

    public ArrayList<Entrada> getEntradasById(ArrayList<Integer> idEntradas) {
        ArrayList<Entrada> entradas = new ArrayList<>();
        String sql = "ver_entrada";
        DBConnection dbConnection = new DBConnection();
        
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            
            for (Integer id : idEntradas) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    Entrada entrada = new Entrada();
                    entrada.setTipo(TipoEntrada.valueOf(rs.getString("tipoEntrada")));
                    entrada.setPrecio(rs.getFloat("precio"));
                    entrada.setNombreEvento(rs.getString("nombreEvento"));
                    entradas.add(entrada);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entradas;
    }

    public boolean comprarEntrada(String correoUsuario, int idEntrada) {
        DBConnection dbConnection = new DBConnection();
        Connection conn = null;
        PreparedStatement psSelectEntrada = null;
        PreparedStatement psSelectUsuario = null;
        PreparedStatement psUpdateEntrada = null;
        PreparedStatement psInsertVendida = null;
        PreparedStatement psUpdateMonedero = null;
        PreparedStatement psInsertTransaccion = null;
        ResultSet rsEntrada = null;
        ResultSet rsUsuario = null;

        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Obtener detalles de la entrada (precio, disponibles, evento)
            String sqlEntrada = "SELECT precio, disponibles, evento FROM entradas WHERE id = ? FOR UPDATE";
            psSelectEntrada = conn.prepareStatement(sqlEntrada);
            psSelectEntrada.setInt(1, idEntrada);
            rsEntrada = psSelectEntrada.executeQuery();

            if (!rsEntrada.next()) {
                conn.rollback();
                return false; // Entrada no existe
            }

            float precio = rsEntrada.getFloat("precio");
            int disponibles = rsEntrada.getInt("disponibles");
            String evento = rsEntrada.getString("evento");

            if (disponibles <= 0) {
                conn.rollback();
                return false; // No hay entradas disponibles
            }

            // 2. Consultar saldo del usuario
            String sqlUsuario = "SELECT monedero FROM usuarios WHERE correo = ? FOR UPDATE";
            psSelectUsuario = conn.prepareStatement(sqlUsuario);
            psSelectUsuario.setString(1, correoUsuario);
            rsUsuario = psSelectUsuario.executeQuery();

            if (!rsUsuario.next()) {
                conn.rollback();
                return false; // Usuario no encontrado
            }

            float saldo = rsUsuario.getFloat("monedero");
            if (saldo < precio) {
                conn.rollback();
                return false; // Saldo insuficiente
            }

            // 3. Actualizar entradas disponibles
            String sqlUpdateEntrada = "UPDATE entradas SET disponibles = disponibles - 1 WHERE id = ?";
            psUpdateEntrada = conn.prepareStatement(sqlUpdateEntrada);
            psUpdateEntrada.setInt(1, idEntrada);
            psUpdateEntrada.executeUpdate();

            // 4. Insertar en entradasVendidas
            String sqlInsertVendida = "INSERT INTO entradasVendidas (idEntrada, correoUsuario) VALUES (?, ?)";
            psInsertVendida = conn.prepareStatement(sqlInsertVendida);
            psInsertVendida.setInt(1, idEntrada);
            psInsertVendida.setString(2, correoUsuario);
            psInsertVendida.executeUpdate();

            // 5. Actualizar monedero usuario
            String sqlUpdateMonedero = "UPDATE usuarios SET monedero = monedero - ? WHERE correo = ?";
            psUpdateMonedero = conn.prepareStatement(sqlUpdateMonedero);
            psUpdateMonedero.setFloat(1, precio);
            psUpdateMonedero.setString(2, correoUsuario);
            psUpdateMonedero.executeUpdate();

            // 6. Insertar transacción
            String sqlInsertTransaccion = "INSERT INTO transacciones (tipo, importe, fecha, comprador, vendedor) VALUES (?, ?, ?, ?, ?)";
            psInsertTransaccion = conn.prepareStatement(sqlInsertTransaccion);
            psInsertTransaccion.setString(1, "VENTAPRIMARIA");
            psInsertTransaccion.setFloat(2, precio);

            // Fecha actual
            java.sql.Date fechaSQL = new java.sql.Date(System.currentTimeMillis());
            psInsertTransaccion.setDate(3, fechaSQL);

            psInsertTransaccion.setString(4, correoUsuario);

            // Aquí deberías poner el vendedor; si no tienes uno, puedes usar un string fijo como "ORGANIZADOR"
            psInsertTransaccion.setString(5, "ORGANIZADOR");

            psInsertTransaccion.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;

        } finally {
            try {
                if (rsEntrada != null) rsEntrada.close();
                if (rsUsuario != null) rsUsuario.close();
                if (psSelectEntrada != null) psSelectEntrada.close();
                if (psSelectUsuario != null) psSelectUsuario.close();
                if (psUpdateEntrada != null) psUpdateEntrada.close();
                if (psInsertVendida != null) psInsertVendida.close();
                if (psUpdateMonedero != null) psUpdateMonedero.close();
                if (psInsertTransaccion != null) psInsertTransaccion.close();
                if (conn != null) conn.setAutoCommit(true); // Restaurar autoCommit
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
