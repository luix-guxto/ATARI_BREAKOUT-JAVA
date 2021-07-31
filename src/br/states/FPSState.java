package br.states;

import br.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class FPSState implements State{

    private Random rm = new Random();

    private final int brickWidth = 30;
    private final int brickHeigth = 10;
    private final Color[] colors = {Color.GREEN, Color.CYAN, Color.RED, Color.BLUE, Color.gray, Color.LIGHT_GRAY, Color.MAGENTA, Color.YELLOW};
    private final int linhaBrick = Game.WIDTH/brickWidth;
    private Rectangle ball = new Rectangle( Game.WIDTH/2-5, Game.HEIGTH/2-5, 10,10);
    private Rectangle p1 = new Rectangle(Game.WIDTH/2-30, Game.HEIGTH-20, 60, 10);

    private final int vex = 5, vey = 5;
    private int velocidadeX = vex, velocidadeY = vey;
    private int wins = 0;

    private Color[] cores1 = new Color[linhaBrick];
    private Color[] cores2 = new Color[linhaBrick];
    private Color[] cores3 = new Color[linhaBrick];

    private boolean[] destruidos1 = new boolean[linhaBrick];
    private boolean[] destruidos2 = new boolean[linhaBrick];
    private boolean[] destruidos3 = new boolean[linhaBrick];

    private Rectangle[] bricks1 = new Rectangle[linhaBrick];
    private Rectangle[] bricks2 = new Rectangle[linhaBrick];
    private Rectangle[] bricks3 = new Rectangle[linhaBrick];

    private long now, lastTime = System.nanoTime();
    private double timer = 0;
    private int tick = 0;
    private int t;

    @Override
    public void init() {
        int side = rm.nextInt(2)+1;

        if(side==1) {
            velocidadeX = vex;
            velocidadeY = vey+(vey*wins);
        }else{
            velocidadeX = vex * -1;
            velocidadeY = (vey + (vey*wins))*-1;
        }

        ball.x=Game.WIDTH/2-ball.width;
        ball.y=Game.HEIGTH/2-ball.height;

        for(int x = 0; x<linhaBrick; x++){
            destruidos1[x]=true;
        }
        for(int x = 0; x<linhaBrick; x++){
            destruidos2[x]=true;
        }
        for(int x = 0; x<linhaBrick; x++){
            destruidos3[x]=true;
        }

        for(int x = 0; x<linhaBrick; x++){
            cores1[x]=colors[rm.nextInt(colors.length)];
        }
        for(int x = 0; x<linhaBrick; x++){
            cores2[x]=colors[rm.nextInt(colors.length)];
        }
        for(int x = 0; x<linhaBrick; x++){
            cores3[x]=colors[rm.nextInt(colors.length)];
        }

        for(int x = 0; x<linhaBrick; x++){
            int brickX = brickWidth*x+5;
            bricks1[x]=new Rectangle(brickX+3, brickHeigth,brickWidth, brickHeigth);
        }
        for(int x = 0; x<linhaBrick; x++){
            int brickX = brickWidth*x+5;
            bricks2[x]=new Rectangle(brickX+3, brickHeigth*2+3,brickWidth, brickHeigth);
        }
        for(int x = 0; x<linhaBrick; x++){
            int brickX = brickWidth*x+5;
            bricks3[x]=new Rectangle(brickX+3, brickHeigth*3+6,brickWidth, brickHeigth);
        }

    }

    @Override
    public void update() {
        now = System.nanoTime();
        timer += now-lastTime;
        lastTime = now;
        tick+=1;

        ball.x+=velocidadeX;
        ball.y+=velocidadeY;
        p1.x=ball.x;
        vitoria();
        limits();
    }
    private void vitoria() {
        boolean vitoria = true;

        for(int x = 0; x < linhaBrick; x++){
            if(destruidos1[x]){
                vitoria=false;
            }
        }

        for(int x = 0; x < linhaBrick; x++){
            if(destruidos2[x]){
                vitoria=false;
            }
        }

        for(int x = 0; x < linhaBrick; x++){
            if(destruidos3[x]){
                vitoria=false;
            }
        }


        if(vitoria){
            wins++;
            init();
        }
    }

    private void limits() {
        if(ball.x < 0){
            ball.x=0;
            velocidadeX*=-1;
        }
        if(ball.x + ball.width > Game.WIDTH){
            ball.x= Game.WIDTH - ball.width;
            velocidadeX*=-1;
        }
        if(ball.y < 0){
            ball.y = 0;
            velocidadeY*=-1;
        }
        if(ball.y + ball.height > Game.HEIGTH){
            ball.y = Game.HEIGTH - ball.height;
            velocidadeY*=-1;
        }

        // Linha 1 brick
        for(int x = 0; x<linhaBrick; x++){
            if(destruidos1[x]) {
                if (ball.intersects(bricks1[x])) {
                    velocidadeY *= -1;
                    destruidos1[x] = false;
                }
            }
        }

        // Linha 2 brick
        for(int x = 0; x<linhaBrick; x++){
            if(destruidos2[x]) {
                if (ball.intersects(bricks2[x])) {
                    velocidadeY *= -1;
                    destruidos2[x] = false;
                }
            }
        }

        // Linha 3 brick
        for(int x = 0; x<linhaBrick; x++){
            if(destruidos3[x]) {
                if (ball.intersects(bricks3[x])) {
                    velocidadeY *= -1;
                    destruidos3[x] = false;
                }
            }
        }

        if(ball.intersects(p1)){
            velocidadeY*=-1;

        }

        if(p1.x<0){
            p1.x=0;
        }
        if(p1.x + p1.width > Game.WIDTH){
            p1.x = Game.WIDTH - p1.width;
        }


    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, Game.WIDTH, Game.HEIGTH);

        // Bola
        g.setColor(Color.WHITE);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);

        // Player
        g.fillRect(p1.x, p1.y, p1.width, p1.height);

        // Linha 1
        for(int x = 0 ; x<linhaBrick; x++){
            g.setColor(cores1[x]);
            if(destruidos1[x]){
                g.fillRect(bricks1[x].x, bricks1[x].y,bricks1[x].width, bricks1[x].height);
            }
        }

        // Linha 2
        for(int x = 0 ; x<linhaBrick; x++){
            g.setColor(cores2[x]);
            if(destruidos2[x]){
                g.fillRect(bricks2[x].x, bricks2[x].y,bricks2[x].width, bricks2[x].height);
            }
        }

        // Linha 3
        for(int x = 0 ; x<linhaBrick; x++){
            g.setColor(cores3[x]);
            if(destruidos3[x]){
                g.fillRect(bricks3[x].x, bricks3[x].y,bricks3[x].width, bricks3[x].height);
            }
        }

        if(timer >= 1000000000){
            t=tick;
            tick=0;
            timer=0;
        }
        g.setColor(Color.WHITE);
        Font font = new Font(Font.DIALOG, Font.PLAIN, 50);
        g.setFont(font);
        String text = "TPS: "+t;

        int xText = Game.WIDTH/2-g.getFontMetrics().stringWidth(text)/2;
        int yText = Game.HEIGTH/2-g.getFontMetrics(font).getHeight()/2;

        g.drawString(text, xText, yText);
    }

    @Override
    public void KeyPress(int cod) {
        System.out.println("Press: "+ cod);
    }

    @Override
    public void KeyReleased(int cod) {
        System.out.println("Released:"+ cod);
        if(cod == KeyEvent.VK_ESCAPE){
            StateManager.setState(StateManager.MENU);
        }

    }
}
