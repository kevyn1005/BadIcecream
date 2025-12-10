package Domain;

import java.awt.Point;

/**
 * Responsable de cargar partidas guardadas y reconstruir el estado del juego
 */
public class GameLoader {
    private GameSaveData saveData;

    public GameLoader(GameSaveData saveData) {
        this.saveData = saveData;
    }

    public String getPlayerName() {
        return saveData.getPlayerName();
    }

    public String getPlayerFlavor() {
        return saveData.getPlayerFlavor();
    }

    public int getPlayerX() {
        return saveData.getPlayerX();
    }

    public int getPlayerY() {
        return saveData.getPlayerY();
    }

    public int getScore() {
        return saveData.getScore();
    }

    public long getRemainingTime() {
        return saveData.getRemainingTime();
    }

    /**
     * Carga los bloques de hielo en el mapa
     */
    public void loadIceBlocks(GameMap gameMap) {
        for (GameSaveData.IceBlockData iceData : saveData.getIceBlocksData()) {
            Point pixelPos = gameMap.getGrid().gridToPixel(iceData.getGridX(), iceData.getGridY());
            Block ice = new SolidIceBlock(pixelPos.x, pixelPos.y);
            gameMap.getBlocks().add(ice);
        }
        System.out.println("Bloques de hielo cargados: " + saveData.getIceBlocksData().size());
    }

    /**
     * Carga las frutas en el gestor de frutas
     */
    public void loadFruits(FruitManager fruitManager, GridSystem grid) {
        for (GameSaveData.FruitData fruitData : saveData.getFruitsData()) {
            Fruit fruit = createFruit(fruitData, grid);

            if (fruit != null) {
                if (fruitData.isCollected()) {
                    fruit.collect();
                }
                fruitManager.addFruit(fruit);
            }
        }
        System.out.println("Frutas cargadas: " + saveData.getFruitsData().size());
    }

    /**
     * Carga los enemigos en el gestor de enemigos
     */
    public void loadEnemies(EnemyManager enemyManager) {
        for (GameSaveData.EnemyData enemyData : saveData.getEnemiesData()) {
            if (enemyData.isActive() && enemyData.getEnemyType().equals("TROLL")) {
                enemyManager.createTroll(enemyData.getGridX(), enemyData.getGridY());
            }
        }
        System.out.println("Enemigos cargados: " + saveData.getEnemiesData().size());
    }

    private Fruit createFruit(GameSaveData.FruitData fruitData, GridSystem grid) {
        String type = fruitData.getFruitType();
        int gridX = fruitData.getGridX();
        int gridY = fruitData.getGridY();

        if (type.equals("BANANA")) {
            return new BananaFruit(gridX, gridY, grid);
        } else if (type.equals("GRAPE")) {
            return new GrapeFruit(gridX, gridY, grid);
        }

        return null;
    }

    /**
     * Verifica si hay datos validos para cargar
     */
    public boolean hasValidData() {
        return saveData != null &&
                saveData.getPlayerName() != null &&
                saveData.getPlayerFlavor() != null;
    }
}