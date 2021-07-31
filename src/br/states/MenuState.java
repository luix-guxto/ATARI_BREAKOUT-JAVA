package br.states;

import br.Game;
import br.audios.AudioPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class MenuState implements State{

    private final Random rm = new Random();
    private AudioPlayer pong;
    public MenuState(){
        pong = new AudioPlayer("/audios/pong.mp3");
    }
    private final int brickWidth = 30;
    private final int brickHeigth = 10;
    private final Color[] colors = {Color.GREEN, Color.CYAN, Color.RED, Color.BLUE, Color.gray, Color.LIGHT_GRAY, Color.MAGENTA, Color.YELLOW};
    private final int linhaBrick = Game.WIDTH/brickWidth;

    private Color[] cores1 = new Color[linhaBrick];
    private Color[] cores2 = new Color[linhaBrick];
    private Color[] cores3 = new Color[linhaBrick];

    private boolean[] destruidos1 = new boolean[linhaBrick];
    private boolean[] destruidos2 = new boolean[linhaBrick];
    private boolean[] destruidos3 = new boolean[linhaBrick];

    private Rectangle[] bricks1 = new Rectangle[linhaBrick];
    private Rectangle[] bricks2 = new Rectangle[linhaBrick];
    private Rectangle[] bricks3 = new Rectangle[linhaBrick];


    private final String[] options = { "START", "HELP", "EXIT"};
    private Font font1, font2;
    private final Rectangle ball = new Rectangle(Game.WIDTH/2-5, Game.HEIGTH/2-5, 10, 10);
    private final Rectangle out = new Rectangle(ball.x-25,ball.y*2-ball.height*2, 50,10);

    private int moveBallx = rm.nextInt(9)+1, moveBally = 5;
    private int choice = 0;


    @Override
    public void init() {
       font1 = new Font(Font.SANS_SERIF, Font.PLAIN, Game.WIDTH*1/8);
       font2 = new Font(Font.SERIF, Font.PLAIN, Game.WIDTH*1/16);

        moveBallx = rm.nextInt(16)-8;
        moveBally = 5;

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
        ball.y+=moveBally;
        ball.x+=moveBallx;
        out.x=ball.x-out.width/2;
        limits();
        vitoria();
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
            init();
        }
    }
    private void limits() {
        if(ball.x+ball.width>Game.WIDTH){
            moveBallx*=-1;
            pong.start();
        }
        if(ball.x<0){
            moveBallx*=-1;
            pong.start();
        }
        if(ball.y+ball.height> Game.HEIGTH){
            moveBally*=-1;
            pong.start();
        }
        if(ball.y<0){
            moveBally*=-1;
            pong.start();
        }
        if(out.x+out.width>Game.WIDTH){
            out.x=Game.WIDTH-out.width;
        }
        if(out.x<0){
            out.x=0;
        }
        if(ball.intersects(out)){
            moveBally*=-1;
            pong.start();
        }
        // Linha 1 brick
        for(int x = 0; x<linhaBrick; x++){
            if(destruidos1[x]) {
                if (ball.intersects(bricks1[x])) {
                    moveBally *= -1;
                    destruidos1[x] = false;
                    pong.start();
                }
            }
        }

        // Linha 2 brick
        for(int x = 0; x<linhaBrick; x++){
            if(destruidos2[x]) {
                if (ball.intersects(bricks2[x])) {
                    moveBally *= -1;
                    destruidos2[x] = false;
                    pong.start();
                }
            }
        }

        // Linha 3 brick
        for(int x = 0; x<linhaBrick; x++){
            if(destruidos3[x]) {
                if (ball.intersects(bricks3[x])) {
                    moveBally *= -1;
                    destruidos3[x] = false;
                    pong.start();
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {

        // Tela preta
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Game.WIDTH, Game.HEIGTH);

        // Bola
        g.setColor(Color.magenta);
        g.fillOval(ball.x, ball.y, ball.width, ball.height);

        // Out
        g.setColor(Color.orange);
        g.fillRect(out.x, out.y, out.width, out.height);

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

        // Titulo
        g.setFont(font1);
        g.setColor(Color.WHITE);
        g.drawString("ATARI - BreakOut", Game.WIDTH/2-g.getFontMetrics().stringWidth("ATARI - BreakOut")/2, Game.HEIGTH*3/7-g.getFontMetrics(font1).getHeight());

        // Opções
        g.setFont(font2);

        for(int i = 0; i < options.length; i++) {
            g.setColor(Color.WHITE);
            if(i==choice){
                g.setColor(Color.YELLOW);
            }
            g.drawString(options[i], Game.WIDTH/2-g.getFontMetrics().stringWidth(options[i])/2, Game.HEIGTH/2+g.getFontMetrics(font2).getHeight()*i);
        }
    }


    @Override
    public void KeyPress(int cod) {

    }

    @Override
    public void KeyReleased(int cod) {
        if(cod == KeyEvent.VK_UP || cod ==  KeyEvent.VK_W){
            choice--;
            if(choice<0) {
                choice = options.length - 1;
            }
        }
        if(cod == KeyEvent.VK_DOWN || cod == KeyEvent.VK_S){
            choice++;
            if(choice>=options.length){
                choice=0;
            }
        }
        if(cod == KeyEvent.VK_ENTER){
            escolha(choice);
        }
        if(cod == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
    }

    private void escolha(int choice) {
        switch (choice){
            case 0:
                StateManager.setState(StateManager.LEVEL);
                break;
            case 1:
                StateManager.setState(StateManager.FPS);
                break;
            case 2:
                System.exit(0);
                break;
            default:
                break;
        }
    }
}
