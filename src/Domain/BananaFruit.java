package Domain;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Point;

/**
 * Implementacion de la fruta Banana (estatica con animacion)
 * No se mueve, pero tiene sprites animados
 * Otorga 100 puntos al ser recolectada
 */
public class BananaFruit implements Fruit {
    private int x, y;
    private Point gridPosition;
    private boolean collected;
    private Image[] sprites;
    private GridSystem grid;

    // Animacion
    private int animationFrame;
    private int animationCounter;
    private static final int ANIMATION_SPEED = 7;
    private static final int TOTAL_FRAMES = 5;
    private static final int POINTS = 100;

    private static final int FRUIT_SIZE = 56;

    public BananaFruit(int gridX, int gridY, GridSystem grid) {
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
            String path = "Images/Fruits/Banana/" + (i + 1) + ".png";
            try {
                ImageIcon icon = new ImageIcon(path);
                if (icon.getIconWidth() > 0) {
                    sprites[i] = icon.getImage();
                } else {
                    System.out.println("Advertencia: No se pudo cargar " + path);
                }
            } catch (Exception e) {
                System.out.println("Error cargando sprite de banana: " + path);
            }
        }

        System.out.println("Sprites de banana cargados: " + TOTAL_FRAMES + " frames");
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
        return FRUIT_SIZE;
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
        System.out.println("Banana recolectada en posicion: " + gridPosition + " (+100 puntos)");
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
        return FruitType.BANANA;
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