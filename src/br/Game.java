package br;

import br.display.Display;
import br.input.KeyManager;
import br.states.StateManager;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game implements Runnable{

    private Display display;
    public static final int WIDTH = 500, HEIGTH = 650;

    private Thread thread;
    private boolean running = false;

    private StateManager sm;
    private KeyManager km;

    public Game(){
        display = new Display("Atari BreakOut", WIDTH, HEIGTH);
        sm = new StateManager();
        km = new KeyManager();

        display.setKeyListener(sm);
        display.setKeyListener(km);
        StateManager.setState(StateManager.MENU);
    }

    @Override
    public void run() {
        init();
        int FPS = 60;
        double timePerTick = 1000000000 / FPS;
        double delta = 0;
        long now, lastTime = System.nanoTime();

        while(running){
            now = System.nanoTime();
            delta += ( now - lastTime ) / timePerTick;
            lastTime = now;

            if(delta >= 1){
                update();
                render();
                delta--;
            }
        }
        stop();
    }

    private void render() {
        BufferStrategy bs = display.getBufferStrategy();
        if(bs == null){
            display.createBufferStrategy();
            bs = display.getBufferStrategy();
        }

        Graphics g = bs.getDrawGraphics();
        g.clearRect(0,0, WIDTH, HEIGTH);
        if(StateManager.getState() != null){ sm.render(g); }

        g.dispose();
        bs.show();
    }

    private void update() {

        if(StateManager.getState() == null){ return; }
        sm.update();
        km.update();

    }

    private void init() {

    }
    public synchronized void start() {
        if (thread != null) { return; }
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        if(thread == null) { return; }
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
