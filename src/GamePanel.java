import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class GamePanel extends JPanel implements ActionListener {
    static final int screen_width = 600;
    static final int screen_height = 600;
    static final int unit_size = 25;
    long moment_of_current_click = 0;
    long moment_of_previous_click = 0;
    static final int game_units = screen_width*screen_height/unit_size;
    int delay = 75;
    final int x[] = new int[game_units];
    final int y[] = new int[game_units];
    int body_parts = 6;
    int apples_eaten;
    int appleX;
    int appleY;
    char direction = 'r';
    int prev_click;
    int current_click;
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
//            for(int i=0; i<screen_height/unit_size; i++){
//                g.drawLine(i*unit_size,0, i*unit_size, screen_height);
//                g.drawLine(0, i*unit_size, screen_width, i*unit_size);
//            }
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unit_size, unit_size);
            for(int i=0; i<body_parts; i++){
                if(i==0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                }
                else{
                    g.setColor(new Color(45,180,0));
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + apples_eaten, (screen_width-metrics.stringWidth("Score: " + apples_eaten))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)screen_width/unit_size)*unit_size;
        appleY = random.nextInt((int)screen_height/unit_size)*unit_size;
    }
    public void move(){
        for(int i=body_parts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction){
            case 'u': y[0] = y[0] - unit_size;
            break;
            case 'd': y[0] = y[0] + unit_size;
            break;
            case 'l': x[0] = x[0] - unit_size;
            break;
            case 'r': x[0] = x[0] + unit_size;
        }
    }
    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            body_parts++;
            apples_eaten++;
            newApple();
        }
    }
    public void checkCollision(){
        //checks if head collides with body
        for(int i=body_parts; i>0; i--){
            if(x[0] == x[i] && y[0] == y[i]){
                running=false;
            }
        }
        //check if head touches left border
        if(x[0]<0){
            running=false;
        }
        //check if head touches right border
        if(x[0]>screen_width){
            running=false;
        }
        //check if head touches top border
        if(y[0]<0){
            running=false;
        }
        //check if head touches bottom border
        if(y[0]>screen_height){
            running=false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + apples_eaten, (screen_width-metrics1.stringWidth("Score: " + apples_eaten))/2, g.getFont().getSize());
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game over", (screen_width-metrics2.stringWidth("Game over"))/2, screen_height/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollision();
            repaint();
        }
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            moment_of_current_click = System.currentTimeMillis();
            current_click = e.getKeyCode();
            if (prev_click == current_click || Math.abs(moment_of_current_click-moment_of_previous_click)>delay){
                switch(current_click){
                    case KeyEvent.VK_LEFT:
                        if(direction!= 'r') {
                            direction = 'l';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if(direction!= 'l') {
                            direction = 'r';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if(direction!= 'd') {
                            direction = 'u';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if(direction!= 'u') {
                            direction = 'd';
                        }
                        break;
                }
            }
            System.out.println(direction);
            moment_of_previous_click = moment_of_current_click;
            prev_click = current_click;
        }
    }
}
