package dao;

import aux.SQLProperties;
import aux.DBConnection;
import dto.Evento;
import java.sql.*;
import java.util.ArrayList;

public class EventoDAO {
    
    private SQLProperties sqlProperties;

    public EventoDAO() {
        sqlProperties = new SQLProperties();
    }

    public Boolean insertarEvento(Evento evento) {
        Boolean publicado = false;
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;

        try {
            Connection connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("insertar_evento");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, evento.getNombre());
            preparedStatement.setString(2, evento.getDescripcion());
            preparedStatement.setString(3, evento.getLugar());
            preparedStatement.setDate(4, evento.getFecha());
            preparedStatement.setString(5, evento.getCorreoOrganizador());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                publicado = true;
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }
            dbConnection.closeConnection();
        } catch (SQLException e) {
            publicado = false;
        }

        return publicado;
    }

    public Boolean cancelarEvento(String nombreEvento) {
        Boolean cancelado = false;
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;

        try {
            Connection connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("cancelar_evento");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nombreEvento);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                cancelado = true;
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }
            dbConnection.closeConnection();
        } catch (SQLException e) {
            cancelado = false;
        }

        return cancelado;
    }

    public Boolean modificarEvento(Evento evento) {
        Boolean modificado = false;
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;

        try {
            Connection connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("modificar_evento");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, evento.getDescripcion());
            preparedStatement.setString(2, evento.getLugar());
            preparedStatement.setDate(3, evento.getFecha());
            preparedStatement.setString(4, evento.getNombre());
            preparedStatement.setString(5, evento.getCorreoOrganizador());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                modificado = true;
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }
            dbConnection.closeConnection();
        } catch (SQLException e) {
            modificado = false;
        }

        return modificado;
    }

    public ArrayList<Evento> verEventosOrganizador(String correoOrganizador) {
        ArrayList<Evento> eventos = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("ver_eventos_organizador");
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, correoOrganizador);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Evento evento = new Evento();
                evento.setNombre(resultSet.getString("nombre"));
                evento.setDescripcion(resultSet.getString("descripcion"));
                evento.setLugar(resultSet.getString("lugar"));
                evento.setFecha(resultSet.getDate("fecha"));
                eventos.add(evento);
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }
            dbConnection.closeConnection();
        } catch (SQLException e) {
            System.out.println("Error al obtener eventos: " + e.getMessage());
        }

        return eventos;
    }

    public ArrayList<Evento> listarEventosDisponibles() {
        ArrayList<Evento> eventos = new ArrayList<>();
        DBConnection dbConnection = new DBConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection connection = dbConnection.getConnection();
            String sql = sqlProperties.getSQLQuery("listar_eventos_disponibles");
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Evento evento = new Evento();
                evento.setNombre(resultSet.getString("nombre"));
                evento.setDescripcion(resultSet.getString("descripcion"));
                evento.setLugar(resultSet.getString("lugar"));
                evento.setFecha(resultSet.getDate("fecha"));
                eventos.add(evento);
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }
            dbConnection.closeConnection();
        } catch (SQLException e) {
            System.out.println("Error al listar eventos: " + e.getMessage());
        }

        return eventos;
    }


}
