/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conexion.ConexionBD;
import excepciones.AccesoDatosException;
import modelo.Horario;
import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz {@link Dao} que utiliza
 * una base de datos MySQL para almacenar y consultar
 * la información de los usuarios y sus horarios.
 *
 * @author Iker
 */
public class DaoBD implements Dao {

    /**
     * Autentica a un usuario mediante su nombre de usuario
     * y contraseña.
     *
     * @param login nombre de usuario
     * @param password contraseña del usuario
     * @return usuario autenticado o {@code null} si las credenciales
     *         no son correctas
     * @throws AccesoDatosException si se produce un error en la
     *         conexión o consulta a la base de datos
     */
    @Override
    public Usuario autenticarUsuario(String login, String password)
            throws AccesoDatosException {

        String sql = """
                SELECT *
                FROM Usuarios
                WHERE Loggin = ?
                AND Password = ?
                """;

        try {
            Connection conexion =
                    ConexionBD.getInstancia().getConexion();

            PreparedStatement sentencia =
                    conexion.prepareStatement(sql);

            sentencia.setString(1, login);
            sentencia.setString(2, password);

            ResultSet resultado =
                    sentencia.executeQuery();

            if (resultado.next()) {
                return crearUsuario(resultado);
            }

            return null;

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al autenticar el usuario.", e);
        }
    }

    /**
     * Obtiene el superior directo de un empleado.
     *
     * @param idSuperior identificador del superior
     * @return usuario que corresponde al superior o {@code null}
     *         si no existe
     * @throws AccesoDatosException si se produce un error en la consulta
     */
    @Override
    public Usuario obtenerSuperiorDirecto(String idSuperior)
            throws AccesoDatosException {

        if (idSuperior == null || idSuperior.isBlank()) {
            return null;
        }

        String sql = """
                SELECT *
                FROM Usuarios
                WHERE idEmpl = ?
                """;

        try {
            Connection conexion =
                    ConexionBD.getInstancia().getConexion();

            PreparedStatement sentencia =
                    conexion.prepareStatement(sql);

            sentencia.setString(1, idSuperior);

            ResultSet resultado =
                    sentencia.executeQuery();

            if (resultado.next()) {
                return crearUsuario(resultado);
            }

            return null;

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al obtener el superior.", e);
        }
    }

    /**
     * Obtiene todos los empleados que dependen directamente
     * del usuario indicado.
     *
     * @param idSuperior identificador del superior
     * @return lista de empleados subordinados
     * @throws AccesoDatosException si se produce un error en la consulta
     */
    @Override
    public List<Usuario> obtenerSubordinados(String idSuperior)
            throws AccesoDatosException {

        List<Usuario> subordinados =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM Usuarios
                WHERE IdSuperior = ?
                """;

        try {
            Connection conexion =
                    ConexionBD.getInstancia().getConexion();

            PreparedStatement sentencia =
                    conexion.prepareStatement(sql);

            sentencia.setString(1, idSuperior);

            ResultSet resultado =
                    sentencia.executeQuery();

            while (resultado.next()) {

                subordinados.add(
                        crearUsuario(resultado)
                );
            }

            return subordinados;

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al obtener los subordinados.", e);
        }
    }

    /**
     * Modifica los datos de un usuario subordinado.
     *
     * @param usuarioSubordinado usuario cuyos datos se modificarán
     * @throws AccesoDatosException si se produce un error al actualizar
     *         la base de datos
     */
    @Override
    public void modificarDatosSubordinado(
            Usuario usuarioSubordinado)
            throws AccesoDatosException {

        String sql = """
                UPDATE Usuarios
                SET Nombre = ?,
                    Apellido = ?,
                    Email = ?,
                    Direccion = ?,
                    Tlfn = ?,
                    Puesto = ?
                WHERE idEmpl = ?
                """;

        try {
            Connection conexion =
                    ConexionBD.getInstancia().getConexion();

            PreparedStatement sentencia =
                    conexion.prepareStatement(sql);

            sentencia.setString(
                    1,
                    usuarioSubordinado.getNombre()
            );

            sentencia.setString(
                    2,
                    usuarioSubordinado.getApellido()
            );

            sentencia.setString(
                    3,
                    usuarioSubordinado.getEmail()
            );

            sentencia.setString(
                    4,
                    usuarioSubordinado.getDireccion()
            );

            sentencia.setString(
                    5,
                    usuarioSubordinado.getTlfn()
            );

            sentencia.setString(
                    6,
                    usuarioSubordinado.getPuesto()
            );

            sentencia.setString(
                    7,
                    usuarioSubordinado.getIdEmpl()
            );

            sentencia.executeUpdate();

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al modificar el usuario.", e);
        }
    }

    /**
     * Consulta el primer horario disponible de un empleado.
     *
     * @param idEmpl identificador del empleado
     * @return primer horario del empleado o {@code null}
     *         si no existe
     * @throws AccesoDatosException si se produce un error en la consulta
     */
    @Override
    public Horario consultarHorario(String idEmpl)
            throws AccesoDatosException {

        String sql = """
                SELECT *
                FROM Horarios
                WHERE IdEmpl = ?
                ORDER BY Fecha
                LIMIT 1
                """;

        try {
            Connection conexion =
                    ConexionBD.getInstancia().getConexion();

            PreparedStatement sentencia =
                    conexion.prepareStatement(sql);

            sentencia.setString(1, idEmpl);

            ResultSet resultado =
                    sentencia.executeQuery();

            if (resultado.next()) {
                return crearHorario(resultado);
            }

            return null;

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al consultar el horario.", e);
        }
    }

    /**
     * Obtiene todos los horarios de un empleado.
     *
     * @param idEmpl identificador del empleado
     * @return lista de horarios ordenados por fecha
     * @throws AccesoDatosException si se produce un error en la consulta
     */
    @Override
    public List<Horario> obtenerHorarios(String idEmpl)
            throws AccesoDatosException {

        List<Horario> horarios =
                new ArrayList<>();

        String sql = """
                SELECT *
                FROM Horarios
                WHERE IdEmpl = ?
                ORDER BY Fecha
                """;

        try {
            Connection conexion =
                    ConexionBD.getInstancia().getConexion();

            PreparedStatement sentencia =
                    conexion.prepareStatement(sql);

            sentencia.setString(1, idEmpl);

            ResultSet resultado =
                    sentencia.executeQuery();

            while (resultado.next()) {

                horarios.add(
                        crearHorario(resultado)
                );
            }

            return horarios;

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al obtener los horarios.", e);
        }
    }

    /**
     * Modifica un horario perteneciente a un empleado subordinado.
     *
     * @param nuevoHorario horario con los nuevos datos
     * @throws AccesoDatosException si se produce un error al actualizar
     *         la base de datos
     */
    @Override
    public void modificarHorarioSubordinado(
            Horario nuevoHorario)
            throws AccesoDatosException {

        String sql = """
                UPDATE Horarios
                SET Fecha = ?,
                    HoraInicio = ?,
                    HoraFin = ?,
                    Turno = ?
                WHERE IdHorario = ?
                """;

        try {
            Connection conexion =
                    ConexionBD.getInstancia().getConexion();

            PreparedStatement sentencia =
                    conexion.prepareStatement(sql);

            sentencia.setObject(
                    1,
                    nuevoHorario.getFecha()
            );

            sentencia.setObject(
                    2,
                    nuevoHorario.getHoraInicio()
            );

            sentencia.setObject(
                    3,
                    nuevoHorario.getHoraFin()
            );

            sentencia.setString(
                    4,
                    nuevoHorario.getTurno()
            );

            sentencia.setInt(
                    5,
                    nuevoHorario.getIdHorario()
            );

            sentencia.executeUpdate();

        } catch (SQLException e) {
            throw new AccesoDatosException(
                    "Error al modificar el horario.", e);
        }
    }

    /**
     * Modifica los datos personales del usuario que ha iniciado sesión.
     *
     * <p>Se permite modificar el correo electrónico,
     * el teléfono y la contraseña.</p>
     *
     * @param usuario usuario cuyos datos se van a modificar
     * @throws AccesoDatosException si se produce un error al actualizar
     *         la base de datos
     */
    @Override
    public void modificarDatosPropios(Usuario usuario)throws AccesoDatosException {

        String sql = """
                UPDATE Usuarios
                SET Email = ?,
                    Tlfn = ?,
                    Password = ?
                WHERE idEmpl = ?
                """;

        try {
            Connection conexion =
                    ConexionBD.getInstancia().getConexion();

            PreparedStatement sentencia =
                    conexion.prepareStatement(sql);

            sentencia.setString(
                    1,
                    usuario.getEmail()
            );

            sentencia.setString(
                    2,
                    usuario.getTlfn()
            );

            sentencia.setString(
                    3,
                    usuario.getPassword()
            );

            sentencia.setString(
                    4,
                    usuario.getIdEmpl()
            );

            sentencia.executeUpdate();

        } catch (SQLException e) {
            throw new AccesoDatosException("Error al modificar tus datos.", e);
        }
    }

    /**
     * Crea un objeto {@link Usuario} a partir de los datos
     * obtenidos de la base de datos.
     *
     * @param resultado resultado de la consulta SQL
     * @return objeto Usuario creado
     * @throws SQLException si se produce un error al obtener
     *         algún dato del resultado
     */
    private Usuario crearUsuario(ResultSet resultado)throws SQLException {

        return new Usuario(
                resultado.getString("Loggin"),
                resultado.getString("Password"),
                resultado.getString("Nombre"),
                resultado.getString("Apellido"),
                resultado.getString("Email"),
                resultado.getString("Dni"),
                resultado.getString("Direccion"),
                resultado.getString("idEmpl"),
                resultado.getString("Tlfn"),
                resultado.getString("Puesto"),
                resultado.getString("Rol"),
                resultado.getString("IdSuperior")
        );
    }

    /**
     * Crea un objeto {@link Horario} a partir de los datos
     * obtenidos de la base de datos.
     *
     * @param resultado resultado de la consulta SQL
     * @return objeto Horario creado
     * @throws SQLException si se produce un error al obtener
     *         algún dato del resultado
     */
    private Horario crearHorario(ResultSet resultado)
            throws SQLException {

        return new Horario(
                resultado.getInt("IdHorario"),
                resultado.getString("IdEmpl"),
                resultado.getObject(
                        "Fecha",
                        LocalDate.class
                ),
                resultado.getObject(
                        "HoraInicio",
                        LocalTime.class
                ),
                resultado.getObject(
                        "HoraFin",
                        LocalTime.class
                ),
                resultado.getString("Turno")
        );
    }
}