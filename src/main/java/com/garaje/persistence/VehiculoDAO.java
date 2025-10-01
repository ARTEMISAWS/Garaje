package com.garaje.persistence;

import com.garaje.model.Vehiculo;
import java.sql.*;
import java.util.*;

/**
 * DAO (Data Access Object) para la entidad {@link Vehiculo}.
 * 
 * Esta clase gestiona todas las operaciones CRUD (Create, Read, Update, Delete)
 * sobre la tabla "vehiculos" en la base de datos.
 * 
 * Principales responsabilidades:
 * <ul>
 *   <li>Listar todos los vehículos registrados.</li>
 *   <li>Buscar un vehículo por su ID.</li>
 *   <li>Validar si existe una placa antes de insertar.</li>
 *   <li>Agregar nuevos vehículos.</li>
 *   <li>Actualizar datos de vehículos existentes.</li>
 *   <li>Eliminar vehículos por ID.</li>
 * </ul>
 */
public class VehiculoDAO {

    /** Conexión activa a la base de datos (inyectada desde fuera). */
    private final Connection con;

    /**
     * Constructor del DAO.
     *
     * @param con conexión JDBC activa hacia la base de datos.
     */
    public VehiculoDAO(Connection con) {
        this.con = con;
    }

    /**
     * Lista todos los vehículos registrados en la tabla.
     *
     * @return Lista de objetos {@link Vehiculo}. Si no hay registros, retorna una lista vacía.
     * @throws SQLException en caso de error de comunicación con la BD.
     */
    public List<Vehiculo> listar() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos";

        System.out.println("DAO: Ejecutando listar() → " + sql);

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int contador = 0;

            // Recorremos el ResultSet para convertir cada fila en un objeto Vehiculo
            while (rs.next()) {
                Vehiculo v = new Vehiculo(
                    rs.getInt("id"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("color"),
                    rs.getString("propietario")
                );
                lista.add(v);

                System.out.println("DAO: Vehículo encontrado → ID: " 
                        + v.getId() + " Placa: " + v.getPlaca());
                contador++;
            }

            System.out.println("DAO: Consulta finalizada. Total encontrados: " + contador);

        } catch (SQLException ex) {
            System.err.println("DAO: ¡Error crítico al listar vehículos!");
            ex.printStackTrace();
            throw ex;
        }
        return lista;
    }

    /**
     * Busca un vehículo por su ID.
     *
     * @param id identificador único del vehículo.
     * @return Vehiculo si existe, o null si no se encontró.
     * @throws SQLException en caso de error en la consulta.
     */
    public Vehiculo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM vehiculos WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Vehiculo(
                    rs.getInt("id"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("color"),
                    rs.getString("propietario")
                );
            }

        } catch (SQLException ex) {
            System.err.println("DAO: Error al buscar vehículo por id → " + ex.getMessage());
            throw ex;
        }
        return null;
    }

    /**
     * Verifica si ya existe un vehículo con una placa específica.
     *
     * @param placa número de placa a validar.
     * @return true si ya existe, false si no.
     * @throws SQLException en caso de error en la consulta.
     */
    public boolean existePlaca(String placa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vehiculos WHERE placa=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException ex) {
            System.err.println("DAO: Error al verificar placa → " + ex.getMessage());
            throw ex;
        }
        return false;
    }

    /**
     * Inserta un nuevo vehículo en la base de datos.
     * 
     * Reglas:
     * <ul>
     *   <li>Debe validarse antes que la placa no exista con {@link #existePlaca(String)}.</li>
     * </ul>
     *
     * @param v vehículo a insertar.
     * @throws SQLException si ocurre un error en la inserción.
     */
    public void agregar(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, color, propietario) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getColor());
            ps.setString(5, v.getPropietario());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("DAO: Error al agregar vehículo → " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Actualiza los datos de un vehículo existente según su ID.
     *
     * @param v objeto {@link Vehiculo} con los datos actualizados.
     * @throws SQLException si ocurre un error en la actualización.
     */
    public void actualizar(Vehiculo v) throws SQLException {
        String sql = "UPDATE vehiculos SET placa=?, marca=?, modelo=?, color=?, propietario=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getColor());
            ps.setString(5, v.getPropietario());
            ps.setInt(6, v.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("DAO: Error al actualizar vehículo → " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Elimina un vehículo de la tabla según su ID.
     *
     * @param id identificador del vehículo a eliminar.
     * @throws SQLException si ocurre un error en la eliminación.
     */
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM vehiculos WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("DAO: Error al eliminar vehículo → " + ex.getMessage());
            throw ex;
        }
    }
}
