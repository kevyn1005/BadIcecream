package Domain;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Point;

/**
 * Implementacion de la fruta Uva (estatica con animacion)
 * Aparece despues de recolectar todas las bananas
 * Otorga 50 puntos al ser recolectada
 */
public class GrapeFruit implements Fruit {
    private int x, y;
    private Point gridPosition;
    private boolean collected;
    private Image[] sprites;
    private GridSystem grid;

    // Animacion
    private int animationFrame;
    private int animationCounter;
    private static final int ANIMATION_SPEED = 9;
    private static final int TOTAL_FRAMES = 4;
    private static final int POINTS = 50;

    private static final int FRUIT_SIZE = 40;

    public GrapeFruit(int gridX, int gridY, GridSystem grid) {
        this.gridPosition = new Point(gridX, gridY);
        this.grid = grid;

        // Convertir posicion de grid a pixeles
        Point pixelPos = grid.gridToPixel(gridX, gridY);
        this.x = pixelPos.x;
        this.y = pixelPos.y;

        this.collected = false;
        this.animationFrame = 0;
        this.animationCounter = 0;

        loadSprites();
    }

    private void loadSprites() {
        sprites = new Image[TOTAL_FRAMES];

        for (int i = 0; i < TOTAL_FRAMES; i++) {
            String path = "Images/Fruits/Grapes/" + (i + 1) + ".png";
            try {
                ImageIcon icon = new ImageIcon(path);
                if (icon.getIconWidth() > 0) {
                    sprites[i] = icon.getImage();
                } else {
                    System.out.println("Advertencia: No se pudo cargar " + path);
                }
            } catch (Exception e) {
                System.out.println("Error cargando sprite de uva: " + path);
            }
        }

        System.out.println("Sprites de uva cargados: " + TOTAL_FRAMES + " frames");
    }

    @Override
    public void update() {
        if (collected) {
            return;
        }

        // Actualizar animacion
        animationCounter++;
        if (animationCounter >= ANIMATION_SPEED) {
            animationCounter = 0;
            animationFrame++;

            if (animationFrame >= TOTAL_FRAMES) {
                animationFrame = 0; // Loop
            }
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return FRUIT_SIZE;
    }

    @Override
    public int getHeight() {
        return FRUIT_SIZE+5;
    }

    @Override
    public Point getGridPosition() {
        return gridPosition;
    }

    @Override
    public boolean isCollected() {
        return collected;
    }

    @Override
    public void collect() {
        collected = true;
        System.out.println("Uva recolectada en posicion: " + gridPosition + " (+50 puntos)");
    }

    @Override
    public Image getCurrentSprite() {
        if (sprites != null && sprites.length > 0 && sprites[animationFrame] != null) {
            return sprites[animationFrame];
        }
        return null;
    }

    @Override
    public FruitType getType() {
        return FruitType.GRAPE;
    }

    @Override
    public int getPoints() {
        return POINTS;
    }

    @Override
    public boolean collidesWith(int playerX, int playerY, int playerWidth, int playerHeight) {
        if (collected) {
            return false;
        }

        // Convertir posicion del jugador a coordenadas de grid
        Point playerGridPos = grid.pixelToGrid(playerX, playerY);

        // Verificar si el jugador esta en la MISMA celda que la fruta
        return playerGridPos.x == gridPosition.x &&
                playerGridPos.y == gridPosition.y;
    }
}