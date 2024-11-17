package application;



public class Socio {
	private int id_Socio;  // Cambiar nombre del atributo
    private String nombreCompleto;
    private String dni;
    private String telefono;
    private String direccion;
    private String email;

    // Constructor con id_Socio
    public Socio(int id_Socio, String nombreCompleto, String dni, String telefono, String direccion, String email) {
        this.id_Socio = id_Socio;
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }

    // Constructor sin id_Socio
    public Socio(String nombreCompleto, String dni, String telefono, String direccion, String email) {
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }

    // Getters y Setters
    public int getId_Socio() {
        return id_Socio;
    }

    public void setId_Socio(int id_Socio) {
        this.id_Socio = id_Socio;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

