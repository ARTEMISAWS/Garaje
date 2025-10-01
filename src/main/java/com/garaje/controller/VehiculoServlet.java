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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/vehiculos")
public class VehiculoServlet extends HttpServlet {

    @EJB
    private VehiculoFacade vehiculoFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        try {
            if ("edit".equals(action)) {
                String idParam = request.getParameter("id");
                if (idParam != null && !idParam.isEmpty()) {
                    int id = Integer.parseInt(idParam);
                    Vehiculo vehiculo = vehiculoFacade.buscarPorId(id);
                    if (vehiculo != null) {
                        request.setAttribute("vehiculoSeleccionado", vehiculo);
                    } else {
                        request.setAttribute("error", "No se encontró un vehículo con ID " + id);
                    }
                } else {
                    request.setAttribute("error", "El ID del vehículo es inválido para editar.");
                }
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                vehiculoFacade.eliminar(id);
                session.setAttribute("success", "Vehículo eliminado con éxito.");
                response.sendRedirect(request.getContextPath() + "/vehiculos");
                return;
            }

            // Pasar mensajes de session -> request y limpiarlos
            if (session.getAttribute("success") != null) {
                request.setAttribute("success", session.getAttribute("success"));
                session.removeAttribute("success");
            }
            if (session.getAttribute("error") != null) {
                request.setAttribute("error", session.getAttribute("error"));
                session.removeAttribute("error");
            }

            // Siempre refrescar lista
            List<Vehiculo> vehiculos = vehiculoFacade.listar();
            request.setAttribute("vehiculos", vehiculos);

        } catch (SQLException | BusinessException | NumberFormatException e) {
            request.setAttribute("error", "Error: " + e.getMessage());
        }

        request.getRequestDispatcher("/vehiculos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

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

                if ("Ferrari".equalsIgnoreCase(marca)) {
                    session.setAttribute("success",
                        "¡Vehículo agregado con éxito! 🚨 Notificación: Se registró un Ferrari con placa " + placa);
                } else {
                    session.setAttribute("success", "¡Vehículo agregado con éxito!");
                }

            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                vehiculo.setId(id);
                vehiculoFacade.actualizar(vehiculo);
                session.setAttribute("success", "¡Vehículo actualizado con éxito!");
            }

            response.sendRedirect(request.getContextPath() + "/vehiculos");

        } catch (BusinessException | SQLException e) {
            session.setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/vehiculos");
        } catch (Exception e) {
            session.setAttribute("error", "Ocurrió un error inesperado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/vehiculos");
        }
    }
}
