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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Servidor_Buscaminas {

    static HashMap<Integer, PrintWriter> jugadores = new HashMap<Integer, PrintWriter>();
    //static HashMap<String, PrintWriter> jugadores = new HashMap<String, PrintWriter>();
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
//ExecutorService pool = Executors.newFixedThreadPool(500);
        try {
            listener = new ServerSocket(59001);
            while (true) {
                /*if(tablero != null && tablero.isEmpezado()){
                 for(int i = tablero.getArrayClaveDelJugadores().size(); i < 4; i++){
                 System.out.println("lo metio, o metiaje");
                 tablero.getPool().execute(new Handler(listener.accept(), false));
                 //tablero.getPool().setMaximumPoolSize(tablero.getArrayClaveDelJugadores().size());
                 }
                 }*/
                Socket cliente = listener.accept();
                if ( tablero.getArrayClaveDelJugadores().size() == 4 || tablero.isEmpezado()) {
                    System.out.println("Entra");
                    //System.out.println("Se ha creado un nuevo tablero");
                    tablero = new Tablero();
                    tablero.CrearTablero();
                    tablero.ponerleNumerosAlTablero();
                    colores = new ArrayList<>(Arrays.asList("green", "red", "blue", "yellow"));
                    tablero.getPool().execute(new Handler(cliente, true, tablero));
                    //aqui tengo problema, ya que se reinicia esta madre y ps ya no le mando sms
                    //jugadores = new HashMap<String, PrintWriter>();           
                }else{
                    tablero.getPool().execute(new Handler(cliente, true, tablero));
                }
                

                //tablero.meterJugador(listener);
                //System.out.println("----> "+ tablero.getPool());
                //pool.execute(new Handler(listener.accept()) {});
                //System.out.println("Cantidad de jugadores:--> "+ jugadores.size());
            }
        } catch (Exception ex) {
        }
    }

    private static class Handler implements Runnable {//privado

        private String name, password;
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
                        //out.println("elijeColor"+coloresDisponibles);

                    //recuerda -> name <- es un string, por eso hago todos estos movimientos de strin a integer (el cliente me manda el número de opcion que eligio)
                    name = "0";
                    //Agrego jugadores
                    String colorElegido = "";
                    colorElegido = colores.get(Integer.parseInt(name));
                    clave++;
                    jugadores.put(clave, out);
                    miTablero.setClaveDelJugadores(clave);
                    miTablero.setColor(colorElegido, clave);
                    miTablero.setColorPrinwiter(clave, out);

                    out.println("TUCOLOR" + colorElegido);
                    //quitar color de las opciones
                    colores.remove(Integer.parseInt(name));
                    name = colorElegido;
                    if (name == null) {
                        return;
                    }

                    out.println("TAMANODEMATRIZ" + miTablero.getMedidaDeMatriz());
                    out.println("NUMERODEMINAS" + miTablero.getNumeroDeMinas());
                    //System.out.println(tablero.tableroComprimido());
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
                    //out.println("ESPERANDORIVALES");
                } else {
                    miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("ACCIONESUn nuevo jugador ha entrado, su color es: " + name);
                    //miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("CERRARESPERANDORIVALES");
                }
            }
            //este while es para esperar jugadores
            System.out.println("Cantidad de rivales: " + miTablero.getArrayClaveDelJugadores().size());
            if (miTablero.getArrayClaveDelJugadores().size() <= 1) {
                out.println("ESPERANDORIVALESBuscando rivales, espere...");
            } else {
                for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                    miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("EMPEZARPARTIDA" + miTablero.getArrayClaveDelJugadores().size());
                }

            }

                //empezar partida
                /*for(int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++){     
             miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("EMPEZARPARTIDA"+miTablero.getArrayClaveDelJugadores().size());                    
             }*/
            while (true) {
                String input = in.nextLine();
                if (input.equals("EMPEZAR-5")) {
                    miTablero.setEmpezado(true);
                        //int i = miTablero.getArrayClaveDelJugadores().size();

                        //ExecutorService pool = Executors.newFixedThreadPool(miTablero.getArrayClaveDelJugadores().size());
                    //miTablero.getPool() = Executors.newFixedThreadPool();
                    //miTablero.getPool().execute(new nulaje(socket));
                    //miTablero.setCantidadAceptada(miTablero.getArrayClaveDelJugadores().size());
                    for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                        miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("STARD");
                    }
                }
                System.out.print("no mames " + input);
                String[] click = input.split("-");
                System.out.println("-<-<<-> " + click[1]);
                if (click[1].equals("1")) {
                    if (miTablero.clickIzquierdo(click[0], click[2], click[3])) {
                        String perdedor = click[0] + "";
                        int claveDePerdedor = miTablero.getColor(perdedor);
                        miTablero.getColorPrinwiter(claveDePerdedor).println("GAMEOVER" + click[2] + "-" + click[3]);
                        //miTablero.getColorPrinwiter(claveDePerdedor).println("ACCIONESEl jugador " + perdedor + " ha explotado");
                        for (PrintWriter writer : miTablero.colorPrinwiter.values()) {
                            writer.println("ACCIONES"+perdedor+" Ha explotado :(");
                        }
                        
                        for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                            miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("ACTUALIZARTABLERO" + miTablero.tableroComprimido());
                        }
                        
                        miTablero.setPerdedores(perdedor);
                    } else {
                        //aqui no le mandes a todos cam¿bron mandale solo a los que estan en el table
                        for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                            System.out.println("-> " + jugadores.containsKey(miTablero.getClaveDelJugadores(i)));
                            //System.out.println(miTablero.tableroComprimido());
                            miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("ACTUALIZARTABLERO" + miTablero.tableroComprimido());
                        }
                        /*for (PrintWriter writer : jugadores.values()) {
                         writer.println("ACTUALIZARTABLERO" + tablero.tableroComprimido());
                         }*/
                    }

                    System.out.println("_3");
                    System.out.println("Llego hasta qui");

                } else if (click[1].equals("3")) {
                    System.out.println("Hasta aqui sii");
                    String[] click3 = input.split(",");
                    System.out.println("-ZzZ-> " + click3[0]);
                    System.out.println("2-ZzZ-> " + click3[1]);
                    if (!click3[1].split("-")[0].equals("bandera") && !click3[1].split("-")[0].equals("quitarbandera")) {
                        if (miTablero.EresGanador(click3[1])) {
                            for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                                if (miTablero.getClaveDelJugadores(i) == miTablero.getColor(click3[0].split("-")[0])) {
                                    System.out.println("true");
                                    miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("GANASTE");
                                } else {
                                    System.out.println("false");
                                    miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("FINDELJUEGO" + click3[0].split("-")[0]);
                                }

                            }
                        } else {

                        }
                    } else if (click3[1].split("-")[0].equals("bandera")) {
                        System.out.println("que está pasandp: " + click3[1]);
                        System.out.println("llego aqui----< " + (Integer.parseInt(click3[1].split("-")[1]) + 1) + (Integer.parseInt(click3[1].split("-")[2]) + 1));
                        miTablero.clickDerecho(click3[0].split("-")[0], Integer.parseInt(click3[1].split("-")[1]) + 1, Integer.parseInt(click3[1].split("-")[2]) + 1);
                        for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                                //System.out.println("-> "+miTablero.tableroComprimido());
                            //System.out.println(miTablero.tableroComprimido());
                            miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("ACTUALIZARTABLERO" + miTablero.tableroComprimido());
                        }
                    } else if (click3[1].split("-")[0].equals("quitarbandera")) {
                        miTablero.clickDerechoquitar(click3[0].split("-")[0], Integer.parseInt(click3[1].split("-")[1]) + 1, Integer.parseInt(click3[1].split("-")[2]) + 1);
                        for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                                //System.out.println("-> "+miTablero.tableroComprimido());
                            //System.out.println(miTablero.tableroComprimido());
                            miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("ACTUALIZARTABLERO" + miTablero.tableroComprimido());
                        }
                    }

                    System.out.println("Clickeo con el derecho");
                }

                if (miTablero.getPerdedores().size() == miTablero.getArrayClaveDelJugadores().size()) {
                    for (int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++) {
                        miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("TODOSPERDIERON");
                    }
                    /*HashMap<Integer,Integer> PuntosDePerdedores = new HashMap<Integer, Integer>();
                     System.out.println("Vueltas de perdedores: "+ miTablero.getArrayClaveDelJugadores().size());
                     for(int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++){   
                     miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("MANDAMETUTABLEROPERDEDOR");
                     //System.out.println("perdedor "+ tablero.getClaveDelJugadores(i));
                                
                     System.out.println("marcador 1");
                                
                     //System.out.println("llego aqui cholo taba: "+in.nextLine());
                     //System.out.println("Don jos pruebas: "+in.nextLine().split(",")[1]);
                     //PuntosDePerdedores.put(miTablero.getClaveDelJugadores(i), miTablero.determinarGanador(in.nextLine().split(",")[1]));
                                    
                                
                     System.out.println("marcador 2");
                     }
                     ArrayList<Integer> ganador = new ArrayList<>();
                     for(int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++){
                     System.out.println("puntos: "+ PuntosDePerdedores.get(i));
                     if(i==0){
                     ganador.add(PuntosDePerdedores.get(i));
                     }else{
                     if(ganador.get(0) < PuntosDePerdedores.get(i)){
                     ganador.clear();
                     ganador.add(miTablero.getClaveDelJugadores(i));
                     }else if(ganador.get(0) == PuntosDePerdedores.get(i)){
                     ganador.add(miTablero.getClaveDelJugadores(i));
                     }
                     }
                     }
                        
                     if(ganador.size() == 1){
                     for(int i = 0; i < miTablero.getArrayClaveDelJugadores().size(); i++){  
                     if(miTablero.getClaveDelJugadores(i) == ganador.get(0)){
                     System.out.println("true");
                     miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("GANASTE");
                     }else{
                     System.out.println("false");
                     miTablero.getColorPrinwiter(miTablero.getClaveDelJugadores(i)).println("FINDELGAME" ); 
                     }     
                     }
                     }else{
                            
                     }
                        
                     //se ciclo pero creo que es eso, lo que arregle
                        
                     System.out.println("me tiras a loko?");*/
                }
                System.out.println("le haces break?");
            }

        
        
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    
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


    


