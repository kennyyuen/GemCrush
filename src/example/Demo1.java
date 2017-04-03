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

        Gem[] gem = new Gem[64];
        int x = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int ran = (int) (Math.random() * 7) + 1;
                gem[x] = new Gem(i, j, ran);
                x++;
            }
        }

        // board dimension can be obtained from console
        int width = console.getBoardWidth();
        int height = console.getBoardHeight();

        // set custom background image
        console.setBackground("/assets/board.png");
        Timer timer = new Timer();
        timer.start();
        // enter the main game loop
        int selectedGem = -1;
        while (true) {

            // get whatever inputs
            Point point = console.getClickedPoint();            
            if(isFocus(gem)){
                gem[selectedGem].toggleFocus();
            }
            selectedGem = showFocus(point,gem);
                
            System.out.println(selectedGem);
            // refresh at the specific rate, default 25 fps
            if (console.shouldUpdate()) {
                console.clear();

                console.drawText(60, 150, "[TIME]", new Font("Helvetica", Font.BOLD, 20), Color.white);
                console.drawText(60, 180, timer.getTimeString(), new Font("Helvetica", Font.PLAIN, 20), Color.white);

                console.drawText(60, 250, "[SCORE]", new Font("Helvetica", Font.BOLD, 20), Color.white);
                console.drawText(60, 280, "220", new Font("Helvetica", Font.PLAIN, 20), Color.white);
                for (int z = 0; z < 64; z++) {
                    gem[z].display();
                }

                console.update();
            }
            // the idle time affects the no. of iterations per second which 
            // should be larger than the frame rate
            // for fps at 25, it should not exceed 40ms
            console.idle(10);
            if(timer.getCurrentTime() > 0)
                timer.countDown();
            
        }
    }

    public static int showFocus(Point point,Gem[] gem) {
        if (point != null) {
            for(int x = 0;x < 64;x++)
              if(gem[x].isAt(point)){
                  gem[x].toggleFocus();
                  return x;
              }            
        }
        return -1;
    }
    public static boolean isFocus(Gem[] gem){
        for(int x = 0;x < 64;x++)
              if(gem[x].isSelected()){
                  return true;
              }  
        return false;
    }
}
