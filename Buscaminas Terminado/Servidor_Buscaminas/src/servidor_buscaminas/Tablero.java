
package servidor_buscaminas;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Tablero {
    int tableroMatriz [][];
    //20x20, 40 minas
    int MedidaDeMatriz = 20 /*16+2*/, NumeroDeMinas = 20;
    ArrayList<Integer> claveDelJugadores = new ArrayList<>();
    HashMap<Integer,PrintWriter> colorPrinwiter = new HashMap<Integer, PrintWriter>();
    HashMap<String,Integer> color = new HashMap<String, Integer>();
    int aceptados = 4;
    ExecutorService pool = Executors.newFixedThreadPool(4);
    ArrayList<String> perdedores = new ArrayList<>();
    boolean empezado = false;
    int banderasPuestas = 0;
    HashMap<String,Boolean> BanderaSobreMina = new HashMap<String, Boolean>();
    ArrayList<String> BombasDeTiempo = new ArrayList<>();
    
    //Setters and getters
    public ExecutorService getPool() {
        return pool;
    }

    public void setPool(ExecutorService pool) {
        this.pool = pool;
    }
    
    public void setCantidadAceptada(int pool) {
        aceptados = pool;
    }

    public PrintWriter getColorPrinwiter(Integer color) {
        return colorPrinwiter.get(color);
    }

    public void setColorPrinwiter(Integer clave, PrintWriter out) {
        this.colorPrinwiter.put(clave, out);
    }  
    
    public int getMedidaDeMatriz() {
        return MedidaDeMatriz;
    }

    public void setMedidaDeMatriz(int MedidaDeMatriz) {
        this.MedidaDeMatriz = MedidaDeMatriz;
    }

    public int getNumeroDeMinas() {
        return NumeroDeMinas;
    }

    public void setNumeroDeMinas(int NumeroDeMinas) {
        this.NumeroDeMinas = NumeroDeMinas;
    }

    public int getClaveDelJugadores(int posicion) {
        return claveDelJugadores.get(posicion);
    }

     public ArrayList getArrayClaveDelJugadores() {
        return claveDelJugadores;
    }
     
    public void setClaveDelJugadores(int claveDelJugadores) {
        this.claveDelJugadores.add(claveDelJugadores);
    }

    public int getColor(String color) {
        return this.color.get(color);
    }

    public void setColor( String color, int clave) {
        this.color.put(color, clave);
    }

    public ArrayList<String> getPerdedores() {
        return perdedores;
    }

    public void setPerdedores(String perdedores) {
        this.perdedores.add(perdedores);
    }

    public boolean isEmpezado() {
        return empezado;
    }

    public void setEmpezado(boolean empezado) {
        this.empezado = empezado;
    } 
 
    public void CrearTablero(){
        tableroMatriz= new int[MedidaDeMatriz][MedidaDeMatriz];

        double w=0;
        double z=0;

        int ntminas=0;

        for(int j=0;j<MedidaDeMatriz;j++){
                for (int i=0;i<MedidaDeMatriz;i++){
                        tableroMatriz [j][i]=0;
                }        
        }        
        int contador = 0;
        do  {  
                w=Math.random()*MedidaDeMatriz;
                z=Math.random()*MedidaDeMatriz;  
                w=(int)w;
                z=(int)z;
                if  (z!=0 && w!=0 && z!=MedidaDeMatriz-1 && w!=MedidaDeMatriz-1 && tableroMatriz[(int)w][(int)z] == 0){
                    tableroMatriz[(int)w][(int) z ]=9;
                    BanderaSobreMina.put((int)w +","+(int)z, false);
                    BombasDeTiempo.add((int)w +","+(int)z);
                    ntminas++;  
                    contador++;
                }
        }while (ntminas<NumeroDeMinas);
        System.out.println("minas agregadas: "+contador);
     
    }
    
    public void ponerleNumerosAlTablero(){
        int[][] fantasma = new int[3][3];
        for(int x = 0; x < MedidaDeMatriz; x++){
            for(int y = 0; y < MedidaDeMatriz; y++){
                if (x!=0 && y!=0 && x!=MedidaDeMatriz-1 && y!=MedidaDeMatriz-1){
                    int xretroceso = -2;
                    for(int i = 0; i < 3; i++){
                        xretroceso++;
                        int yretroceso = -2;
                        for(int j = 0; j < 3; j++){
                            yretroceso++;
                            fantasma[i][j] = tableroMatriz[x+xretroceso][y+yretroceso];
                        }
                    }
                    int contadorDeMinas = 0;
                    for(int i = 0; i < 3; i++){
                        for(int j = 0; j < 3; j++){
                            if(fantasma[i][j] == 9 && i!=9 && j!= 9){
                                contadorDeMinas++;
                            }
                        }
                    }
                    if(fantasma[1][1] == 9){    
                    }else if(fantasma[1][1] == 0){
                        tableroMatriz[x][y] = contadorDeMinas;
                    }                                     
                }
            }
        }  
    }
    
    public boolean clickIzquierdo(String color,String xx, String yy){
        int x=Integer.parseInt(xx)+1;
        int y =Integer.parseInt(yy)+1;  
        boolean explotado = true;
        if(tableroMatriz[x][y] > 100){
            if(tableroMatriz[x][y] < 300){
                tableroMatriz[x][y] = tableroMatriz[x][y] - 200;
            }else if(tableroMatriz[x][y] < 400){
                tableroMatriz[x][y] = tableroMatriz[x][y] -300;
            }else if(tableroMatriz[x][y] < 500){
                tableroMatriz[x][y] = tableroMatriz[x][y] -400;
            }else if(tableroMatriz[x][y] < 600){
                tableroMatriz[x][y] = tableroMatriz[x][y] -500;
            }
        }
        if(tableroMatriz[x][y] == 9){
           explotado = true;
           if(color.equals("green")){
               tableroMatriz[x][y] = 999;
           }else if(color.equals("red")){
               tableroMatriz[x][y] = 9999;
           }else if(color.equals("blue")){
               tableroMatriz[x][y] = 99999;
           }else if(color.equals("yellow")){
               tableroMatriz[x][y] = 999999;
           }
           BanderaSobreMina.remove(x+","+y);
           BombasDeTiempo.remove(x+","+y);
           NumeroDeMinas--;
           
        }else if(tableroMatriz[x][y] != 0){
            
            for(PrintWriter writer : colorPrinwiter.values()) {
                writer.println("ACCIONES"+color+" Ha desenterrado con exito");
            }
               
            explotado = false;
            if(tableroMatriz[x][y] < 0){
                
            }else{
                tableroMatriz[x][y] = tableroMatriz[x][y] * -1;
            }
            
        }else if(tableroMatriz[x][y] == 0){
            for (PrintWriter writer : colorPrinwiter.values()) {
                writer.println("ACCIONES"+color+" Ha desenterrado una gran cantidad de terreno");
            }
            explotado = false;
            terrenoVacio(x, y);
        }
        
        return explotado;
    }
    
    public void clickDerecho(String color, int x, int y){
        if(BanderaSobreMina.get(x+","+y) != null){
            BanderaSobreMina.put((x+","+y), true);
        }
        if(tableroMatriz[x][y] == 9){
            BombasDeTiempo.remove(x+","+y);
        }
        
        if(color.equals("green")){
            tableroMatriz[x][y] = tableroMatriz[x][y] + 200; 
        }else if(color.equals("red")){
            tableroMatriz[x][y] = tableroMatriz[x][y] + 300;
        }else if(color.equals("blue")){
            tableroMatriz[x][y] = tableroMatriz[x][y] + 400;
        }else if(color.equals("yellow")){
            tableroMatriz[x][y] = tableroMatriz[x][y] + 500;
        }
        banderasPuestas++;
        
        tieneCadaMinaUnaBandera();
    }
    
    public void clickDerechoquitar(String color, int x, int y){
        if(BanderaSobreMina.get(x+","+y) != null){
            BanderaSobreMina.put((x+","+y), false);
        }
        
        if(tableroMatriz[x][y] == 9){
           BombasDeTiempo.add(x+","+y); 
        }
        
        if(tableroMatriz[x][y] > 100){
            if(color.equals("green")){
                tableroMatriz[x][y] = tableroMatriz[x][y] - 200;
            }else if(color.equals("red")){
                tableroMatriz[x][y] = tableroMatriz[x][y] - 300;
            }else if(color.equals("blue")){
                tableroMatriz[x][y] = tableroMatriz[x][y] - 400;
            }else if(color.equals("yellow")){
                tableroMatriz[x][y] = tableroMatriz[x][y] - 500;
            }
        }else{
            if(color.equals("green")){
                tableroMatriz[x][y] = tableroMatriz[x][y] + 200;
            }else if(color.equals("red")){
                tableroMatriz[x][y] = tableroMatriz[x][y] + 300;
            }else if(color.equals("blue")){
                tableroMatriz[x][y] = tableroMatriz[x][y] + 400;
            }else if(color.equals("yellow")){
                tableroMatriz[x][y] = tableroMatriz[x][y] + 500;
            }
        }
        banderasPuestas--;
    }
    
    String[] recursivo;
    public void terrenoVacio(int x, int y){
        String[][] fantasma = new String[3][3];
        int xretroceso = -2;
        for(int i = 0; i < 3; i++){
            xretroceso++;
            int yretroceso = -2;
            for(int j = 0; j < 3; j++){
                yretroceso++;
                if(x+xretroceso == 0 || y+yretroceso == 0 || x+xretroceso == MedidaDeMatriz-1 || y+yretroceso == MedidaDeMatriz-1){
                    fantasma[i][j] = 44 +"";
                }else if(i==1 && j==1){
                   fantasma[1][1] = 10 + ","+(x+xretroceso)+","+(y+yretroceso); 
                }else{
                    fantasma[i][j] = tableroMatriz[x+xretroceso][y+yretroceso] + ","+(x+xretroceso)+","+(y+yretroceso);
                }
                
            }
        }
        recursivo = fantasma[1][1].split(",");    
        tableroMatriz[Integer.parseInt(recursivo[1])][Integer.parseInt(recursivo[2])] = Integer.parseInt(recursivo[0]);
        
        int posicionX;
        int posicionY;
        int numeroEnCasilla;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                numeroEnCasilla = Integer.parseInt(fantasma[i][j].split(",")[0]);       
                if((numeroEnCasilla != 0) && (numeroEnCasilla != 9) && (numeroEnCasilla != 10) && (numeroEnCasilla != 44)){
                    posicionX = Integer.parseInt(fantasma[i][j].split(",")[1]);
                    posicionY = Integer.parseInt(fantasma[i][j].split(",")[2]);
                    if(tableroMatriz[posicionX][posicionY] > 0){
                        tableroMatriz[posicionX][posicionY] = (tableroMatriz[posicionX][posicionY])*-1;
                    }
                }
            }
        }
        
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                recursivo = fantasma[i][j].split(",");
                if(recursivo[0].equals("0")){
                    terrenoVacio(Integer.parseInt(recursivo[1]), Integer.parseInt(recursivo[2]));
                } 
            }            
        }
        
    }
    
    public String tableroComprimido(){
        String matriz = "";
        for(int x = 1; x < MedidaDeMatriz;x++){
            for(int y = 1; y < MedidaDeMatriz; y++){
                if(x!=0 && y!=0 && x!=MedidaDeMatriz-1 && y!=MedidaDeMatriz-1){
                    matriz = matriz + tableroMatriz[x][y] + "_";
                }
            }
        }
        return matriz;
    }

    public void tieneCadaMinaUnaBandera(){
        int NumeroDeBanderasSobreBombas = 0;
        for (Boolean bandera : BanderaSobreMina.values()) {
            if(bandera == false){
                break;
            }else{
                NumeroDeBanderasSobreBombas++;
            }
        }
        if(NumeroDeBanderasSobreBombas == NumeroDeMinas){
            int[] banderasAcertadas = new int[claveDelJugadores.size()];
            String[] ranking = new String[claveDelJugadores.size()];
            for(int i = 0; i < ranking.length; i++){
                if(i == 0){
                    ranking[i] = "green";
                }else if(i == 1){
                    ranking[i] = "red";
                }else if(i == 2){
                    ranking[i] = "blue";
                }else if(i == 3){
                    ranking[i] = "yellow";
                }
            }
            for(int i = 0; i < MedidaDeMatriz; i++){
                for(int j = 0; j < MedidaDeMatriz; j++){
                    if(tableroMatriz[i][j] >= 200 && tableroMatriz[i][j] < 300){
                        banderasAcertadas[0]++;
                    }else if(tableroMatriz[i][j] >= 300 && tableroMatriz[i][j] < 400){
                        banderasAcertadas[1]++;
                    }else if(tableroMatriz[i][j] >= 400 && tableroMatriz[i][j] < 500){
                        banderasAcertadas[2]++;
                    }else if(tableroMatriz[i][j] >= 500 && tableroMatriz[i][j] < 600){
                        banderasAcertadas[3]++;
                    }
                }
            }
            
            String auxiliar = "";
            //metodo de la burbuja
            for(int i = 0; i < claveDelJugadores.size()-1; i++){
                //sacar al ganador
                for(int j = 0; j < claveDelJugadores.size()-1; j++){
                    if(banderasAcertadas[j] < banderasAcertadas[j+1]){
                    
                        auxiliar = banderasAcertadas[j]+"";
                        banderasAcertadas[j] = banderasAcertadas[j+1];
                        banderasAcertadas[j+1] = Integer.parseInt(auxiliar);

                        auxiliar = ranking[j]+"";
                        ranking[j] = ranking[j+1];
                        ranking[j+1] = auxiliar;
                    }
                    
                }
            }
            
            int nadiePusoNada = 0;
            for(int i = 0; i < claveDelJugadores.size(); i++){
                if(banderasAcertadas[i] == 0){
                   nadiePusoNada++;
                }
            }
            
            String Empatados = "";
            for(int i = 0; i < claveDelJugadores.size(); i++){
                if(banderasAcertadas[0] == banderasAcertadas[i]){
                    Empatados = Empatados + " -- "+ ranking[i] + " -- ";
                }
            }
            
            boolean empate = false;
            for(int i = 1; i < claveDelJugadores.size(); i++){
                if(banderasAcertadas[0] == banderasAcertadas[i]){
                    empate = true;
                }
            }
            if(nadiePusoNada == claveDelJugadores.size()){
                for(PrintWriter writer : colorPrinwiter.values()) {
                        writer.println("TIEMPOAGOTADO");
                    }
            }else{
               if(empate){
                    for(PrintWriter writer : colorPrinwiter.values()) {
                        writer.println("EMPATE"+Empatados);
                    }
                }
            
                if(!empate){
                    colorPrinwiter.get(color.get(ranking[0])).println("GANASTE");
                    for(int i = 1; i < claveDelJugadores.size(); i++){
                        colorPrinwiter.get(color.get(ranking[i])).println("FINDELGAME"+ranking[0]);
                    }
                }else{
                    colorPrinwiter.get(color.get(ranking[0])).println("EMPATE"+Empatados);
                } 
            }
        }
    }
    
    int quitarBombasDeUsuarios;
    int e = 0;
    public void explotarBombaDeTiempo(){
        if(e > 0 ){
            //elegir bomba random
            if(BombasDeTiempo.isEmpty()){
                for(PrintWriter writer : colorPrinwiter.values()) {
                    writer.println("TIEMPOAGOTADO");
                }
            }else{
                Random random = new Random();
                String PosicionRandom = BombasDeTiempo.get(random.nextInt(BombasDeTiempo.size()));
                int x = Integer.parseInt(PosicionRandom.split(",")[0]);
                int y = Integer.parseInt(PosicionRandom.split(",")[1]);
                
                quitarBombasDeUsuarios = 0;
                //abrir casillas de las orillas
                descubrirCasillasDeBombaDeTiempo(x,y);
                
                for (PrintWriter writer : colorPrinwiter.values()) {
                    writer.println("QUITARMINADEMARCADOR"+quitarBombasDeUsuarios);
                    writer.println("ACCIONES"+quitarBombasDeUsuarios+" Bombas de tiempo se ha(n) activado");
                    writer.println("ACTUALIZARTABLERO" + tableroComprimido());
                }
                tieneCadaMinaUnaBandera();
            }
        }
        e++;
        
    }
    
    public void descubrirCasillasDeBombaDeTiempo(int x, int y){
        String[][] fantasma = new String[3][3];
        int yAtras = x-2;

        for(int i = 0; i < 3; i++){
            yAtras++;
            int xAtras = y-2;
            for(int j = 0; j < 3; j++){
                xAtras++;
                if(i==1 && j == 1){
                    fantasma[i][j] = 900 + "," + yAtras + ","+ xAtras;
                    
                    if(BanderaSobreMina.containsKey(yAtras+","+xAtras)){
                        NumeroDeMinas--;
                        quitarBombasDeUsuarios++;  
                        BombasDeTiempo.remove(yAtras+","+xAtras);
                    }
                    BanderaSobreMina.remove(yAtras+","+xAtras);
                }else{
                    fantasma[i][j] = tableroMatriz[yAtras][xAtras]+ "," + yAtras + ","+ xAtras;
                }
            }
        }
        
        recursivo = fantasma[1][1].split(",");
        tableroMatriz[Integer.parseInt(recursivo[1])][Integer.parseInt(recursivo[2])] = Integer.parseInt(recursivo[0]);
        
        int posicionX, posicionY, numeroDeCasilla;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                numeroDeCasilla = Integer.parseInt(fantasma[i][j].split(",")[0]);
                if((numeroDeCasilla) >= 1 && (numeroDeCasilla) <= 8){
                    posicionX = Integer.parseInt(fantasma[i][j].split(",")[1]);
                    posicionY = Integer.parseInt(fantasma[i][j].split(",")[2]);
                    if(tableroMatriz[posicionX][posicionY] > 0){
                        tableroMatriz[posicionX][posicionY] = (tableroMatriz[posicionX][posicionY])*-1;
                    }
                }
            }
        }
        
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                recursivo = fantasma[i][j].split(",");
                if(recursivo[0].equals("9")){
                    fantasma[i][j] = 900 + "," +Integer.parseInt(recursivo[1]) + "," + Integer.parseInt(recursivo[2]);
                    descubrirCasillasDeBombaDeTiempo(Integer.parseInt(recursivo[1]), Integer.parseInt(recursivo[2]));
                } 
            }
        }
        
    }
    
   public void imprimirTablero(){
        for(int i = 0; i <MedidaDeMatriz; i++){
            for(int j = 0; j < MedidaDeMatriz; j++ ){
                System.out.print(tableroMatriz[i][j]);
            }
            System.out.println("");
        }
    }
    
    

}
