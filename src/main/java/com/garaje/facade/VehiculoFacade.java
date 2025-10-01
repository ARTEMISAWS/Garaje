package com.garaje.facade;

import com.garaje.model.Vehiculo;
import com.garaje.persistence.VehiculoDAO;
import com.garaje.exception.BusinessException;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

/**
 * Fachada (Facade) que expone los métodos principales del negocio para la gestión de vehículos.
 */
@Stateless
public class VehiculoFacade {

    @Resource(lookup = "jdbc/garageDB")
    private DataSource ds;

    public List<Vehiculo> listar() throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            return dao.listar();
        }
    }

    public Vehiculo buscarPorId(int id) throws SQLException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            return dao.buscarPorId(id);
        }
    }

    public void agregar(Vehiculo v) throws SQLException, BusinessException {
        validarCamposSospechosos(v);

        if (v.getPropietario() == null || v.getPropietario().trim().length() < 5) {
            throw new BusinessException("El nombre del propietario debe tener al menos 5 caracteres.");
        }

        if (v.getPlaca().trim().length() < 3 || v.getMarca().trim().length() < 3 || v.getModelo().trim().length() < 3) {
            throw new BusinessException("La placa, marca y modelo deben tener al menos 3 caracteres.");
        }

        List<String> coloresPermitidos = Arrays.asList("ROJO", "BLANCO", "NEGRO", "AZUL", "GRIS");
        if (v.getColor() == null || !coloresPermitidos.contains(v.getColor().toUpperCase())) {
            throw new BusinessException("Color inválido. Permitidos: Rojo, Blanco, Negro, Azul, Gris.");
        }

        try {
            int anioModelo = Integer.parseInt(v.getModelo());
            int anioActual = Year.now().getValue();
            if (anioModelo < (anioActual - 20)) {
                throw new BusinessException("El modelo no puede tener más de 20 años de antigüedad.");
            }
        } catch (NumberFormatException e) {
            throw new BusinessException("El modelo debe ser un año válido (ej: 2023).");
        }

        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);

            if (dao.existePlaca(v.getPlaca())) {
                throw new BusinessException("La placa '" + v.getPlaca() + "' ya existe en la base de datos.");
            }

            dao.agregar(v);

            if ("Ferrari".equalsIgnoreCase(v.getMarca())) {
                System.out.println("🚨 NOTIFICACIÓN: ¡Se ha registrado un Ferrari con placa " + v.getPlaca() + "!");
            }
        }
    }

    public void actualizar(Vehiculo v) throws SQLException, BusinessException {
        validarCamposSospechosos(v);

        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);

            if (dao.buscarPorId(v.getId()) == null) {
                throw new BusinessException("No se puede actualizar, el vehículo con ID " + v.getId() + " no existe.");
            }

            dao.actualizar(v);
        }
    }

    public void eliminar(int id) throws SQLException, BusinessException {
        try (Connection con = ds.getConnection()) {
            VehiculoDAO dao = new VehiculoDAO(con);
            Vehiculo vehiculoAEliminar = dao.buscarPorId(id);

            if (vehiculoAEliminar == null) {
                throw new BusinessException("No se puede eliminar, el vehículo con ID " + id + " no existe.");
            }

            if ("Administrador".equalsIgnoreCase(vehiculoAEliminar.getPropietario())) {
                throw new BusinessException("No se puede eliminar un vehículo de 'Administrador'.");
            }

            dao.eliminar(id);
        }
    }

    private void validarCamposSospechosos(Vehiculo v) throws BusinessException {
        String[] campos = {v.getPlaca(), v.getMarca(), v.getColor(), v.getPropietario()};
        String[] caracteresPeligrosos = {"'", ";", "--", "/*", "*/"};

        for (String campo : campos) {
            if (campo != null) {
                for (String caracter : caracteresPeligrosos) {
                    if (campo.contains(caracter)) {
                        throw new BusinessException("El campo '" + campo + "' contiene caracteres no permitidos.");
                    }
                }
            }
        }
    }
}
