/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import excepciones.AccesoDatosException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestor Singleton para la conexión con la base de datos relacional WorkApp.
 * Carga los parámetros de acceso dinámicamente desde el archivo config.properties.
 * 
 * @author Inés Carrasco
 * @version 1.0
 */
public class ConexionBD {

    private static ConexionBD instancia;
    private Connection conexion;

    /**
     * Constructor privado para forzar el patrón Singleton.
     * Carga el driver JDBC y establece la conexión leyendo config.properties.
     * 
     * @throws AccesoDatosException Si falla la carga del driver o la conexión con WorkApp.
     */
    private ConexionBD() throws AccesoDatosException {
        Properties props = new Properties();
        try (InputStream input = ConexionBD.class.getClassLoader().getResourceAsStream("config/config.properties")) {
            if (input == null) {
                throw new AccesoDatosException("No se encontró el archivo de configuración config.properties", null);
            }
            props.load(input);

            String url = props.getProperty("db.url");
            String usuario = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, usuario, password);

        } catch (ClassNotFoundException e) {
            throw new AccesoDatosException("No se encontró el driver JDBC de MySQL", e);
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al conectar con la base de datos WorkApp", e);
        } catch (Exception e) {
            throw new AccesoDatosException("Error al leer el archivo de configuración", e);
        }
    }

    /**
     * Obtiene la instancia única de ConexionBD.
     *
     * @return La instancia Singleton de {@link ConexionBD}.
     * @throws AccesoDatosException Si ocurre un error al abrir la conexión.
     */
    public static synchronized ConexionBD getInstancia() throws AccesoDatosException {
        if (instancia == null || instancia.conexionCerrada()) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    /**
     * Comprueba si la conexión actual se encuentra cerrada o es nula.
     *
     * @return {@code true} si la conexión está cerrada o nula, {@code false} en caso contrario.
     */
    private boolean conexionCerrada() {
        try {
            return conexion == null || conexion.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * Obtiene el objeto {@link Connection} activo.
     *
     * @return La conexión SQL activa con WorkApp.
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Cierra la conexión activa con la base de datos WorkApp.
     *
     * @throws AccesoDatosException Si ocurre un error al intentar cerrar la conexión.
     */
    public void cerrar() throws AccesoDatosException {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al cerrar la conexión con la base de datos WorkApp", e);
        }
    }
}