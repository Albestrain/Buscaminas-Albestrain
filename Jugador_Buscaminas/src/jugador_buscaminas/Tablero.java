
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

    
    
    
    
    //ImageIcon bandera = new ImageIcon(this.getClass().getResource("Imagenes/buscaminas.png"));
    
    public void crearPantalla(){
        tablero = new JFrame();
        tablero.setSize((tamanoDeMatriz*40)+50, 700);
        tablero.setTitle("Buscaminas");
        //tablero.setResizable(false);
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
        //System.out.println(tableroComprimido);
        
        
        int n = 0;
        for(int x = 0; x < tamanoDeMatriz; x++){
            for(int y = 0; y < tamanoDeMatriz; y++){
                //System.out.print("-> "+matrizDescomprimida[n]);
                //System.out.print("("+x+" , "+y+")");
                tableroMatriz[x][y] = Integer.parseInt(matrizDescomprimida[n]);
                n++;
            }
            //System.out.println("");
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
                matrizDeBotones[i][j].setBackground( new Color(255, 254, 221));
                matrizDeBotones[i][j].setSize(40,40);
                if(tableroMatriz[i][j] == 10 || tableroMatriz[i][j] == -10){
                    matrizDeBotones[i][j].setEnabled(false);
                    matrizDeBotones[i][j].setBackground(Color.WHITE);
                    matrizDeBotones[i][j].setText(null);
                }else if(tableroMatriz[i][j] < 0){
                    matrizDeBotones[i][j].setEnabled(false);
                    matrizDeBotones[i][j].setBackground(Color.WHITE);
                    matrizDeBotones[i][j].setText((tableroMatriz[i][j] * -1)+"");
                }
                
               
                
                
                matrizDeBotones[i][j].addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println(e.getButton()+"  "+i2+" : "+j2);
                        System.out.println("------le dio click");
                        if( matrizDeBotones[i2][j2].isEnabled()){
                            if(e.getButton() == 1){
                                System.out.println("------le dio click");
                                clickes++;
                                if(tableroMatriz[i2][j2] < 100){
                                    System.out.println(Jugador_Buscaminas.MiColor+"-"+"1"+"-"+i2+"-"+j2+"-");
                                    Jugador_Buscaminas.out.println(Jugador_Buscaminas.MiColor+"-"+"1"+"-"+i2+"-"+j2+"-");
                                }
                                                        
                            }else if(e.getButton() == 3){
                                
                                if(tableroMatriz[i2][j2] < 100){
                                    if(cantidadDeBanderas >= 1){//cambie esto, si llega a cero ya no puedes quitarle banderas
                                       cantidadDeBanderas--;
                                    TusBanderas2.setText(cantidadDeBanderas+"");
                                    tableroMatriz[i2][j2] = tableroMatriz[i2][j2] + 100;
                                    ImageIcon bandera = new ImageIcon(getClass().getResource("/Imagenes/"+Jugador_Buscaminas.MiColor+".png"));
                                    matrizDeBotones[i2][j2].setIcon(new ImageIcon(bandera.getImage().getScaledInstance(matrizDeBotones[i2][j2].getWidth(), matrizDeBotones[i2][j2].getHeight(), Image.SCALE_SMOOTH)));
                                    if(cantidadDeBanderas == 0){
                                        //JOptionPane.showMessageDialog(null, mandarMisBombasAlServer());
                                        Jugador_Buscaminas.out.println(mandarMisBombasAlServer());
                                        //JOptionPane.showMessageDialog(null, "Dame tiempo para ver si ganaste, simon?");
                                    } 
                                    }
                                    
                                }else{
                                    cantidadDeBanderas++;
                                    TusBanderas2.setText(cantidadDeBanderas+"");
                                    tableroMatriz[i2][j2] = tableroMatriz[i2][j2] - 100;
                                    matrizDeBotones[i2][j2].setIcon(null);
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
        empezar.setBackground(new Color(237, 255, 247));
        empezar.setFont(new Font("Arial", Font.PLAIN, 50));
        //empezar.setText("Empezar"+"\n"+"Cantidad de jugadores: "+cantidad);
        empezar.setText("<html><p>"+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Empezar"+"</p><p>"+"Cantidad de jugadores: "+cantidad+"</p></html>");
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
        estadisticas.setBackground(new Color(237, 255, 247));
        JButton TusBanderas = new JButton();
        ImageIcon bandera2 = new ImageIcon(getClass().getResource("/Imagenes/"+Jugador_Buscaminas.MiColor+".png"));
        TusBanderas.setBackground(Color.WHITE);
        TusBanderas.setIcon(new ImageIcon(bandera2.getImage()));
        estadisticas.add(TusBanderas);
        TusBanderas2.setText(cantidadDeBanderas+"");
        TusBanderas2.setBackground(Color.WHITE);
        estadisticas.add(TusBanderas2);
        //estadisticas.setLayout(new FlowLayout());
        

        parcela = new JPanel();
        parcela.setLayout(new GridLayout(tamanoDeMatriz,tamanoDeMatriz));
        parcela.setFont(new Font("Dialog", 1, 16));
        
        chat = new JPanel();
        chat.setLayout(new GridLayout(2,1));
        //chat.setFont(new Font("Dialog", 1, 20));
        messageArea.setEditable(false);
        /*tablero.getContentPane().add(messageArea, BorderLayout.SOUTH);
        tablero.getContentPane().add(new JScrollPane(messageArea),
                BorderLayout.EAST);*/
        
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
         
        for(int j = 0; j < tamanoDeMatriz; j++){
            for(int i = 0; i < tamanoDeMatriz; i++){
                if(tusBanderas != null){
                    if(tusBanderas[j][i] == 1){
                        
                        System.out.println("casilla "+ j + " - " + i);
                        tableroMatriz[j][i] = tableroMatriz[j][i] + 100;
                        ImageIcon bandera = new ImageIcon(getClass().getResource("/Imagenes/"+Jugador_Buscaminas.MiColor+".png"));
                        System.out.println("wid and hay: "+matrizDeBotones[j][i].getWidth()+ " "+matrizDeBotones[j][i].getHeight());
                        matrizDeBotones[j][i].setIcon(new ImageIcon(bandera.getImage().getScaledInstance(matrizDeBotones[j][i].getWidth(), matrizDeBotones[j][i].getHeight(), Image.SCALE_SMOOTH)));
                    }  
                }
            }
        }
    }
    public void addBotonesDeEstadisticas(String jugadores){

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
                    System.out.println(j);
                    if(i == 0 && j < tamanoDeMatriz){
                        
                        System.out.println("-j-"+j);
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
                    System.out.print("("+i+" - "+j+")");
                    if(j == tamanoDeMatriz-1){
                        matrizDeBotones[i][j].setBackground(Color.BLUE);
                        matrizDeBotones[i][j].setEnabled(true);
                        matrizDeBotones[i][j].setText(null);
                    }else{
                        matrizDeBotones[i][j].setBackground(Color.WHITE);
                        matrizDeBotones[i][j].setEnabled(false);
                    }
                }
                System.out.println("");
            }
        }else if(Jugador_Buscaminas.MiColor.equals("yellow")){
            for(int i = 0; i < tamanoDeMatriz; i++){
                for(int j = 0; j < tamanoDeMatriz; j++){
                    System.out.println(j);
                    if(i == tamanoDeMatriz-1 ){
                        System.out.println("-j-"+j);
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
    
    //va en el servidor
     public void imprimirTablero(){
        for(int i = 0; i <tamanoDeMatriz; i++){
            for(int j = 0; j < tamanoDeMatriz; j++ ){
                System.out.print(tableroMatriz[i][j]);
                
            }
            System.out.println("");
        }
    }
   
}
