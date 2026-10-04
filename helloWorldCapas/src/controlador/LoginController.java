package controlador;

import dao.Dao;
import dao.DaoBD;
import excepciones.AccesoDatosException;
import modelo.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla de inicio de sesión.
 *
 * <p>Gestiona la introducción de las credenciales del usuario,
 * la autenticación mediante la capa DAO y la apertura de la
 * pantalla principal de la aplicación.</p>
 *
 * @author Iker
 * @version 1.0
 */
public class LoginController {

    /**
     * Campo de texto donde el usuario introduce su nombre de usuario.
     */
    @FXML
    private TextField txtUsuario;

    /**
     * Campo donde el usuario introduce su contraseña.
     */
    @FXML
    private PasswordField txtPassword;

    /**
     * Objeto DAO utilizado para realizar las operaciones
     * de acceso a los datos.
     */
    private final Dao dao = new DaoBD();

    /**
     * Comprueba las credenciales introducidas por el usuario
     * y realiza el inicio de sesión.
     *
     * <p>Si las credenciales son correctas, se abre la pantalla
     * principal de la aplicación. Si son incorrectas o se produce
     * un error de acceso a los datos, se muestra una alerta.</p>
     *
     * @param event evento generado al pulsar el botón de inicio de sesión
     */
    @FXML
    private void iniciarSesion(ActionEvent event) {
        String login = txtUsuario.getText().trim();
        String password = txtPassword.getText();
        if (login.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Datos incompletos","Introduce el usuario y la contraseña."
            );
            return;
        }

        try {
            Usuario usuario = dao.autenticarUsuario(login, password);
            if (usuario == null) {
                mostrarAlerta("Error de acceso","El usuario o la contraseña son incorrectos."
                );
                return;
            }
            abrirPrincipal(usuario);
        } catch (AccesoDatosException e) {
            mostrarAlerta("Error","No se ha podido acceder a la base de datos."
            );
            e.printStackTrace();
        } catch (Exception e) {
            mostrarAlerta("Error","No se ha podido abrir la aplicación."
            );
            e.printStackTrace();
        }
    }

    /**
     * Abre la pantalla principal de la aplicación y le pasa
     * el usuario que ha sido autenticado correctamente.
     *
     * @param usuario usuario autenticado que se enviará
     *                al controlador de la pantalla principal
     * @throws Exception si se produce un error al cargar
     *                   la pantalla principal
     */
    private void abrirPrincipal(Usuario usuario)throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/principal.fxml")
        );
        Scene scene = new Scene(loader.load());
        PrincipalController controller = loader.getController();
        controller.setUsuario(usuario);
        Stage stage = (Stage) txtUsuario.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("WorkApp");
        stage.centerOnScreen();
    }

    /**
     * Muestra una ventana de alerta con un título y un mensaje
     * informativo para el usuario.
     *
     * @param titulo título que aparecerá en la ventana de alerta
     * @param mensaje mensaje que se mostrará al usuario
     */
    private void mostrarAlerta(String titulo,String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}