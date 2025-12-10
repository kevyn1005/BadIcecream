package Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de frutas del juego
 * Maneja la creacion, actualizacion y colision de todas las frutas
 * Controla la aparicion de uvas tras recolectar todas las bananas
 */
public class FruitManager {
    private List<Fruit> fruits;
    private GridSystem grid;
    private int totalFruits;
    private int collectedFruits;
    private int score;
    private boolean bananasCompleted;
    private boolean grapesSpawned;
    private int totalBananas;
    private int totalGrapes;

    public FruitManager(GridSystem grid) {
        this.grid = grid;
        this.fruits = new ArrayList<>();
        this.totalFruits = 0;
        this.collectedFruits = 0;
        this.score = 0;
        this.bananasCompleted = false;
        this.grapesSpawned = false;
        this.totalBananas = 0;
        this.totalGrapes = 0;
    }

    /**
     * Agrega una fruta en una posicion especifica de la grid
     */
    public void addFruit(Fruit fruit) {
        fruits.add(fruit);
        totalFruits++;

        // Contar bananas separadamente
        if (fruit.getType() == Fruit.FruitType.BANANA) {
            totalBananas++;
        }
    }

    /**
     * Crea bananas en posiciones especificas del mapa
     */
    public void createLevelBananas() {
        // Esquina superior izquierda
        addFruit(new BananaFruit(3, 4, grid));
        addFruit(new BananaFruit(4, 4, grid));
        addFruit(new BananaFruit(3, 5, grid));

        // Esquina inferior izquierda
        addFruit(new BananaFruit(3, 12, grid));
        addFruit(new BananaFruit(3, 13, grid));
        addFruit(new BananaFruit(4, 13, grid));

        // Esquina superior derecha
        addFruit(new BananaFruit(14, 4, grid));
        addFruit(new BananaFruit(14, 5, grid));
        addFruit(new BananaFruit(13, 4, grid));

        // Esquina inferior derecha
        addFruit(new BananaFruit(14, 12, grid));
        addFruit(new BananaFruit(14, 13, grid));
        addFruit(new BananaFruit(13, 13, grid));

        // Zona superior central
        addFruit(new BananaFruit(7, 6, grid));
        addFruit(new BananaFruit(10, 6, grid));

        // Zona inferior central
        addFruit(new BananaFruit(7, 11, grid));
        addFruit(new BananaFruit(10, 11, grid));

        // Zona central izquierda
        addFruit(new BananaFruit(6, 8, grid));
        addFruit(new BananaFruit(6, 9, grid));

        // Zona central derecha
        addFruit(new BananaFruit(11, 8, grid));
        addFruit(new BananaFruit(11, 9, grid));

        System.out.println("Bananas creadas en el nivel. Total bananas: " + totalBananas);
    }

    /**
     * Crea uvas en posiciones estrategicas del mapa
     * Se llama automaticamente cuando todas las bananas son recolectadas
     */
    private void spawnGrapes() {
        if (grapesSpawned) {
            return;
        }

        // Bordes del mapa
        addFruit(new GrapeFruit(2, 2, grid));
        addFruit(new GrapeFruit(3, 2, grid));
        addFruit(new GrapeFruit(2, 3, grid));

        addFruit(new GrapeFruit(14, 2, grid));
        addFruit(new GrapeFruit(15, 2, grid));
        addFruit(new GrapeFruit(15, 3, grid));

        addFruit(new GrapeFruit(2, 14, grid));
        addFruit(new GrapeFruit(2, 15, grid));
        addFruit(new GrapeFruit(3, 15, grid));

        addFruit(new GrapeFruit(15, 14, grid));
        addFruit(new GrapeFruit(15, 15, grid));
        addFruit(new GrapeFruit(14, 15, grid));

        // Centro del mapa
        addFruit(new GrapeFruit(5, 6, grid));
        addFruit(new GrapeFruit(5, 11, grid));
        addFruit(new GrapeFruit(12, 6, grid));
        addFruit(new GrapeFruit(12, 11, grid));

        // Contar cuantas uvas se agregaron
        totalGrapes = totalFruits - totalBananas;

        grapesSpawned = true;
        System.out.println("Uvas aparecieron! Total de uvas: " + totalGrapes);
        System.out.println("Total de frutas en el nivel: " + totalFruits);
    }

    /**
     * Verifica si todas las bananas han sido recolectadas
     */
    private boolean allBananasCollected() {
        for (Fruit fruit : fruits) {
            if (fruit.getType() == Fruit.FruitType.BANANA && !fruit.isCollected()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Actualiza todas las frutas (para frutas moviles o con animacion)
     * Tambien verifica si debe aparecer las uvas
     */
    public void update() {
        for (Fruit fruit : fruits) {
            fruit.update();
        }

        // Verificar si todas las bananas fueron recolectadas para spawear uvas
        if (!bananasCompleted && allBananasCollected()) {
            bananasCompleted = true;
            System.out.println("Todas las bananas recolectadas! Aparecen las uvas...");
            spawnGrapes();
        }
    }

    /**
     * Verifica colisiones con el jugador y recolecta frutas
     */
    public void checkCollisions(Player player) {
        for (Fruit fruit : fruits) {
            if (!fruit.isCollected() &&
                    fruit.collidesWith(player.getX(), player.getY(),
                            player.getWidth(), player.getHeight())) {
                fruit.collect();
                collectedFruits++;
                score += fruit.getPoints();
                System.out.println("Frutas recolectadas: " + collectedFruits + "/" + totalFruits +
                        " | Puntos: " + score);
            }
        }
    }

    /**
     * Obtiene todas las frutas (para dibujar)
     */
    public List<Fruit> getFruits() {
        return fruits;
    }

    /**
     * Verifica si todas las frutas fueron recolectadas (condicion de victoria)
     * Solo retorna true cuando TODAS las frutas (bananas Y uvas) han sido recolectadas
     */
    public boolean allFruitsCollected() {
        // Si las uvas no han aparecido todavia, el nivel NO esta completo
        if (!grapesSpawned) {
            return false;
        }

        // Si las uvas ya aparecieron, verificar si todas las frutas fueron recolectadas
        return collectedFruits >= totalFruits;
    }

    /**
     * Obtiene el numero de frutas recolectadas
     */
    public int getCollectedCount() {
        return collectedFruits;
    }

    /**
     * Obtiene el numero total de frutas
     */
    public int getTotalCount() {
        return totalFruits;
    }

    /**
     * Obtiene el puntaje actual
     */
    public int getScore() {
        return score;
    }

    /**
     * Verifica si las uvas ya aparecieron
     */
    public boolean areGrapesSpawned() {
        return grapesSpawned;
    }

    /**
     * Reinicia el gestor de frutas (para nuevo nivel)
     */
    public void clear() {
        fruits.clear();
        totalFruits = 0;
        collectedFruits = 0;
        score = 0;
        bananasCompleted = false;
        grapesSpawned = false;
        totalBananas = 0;
        totalGrapes = 0;
    }
}