/*    */ package es.espai.windowbuilder.Referenciados;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import org.json.JSONException;
/*    */ import org.json.JSONObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Archivo
/*    */ {
/*    */   public static JSONObject inicializar() throws IOException {
/*    */     try {
/* 22 */       JSONObject application = new JSONObject();
/*    */       
/* 24 */       FileReader f = new FileReader("application.properties");
/* 25 */       BufferedReader b = new BufferedReader(f); String cadena;
/* 26 */       while ((cadena = b.readLine()) != null) {
/* 27 */         String[] c = cadena.split("=");
/* 28 */         application.put(c[0], c[1]);
/*    */       } 
/* 30 */       b.close();
/* 31 */       return application;
/* 32 */     } catch (JSONException jSONException) {
/*    */ 
/*    */       
/* 35 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Admin\Desktop\Referenciados.jar!\es\espai\windowbuilder\Archivo.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */