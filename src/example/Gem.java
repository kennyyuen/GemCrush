/*
 * Project Name: EE2311 Project - Gems Crush
 * Student Name:
 * Student ID:
 * 
 */
package example;

import game.GameConsole;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;

/**
 * Sample design of a toggle-enable gem
 *
 * @author Your Name and ID
 */
public class Gem {

    // the upper-left corner of the board, reference origin point
    public static final int orgX = 240;
    public static final int orgY = 40;
    // the size of the gem
    public static final int w = 65;
    public static final int h = 65;
    // default position in 8x8 grid    
    private int posX = 0;
    private int posY = 0;
    private boolean selected = false;

    private Image pic;
    private Image focus;
    private int type;

    Gem(int x, int y, int t) {
        String file = "";
        switch (t) {
            case 1:
                file = "/assets/gemBlue.png";
                break;
            case 2:
                file = "/assets/gemGreen.png";
                break;
            case 3:
                file = "/assets/gemOrange.png";
                break;
            case 4:
                file = "/assets/gemPurple.png";
                break;
            case 5:
                file = "/assets/gemRed.png";
                break;
            case 6:
                file = "/assets/gemWhite.png";
                break;
            default:
                file = "/assets/gemYellow.png";
        }
        this.focus = new ImageIcon(this.getClass().getResource("/assets/focus.png")).getImage();
        this.pic = new ImageIcon(this.getClass().getResource(file)).getImage();
        this.posX = x;
        this.posY = y;
        this.type = t;
    }

    public void display() {
        GameConsole.getInstance().drawImage((int) (posX * w + orgX), (int) (posY * h + orgY), pic);
        if (selected) {
            GameConsole.getInstance().drawImage((int) (posX * w + orgX), (int) (posY * h + orgY), focus);
        }
    }

    public boolean isAt(Point point) {
        if (point != null) {
            return (point.x > (posX * w + orgX) && point.x <= ((posX + 1) * w + orgX) && point.y > (posY * h + orgY) && point.y <= ((posY + 1) * h + orgY));
        } else {
            return false;
        }
    }

    public Image getPic() {
        return pic;
    }

    public void setPic(String file) {
        this.pic = new ImageIcon(this.getClass().getResource(file)).getImage();
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggleFocus() {
        selected = !selected;
    }

}
