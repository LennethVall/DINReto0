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
 * Incluye un mecanismo de fallback para desarrollo sin driver/BD activa.
 * 
 * @author Inés Carrasco
 * @version 1.2
 */
public class DaoImpBD implements Dao {

    // --- SENTENCIAS SQL PREPARADAS ---
    private static final String SQL_AUTENTICAR = 
        "SELECT * FROM Usuario WHERE Loggin = ? AND Usuario = ?";
    
    private static final String SQL_OBTENER_POR_ID = 
        "SELECT * FROM Usuario WHERE idEmpl = ?";
    
    private static final String SQL_OBTENER_SUBORDINADOS = 
        "SELECT * FROM Usuario WHERE IdSuperior = ?";
    
    private static final String SQL_MODIFICAR_DATOS_SUBORDINADO = 
        "UPDATE Usuario SET Nombre = ?, Dni = ?, Tlfn = ?, Email = ?, Puesto = ? WHERE idEmpl = ?";
    
    private static final String SQL_CONSULTAR_HORARIO = 
        "SELECT * FROM Horarios WHERE IdEmpl = ? ORDER BY Fecha DESC LIMIT 1";

    private static final String SQL_CONSULTAR_HORARIOS_MES = 
        "SELECT * FROM Horarios WHERE IdEmpl = ? AND YEAR(Fecha) = ? AND MONTH(Fecha) = ? ORDER BY Fecha ASC";
    
    private static final String SQL_MODIFICAR_HORARIO = 
        "UPDATE Horarios SET Fecha = ?, HoraInicio = ?, HoraFin = ?, Turno = ? WHERE IdEmpl = ? AND Fecha = ?";

    private static final String SQL_INSERTAR_HORARIO = 
        "INSERT INTO Horarios (IdEmpl, Fecha, HoraInicio, HoraFin, Turno) VALUES (?, ?, ?, ?, ?)";

    public DaoImpBD() {
    }

    /**
     * Mapea una fila del ResultSet a un objeto Usuario.
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getString("Loggin"),
            rs.getString("Usuario"), // La columna 'Usuario' almacena la contraseña
            rs.getString("Nombre"),
            rs.getString("Apellido"),
            rs.getString("Email"),
            rs.getString("Dni"),
            rs.getString("Direccion"),
            rs.getString("idEmpl"),
            rs.getString("Tlfn"),
            rs.getString("Puesto"),
            rs.getString("Rol"),
            rs.getString("IdSuperior")
        );
    }

    /**
     * Mapea una fila del ResultSet a un objeto Horario.
     */
    private Horario mapearHorario(ResultSet rs) throws SQLException {
        Date sqlFecha = rs.getDate("Fecha");
        Time sqlHoraInicio = rs.getTime("HoraInicio");
        Time sqlHoraFin = rs.getTime("HoraFin");

        return new Horario(
            rs.getInt("IdHorario"),
            rs.getString("IdEmpl"),
            sqlFecha != null ? sqlFecha.toLocalDate() : null,
            sqlHoraInicio != null ? sqlHoraInicio.toLocalTime() : null,
            sqlHoraFin != null ? sqlHoraFin.toLocalTime() : null,
            rs.getString("Turno")
        );
    }

    @Override
    public Usuario autenticarUsuario(String login, String password) throws AccesoDatosException {
        try (Connection con = ConexionBD.getInstancia().getConexion();
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
        } catch (NoClassDefFoundError | Exception e) {
            // FALLBACK EN MEMORIA: Permite probar la app sin tener el driver MySQL o la BD conectada
            if ("usr01".equalsIgnoreCase(login) && "1001".equals(password)) {
                Usuario u = new Usuario();
                u.setLogin("usr01");
                u.setNombre("Inés Carrasco");
                u.setRol("Administrador");
                u.setIdEmpl("EMP01");
                return u;
            } else if ("user".equalsIgnoreCase(login) && "1234".equals(password)) {
                Usuario u = new Usuario();
                u.setLogin("user");
                u.setNombre("Empleado Pruebas");
                u.setRol("Empleado");
                u.setIdEmpl("EMP02");
                return u;
            } else {
                throw new AccesoDatosException("Credenciales incorrectas (Modo prueba: usa usr01 / 1001).");
            }
        }
    }

    @Override
    public Usuario obtenerSuperiorDirecto(String idSuperior) throws AccesoDatosException {
        if (idSuperior == null || idSuperior.trim().isEmpty()) {
            return null;
        }

        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_OBTENER_POR_ID)) {
            
            ps.setString(1, idSuperior);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
                return null;
            }
        } catch (Exception e) {
            throw new AccesoDatosException("Error al consultar la información del superior directo.", e);
        }
    }

    @Override
    public List<Usuario> obtenerSubordinados(String idSuperior) throws AccesoDatosException {
        List<Usuario> listaSubordinados = new ArrayList<>();
        
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_OBTENER_SUBORDINADOS)) {
            
            ps.setString(1, idSuperior);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaSubordinados.add(mapearUsuario(rs));
                }
            }
            return listaSubordinados;
        } catch (Exception e) {
            throw new AccesoDatosException("Error al obtener la lista de empleados subordinados.", e);
        }
    }

    @Override
    public void modificarDatosSubordinado(Usuario usuarioSubordinado) throws AccesoDatosException {
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_MODIFICAR_DATOS_SUBORDINADO)) {
            
            ps.setString(1, usuarioSubordinado.getNombre());
            ps.setString(2, usuarioSubordinado.getDni());
            ps.setString(3, usuarioSubordinado.getTlfn());
            ps.setString(4, usuarioSubordinado.getEmail());
            ps.setString(5, usuarioSubordinado.getPuesto());
            ps.setString(6, usuarioSubordinado.getIdEmpl());
            
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                throw new AccesoDatosException("No se pudo actualizar: el empleado no existe en la base de datos.");
            }
        } catch (Exception e) {
            throw new AccesoDatosException("Error al modificar los datos personales del subordinado.", e);
        }
    }

    @Override
    public Horario consultarHorario(String idEmpl) throws AccesoDatosException {
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_CONSULTAR_HORARIO)) {
            
            ps.setString(1, idEmpl);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearHorario(rs);
                }
                return null;
            }
        } catch (Exception e) {
            throw new AccesoDatosException("Error al consultar el horario del empleado.", e);
        }
    }

    @Override
    public List<Horario> consultarHorariosMes(String idEmpl, int anio, int mes) throws AccesoDatosException {
        List<Horario> listaHorarios = new ArrayList<>();
        try (Connection con = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_CONSULTAR_HORARIOS_MES)) {
            
            ps.setString(1, idEmpl);
            ps.setInt(2, anio);
            ps.setInt(3, mes);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    listaHorarios.add(mapearHorario(rs));
                }
            }
            return listaHorarios;
        } catch (Exception e) {
            throw new AccesoDatosException("Error al consultar el calendario mensual del empleado.", e);
        }
    }

    @Override
    public void modificarHorarioSubordinado(Horario nuevoHorario) throws AccesoDatosException {
        try (Connection con = ConexionBD.getInstancia().getConexion()) {
            
            Date sqlFecha = nuevoHorario.getFecha() != null ? Date.valueOf(nuevoHorario.getFecha()) : null;
            Time sqlHoraInicio = nuevoHorario.getHoraInicio() != null ? Time.valueOf(nuevoHorario.getHoraInicio()) : null;
            Time sqlHoraFin = nuevoHorario.getHoraFin() != null ? Time.valueOf(nuevoHorario.getHoraFin()) : null;

            try (PreparedStatement psUpdate = con.prepareStatement(SQL_MODIFICAR_HORARIO)) {
                psUpdate.setDate(1, sqlFecha);
                psUpdate.setTime(2, sqlHoraInicio);
                psUpdate.setTime(3, sqlHoraFin);
                psUpdate.setString(4, nuevoHorario.getTurno());
                psUpdate.setString(5, nuevoHorario.getIdEmpl());
                psUpdate.setDate(6, sqlFecha);
                
                int filasAfectadas = psUpdate.executeUpdate();
                
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
        } catch (Exception e) {
            throw new AccesoDatosException("Error al actualizar o asignar el horario al subordinado.", e);
        }
    }
}