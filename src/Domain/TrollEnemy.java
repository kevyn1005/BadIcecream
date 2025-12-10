package Domain;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Point;

/**
 * Implementacion del enemigo Troll
 * Se mueve en un patron cuadrado, girando a la derecha cuando encuentra un obstaculo
 */
public class TrollEnemy implements Enemy {
    private int x, y;
    private Point gridPosition;
    private boolean active;
    private GridSystem grid;
    private CollisionDetector collisionDetector;

    // Movimiento
    private Player.Direction currentDirection;
    private boolean isMoving;
    private int targetX, targetY;
    private static final int MOVE_SPEED = 1;

    // Animacion
    private Image[][] sprites; // [direccion][frame]
    private int animationFrame;
    private int animationCounter;
    private static final int ANIMATION_SPEED = 8;
    private static final int TOTAL_FRAMES = 8;

    private static final int ENEMY_WIDTH = 52;
    private static final int ENEMY_HEIGHT = 62;

    public TrollEnemy(int gridX, int gridY, GridSystem grid, CollisionDetector collisionDetector) {
        this.gridPosition = new Point(gridX, gridY);
        this.grid = grid;
        this.collisionDetector = collisionDetector;

        // Convertir posicion de grid a pixeles
        Point pixelPos = grid.gridToPixel(gridX, gridY);
        this.x = pixelPos.x;
        this.y = pixelPos.y;
        this.targetX = this.x;
        this.targetY = this.y;

        this.active = true;
        this.currentDirection = Player.Direction.RIGHT; // Empieza moviendose a la derecha
        this.isMoving = false;
        this.animationFrame = 0;
        this.animationCounter = 0;

        loadSprites();
        startNextMove();
    }

    private void loadSprites() {
        sprites = new Image[4][TOTAL_FRAMES]; // DOWN, UP, LEFT, RIGHT

        String[] directions = {"Down", "Up", "Left", "Right"};

        for (int dir = 0; dir < 4; dir++) {
            for (int frame = 0; frame < TOTAL_FRAMES; frame++) {
                String path = "Images/Enemies/Troll/Walk/" + directions[dir] + "/" + (frame + 1) + ".png";
                try {
                    ImageIcon icon = new ImageIcon(path);
                    if (icon.getIconWidth() > 0) {
                        sprites[dir][frame] = icon.getImage();
                    } else {
                        System.out.println("Advertencia: No se pudo cargar " + path);
                    }
                } catch (Exception e) {
                    System.out.println("Error cargando sprite de troll: " + path);
                }
            }
        }

        System.out.println("Sprites de troll cargados: " + directions.length + " direcciones, " + TOTAL_FRAMES + " frames");
    }

    @Override
    public void update() {
        if (!active) {
            return;
        }

        updateAnimation();

        if (isMoving) {
            moveTowardsTarget();
        } else {
            startNextMove();
        }
    }

    private void updateAnimation() {
        animationCounter++;
        if (animationCounter >= ANIMATION_SPEED) {
            animationCounter = 0;
            animationFrame++;

            if (animationFrame >= TOTAL_FRAMES) {
                animationFrame = 0;
            }
        }
    }

    private void moveTowardsTarget() {
        boolean reachedTarget = false;

        // Mover hacia el objetivo
        if (x < targetX) {
            x += MOVE_SPEED;
            if (x >= targetX) {
                x = targetX;
                reachedTarget = true;
            }
        } else if (x > targetX) {
            x -= MOVE_SPEED;
            if (x <= targetX) {
                x = targetX;
                reachedTarget = true;
            }
        }

        if (y < targetY) {
            y += MOVE_SPEED;
            if (y >= targetY) {
                y = targetY;
                reachedTarget = true;
            }
        } else if (y > targetY) {
            y -= MOVE_SPEED;
            if (y <= targetY) {
                y = targetY;
                reachedTarget = true;
            }
        }

        // Si llego al objetivo, actualizar posicion en grid
        if (reachedTarget) {
            gridPosition = grid.pixelToGrid(x, y);
            isMoving = false;
        }
    }

    private void startNextMove() {
        // Intentar moverse en la direccion actual
        Point nextPos = collisionDetector.getValidNextPosition(
                gridPosition.x, gridPosition.y, currentDirection);

        // Si no puede moverse en la direccion actual, girar a la derecha
        if (nextPos.equals(gridPosition)) {
            turnRight();
            nextPos = collisionDetector.getValidNextPosition(
                    gridPosition.x, gridPosition.y, currentDirection);

            // Si aun no puede moverse, seguir girando
            if (nextPos.equals(gridPosition)) {
                turnRight();
                nextPos = collisionDetector.getValidNextPosition(
                        gridPosition.x, gridPosition.y, currentDirection);

                // Un ultimo intento
                if (nextPos.equals(gridPosition)) {
                    turnRight();
                    nextPos = collisionDetector.getValidNextPosition(
                            gridPosition.x, gridPosition.y, currentDirection);
                }
            }
        }

        // Establecer objetivo
        Point targetPixel = grid.gridToPixel(nextPos.x, nextPos.y);
        targetX = targetPixel.x;
        targetY = targetPixel.y;

        // Si el objetivo es diferente a la posicion actual, empezar a moverse
        if (targetX != x || targetY != y) {
            isMoving = true;
        }
    }

    private void turnRight() {
        // Girar 90 grados a la derecha
        switch (currentDirection) {
            case UP:
                currentDirection = Player.Direction.RIGHT;
                break;
            case RIGHT:
                currentDirection = Player.Direction.DOWN;
                break;
            case DOWN:
                currentDirection = Player.Direction.LEFT;
                break;
            case LEFT:
                currentDirection = Player.Direction.UP;
                break;
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
        return ENEMY_WIDTH;
    }

    @Override
    public int getHeight() {
        return ENEMY_HEIGHT;
    }

    @Override
    public Point getGridPosition() {
        return gridPosition;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void deactivate() {
        active = false;
        System.out.println("Troll desactivado en posicion: " + gridPosition);
    }

    @Override
    public Image getCurrentSprite() {
        int dirIndex = getDirectionIndex();
        if (sprites != null && sprites[dirIndex] != null && sprites[dirIndex][animationFrame] != null) {
            return sprites[dirIndex][animationFrame];
        }
        return null;
    }

    private int getDirectionIndex() {
        switch (currentDirection) {
            case DOWN:
                return 0;
            case UP:
                return 1;
            case LEFT:
                return 2;
            case RIGHT:
                return 3;
            default:
                return 0;
        }
    }

    @Override
    public boolean collidesWith(int playerX, int playerY, int playerWidth, int playerHeight) {
        if (!active) {
            return false;
        }

        // Convertir posicion del jugador a coordenadas de grid
        Point playerGridPos = grid.pixelToGrid(playerX, playerY);

        // Verificar si el jugador esta en la MISMA celda que el enemigo
        return playerGridPos.x == gridPosition.x &&
                playerGridPos.y == gridPosition.y;
    }

    @Override
    public EnemyType getType() {
        return EnemyType.TROLL;
    }
}