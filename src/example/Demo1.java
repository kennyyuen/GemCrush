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

        Gem[][] gem = new Gem[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int ran = (int) (Math.random() * 7) + 1;
                gem[i][j] = new Gem(i, j, ran);
            }
        }

        // board dimension can be obtained from console
        int width = console.getBoardWidth();
        int height = console.getBoardHeight();

        // set custom background image
        console.setBackground("/assets/board.png");
        Timer timer = new Timer();
        timer.start();
        Engine.checkMatchHorizontal(gem);
        // enter the main game loop
        int[] selectedGem = {-1, -1};
        while (true) {

            // get whatever inputs
            Point point = console.getClickedPoint();
            if (Engine.isFocus(gem)) {
                gem[selectedGem[0]][selectedGem[1]].toggleFocus();
            }
            selectedGem = Engine.showFocus(point, gem);
            
            // refresh at the specific rate, default 25 fps
            if (console.shouldUpdate()) {
                console.clear();

                console.drawText(60, 150, "[TIME]", new Font("Helvetica", Font.BOLD, 20), Color.white);
                console.drawText(60, 180, timer.getTimeString(), new Font("Helvetica", Font.PLAIN, 20), Color.white);

                console.drawText(60, 250, "[SCORE]", new Font("Helvetica", Font.BOLD, 20), Color.white);
                console.drawText(60, 280, "220", new Font("Helvetica", Font.PLAIN, 20), Color.white);
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        gem[i][j].display();
                    }
                }

                console.update();
            }
            // the idle time affects the no. of iterations per second which 
            // should be larger than the frame rate
            // for fps at 25, it should not exceed 40ms
            console.idle(10);
            if (timer.getCurrentTime() > 0) {
                timer.countDown();
            }

        }
    }
    
}
