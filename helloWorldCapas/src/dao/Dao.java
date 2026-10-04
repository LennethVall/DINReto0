package dao;

import excepciones.AccesoDatosException;
import modelo.Horario;
import modelo.Usuario;

import java.util.List;

/**
 * Interfaz que define las operaciones de acceso a los datos
 * de los usuarios y sus horarios.
 *
 * <p>Las implementaciones de esta interfaz permiten trabajar
 * con diferentes sistemas de almacenamiento.</p>
 *
 * @author Iker
 */
public interface Dao {

    /**
     * Autentica a un usuario mediante sus credenciales.
     *
     * @param login nombre de usuario
     * @param password contraseña del usuario
     * @return usuario autenticado o {@code null} si las credenciales
     *         no son correctas
     * @throws AccesoDatosException si se produce un error al acceder
     *         a los datos
     */
    Usuario autenticarUsuario(String login, String password)
            throws AccesoDatosException;

    /**
     * Obtiene el superior directo de un empleado.
     *
     * @param idSuperior identificador del superior
     * @return usuario que corresponde al superior o {@code null}
     *         si no existe
     * @throws AccesoDatosException si se produce un error al acceder
     *         a los datos
     */
    Usuario obtenerSuperiorDirecto(String idSuperior)
            throws AccesoDatosException;

    /**
     * Obtiene los empleados que dependen directamente de un usuario.
     *
     * @param idSuperior identificador del superior
     * @return lista de empleados subordinados
     * @throws AccesoDatosException si se produce un error al acceder
     *         a los datos
     */
    List<Usuario> obtenerSubordinados(String idSuperior)
            throws AccesoDatosException;

    /**
     * Modifica los datos de un usuario subordinado.
     *
     * @param usuarioSubordinado usuario cuyos datos se van a modificar
     * @throws AccesoDatosException si se produce un error al modificar
     *         los datos
     */
    void modificarDatosSubordinado(Usuario usuarioSubordinado)
            throws AccesoDatosException;

    /**
     * Consulta el primer horario disponible de un empleado.
     *
     * @param idEmpl identificador del empleado
     * @return horario del empleado o {@code null} si no existe
     * @throws AccesoDatosException si se produce un error al acceder
     *         a los datos
     */
    Horario consultarHorario(String idEmpl)
            throws AccesoDatosException;

    /**
     * Obtiene todos los horarios de un empleado.
     *
     * @param idEmpl identificador del empleado
     * @return lista de horarios del empleado
     * @throws AccesoDatosException si se produce un error al acceder
     *         a los datos
     */
    List<Horario> obtenerHorarios(String idEmpl)
            throws AccesoDatosException;

    /**
     * Modifica un horario perteneciente a un empleado subordinado.
     *
     * @param nuevoHorario horario con los nuevos datos
     * @throws AccesoDatosException si se produce un error al modificar
     *         el horario
     */
    void modificarHorarioSubordinado(Horario nuevoHorario)
            throws AccesoDatosException;

    /**
     * Modifica los datos personales del usuario que ha iniciado sesión.
     *
     * <p>Los datos que se pueden modificar son el correo electrónico,
     * el teléfono y la contraseña.</p>
     *
     * @param usuario usuario cuyos datos se van a modificar
     * @throws AccesoDatosException si se produce un error al modificar
     *         los datos
     */
    void modificarDatosPropios(Usuario usuario)
            throws AccesoDatosException;
}