# 🚗 Sistema de Gestión de Garaje

Este proyecto es una aplicación web desarrollada en **Java EE** con **JSP/Servlets** y acceso a base de datos mediante **DAO y JDBC**.  
Su objetivo es registrar, listar, actualizar y eliminar vehículos en un garaje.

---

## 📂 Arquitectura del Proyecto

- **Modelo (Model):**  
  `Vehiculo.java` representa la entidad vehículo.  
- **DAO (Data Access Object):**  
  `VehiculoDAO.java` contiene las operaciones CRUD contra la base de datos.  
- **Fachada (Facade):**  
  `VehiculoFacade.java` aplica las reglas de negocio y validaciones.  
- **Controlador (Controller):**  
  `VehiculoServlet.java` recibe las peticiones del usuario y coordina el flujo.  
- **Vista (View):**  
  `vehiculos.jsp` muestra el formulario y la tabla con los vehículos.

---

## 📌 Convenciones de nombres

- Paquetes: `com.garaje.[capa]`  
- Clases: **CamelCase**, comenzando en mayúscula (`VehiculoDAO`, `VehiculoServlet`).  
- Variables y métodos: **camelCase**, comenzando en minúscula (`listarVehiculos`, `buscarPorId`).  

---

## 🔄 Flujo de uso

1. El usuario abre `vehiculos.jsp`.  
2. Desde el formulario puede **agregar** un vehículo.  
3. El `VehiculoServlet` envía la petición a la **fachada**.  
4. La fachada valida las reglas de negocio y delega al **DAO**.  
5. Los datos se guardan en la base de datos y se recarga la lista.  

---

## ⚖️ Reglas de negocio principales

- La **placa** debe ser única (valida en BD y en fachada).  
- El **propietario** debe tener al menos 5 caracteres.  
- El **modelo (año)** no puede ser menor a 20 años de antigüedad.  
- El **color** debe estar dentro de los permitidos (Rojo, Blanco, Negro, Azul, Gris).  

---

## 🛠️ Cómo ejecutar el sistema

1. Clonar el repositorio:
   ```bash
   git clone <https://github.com/ARTEMISAWS/Garaje.git>
