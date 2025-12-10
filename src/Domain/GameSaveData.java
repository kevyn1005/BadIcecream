package Domain;

import java.io.*;
import java.util.*;

/**
 * Maneja el guardado y carga del estado del juego
 * SIN clases extra - usa Maps para almacenar datos
 */
public class GameSaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    // Todos los datos en un Map
    private Map<String, Object> gameData;

    public GameSaveData() {
        gameData = new HashMap<>();
        gameData.put("fruits", new ArrayList<Map<String, Object>>());
        gameData.put("enemies", new ArrayList<Map<String, Object>>());
        gameData.put("iceBlocks", new ArrayList<Map<String, Object>>());
    }

    // Datos del jugador
    public void setPlayerName(String name) { gameData.put("playerName", name); }
    public String getPlayerName() { return (String) gameData.get("playerName"); }

    public void setPlayerFlavor(String flavor) { gameData.put("playerFlavor", flavor); }
    public String getPlayerFlavor() { return (String) gameData.get("playerFlavor"); }

    public void setPlayerX(int x) { gameData.put("playerX", x); }
    public int getPlayerX() { return (Integer) gameData.get("playerX"); }

    public void setPlayerY(int y) { gameData.put("playerY", y); }
    public int getPlayerY() { return (Integer) gameData.get("playerY"); }

    public void setScore(int score) { gameData.put("score", score); }
    public int getScore() { return (Integer) gameData.get("score"); }

    // Datos del nivel
    public void setCollectedFruits(int count) { gameData.put("collectedFruits", count); }
    public int getCollectedFruits() { return (Integer) gameData.get("collectedFruits"); }

    public void setTotalFruits(int count) { gameData.put("totalFruits", count); }
    public int getTotalFruits() { return (Integer) gameData.get("totalFruits"); }

    public void setRemainingTime(long time) { gameData.put("remainingTime", time); }
    public long getRemainingTime() { return (Long) gameData.get("remainingTime"); }

    // Agregar fruta
    public void addFruit(int gridX, int gridY, boolean collected, String type) {
        Map<String, Object> fruit = new HashMap<>();
        fruit.put("gridX", gridX);
        fruit.put("gridY", gridY);
        fruit.put("collected", collected);
        fruit.put("type", type);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> fruits = (List<Map<String, Object>>) gameData.get("fruits");
        fruits.add(fruit);
    }

    // Obtener frutas
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getFruits() {
        return (List<Map<String, Object>>) gameData.get("fruits");
    }

    // Agregar enemigo
    public void addEnemy(int gridX, int gridY, boolean active, String type, String direction) {
        Map<String, Object> enemy = new HashMap<>();
        enemy.put("gridX", gridX);
        enemy.put("gridY", gridY);
        enemy.put("active", active);
        enemy.put("type", type);
        enemy.put("direction", direction);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> enemies = (List<Map<String, Object>>) gameData.get("enemies");
        enemies.add(enemy);
    }

    // Obtener enemigos
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getEnemies() {
        return (List<Map<String, Object>>) gameData.get("enemies");
    }

    // Agregar bloque de hielo
    public void addIceBlock(int gridX, int gridY, String type) {
        Map<String, Object> block = new HashMap<>();
        block.put("gridX", gridX);
        block.put("gridY", gridY);
        block.put("type", type);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> blocks = (List<Map<String, Object>>) gameData.get("iceBlocks");
        blocks.add(block);
    }

    // Obtener bloques
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getIceBlocks() {
        return (List<Map<String, Object>>) gameData.get("iceBlocks");
    }

    /**
     * Guarda el estado del juego en un archivo
     */
    public boolean saveToFile(String filename) {
        try {
            File saveDir = new File("saves");
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }

            String filepath = "saves/" + filename + ".sav";
            FileOutputStream fileOut = new FileOutputStream(filepath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();

            System.out.println("Juego guardado en: " + filepath);
            return true;
        } catch (IOException e) {
            System.out.println("Error al guardar el juego: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carga el estado del juego desde un archivo
     */
    public static GameSaveData loadFromFile(String filename) {
        try {
            String filepath = "saves/" + filename + ".sav";
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            GameSaveData data = (GameSaveData) in.readObject();
            in.close();
            fileIn.close();

            System.out.println("Juego cargado desde: " + filepath);
            return data;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el juego: " + e.getMessage());
            return null;
        }
    }
}