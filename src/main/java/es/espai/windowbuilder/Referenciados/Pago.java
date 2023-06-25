package es.espai.windowbuilder.Referenciados;
 
import javax.swing.JLabel;

public class Pago
extends JLabel
{
   private int idCliente;
   private String fecha;
   private String fechapago;
   private String noReferencia;
   private String importe; 
   private String noAutorizacion; 
   private String noMembresia; 
   private String nombreTitular;

   public void setFecha(String fecha) {
    this.fecha = fecha;
   }

   public String getFecha() {
     return this.fecha;
   }
   
   public void setFechaPago(String fechapago) {
     this.fechapago = fechapago;
   }

   public String getFechaPago() {
    return this.fechapago;
   }

   public String getNoReferencia() {
     return this.noReferencia;
   }

   public void setNoReferencia(String noReferencia) {
     this.noReferencia = noReferencia;
   }
   
   public String getImporte() {
     return this.importe;
   }
   
   public void setImporte(String importe) {
     this.importe = importe;
   }
   
   public String getNoAutorizacion() {
     return this.noAutorizacion;
   }
   
   public void setNoAutorizacion(String noAutorizacion) {
     this.noAutorizacion = noAutorizacion;
   }
   
   public String getNombreTitular() {
     return this.nombreTitular;
   }
   
   public void setNombreTitular(String nombreTitular) {
     this.nombreTitular = nombreTitular;
   }
   
   public String getNoMembresia() {
     return this.noMembresia;
   }
   
   public void setNoMembresia(String noMembresia) {
     this.noMembresia = noMembresia;
   }
   
   public int getIdCliente() {
     return this.idCliente;
   }
   
   public void setIdCliente(int idCliente) {
     this.idCliente = idCliente;
   }
   
   public String toString() {
     return "Pago [idCliente=" + this.idCliente + ", fecha=" + this.fecha + ", noReferencia=" + this.noReferencia + ", importe=" + this.importe + ", noAutorizacion=" + this.noAutorizacion + ", noMembresia=" + this.noMembresia + 
            ", nombreTitular=" + this.nombreTitular + ", fechaDelPago=" + this.fechapago + "]";
   }
 }
