package application;

	//Clase que gestiona todos los datos manejados en relacion a los Socios.
public class Socio {
    private String nombreCompleto;
    private String dni;
    private String telefono;
    private String direccion;
    private String email;

    // Constructor
    public Socio(String nombreCompleto, String dni, String telefono, String direccion, String email) {
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }

    // Getters
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

