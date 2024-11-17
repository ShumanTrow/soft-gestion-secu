package application;

import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Cuota {
    private int idCuota;
    private LocalDate fechaCreacion;
    private LocalDate fechaVencimiento;
    private String socioAPagar;
    private String estado;
    private double monto;
    private String alumnoAsociado; // Nuevo campo para el nombre del alumno
    private BooleanProperty pagado; // Propiedad observable para el CheckBox

    // Constructor, getters y setters
    public Cuota(int idCuota, LocalDate fechaCreacion, LocalDate fechaVencimiento, 
                 String socioAPagar, String estado, double monto, String alumnoAsociado) {
        this.idCuota = idCuota;
        this.fechaCreacion = fechaCreacion;
        this.fechaVencimiento = fechaVencimiento;
        this.socioAPagar = socioAPagar;
        this.estado = estado;
        this.monto = monto;
        this.alumnoAsociado = alumnoAsociado;
        this.pagado = new SimpleBooleanProperty("pagada".equals(estado)); // Inicializa según el estado
    }

    public BooleanProperty pagadoProperty() {
        return pagado;
    }

    public boolean isPagado() {
        return pagado.get();
    }

    public void setPagado(boolean pagado) {
        this.pagado.set(pagado);
    }

    public String getAlumnoAsociado() {
        return alumnoAsociado;
    }

    public void setAlumnoAsociado(String alumnoAsociado) {
        this.alumnoAsociado = alumnoAsociado;
    }

    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getSocioAPagar() {
        return socioAPagar;
    }

    public void setSocioAPagar(String socioAPagar) {
        this.socioAPagar = socioAPagar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
