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
    <title>Gestión de Garaje HOLA jeimy</title>
    
    <style>
        body { font-family: sans-serif; background-color: #f9f9f9; padding: 15px; }
        .container { max-width: 800px; margin: auto; background: #fff; border: 1px solid #ccc; padding: 20px; }
        h1, h2 { text-align: center; color: #333; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;}
        .form-group { margin-bottom: 15px; }
        label { display: block; font-weight: bold; margin-bottom: 5px; }
        input[type="text"], input[type="number"], select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            box-sizing: border-box; /* Asegura que el padding no afecte el ancho total */
        }
        .btn {
            padding: 10px 15px; border: none; font-size: 16px;
            cursor: pointer; color: white; text-decoration: none;
        }
        .btn-primary { background-color: #007bff; }
        .btn-success { background-color: #28a745; }
        .btn-warning { background-color: #ffc107; }
        .btn-danger { background-color: #dc3545; }
        .btn-secondary { background-color: #6c757d; }
        .form-actions { margin-top: 20px; text-align: right; }
        .alert {
            padding: 15px; margin-bottom: 20px; border: 1px solid;
        }
        .alert-danger { background-color: #f8d7da; border-color: #f5c6cb; color: #721c24; }
        .alert-success { background-color: #d4edda; border-color: #c3e6cb; color: #155724; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 8px; border: 1px solid #ddd; text-align: left; }
        th { background-color: #f2f2f2; }
        .actions-cell { text-align: center; }
    </style>
</head>
<body>

<div class="container">
    <h1>Gestión de Garaje</h1>

    <%-- Sección para mostrar mensajes de error o éxito --%>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>

    <div class="form-container">
        <h2><%= modoEdicion ? " Editando Vehículo" : "Registrar Nuevo Vehículo" %></h2>
        
        <form action="vehiculos" method="post">
            
            <input type="hidden" name="action" value="<%= modoEdicion ? "update" : "add" %>">
            
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
                    <%-- Se usa c:choose para generar el HTML limpio y evitar errores de validación del IDE --%>
                    <c:choose>
                        <c:when test="${modoEdicion}">
                            <input type="number" id="modelo" name="modelo" value="${vehiculoSeleccionado.modelo}" required placeholder="Ej: 2023">
                        </c:when>
                        <c:otherwise>
                            <input type="number" id="modelo" name="modelo" required placeholder="Ej: 2023">
                        </c:otherwise>
                    </c:choose>
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
                    <a href="vehiculos" class="btn btn-secondary">Cancelar</a>
                </c:if>
                <button type="submit" class="btn <%= modoEdicion ? "btn-success" : "btn-primary" %>">
                    <%= modoEdicion ? "Actualizar Vehículo" : "Agregar Vehículo" %>
                </button>
            </div>
        </form>
    </div>

    <h2>Listado de Vehículos</h2>
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