package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Representa la asignación horaria y de turno de un empleado dentro del modelo.
 * Implementa {@link Serializable} para permitir la persistencia.
 * 
 * @author Inés Carrasco
 * @version 1.0
 */
public class Horario implements Serializable {

    
    private static final long serialVersionUID = 1L;

    
    private int idHorario;
    private String idEmpl;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String turno;

    /**
     * Constructor por defecto sin parámetros.
     */
    public Horario() {
    }

    /**
     * Constructor completo para inicializar Horario con todos sus datos.
     *
     * @param idHorario  Identificador único del registro de horario.
     * @param idEmpl     Identificador del empleado asignado.
     * @param fecha      Fecha de la jornada laboral.
     * @param horaInicio Hora de inicio de la jornada.
     * @param horaFin    Hora de fin de la jornada.
     * @param turno      Turno de trabajo asignado.
     */
    public Horario(int idHorario, String idEmpl, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, String turno) {
        this.idHorario = idHorario;
        this.idEmpl = idEmpl;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.turno = turno;
    }

  
    /**
     * Obtiene el identificador único del registro de horario.
     *
     * @return El ID del horario.
     */
    public int getIdHorario() {
        return idHorario;
    }

    /**
     * Establece el identificador único del registro de horario.
     *
     * @param idHorario El nuevo ID del horario a asignar.
     */
    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    /**
     * Obtiene el identificador del empleado asignado.
     *
     * @return El ID del empleado.
     */
    public String getIdEmpl() {
        return idEmpl;
    }

    /**
     * Establece el identificador del empleado asignado.
     *
     * @param idEmpl El nuevo ID de empleado a asignar.
     */
    public void setIdEmpl(String idEmpl) {
        this.idEmpl = idEmpl;
    }

    /**
     * Obtiene la fecha correspondiente al horario laboral.
     *
     * @return La fecha en formato {@link LocalDate}.
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha correspondiente al horario laboral.
     *
     * @param fecha La nueva fecha a asignar.
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene la hora de inicio de la jornada laboral.
     *
     * @return La hora de inicio en formato {@link LocalTime}.
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * Establece la hora de inicio de la jornada laboral.
     *
     * @param horaInicio La nueva hora de inicio a asignar.
     */
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    /**
     * Obtiene la hora de finalización de la jornada laboral.
     *
     * @return La hora de fin en formato {@link LocalTime}.
     */
    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * Establece la hora de finalización de la jornada laboral.
     *
     * @param horaFin La nueva hora de fin a asignar.
     */
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * Obtiene la descripción del turno de trabajo.
     *
     * @return El nombre del turno.
     */
    public String getTurno() {
        return turno;
    }

    /**
     * Establece la descripción del turno de trabajo.
     *
     * @param turno El nuevo turno a asignar.
     */
    public void setTurno(String turno) {
        this.turno = turno;
    }


     
    /**
     * Devuelve una representación en formato de texto de la asignación horaria.
     *
     * @return Cadena de texto con los detalles del horario.
     */
    @Override
    public String toString() {
        return "Horario{" +
                "idHorario=" + idHorario +
                ", idEmpl='" + idEmpl + '\'' +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", turno='" + turno + '\'' +
                '}';
    }
}