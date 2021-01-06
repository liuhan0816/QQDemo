package com.arthur.mychat.core.swing;

import javax.swing.*;
import java.awt.*;

/**
 * @ClassName ImagePanel
 * @Author liuhan
 * @Date 2020/9/9 14:44
 */
public class ImagePanel extends JPanel {
    JFrame frame;
    ImageIcon icon;
    public ImagePanel(JFrame frame,ImageIcon icon) {
        super();
        this.frame = frame;
        this.icon = icon;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(icon.getImage(), 0, 0, frame.getSize().width, frame.getSize().height, frame);
    }
}
