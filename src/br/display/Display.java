package br.display;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Display {
    private JFrame jframe;
    private Canvas canvas;
    public Display(String titulo, int width, int heigth){

        canvas = new Canvas();

        // Preferencias de layout
        canvas.setPreferredSize(new Dimension(width, heigth));
        canvas.setMaximumSize(new Dimension(width, heigth));
        canvas.setMinimumSize(new Dimension(width, heigth));

        jframe = new JFrame(titulo);

        // Desabilita o foco no canvas
        canvas.setFocusable(false);

        // Adiciona o canvas ao jframe
        jframe.add(canvas);
        jframe.pack();

        // Configura a janela do jframe
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setResizable(false);
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);

    }

    public BufferStrategy getBufferStrategy(){
        return canvas.getBufferStrategy();
    }
    public void createBufferStrategy(){
        canvas.createBufferStrategy(3);
    }
    public void setKeyListener(KeyListener kl){
        jframe.addKeyListener(kl);
    }
}
