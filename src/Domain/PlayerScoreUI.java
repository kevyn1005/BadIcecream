package Domain;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;

/**
 * Maneja la visualizacion del icono, puntaje, nombre y sabor del jugador
 */
public class PlayerScoreUI {
    private Image j1Icon;
    private String playerName;
    private String playerFlavor;

    private static final int ICON_X = 20;
    private static final int ICON_Y = 20;
    private static final int ICON_SIZE = 45;

    private static final int SCORE_X = ICON_X + ICON_SIZE + 8;
    private static final int SCORE_Y = ICON_Y + 28;

    private static final int NAME_X = ICON_X;
    private static final int NAME_Y = ICON_Y + ICON_SIZE + 18;

    private static final int FLAVOR_X = ICON_X;
    private static final int FLAVOR_Y = NAME_Y + 20;

    public PlayerScoreUI(String playerName, String playerFlavor) {
        this.playerName = playerName;
        this.playerFlavor = getFlavorDisplayName(playerFlavor);
        loadIcon();
    }

    private String getFlavorDisplayName(String flavor) {
        if (flavor == null || flavor.isEmpty()) return "Vainilla";

        switch (flavor.toLowerCase()) {
            case "brown":
                return "Chocolate";
            case "vanilla":
                return "Vainilla";
            case "pink":
                return "Fresa";
            default:
                return "Vainilla";
        }
    }

    private void loadIcon() {
        try {
            String path = "Images/Resources/J1.png";
            ImageIcon icon = new ImageIcon(path);
            if (icon.getIconWidth() > 0) {
                j1Icon = icon.getImage();
                System.out.println("Icono J1 cargado correctamente");
            } else {
                System.out.println("Advertencia: No se pudo cargar " + path);
            }
        } catch (Exception e) {
            System.out.println("Error cargando icono J1: " + e.getMessage());
        }
    }

    public void draw(Graphics g, int score) {
        // Dibujar icono J1
        if (j1Icon != null) {
            g.drawImage(j1Icon, ICON_X, ICON_Y, ICON_SIZE, ICON_SIZE, null);
        }

        // Dibujar puntaje
        drawScore(g, score);

        // Dibujar nombre del jugador
        drawPlayerName(g);

        // Dibujar sabor del jugador
        drawPlayerFlavor(g);
    }

    private void drawScore(Graphics g, int score) {
        Font scoreFont = new Font("Arial", Font.BOLD, 24);
        g.setFont(scoreFont);

        String scoreText = "" + score;

        // Sombra negra
        g.setColor(Color.BLACK);
        g.drawString(scoreText, SCORE_X + 1, SCORE_Y + 11);

        // Texto amarillo
        g.setColor(Color.YELLOW);
        g.drawString(scoreText, SCORE_X, SCORE_Y + 10);
    }

    private void drawPlayerName(Graphics g) {
        Font nameFont = new Font("Arial", Font.BOLD, 16);
        g.setFont(nameFont);

        // Sombra negra
        g.setColor(Color.BLACK);
        g.drawString(playerName, NAME_X + 1, NAME_Y + 1);

        // Texto blanco
        g.setColor(Color.WHITE);
        g.drawString(playerName, NAME_X, NAME_Y);
    }

    private void drawPlayerFlavor(Graphics g) {
        Font flavorFont = new Font("Arial", Font.PLAIN, 14);
        g.setFont(flavorFont);

        // Sombra negra
        g.setColor(Color.BLACK);
        g.drawString(playerFlavor, FLAVOR_X + 1, FLAVOR_Y + 1);

        // Texto cyan claro
        g.setColor(Color.WHITE);
        g.drawString(playerFlavor, FLAVOR_X, FLAVOR_Y);
    }
}