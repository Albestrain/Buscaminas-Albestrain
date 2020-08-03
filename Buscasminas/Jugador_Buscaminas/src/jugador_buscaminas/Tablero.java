
package jugador_buscaminas;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Tablero extends Applet {
    JFrame tablero;
    JPanel Principal,estadisticas,chat,parcela;
    JButton terreno;
    ArrayList<JButton> jugadores = new ArrayList<>();
    JButton[][] matrizDeBotones;
    int tamanoDeMatriz;
    int[][] tableroMatriz, tusBanderas;
    boolean perdio = false;
    int cantidadDeBanderas;
    int clickes = 0;
    JTextArea messageArea = new JTextArea(5,10);
    JButton TusBanderas2 = new JButton();


    public int[][] getTableroMatriz() {
        return tableroMatriz;
    }

    public void setTableroMatriz(int[][] tableroMatriz) {
        this.tableroMatriz = tableroMatriz;
    }

    public int getTamanoDeMatriz() {
        return tamanoDeMatriz;
    }

    public void setTamanoDeMatriz(int tamanoDeMatriz) {
        this.tamanoDeMatriz = tamanoDeMatriz-2;
    }

    public int getCantidadDeBanderas() {
        return cantidadDeBanderas;
    }

    public void setCantidadDeBanderas(int cantidadDeBanderas) {
        this.cantidadDeBanderas = cantidadDeBanderas;
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }

    public void setMessageArea(JTextArea messageArea) {
        this.messageArea = messageArea;
    }

    public void crearPantalla(){
        tablero = new JFrame();
        tablero.setSize((tamanoDeMatriz*40)+50, 700);
        tablero.setTitle("Buscaminas");
        
        tablero.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {       
                //JOptionPane.showMessageDialog(null, "Has abandonado la partida");
                System.exit(0);
            }
        });
    }
    
    public void ponerTitulo(String titulo){
        tablero.setTitle(titulo);
    }
    
    public void descomprimirTablero(String tableroComprimido){
        if(tableroMatriz != null){
            tusBanderas = new int[tamanoDeMatriz][tamanoDeMatriz];
            for(int x = 0; x < tamanoDeMatriz; x++){
                for(int y = 0; y < tamanoDeMatriz; y++){
                    if(tableroMatriz[x][y] >= 100){
                        tusBanderas[x][y] = 1;
                    }else{
                        tusBanderas[x][y] = 0;
                    }
                }
            }
        }
        
        String[] matrizDescomprimida = tableroComprimido.split("_");
        tableroMatriz  = new int[tamanoDeMatriz][tamanoDeMatriz];
        System.out.println("tamaño de matriz: "+ tamanoDeMatriz);

        int n = 0;
        for(int x = 0; x < tamanoDeMatriz; x++){
            for(int y = 0; y < tamanoDeMatriz; y++){
                tableroMatriz[x][y] = Integer.parseInt(matrizDescomprimida[n]);
                n++;
            }
        }
        imprimirTablero();
    }
    
    public void ActualizarBotonesDelTablero(){
        System.out.println("corriendo");
        matrizDeBotones = new JButton[tamanoDeMatriz][tamanoDeMatriz];
        for(int i = 0; i < tamanoDeMatriz; i++){ 
            
            for(int j = 0; j < tamanoDeMatriz; j++){
                final int i2 = i;
                final int j2 = j;
                
                matrizDeBotones[i][j] = new JButton();
                matrizDeBotones[i][j].setEnabled(true);
                matrizDeBotones[i][j].setBackground( new Color(201, 194, 106));
                matrizDeBotones[i][j].setSize(40,40);
                // 10 = terrenoVacioDesmarcado // entre 200 y 300, es bandera green // entre 300 y 400, es bandera red // entre 400 y 500, es bandera blue
                // entre 500 y 600, es bandera yellow positivo o negativo
                if(tableroMatriz[i][j] == 10 || tableroMatriz[i][j] == -10){
                    matrizDeBotones[i][j].setEnabled(false);
                    matrizDeBotones[i][j].setBackground(Color.WHITE);
                    matrizDeBotones[i][j].setText(null);
                }else if(tableroMatriz[i][j] < -100){
                    if(tableroMatriz[i][j] <= -200 && tableroMatriz[i][j] > -300){
                        dibujarBanderaEnTablero(i, j, "green");
                    }else if(tableroMatriz[i][j] <= -300 && tableroMatriz[i][j] > -400){
                        dibujarBanderaEnTablero(i, j, "red");
                    }else if(tableroMatriz[i][j] <= -400 && tableroMatriz[i][j] > -500){
                        dibujarBanderaEnTablero(i, j, "blue");
                    }else if(tableroMatriz[i][j] <= -500 && tableroMatriz[i][j] > -600){
                        dibujarBanderaEnTablero(i, j, "yellow");
                    }
                }else if(tableroMatriz[i][j] < 0){
                    matrizDeBotones[i][j].setEnabled(false);
                    matrizDeBotones[i][j].setBackground(Color.WHITE);
                    matrizDeBotones[i][j].setText((tableroMatriz[i][j] * -1)+"");
                }else if(tableroMatriz[i][j] >= 200 && tableroMatriz[i][j] < 300){
                    dibujarBanderaEnTablero(i, j, "green");
                }else if(tableroMatriz[i][j] >= 300 && tableroMatriz[i][j] < 400){
                    dibujarBanderaEnTablero(i, j, "red");
                }else if(tableroMatriz[i][j] >= 400 && tableroMatriz[i][j] < 500){
                    dibujarBanderaEnTablero(i, j, "blue");
                }else if(tableroMatriz[i][j] >= 500 && tableroMatriz[i][j] < 600){
                    dibujarBanderaEnTablero(i, j, "yellow");
                }else if(tableroMatriz[i][j] > 800){
                    dibujarBanderaEnTablero(i, j, "dinamita");
                    matrizDeBotones[i][j].setEnabled(false);
                    if(tableroMatriz[i][j] == 999){
                        matrizDeBotones[i][j].setBackground(Color.green);
                    }else if(tableroMatriz[i][j] == 9999){
                        matrizDeBotones[i][j].setBackground(Color.red);
                    }else if(tableroMatriz[i][j] == 99999){
                        matrizDeBotones[i][j].setBackground(Color.blue);
                    }else if(tableroMatriz[i][j] == 999999){
                        matrizDeBotones[i][j].setBackground(Color.yellow);
                    }else if(tableroMatriz[i][j] == 900){
                        matrizDeBotones[i][j].setBackground(new Color(243, 154, 0));
                    }
                }

                matrizDeBotones[i][j].addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if( matrizDeBotones[i2][j2].isEnabled()){
                            if(e.getButton() == 1){

                                if(tableroMatriz[i2][j2] < 100){
                                    Jugador_Buscaminas.out.println(Jugador_Buscaminas.MiColor+"-"+"1"+"-"+i2+"-"+j2+"-");
                                    clickes++;
                                }
                                                        
                            }else if(e.getButton() == 3){
                                
                                if(tableroMatriz[i2][j2] < 100){
                                    
                                    if(cantidadDeBanderas >= 1){
                                       cantidadDeBanderas--;
                                      
                                    TusBanderas2.setText(cantidadDeBanderas+"");
                                    ImageIcon bandera = new ImageIcon(getClass().getResource("/Imagenes/"+Jugador_Buscaminas.MiColor+".png"));
                                    matrizDeBotones[i2][j2].setIcon(new ImageIcon(bandera.getImage().getScaledInstance(matrizDeBotones[i2][j2].getWidth(), matrizDeBotones[i2][j2].getHeight(), Image.SCALE_SMOOTH)));
                                    Jugador_Buscaminas.out.println(Jugador_Buscaminas.MiColor + "-3-,bandera-"+i2+"-"+j2);
                                    }
                                    
                                }else{
                                    if(Jugador_Buscaminas.MiColor.equals("green")){
                                        if(tableroMatriz[i2][j2] >= 200 && tableroMatriz[i2][j2] < 300){
                                            Jugador_Buscaminas.out.println(Jugador_Buscaminas.MiColor + "-3-,quitarbandera-"+i2+"-"+j2);
                                            cantidadDeBanderas++;
                                        }
                                    }else if(Jugador_Buscaminas.MiColor.equals("red")){
                                        if(tableroMatriz[i2][j2] >= 300 && tableroMatriz[i2][j2] < 400){
                                            Jugador_Buscaminas.out.println(Jugador_Buscaminas.MiColor + "-3-,quitarbandera-"+i2+"-"+j2);
                                            cantidadDeBanderas++;
                                        }
                                    }else if(Jugador_Buscaminas.MiColor.equals("blue")){
                                        if(tableroMatriz[i2][j2] >= 400 && tableroMatriz[i2][j2] < 500){
                                            Jugador_Buscaminas.out.println(Jugador_Buscaminas.MiColor + "-3-,quitarbandera-"+i2+"-"+j2);
                                            cantidadDeBanderas++;
                                        }
                                    }else{
                                        if(tableroMatriz[i2][j2] >= 500 && tableroMatriz[i2][j2] < 600){
                                            Jugador_Buscaminas.out.println(Jugador_Buscaminas.MiColor + "-3-,quitarbandera-"+i2+"-"+j2);
                                            cantidadDeBanderas++;
                                        }
                                    }
                                }
                                
                            }
                         }
                        }

                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseExited(MouseEvent e) {}
                });
                
            }
        }
    }
    
    public void dibujarBanderaEnTablero(int posicionX, int posicionY, String color){
        ImageIcon bandera = new ImageIcon(getClass().getResource("/Imagenes/"+color+".png"));
        matrizDeBotones[posicionX][posicionY].setIcon(new ImageIcon(bandera.getImage().getScaledInstance(matrizDeBotones[posicionX][posicionY].getWidth(), matrizDeBotones[posicionX][posicionY].getHeight(), Image.SCALE_SMOOTH)));
    }
    
    public String mandarMisBombasAlServer(){
        String MisBombas = Jugador_Buscaminas.MiColor+"-"+"3"+"-,";
        for(int i = 0; i < tamanoDeMatriz; i++){
            for(int j = 0; j < tamanoDeMatriz; j++){
                if(tableroMatriz[i][j] >= 100){
                    MisBombas = MisBombas + i + "_"+ j + "-";
                }
            }
        }
        return MisBombas;
    }
    
    public String mandarMisBombasParaVerficar(){
        String MisBombas =Jugador_Buscaminas.MiColor + ",";
        for(int i = 0; i < tamanoDeMatriz; i++){
            for(int j = 0; j < tamanoDeMatriz; j++){
                if(tableroMatriz[i][j] >= 100){
                    MisBombas = MisBombas + i + "_"+ j + "-";
                }
            }
        }
        System.out.println("mis bombas: "+ MisBombas);
        return MisBombas;
    }
    
    public void empezar(String cantidad){
        if(Principal != null){
            tablero.remove(Principal);
        }
        
        Principal = new JPanel();
        Principal.setLayout(new GridLayout(1,1));
        JButton empezar = new JButton();
        
        empezar.setSize((tamanoDeMatriz*40)+50, 700);
        empezar.setBackground(new Color(201, 194, 106));
        empezar.setFont(new Font("Arial", Font.PLAIN, 70));
        empezar.setForeground(Color.white);
        empezar.setText("<html><p>"+"= EMPEZAR ="+"</p></html>");
        tablero.setTitle("Empezar partida - " +cantidad + " Jugadores en linea");
        empezar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Jugador_Buscaminas.out.println("EMPEZAR-5");
            }
        });
        Principal.add(empezar);
        tablero.add(Principal);
        tablero.setVisible(true);
    }
    boolean crearChat = false;
    
    public void mostrarTablero(){
        if(Principal != null){
            tablero.remove(Principal);
        }
        if(parcela != null){
            Principal.remove(parcela);
        }
        if(estadisticas != null){
            tablero.remove(Principal);
        }
        
        Principal = new JPanel();
        Principal.setLayout(new BorderLayout());
        estadisticas = new JPanel();
        estadisticas.setFont(new Font("Dialog", 1, 16));
        estadisticas.setBackground(new Color(201, 194, 106));
        JButton TusBanderas = new JButton();
        ImageIcon bandera2 = new ImageIcon(getClass().getResource("/Imagenes/"+Jugador_Buscaminas.MiColor+".png"));
        TusBanderas.setBackground(Color.WHITE);
        TusBanderas.setIcon(new ImageIcon(bandera2.getImage()));
        estadisticas.add(TusBanderas);
        TusBanderas2.setText(cantidadDeBanderas+"");
        TusBanderas2.setBackground(Color.WHITE);
        estadisticas.add(TusBanderas2);

        parcela = new JPanel();
        parcela.setLayout(new GridLayout(tamanoDeMatriz,tamanoDeMatriz));
        parcela.setFont(new Font("Dialog", 1, 16));
        
        chat = new JPanel();
        chat.setLayout(new GridLayout(2,1));
        messageArea.setEditable(false);
        
        for(int j = 0; j < tamanoDeMatriz; j++){
            for(int i = 0; i < tamanoDeMatriz; i++){
                parcela.add(matrizDeBotones[j][i]);
                if(perdio){
                    matrizDeBotones[j][i].setEnabled(false);
                } 
            }
        }

        chat.add(messageArea);
        Principal.add(estadisticas, BorderLayout.NORTH);
        Principal.add(parcela, BorderLayout.CENTER);
        Principal.add(new JScrollPane(messageArea), BorderLayout.SOUTH);
        tablero.add(Principal);
        tablero.setVisible(true);
         
    }
    
    public void GAMEOVER(int x, int y){
        perdio = true;
        ImageIcon bandera = new ImageIcon(getClass().getResource("/Imagenes/dinamita.png"));
        matrizDeBotones[x][y].setIcon(new ImageIcon(bandera.getImage().getScaledInstance(matrizDeBotones[x][y].getWidth(), matrizDeBotones[x][y].getHeight(), Image.SCALE_SMOOTH)));
        for(int i=0; i < tamanoDeMatriz; i++){
            for(int j = 0; j < tamanoDeMatriz; j++){
                if(i == x && j == y){
                     matrizDeBotones[i][j].setEnabled(false); 
                }else{
                    matrizDeBotones[i][j].setEnabled(false);
                    matrizDeBotones[i][j].setBackground( Color.red); 
                }
            }
        }
    }
    
    public void FINDELJUEGO(String color){
        for(int i=0; i < tamanoDeMatriz; i++){
            for(int j = 0; j < tamanoDeMatriz; j++){
                
                    matrizDeBotones[i][j].setEnabled(false);
                    matrizDeBotones[i][j].setBackground( Color.red); 
                    
            }
        }
        JOptionPane.showMessageDialog(null, "El Juego ha terminado, el ganador es el color: "+ color);
        System.exit(1);
    }
    
    public void ponerTableroEnableFalse(boolean enable){
        if(!enable){
           for(int i=0; i < tamanoDeMatriz; i++){
                for(int j = 0; j < tamanoDeMatriz; j++){           
                    matrizDeBotones[i][j].setEnabled(false);
                    matrizDeBotones[i][j].setBackground( new Color(245, 254, 250));                 
                }
            } 
        }else{
            for(int i=0; i < tamanoDeMatriz; i++){
                for(int j = 0; j < tamanoDeMatriz; j++){           
                    matrizDeBotones[i][j].setEnabled(true);
                    matrizDeBotones[i][j].setBackground( new Color(255, 254, 221));                 
                }
            }
        }
        
    }
    
    public void ladoPorElQueEmpiezas(){
        int primeravez = 1;
        if(Jugador_Buscaminas.MiColor.equals("green")){
            for(int i = 0; i < tamanoDeMatriz; i++){
                for(int j = 0; j < tamanoDeMatriz; j++){
                    if(j == 0 && i < tamanoDeMatriz){
                        matrizDeBotones[i][j].setBackground(Color.GREEN);
                        matrizDeBotones[i][j].setEnabled(true);
                        matrizDeBotones[i][j].setText(null);
                    }else{
                        matrizDeBotones[i][j].setBackground(Color.WHITE);
                        matrizDeBotones[i][j].setEnabled(false);
                    }
                }
            }
        }else if(Jugador_Buscaminas.MiColor.equals("red")){
            for(int i = 0; i < tamanoDeMatriz; i++){
                for(int j = 0; j < tamanoDeMatriz; j++){
                    if(i == 0 && j < tamanoDeMatriz){
                        matrizDeBotones[i][j].setBackground(Color.red);
                        matrizDeBotones[i][j].setEnabled(true);
                        matrizDeBotones[i][j].setText(null);
                    }else{
                        matrizDeBotones[i][j].setBackground(Color.WHITE);
                        matrizDeBotones[i][j].setEnabled(false);
                    }
                }
            }
        }else if(Jugador_Buscaminas.MiColor.equals("blue")){
            
            for(int i = 0; i < tamanoDeMatriz; i++){
                for(int j = 0; j < tamanoDeMatriz; j++){
                    if(j == tamanoDeMatriz-1){
                        matrizDeBotones[i][j].setBackground(Color.BLUE);
                        matrizDeBotones[i][j].setEnabled(true);
                        matrizDeBotones[i][j].setText(null);
                    }else{
                        matrizDeBotones[i][j].setBackground(Color.WHITE);
                        matrizDeBotones[i][j].setEnabled(false);
                    }
                }
            }
        }else if(Jugador_Buscaminas.MiColor.equals("yellow")){
            for(int i = 0; i < tamanoDeMatriz; i++){
                for(int j = 0; j < tamanoDeMatriz; j++){
                    if(i == tamanoDeMatriz-1 ){
                        matrizDeBotones[i][j].setBackground(Color.YELLOW);
                        matrizDeBotones[i][j].setEnabled(true);
                        matrizDeBotones[i][j].setText(null);
                    }else{
                        matrizDeBotones[i][j].setBackground(Color.WHITE);
                        matrizDeBotones[i][j].setEnabled(false);
                    }
                }
            }
        }
    }

     public void imprimirTablero(){
        for(int i = 0; i <tamanoDeMatriz; i++){
            for(int j = 0; j < tamanoDeMatriz; j++ ){
                System.out.print(tableroMatriz[i][j]);
                
            }
            System.out.println("");
        }
    }
   
}
