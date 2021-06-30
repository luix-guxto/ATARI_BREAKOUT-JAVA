package br.states;

import br.Game;
import br.audios.AudioPlayer;
import br.input.KeyManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Level implements State{

    private Random rm = new Random();
    private AudioPlayer pong, music;
    public Level(){
        pong = new AudioPlayer("/audios/pong.mp3");
        music = new AudioPlayer("/audios/theblackframe.mp3");
    }

    private final int brickWidth = 50;
    private boolean inicio;
    private int upLife = 0;
    private final int brickHeigth = 20;
    private final Color[] colors = {Color.GREEN, Color.CYAN, Color.RED, Color.BLUE, Color.gray, Color.LIGHT_GRAY, Color.MAGENTA, Color.YELLOW};
    private final int linhaBrick = Game.WIDTH/brickWidth;
    private Rectangle ball = new Rectangle( Game.WIDTH/2-5, Game.HEIGTH/2-5, 20, 20);
    private Rectangle p1 = new Rectangle(Game.WIDTH/2-30, Game.HEIGTH-60, 80, 20);

    private final int vex = 5;
    private final int vey = 4;
    private int velp1;
    private final int vemax=10;
    private int velocidadeX = vex, velocidadeY = vey, velocidadePlayer = 0;
    private int wins = 0, score = 0, life = 3;
    private boolean cheat = false;

    private Color[] cores1 = new Color[linhaBrick];
    private Color[] cores2 = new Color[linhaBrick];
    private Color[] cores3 = new Color[linhaBrick];

    private boolean[] destruidos1 = new boolean[linhaBrick];
    private boolean[] destruidos2 = new boolean[linhaBrick];
    private boolean[] destruidos3 = new boolean[linhaBrick];

    private Rectangle[] bricks1 = new Rectangle[linhaBrick];
    private Rectangle[] bricks2 = new Rectangle[linhaBrick];
    private Rectangle[] bricks3 = new Rectangle[linhaBrick];

    @Override
    public void init() {

        music.start();
        int side = rm.nextInt(2)+1;

        if(side==1) {
            velocidadeX = vex;
            velocidadeY = Math.round(vey+(wins * vey*1/10));
        }else{
            velocidadeX = vex * -1;
            velocidadeY = vey+wins;
        }
        if(velocidadeY>vemax){
            velocidadeY=vemax;
        }else if(velocidadeY<(vemax*-1)){
            velocidadeY=(vemax*-1);
        }
        velp1=velocidadeY+5;


        ball.x=p1.x+p1.width/2;
        ball.y=p1.y-ball.width;

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
        	int brickX = (1+brickWidth)*x;
            bricks1[x]=new Rectangle(brickX, brickHeigth,brickWidth, brickHeigth);
        }
        for(int x = 0; x<linhaBrick; x++){
        	int brickX = (1+brickWidth)*x;
            bricks2[x]=new Rectangle(brickX, brickHeigth*2+1,brickWidth, brickHeigth);
        }
        for(int x = 0; x<linhaBrick; x++){
            int brickX = (1+brickWidth)*x;
            bricks3[x]=new Rectangle(brickX, brickHeigth*3+2,brickWidth, brickHeigth);
        }
        inicio=true;
    }


    private void gameOver() {
        GameOver.setPontuacao(wins, score);
        music.stop();
        wins = 0;
        score = 0;
        life = 3;
        StateManager.setState(StateManager.GAME_OVER);
    }

    @Override
    public void update() {
        if(inicio){
            ball.x=p1.x+p1.width/2;
            ball.y=p1.y-ball.height;
            if(KeyManager.b || KeyManager.espace){
                inicio=false;
            }
        }else {
            ball.x += velocidadeX;
            ball.y += velocidadeY;
        }

        if(KeyManager.a || KeyManager.left){
            velocidadePlayer = -velp1;
        }else if(KeyManager.d || KeyManager.rigth){
            velocidadePlayer = velp1;
        }else if(KeyManager.c){
            if(cheat) {
                velocidadePlayer = velocidadeX;
                p1.x = ball.x - p1.width / 2;
            }
        }else{
            velocidadePlayer = 0;
        }


        p1.x+=velocidadePlayer;

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
            upLife++;
            if(upLife == 3){
                life++;
                upLife=0;
            }
            wins++;
            init();
        }
    }

    private void limits() {
        if(ball.x < 0){
            ball.x=0;
            velocidadeX*=-1;
            pong.start();
        }
        if(ball.x + ball.width > Game.WIDTH){
            ball.x= Game.WIDTH - ball.width;
            velocidadeX*=-1;
            pong.start();
        }
        if(ball.y < 0){
            ball.y=0;
            velocidadeY*=-1;
            pong.start();
        }
        if(ball.y + ball.height > Game.HEIGTH){
            life--;
            music.start();
            inicio=true;
            if(life<=0){
                gameOver();
            }
            ball.y=Game.WIDTH/2-ball.width/2;
            ball.x=Game.HEIGTH/2- ball.height/2;
        }

        // Linha 1 brick
        for(int x = 0; x<linhaBrick; x++){
            if(destruidos1[x]) {
                if (ball.intersects(bricks1[x])) {
                    velocidadeY *= -1;
                    destruidos1[x] = false;
                    pong.start();
                    score+=30+(30*wins);
                }
            }
        }

        // Linha 2 brick
        for(int x = 0; x<linhaBrick; x++){
            if(destruidos2[x]) {
                if (ball.intersects(bricks2[x])) {
                    velocidadeY *= -1;
                    destruidos2[x] = false;
                    pong.start();
                    score+=20+(20*wins);
                }
            }
        }

        // Linha 3 brick
        for(int x = 0; x<linhaBrick; x++){
            if(destruidos3[x]) {
                if (ball.intersects(bricks3[x])) {
                    velocidadeY *= -1;
                    destruidos3[x] = false;
                    pong.start();
                    score+=10+(10*wins);
                }
            }
        }

        if(ball.intersects(p1)){
            velocidadeY*=-1;
            ball.y=p1.y-ball.width;
            if(velocidadePlayer<0){
                velocidadeX=rm.nextInt(5)-5;
                pong.start();
            }
            else if(velocidadePlayer>0){
                velocidadeX=rm.nextInt(5);
                pong.start();
            }


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

        // Tela Preta
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Game.WIDTH, Game.HEIGTH);

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

        // Score
        g.setColor(Color.WHITE);
        Font font = new Font(Font.DIALOG, Font.PLAIN, 20);
        g.setFont(font);
        g.drawString("Score: "+score, 2, Game.HEIGTH-3);
        g.drawString("Vitorias: "+wins, 10+g.getFontMetrics().stringWidth("Score: "+score), Game.HEIGTH-3);
        if(life<=5) {
            for(int x = 0; x<life;x++) {
                g.fillOval(
                        ( Game.WIDTH - 35 ) - ( 30 * x ),
                        Game.HEIGTH - 35,
                        30,
                        30);
            }
        }else{
            g.drawString("Vidas: "+life, Game.WIDTH-g.getFontMetrics().stringWidth("Vidas: "+life), Game.HEIGTH-3);
        }

        // Cheat
        if(cheat){
            g.setColor(Color.RED);
            g.fillOval(Game.WIDTH/2-5, Game.HEIGTH-3, 10, 10);
        }
    }

    @Override
    public void KeyPress(int cod) {

    }

    @Override
    public void KeyReleased(int cod) {
        if(cod == KeyEvent.VK_ESCAPE){
            StateManager.setState(StateManager.MENU);
        }
        if(cod == KeyEvent.VK_F12){
            if(cheat){
                cheat=false;
            }else{
                cheat=true;
            }
        }
        if(cod == KeyEvent.VK_L){
            if(cheat){
                life++;
            }
        }
        if(cod == KeyEvent.VK_ESCAPE){
            wins=0;
            score=0;
            life=3;
            music.stop();
            StateManager.setState(StateManager.MENU);
        }
        if(cod == KeyEvent.VK_V){
            if(cheat) {
                for (int x = 0; x < linhaBrick; x++) {
                    destruidos1[x] = false;
                }
                for (int x = 0; x < linhaBrick; x++) {
                    destruidos2[x] = false;
                }
                for (int x = 0; x < linhaBrick; x++) {
                    destruidos3[x] = false;
                }
            }
        }
    }

}
