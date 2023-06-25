/*    */ package es.espai.windowbuilder.Referenciados;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Calendar;
/*    */ import javax.swing.JFormattedTextField;
/*    */ 
/*    */ public class DateLabelFormatter
/*    */   extends JFormattedTextField.AbstractFormatter
/*    */ {
/* 11 */   private String datePattern = "yyyy-dd-MM";
/* 12 */   private SimpleDateFormat dateFormatter = new SimpleDateFormat(this.datePattern);
/*    */   
/*    */   public static String fecha;
/*    */   
/*    */   public Object stringToValue(String text) throws ParseException {
/* 17 */     return this.dateFormatter.parseObject(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public String valueToString(Object value) throws ParseException {
/* 22 */     if (value != null) {
/* 23 */       Calendar cal = (Calendar)value;
/* 24 */       fecha = this.dateFormatter.format(cal.getTime());
/* 25 */       return this.dateFormatter.format(cal.getTime());
/*    */     } 
/*    */     
/* 28 */     return "";
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\Desktop\Referenciados.jar!\es\espai\windowbuilder\DateLabelFormatter.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */