package com.garaje.exception;

/**
 * Excepción personalizada para representar errores relacionados con
 * las reglas de negocio de la aplicación Garaje.
 *
 * Su objetivo principal es proporcionar una forma clara y estructurada
 * de notificar a las capas superiores (por ejemplo, el Servlet) cuando
 * se incumple una regla de negocio definida en la lógica de la aplicación.
 *
 * Ejemplos de uso en esta aplicación:
 *  - Intentar actualizar un vehículo que no existe.
 *  - Intentar registrar una placa duplicada.
 *  - Intentar registrar un vehículo Ferrari (regla especial).
 */
public class BusinessException extends Exception {

    /**
     * Constructor que recibe un mensaje de error personalizado.
     *
     * @param message Descripción detallada de la violación de la regla de negocio.
     */
    public BusinessException(String message) {
        super(message);
    }
}
