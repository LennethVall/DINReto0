/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 * Excepción personalizada utilizada cuando se introduce
 * un número de teléfono no válido.
 *
 * @author Iker
 * @version 1.0
 */
public class TelefonoInvalidoException extends Exception {

    /**
     * Crea una excepción con un mensaje descriptivo.
     *
     * @param mensaje mensaje que describe el error del teléfono
     */
    public TelefonoInvalidoException(String mensaje) {
        super(mensaje);
    }
}