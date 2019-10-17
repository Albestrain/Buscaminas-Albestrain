
package jugador_buscaminas;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JDialog;
       
//import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Jugador_Buscaminas extends JFrame{
    String serverAddress;
    static String MiColor;
    String click;
    Scanner in;
    static PrintWriter out;
    Tablero tablero;

    public void setOut(String out) {
         this.out.println(out);
    }

    
    
    public  Jugador_Buscaminas(String serverAddress){
         this.serverAddress = serverAddress;
    }

    private int getColor(String disponibes) {
        String[] opciones = disponibes.split("-");
        return JOptionPane.showOptionDialog(rootPane, "Seleccione color disponible", "Elección de color", JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,opciones , rootPane);
    }
    
    final JOptionPane espere = new JOptionPane("Hello world", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                    final JDialog dialog = new JDialog();
                    
    
      private void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            tablero = new Tablero();
            
            dialog.setTitle("Message");
            dialog.setModal(true);
            dialog.setContentPane(espere);
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialog.pack();
            
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("elijeColor")) {
                    out.println(getColor(line.substring(10)));
                }else if(line.startsWith("TUCOLOR")){
                    MiColor = line.substring(7);    
                }if(line.startsWith("TAMANODEMATRIZ")){                   
                    tablero.setTamanoDeMatriz(Integer.parseInt(line.substring(14)));
                }else if(line.startsWith("NUMERODEMINAS")){
                    tablero.setCantidadDeBanderas(Integer.parseInt(line.substring(13)));
                }if(line.startsWith("TABLERO")){
                    tablero.descomprimirTablero(line.substring(7));
                    tablero.crearPantalla();
                    tablero.ActualizarBotonesDelTablero();
                    tablero.mostrarTablero();
                    if(tablero.clickes == 0){
                        tablero.ladoPorElQueEmpiezas();
                    }
                }else if(line.startsWith("ACCIONES")){
                    tablero.getMessageArea().append(line.substring(8) + "\n");
                }if(line.startsWith("ACTUALIZARTABLERO")){
                    tablero.descomprimirTablero(line.substring(17));
                    tablero.ActualizarBotonesDelTablero();
                    tablero.mostrarTablero();
                    if(tablero.clickes == 0){
                        tablero.ladoPorElQueEmpiezas();
                    }
                }else if(line.startsWith("GAMEOVER")){
                    String[] coordenadas = line.substring(8).split("-");
                    tablero.GAMEOVER(Integer.parseInt(coordenadas[0]), Integer.parseInt(coordenadas[1]));
                    JOptionPane.showMessageDialog(null, "GAME OVER, Puedes quedarte a ver la partida");         
                }else if(line.startsWith("MANDAMETUTABLEROPERDEDOR")){
                    System.out.println("Aqui todo bien "+ tablero.mandarMisBombasParaVerficar());
                    out.println(tablero.mandarMisBombasParaVerficar());
                }else if(line.startsWith("FINDELJUEGO")){
                    tablero.FINDELJUEGO(line.substring(11));
                }else if(line.startsWith("GANASTE")){
                    JOptionPane.showMessageDialog(null, "¡ Has Ganado, Felicidades !");
                    System.exit(1);
                }else if(line.startsWith("FINDELGAME")){
                    JOptionPane.showMessageDialog(null, "¡ pERDISTE !");
                }else if(line.startsWith("ESPERANDORIVALES")){
                    System.out.println("entro entro");
                    tablero.ponerTitulo(line.substring(16));
                    tablero.ponerTableroEnableFalse(false);
                }else if(line.startsWith("STARD")){
                    tablero.ActualizarBotonesDelTablero();
                    tablero.mostrarTablero();
                    //tablero.ponerTableroEnableFalse(true);
                    tablero.ladoPorElQueEmpiezas();
                    tablero.ponerTitulo("Buscaminas-. "+MiColor);
                }else if(line.startsWith("EMPEZARPARTIDA")){
                    tablero.ponerTitulo("Empezar partida");
                    if(line.substring(14).equals("4")){
                        out.println("EMPEZAR-5");
                    }else{
                       System.out.println("llego aqui "+ line.substring(14)); 
                    }
                    
                    tablero.empezar(line.substring(14));
                }else if(line.startsWith("NOVISIBLE")){
                    System.out.println("hasta la proxima");
                    System.exit(1);
                }
                
            }
        } finally {
           
        }
    }
    
    
    
    public static void main(String[] args) throws IOException {
        
        /*if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }*/
        Jugador_Buscaminas client = new Jugador_Buscaminas("169.254.142.107");//arg0
        client.run();
        /*Tablero tablero = new Tablero();
        tablero.CrearTablero();
        tablero.crearPantalla();
        tablero.ActualizarBotonesDelTablero();
        tablero.mostrarTablero();
        tablero.ponerleNumerosAlTablero();
        tablero.imprimirTablero();*/
    }
    
}
