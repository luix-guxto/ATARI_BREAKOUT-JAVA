package br.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

    private boolean[] keys = new boolean[256];
    public static boolean a, d, left, rigth, c, b, espace;

    public void update(){
        a=keys[KeyEvent.VK_A];
        d=keys[KeyEvent.VK_D];
        left=keys[KeyEvent.VK_LEFT];
        rigth=keys[KeyEvent.VK_RIGHT];
        b=keys[KeyEvent.VK_B];
        c=keys[KeyEvent.VK_C];
        espace=keys[32];
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()<0||e.getKeyCode()>255){ return; }
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode()<0||e.getKeyCode()>255){ return; }
        keys[e.getKeyCode()] = false;
    }
}
