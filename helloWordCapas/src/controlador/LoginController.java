/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import dao.Dao;
import dao.DaoImpBD;
import excepciones.AccesoDatosException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Usuario;

/**
 * Controlador para la pantalla de autenticación de usuarios (Login).
 * 
 * @author Inés Carrasco
 * @version 1.0
 */
public class LoginController {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblError;

    private final Dao dao;

    public LoginController() {
        this.dao = new DaoImpBD();
    }

    /**
     * Maneja el evento de autenticación al pulsar el botón "Iniciar Sesión".
     */
 @FXML
    private void handleLogin(ActionEvent event) {
        String login = txtLogin.getText() != null ? txtLogin.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText().trim() : "";

        if (login.isEmpty() || password.isEmpty()) {
            lblError.setStyle("-fx-text-fill: red;");
            lblError.setText("Por favor, introduce usuario y contraseña.");
            return;
        }

        try {
            // Descomentar cuando tengas el driver de MySQL:
            // Usuario usuarioAutenticado = dao.autenticarUsuario(login, password);

            // SIMULACIÓN TEMPORAL (mientras no esté el driver):
            Usuario usuarioAutenticado = new Usuario();
            usuarioAutenticado.setNombre(login);
            usuarioAutenticado.setRol("Administrador");

            // Cargar la nueva vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Principal.fxml"));
            Parent root = loader.load();

            // Pasar el usuario al nuevo controlador
            PrincipalController principalCtrl = loader.getController();
            principalCtrl.setUsuarioActual(usuarioAutenticado);

            // Cambiar la escena en la ventana actual
            Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stageActual.setScene(scene);
            stageActual.setTitle("WorkApp - Panel Principal");
            stageActual.centerOnScreen();

        } catch (IOException e) {
            lblError.setStyle("-fx-text-fill: red;");
            lblError.setText("Error al cargar la vista principal.");
            e.printStackTrace();
        }
    }
}
    