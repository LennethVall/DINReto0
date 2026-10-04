package controlador;

import dao.Dao;
import dao.DaoBD;
import excepciones.AccesoDatosException;
import modelo.Horario;
import modelo.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Controlador de la ventana principal de WorkApp.
 *
 * <p>
 * Gestiona la información del usuario autenticado, sus datos personales, su
 * horario y las operaciones relacionadas con sus subordinados.</p>
 *
 * @author Iker
 */
public class PrincipalController {

    /**
     * Etiqueta que muestra el nombre del usuario.
     */
    @FXML
    private Label lblBienvenida;

    /**
     * Etiqueta que muestra el rol del usuario.
     */
    @FXML
    private Label lblRol;

    /**
     * Etiqueta que muestra el puesto del usuario.
     */
    @FXML
    private Label lblPuesto;

    /**
     * Etiqueta que muestra el superior directo.
     */
    @FXML
    private Label lblSuperior;

    /**
     * Etiqueta que muestra el horario del usuario.
     */
    @FXML
    private Label lblHorario;

    /**
     * Etiqueta que muestra las acciones disponibles según el rol del usuario.
     */
    @FXML
    private Label lblAcciones;

    /**
     * Campo donde se muestra y modifica el correo electrónico.
     */
    @FXML
    private TextField txtEmail;

    /**
     * Campo donde se muestra y modifica el teléfono.
     */
    @FXML
    private TextField txtTelefono;

    /**
     * Campo donde se muestra y modifica la contraseña.
     */
    @FXML
    private PasswordField txtPassword;

    /**
     * Botón para consultar los subordinados.
     */
    @FXML
    private Button btnSubordinados;

    /**
     * Título de la sección de subordinados.
     */
    @FXML
    private Label lblSubordinadosTitulo;

    /**
     * Lista de subordinados del usuario.
     */
    @FXML
    private ListView<String> listaSubordinados;

    /**
     * Título de la sección de edición de horarios.
     */
    @FXML
    private Label lblEditarHorario;

    /**
     * Etiqueta que muestra el subordinado seleccionado.
     */
    @FXML
    private Label lblSubordinadoSeleccionado;

    /**
     * Etiqueta que muestra el horario seleccionado.
     */
    @FXML
    private Label lblHorarioSeleccionado;

    /**
     * Panel que contiene los campos de edición del horario.
     */
    @FXML
    private HBox panelHorario;

    /**
     * Selector de fecha del horario.
     */
    @FXML
    private DatePicker dateHorario;

    /**
     * Campo para introducir la hora de inicio.
     */
    @FXML
    private TextField txtHoraInicio;

    /**
     * Campo para introducir la hora de finalización.
     */
    @FXML
    private TextField txtHoraFin;

    /**
     * Selector del turno del empleado.
     */
    @FXML
    private ComboBox<String> comboTurno;

    /**
     * Botón para guardar los cambios del horario.
     */
    @FXML
    private Button btnGuardarHorario;

    /**
     * Usuario que ha iniciado sesión.
     */
    private Usuario usuario;

    /**
     * Objeto encargado de realizar las operaciones de acceso a los datos.
     */
    private final Dao dao = new DaoBD();

    /**
     * Lista de subordinados del usuario actual.
     */
    private List<Usuario> subordinados;

    /**
     * Lista de horarios del subordinado seleccionado.
     */
    private List<Horario> horariosSubordinado;

    /**
     * Subordinado seleccionado actualmente.
     */
    private Usuario subordinadoSeleccionado;

    /**
     * Horario que se está editando actualmente.
     */
    private Horario horarioSeleccionado;

    /**
     * Formato utilizado para mostrar y leer las horas.
     */
    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Inicializa los componentes de la ventana.
     *
     * <p>
     * Configura los turnos disponibles y establece el comportamiento de
     * selección de subordinados.</p>
     */
    @FXML
    public void initialize() {
        comboTurno.getItems().addAll("MAÑANA", "TARDE", "NOCHE");
        listaSubordinados.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int indice = newValue.intValue();
            if (indice >= 0 && subordinados != null && indice < subordinados.size()) {
                seleccionarSubordinado(subordinados.get(indice)
                );
            }
        }
        );
    }

    /**
     * Establece el usuario que ha iniciado sesión.
     *
     * @param usuario usuario autenticado
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        mostrarDatosUsuario();
        mostrarHorario();
        configurarSegunRol();
    }

    /**
     * Muestra los datos principales y personales del usuario en la interfaz.
     */
    private void mostrarDatosUsuario() {
        lblBienvenida.setText("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido());
        lblRol.setText("Rol: " + usuario.getRol());
        lblPuesto.setText("Puesto: " + usuario.getPuesto());
        txtEmail.setText(usuario.getEmail());
        txtTelefono.setText(usuario.getTlfn());
        txtPassword.setText(usuario.getPassword());
        cargarSuperior();
    }

    /**
     * Obtiene y muestra el superior directo del usuario.
     */
    private void cargarSuperior() {
        try {
            if (usuario.getIdSuperior() == null || usuario.getIdSuperior().isBlank()) {
                lblSuperior.setText("Superior: No tiene");
                return;
            }
            Usuario superior = dao.obtenerSuperiorDirecto(usuario.getIdSuperior());
            if (superior != null) {
                lblSuperior.setText("Superior: " + superior.getNombre() + " " + superior.getApellido());
            } else {
                lblSuperior.setText("Superior: No encontrado");
            }
        } catch (AccesoDatosException e) {
            lblSuperior.setText("Superior: Error al consultar");
            e.printStackTrace();
        }
    }

    /**
     * Obtiene y muestra el primer horario del usuario.
     */
    private void mostrarHorario() {
        try {
            Horario horario = dao.consultarHorario(usuario.getIdEmpl());
            if (horario == null) {
                lblHorario.setText("Horario: No hay horario asignado");
                return;
            }
            String fecha = horario.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            lblHorario.setText("Horario: " + fecha + " | " + horario.getHoraInicio() + " - " + horario.getHoraFin() + " | " + horario.getTurno());
        } catch (AccesoDatosException e) {
            lblHorario.setText("Horario: Error al consultar");
            e.printStackTrace();
        }
    }

    /**
     * Configura las opciones disponibles según el rol del usuario autenticado.
     */
    private void configurarSegunRol() {
        String rol = usuario.getRol();
        if (rol == null) {
            return;
        }
        switch (rol.toUpperCase()) {
            case "EMPLEADO":
                lblAcciones.setText("Puedes consultar tus datos, " + "tu superior y tu horario.");
                break;
            case "ENCARGADO":
                mostrarZonaSubordinados();
                lblAcciones.setText("Puedes gestionar los horarios " + "de tus empleados.");
                break;
            case "GERENTE":
                mostrarZonaSubordinados();
                lblAcciones.setText("Puedes gestionar los horarios " + "de tus subordinados.");
                break;
            case "JEFE":
                mostrarZonaSubordinados();
                lblAcciones.setText("Puedes gestionar los horarios " + "de tus subordinados.");
                break;
            default:
                lblAcciones.setText("Rol no reconocido.");
                break;
        }
    }

    /**
     * Hace visible la zona de gestión de subordinados.
     */
    private void mostrarZonaSubordinados() {
        lblSubordinadosTitulo.setVisible(true);
        lblSubordinadosTitulo.setManaged(true);
        btnSubordinados.setVisible(true);
        btnSubordinados.setManaged(true);
    }

    /**
     * Guarda el correo, teléfono y contraseña modificados por el usuario.
     */
    @FXML
    private void guardarDatosPropios() {
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String password = txtPassword.getText();
        if (email.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Datos incompletos", "Debes rellenar correo, teléfono " + "y contraseña.");
            return;
        }
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            mostrarAlerta("Correo incorrecto","Introduce un correo electrónico válido.");
            return;
        }
        if (!telefono.matches("^\\+?[0-9]{8,15}$")) {
            mostrarAlerta("Teléfono incorrecto","Introduce un teléfono válido.");
            return;
        }
        usuario.setEmail(email);
        usuario.setTlfn(telefono);
        usuario.setPassword(password);

        try {
            dao.modificarDatosPropios(usuario);
            mostrarAlerta("Datos guardados","Tus datos se han actualizado correctamente.");
        } catch (AccesoDatosException e) {
            mostrarAlerta("Error","No se han podido guardar tus datos."
            );
            e.printStackTrace();
        }
    }

    /**
     * Obtiene y muestra los subordinados del usuario actual.
     */
    @FXML
    private void verSubordinados() {
        try {
            subordinados = dao.obtenerSubordinados(usuario.getIdEmpl());
            listaSubordinados.getItems().clear();
            if (subordinados.isEmpty()) {
                listaSubordinados.getItems().add("No tienes subordinados.");
            } else {
                for (Usuario subordinado : subordinados) {
                    listaSubordinados.getItems().add(subordinado.getNombre() + " " + subordinado.getApellido() + " - " + subordinado.getRol());
                }
            }
            listaSubordinados.setVisible(true);
            listaSubordinados.setManaged(true);
        } catch (AccesoDatosException e) {
            mostrarAlerta("Error","No se han podido obtener " + "los subordinados.");
            e.printStackTrace();
        }
    }

    /**
     * Selecciona un subordinado y carga sus horarios.
     *
     * @param subordinado empleado subordinado seleccionado
     */
    private void seleccionarSubordinado(Usuario subordinado) {
        subordinadoSeleccionado = subordinado;
        lblEditarHorario.setVisible(true);
        lblEditarHorario.setManaged(true);
        lblSubordinadoSeleccionado.setVisible(true);
        lblSubordinadoSeleccionado.setManaged(true);
        lblSubordinadoSeleccionado.setText("Empleado seleccionado: " + subordinado.getNombre() + " " + subordinado.getApellido());
        cargarHorariosSubordinado();
    }

    /**
     * Obtiene los horarios del subordinado seleccionado.
     */
    private void cargarHorariosSubordinado() {
        try {
            horariosSubordinado = dao.obtenerHorarios(subordinadoSeleccionado.getIdEmpl());
            if (horariosSubordinado.isEmpty()) {
                lblHorarioSeleccionado.setText("No tiene horarios.");
                lblHorarioSeleccionado.setVisible(true);
                lblHorarioSeleccionado.setManaged(true);
                panelHorario.setVisible(false);
                panelHorario.setManaged(false);
                btnGuardarHorario.setVisible(false);
                btnGuardarHorario.setManaged(false);
                return;
            }
            horarioSeleccionado = horariosSubordinado.get(0);
            cargarHorarioEnFormulario();
        } catch (AccesoDatosException e) {
            mostrarAlerta("Error","No se han podido cargar " + "los horarios.");
            e.printStackTrace();
        }
    }

    /**
     * Carga los datos del horario seleccionado en los campos de edición.
     */
    private void cargarHorarioEnFormulario() {
        lblHorarioSeleccionado.setVisible(true);
        lblHorarioSeleccionado.setManaged(true);
        panelHorario.setVisible(true);
        panelHorario.setManaged(true);
        btnGuardarHorario.setVisible(true);
        btnGuardarHorario.setManaged(true);
        dateHorario.setValue(horarioSeleccionado.getFecha());
        txtHoraInicio.setText(horarioSeleccionado.getHoraInicio().format(formatoHora));
        txtHoraFin.setText(horarioSeleccionado.getHoraFin().format(formatoHora));
        comboTurno.setValue(horarioSeleccionado.getTurno());
        lblHorarioSeleccionado.setText("Horario seleccionado: " + horarioSeleccionado.getFecha() + " | " + horarioSeleccionado.getHoraInicio() + " - " + horarioSeleccionado.getHoraFin() + " | " + horarioSeleccionado.getTurno());
    }

    /**
     * Guarda los cambios realizados en el horario del subordinado seleccionado.
     */
    @FXML
    private void guardarHorario() {
        if (horarioSeleccionado == null) {
            mostrarAlerta("Error","No hay ningún horario seleccionado.");
            return;
        }

        if (dateHorario.getValue() == null || txtHoraInicio.getText().isBlank() || txtHoraFin.getText().isBlank() || comboTurno.getValue() == null) {
            mostrarAlerta("Datos incompletos","Rellena todos los datos del horario.");
            return;
        }

        try {
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText(),formatoHora);
            LocalTime horaFin = LocalTime.parse(txtHoraFin.getText(),formatoHora);
            if (!horaFin.isAfter(horaInicio)) {
                mostrarAlerta("Horario incorrecto","La hora de fin debe ser posterior " + "a la hora de inicio.");
                return;
            }
            
            horarioSeleccionado.setFecha(dateHorario.getValue());
            horarioSeleccionado.setHoraInicio(horaInicio);
            horarioSeleccionado.setHoraFin(horaFin);
            horarioSeleccionado.setTurno(comboTurno.getValue());
            dao.modificarHorarioSubordinado(horarioSeleccionado);
            mostrarAlerta("Horario actualizado","El horario se ha modificado correctamente.");
            cargarHorariosSubordinado();

        } catch (DateTimeParseException e) {
            mostrarAlerta("Hora incorrecta","Utiliza el formato HH:mm. " + "Por ejemplo: 08:00");

        } catch (AccesoDatosException e) {
            mostrarAlerta("Error","No se ha podido guardar el horario.");
            e.printStackTrace();
        }
    }

    /**
     * Cierra la sesión actual y vuelve a la pantalla de inicio de sesión.
     */
    @FXML
    private void cerrarSesion() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/login.fxml"));
            Scene scene= new Scene(loader.load());
            Stage stage= (Stage) lblBienvenida.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("WorkApp");
            stage.centerOnScreen();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error","No se ha podido cerrar la sesión.");
        }
    }

    /**
     * Muestra una ventana de información al usuario.
     *
     * @param titulo título de la ventana
     * @param mensaje mensaje que se mostrará
     */
    private void mostrarAlerta(String titulo,String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
