/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package excepciones;

/**
 * Excepción personalizada utilizada para representar errores
 * relacionados con el acceso a los datos de la aplicación.
 *
 * <p>Permite informar de errores producidos durante las operaciones
 * de acceso, consulta o modificación de los datos.</p>
 *
 * @author Iker
 * @version 1.0
 */
public class AccesoDatosException extends Exception {

    /**
     * Crea una excepción con un mensaje descriptivo.
     *
     * @param mensaje mensaje que describe el error producido
     */
    public AccesoDatosException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea una excepción con un mensaje y la causa
     * que originó el error.
     *
     * @param mensaje mensaje que describe el error producido
     * @param causa excepción original que ha provocado el error
     */
    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}