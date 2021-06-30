package br.states;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class StateManager implements KeyListener {
    public static final int numberStates = 4;
    public static State[] states = new State[numberStates];
    public static int currentState = 0;

    public static final int FPS = 0, MENU = 1, LEVEL = 2, GAME_OVER = 3;

    public static void setState(int stade){
        currentState = stade;
        states[currentState].init();
    }
    public StateManager(){
        states[0] = new FPSState();
        states[1] = new MenuState();
        states[2] = new Level();
        states[3] = new GameOver();
    }
    public void update(){
        states[currentState].update();
    }
    public void render(Graphics g){
        states[currentState].render(g);
    }
    public static State getState(){
        return states[currentState];
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        states[currentState].KeyPress(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        states[currentState].KeyReleased(e.getKeyCode());
    }
}
