package servidor_buscaminas;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Servidor_Buscaminas {

    static HashMap<Integer, PrintWriter> jugadores = new HashMap<Integer, PrintWriter>();
    static ArrayList<String> colores = new ArrayList<String>();
    ArrayList<String> colorDeJugador = new ArrayList<>();

    static int clave;

    public static void main(String[] args) throws IOException {
        System.out.println("The busca server is running... ");

        Tablero tablero = new Tablero();
        tablero.CrearTablero();
        tablero.ponerleNumerosAlTablero();
        colores = new ArrayList<>(Arrays.asList("green", "red", "blue", "yellow"));
        ServerSocket listener;
        try {
            listener = new ServerSocket(59001);
            while (true) {
                Socket cliente = listener.accept();
                if ( tablero.getArrayClaveDelJugadores().size() == 4 || tablero.isEmpezado()) {
                    tablero = new Tablero();
                    tablero.CrearTablero();
                    tablero.ponerleNumerosAlTablero();
                    colores = new ArrayList<>(Arrays.asList("green", "red", "blue", "yellow"));
                    tablero.getPool().execute(new Handler(cliente, true, tablero));        
                }else{
                    tablero.getPool().execute(new Handler(cliente, true, tablero));
                }
            }
        } catch (Exception ex) {
        }
    }

    private static class Handler implements Runnable {

        private String name;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        private Tablero miTablero;
        private boolean meter;

        public Handler(Socket socket, boolean meter, Tablero tablero) {
            this.socket = socket;
            miTablero = tablero;
            this.meter = meter;
        }

        public void run() {
            try {
            
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
            

                while (true) {

                    synchronized (jugadores) {
                        String coloresDisponibles = "";
                        if (colores != null || colores.size() != 0) {
                            for (int i = 0; i < colores.size(); i++) {
                                coloresDisponibles = coloresDisponibles + colores.get(i) + "-";
                            }
                        }

                        name = "0";
                        //Agrego jugadores
                        String colorElegido = "";
                        colorElegido = colores.get(Integer.parseInt(name));
                        clave++;
                        jugadores.put(clave, out);
                        miTablero.setClaveDelJugadores(clave);
                        miTablero.setColor(colorElegido, clave);
                        miTablero.setColorPrinwiter(clave, out);
  
                        //quitar color de las opciones
                        colores.remove(Integer.parseInt(name));
                        name = colorElegido;
                        if (name == null) {
                            return;
                        }
                        
                        out.println("TUCOLOR" + colorElegido);
                        out.println("TAMANODEMATRIZ" + miTablero.getMedidaDeMatriz());
                        out.println("NUMERODEMINAS" + miTablero.getNumeroDeMinas());
                        out.println("TABLERO" + miTablero.tableroComprimido());
                        if (!meter) {
                            out.println("NOVISIBLE");
                        }
                        break;

                    }
                }

                for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                    if (miTablero.getClaveDelJugadores(i) == miTablero.getColor(name)) {
                        out.println("ACCIONESTe has unido");
                    } else {
                      miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("ACCIONESUn nuevo jugador ha entrado, su color es: " + name);
                    }
                }
                //este while es para esperar jugadores
                System.out.println("Cantidad de rivales: " + miTablero.getArrayClaveDelJugadores().size());
                if (miTablero.getArrayClaveDelJugadores().size() <= 1) {
                    out.println("ESPERANDORIVALESBuscando rivales, espere...");
                } else {
                    for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                        if( miTablero.getArrayClaveDelJugadores().size() < 4){
                            miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("EMPEZARPARTIDA" + miTablero.getArrayClaveDelJugadores().size());
                        }else{
                            miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("EMPEZARPARTIDA");
                        }
                        
                    }
                    out.println("EMPEZARPARTIDA" + miTablero.getArrayClaveDelJugadores().size());
                }

                while (true) {
                    String input = in.nextLine();
                    //Sí alguien le da empezar, le mando mensaje a todos INICIA LA PARTIDA(stard)
                    if (input.equals("EMPEZAR-5")) {
                        miTablero.setEmpezado(true);

                        for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                            miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("STARD");
                        }

                        TimerTask timerTask = new TimerTask(){
                            public void run(){
                                miTablero.explotarBombaDeTiempo();
                            }
                        };

                        Timer timer = new Timer();
                        // Dentro de 0 milisegundos avísame cada 1000 milisegundos
                        timer.scheduleAtFixedRate(timerTask, 0, 10000);

                    }
                    //Sí alguien le da click ejecuta una de estas opciones
                    String[] click = input.split("-");
                    if (click[1].equals("1")) {
                        if (miTablero.clickIzquierdo(click[0], click[2], click[3])) {
                            String perdedor = click[0] + "";
                            int claveDePerdedor = miTablero.getColor(perdedor);
                            miTablero.getColorPrinwiter(claveDePerdedor).println("GAMEOVER" + click[2] + "-" + click[3]);

                            for (PrintWriter writer : miTablero.colorPrinwiter.values()) {
                                writer.println("ACCIONES"+perdedor+" Ha explotado :(");
                            }

                            for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                                miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("ACTUALIZARTABLERO" + miTablero.tableroComprimido());
                            }

                            miTablero.setPerdedores(perdedor);
                        } else {
                            for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                                System.out.println("-> " + jugadores.containsKey(miTablero.getClaveDelJugadores(i)));
                                miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("ACTUALIZARTABLERO" + miTablero.tableroComprimido());
                            }
                        }
                    } else if (click[1].equals("3")) {
                        String[] click3 = input.split(",");
                        if (click3[1].split("-")[0].equals("bandera")) {
                            miTablero.clickDerecho(click3[0].split("-")[0], Integer.parseInt(click3[1].split("-")[1]) + 1, Integer.parseInt(click3[1].split("-")[2]) + 1);
                            for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                                miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("ACTUALIZARTABLERO" + miTablero.tableroComprimido());
                            }
                        } else if (click3[1].split("-")[0].equals("quitarbandera")) {
                            miTablero.clickDerechoquitar(click3[0].split("-")[0], Integer.parseInt(click3[1].split("-")[1]) + 1, Integer.parseInt(click3[1].split("-")[2]) + 1);
                            for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                                miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("ACTUALIZARTABLERO" + miTablero.tableroComprimido());
                            }
                        }
                    }
                    
                    //Todos perdieron :(
                    if (miTablero.getPerdedores().size() == miTablero.getArrayClaveDelJugadores().size()) {
                        for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                            miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("TODOSPERDIERON");
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            } finally {
                
                if (out != null) {
                    miTablero.claveDelJugadores.remove(miTablero.color.get(name));
                    miTablero.colorPrinwiter.remove(miTablero.color.get(name));
                    jugadores.remove(miTablero.color.get(name));
                    colores.add(name);
                    miTablero.color.remove(name);
                    
                    if(miTablero.isEmpezado()){
                        for (PrintWriter writer : miTablero.colorPrinwiter.values()) {
                            writer.println("ACCIONES"+name+" Ha abandonado la partida");
                        }
                        
                        if(miTablero.getArrayClaveDelJugadores().size() == 1){
                            for (PrintWriter writer : miTablero.colorPrinwiter.values()) {
                                writer.println("SOLO");
                            }
                        }
                    }
                    
                }
                if (name != null) {                    
                    System.out.println(" is leaving"); 
                }
                try{
                    socket.close();
                } catch(IOException e){
                    
                }
    }   
    }
}
}


    


