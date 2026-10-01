package controlador;

import dao.Dao;
import dao.DaoImpBD;
import excepciones.AccesoDatosException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Horario;
import modelo.Usuario;

/**
 * Controlador para el panel principal adaptativo por rol con gestión de calendario.
 * 
 * @author Inés Carrasco
 */
public class PrincipalController {

    @FXML private Label lblBienvenida, lblRol, lblDatosEncargado;
    @FXML private Button btnEncargado, btnSubordinados, btnModificarDatos, btnAceptarDatos;
    
    // Paneles de vistas
    @FXML private VBox panelDatosPersonales, panelEncargado, panelSubordinados, panelHorario;

    // Campos de datos personales (sin txtLogin)
    @FXML private TextField txtNombre, txtApellido, txtDni, txtPuesto, txtIdEmpl;
    @FXML private TextField txtDireccion, txtTelefono, txtEmail;

    @FXML private ListView<String> listSubordinados;

    // Elementos del Calendario
    @FXML private Label lblMesAnio, lblDiaSeleccionado;
    @FXML private GridPane gridCalendario;
    @FXML private TextField txtHoraInicio, txtHoraFin, txtTurno;
    @FXML private Button btnEditarHorario, btnAceptarHorario;

    private YearMonth mesActual;
    private LocalDate fechaSeleccionada;
    private Usuario usuarioActual;
    private final Dao dao = new DaoImpBD();

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        if (usuarioActual != null) {
            String nombreCompleto = usuarioActual.getNombre() + 
                (usuarioActual.getApellido() != null ? " " + usuarioActual.getApellido() : "");
            
            lblBienvenida.setText("Bienvenido/a, " + nombreCompleto);
            lblRol.setText("Rol: " + usuarioActual.getRol());

            configurarVistaPorRol();
            cargarDatosPersonales();

            mesActual = YearMonth.now();
        }
    }

    private void configurarVistaPorRol() {
        String rol = usuarioActual.getRol() != null ? usuarioActual.getRol().toLowerCase() : "";

        switch (rol) {
            case "jefe":
                btnEncargado.setVisible(false);
                btnEncargado.setManaged(false);
                btnSubordinados.setVisible(true);
                break;

            case "gerente":
            case "encargado":
                btnEncargado.setVisible(true);
                btnSubordinados.setVisible(true);
                break;

            case "empleado":
            default:
                btnEncargado.setVisible(true);
                btnSubordinados.setVisible(false);
                btnSubordinados.setManaged(false);
                break;
        }
    }

    private void cargarDatosPersonales() {
        txtNombre.setText(usuarioActual.getNombre());
        txtApellido.setText(usuarioActual.getApellido());
        txtDni.setText(usuarioActual.getDni());
        txtPuesto.setText(usuarioActual.getPuesto());
        txtIdEmpl.setText(usuarioActual.getIdEmpl());
        txtDireccion.setText(usuarioActual.getDireccion());
        txtTelefono.setText(usuarioActual.getTlfn());
        txtEmail.setText(usuarioActual.getEmail());
    }

    // --- LÓGICA DEL CALENDARIO ---

    @FXML
    private void mostrarHorario() {
        ocultarPaneles();
        panelHorario.setVisible(true);
        cargarCalendario();
    }

    private void cargarCalendario() {
        if (gridCalendario == null) return;
        
        gridCalendario.getChildren().clear();

        String nombreMes = mesActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        if (lblMesAnio != null) {
            lblMesAnio.setText(nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1) + " " + mesActual.getYear());
        }

        String[] diasSemana = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (int i = 0; i < diasSemana.length; i++) {
            gridCalendario.add(new Label(diasSemana[i]), i, 0);
        }

        List<Horario> horariosMes = null;
        try {
            horariosMes = dao.consultarHorariosMes(usuarioActual.getIdEmpl(), mesActual.getYear(), mesActual.getMonthValue());
        } catch (AccesoDatosException e) {
            System.err.println("Error al cargar horarios: " + e.getMessage());
        }

        LocalDate primerDiaMes = mesActual.atDay(1);
        int diaSemanaInicio = primerDiaMes.getDayOfWeek().getValue() - 1;
        int diasEnMes = mesActual.lengthOfMonth();

        int fila = 1;
        int columna = diaSemanaInicio;

        for (int dia = 1; dia <= diasEnMes; dia++) {
            LocalDate fechaDia = mesActual.atDay(dia);
            Button btnDia = new Button(String.valueOf(dia));
            btnDia.setMaxWidth(Double.MAX_VALUE);

            Horario hEncontrado = null;
            if (horariosMes != null) {
                for (Horario h : horariosMes) {
                    if (fechaDia.equals(h.getFecha())) {
                        hEncontrado = h;
                        break;
                    }
                }
            }

            final Horario horarioDia = hEncontrado;
            btnDia.setOnAction(e -> seleccionarDia(fechaDia, horarioDia));

            gridCalendario.add(btnDia, columna, fila);

            columna++;
            if (columna > 6) {
                columna = 0;
                fila++;
            }
        }
    }

    private void seleccionarDia(LocalDate fecha, Horario horario) {
        this.fechaSeleccionada = fecha;
        if (lblDiaSeleccionado != null) {
            lblDiaSeleccionado.setText("Día seleccionado: " + fecha.toString());
        }

        if (horario != null) {
            if (txtHoraInicio != null) txtHoraInicio.setText(horario.getHoraInicio() != null ? horario.getHoraInicio().toString() : "");
            if (txtHoraFin != null) txtHoraFin.setText(horario.getHoraFin() != null ? horario.getHoraFin().toString() : "");
            if (txtTurno != null) txtTurno.setText(horario.getTurno() != null ? horario.getTurno() : "");
        } else {
            if (txtHoraInicio != null) txtHoraInicio.setText("");
            if (txtHoraFin != null) txtHoraFin.setText("");
            if (txtTurno != null) txtTurno.setText("");
        }

        if (txtHoraInicio != null) txtHoraInicio.setEditable(false);
        if (txtHoraFin != null) txtHoraFin.setEditable(false);
        if (txtTurno != null) txtTurno.setEditable(false);
        if (btnAceptarHorario != null) btnAceptarHorario.setDisable(true);
    }

    @FXML
    private void anteriorMes() {
        mesActual = mesActual.minusMonths(1);
        cargarCalendario();
    }

    @FXML
    private void siguienteMes() {
        mesActual = mesActual.plusMonths(1);
        cargarCalendario();
    }

    @FXML
    private void habilitarEdicionHorario() {
        if (fechaSeleccionada != null) {
            if (txtHoraInicio != null) txtHoraInicio.setEditable(true);
            if (txtHoraFin != null) txtHoraFin.setEditable(true);
            if (txtTurno != null) txtTurno.setEditable(true);
            if (btnAceptarHorario != null) btnAceptarHorario.setDisable(false);
        }
    }

    @FXML
    private void guardarHorario() {
        if (fechaSeleccionada == null) return;

        try {
            LocalTime hInicio = (txtHoraInicio != null && !txtHoraInicio.getText().trim().isEmpty()) ? LocalTime.parse(txtHoraInicio.getText().trim()) : null;
            LocalTime hFin = (txtHoraFin != null && !txtHoraFin.getText().trim().isEmpty()) ? LocalTime.parse(txtHoraFin.getText().trim()) : null;
            String turnoText = txtTurno != null ? txtTurno.getText().trim() : "";

            Horario nuevoHorario = new Horario(0, usuarioActual.getIdEmpl(), fechaSeleccionada, hInicio, hFin, turnoText);
            dao.modificarHorarioSubordinado(nuevoHorario);

            if (txtHoraInicio != null) txtHoraInicio.setEditable(false);
            if (txtHoraFin != null) txtHoraFin.setEditable(false);
            if (txtTurno != null) txtTurno.setEditable(false);
            if (btnAceptarHorario != null) btnAceptarHorario.setDisable(true);

            cargarCalendario();
        } catch (Exception e) {
            if (lblDiaSeleccionado != null) lblDiaSeleccionado.setText("Error en formato de hora (Usa HH:mm)");
        }
    }

    // --- NAVEGACIÓN Y DATOS PERSONALES ---

    @FXML
    private void mostrarDatosPersonales() {
        ocultarPaneles();
        panelDatosPersonales.setVisible(true);
    }

    @FXML
    private void mostrarEncargado() {
        ocultarPaneles();
        panelEncargado.setVisible(true);
        try {
            Usuario superior = dao.obtenerSuperiorDirecto(usuarioActual.getIdSuperior());
            if (superior != null) {
                lblDatosEncargado.setText(superior.getNombre() + " " + superior.getApellido() + 
                        " | Email: " + superior.getEmail() + " | Tlfn: " + superior.getTlfn());
            } else {
                lblDatosEncargado.setText("No tiene asignado un superior directo.");
            }
        } catch (AccesoDatosException e) {
            lblDatosEncargado.setText("Error al consultar el superior directo.");
        }
    }

    @FXML
    private void mostrarSubordinados() {
        ocultarPaneles();
        panelSubordinados.setVisible(true);
        listSubordinados.getItems().clear();
        try {
            for (Usuario sub : dao.obtenerSubordinados(usuarioActual.getIdEmpl())) {
                listSubordinados.getItems().add(sub.getIdEmpl() + " - " + sub.getNombre() + " " + sub.getApellido());
            }
        } catch (AccesoDatosException e) {
            listSubordinados.getItems().add("Error al cargar lista de subordinados.");
        }
    }

    @FXML
    private void habilitarEdicionDatos() {
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtEmail.setEditable(true);
        btnAceptarDatos.setDisable(false);
    }

    @FXML
    private void guardarDatosPersonales() {
        usuarioActual.setDireccion(txtDireccion.getText());
        usuarioActual.setTlfn(txtTelefono.getText());
        usuarioActual.setEmail(txtEmail.getText());

        try {
            dao.modificarDatosSubordinado(usuarioActual);
        } catch (AccesoDatosException e) {
            System.err.println("Error al persistir cambios: " + e.getMessage());
        }

        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtEmail.setEditable(false);
        btnAceptarDatos.setDisable(true);
    }

    @FXML private void consultarDatosSubordinado() {}
    @FXML private void consultarHorarioSubordinado() {}

    private void ocultarPaneles() {
        panelDatosPersonales.setVisible(false);
        panelEncargado.setVisible(false);
        panelSubordinados.setVisible(false);
        panelHorario.setVisible(false);
    }

    @FXML
    private void handleCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("WorkApp - Iniciar Sesión");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}