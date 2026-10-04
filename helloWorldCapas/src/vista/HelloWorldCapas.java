/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

/**
 * Clase principal de la aplicación WorkApp.
 *
 * <p>Se encarga de iniciar la aplicación JavaFX y cargar
 * la pantalla inicial de inicio de sesión.</p>
 *
 * @author Ines.Carrasco
 * @version 1.0
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX WorkApp.
 */
public class HelloWorldCapas extends Application {

    /**
     * Inicia la interfaz gráfica de la aplicación.
     *
     * <p>Carga el archivo FXML correspondiente a la pantalla
     * de inicio de sesión y configura la ventana principal.</p>
     *
     * @param stage ventana principal de la aplicación
     * @throws Exception si se produce un error al cargar
     *                   la interfaz FXML
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("login.fxml")
        );
        Scene scene = new Scene(loader.load());
        stage.setTitle("WorkApp");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método principal que inicia la aplicación JavaFX.
     *
     * @param args argumentos recibidos desde la línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}