/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import excepciones.AccesoDatosException;
import java.util.List;
import modelo.Horario;
import modelo.Usuario;

/**
 * Interfaz de la Capa de Acceso a Datos (DAO).
 * Define las operaciones de lectura y modificación de usuarios, jerarquías y horarios.
 * 
 * @author Inés Carrasco
 * @version 1.0
 */
public interface Dao {

    /**
     * Autentica un usuario en el sistema mediante sus credenciales.
     *
     * @param login    Identificador de acceso ingresado.
     * @param password Contraseña ingresada.
     * @return El objeto {@link Usuario} autenticado.
     * @throws AccesoDatosException Si las credenciales son incorrectas o falla el acceso a datos.
     */
    Usuario autenticarUsuario(String login, String password) throws AccesoDatosException;

    /**
     * Obtiene los datos del superior directo de un usuario concreto.
     *
     * @param idSuperior Identificador de empleado del jefe directo.
     * @return Objeto {@link Usuario} del superior o {@code null}.
     * @throws AccesoDatosException Si ocurre un error en la consulta.
     */
    Usuario obtenerSuperiorDirecto(String idSuperior) throws AccesoDatosException;

    /**
     * Obtiene la lista de empleados subordinados que dependen de un superior.
     *
     * @param idSuperior Identificador de empleado del superior.
     * @return Lista de objetos {@link Usuario} asignados a dicho superior.
     * @throws AccesoDatosException Si ocurre un error en la consulta.
     */
    List<Usuario> obtenerSubordinados(String idSuperior) throws AccesoDatosException;

    /**
     * Permite a un superior actualizar datos de un usuario inferior.
     *
     * @param usuarioSubordinado Objeto {@link Usuario} con los datos actualizados.
     * @throws AccesoDatosException Si falla la actualización.
     */
    void modificarDatosSubordinado(Usuario usuarioSubordinado) throws AccesoDatosException;

    /**
     * Consulta el horario asignado a un empleado.
     *
     * @param idEmpl Identificador de empleado.
     * @return Objeto {@link Horario} asignado.
     * @throws AccesoDatosException Si falla la consulta.
     */
    Horario consultarHorario(String idEmpl) throws AccesoDatosException;

    /**
     * Modifica o asigna el horario de un usuario inferior.
     *
     * @param nuevoHorario Objeto {@link Horario} actualizado.
     * @throws AccesoDatosException Si falla la actualización.
     */
    void modificarHorarioSubordinado(Horario nuevoHorario) throws AccesoDatosException;
}