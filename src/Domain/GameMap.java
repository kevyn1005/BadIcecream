package Domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Maneja el mapa del juego, bloques y muros usando un sistema de cuadricula.
 */
public class GameMap {
    private List<Block> blocks;
    private List<Wall> walls;
    private GridSystem grid;

    // Mapa para verificar rapidamente si hay un bloque en una celda
    private Map<Point, Block> gridOccupancy;

    public GameMap(int mapWidth, int mapHeight, int cellSize) {
        this.grid = new GameGrid(mapWidth, mapHeight, cellSize);
        this.blocks = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.gridOccupancy = new HashMap<>();
    }

    /**
     * Inicializa los muros del borde del mapa
     */
    public void initializeBorderWalls() {
        int gridWidth = grid.getGridWidth();
        int gridHeight = grid.getGridHeight();

        // Esquinas (2x2 celdas cada una)
        walls.add(new CornerWall(0, 0, grid, "Images/Resources/MuroEsquina.png"));
        walls.add(new CornerWall(gridWidth - 2, 0, grid, "Images/Resources/MuroEsquina.png"));
        walls.add(new CornerWall(0, gridHeight - 2, grid, "Images/Resources/MuroEsquina.png"));
        walls.add(new CornerWall(gridWidth - 2, gridHeight - 2, grid, "Images/Resources/MuroEsquina.png"));

        // Muros horizontales superiores
        for (int x = 2; x < gridWidth - 2; x++) {
            walls.add(new HorizontalWall(x, 0, grid, "Images/Resources/MuroInferior.png"));
        }

        // Muros horizontales inferiores
        for (int x = 2; x < gridWidth - 2; x++) {
            walls.add(new HorizontalWall(x, gridHeight - 1, grid, "Images/Resources/MuroInferior.png"));
        }

        // Muros verticales izquierdos
        for (int y = 2; y < gridHeight - 2; y++) {
            walls.add(new VerticalWall(0, y, grid, "Images/Resources/Muro.png"));
        }

        // Muros verticales derechos
        for (int y = 2; y < gridHeight - 2; y++) {
            walls.add(new VerticalWall(gridWidth - 1, y, grid, "Images/Resources/Muro.png"));
        }

        System.out.println("Muros del borde inicializados: " + walls.size() + " muros");
    }

    /**
     * Crea hielos iniciales en posiciones especificas del mapa
     * Estos hielos comienzan en estado solido (sprite 7)
     */
    public void createInitialIceLayout() {
        // ORDEN: De arriba hacia abajo, de izquierda a derecha
        // Esto asegura que los hielos inferiores se dibujen sobre los superiores

        // Fila 1 (borde superior) - de izquierda a derecha
        createSolidIceAt(2, 1);
        createSolidIceAt(3, 1);
        createSolidIceAt(4, 1);
        createSolidIceAt(5, 1);
        createSolidIceAt(6, 1);
        createSolidIceAt(7, 1);
        createSolidIceAt(8, 1);
        createSolidIceAt(9, 1);
        createSolidIceAt(10, 1);
        createSolidIceAt(11, 1);
        createSolidIceAt(12, 1);
        createSolidIceAt(13, 1);
        createSolidIceAt(14, 1);
        createSolidIceAt(15, 1);

        // Columna 1 (borde izquierdo) - de arriba hacia abajo
        createSolidIceAt(1, 2);
        createSolidIceAt(1, 3);
        createSolidIceAt(1, 4);
        createSolidIceAt(1, 5);
        createSolidIceAt(1, 6);
        createSolidIceAt(1, 7);
        createSolidIceAt(1, 8);
        createSolidIceAt(1, 9);
        createSolidIceAt(1, 10);
        createSolidIceAt(1, 11);
        createSolidIceAt(1, 12);
        createSolidIceAt(1, 13);
        createSolidIceAt(1, 14);
        createSolidIceAt(1, 15);

        // Columna 16 (borde derecho) - de arriba hacia abajo
        createSolidIceAt(16, 2);
        createSolidIceAt(16, 3);
        createSolidIceAt(16, 4);
        createSolidIceAt(16, 5);
        createSolidIceAt(16, 6);
        createSolidIceAt(16, 7);
        createSolidIceAt(16, 8);
        createSolidIceAt(16, 9);
        createSolidIceAt(16, 10);
        createSolidIceAt(16, 11);
        createSolidIceAt(16, 12);
        createSolidIceAt(16, 13);
        createSolidIceAt(16, 14);
        createSolidIceAt(16, 15);

        // Bloque superior izquierdo (fila 5) - de izquierda a derecha
        createSolidIceAt(4, 5);
        createSolidIceAt(5, 5);
        createSolidIceAt(6, 5);

        // Bloque superior derecho (fila 5) - de izquierda a derecha
        createSolidIceAt(11, 5);
        createSolidIceAt(12, 5);
        createSolidIceAt(13, 5);

        // Columna 4 (fila 6) - individual
        createSolidIceAt(4, 6);

        // Columna 13 (fila 6) - individual
        createSolidIceAt(13, 6);

        // Bloque medio izquierdo (columna 4, filas 7-9) - de arriba hacia abajo
        createSolidIceAt(4, 7);
        createSolidIceAt(4, 8);
        createSolidIceAt(4, 9);

        // Bloque medio derecho (columna 13, filas 7-9) - de arriba hacia abajo
        createSolidIceAt(13, 7);
        createSolidIceAt(13, 8);
        createSolidIceAt(13, 9);

        // Columna 4 (fila 10) - individual
        createSolidIceAt(4, 10);

        // Columna 13 (fila 10) - individual
        createSolidIceAt(13, 10);

        // Bloque inferior izquierdo (columna 4, fila 11) - individual
        createSolidIceAt(4, 11);

        // Bloque inferior derecho (columna 13, fila 11) - individual
        createSolidIceAt(13, 11);

        // Fila 12 - de izquierda a derecha (todos los bloques de esta fila)
        createSolidIceAt(4, 12);
        createSolidIceAt(5, 12);
        createSolidIceAt(6, 12);
        createSolidIceAt(11, 12);
        createSolidIceAt(12, 12);
        createSolidIceAt(13, 12);

        // Fila 16 (borde inferior) - de izquierda a derecha
        createSolidIceAt(2, 16);
        createSolidIceAt(3, 16);
        createSolidIceAt(4, 16);
        createSolidIceAt(5, 16);
        createSolidIceAt(6, 16);
        createSolidIceAt(7, 16);
        createSolidIceAt(8, 16);
        createSolidIceAt(9, 16);
        createSolidIceAt(10, 16);
        createSolidIceAt(11, 16);
        createSolidIceAt(12, 16);
        createSolidIceAt(13, 16);
        createSolidIceAt(14, 16);
        createSolidIceAt(15, 16);
        createSolidIceAt(16, 16);

        System.out.println("Hielos iniciales creados: " + blocks.size() + " bloques");
    }

    /**
     * Crea un bloque de hielo solido en una posicion de grid especifica
     */
    private void createSolidIceAt(int gridX, int gridY) {
        Point pixelPos = grid.gridToPixel(gridX, gridY);
        Block ice = new SolidIceBlock(pixelPos.x, pixelPos.y);
        blocks.add(ice);
        gridOccupancy.put(new Point(gridX, gridY), ice);
    }

    /**
     * Crear hielo en una direccion hasta encontrar limite o obstaculo
     */
    public void createIceInDirection(int playerPixelX, int playerPixelY, Player.Direction direction) {
        Point playerGrid = grid.pixelToGrid(playerPixelX, playerPixelY);
        Point currentGrid = grid.getNextGridPosition(playerGrid.x, playerGrid.y, direction);

        while (grid.isInBounds(currentGrid.x, currentGrid.y)) {
            if (hasBlockAtGrid(currentGrid.x, currentGrid.y)) {
                break;
            }

            Point pixelPos = grid.gridToPixel(currentGrid.x, currentGrid.y);
            Block newBlock = new IceBlock(pixelPos.x, pixelPos.y);
            blocks.add(newBlock);
            gridOccupancy.put(new Point(currentGrid.x, currentGrid.y), newBlock);

            currentGrid = grid.getNextGridPosition(currentGrid.x, currentGrid.y, direction);
        }
    }

    /**
     * Destruye bloques de hielo en una direccion
     */
    public boolean destroyIceInDirection(int playerPixelX, int playerPixelY, Player.Direction direction) {
        Point playerGrid = grid.pixelToGrid(playerPixelX, playerPixelY);
        Point currentGrid = grid.getNextGridPosition(playerGrid.x, playerGrid.y, direction);

        boolean foundIce = false;

        while (grid.isInBounds(currentGrid.x, currentGrid.y)) {
            Point gridPoint = new Point(currentGrid.x, currentGrid.y);
            Block block = gridOccupancy.get(gridPoint);

            if (block != null && block.getType() == Block.BlockType.ICE) {
                block.startDestroy();
                foundIce = true;
                System.out.println("Destruyendo hielo en: (" + currentGrid.x + ", " + currentGrid.y + ")");
            } else if (hasBlockAtGrid(currentGrid.x, currentGrid.y)) {
                break;
            }

            currentGrid = grid.getNextGridPosition(currentGrid.x, currentGrid.y, direction);
        }

        return foundIce;
    }

    /**
     * Verifica si hay un bloque o muro en una posicion de cuadricula
     */
    public boolean hasBlockAtGrid(int gridX, int gridY) {
        for (Wall wall : walls) {
            if (wall.occupiesCell(gridX, gridY)) {
                return true;
            }
        }

        Point gridPoint = new Point(gridX, gridY);
        Block block = gridOccupancy.get(gridPoint);
        return block != null && block.isSolid();
    }

    /**
     * Verifica si hay un bloque en una posicion de pixeles
     */
    public boolean hasBlockAtPixel(int pixelX, int pixelY) {
        Point gridPos = grid.pixelToGrid(pixelX, pixelY);
        return hasBlockAtGrid(gridPos.x, gridPos.y);
    }

    /**
     * Obtiene bloques que deben dibujarse en capa inferior (fila 16)
     */
    public List<Block> getBottomLayerBlocks() {
        List<Block> bottomBlocks = new ArrayList<>();
        for (Block block : blocks) {
            Point gridPos = grid.pixelToGrid(block.getX(), block.getY());
            if (gridPos.y == 16) {
                bottomBlocks.add(block);
            }
        }
        return bottomBlocks;
    }

    /**
     * Obtiene bloques que deben dibujarse en capa superior (todas excepto fila 16)
     */
    public List<Block> getTopLayerBlocks() {
        List<Block> topBlocks = new ArrayList<>();
        for (Block block : blocks) {
            Point gridPos = grid.pixelToGrid(block.getX(), block.getY());
            if (gridPos.y != 16) {
                topBlocks.add(block);
            }
        }
        return topBlocks;
    }

    /**
     * Actualizar todos los bloques y limpiar los destruidos
     */
    public void update() {
        List<Block> toRemove = new ArrayList<>();
        List<Point> keysToRemove = new ArrayList<>();

        for (Block block : blocks) {
            block.update();

            if (block.isDestroyed()) {
                toRemove.add(block);

                for (Map.Entry<Point, Block> entry : gridOccupancy.entrySet()) {
                    if (entry.getValue() == block) {
                        keysToRemove.add(entry.getKey());
                        break;
                    }
                }
            }
        }

        blocks.removeAll(toRemove);
        for (Point key : keysToRemove) {
            gridOccupancy.remove(key);
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public void clear() {
        blocks.clear();
        gridOccupancy.clear();
        walls.clear();
    }

    public GridSystem getGrid() {
        return grid;
    }

    public Point snapToGrid(int pixelX, int pixelY) {
        return grid.snapToGrid(pixelX, pixelY);
    }
}