/*    */ package es.espai.windowbuilder.Referenciados;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Pago2
/*    */ {
/*    */   private int idCliente;
/*    */   private String fecha;
/*    */   private String noReferencia;
/*    */   private String importe;
/*    */   private String noAutorizacion;
/*    */   private String noMembresia;
/*    */   private String nombreTitular;
/*    */   private String nombrePrograma;
/*    */   
/*    */   public Pago2(int idCliente, String fecha, String noReferencia, String importe, String noAutorizacion, String noMembresia, String nombreTitular, String nombrePrograma) {
/* 18 */     this.idCliente = idCliente;
/* 19 */     this.fecha = fecha;
/* 20 */     this.noReferencia = noReferencia;
/* 21 */     this.importe = importe;
/* 22 */     this.noAutorizacion = noAutorizacion;
/* 23 */     this.noMembresia = noMembresia;
/* 24 */     this.nombreTitular = nombreTitular;
/* 25 */     this.nombrePrograma = nombrePrograma;
/*    */   }
/*    */   public String getFecha() {
/* 28 */     return this.fecha;
/*    */   }
/*    */   public void setFecha(String fecha) {
/* 31 */     this.fecha = fecha;
/*    */   }
/*    */   public String getNoReferencia() {
/* 34 */     return this.noReferencia;
/*    */   }
/*    */   public void setNoReferencia(String noReferencia) {
/* 37 */     this.noReferencia = noReferencia;
/*    */   }
/*    */   public String getImporte() {
/* 40 */     return this.importe;
/*    */   }
/*    */   public void setImporte(String importe) {
/* 43 */     this.importe = importe;
/*    */   }
/*    */   public String getNoAutorizacion() {
/* 46 */     return this.noAutorizacion;
/*    */   }
/*    */   public void setNoAutorizacion(String noAutorizacion) {
/* 49 */     this.noAutorizacion = noAutorizacion;
/*    */   }
/*    */   public String getNombreTitular() {
/* 52 */     return this.nombreTitular;
/*    */   }
/*    */   public void setNombreTitular(String nombreTitular) {
/* 55 */     this.nombreTitular = nombreTitular;
/*    */   }
/*    */   public String getNoMembresia() {
/* 58 */     return this.noMembresia;
/*    */   }
/*    */   public void setNoMembresia(String noMembresia) {
/* 61 */     this.noMembresia = noMembresia;
/*    */   }
/*    */   
/*    */   public int getIdCliente() {
/* 65 */     return this.idCliente;
/*    */   }
/*    */   public void setIdCliente(int idCliente) {
/* 68 */     this.idCliente = idCliente;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNombrePrograma() {
/* 73 */     return this.nombrePrograma;
/*    */   }
/*    */   public void setNombrePrograma(String nombrePrograma) {
/* 76 */     this.nombrePrograma = nombrePrograma;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 80 */     return "Pago2 [idCliente=" + this.idCliente + ", fecha=" + this.fecha + ", noReferencia=" + this.noReferencia + ", importe=" + 
/* 81 */       this.importe + ", noAutorizacion=" + this.noAutorizacion + ", noMembresia=" + this.noMembresia + ", nombreTitular=" + 
/* 82 */       this.nombreTitular + ", nombrePrograma=" + this.nombrePrograma + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\Desktop\Referenciados.jar!\es\espai\windowbuilder\Pago2.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */