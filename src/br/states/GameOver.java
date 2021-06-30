package br.states;

import br.Game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOver implements State{

    private static int win, pontuacao;
    private Font font1, font2;
    private String[] text = {"Score: ", "Vitorias: "};

    public static void setPontuacao(int wins, int score){
        win=wins;
        pontuacao=score;
    }

    @Override
    public void init() {
        font1 = new Font(Font.DIALOG_INPUT, Font.BOLD, Game.WIDTH*1/8);
        font2 = new Font(Font.MONOSPACED, Font.PLAIN, Game.WIDTH*1/16);
        text[0]+=pontuacao;
        text[1]+=win;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0,0, Game.WIDTH, Game.HEIGTH);


        g.setColor(Color.RED);
        g.setFont(font1);
        g.drawString("GAME-OVER",Game.WIDTH/2-g.getFontMetrics().stringWidth("GAME-OVER")/2, Game.HEIGTH*3/7-g.getFontMetrics(font1).getHeight());

        g.setColor(Color.WHITE);
        g.setFont(font2);
        for(int x = 0; x < text.length; x++){
            g.drawString(text[x], Game.WIDTH/2-g.getFontMetrics().stringWidth(text[x])/2, Game.HEIGTH/2+g.getFontMetrics(font2).getHeight()*x);
        }
    }

    @Override
    public void KeyPress(int cod) {

    }

    @Override
    public void KeyReleased(int cod) {
        if(cod == KeyEvent.VK_ESCAPE || cod == KeyEvent.VK_ENTER){
            StateManager.setState(StateManager.MENU);
        }
    }
}
