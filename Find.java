/**
* Find.java
* 13.1.2014
* Purpose : diese klasse durchsucht streams nach ganzen Zahlen
*           (Wertebereich 0 - 2**32-1) und speichert die aus dem
*           Text extrahierten Zahlen in einem int-array. die groesse 
*           des arrays wird als 1. kz-argument uebergeben. es koennen 
*           beliebig viele dateien nacheinander durchsucht werden.
*           alle Zahlen werden im Find - objekt gesammelt
*           und am ende in form einer Tabelle ausgegeben. Die Spalten-
*           breite der Tabelle entspricht der Ziffernanzahl der
*           groessten Zahl + 1 leerzeichen
*           
*           Aufruf:
*           java Find [-h] size [dat1 ... datn]
*           
*           Beispiel:
*zahl: 3444123123 konnte nicht gespeichert werden
*Tabelle der gefundenen Zahlen
*           
*43         515        45         43         3567       24         36         23         
*478        6          59         346        1356887656 266        57         3624       
*487        346        57         345        245        6          6          3          
*3567       4          2134       934        193        4          934        934        
*931        193        934        3498       1943       190843     314        143        
*4          4          43         13         
*/



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Find {

	
	// Attribute
   private static final int EOF = -1;
   private int[] numbers; // Speicher fuer Zahlen
   private int ind;       // aktueller Index
  
    // Methoden
   /**
    * Erzeuge Zahlenspeicher und initialisiere Attribute
    * 
    * @param value
    *          laenge des arrays
    */
   public Find(int value) {
     numbers = new int[value];     // Konstruktion des Feldes
     ind=0;                        // Index der nächsten Zahl
   }

   /**
    * finde ganze Zahlen , pruefe auf NumberFormat exception
    * 
    * @param in
    *          geöffneter Stream
    * @throws IOException
    */
   public void find(BufferedReader in) throws IOException {
     int input;
     StringBuffer numb=null;
     Boolean inNum=false;     // schalter true wenn zahl geparsed wird
     while ((input = in.read()) != EOF) {
        if (Character.isDigit(input) && !inNum) {
           inNum=true;
           numb=new StringBuffer((char)input);//  stringbuffer leeren
        }
        if (Character.isDigit(input) && inNum) {
           numb.append((char)input);  //  zahl aufbauen
        } 
        if (!Character.isDigit(input)&& inNum) {
           // Zahl speichern
           fill(numb.toString());
           inNum=false;          
        }
     }
     // sonderfall letzte ziffer der zahl vor EOF
     if (inNum) {
        fill(numb.toString());
     }
  }
   /**
    * speichert zahl in array wenn platz frei
    * 
    * @param b  zahl im StringBuffer
    */
   private void fill(String b ) {

     if (ind<numbers.length) {
       try {
         numbers[ind] = Integer.parseInt(b.toString());
         ind++;      // index weitersetzen
       } catch (NumberFormatException e) {
         System.err.println("zahl: " + b + 
             " konnte nicht gespeichert werden");
       }
     } else {
       System.err.println("Überlauf....."+ind);
     }

   }
   /**
    *   Ausgabe der Objektdaten in Tabellenform
    *   
    *   @return Tabelle als String
    */
   public String print() {
     String sb = "";
     // maxnum kann hier auch erst ermittelt werden
     int max=0;
     for (int i=0;i<ind;i++) {
    	 if (max < numbers[i]) {
    		 max=numbers[i];
    	 }
     }
     int width=Integer.toString(max).length();
     String fs="%-"+width+"d ";
   //  System.err.println(width);
     String out;
     int col=0;
     sb+="Tabelle der gefundenen Zahlen\n\n";
     for (int i = 0; i < ind; i++) {
     //   System.err.println("i: "+i+"row: "+ col);
        //if ((width+1)*(i-col)>=80){
        if ((width+1)*(i-col+1)>80){
           sb+="\n";
           col=i;
        }   
        out=String.format(fs,numbers[i]);
        sb+=out;
      
     }
     return sb;
   }

   /**
    * Fehlermeldung ausgeben
    * 
    * @param string
    *          Formatstring
    * @param args
    *          optional weitere Argumente
    */
   private static void fehler(String string, Object... args) {
     System.err.print("Fehler: ");
     System.err.printf(string, args);
     System.err.println();
   }

   /**
    * @param args
    *          [-h] size [dateien...]
    */
   public static void main(String[] args) {

     if (args.length == 0 || (args.length > 0 && args[0].equals("-h"))) {
       if (args.length == 0) {
         fehler("mindestens Anzahl muss angegeben werden!");
       }
       System.out
           .println("Finden aller Zahlen und speichern der Zahlen.\n"
               + "Aufruf: java Find [-h] size [dat1 ... datn]");
     } else if (args.length >= 1) {
       Find sin=new Find(Integer.parseInt(args[0]));
       if (args.length > 1) { // files
         for (int i = 1; i < args.length; i++) {
           BufferedReader in;
           try {
             in = new BufferedReader(new FileReader(args[i]));
             sin.find(in);
             in.close();
           } catch (FileNotFoundException e) {
             fehler("Datei %s existiert nicht (%s)", args[i], e
                 .getLocalizedMessage());
           } catch (IOException e) {
             fehler("Ein-/Ausgabefehler bei Datei %s (%s)", args[i], e
                 .getLocalizedMessage());
           }
         }
       } else { // stdin
         try {
           sin.find(new BufferedReader(new InputStreamReader(System.in)));
         } catch (IOException e) {
           fehler("Ein-/Ausgabefehler (%s)", e.getLocalizedMessage());
         }
       }
       System.out.print(sin.print());
     }
   }

}
