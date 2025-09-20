<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.garaje.model.Vehiculo" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Se recuperan los datos que el Servlet ha colocado en el request --%>
<%
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    List<Vehiculo> vehiculos = (List<Vehiculo>) request.getAttribute("vehiculos");
    Vehiculo vehiculoSeleccionado = (Vehiculo) request.getAttribute("vehiculoSeleccionado");
    String action = request.getParameter("action");
    boolean modoEdicion = vehiculoSeleccionado != null && "edit".equals(action);
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Garaje</title>
    <style>
        body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif; background-color: #f4f7f9; color: #333; margin: 0; padding: 20px; }
        .container { max-width: 900px; margin: 20px auto; background: #fff; padding: 25px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05); }
        h1, h2 { color: #2c3e50; text-align: center; }
        h1 { margin-bottom: 10px; }
        h2 { margin-bottom: 20px; border-bottom: 2px solid #ecf0f1; padding-bottom: 10px; }
        .form-container { background-color: #ecf0f1; padding: 20px; border-radius: 8px; margin-bottom: 30px; }
        .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
        .form-group { display: flex; flex-direction: column; }
        label { font-weight: 600; margin-bottom: 5px; font-size: 14px; }
        input[type="text"], input[type="number"], select {
            padding: 10px; border: 1px solid #bdc3c7; border-radius: 4px; font-size: 16px;
            transition: border-color 0.3s;
        }
        input:focus { border-color: #3498db; outline: none; }
        .btn {
            padding: 10px 15px; border: none; border-radius: 4px; font-size: 16px;
            cursor: pointer; color: white; text-decoration: none; display: inline-block; text-align: center;
        }
        .btn-primary { background-color: #3498db; }
        .btn-success { background-color: #2ecc71; }
        .btn-warning { background-color: #f1c40f; }
        .btn-danger { background-color: #e74c3c; }
        .btn-secondary { background-color: #95a5a6; }
        .form-actions { margin-top: 20px; text-align: right; }
        .alert {
            padding: 15px; margin-bottom: 20px; border-radius: 4px;
            font-weight: bold; text-align: center;
        }
        .alert-danger { background-color: #e74c3c; color: white; }
        .alert-success { background-color: #2ecc71; color: white; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #34495e; color: white; }
        tr:nth-child(even) { background-color: #f2f2f2; }
        tr:hover { background-color: #ecf0f1; }
        .actions-cell { text-align: center; }
    </style>
</head>
<body>

<div class="container">
    <h1>🚗 Gestión de Garaje</h1>

    <%-- Sección para mostrar mensajes de error o éxito --%>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>

    <div class="form-container">
        <h2><%= modoEdicion ? "📝 Editando Vehículo" : "➕ Registrar Nuevo Vehículo" %></h2>
        
        <%-- El formulario envía los datos al servlet 'vehiculos' --%>
        <form action="vehiculos" method="post">
            
            <%-- Campo oculto para manejar la acción (agregar o actualizar) --%>
            <input type="hidden" name="action" value="<%= modoEdicion ? "update" : "add" %>">
            
            <%-- Si estamos en modo edición, incluimos el ID del vehículo --%>
            <c:if test="${modoEdicion}">
                <input type="hidden" name="id" value="${vehiculoSeleccionado.id}">
            </c:if>

            <div class="form-grid">
                <div class="form-group">
                    <label for="placa">Placa</label>
                    <input type="text" id="placa" name="placa" value="${modoEdicion ? vehiculoSeleccionado.placa : ''}" required minlength="3">
                </div>
                <div class="form-group">
                    <label for="marca">Marca</label>
                    <input type="text" id="marca" name="marca" value="${modoEdicion ? vehiculoSeleccionado.marca : ''}" required minlength="3">
                </div>
                <div class="form-group">
                    <label for="modelo">Modelo (Año)</label>
                    
                </div>
                <div class="form-group">
                    <label for="color">Color</label>
                    <select id="color" name="color" required>
                        <% String colorSeleccionado = modoEdicion ? vehiculoSeleccionado.getColor() : ""; %>
                        <option value="" disabled <%= "".equals(colorSeleccionado) ? "selected" : "" %>>Seleccione un color</option>
                        <option value="Rojo" <%= "Rojo".equalsIgnoreCase(colorSeleccionado) ? "selected" : "" %>>Rojo</option>
                        <option value="Blanco" <%= "Blanco".equalsIgnoreCase(colorSeleccionado) ? "selected" : "" %>>Blanco</option>
                        <option value="Negro" <%= "Negro".equalsIgnoreCase(colorSeleccionado) ? "selected" : "" %>>Negro</option>
                        <option value="Azul" <%= "Azul".equalsIgnoreCase(colorSeleccionado) ? "selected" : "" %>>Azul</option>
                        <option value="Gris" <%= "Gris".equalsIgnoreCase(colorSeleccionado) ? "selected" : "" %>>Gris</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="propietario">Propietario</label>
                    <input type="text" id="propietario" name="propietario" value="${modoEdicion ? vehiculoSeleccionado.propietario : ''}" required minlength="5">
                </div>
            </div>

            <div class="form-actions">
                <c:if test="${modoEdicion}">
                    <a href="vehiculos" class="btn btn-secondary">Cancelar Edición</a>
                </c:if>
                <button type="submit" class="btn <%= modoEdicion ? "btn-success" : "btn-primary" %>">
                    <%= modoEdicion ? "Actualizar Vehículo" : "Agregar Vehículo" %>
                </button>
            </div>
        </form>
    </div>

    <h2>📋 Listado de Vehículos</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Placa</th>
                <th>Marca</th>
                <th>Modelo</th>
                <th>Color</th>
                <th>Propietario</th>
                <th class="actions-cell">Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty vehiculos}">
                    <c:forEach var="vehiculo" items="${vehiculos}">
                        <tr>
                            <td>${vehiculo.id}</td>
                            <td>${vehiculo.placa}</td>
                            <td>${vehiculo.marca}</td>
                            <td>${vehiculo.modelo}</td>
                            <td>${vehiculo.color}</td>
                            <td>${vehiculo.propietario}</td>
                            <td class="actions-cell">
                                <a href="vehiculos?action=edit&id=${vehiculo.id}" class="btn btn-warning">Editar</a>
                                <a href="vehiculos?action=delete&id=${vehiculo.id}" class="btn btn-danger" onclick="return confirm('¿Estás seguro de que deseas eliminar este vehículo?');">Eliminar</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="7" style="text-align:center;">No hay vehículos registrados en el garaje.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>

</body>
</html>