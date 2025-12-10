package Domain;

import java.awt.Image;
import java.awt.Point;

/**
 * Interfaz que define el comportamiento de un enemigo (polimorfismo)
 */
public interface Enemy {
    // Actualizacion del estado (se llama cada frame)
    void update();

    // Getters de posicion
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    // Posicion en la cuadricula
    Point getGridPosition();

    // Sprite actual
    Image getCurrentSprite();

    // Estado del enemigo
    boolean isActive();
    void deactivate();

    // Colision con jugador
    boolean collidesWith(int playerX, int playerY, int playerWidth, int playerHeight);

    // Tipo de enemigo
    EnemyType getType();

    enum EnemyType {
        TROLL
    }
}