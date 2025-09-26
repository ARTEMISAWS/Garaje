package com.garaje.controller;

import com.garaje.exception.BusinessException;
import com.garaje.facade.VehiculoFacade;
import com.garaje.model.Vehiculo;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/vehiculos")
public class VehiculoServlet extends HttpServlet {

    @EJB
    private VehiculoFacade vehiculoFacade;

    /**
     * Maneja las peticiones GET. Se encarga de mostrar la lista de vehículos
     * y el formulario de edición.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            // Acción para mostrar el formulario de edición
            if ("edit".equals(action)) {
                String idParam = request.getParameter("id");
                //  Se verifica que el ID no sea nulo o vacío
                if (idParam != null && !idParam.isEmpty()) {
                    int id = Integer.parseInt(idParam);
                    Vehiculo vehiculo = vehiculoFacade.buscarPorId(id);
                    //  Se verifica que el vehículo exista
                    if (vehiculo != null) {
                        request.setAttribute("vehiculoSeleccionado", vehiculo);
                    } else {
                        request.setAttribute("error", "Error: No se encontró un vehículo con el ID " + id);
                    }
                } else {
                    request.setAttribute("error", "Error: El ID del vehículo es inválido para la edición.");
                }
            } 
            // Acción para eliminar un vehículo
            else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                vehiculoFacade.eliminar(id);
                request.setAttribute("success", "Vehículo eliminado con éxito.");
            }
            
            // Al final, siempre se recarga la lista de vehículos
            List<Vehiculo> vehiculos = vehiculoFacade.listar();
            request.setAttribute("vehiculos", vehiculos);

        } catch (SQLException | BusinessException | NumberFormatException e) {
            request.setAttribute("error", "Error: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/vehiculos.jsp").forward(request, response);
    }

    /**
     * Maneja las peticiones POST para agregar o actualizar un vehículo.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        try {
            String placa = request.getParameter("placa");
            String marca = request.getParameter("marca");
            String modelo = request.getParameter("modelo");
            String color = request.getParameter("color");
            String propietario = request.getParameter("propietario");

            Vehiculo vehiculo = new Vehiculo();
            vehiculo.setPlaca(placa);
            vehiculo.setMarca(marca);
            vehiculo.setModelo(modelo);
            vehiculo.setColor(color);
            vehiculo.setPropietario(propietario);

            if ("add".equals(action)) {
                vehiculoFacade.agregar(vehiculo);
                request.setAttribute("success", "¡Vehículo agregado con éxito!");
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                vehiculo.setId(id);
                vehiculoFacade.actualizar(vehiculo);
                request.setAttribute("success", "¡Vehículo actualizado con éxito!");
            }

        } catch (SQLException | BusinessException e) {
            request.setAttribute("error", "Error al procesar: " + e.getMessage());
            try {
                // ** Si hay un error, se conservan los datos ingresados en el formulario
                request.setAttribute("vehiculoSeleccionado", request.getParameter("id") != null ? vehiculoFacade.buscarPorId(Integer.parseInt(request.getParameter("id"))) : null);
            } catch (SQLException ex) {
                Logger.getLogger(VehiculoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Ocurrió un error inesperado: " + e.getMessage());
        }
        
        // Se llama a doGet para recargar la página con la lista actualizada y los mensajes.
        response.sendRedirect(request.getContextPath() + "/vehiculos");
    }
}