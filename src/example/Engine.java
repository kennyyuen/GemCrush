/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import game.GameConsole;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author yuen
 */
public class Engine {

    private static int selectedX;
    private static int selectedY;
    private static int nextGem1;
    private static int nextGem2;
    private static Gem[][] gem;
    private static Timer timer;
    private static boolean load;

    public static class Combo {

        private int matches; // how many match 
        private int xIndex; //where start the combo
        private int yIndex; //which row or col

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

        public void setYIndex(int y) {
            yIndex = y;
        }

        public int getCombo() {
            return matches;
        }

        public int getXIndex() {
            return xIndex;
        }

        public int getYIndex() {
            return yIndex;
        }
    }

    public static void resetSelectedXY() {
        selectedX = -1;
        selectedY = -1;
    }

    public static int getSelectedX() {
        return selectedX;
    }

    public static int getSelectedY() {
        return selectedY;
    }

    private static int[] checkFocus(Point point, Gem[][] gem) { //see which one user select 
        if (point != null) {                                    // if no return -1,-1
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (gem[i][j].isAt(point)) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        return new int[]{-1, -1};
    }

    public static void showFocus(Gem[][] gem, Point point) { //text the selected one and do action
        int[] selected = checkFocus(point, gem);
        if (selected[0] != -1) {
            if (!isFocus(gem)) { //new select
                Sound sound = new Sound("/assets/select.wav");
                gem[selected[0]][selected[1]].toggleFocus(); //light up
                sound.playSound();
                selectedX = selected[0]; //store the selected x,y
                selectedY = selected[1];
            } else { //the one select is one left or right, then swap
                if (selected[0] == selectedX - 1 || selected[0] == selectedX + 1) {
                    if (selected[1] == selectedY) {
                        swapGem(gem, selected[0], selected[1]);
                        setAllFocus(gem, false); //light down
                    } else { //the selected one is not adjacent, then change it to new x,y
                        Sound sound = new Sound("/assets/select.wav");
                        gem[selectedX][selectedY].toggleFocus();
                        gem[selected[0]][selected[1]].toggleFocus();
                        sound.playSound();
                        selectedX = selected[0];
                        selectedY = selected[1];
                    }
                } else { //the one select is one up or down, then swap
                    if (selected[1] == selectedY - 1 || selected[1] == selectedY + 1) {
                        if (selected[0] == selectedX) {
                            swapGem(gem, selected[0], selected[1]);
                            setAllFocus(gem, false);
                        } else {
                            Sound sound = new Sound("/assets/select.wav");
                            gem[selectedX][selectedY].toggleFocus();
                            gem[selected[0]][selected[1]].toggleFocus();
                            sound.playSound();
                            selectedX = selected[0];
                            selectedY = selected[1];
                        }
                    } else {
                        Sound sound = new Sound("/assets/select.wav");
                        gem[selectedX][selectedY].toggleFocus();
                        gem[selected[0]][selected[1]].toggleFocus();
                        sound.playSound();
                        selectedX = selected[0];
                        selectedY = selected[1];
                    }
                }
            }
        }
    }

    public static boolean isFocus(Gem[][] gem) { //check selected or not
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gem[i][j].isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void setAllFocus(Gem[][] gem, boolean flag) { //all light down or up
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gem[i][j].setSelected(flag);
            }
        }
    }

    private static Gem[] rowcomboToGems(Gem[][] gem, Combo combo) { //
        Gem[] temp = new Gem[combo.getCombo()];
        for (int i = 0; i < combo.getCombo(); i++) {
            temp[i] = gem[combo.getXIndex() + i][combo.getYIndex()];
        }
        return temp;
    }

    private static Gem[] colcomboToGems(Gem[][] gem, Combo combo) {
        Gem[] temp = new Gem[combo.getCombo()];
        for (int i = 0; i < combo.getCombo(); i++) {
            temp[i] = gem[combo.getYIndex()][combo.getXIndex() + i];
        }
        return temp;
    }

    private static void removeGem(Gem gem) {
        gem.setType(7);
        gem.setPic(Gem.getTypeFile(gem.getType()));
    }

    public static boolean isMatch(Gem[][] gem) {
        boolean match = false;
        Combo[] rowCombo = checkMatchHorizontal(gem);
        Combo[] colCombo = checkMatchVertical(gem);
        if (rowCombo[0].getCombo() != 0 || colCombo[0].getCombo() != 0) {
            match = true;
        }
        return match;
    }

    public static void checkMatch(Gem[][] gem) {
        Combo[] rowCombo = checkMatchHorizontal(gem); //see how many and where is row combo
        Combo[] colCombo = checkMatchVertical(gem); //see how many and where is col combo
        int count = 0;
        Sound sound = new Sound("/assets/match.wav");
        if (rowCombo[0].getCombo() != 0 || colCombo[0].getCombo() != 0) { //if there is combo
            int i = 0;
            while (rowCombo[i] != null) { //have rowcombo, remove it, how many gems removed
                Gem[] remGem = rowcomboToGems(gem, rowCombo[i]);
                int j = 0;
                while (j < remGem.length) {
                    removeGem(remGem[j]);
                    j++;
                    count++;
                }
                //sound.playSound();
                i++;
            }
            i = 0;
            while (colCombo[i] != null) {
                Gem[] remGem = colcomboToGems(gem, colCombo[i]);
                int j = 0;
                while (j < remGem.length) {
                    if (remGem[j].getType() != 7) //if already is dummy, not to increment count
                    {
                        count++;
                    }
                    removeGem(remGem[j]);
                    j++;
                }
                //sound.playSound();
                i++;
            }
        }
        fallDown(gem);
        addNew(gem);
        if (nextGem1 == -1 && nextGem2 == -1) {
            random2GemType();
        }
        Score.calcScore(count);
    }

    private static void fallDown(Gem[][] gem) {
        for (int i = 0; i < 8; i++) {
            int[] type = getColType(i, gem); //the ith row of col type array 
            for (int j = 0; j <= 7; j++) {
                if (type[j] == 7) {
                    for (int z = j; z > 0; z--) {
                        type[z] = type[z - 1];
                        type[z - 1] = 7;
                        gem[i][z].setType(gem[i][z - 1].getType());
                        gem[i][z - 1].setType(7);
                    }
                }         
            }
        }
        reprint(gem);
    }
    

    private static void addNew(Gem[][] gem) { //add new gems to empty gems
        for (int i = 0; i < 8; i++) {
            int[] type = getColType(i, gem);
            for (int j = 7; j >= 0; j--) {
                if (type[j] == 7) {
                    if (nextGem1 != -1) {
                        gem[i][j].setType(nextGem1);
                        nextGem1 = -1;
                    } else if (nextGem2 != -1) {
                        gem[i][j].setType(nextGem2);
                        nextGem2 = -1;
                    } else {
                        gem[i][j].setType((int) (Math.random() * 7));
                    }
                }
            }
        }
    }

    public static Combo[] checkMatchHorizontal(Gem[][] gem) { //what combo in each row line
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
                }
            }
        }
        return tRowCombo; //total row combo
    }

    public static Combo[] checkMatchVertical(Gem[][] gem) { //what combo in each col line
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
                }
            }
        }
        return tColCombo; //return col combo
    }

    private static Combo[] findMatch(int[] type) { //find combo
        int count = 1, j = 0;
        Combo[] c = new Combo[2];
        for (int x = 0; x < 2; x++) {
            c[x] = new Combo();
        }
        for (int i = 0; i < 6; i = i + count) {
            count = 1;
            do {
                if (type[i] == type[i + count] && type[i] != 7) {
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

    public static void random2GemType() {
        nextGem1 = (int) (Math.random() * 7);
        nextGem2 = (int) (Math.random() * 7);
    }

    public static int getNextGem1Type() {
        return nextGem1;
    }

    public static int getNextGem2Type() {
        return nextGem2;
    }

    public static void drawNext2() {
        GameConsole.getInstance().drawImage(60, 250, new ImageIcon(Gem.getTypeFile(nextGem1)).getImage());
    }

    private static void swapGem(Gem[][] gem, int x, int y) {
        int temp = gem[selectedX][selectedY].getType();
        gem[selectedX][selectedY].setType(gem[x][y].getType());
        gem[x][y].setType(temp);
        reprint(gem);
    }

    private static void reprint(Gem[][] gem) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gem[i][j].setPic(Gem.getTypeFile(gem[i][j].getType()));
                gem[i][j].display();
            }
        }
    }

    public static Score newScore() {
        return new Score();
    }

    public static String showScore() {
        return Integer.toString(Score.getScore());
    }

    public static void setGem(Gem[][] g) {
        gem = g;
    }

    public static Gem[][] getGem() {
        return gem;
    }

    public static void setTimer(Timer t) {
        timer = t;
    }

    public static Timer getTimer() {
        return timer;
    }

    private static String[] typeData() {
        String[] data = new String[64];
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                data[count] = Integer.toString(gem[i][j].getType());
                count++;
            }
        }
        return data;
    }

    private static String timeData() {
        return Integer.toString(timer.getCurrentTime());
    }

    private static String scoreData() {
        return showScore();
    }

    private static String[] next2Data() {
        String[] type = new String[2];
        type[0] = Integer.toString(nextGem1);
        type[1] = Integer.toString(nextGem2);
        return type;
    }

    private static void loadSave(String[] data, String s, String time, String[] next) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gem[i][j].setType(Integer.parseInt(data[count]));
                count++;
            }
        }
        Score.setScore(Integer.parseInt(s));
        timer.setCurrentTime(Integer.parseInt(time));
        nextGem1 = Integer.parseInt(next[0]);
        nextGem2 = Integer.parseInt(next[1]);
    }

    private static void setLoadSave(boolean flag) {
        load = flag;
    }

    public static boolean isLoadSave() {
        return load;
    }

    public static class Score {

        private static int score;

        public Score() {
            score = 0;
        }

        public static void calcScore(int gemRemoved) {
            score += gemRemoved * 10;
        }

        public static int getScore() {
            return score;
        }

        private static void setScore(int s) {
            score = s;
        }
    }

    public static class SaveLoad extends JFrame {

        private final JButton save, load, back;
        private final JPanel main;
        Font font = new Font("Time new Roman", Font.BOLD, 14);

        public SaveLoad() {
            setLocationRelativeTo(null);
            setTitle("Save & Load");
            setSize(300, 300);
            main = new JPanel();
            save = new JButton("New Save");
            load = new JButton("Load Save");
            back = new JButton("Back");
            ActionListener listener = new ButtonListener();

            save.addActionListener(listener);
            load.addActionListener(listener);
            back.addActionListener(listener);

            save.setFont(font);
            load.setFont(font);
            back.setFont(font);

            Border margin = new EmptyBorder(20, 40, 20, 40);
            main.setBorder(margin);
            main.setLayout(new GridLayout(3, 1, 0, 20));
            main.add(save);
            main.add(load);
            main.add(back);

            add(main);
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        }

        private class ButtonListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                if (event.getSource() == back) {
                    setVisible(false);
                }
                if (event.getSource() == save) {
                    try {
                        String name = (String) JOptionPane.showInputDialog(null, "Please choose a Save location", null,
                                JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Save 1",
                                    "Save 2", "Save 3"}, null);
                        if (name != null) {
                            Scanner save = new Scanner(new File(name + ".txt"));
                            if (save.hasNextLine()) {
                                int response = JOptionPane.showConfirmDialog(null, "This Save location already has save. Do you want to cover it?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (response == JOptionPane.YES_OPTION) {
                                    FileOutputStream out = new FileOutputStream(name + ".txt");
                                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
                                    String[] data = Engine.typeData();
                                    String[] next = Engine.next2Data();
                                    for (int i = 0; i < 64; i++) {
                                        bw.write(data[i]);
                                        bw.newLine();
                                    }
                                    bw.write(Engine.scoreData());
                                    bw.newLine();
                                    bw.write(Engine.timeData());
                                    for (int i = 0; i < 2; i++) {
                                        bw.newLine();
                                        bw.write(next[i]);
                                    }
                                    bw.close();
                                }
                            }

                        }
                    } catch (IOException ex) {
                    }
                }
                if (event.getSource() == load) {
                    String name = (String) JOptionPane.showInputDialog(null, "Please choose a Save", null,
                            JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Save 1",
                                "Save 2", "Save 3"}, null);
                    if (name == null) {
                        System.out.println("Cancel is pressed");
                    }
                    if (name != null) {
                        try {
                            Scanner save = new Scanner(new File(name + ".txt"));
                            if (save.hasNextLine()) {
                                String[] data = new String[64];
                                for (int i = 0; i < 64; i++) {
                                    data[i] = save.nextLine();
                                }
                                String score = save.nextLine();
                                String time = save.nextLine();
                                String[] next = new String[2];
                                for (int i = 0; i < 2; i++) {
                                    next[i] = save.nextLine();
                                }
                                Engine.setLoadSave(true);
                                Engine.loadSave(data, score, time, next);
                            } else {
                                JOptionPane.showMessageDialog(null, "This Save location has no content.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (FileNotFoundException ex) {

                        }
                    }

                }

            }

        }

        public static boolean isAt(Point point) {
            if (point != null) {
                return (point.x >= (60) && point.x < (150) && point.y >= (464) && point.y < (482));
            } else {
                return false;
            }

        }

        public static void loadOptionPanel(Point point) {
            if (isAt(point)) {
                SaveLoad saveload = new SaveLoad();
                saveload.setVisible(true);
            }

        }

    }

}
