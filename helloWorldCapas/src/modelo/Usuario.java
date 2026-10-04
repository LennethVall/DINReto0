/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java
 */

package modelo;

import java.io.Serializable;

/**
 * Clase que representa la entidad Usuario en el modelo de la aplicación.
 * Implementa {@link Serializable} para permitir la persistencia en archivos de objetos.
 * 
 * @author Inés Carrasco
 * @version 1.0
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String login;
    private String password;
    private String nombre;
    private String apellido;
    private String email; 
    private String dni;
    private String direccion;
    private String idEmpl;
    private String tlfn;
    private String puesto;
    private String rol;
    private String idSuperior; 

    /**
     * Constructor por defecto.
     */
    public Usuario() {
    }

    /**
     * Constructor con todos los campos de la clase Usuario.
     *
     * @param login     Identificador de acceso del usuario.
     * @param password  Contraseña de acceso del usuario.
     * @param nombre    Nombre del usuario.
     * @param apellido  Apellido del usuario.
     * @param email     Dirección de correo electrónico del usuario.
     * @param dni       DNI del usuario.
     * @param direccion Dirección del empleado.
     * @param idEmpl    Identificación de usuario/empleado.
     * @param tlfn      Teléfono de contacto del usuario.
     * @param puesto    Puesto en el que le toca trabajar al usuario.
     * @param rol       Rol asignado al usuario.
     * @param idSuperior Empleado de rango superior.
     */
    public Usuario(String login, String password, String nombre, String apellido, String email, String dni, String direccion, String idEmpl, String tlfn, String puesto, String rol, String idSuperior) {
        this.login = login;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.dni = dni;
        this.direccion = direccion;
        this.idEmpl = idEmpl;
        this.tlfn = tlfn;
        this.puesto = puesto;
        this.rol = rol;
        this.idSuperior = idSuperior;
    }

  

    /**
     * Obtiene el login del usuario.
     * @return El identificador de acceso.
     */
    public String getLogin() {
        return login;
    }

    /**
     * Establece el login del usuario.
     * @param login El identificador de acceso a asignar.
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return La contraseña.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * @param password La contraseña a asignar.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     * @param nombre El nombre a asignar.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     * @return El apellido.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del usuario.
     * @param apellido El apellido a asignar.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * @return El correo electrónico.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * @param email El correo electrónico a asignar.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el DNI del usuario.
     * @return El DNI.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del usuario.
     * @param dni El DNI a asignar.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene la dirección del usuario.
     * @return La dirección.
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del usuario.
     * @param direccion La dirección a asignar.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el ID de empleado del usuario.
     * @return El ID de empleado.
     */
    public String getIdEmpl() {
        return idEmpl;
    }

    /**
     * Establece el ID de empleado del usuario.
     * @param idEmpl El ID de empleado a asignar.
     */
    public void setIdEmpl(String idEmpl) {
        this.idEmpl = idEmpl;
    }

    /**
     * Obtiene el teléfono del usuario.
     * @return El número de teléfono.
     */
    public String getTlfn() {
        return tlfn;
    }

    /**
     * Establece el teléfono del usuario.
     * @param tlfn El teléfono a asignar.
     */
    public void setTlfn(String tlfn) {
        this.tlfn = tlfn;
    }
    
    /**
     * Obtiene el puesto del usuario.
     * @return El puesto a asignar.
     */
    public String getPuesto() { 
        return puesto; 
    }
    
    /**
     * Establece el puesto del usuario.
     * @param puesto El puesto a asignar.
     */
    public void setPuesto(String puesto) {
        this.puesto = puesto; 
    }
    
    /**
     * Obtiene el rol asignado al usuario.
     *
     * @return El rol del usuario.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rol El rol a asignar.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
    
    /**
     * Obtiene el empleado superior del usuario.
     * @return El empleado superior a asignar.
     */
    public String getIdSuperior() {
        return idSuperior; 
    }
    
    /**
     * Establece el empleado duperior del usuario.
     * @param idSuperior El empleado superior a asignar.
     */
    public void setIdSuperior(String idSuperior) {
        this.idSuperior = idSuperior; 
    }



  /**
     * Devuelve una representación en formato de texto de los datos de usuario.
     *
     * @return Cadena de texto con datos de usuario.
     */

    @Override
    public String toString() {
        return "Usuario{" +
                "login='" + login + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", dni='" + dni + '\'' +
                ", idEmpl='" + idEmpl + '\'' +
                ", puesto='" + puesto + '\'' +
                ", rol='" + rol + '\'' +
                ", idSuperior='" + idSuperior + '\'' +
                '}';
    }
}