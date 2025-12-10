package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Pantalla de seleccion de nivel con botones invisibles
 */
public class LevelSelectionScreen implements Screen {
    private JPanel panel;
    private Image backgroundImage;
    private final ScreenManager screenManager;

    public LevelSelectionScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        initializePanel();
    }

    private void initializePanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBackground(g);
            }
        };

        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(680, 680));
        panel.setBackground(Color.WHITE);

        loadImage();
        createButtons();
    }

    private void loadImage() {
        try {
            String[] paths = {"Images/SelcNiveles.png"};

            for (String path : paths) {
                try {
                    backgroundImage = new ImageIcon(path).getImage();
                    if (backgroundImage.getWidth(null) > 0) {
                        System.out.println("Imagen de seleccion de nivel cargada desde: " + path);
                        return;
                    }
                } catch (Exception ex) {
                    // Intenta siguiente ruta
                }
            }

            System.out.println("No se pudo cargar la imagen de seleccion de nivel desde ninguna ruta");
        } catch (Exception e) {
            System.out.println("Error cargando imagen de seleccion de nivel: " + e.getMessage());
        }
    }

    private void drawBackground(Graphics g) {
        if (backgroundImage != null) {
            int panelWidth = panel.getWidth();
            int panelHeight = panel.getHeight();

            int imgWidth = backgroundImage.getWidth(null);
            int imgHeight = backgroundImage.getHeight(null);

            double scaleX = (double) panelWidth / imgWidth;
            double scaleY = (double) panelHeight / imgHeight;
            double scale = Math.min(scaleX, scaleY);

            int newWidth = (int) (imgWidth * scale);
            int newHeight = (int) (imgHeight * scale);

            int x = (panelWidth - newWidth) / 2;
            int y = (panelHeight - newHeight) / 2;

            g.drawImage(backgroundImage, x, y, newWidth, newHeight, null);
        }
    }

    private void createButtons() {
        // Boton para nivel 1 - INVISIBLE
        JButton level1Button = createInvisibleButton(232, 245, 90, 65);
        level1Button.addActionListener(e -> {
            System.out.println("Nivel 1 seleccionado");
            screenManager.showScreen("game");
        });

        // Boton para nivel 2 - INVISIBLE
        JButton level2Button = createInvisibleButton(335, 245, 90, 65);
        level2Button.addActionListener(e -> {
            System.out.println("Nivel 2 seleccionado (no implementado)");
        });

        // Boton para nivel 3 - INVISIBLE
        JButton level3Button = createInvisibleButton(440, 245, 90, 65);
        level3Button.addActionListener(e -> {
            System.out.println("Nivel 3 seleccionado (no implementado)");
        });

        // Boton para nivel 4 - INVISIBLE
        JButton level4Button = createInvisibleButton(337, 356, 85, 65);
        level4Button.addActionListener(e -> {
            System.out.println("Nivel 4 seleccionado (no implementado)");
        });

        // Boton BACK - INVISIBLE
        JButton backButton = createInvisibleButton(295, 480, 170, 72);
        backButton.addActionListener(e -> {
            System.out.println("Volver a seleccion de sabor");
            screenManager.showScreen("flavorSelection");
        });

        panel.add(level1Button);
        panel.add(level2Button);
        panel.add(level3Button);
        panel.add(level4Button);
        panel.add(backButton);
    }

    private JButton createInvisibleButton(int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);

        // Hacer el boton completamente invisible
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Cambiar cursor al pasar sobre el boton
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void onShow() {
        System.out.println("Mostrando pantalla de seleccion de nivel");
    }

    @Override
    public void onHide() {
        System.out.println("Ocultando pantalla de seleccion de nivel");
    }
}