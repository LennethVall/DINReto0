package dao;

import conexion.ConexionBD;
import excepciones.AccesoDatosException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import modelo.Horario;
import modelo.Usuario;

/**
 * Implementación de la interfaz DAO para la persistencia en Base de Datos MySQL (WorkApp).
 * Adaptada a los modelos Usuario y Horario (LocalDate/LocalTime).
 * 
 * @author Inés Carrasco
 * @version 1.0
 */
public class DaoImpBD implements Dao {

    // --- SENTENCIAS SQL PREPARADAS ---
    private static final String SQL_AUTENTICAR = 
        "SELECT * FROM Usuarios WHERE login = ? AND password = ?";
    
    private static final String SQL_OBTENER_POR_ID = 
        "SELECT * FROM Usuarios WHERE idEmpleado = ?";
    
    private static final String SQL_OBTENER_SUBORDINADOS = 
        "SELECT * FROM Usuarios WHERE idSuperior = ?";
    
    private static final String SQL_MODIFICAR_DATOS_SUBORDINADO = 
        "UPDATE Usuarios SET nombre = ?, dni = ?, tlfn = ?, email = ? WHERE idEmpleado = ?";
    
    private static final String SQL_CONSULTAR_HORARIO = 
        "SELECT * FROM Horarios WHERE idEmpl = ? ORDER BY fecha DESC LIMIT 1";
    
    private static final String SQL_MODIFICAR_HORARIO = 
        "UPDATE Horarios SET fecha = ?, horaInicio = ?, horaFin = ?, turno = ? WHERE idEmpl = ? AND fecha = ?";

    private static final String SQL_INSERTAR_HORARIO = 
        "INSERT INTO Horarios (idEmpl, fecha, horaInicio, horaFin, turno) VALUES (?, ?, ?, ?, ?)";

    public DaoImpBD() {
    }

    /**
     * Mapea una fila del ResultSet de Usuarios a un objeto Usuario.
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getString("idEmpleado"),
            rs.getString("nombre"),
            rs.getString("login"),
            rs.getString("password"),
            rs.getString("dni"),
            rs.getString("tlfn"),
            rs.getString("email"),
            rs.getString("rol"),
            rs.getString("idSuperior")
        );
    }

    /**
     * Mapea una fila del ResultSet de Horarios al objeto Horario de tu modelo.
     */
    private Horario mapearHorario(ResultSet rs) throws SQLException {
        Date sqlFecha = rs.getDate("fecha");
        Time sqlHoraInicio = rs.getTime("horaInicio");
        Time sqlHoraFin = rs.getTime("horaFin");

        return new Horario(
            rs.getInt("idHorario"),
            rs.getString("idEmpl"),
            sqlFecha != null ? sqlFecha.toLocalDate() : null,
            sqlHoraInicio != null ? sqlHoraInicio.toLocalTime() : null,
            sqlHoraFin != null ? sqlHoraFin.toLocalTime() : null,
            rs.getString("turno")
        );
    }

    @Override
    public Usuario autenticarUsuario(String login, String password) throws AccesoDatosException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_AUTENTICAR)) {
            
            ps.setString(1, login);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                } else {
                    throw new AccesoDatosException("Credenciales incorrectas: usuario o contraseña no válidos.");
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al intentar autenticar al usuario en la base de datos.", e);
        }
    }

    @Override
    public Usuario obtenerSuperiorDirecto(String idSuperior) throws AccesoDatosException {
        if (idSuperior == null || idSuperior.trim().isEmpty()) {
            return null;
        }

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_OBTENER_POR_ID)) {
            
            ps.setString(1, idSuperior);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al consultar la información del superior directo.", e);
        }
    }

    @Override
    public List<Usuario> obtenerSubordinados(String idSuperior) throws AccesoDatosException {
        List<Usuario> listaSubordinados = new ArrayList<>();
        
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_OBTENER_SUBORDINADOS)) {
            
            ps.setString(1, idSuperior);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaSubordinados.add(mapearUsuario(rs));
                }
            }
            return listaSubordinados;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al obtener la lista de empleados subordinados.", e);
        }
    }

    @Override
    public void modificarDatosSubordinado(Usuario usuarioSubordinado) throws AccesoDatosException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_MODIFICAR_DATOS_SUBORDINADO)) {
            
            ps.setString(1, usuarioSubordinado.getNombre());
            ps.setString(2, usuarioSubordinado.getDni());
            ps.setString(3, usuarioSubordinado.getTlfn());
            ps.setString(4, usuarioSubordinado.getEmail());
            ps.setString(5, usuarioSubordinado.getIdEmpleado());
            
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new AccesoDatosException("No se pudo actualizar: el empleado no existe en la base de datos.");
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al modificar los datos personales del subordinado.", e);
        }
    }

    @Override
    public Horario consultarHorario(String idEmpl) throws AccesoDatosException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_CONSULTAR_HORARIO)) {
            
            ps.setString(1, idEmpl);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearHorario(rs);
                }
                return null; // No tiene horario asignado
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al consultar el horario del empleado.", e);
        }
    }

    @Override
    public void modificarHorarioSubordinado(Horario nuevoHorario) throws AccesoDatosException {
        try (Connection con = ConexionBD.getConexion()) {
            
            // Convertimos los campos LocalDate y LocalTime a SQL
            Date sqlFecha = nuevoHorario.getFecha() != null ? Date.valueOf(nuevoHorario.getFecha()) : null;
            Time sqlHoraInicio = nuevoHorario.getHoraInicio() != null ? Time.valueOf(nuevoHorario.getHoraInicio()) : null;
            Time sqlHoraFin = nuevoHorario.getHoraFin() != null ? Time.valueOf(nuevoHorario.getHoraFin()) : null;

            // Intentamos modificar el registro existente para esa fecha
            try (PreparedStatement psUpdate = con.prepareStatement(SQL_MODIFICAR_HORARIO)) {
                psUpdate.setDate(1, sqlFecha);
                psUpdate.setTime(2, sqlHoraInicio);
                psUpdate.setTime(3, sqlHoraFin);
                psUpdate.setString(4, nuevoHorario.getTurno());
                psUpdate.setString(5, nuevoHorario.getIdEmpl());
                psUpdate.setDate(6, sqlFecha);
                
                int filasAfectadas = psUpdate.executeUpdate();
                
                // Si no existía horario previo asignado para esa fecha, lo insertamos
                if (filasAfectadas == 0) {
                    try (PreparedStatement psInsert = con.prepareStatement(SQL_INSERTAR_HORARIO)) {
                        psInsert.setString(1, nuevoHorario.getIdEmpl());
                        psInsert.setDate(2, sqlFecha);
                        psInsert.setTime(3, sqlHoraInicio);
                        psInsert.setTime(4, sqlHoraFin);
                        psInsert.setString(5, nuevoHorario.getTurno());
                        psInsert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar o asignar el horario al subordinado.", e);
        }
    }
}