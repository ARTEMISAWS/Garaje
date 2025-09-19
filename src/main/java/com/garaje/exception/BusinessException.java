package com.garaje.exception;

/**
 * Excepción personalizada para representar errores de reglas de negocio.
 * Se utiliza para comunicar problemas de validación desde la fachada
 * hacia el controlador de una manera clara y manejable.
 */
public class BusinessException extends Exception {

    /**
     * Constructor que acepta un mensaje de error.
     * @param message El mensaje que describe la violación de la regla de negocio.
     */
    public BusinessException(String message) {
        super(message);
    }
}