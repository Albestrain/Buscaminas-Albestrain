
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
                if(line.startsWith("TUCOLOR")){
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
                }else if(line.startsWith("TIEMPOAGOTADO")){
                    JOptionPane.showMessageDialog(null, "El tiempo se agotó, nadie puso bandera, hasta la proxima");
                    System.exit(1);
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
                }else if(line.startsWith("EMPATE")){
                    JOptionPane.showMessageDialog(null, "¡ Empate ! Entre--> "+line.substring(6));
                    System.exit(1);
                }else if (line.startsWith("EMPATADOSPERDEDOR")){
                    JOptionPane.showMessageDialog(null, "¡ Perdiste ! Empate entre--> "+line.substring(17));
                    System.exit(1);
                }else if(line.startsWith("FINDELGAME")){
                    tablero.FINDELJUEGO(line.substring(10));
                }else if(line.startsWith("ESPERANDORIVALES")){
                    System.out.println("entro entro");
                    tablero.ponerTitulo(line.substring(16));
                    tablero.ponerTableroEnableFalse(false);
                }else if(line.startsWith("STARD")){
                    tablero.ActualizarBotonesDelTablero();
                    tablero.mostrarTablero();
                    tablero.ladoPorElQueEmpiezas();
                    tablero.ponerTitulo("Buscaminas-. "+MiColor);
                }else if(line.startsWith("EMPEZARPARTIDA")){
                    tablero.ponerTitulo("Empezar partida");
                    if(line.substring(14).equals("4")){
                        out.println("EMPEZAR-5");
                    }
                    tablero.empezar(line.substring(14));
                }else if(line.startsWith("NOVISIBLE")){
                    System.exit(1);
                }else if(line.startsWith("TODOSPERDIERON")){
                    JOptionPane.showMessageDialog(null, "Todos han perdido");
                    System.exit(1);
                }else if(line.startsWith("QUITARMINADEMARCADOR")){
                    tablero.cantidadDeBanderas = tablero.cantidadDeBanderas - Integer.parseInt(line.substring(20));
                }else if(line.startsWith("SOLO")){
                    JOptionPane.showMessageDialog(null, "Todos se han retirado, puedes quedarte a terminar la partida");
                }
                
            }
        } catch(Exception e){
            System.out.println("Tuvimos un problema al intentar conectarlo con el servidor :(");
        }finally {
               
        }
    }
    
    
    
    public static void main(String[] args) throws IOException {
        
        /*if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }*/
        //String ip = JOptionPane.showInputDialog("Ip del servidor:");
        //Jugador_Buscaminas client = new Jugador_Buscaminas(ip);//arg0
        Jugador_Buscaminas client = new Jugador_Buscaminas("127.0.0.1");
        client.run();
    }
    
}
