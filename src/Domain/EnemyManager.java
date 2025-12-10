package Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Gestiona todos los enemigos del juego
 * Maneja actualizacion, colisiones y creacion de enemigos
 */
public class EnemyManager {
    private List<Enemy> enemies;
    private GridSystem grid;
    private CollisionDetector collisionDetector;

    public EnemyManager(GridSystem grid, CollisionDetector collisionDetector) {
        this.grid = grid;
        this.collisionDetector = collisionDetector;
        this.enemies = new ArrayList<>();
    }

    /**
     * Crea un enemigo troll en una posicion de grid
     */
    public void createTroll(int gridX, int gridY) {
        Enemy troll = new TrollEnemy(gridX, gridY, grid, collisionDetector);
        enemies.add(troll);
        System.out.println("Troll creado en posicion: (" + gridX + ", " + gridY + ")");
    }

    /**
     * Actualiza todos los enemigos
     */
    public void update() {
        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                enemy.update();
            }
        }
    }

    /**
     * Verifica colisiones con el jugador
     * @return true si el jugador colisiono con algun enemigo
     */
    public boolean checkCollisionWithPlayer(Player player) {
        for (Enemy enemy : enemies) {
            if (enemy.isActive() && enemy.collidesWith(
                    player.getX(), player.getY(),
                    player.getWidth(), player.getHeight())) {
                System.out.println("Colision detectada con enemigo tipo: " + enemy.getType());
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene todos los enemigos
     */
    public List<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Limpia todos los enemigos (para reiniciar nivel)
     */
    public void clear() {
        enemies.clear();
    }

    /**
     * Obtiene cantidad de enemigos activos
     */
    public int getActiveEnemyCount() {
        int count = 0;
        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Obtiene un mapa con el conteo de enemigos por tipo
     * @return Map con el tipo de enemigo y su cantidad activa
     */
    public Map<Enemy.EnemyType, Integer> getEnemyCountsByType() {
        Map<Enemy.EnemyType, Integer> counts = new HashMap<>();

        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                Enemy.EnemyType type = enemy.getType();
                counts.put(type, counts.getOrDefault(type, 0) + 1);
            }
        }

        return counts;
    }

    /**
     * Obtiene la cantidad de trolls activos
     */
    public int getActiveTrollCount() {
        int count = 0;
        for (Enemy enemy : enemies) {
            if (enemy.isActive() && enemy.getType() == Enemy.EnemyType.TROLL) {
                count++;
            }
        }
        return count;
    }
}