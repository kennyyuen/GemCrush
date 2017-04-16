/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import java.awt.Point;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author yuen
 */
public class Engine {
    private static int selectedX;
    private static int selectedY;

    public static class Combo {

        private int matches;
        private int xIndex;
        private int yIndex;
        
        public Combo() {
            matches = 0;
            xIndex = -1;
            yIndex = -1;
        }

        public Combo(int m, int in) {
            matches = m;
            xIndex = in;
            yIndex = -1;
        }

        public void setYIndex(int y){
            yIndex = y;
        }
        public int getCombo() {
            return matches;
        }

        public int getXIndex() {
            return xIndex;
        }
        public int getYIndex(){
            return yIndex;
        }
    }

    private static int[] checkFocus(Point point, Gem[][] gem) {
        if (point != null) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (gem[i][j].isAt(point)) {
                       return new int[]{i,j};
                    }
                }
            }
        }
        return new int[]{-1,-1};
    }
    
    public static void showFocus(Gem[][] gem,Point point){
        int[] selected = checkFocus(point,gem);        
        if(selected[0] != -1){
            for (int i = 0; i < 8; i++) 
                for (int j = 0; j < 8; j++) 
                    gem[i][j].setSelected(false);
            gem[selected[0]][selected[1]].toggleFocus();
            selectedX = selected[0];
            selectedY = selected[1];
        }
    }
    
    public static boolean isFocus(Gem[][] gem) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gem[i][j].isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkMatch(Gem[][] gem){
        boolean match = false;
        Combo[] rowCombo = checkMatchHorizontal(gem);
        Combo[] colCombo = checkMatchVertical(gem);
        if(rowCombo[0].getCombo() != 0 || colCombo[0].getCombo() != 0)
            match = true;
        return match;       
        //Combo[] combo = combineCombo(rowCombo,colCombo);
    }
    public static Combo[] checkMatchHorizontal(Gem[][] gem) {
        Combo[] tRowCombo = new Combo[16];
        tRowCombo[0] = new Combo();
        int i = 0;
        for (int j = 0; j < 8; j++) {
            int[] type = getRowType(j, gem);
            Combo[] rowCombo = findMatch(type);
            for (int a = 0; a < 2; a++) {
                if (rowCombo[a].getCombo() != 0) {
                    rowCombo[a].setYIndex(j);
                    tRowCombo[i] = rowCombo[a];
                    i++;
                    System.out.println("row " + j + " has " + rowCombo[a].getCombo() + "x combo at " + rowCombo[a].getXIndex());
                }
            }
        }
        return tRowCombo;
    }

    public static Combo[] checkMatchVertical(Gem[][] gem) {
        Combo[] tColCombo = new Combo[16];
        tColCombo[0] = new Combo();
        int j = 0;
        for (int i = 0; i < 8; i++) {
            int[] type = getColType(i, gem);
            Combo[] colCombo = findMatch(type);
            for (int a = 0; a < 2; a++) {
                if (colCombo[a].getCombo() != 0) {
                    colCombo[a].setYIndex(i);
                    tColCombo[j] = colCombo[a];
                    j++;
                    System.out.println("Col " + i + " has " + colCombo[a].getCombo() + "x combo at " + colCombo[a].getXIndex());
                }
            }
        }
        return tColCombo;
    }

    private static Combo[] findMatch(int[] type) {
        int count = 1, j = 0;
        Combo[] c = new Combo[2];
        for (int x = 0; x < 2; x++) {
            c[x] = new Combo();
        }
        for (int i = 0; i < 6; i = i + count) {
            count = 1;
            do {
                if (type[i] == type[i + count]) {
                    count++;
                } else {
                    break;
                }
            } while (count < 8 - i);
            if (count >= 3) {
                c[j] = new Combo(count, i);
                j++;
            }
        }
        return c;
    }

    private static int[] getRowType(int j, Gem[][] gem) {
        int[] type = new int[8];
        for (int i = 0; i < 8; i++) {
            type[i] = gem[i][j].getType();
        }
        return type;
    }

    private static int[] getColType(int i, Gem[][] gem) {
        int[] type = new int[8];
        for (int j = 0; j < 8; j++) {
            type[j] = gem[i][j].getType();
        }
        return type;
    }

    private static int[] random2GemType() {
        int[] ran = new int[2];
        ran[0] = (int) (Math.random() * 7) + 1;
        ran[1] = (int) (Math.random() * 7) + 1;
        return ran;
    }
    
    private static Combo[] combineCombo(Combo[] x,Combo[] y){
        int count = 0;
        for (int i = 0;i < x.length;i++)
            if(x[i].getCombo() != 0)
                count++;
        for (int i = 0;i < y.length;i++)
            if(y[i].getCombo() != 0)
                count++;
        Combo[] totalCombo = new Combo[count];
        count = 0;
        for (int i = 0;i < x.length;i++,count++)
            if(x[i].getCombo() != 0)
                totalCombo[count] = x[i];
        for (int j = 0;j < y.length;j++,count++)
            if(y[j].getCombo() != 0)
                totalCombo[count] = y[j];
        return totalCombo;
    }
    
    private static void removeGem() {
        
    }

    private static void swapGem(Gem[][] gem,int x,int y) {
        
    }
    
    public static Score newScore(){
        return new Score();
    }
    
    public static String showScore(){
        return Integer.toString(Score.getScore());
    }

    public static class Score {
        private static int score;
        public Score(){
            score = 0;
        }       

        public static void calcScore(int gemRemoved) {
            score = gemRemoved * 10;
        }

        public static int getScore() {
            return score;
        }
    }

}
