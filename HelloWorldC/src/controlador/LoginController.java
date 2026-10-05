package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Ines.Carrasco
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesaria
    }    

    /**
     * Manejador del evento del botón Iniciar sesión.
     *
     * @param event Evento de acción enviado por JavaFX
     */
    @FXML
    public void iniciarSesion(ActionEvent event) {
        // Aquí irás implementando la lógica de autenticación
        System.out.println("Intentando iniciar sesión con el usuario: " + txtUsuario.getText());
    }
}