
package jugador_buscaminas;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Interfaz_Del_Juego extends JFrame{

    
    public void pantalla(boolean b) {
        
        super.setVisible(true); 
        super.setSize(405, 450);
        super.setTitle("jugador");
        super.setResizable(false);
    }
    
    
    /*JFrame pantalla;
    JPanel panel;
    JButton boton;
    
    

   /* public void crearVentana(){
        pantalla = new JFrame("jugador");
        pantalla.setSize(500, 500);
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 5));
        JLabel jos = new JLabel("ea");
        
        //panel.add(jos);
        //panel.add(jos2);
        for(int i = 0; i < 25; i++){
            boton = new JButton("EVO " +i);
            
            for(int j = 0; j < 25; j++){
                panel.add(boton); 
                /*boton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    System.out.println("le dio "+arg0);
                    if(arg0.getSource().equals(boton)){//devuelve verdadero si es ese mismo el botón que se ha pulsado
                    //Tu código si se ha pulsado el botón
                        
                    }
                }
                });
            }
        panel.setBorder(null);
        pantalla.add(panel);
        
        }
    }*/
    
     
    
    
    /*public void visualizarPantalla(){
        pantalla.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);  
        pantalla.setVisible ( true ); 
        pantalla.setResizable(false);
    }*/
}
 
