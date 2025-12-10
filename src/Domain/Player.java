package Domain;

import java.awt.Image;

/**
 * Interfaz que define el comportamiento de un jugador (polimorfismo)
 */
public interface Player {
    // Movimiento
    void moveUp();
    void moveDown();
    void moveLeft();
    void moveRight();
    void stopMoving();

    // Actualizacion del estado (se llama cada frame)
    void update();

    // Getters de posicion
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    // Setter de posicion (para alinear a grid)
    void setPosition(int x, int y);

    // Movimiento por casillas (requiere GridSystem)
    void startMoveInDirection(Direction direction, GridSystem grid);

    // Direccion y animacion
    Direction getDirection();
    Image getCurrentSprite();

    // Estado del jugador
    boolean isMoving();
    void snapToNearestGrid(GridSystem grid);
    void startGridMovement(Direction direction, GridSystem grid);

    // Accion de crear hielo
    /**
     * Inicia la animacion de crear hielo
     * @return true si se inicio la animacion correctamente
     */
    boolean startPutIceAnimation();

    /**
     * Inicia la animacion de romper hielo
     * @return true si se inicio la animacion correctamente
     */
    boolean startBreakIceAnimation();

    /**
     * Verifica si el jugador esta realizando una animacion de accion
     * @return true si esta ejecutando una animacion que bloquea el movimiento
     */
    boolean isPerformingAction();

    /**
     * Verifica si esta ejecutando la animacion de poner hielo
     * @return true si esta en animacion de poner hielo
     */
    boolean isPerformingPutIceAction();

    /**
     * Verifica si esta ejecutando la animacion de romper hielo
     * @return true si esta en animacion de romper hielo
     */
    boolean isPerformingBreakIceAction();

    /**
     * Verifica si la animacion de crear hielo ha terminado y es momento de crear el hielo
     * @return true cuando debe crearse el hielo
     */
    boolean shouldCreateIce();

    /**
     * Verifica si la animacion de romper hielo ha terminado y es momento de destruir el hielo
     * @return true cuando debe destruirse el hielo
     */
    boolean shouldDestroyIce();

    /**
     * Resetea el flag de creacion de hielo despues de que se haya creado
     */
    void resetIceCreation();

    /**
     * Resetea el flag de destruccion de hielo despues de que se haya destruido
     */
    void resetIceDestruction();

    // Enum para las direcciones
    enum Direction {
        UP, DOWN, LEFT, RIGHT,
        IDLE_DOWN, IDLE_UP, IDLE_LEFT, IDLE_RIGHT
    }
}