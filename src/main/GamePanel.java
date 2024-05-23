package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSIze = 16;
    final int scale = 4;
    static int highscore = 0;
    final int tileSize = originalTileSIze * scale;
    final int maxScreenCol = 23;
    final int maxScreenRow = 17;
    int bullets = 0;
    int counter=1;
    boolean cnt = true;
    int points = 0;
    bullet b1 = new bullet(-50,-50,10, false);
    bullet b2 = new bullet(-50,-50,10, false);
    bullet b3 = new bullet(-50,-50,10, false);
    bullet b4 = new bullet(-50,-50,10, false);
    bullet bulletarray[] = {b1,b2,b3,b4};
    int b1dx = 0;
    int b1dy = 0;

    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;
    BufferedImage playerimage = ImageIO.read(new File(System.getProperty("user.dir")+"/noah.png"));
    BufferedImage enemyimage = ImageIO.read(new File(System.getProperty("user.dir")+"/cock.png"));
    BufferedImage enemyhardimage = ImageIO.read(new File(System.getProperty("user.dir")+"/cock2.png"));
    BufferedImage enemyharderimage = ImageIO.read(new File(System.getProperty("user.dir")+"/cock3.png"));
    BufferedImage hotplayerimage = ImageIO.read(new File(System.getProperty("user.dir")+"/hotnoah.png"));
    BufferedImage bullet = ImageIO.read(new File(System.getProperty("user.dir")+"/bullet.png"));
    BufferedImage shootimage = ImageIO.read(new File(System.getProperty("user.dir")+"/bullet.png"));
    BufferedImage bulletss = ImageIO.read(new File(System.getProperty("user.dir")+"/bullets.png"));
    BufferedImage fire = ImageIO.read(new File(System.getProperty("user.dir")+"/fire.png"));
    BufferedImage grass = ImageIO.read(new File(System.getProperty("user.dir")+"/grass.png"));
    BufferedImage pointstext = ImageIO.read(new File(System.getProperty("user.dir")+"/points.png"));
    BufferedImage highscoretext = ImageIO.read(new File(System.getProperty("user.dir")+"/highscore.png"));
    BufferedImage healthtext = ImageIO.read(new File(System.getProperty("user.dir")+"/health.png"));
    BufferedImage bulletstext = ImageIO.read(new File(System.getProperty("user.dir")+"/bulletstext.png"));


    Image resultingitemImage,resultingitem2Image,resultingitem3Image;
    int fps = 60;
    KeyHandler keyH = new KeyHandler();
    MouseHandler MouseH = new MouseHandler();

    Thread gameThread;

    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    enemy easy1 = new enemy(tileSize,screenWidth-tileSize*2,screenHeight-tileSize*2,2,1, true);
    enemy easy2 = new enemy((int)(tileSize*1.5),screenWidth/2,-tileSize*2,3,2, true);
    enemy easy3 = new enemy((int)(tileSize/2),-100,screenHeight/2,1,3, true);
    int fulllifearr[] = {2,3,1};
    enemy enemyarr[] = {easy1,easy2,easy3};
    double originpossibleenemyarr[][]={
            {tileSize,screenWidth-tileSize*2,screenHeight-tileSize*2,2,1.2},
            {(int)(tileSize*1.5),screenWidth/2,-tileSize*2,3,1.8},
            {(tileSize/2),-100,screenHeight/2,1,2.5},
            {tileSize*2,screenWidth/2,screenWidth+2*tileSize,3,1.4},
            {tileSize,-tileSize,-tileSize,5,1.9}
    };
    double possibleenemyarr[][]={
            {tileSize,screenWidth-tileSize*2,screenHeight-tileSize*2,2,1.2},
            {(int)(tileSize*1.5),screenWidth/2,-tileSize*2,3,1.8},
            {(tileSize/2),-100,screenHeight/2,1,2.5},
            {tileSize*2,screenWidth/2,screenWidth+2*tileSize,3,1.4},
            {tileSize,-tileSize,-tileSize,5,1.9}
    };
    double hardpossibleenemyarr[][]={
            {tileSize*6,screenWidth-tileSize*2,screenHeight-tileSize*2,2,2.5},
            {(tileSize*5),screenWidth/2,-tileSize*2,3,3.2},
            {(tileSize*4),-100,screenHeight/2,1,2.6},
            {tileSize*8,screenWidth/2,screenWidth+2*tileSize,3,2.2},
            {tileSize*10,-tileSize,-tileSize,5,2.3}
    };

    public GamePanel() throws IOException {
        this.addKeyListener(keyH);
        this.addMouseListener(MouseH);
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
        public int[][] walkarr = new int[maxScreenCol][maxScreenRow];
        public void initarr(){

        for(int i = 0; i < maxScreenCol; i++){
            for(int j = 0; j < maxScreenRow; j++){
                if(i==0 || i==maxScreenCol-1 || j==0 || j==maxScreenRow-1){
                    walkarr[i][j] = 1;
                }
            }
        }
        }
    @Override
    public void run() {
        initarr();
        readscore();
        for(int i=0;i<3;i++){
            collectitem(itemarr[i]);
        }

        double drawInterval = 1000000000/fps;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                Thread.sleep(Math.abs((long)(remainingTime/1000000)));
                nextDrawTime = nextDrawTime + drawInterval;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public double score = 0;
    item item = new item(0,"item",0,0,true,100,100);
    item item2 = new item(1,"item",0,0,true,100,100);
    item item3 = new item(2,"item",0,0,true,100,100);

    item itemarr[] = {item,item2,item3};

    boolean gonestage2 = false;
    boolean gonestage3 = false;
    int reloadtimer = 0;
    int vecdy,vecdx;
    double wichtig = 0;
    public void update() {
        reloadtimer++;
        if(keyH.escapePressed){
            gameThread = null;
            System.exit(0);
        }
        for(int i=0;i<3;i++){
            itemarr[i].itemcounter++;
        }
        if(keyH.upPressed && free(1)){
            playerY -= playerSpeed;
        }
        if(keyH.downPressed && free(2)){
            playerY += playerSpeed;
        }
        if(keyH.leftPressed && free(3)){
            playerX -= playerSpeed;
        }
        if(keyH.rightPressed && free(4)){
            playerX += playerSpeed;
        }
        if(keyH.enterPressed && bullets>=1 && reloadtimer>30){
            shootbullet();
            reloadtimer = 0;
        }
        if(MouseH.leftclicked && bullets>=1 && reloadtimer>30){
            shootbullet();
            reloadtimer = 0;
        }
        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int x = (int) b.getX();
        int y = (int) b.getY();

        if(reloadtimer<=5){
            vecdx = x-playerX;
            vecdy = y-playerY;
            wichtig = Math.sqrt(vecdx*vecdx+vecdy*vecdy);
        }
        for(int i=0;i<bulletarray.length;i++){
            if(bulletarray[i].exists){
                bulletarray[i].y = (int)(bulletarray[i].y + (vecdy * bulletarray[i].speed / wichtig ));
                bulletarray[i].x = (int)(bulletarray[i].x + (vecdx * bulletarray[i].speed / wichtig ));
            }
        }
        boolean gethoter = false;
for(int i=0;i<3;i++){
    if((enemyarr[i].x+(0.5*enemyarr[i].size))>=(playerX+(0.5*size))){
        enemyarr[i].x-= enemyarr[i].speed;
    }else enemyarr[i].x += enemyarr[i].speed;

    if((enemyarr[i].y+(0.5*enemyarr[i].size))>=(playerY+(0.5*size))){
        enemyarr[i].y -= enemyarr[i].speed;
    }else enemyarr[i].y += enemyarr[i].speed;
}
for(int i=0;i<3;i++){
    if(cnt && enemyarr[i].shown) {
        if ((playerX + size >= enemyarr[i].x && enemyarr[i].x + enemyarr[i].size >= playerX) && (playerY + size >= enemyarr[i].y && enemyarr[i].y + enemyarr[i].size >= playerY)) {
            gethoter = true;
            break;
        } else if (score >= 0) {
            gethoter = false;
        }
    }
}
        if(gethoter){
            score += 0.03;
        }else if(score>0.01 && playerSpeed>0) score -= 0.01;

        for(int i=0;i<3;i++){
            if(itemarr[i].itemcounter>=600){
                itemarr[i].itemcounter = 0;
                respawnitem(itemarr[i]);
            }
        }
        for(int j=0;j<bulletarray.length;j++){
            if(bulletarray[j].x<=0 || bulletarray[j].x>=screenWidth || bulletarray[j].y<=0 || bulletarray[j].y>=screenHeight){
                bulletarray[j].exists = false;
            }
        }
        for(int j=0;j<bulletarray.length;j++){
            for(int i=0;i<3;i++){
                if(enemyarr[i].shown && bulletarray[j].exists && (bulletarray[j].x>=enemyarr[i].x-size && bulletarray[j].x<=enemyarr[i].x+enemyarr[i].size-originalTileSIze) && (bulletarray[j].y>=enemyarr[i].y-size && bulletarray[j].y<=enemyarr[i].y+enemyarr[i].size)){
                    enemyarr[i].life--;
                    points+=counter;
                    bulletarray[i].x = -100;
                    bulletarray[i].y = -100;
                    bulletarray[j].exists = false;
                }
            }
        }
       if(points>=50 && !gonestage2){
           for(int i=0;i<5;i++){
               possibleenemyarr[i][0]*=3;
               possibleenemyarr[i][3]+=3;
               possibleenemyarr[i][4]+=0.3;
           }
           possibleenemyarr[2][4]-=0.3;
           gonestage2 = true;
       }
        if(points>=200 && !gonestage3){
            for(int i=0;i<5;i++){
                for(int j=0;j<5;j++){
                    possibleenemyarr[i][j]=hardpossibleenemyarr[i][j];
                }
            }
            possibleenemyarr[2][4]-=0.1;
            gonestage2 = true;
        }
        for(int i=0;i<3;i++) {
            if (enemyarr[i].life == 0) {
                int rand = ThreadLocalRandom.current().nextInt(0, 3);
                enemyarr[i].size = (int)possibleenemyarr[rand][0];
                enemyarr[i].x = possibleenemyarr[rand][1];
                enemyarr[i].y = possibleenemyarr[rand][2];
                enemyarr[i].life = (int)possibleenemyarr[rand][3];
                fulllifearr[i]=enemyarr[i].life;
                enemyarr[i].speed = possibleenemyarr[rand][4];
                counter++;
            }
        }
        for(int i=0;i<3;i++){
            collectitem(itemarr[i]);
        }
    }
    public void shootbullet() {
        bullet curbul = null;
        for(int j = 0;j<bulletarray.length;j++){
            if(!bulletarray[j].exists){
                curbul = bulletarray[j];
                break;
            }
        }
        if(curbul==null) return;
        if(curbul.x>=screenWidth || curbul.y >=screenWidth || curbul.x<=0 || curbul.y<=0) curbul.exists = false;
        if (curbul.exists == false) {
            bullets--;
            curbul.y = playerY;
            curbul.x = playerX;
            curbul.exists = true;
        }
    }
    public void respawnitem(item currentitem){
        currentitem.x = ThreadLocalRandom.current().nextInt(64, screenWidth - 128);
        currentitem.y = ThreadLocalRandom.current().nextInt(64, screenHeight - 128);
        currentitem.type = ThreadLocalRandom.current().nextInt(1, 4);
        switch(currentitem.index){
            case 0:
                switch (currentitem.type) {
                    case 1:
                        resultingitemImage = fire.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    case 2:
                        resultingitemImage = bullet.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    case 3:
                        resultingitemImage = bulletss.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                switch (currentitem.type) {
                    case 1:
                        resultingitem2Image = fire.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    case 2:
                        resultingitem2Image = bullet.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    case 3:
                        resultingitem2Image = bulletss.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    default:
                        break;
                }
                ;break;
            case 2:
                switch (currentitem.type) {
                    case 1:
                        resultingitem3Image = fire.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    case 2:
                        resultingitem3Image = bullet.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    case 3:
                        resultingitem3Image = bulletss.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    default:
                        break;
                }
                ;break;
            default: break;
        }
        BufferedImage outputitemImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
    }
    public void collectitem(item currentitem) {
        if ((Math.abs(playerX - currentitem.x) <= tileSize) && (Math.abs(playerY - currentitem.y) <= tileSize)) {
            currentitem.itemcounter = 0;
            switch (currentitem.type) {
                case 1:
                    score += 10;
                    break;
                case 2:
                    bullets++;
                    break;
                case 3:
                    bullets += 2;
                    break;
                default:
                    break;
            }
            if(bullets>=15) bullets = 15;
            currentitem.x = ThreadLocalRandom.current().nextInt(64, screenWidth - 128);
            currentitem.y = ThreadLocalRandom.current().nextInt(64, screenHeight - 128);
            currentitem.type = ThreadLocalRandom.current().nextInt(1, 4);
            switch(currentitem.index){
            case 0:
            switch (currentitem.type) {
                case 1:
                    resultingitemImage = fire.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                    break;
                case 2:
                    resultingitemImage = bullet.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                    break;
                case 3:
                    resultingitemImage = bulletss.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                    break;
                default:
                    break;
            }
            break;
            case 1:
                switch (currentitem.type) {
                    case 1:
                        resultingitem2Image = fire.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    case 2:
                        resultingitem2Image = bullet.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    case 3:
                        resultingitem2Image = bulletss.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    default:
                        break;
                }
            ;break;
            case 2:
                switch (currentitem.type) {
                    case 1:
                        resultingitem3Image = fire.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    case 2:
                        resultingitem3Image = bullet.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    case 3:
                        resultingitem3Image = bulletss.getScaledInstance(size, size, Image.SCALE_DEFAULT);
                        break;
                    default:
                        break;
                }
            ;break;
                default: break;
        }
        }

    }
    public boolean free(int dir){
        switch(dir){
            case 1:if(playerY-playerSpeed==tileSize) return false;
            break;
            case 2:if(playerY+playerSpeed==screenHeight-(tileSize*2)) return false;
            break;
            case 3:if(playerX-playerSpeed==tileSize) return false;
            break;
            case 4:if(playerX+playerSpeed==screenWidth-(tileSize*2)) return false;
            break;
        }

            return true;
    }

    int size = tileSize;

    public void drawarray(Graphics g){
        for(int i = 0; i < maxScreenCol; i++){
            for(int j = 0; j < maxScreenRow; j++){
                if(walkarr[i][j]==1){
                    g.fillRect(i*size, j*size, size, size);
                }
            }
        }
    }
    public void whenAppendStringUsingBufferedWritter_thenOldContentShouldExistToo(int highscore)
        throws IOException {
        new FileWriter(System.getProperty("user.dir")+"/highscore.txt", false).close();
        File fileName = new File(System.getProperty("user.dir")+"/highscore.txt");
        String str = String.valueOf(highscore);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.write(str);
        writer.close();
    }
    public static void readscore(){
        try {
            File myObj = new File(System.getProperty("user.dir")+"/highscore.txt");
            Scanner myReader = new Scanner(myObj);
                String data = myReader.nextLine();
                highscore = Integer.parseInt(data);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //ersetzt Bild
        Graphics2D g2 = (Graphics2D) g; //größere Lib
        g.setColor(Color.green);
        g.fillRect(0, 0, screenWidth, screenHeight);

        Image grassimage = grass.getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT); //get Sprite
        for(int i=1;i<maxScreenCol;i++){
            for(int j=1;j<maxScreenRow;j++){
                g.drawImage(grassimage,i*tileSize,j*tileSize,null);
            }
        }

        g.setColor(Color.black);
        drawarray(g);
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
        g.drawRect(screenWidth+4*tileSize,(int)(tileSize*2.2),tileSize*2,tileSize);
        g.drawRect(screenWidth+4*tileSize,(int)(tileSize*4.2),tileSize*2,tileSize);
        g.setColor(Color.black);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        g.setColor(Color.white);
        g2.drawString(""+points,screenWidth+(int)(4.2*tileSize),(int)(tileSize*2.8));
        g2.drawString(""+highscore,screenWidth+(int)(4.2*tileSize),(int)(tileSize*4.8));
        g.drawRect(screenWidth-8, 100,tileSize*2,screenHeight-200);
        g.setColor(Color.red);
        g.fillRect(screenWidth-4, screenHeight-104,tileSize*2-6,-(int)((screenHeight-207)*(score/100)));
        for(int i=0;i<3;i++){
            if(enemyarr[i].shown){
                if(enemyarr[i].size>=tileSize*5){
                    Image resultingenemyImage = enemyharderimage.getScaledInstance((int)enemyarr[i].size, (int)enemyarr[i].size, Image.SCALE_DEFAULT); //get Sprite
                    g.drawImage(resultingenemyImage,(int)enemyarr[i].x,(int)enemyarr[i].y,null);
                }else if(enemyarr[i].size>=tileSize*2){
                    Image resultingenemyImage = enemyhardimage.getScaledInstance((int)enemyarr[i].size, (int)enemyarr[i].size, Image.SCALE_DEFAULT); //get Sprite
                    g.drawImage(resultingenemyImage,(int)enemyarr[i].x,(int)enemyarr[i].y,null);
                }else{
                Image resultingenemyImage = enemyimage.getScaledInstance((int)enemyarr[i].size, (int)enemyarr[i].size, Image.SCALE_DEFAULT); //get Sprite
                g.drawImage(resultingenemyImage,(int)enemyarr[i].x,(int)enemyarr[i].y,null);
                }
                g.setColor(Color.black);
                g.fillRect((int)enemyarr[i].x-2,(int)enemyarr[i].y-16,(int)enemyarr[i].size+4,9);
                g2.setColor(Color.red);
                g.fillRect((int)enemyarr[i].x,(int)enemyarr[i].y-14,(int)(enemyarr[i].size*(((double)enemyarr[i].life)/(fulllifearr[i]))),5);
            }
        }
        g.setColor(Color.white);
        g.drawRect(screenWidth+2*tileSize, 100,(int)(tileSize*1.45),screenHeight-200);
        for(int i=bullets;i>0;i--){
            Image magazine = bullet.getScaledInstance(size, size, Image.SCALE_DEFAULT);
            g.drawImage(magazine,(int)(screenWidth+(tileSize*2.25)),screenHeight-(tileSize*2)-(i*(tileSize*6/7)),null);
        }

    for(int j=0;j<bulletarray.length;j++){
        if(bulletarray[j].exists){
            Image resultingshootImage = shootimage.getScaledInstance(size, size, Image.SCALE_DEFAULT); //get Sprite
            BufferedImage outputshootImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB); // ''
            g.drawImage(resultingshootImage,bulletarray[j].x,bulletarray[j].y,null);
        }
    }


        g.drawImage(resultingitemImage,itemarr[0].x,itemarr[0].y,null);
        g.drawImage(resultingitem2Image,itemarr[1].x,itemarr[1].y,null);
        g.drawImage(resultingitem3Image,itemarr[2].x,itemarr[2].y,null);

        if(score>=80){
            Image resultingImage = hotplayerimage.getScaledInstance(size, size, Image.SCALE_DEFAULT); //get Sprite
            BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB); // ''
            g.drawImage(resultingImage, playerX, playerY, null);
        }else {
            Image resultingImage = playerimage.getScaledInstance(size, size, Image.SCALE_DEFAULT); //get Sprite
            BufferedImage outputImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB); // ''
            g.drawImage(resultingImage, playerX, playerY, null);
        }
        if(score>100){
            score = 100.01;
            cnt = false;
            g.setColor(Color.red);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 100));
            g2.drawString("YOU SUCK AT THIS GAME",screenWidth/12,screenHeight/2);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
            g2.drawString("Press Space to restart",screenWidth/12,screenHeight*2/3);
            g2.drawString("Press Escape to end Game",screenWidth/12,screenHeight*2/3+tileSize);

            playerSpeed = 0;
            if(keyH.spacePressed){
                cnt = true;
                if(points>highscore) highscore = points;
                try {
                    whenAppendStringUsingBufferedWritter_thenOldContentShouldExistToo(highscore);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                score = 0;
                playerX = 100;
                playerY = 100;
                points = 0;
                counter = 1;
                bullets = 0;
                for(int i=0;i<5;i++){
                    for(int j=0;j<5;j++){
                        possibleenemyarr[i][j]=originpossibleenemyarr[i][j];
                    }
                }
                possibleenemyarr[2][4]+=0.3;
                gonestage2 = false;
                for(int i=0;i<3;i++) {
                    int rand = ThreadLocalRandom.current().nextInt(0, 3);
                    enemyarr[i].size = possibleenemyarr[rand][0];
                    enemyarr[i].x = possibleenemyarr[rand][1];
                    enemyarr[i].y = possibleenemyarr[rand][2];
                    enemyarr[i].life = (int)possibleenemyarr[rand][3];
                    enemyarr[i].speed = possibleenemyarr[rand][4];
                    enemyarr[i].shown = true;
                }
                playerSpeed = 4;
            }
        }
            g2.dispose();//male
    }
}
