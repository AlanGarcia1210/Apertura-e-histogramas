package com.mycompany.main;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class JFrameImagen extends JFrame {
    public JFrameImagen(BufferedImage imagen) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JLabel(new ImageIcon(imagen)));
        pack();
        setLocationRelativeTo(null);
    }
}
