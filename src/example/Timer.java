/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import java.util.concurrent.TimeUnit;

public class Timer {

    private int initTime = 300000;
    private int current;

    public void start(){
        current = initTime;
    }

    public String getTimeString() {
        int time = current / 1000;
        int minute = time / 60;
        int second = time % 60;

        return String.format("%02d:%02d", minute, second);
    }
    public void countDown(){
        current = current - 10;
    }
    public int getCurrentTime(){
       return current;
    }
}
