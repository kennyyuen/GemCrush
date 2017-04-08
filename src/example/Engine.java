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

    public static class Combo {

        private int matches;
        private int index;

        public Combo() {
            matches = 0;
            index = 0;
        }

        public Combo(int m, int in) {
            matches = m;
            index = in;
        }

        public int getCombo() {
            return matches;
        }
    }

    public static int[] showFocus(Point point, Gem[][] gem) {
        if (point != null) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (gem[i][j].isAt(point)) {
                        gem[i][j].toggleFocus();
                        return new int[]{i, j};
                    }
                }
            }
        }
        return new int[]{-1, -1};
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

    public static void checkMatchHorizontal(Gem[][] gem) {
        boolean match = false;
        for (int j = 0; j < 8; j++) {
            int[] type = getRowType(j, gem);
            Combo[] rowCombo = findRowMatch(type);
            for (int a = 0; a < 2; a++) {
                if (rowCombo[a].getCombo() != 0) {
                    System.out.println("row " + j + " has " + rowCombo[a].getCombo() + "x combo.");
                }
            }
        }
    }

    private static Combo[] findRowMatch(int[] type) {
        int count = 1, j = 0;
        Combo[] c = new Combo[2];
        for(int x = 0;x < 2;x++)
            c[x] = new Combo();
        for (int i = 0; i < 6; i += count) {
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
}
