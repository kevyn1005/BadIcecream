package Domain;

import java.awt.Image;

/**
 * Interfaz para los bloques del juego (polimorfismo)
 */
public interface Block {
    // Actualizacion del bloque (para animaciones)
    void update();

    // Posicion
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    // Estado del bloque
    boolean isAnimating();
    boolean isSolid();

    // Sprite actual
    Image getCurrentSprite();

    // Tipo de bloque
    BlockType getType();

    // Destruccion del bloque (polimorfismo)
    /**
     * Inicia la animacion de destruccion del bloque
     */
    void startDestroy();

    /**
     * Verifica si el bloque esta completamente destruido
     * @return true si termino la animacion de destruccion
     */
    boolean isDestroyed();

    enum BlockType {
        ICE, WALL, EMPTY
    }
}