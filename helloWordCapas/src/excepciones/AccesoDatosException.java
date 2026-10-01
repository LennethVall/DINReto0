package excepciones;

/**
 * Excepción personalizada para la gestión de errores en la capa de acceso a datos (DAO).
 * Permite envolver excepciones técnicas de infraestructura (SQLException, IOException)
 * y transmitir mensajes descriptivos a las capas superiores de la aplicación.
 * 
 * @author Inés Carrasco
 * @version 1.0
 */
public class AccesoDatosException extends Exception {

    /**
     * Construye una nueva excepción con el mensaje descriptivo especificado.
     *
     * @param mensaje Detalle explicativo sobre el error producido.
     */
    public AccesoDatosException(String mensaje) {
        super(mensaje);
    }

    /**
     * Construye una nueva excepción con el mensaje descriptivo y la causa raíz del error.
     *
     * @param mensaje Detalle explicativo sobre el error producido.
     * @param causa   Excepción original lanzada por el sistema (SQLException, IOException, etc.).
     */
    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}