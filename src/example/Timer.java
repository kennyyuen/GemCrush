/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package example;


public class Timer {

    private int initTime = 300000; //equals to 5mins
    private int current;

    public void start(){
        current = initTime;
    }
    
    public void setCurrentTime(int c){
        current = c;
    }

    public String getTimeString() {
        int time = current / 1000;
        int minute = time / 60;
        int second = time % 60;

        return String.format("%02d:%02d", minute, second);
    }
    public void countDown(){ //count down time 1second
        current = current - 10;
    }
    
    public int getCurrentTime(){
       return current;
    }
    
    public void setInitTime(int t){
        initTime = t;
    }
}
