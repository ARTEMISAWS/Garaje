package com.garaje.facade;

import com.garaje.model.Vehiculo;
import com.garaje.persistence.VehiculoDAO;
import com.garaje.exception.BusinessException; // Importa la excepción personalizada

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Year; // Import para obtener el año actual
import java.util.Arrays;
import java.util.List;


@Stateless
public class VehiculoFacade {

    @Resource(lookup = "jdbc/garajeDB")
    private DataSource ds;

    /**
     * Lista todos los vehículos.
     */
    public List<Vehiculo> listar() throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            return dao.listar();
        }
    }

    /** * Busca un vehículo por id.
     */
    public Vehiculo buscarPorId(int id) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            return dao.buscarPorId(id);
        }
    }

    /**
     * Agrega un vehículo validando todas las reglas de negocio.
     * @param v El vehículo a agregar.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws BusinessException si se viola una regla de negocio.
     */
    public void agregar(Vehiculo v) throws SQLException, BusinessException {
        
        // Regla: Validar campos para simular protección contra SQL Injection[cite: 334].
        validarCamposSospechosos(v);

        // Regla: No aceptar propietario vacío o con menos de 5 caracteres[cite: 328].
        if (v.getPropietario() == null || v.getPropietario().trim().length() < 5) {
            throw new BusinessException("El nombre del propietario es obligatorio y debe tener al menos 5 caracteres.");
        }

        // Regla: La marca, modelo y placa deben tener al menos 3 caracteres[cite: 329].
        if (v.getPlaca().trim().length() < 3 || v.getMarca().trim().length() < 3 || v.getModelo().trim().length() < 3) {
            throw new BusinessException("La placa, la marca y el modelo deben tener al menos 3 caracteres cada uno.");
        }

        // Regla: El color solo acepta valores de una lista predefinida[cite: 330].
        List<String> coloresPermitidos = Arrays.asList("ROJO", "BLANCO", "NEGRO", "AZUL", "GRIS");
        if (v.getColor() == null || !coloresPermitidos.contains(v.getColor().toUpperCase())) {
            throw new BusinessException("El color no es válido. Colores permitidos: Rojo, Blanco, Negro, Azul, Gris.");
        }

        // Regla: No aceptar vehículos con más de 20 años de antigüedad[cite: 331].
        try {
            int anioModelo = Integer.parseInt(v.getModelo());
            int anioActual = Year.now().getValue();
            if (anioModelo < (anioActual - 20)) {
                throw new BusinessException("El modelo del vehículo no puede tener más de 20 años de antigüedad.");
            }
        } catch (NumberFormatException e) {
            throw new BusinessException("El modelo debe ser un año válido (ej: 2023).");
        }
        
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);

            // Regla: No permitir agregar un vehículo con la placa duplicada[cite: 327, 332].
            if (dao.existePlaca(v.getPlaca())) {
                throw new BusinessException("La placa '" + v.getPlaca() + "' ya existe en la base de datos.");
            }
            
            dao.agregar(v);

            // Regla: Al agregar un vehículo con marca "Ferrari", enviar notificación (simulada)[cite: 335].
            if ("Ferrari".equalsIgnoreCase(v.getMarca())) {
                System.out.println("LOG DE NOTIFICACIÓN: ¡Se ha registrado un nuevo Ferrari con placa " + v.getPlaca() + "!");
            }
        }
    }

    /**
     * Actualiza un vehículo existente, validando las reglas de negocio.
     * @param v El vehículo con los datos nuevos.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws BusinessException si se viola una regla de negocio.
     */
    public void actualizar(Vehiculo v) throws SQLException, BusinessException {
        // Regla: Validar campos para simular protección contra SQL Injection[cite: 334].
        validarCamposSospechosos(v);
        
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);

            // Regla: Actualizar solo si el vehículo realmente existe[cite: 333].
            if (dao.buscarPorId(v.getId()) == null) {
                throw new BusinessException("No se puede actualizar el vehículo porque no existe (ID: " + v.getId() + ").");
            }
            
            dao.actualizar(v);
        }
    }

    /** * Elimina un vehículo por id, aplicando reglas de negocio.
     * @param id El ID del vehículo a eliminar.
     * @throws SQLException si ocurre un error en la base de datos.
     * @throws BusinessException si se viola una regla de negocio.
     */
    public void eliminar(int id) throws SQLException, BusinessException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            
            Vehiculo vehiculoAEliminar = dao.buscarPorId(id);

            if (vehiculoAEliminar == null) {
                throw new BusinessException("No se puede eliminar porque el vehículo con ID " + id + " no existe.");
            }

            // Regla: No se puede eliminar un vehículo si el propietario es "Administrador"
            if ("Administrador".equalsIgnoreCase(vehiculoAEliminar.getPropietario())) {
                throw new BusinessException("No se puede eliminar vehiculo de 'Administrador'.");
            }

            dao.eliminar(id);
        }
    }

    /**
     * Método privado para simular una validación de seguridad contra SQL Injection[cite: 334].
     * @param v El vehículo a validar.
     * @throws BusinessException si se detecta un carácter sospechoso.
     */
    private void validarCamposSospechosos(Vehiculo v) throws BusinessException {
        String[] campos = {v.getPlaca(), v.getMarca(), v.getColor(), v.getPropietario()};
        String[] caracteresPeligrosos = {"'", ";", "--", "/*", "*/"};

        for (String campo : campos) {
            if (campo != null) {
                for (String caracter : caracteresPeligrosos) {
                    if (campo.contains(caracter)) {
                        throw new BusinessException("El campo '" + campo + "' contiene caracteres no permitidos por seguridad.");
                    }
                }
            }
        }
    }
}