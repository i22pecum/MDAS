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
        Entrada entrada = null;
        float precio = 0;
        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);

            for (Integer id : idEntradas) {
                pstmt.setInt(1, id);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    tipoEntrada = TipoEntrada.valueOf(rs.getString("tipo"));
                    precio = rs.getFloat("precio");
                    nombreEvento = rs.getString("evento");
                    entrada = EntradaFactory.createEntrada(tipoEntrada, precio, nombreEvento);
                    entrada.setId(id);
                    entradas.add(entrada);
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
            preparedStatement.setString(6, entrada.getCorreoVendedor());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                publicada = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar entrada: " + e.getMessage());
            publicada = false;
        } finally {
        
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
            conn.setAutoCommit(false); 

            String sqlEntrada = "SELECT precio, cantidad AS disponibles FROM entradas WHERE nombreEvento = ? AND tipo = ? FOR UPDATE";
            psSelectEntrada = conn.prepareStatement(sqlEntrada);
            psSelectEntrada.setString(1, nombreEvento);
            psSelectEntrada.setString(2, tipoEntrada.name());
            rsEntrada = psSelectEntrada.executeQuery();

            if (!rsEntrada.next()) {
                conn.rollback();
                return false; 
            }

            float precio = rsEntrada.getFloat("precio");
            int disponibles = rsEntrada.getInt("disponibles");

            if (disponibles <= 0) {
                conn.rollback();
                return false; 
            }

           
            String sqlUsuario = "SELECT monedero FROM usuarios WHERE correo = ? FOR UPDATE";
            psSelectUsuario = conn.prepareStatement(sqlUsuario);
            psSelectUsuario.setString(1, correoUsuario);
            rsUsuario = psSelectUsuario.executeQuery();

            if (!rsUsuario.next()) {
                conn.rollback();
                return false; 
            }

            float saldo = rsUsuario.getFloat("monedero");
            if (saldo < precio) {
                conn.rollback();
                return false; 
            }

           
            String sqlUpdateEntrada = "UPDATE entradas SET cantidad = cantidad - 1 WHERE nombreEvento = ? AND tipo = ?";
            psUpdateEntrada = conn.prepareStatement(sqlUpdateEntrada);
            psUpdateEntrada.setString(1, nombreEvento);
            psUpdateEntrada.setString(2, tipoEntrada.name());
            psUpdateEntrada.executeUpdate();

        
            String sqlInsertVendida = "INSERT INTO entradasVendidas (nombreEvento, tipo, correoUsuario) VALUES (?, ?, ?)";
            psInsertVendida = conn.prepareStatement(sqlInsertVendida);
            psInsertVendida.setString(1, nombreEvento);
            psInsertVendida.setString(2, tipoEntrada.name());
            psInsertVendida.setString(3, correoUsuario);
            psInsertVendida.executeUpdate();

           
            String sqlUpdateMonedero = "UPDATE usuarios SET monedero = monedero - ? WHERE correo = ?";
            psUpdateMonedero = conn.prepareStatement(sqlUpdateMonedero);
            psUpdateMonedero.setFloat(1, precio);
            psUpdateMonedero.setString(2, correoUsuario);
            psUpdateMonedero.executeUpdate();

          
            String sqlInsertTransaccion = "INSERT INTO transacciones (tipo, importe, fecha, comprador, vendedor) VALUES (?, ?, ?, ?, ?)";
            psInsertTransaccion = conn.prepareStatement(sqlInsertTransaccion);
            psInsertTransaccion.setString(1, "VENTAPRIMARIA");
            psInsertTransaccion.setFloat(2, precio);
            psInsertTransaccion.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            psInsertTransaccion.setString(4, correoUsuario);
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
                    conn.setAutoCommit(true); 
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int insertarReventa(Entrada entrada) {
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet generatedKeys = null;
        int idReventa = 0;
        TipoTransaccion tipotransaccion = TipoTransaccion.VENTASECUNDARIA;

        try {
            connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("insertar_reventa");
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entrada.getNombreEvento());
            preparedStatement.setString(2, entrada.getCorreoVendedor());
            preparedStatement.setString(3, entrada.getTipo().name());
            preparedStatement.setFloat(4, entrada.getPrecio());
            preparedStatement.setString(5, tipotransaccion.name());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idReventa = generatedKeys.getInt(1); 
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar entrada de reventa: " + e.getMessage());
        } finally {
       
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return idReventa;
    }

    public boolean actualizarId(int idEntradaOriginal, int idReventa) {
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        boolean actualizado = false;

        try {
            connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("actualizar_id_entrada_vendida");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idReventa);
            preparedStatement.setInt(2, idEntradaOriginal);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                actualizado = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar id de entrada vendida: " + e.getMessage());
        } finally {
  
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return actualizado;
    }



    public ArrayList<Entrada> getEntradasDisponiblesByNombreEvento(String nombreEvento,
            TipoTransaccion tipoTransaccion) {
        ArrayList<Entrada> entradas = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = dbConnection.getConnection();
            String sql = "SELECT id, tipo, precio, correoVendedor FROM entradas WHERE evento = ? AND tipoVenta = ? AND disponibles > 0";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nombreEvento);
            preparedStatement.setString(2, tipoTransaccion.name());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Entrada entrada = new Entrada();


                entrada.setId(resultSet.getInt("id"));
                String tipoStr = resultSet.getString("tipo");
                entrada.setTipo(TipoEntrada.valueOf(tipoStr));
                entrada.setPrecio(resultSet.getFloat("precio"));
                entrada.setCorreoVendedor(resultSet.getString("correoVendedor"));
                entrada.setNombreEvento(nombreEvento);

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

    public boolean disminuirCantidadEntrada(int idEntrada) {
        DBConnection dbConnection = new DBConnection();
        Connection conn = null;
        PreparedStatement psUpdateEntrada = null;

        try {
            conn = dbConnection.getConnection();

            String sqlUpdateEntrada = "UPDATE entradas SET disponibles = disponibles - 1 WHERE id = ?";
            psUpdateEntrada = conn.prepareStatement(sqlUpdateEntrada);
            psUpdateEntrada.setInt(1, idEntrada);
            int filasAfectadas = psUpdateEntrada.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al disminuir cantidad de entrada: " + e.getMessage());
            return false;
        } finally {
            try {
                if (psUpdateEntrada != null)
                    psUpdateEntrada.close();
                dbConnection.closeConnection();
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    public boolean insertarEntradaVendida(int idEntrada, String correoUsuario) {
        DBConnection dbConnection = new DBConnection();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean insertada = false;

        try {
            connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("insertar_entrada_vendida");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idEntrada);
            preparedStatement.setString(2, correoUsuario);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                insertada = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar entrada vendida: " + e.getMessage());
            insertada = false;
        } finally {

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

        return insertada;
    }
}
