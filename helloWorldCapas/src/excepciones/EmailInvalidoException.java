package excepciones;

/**
 * Excepción personalizada utilizada cuando se introduce
 * una dirección de correo electrónico no válida.
 *
 * @author Iker
 * @version 1.0
 */
public class EmailInvalidoException extends Exception {

    /**
     * Crea una excepción con un mensaje descriptivo.
     *
     * @param mensaje mensaje que describe el error del correo electrónico
     */
    public EmailInvalidoException(String mensaje) {
        super(mensaje);
    }
}