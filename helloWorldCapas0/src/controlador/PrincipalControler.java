/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import dao.Dao;
import dao.DaoImpBD; // O DaoImpFichero según estés probando
import excepciones.AccesoDatosException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import modelo.Usuario;

/**
 * Controlador principal de la aplicación.
 * Conecta la interfaz gráfica con la capa DAO e integra el sistema de logging en tiempo real.
 * 
 * @author Inés Carrasco
 * @version 1.0
 */



/**
 * Controlador principal de la aplicación sin uso de DaoFactory.
 * Instancia directamente la implementación de persistencia activa.
 * 
 * @author Inés Carrasco
 * @version 1.0
 */
public class PrincipalControler implements Initializable {

    @FXML
    private TextArea txtAreaLog; // Conectado con el fx:id de Scene Builder

    private Dao dao;
    private Usuario usuarioActual;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Inicializa el controlador e instancia la implementación del DAO.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Instanciación directa de la implementación de BD
            this.dao = new DaoImpBD(); 
            registrarLog("Capa DAO (Base de Datos) inicializada correctamente.");
        } catch (Exception e) {
            registrarLog("ERROR al conectar con la capa de datos: " + e.getMessage());
        }
    }

    /**
     * Recibe el usuario autenticado desde el login y carga sus datos.
     *
     * @param usuario Instancia del usuario con la sesión activa.
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        registrarLog("Sesión iniciada como: " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        
        cargarDatosSegunJerarquia();
    }

    /**
     * Escribe un mensaje con marca de tiempo en la consola de logging.
     *
     * @param mensaje Texto explicativo del evento o error.
     */
    public void registrarLog(String mensaje) {
        if (txtAreaLog != null) {
            String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
            String lineaLog = "[" + timestamp + "] " + mensaje + "\n";
            Platform.runLater(() -> txtAreaLog.appendText(lineaLog));
        } else {
            System.out.println(mensaje);
        }
    }

    /**
     * Carga la información dependiente del rol y jerarquía del usuario logueado.
     */
    private void cargarDatosSegunJerarquia() {
        if (usuarioActual == null || dao == null) return;

        try {
            if (usuarioActual.esJefe()) {
                registrarLog("Consultando lista de subordinados asignados...");
                List<Usuario> subordinados = dao.obtenerSubordinados(usuarioActual.getIdEmpleado());
                registrarLog("Subordinados cargados: " + subordinados.size() + " registros.");
            } else {
                registrarLog("Cargando horario y datos personales del empleado...");
            }
        } catch (AccesoDatosException e) {
            registrarLog("ERROR de consulta DAO: " + e.getMessage());
        }
    }
}