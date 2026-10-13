package com.mycompany.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                JFileChooser selector = new JFileChooser();
                selector.setDialogTitle("Selecciona una imagen con poco contraste");
                selector.setFileFilter(new FileNameExtensionFilter("Imagenes", "png", "jpg", "jpeg"));
                if (selector.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;
                
                BufferedImage imgOriginal = ImageIO.read(selector.getSelectedFile());
                int ancho = imgOriginal.getWidth();
                int alto = imgOriginal.getHeight();

                BufferedImage imgGrises = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
                BufferedImage imgExpandida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

                int[] histograma = new int[256];
                int maxFrecuencia = 0, minGris = 255, maxGris = 0;

                for (int y = 0; y < alto; y++) {
                    for (int x = 0; x < ancho; x++) {
                        Color aux = new Color(imgOriginal.getRGB(x, y));
                        int gris = (aux.getRed() + aux.getGreen() + aux.getBlue()) / 3;
                        imgGrises.setRGB(x, y, new Color(gris, gris, gris).getRGB());
                        
                        histograma[gris]++;
                        if (histograma[gris] > maxFrecuencia) maxFrecuencia = histograma[gris];
                        if (gris < minGris) minGris = gris;
                        if (gris > maxGris) maxGris = gris;
                    }
                } 

                int[] histExpandido = new int[256];
                int maxFrecuenciaExp = 0;
                for (int y = 0; y < alto; y++) {
                    for (int x = 0; x < ancho; x++) {
                        int grisOriginal = new Color(imgGrises.getRGB(x, y)).getRed();
                        int nuevoGris = 0;
                        if (maxGris > minGris) { 
                            nuevoGris = (int) (((double)(grisOriginal - minGris) / (maxGris - minGris)) * 255.0);
                        }
                        nuevoGris = Math.max(0, Math.min(255, nuevoGris)); 
                        imgExpandida.setRGB(x, y, new Color(nuevoGris, nuevoGris, nuevoGris).getRGB());

                        histExpandido[nuevoGris]++;
                        if (histExpandido[nuevoGris] > maxFrecuenciaExp) maxFrecuenciaExp = histExpandido[nuevoGris];
                    }
                }

                new JFrameImagen(imgGrises).setVisible(true);
                new JFrameImagen(generarGrafica(histograma, maxFrecuencia, Color.BLACK)).setVisible(true);
                new JFrameImagen(imgExpandida).setVisible(true);
                new JFrameImagen(generarGrafica(histExpandido, maxFrecuenciaExp, Color.RED)).setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        });
    }

    private static BufferedImage generarGrafica(int[] datos, int maxFrecuencia, Color colorLinea) {
        int hAncho = 256, hAlto = 200;
        BufferedImage imgHist = new BufferedImage(hAncho, hAlto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imgHist.createGraphics();
        g.setColor(Color.WHITE); g.fillRect(0, 0, hAncho, hAlto);
        g.setColor(colorLinea);
        for (int i = 0; i < 256; i++) {
            if (maxFrecuencia > 0) {
                int altura = (int) (((double) datos[i] / maxFrecuencia) * hAlto);
                g.drawLine(i, hAlto, i, hAlto - altura);
            }
        }
        g.dispose(); 
        return imgHist;
    }
}
