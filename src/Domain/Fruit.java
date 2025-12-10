package Domain;

import java.awt.Image;
import java.awt.Point;

/**
 * Interfaz para las frutas del juego (polimorfismo)
 * Diferentes tipos: Banana (estatica), Uva (aparece tras bananas),
 * Cereza (teletransporte), Pina (movil), Cactus (estatico)
 */
public interface Fruit {
    // Actualizacion (para frutas que se mueven o animan)
    void update();

    // Posicion
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    Point getGridPosition();

    // Estado
    boolean isCollected();
    void collect();

    // Sprite actual
    Image getCurrentSprite();

    // Tipo de fruta
    FruitType getType();

    // Verificar colision con jugador
    boolean collidesWith(int playerX, int playerY, int playerWidth, int playerHeight);

    // Puntos que otorga la fruta al ser recolectada
    int getPoints();

    enum FruitType {
        BANANA, GRAPE, CHERRY, PINEAPPLE, CACTUS
    }
}