/*
 * Project Name: EE2311 Project - Gems Crush
 * Student Name:
 * Student ID:
 * 
 */
package example;

import game.GameConsole;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import javax.swing.ImageIcon;

/**
 * Demo for the use of:
 *
 * 1. create and display game console 2. start a game loop 3. create and display
 * gems 4. detect mouse click and toggle gem's selection 5. update screen at
 * predefined interval 6. draw text to show score/time information
 *
 * @author Your Name and ID
 */
public class Demo1 {

    // create the game console for drawing         
    // singleton, always return the same instance
    private GameConsole console = GameConsole.getInstance();

    public Demo1() {
        // make the console visible
        console.show();
    }

    public static void main(String[] args) {
        // a more OO approach to write the main method
        Demo1 game = new Demo1();
        game.startGame();
    }

    public void startGame() {

        Gem[][] gem = new Gem[8][8]; //2d array to save gems
        boolean match = false;
        do{
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int ran = (int) (Math.random() * 7); //create random gems
                gem[i][j] = new Gem(i, j, ran);
            }
        }
        match = Engine.isMatch(gem);
        }while(match == true);

        // board dimension can be obtained from console
        int width = console.getBoardWidth();
        int height = console.getBoardHeight();

        // set custom background image
        console.setBackground("/assets/board.png");
        Timer timer = new Timer();
        timer.start();
        // enter the main game loop
        Engine.Score Score = Engine.newScore();
        Engine.random2GemType(); //random next two gems
        Engine.resetSelectedXY(); 
        while (true) {

            // get whatever inputs
            Point point = console.getClickedPoint();
            Engine.showFocus(gem, point); //selected point 
            Engine.checkMatch(gem); //check if there any combo
            // refresh at the specific rate, default 25 fps
            if (console.shouldUpdate()) {
                console.clear();
                
                console.drawText(60, 150, "[TIME]", new Font("Helvetica", Font.BOLD, 20), Color.white);
                console.drawText(60, 180, timer.getTimeString(), new Font("Helvetica", Font.PLAIN, 20), Color.white);
                console.drawText(60, 250, "[Next 2 Gems]", new Font("Helvetica", Font.BOLD, 20), Color.white);
                
                console.drawImage(60,250,new ImageIcon(getClass().getResource(Gem.getTypeFile(Engine.getNextGem1Type()))).getImage());
                console.drawImage(120,250,new ImageIcon(getClass().getResource(Gem.getTypeFile(Engine.getNextGem2Type()))).getImage());
                
                console.drawText(60, 380, "[SCORE]", new Font("Helvetica", Font.BOLD, 20), Color.white);
                console.drawText(60, 410, Engine.showScore(), new Font("Helvetica", Font.PLAIN, 20), Color.white);
                for (int i = 0; i < 8; i++) { //display gems
                    for (int j = 0; j < 8; j++) {
                        gem[i][j].display();
                    }
                }
                console.update();
            }
            
            // the idle time affects the no. of iterations per second which 
            // should be larger than the frame rate
            // for fps at 25, it should not exceed 40ms
            console.idle(10); //1second
            if (timer.getCurrentTime() > 0) {
                timer.countDown();
            }

        }
    }   
    
    
}
