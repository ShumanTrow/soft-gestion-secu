package application;

public class Alumno {
    private int idAlumno;
    private String nombreCompleto;
    private String dni;
    private String telefono;
    private String direccion;
    private int socioId; // ID del socio asociado
    private String socioNombre;

    // Constructor con idAlumno (usado cuando recuperas datos desde la BD)
    public Alumno(int idAlumno, String nombreCompleto, String dni, String telefono, String direccion, int socioId, String socioNombre) {
        this.idAlumno = idAlumno;
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.socioId = socioId;
        this.socioNombre = socioNombre;
    }

    // Constructor sin idAlumno (usado cuando se crea un nuevo alumno antes de insertar en la BD)
    public Alumno(String nombreCompleto, String dni, String telefono, String direccion, int socioId, String socioNombre) {
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.socioId = socioId;
        this.socioNombre = socioNombre;
    }

    // Getters y setters
    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
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

    public int getSocioId() {
        return socioId;
    }

    public void setSocioId(int socioId) {
        this.socioId = socioId;
    }

    public String getSocioNombre() {
        return socioNombre;
    }

    public void setSocioNombre(String socioNombre) {
        this.socioNombre = socioNombre;
    }

    // Método toString (útil para depuración)
    @Override
    public String toString() {
        return "Alumno {" +
                "idAlumno=" + idAlumno +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", socioId=" + socioId +
                ", socioNombre='" + socioNombre + '\'' +
                '}';
    }
}
