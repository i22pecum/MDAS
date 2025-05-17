package dao;

import dto.Entrada;
import dto.EntradaFactory;
import dto.TipoEntrada;
import dto.TipoTransaccion;

import java.util.ArrayList;
import aux.DBConnection;
import aux.SQLProperties;

import java.sql.*;

public class EntradaDAO {

    private SQLProperties sqlProperties;

    public EntradaDAO() {
        sqlProperties = new SQLProperties();
    }

    public ArrayList<Integer> getIdEntradasByCorreo(String correo) {
        ArrayList<Integer> entradas = new ArrayList<>();
        String sql = sqlProperties.getSQLQuery("ver_entradas_correo");
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

    public ArrayList<Integer> getIdEntradasByEvento(String nombreEvento) {
        ArrayList<Integer> entradas = new ArrayList<>();
        String sql = sqlProperties.getSQLQuery("ver_identradas_evento");
        DBConnection dbConnection = new DBConnection();

        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, nombreEvento);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer idEntrada = rs.getInt("id");
                entradas.add(idEntrada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entradas;
    }

    public ArrayList<Entrada> getEntradasById(ArrayList<Integer> idEntradas) {
        ArrayList<Entrada> entradas = new ArrayList<>();
        String sql = sqlProperties.getSQLQuery("ver_entrada");
        DBConnection dbConnection = new DBConnection();
        TipoEntrada tipoEntrada = null;
        String nombreEvento = null;
        float precio = 0;
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);

            for (Integer id : idEntradas) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    tipoEntrada = TipoEntrada.valueOf(rs.getString("tipoEntrada"));
                    precio = rs.getFloat("precio");
                    nombreEvento = rs.getString("nombreEvento");
                    entradas.add(EntradaFactory.createEntrada(tipoEntrada, precio, nombreEvento));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entradas;
    }

    public Boolean eliminarEntradasVendidasByIdEntrada(ArrayList<Integer> idEntradas) {
        Boolean eliminado = false;
        String sql = sqlProperties.getSQLQuery("eliminar_entradas_vendidas");
        DBConnection dbConnection = new DBConnection();

        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);

            for (Integer id : idEntradas) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
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

    public Boolean eliminarEntradasByNombreEvento(String nombreEvento) {
        Boolean eliminado = false;
        String sql = sqlProperties.getSQLQuery("eliminar_entradas_evento");
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

    public boolean insertarEntrada(Entrada entrada) {
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        boolean publicada = false;

        try {
            connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("insertar_entrada");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entrada.getTipo().name());
            preparedStatement.setFloat(2, entrada.getPrecio());
            preparedStatement.setString(3, entrada.getNombreEvento());
            preparedStatement.setInt(4, entrada.getCantidad());
            preparedStatement.setString(5, TipoTransaccion.VENTAPRIMARIA.name());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                publicada = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar entrada: " + e.getMessage());
            publicada = false;
        } finally {
            // Cerrar recursos en el bloque finally para evitar fugas
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return publicada;
    }

    public boolean comprarEntrada(String correoUsuario, String nombreEvento, TipoEntrada tipoEntrada) {
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

            // 1. Obtener detalles de la entrada (precio, disponibles, evento) con bloqueo
            // FOR UPDATE
            String sqlEntrada = "SELECT precio, cantidad AS disponibles FROM entradas WHERE nombreEvento = ? AND tipo = ? FOR UPDATE";
            psSelectEntrada = conn.prepareStatement(sqlEntrada);
            psSelectEntrada.setString(1, nombreEvento);
            psSelectEntrada.setString(2, tipoEntrada.name());
            rsEntrada = psSelectEntrada.executeQuery();

            if (!rsEntrada.next()) {
                conn.rollback();
                return false; // Entrada no existe
            }

            float precio = rsEntrada.getFloat("precio");
            int disponibles = rsEntrada.getInt("disponibles");

            if (disponibles <= 0) {
                conn.rollback();
                return false; // No hay entradas disponibles
            }

            // 2. Consultar saldo del usuario con bloqueo FOR UPDATE
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

            // 3. Actualizar entradas disponibles (cantidad - 1)
            String sqlUpdateEntrada = "UPDATE entradas SET cantidad = cantidad - 1 WHERE nombreEvento = ? AND tipo = ?";
            psUpdateEntrada = conn.prepareStatement(sqlUpdateEntrada);
            psUpdateEntrada.setString(1, nombreEvento);
            psUpdateEntrada.setString(2, tipoEntrada.name());
            psUpdateEntrada.executeUpdate();

            // 4. Insertar en entradasVendidas
            String sqlInsertVendida = "INSERT INTO entradasVendidas (nombreEvento, tipo, correoUsuario) VALUES (?, ?, ?)";
            psInsertVendida = conn.prepareStatement(sqlInsertVendida);
            psInsertVendida.setString(1, nombreEvento);
            psInsertVendida.setString(2, tipoEntrada.name());
            psInsertVendida.setString(3, correoUsuario);
            psInsertVendida.executeUpdate();

            // 5. Actualizar monedero usuario (saldo - precio)
            String sqlUpdateMonedero = "UPDATE usuarios SET monedero = monedero - ? WHERE correo = ?";
            psUpdateMonedero = conn.prepareStatement(sqlUpdateMonedero);
            psUpdateMonedero.setFloat(1, precio);
            psUpdateMonedero.setString(2, correoUsuario);
            psUpdateMonedero.executeUpdate();

            // 6. Insertar transacción (tipo VENTAPRIMARIA, fecha actual)
            String sqlInsertTransaccion = "INSERT INTO transacciones (tipo, importe, fecha, comprador, vendedor) VALUES (?, ?, ?, ?, ?)";
            psInsertTransaccion = conn.prepareStatement(sqlInsertTransaccion);
            psInsertTransaccion.setString(1, "VENTAPRIMARIA");
            psInsertTransaccion.setFloat(2, precio);
            psInsertTransaccion.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            psInsertTransaccion.setString(4, correoUsuario);
            psInsertTransaccion.setString(5, "ORGANIZADOR"); // vendedor fijo

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
                if (rsEntrada != null)
                    rsEntrada.close();
                if (rsUsuario != null)
                    rsUsuario.close();
                if (psSelectEntrada != null)
                    psSelectEntrada.close();
                if (psSelectUsuario != null)
                    psSelectUsuario.close();
                if (psUpdateEntrada != null)
                    psUpdateEntrada.close();
                if (psInsertVendida != null)
                    psInsertVendida.close();
                if (psUpdateMonedero != null)
                    psUpdateMonedero.close();
                if (psInsertTransaccion != null)
                    psInsertTransaccion.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Restaurar autoCommit
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean insertarReventa(Entrada entrada) {
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        boolean publicada = false;

        try {
            connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("insertar_reventa");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entrada.getNombreEvento());
            preparedStatement.setString(2, entrada.getCorreoVendedor());
            preparedStatement.setString(3, entrada.getTipo().name());
            preparedStatement.setFloat(4, entrada.getPrecio());
            preparedStatement.setString(5, "SECUNDARIA"); // tipo de venta fija

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                publicada = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar entrada de reventa: " + e.getMessage());
            publicada = false;
        } finally {
            // Cerrar recursos en el bloque finally para evitar fugas
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return publicada;
    }

    public ArrayList<Entrada> getEntradasByNombreEvento(String nombreEvento) {
        ArrayList<Entrada> entradas = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = dbConnection.getConnection();
            String sql = "SELECT tipo, precio, evento FROM entradas WHERE evento = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nombreEvento);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Entrada entrada = new Entrada();

                // Convierte el String a enum TipoEntrada
                String tipoStr = resultSet.getString("tipo");
                entrada.setTipo(TipoEntrada.valueOf(tipoStr));

                entrada.setPrecio(resultSet.getFloat("precio"));
                entrada.setNombreEvento(resultSet.getString("evento"));

                entradas.add(entrada);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener entradas del evento: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                dbConnection.closeConnection();
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }

        return entradas;
    }

}
