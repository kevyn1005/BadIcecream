package Presentation;

import Domain.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Pantalla del juego con sistema de capas para renderizado correcto
 * Incluye sistema de pausa, animacion de muerte, pantalla de muerte, animacion de victoria y pantalla de victoria
 */
public class GameScreen implements Screen {
    private JPanel panel;
    private GamePanel gamePanel;
    private final ScreenManager screenManager;
    private IceCreamPlayer player;
    private GameMap gameMap;
    private CollisionDetector collisionDetector;
    private FruitManager fruitManager;
    private EnemyManager enemyManager;
    private PauseButton pauseButton;
    private PauseOverlay pauseOverlay;
    private DeathOverlay deathOverlay;
    private VictoryOverlay victoryOverlay;
    private PlayerScoreUI playerScoreUI;
    private Timer gameTimer;
    private GameTimer timeLimit;

    private static final int CELL_SIZE = 42;
    private static final int GAME_WIDTH = 756;
    private static final int GAME_HEIGHT = 756;
    private boolean playerDead = false;
    private boolean levelComplete = false;
    private int winAnimationCounter = 0;
    private static final int WIN_ANIMATION_DURATION = 120;

    public GameScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        initializePanel();
    }

    private void initializePanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        panel.setBackground(Color.BLACK);

        gamePanel = new GamePanel();
        panel.add(gamePanel, BorderLayout.CENTER);
    }

    private void startGame() {
        playerDead = false;
        levelComplete = false;
        winAnimationCounter = 0;

        GameSessionData sessionData = screenManager.getSessionData();

        // VERIFICAR SI HAY UN LOADER CON DATOS DE GUARDADO
        if (sessionData.hasGameLoader()) {
            loadGameFromSave(sessionData.getGameLoader());
            sessionData.clearGameLoader();
            return;
        }

        // CODIGO ORIGINAL PARA JUEGO NUEVO
        String playerName = sessionData.getPlayer1Name();
        String playerFlavor = sessionData.getPlayer1Flavor();

        System.out.println("=== INICIANDO JUEGO ===");
        System.out.println("Jugador: " + playerName);
        System.out.println("Sabor: " + playerFlavor);
        System.out.println("=======================");

        playerScoreUI = new PlayerScoreUI(playerName, playerFlavor);
        pauseButton = new PauseButton(GAME_WIDTH, GAME_HEIGHT);
        pauseOverlay = new PauseOverlay(GAME_WIDTH, GAME_HEIGHT);
        deathOverlay = new DeathOverlay(GAME_WIDTH, GAME_HEIGHT);
        victoryOverlay = new VictoryOverlay(GAME_WIDTH, GAME_HEIGHT);
        timeLimit = new GameTimer(GAME_WIDTH);

        gameMap = new GameMap(GAME_WIDTH, GAME_HEIGHT, CELL_SIZE);
        gameMap.initializeBorderWalls();
        gameMap.createInitialIceLayout();

        collisionDetector = new GameCollisionDetector(gameMap, gameMap.getGrid());

        fruitManager = new FruitManager(gameMap.getGrid());
        fruitManager.createLevelBananas();

        enemyManager = new EnemyManager(gameMap.getGrid(), collisionDetector);
        enemyManager.createTroll(8, 7);
        enemyManager.createTroll(5, 5);

        // USAR EL SABOR PARA CARGAR LOS SPRITES CORRECTOS
        String spritePath = getSpritePathForFlavor(playerFlavor);
        player = new IceCreamPlayer(320, 300, spritePath);
        player.setCollisionDetector(collisionDetector, gameMap.getGrid());

        Point alignedPos = gameMap.snapToGrid(player.getX(), player.getY());
        player.setPosition(alignedPos.x, alignedPos.y);

        gameTimer = new Timer(16, e -> {
            if (!playerDead && !levelComplete && !pauseButton.isPaused()) {
                player.update();
                gameMap.update();
                fruitManager.update();
                enemyManager.update();
                timeLimit.update();

                if (timeLimit.isTimeUp()) {
                    handlePlayerDeath();
                }

                if (player.shouldCreateIce()) {
                    Player.Direction dir = player.getDirection();
                    gameMap.createIceInDirection(player.getX(), player.getY(), dir);
                    player.resetIceCreation();
                }

                if (player.shouldDestroyIce()) {
                    Player.Direction dir = player.getDirection();
                    boolean destroyed = gameMap.destroyIceInDirection(
                            player.getX(), player.getY(), dir);
                    player.resetIceDestruction();
                }

                fruitManager.checkCollisions(player);

                if (enemyManager.checkCollisionWithPlayer(player)) {
                    handlePlayerDeath();
                }

                if (fruitManager.allFruitsCollected() && !levelComplete) {
                    handleLevelComplete();
                }
            } else if (levelComplete && player != null) {
                player.update();
                winAnimationCounter++;

                if (winAnimationCounter >= WIN_ANIMATION_DURATION) {
                    final int finalScore = (fruitManager != null) ? fruitManager.getScore() : 0;
                    victoryOverlay.show(finalScore);
                }
            } else if (playerDead && player != null) {
                player.update();
            }

            gamePanel.repaint();
        });
        gameTimer.start();

        System.out.println("Juego iniciado con hielos iniciales.");
    }

    private void loadGameFromSave(GameLoader loader) {
        System.out.println("=== CARGANDO PARTIDA GUARDADA ===");
        System.out.println("Jugador: " + loader.getPlayerName());
        System.out.println("Sabor: " + loader.getPlayerFlavor());
        System.out.println("Puntaje: " + loader.getScore());
        System.out.println("==================================");

        playerScoreUI = new PlayerScoreUI(loader.getPlayerName(), loader.getPlayerFlavor());
        pauseButton = new PauseButton(GAME_WIDTH, GAME_HEIGHT);
        pauseOverlay = new PauseOverlay(GAME_WIDTH, GAME_HEIGHT);
        deathOverlay = new DeathOverlay(GAME_WIDTH, GAME_HEIGHT);
        victoryOverlay = new VictoryOverlay(GAME_WIDTH, GAME_HEIGHT);
        timeLimit = new GameTimer(GAME_WIDTH);

        gameMap = new GameMap(GAME_WIDTH, GAME_HEIGHT, CELL_SIZE);
        gameMap.initializeBorderWalls();

        collisionDetector = new GameCollisionDetector(gameMap, gameMap.getGrid());

        // Usar el loader para cargar todo
        loader.loadIceBlocks(gameMap);

        fruitManager = new FruitManager(gameMap.getGrid());
        loader.loadFruits(fruitManager, gameMap.getGrid());

        enemyManager = new EnemyManager(gameMap.getGrid(), collisionDetector);
        loader.loadEnemies(enemyManager);

        String spritePath = getSpritePathForFlavor(loader.getPlayerFlavor());
        player = new IceCreamPlayer(loader.getPlayerX(), loader.getPlayerY(), spritePath);
        player.setCollisionDetector(collisionDetector, gameMap.getGrid());

        Point alignedPos = gameMap.snapToGrid(player.getX(), player.getY());
        player.setPosition(alignedPos.x, alignedPos.y);

        // MISMO CODIGO DEL TIMER QUE EN startGame()
        gameTimer = new Timer(16, e -> {
            if (!playerDead && !levelComplete && !pauseButton.isPaused()) {
                player.update();
                gameMap.update();
                fruitManager.update();
                enemyManager.update();
                timeLimit.update();

                if (timeLimit.isTimeUp()) {
                    handlePlayerDeath();
                }

                if (player.shouldCreateIce()) {
                    Player.Direction dir = player.getDirection();
                    gameMap.createIceInDirection(player.getX(), player.getY(), dir);
                    player.resetIceCreation();
                }

                if (player.shouldDestroyIce()) {
                    Player.Direction dir = player.getDirection();
                    boolean destroyed = gameMap.destroyIceInDirection(
                            player.getX(), player.getY(), dir);
                    player.resetIceDestruction();
                }

                fruitManager.checkCollisions(player);

                if (enemyManager.checkCollisionWithPlayer(player)) {
                    handlePlayerDeath();
                }

                if (fruitManager.allFruitsCollected() && !levelComplete) {
                    handleLevelComplete();
                }
            } else if (levelComplete && player != null) {
                player.update();
                winAnimationCounter++;

                if (winAnimationCounter >= WIN_ANIMATION_DURATION) {
                    final int finalScore = (fruitManager != null) ? fruitManager.getScore() : 0;
                    victoryOverlay.show(finalScore);
                }
            } else if (playerDead && player != null) {
                player.update();
            }

            gamePanel.repaint();
        });
        gameTimer.start();

        System.out.println("Partida cargada correctamente.");
    }



    private String getSpritePathForFlavor(String flavor) {
        if (flavor == null || flavor.isEmpty()) {
            return "Images/IceCreams/Vanilla";
        }

        switch (flavor.toLowerCase()) {
            case "brown":
                return "Images/IceCreams/Brown";
            case "vanilla":
                return "Images/IceCreams/Vanilla";
            case "pink":
                return "Images/IceCreams/Pink";
            default:
                return "Images/IceCreams/Vanilla";
        }
    }

    private void handleLevelComplete() {
        if (levelComplete) return;

        levelComplete = true;
        winAnimationCounter = 0;
        System.out.println("Nivel completado! Puntaje final: " + fruitManager.getScore());

        if (player != null) {
            player.startWinAnimation();
        }
    }

    private void handlePlayerDeath() {
        if (playerDead) return;

        playerDead = true;
        System.out.println("El jugador ha muerto!");

        if (player != null) {
            player.startDeathAnimation();
        }

        final int finalScore = (fruitManager != null) ? fruitManager.getScore() : 0;

        Timer showDeathOverlayTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player != null && player.isDeathAnimationComplete()) {
                    deathOverlay.show(finalScore);
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        showDeathOverlayTimer.start();
    }

    /**
     * Guarda el estado actual del juego
     * Abre un dialogo para que el usuario seleccione donde guardar
     */
    /**
     * Guarda el estado actual del juego
     * Abre un dialogo para que el usuario seleccione donde guardar
     */
    private void saveGame() {
        if (player == null || fruitManager == null || enemyManager == null || gameMap == null) {
            System.out.println("No se puede guardar el juego en este momento");
            return;
        }

        // Crear el dialogo de seleccion de archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar partida");

        // Filtro para solo mostrar archivos .sav
        javax.swing.filechooser.FileNameExtensionFilter filter =
                new javax.swing.filechooser.FileNameExtensionFilter("Archivos de guardado (*.sav)", "sav");
        fileChooser.setFileFilter(filter);

        // Sugerir un nombre de archivo por defecto
        GameSessionData sessionData = screenManager.getSessionData();
        String defaultName = sessionData.getPlayer1Name() + "_" + System.currentTimeMillis();
        fileChooser.setSelectedFile(new java.io.File(defaultName));

        // Mostrar el dialogo
        int userSelection = fileChooser.showSaveDialog(panel);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String filepath = fileToSave.getAbsolutePath();

            // Asegurar que termine en .sav
            if (!filepath.endsWith(".sav")) {
                filepath += ".sav";
            }

            // Crear objeto de guardado con todos los datos
            GameSaveData saveData = new GameSaveData();

            // Guardar datos del jugador
            saveData.setPlayerName(sessionData.getPlayer1Name());
            saveData.setPlayerFlavor(sessionData.getPlayer1Flavor());
            saveData.setPlayerX(player.getX());
            saveData.setPlayerY(player.getY());
            saveData.setScore(fruitManager.getScore());

            // Guardar datos del nivel
            saveData.setCollectedFruits(fruitManager.getCollectedCount());
            saveData.setTotalFruits(fruitManager.getTotalCount());
            saveData.setRemainingTime(timeLimit != null ? timeLimit.getRemainingTimeMs() : 0);

            // Guardar frutas
            for (Fruit fruit : fruitManager.getFruits()) {
                Point gridPos = gameMap.getGrid().pixelToGrid(fruit.getX(), fruit.getY());
                saveData.addFruit(
                        gridPos.x,
                        gridPos.y,
                        fruit.isCollected(),
                        fruit.getType().toString()
                );
            }

            // Guardar enemigos
            for (Enemy enemy : enemyManager.getEnemies()) {
                Point gridPos = enemy.getGridPosition();
                saveData.addEnemy(
                        gridPos.x,
                        gridPos.y,
                        enemy.isActive(),
                        enemy.getType().toString(),
                        "DOWN"  // Direccion por defecto
                );
            }

            // Guardar bloques de hielo
            for (Block block : gameMap.getBlocks()) {
                Point gridPos = gameMap.getGrid().pixelToGrid(block.getX(), block.getY());
                saveData.addIceBlock(
                        gridPos.x,
                        gridPos.y,
                        block.getType().toString()
                );
            }

            // Guardar usando el path completo (sin agregar "saves/" otra vez)
            if (saveData.saveToFile(filepath)) {
                System.out.println("Juego guardado exitosamente en: " + filepath);
                JOptionPane.showMessageDialog(panel,
                        "Partida guardada exitosamente!",
                        "Guardar",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("Error al guardar el juego");
                JOptionPane.showMessageDialog(panel,
                        "Error al guardar la partida",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Guardado cancelado por el usuario");
        }
    }


    private void stopGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void onShow() {
        System.out.println("Mostrando pantalla de juego");
        startGame();

        SwingUtilities.invokeLater(() -> {
            gamePanel.requestFocusInWindow();
            gamePanel.requestFocus();
        });
    }

    @Override
    public void onHide() {
        System.out.println("Ocultando pantalla de juego");
        stopGame();
    }

    private class GamePanel extends JPanel {
        private Image backgroundImage;
        private boolean upPressed, downPressed, leftPressed, rightPressed;
        private boolean showGrid = false;

        public GamePanel() {
            setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
            setBackground(Color.BLACK);
            setFocusable(true);
            loadBackgroundImage();

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    handleKeyPress(e, true);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    handleKeyPress(e, false);
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Prioridad 1: Victory overlay
                    if (victoryOverlay != null && victoryOverlay.isVisible()) {
                        String action = victoryOverlay.handleClick(e.getPoint());
                        if (action != null) {
                            stopGame();
                            if (action.equals("levelSelection")) {
                                screenManager.showScreen("levelSelection");
                            } else if (action.equals("menu")) {
                                screenManager.showScreen("menu");
                            }
                            return;
                        }
                    }

                    // Prioridad 2: Death overlay
                    if (deathOverlay != null && deathOverlay.isVisible()) {
                        String action = deathOverlay.handleClick(e.getPoint());
                        if (action != null) {
                            if (action.equals("restart")) {
                                stopGame();
                                startGame();
                            } else if (action.equals("menu")) {
                                stopGame();
                                screenManager.showScreen("menu");
                            }
                            return;
                        }
                    }

                    // Prioridad 3: Pause overlay
                    if (pauseOverlay != null && pauseOverlay.isVisible()) {
                        String action = pauseOverlay.handleClick(e.getPoint());
                        if (action != null) {
                            if (action.equals("continue")) {
                                pauseOverlay.hide();
                                pauseButton.resume();
                                if (timeLimit != null) {
                                    timeLimit.resume();
                                }
                            } else if (action.equals("back")) {
                                pauseOverlay.hide();
                                stopGame();
                                screenManager.showScreen("menu");
                            } else if (action.equals("save")) {
                                saveGame();
                            }
                            repaint();
                            return;
                        }
                    }

                    // Prioridad 4: Pause button
                    if (pauseButton != null && !playerDead && !levelComplete) {
                        if (pauseButton.handleClick(e.getPoint())) {
                            if (pauseButton.isPaused()) {
                                pauseOverlay.show();
                                if (timeLimit != null) {
                                    timeLimit.pause();
                                }
                            } else {
                                pauseOverlay.hide();
                                if (timeLimit != null) {
                                    timeLimit.resume();
                                }
                            }
                            repaint();
                        }
                    }
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    Point mousePos = e.getPoint();
                    boolean needsRepaint = false;

                    if (victoryOverlay != null && victoryOverlay.isVisible()) {
                        if (victoryOverlay.updateHover(mousePos)) {
                            needsRepaint = true;
                        }
                        setCursor(victoryOverlay.getCursor(mousePos));
                    } else if (deathOverlay != null && deathOverlay.isVisible()) {
                        if (deathOverlay.updateHover(mousePos)) {
                            needsRepaint = true;
                        }
                        setCursor(deathOverlay.getCursor(mousePos));
                    } else if (pauseOverlay != null && pauseOverlay.isVisible()) {
                        if (pauseOverlay.updateHover(mousePos)) {
                            needsRepaint = true;
                        }
                        setCursor(pauseOverlay.getCursor(mousePos));
                    } else {
                        if (pauseButton != null) {
                            if (pauseButton.updateHover(mousePos)) {
                                needsRepaint = true;
                            }
                        }
                        setCursor(Cursor.getDefaultCursor());
                    }

                    if (needsRepaint) {
                        repaint();
                    }
                }
            });
        }

        private void loadBackgroundImage() {
            try {
                String path = "Images/Nieve.png";
                backgroundImage = new ImageIcon(path).getImage();
                if (backgroundImage.getWidth(null) > 0) {
                    System.out.println("Imagen de fondo cargada desde: " + path);
                }
            } catch (Exception e) {
                System.out.println("Error cargando imagen de fondo: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGame(g);
        }

        private void drawGame(Graphics g) {
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            }

            if (showGrid && gameMap != null) {
                drawDebugGrid(g);
            }

            if (gameMap != null) {
                for (Block block : gameMap.getBottomLayerBlocks()) {
                    drawBlock(g, block);
                }
            }

            if (gameMap != null) {
                for (Wall wall : gameMap.getWalls()) {
                    drawWall(g, wall);
                }
            }

            if (gameMap != null) {
                for (Block block : gameMap.getTopLayerBlocks()) {
                    drawBlock(g, block);
                }
            }

            if (fruitManager != null) {
                for (Fruit fruit : fruitManager.getFruits()) {
                    if (!fruit.isCollected()) {
                        drawFruit(g, fruit);
                    }
                }
            }

            if (enemyManager != null) {
                for (Enemy enemy : enemyManager.getEnemies()) {
                    if (enemy.isActive()) {
                        drawEnemy(g, enemy);
                    }
                }
            }

            if (player != null) {
                drawPlayer(g);
            }

            // Dibujar UI del jugador (icono, puntaje y nombre)
            if (playerScoreUI != null && fruitManager != null) {
                playerScoreUI.draw(g, fruitManager.getScore());
            }

            // Dibujar contador de enemigos
            drawEnemyCounter(g);

            // Dibujar contador de frutas
            drawFruitCounter(g);

            if (timeLimit != null && !playerDead && !levelComplete) {
                timeLimit.draw(g);
            }

            drawPauseButton(g);

            if (pauseOverlay != null) {
                pauseOverlay.draw(g);
            }

            if (deathOverlay != null) {
                deathOverlay.draw(g);
            }

            if (victoryOverlay != null) {
                victoryOverlay.draw(g);
            }
        }

        private void drawEnemyCounter(Graphics g) {
            if (enemyManager == null) return;

            Map<Enemy.EnemyType, Integer> enemyCounts = enemyManager.getEnemyCountsByType();

            if (enemyCounts.isEmpty()) return;

            int startX = 670;
            int startY = 25;
            int lineHeight = 22;
            int currentY = startY;


            currentY += lineHeight;

            // Dibujar cada tipo de enemigo y su cantidad
            Font countFont = new Font("Arial", Font.PLAIN, 15);
            g.setFont(countFont);

            for (Map.Entry<Enemy.EnemyType, Integer> entry : enemyCounts.entrySet()) {
                String enemyName = getEnemyDisplayName(entry.getKey());
                String text = "  " + enemyName + ": " + entry.getValue();

                // Sombra negra
                g.setColor(Color.BLACK);
                g.drawString(text, startX + 1, currentY + 1);

                // Texto blanco
                g.setColor(Color.WHITE);
                g.drawString(text, startX, currentY);

                currentY += lineHeight;
            }
        }

        private String getEnemyDisplayName(Enemy.EnemyType type) {
            switch (type) {
                case TROLL:
                    return "Troll";
                default:
                    return "Enemigo";
            }
        }

        private void drawFruitCounter(Graphics g) {
            if (fruitManager == null) return;

            g.setFont(new Font("Arial", Font.PLAIN, 16));
            String fruitText = "Frutas: " + fruitManager.getCollectedCount() + "/" + fruitManager.getTotalCount();

            g.setColor(Color.BLACK);
            g.drawString(fruitText, 21, 735 );

            g.setColor(Color.WHITE);
            g.drawString(fruitText, 20, 735);
        }

        private void drawPauseButton(Graphics g) {
            if (pauseButton == null) return;

            Image buttonImage = pauseButton.getCurrentImage();
            if (buttonImage != null) {
                g.drawImage(buttonImage,
                        pauseButton.getX(),
                        pauseButton.getY(),
                        pauseButton.getWidth(),
                        pauseButton.getHeight(),
                        null);
            }
        }

        private void drawBlock(Graphics g, Block block) {
            Image sprite = block.getCurrentSprite();
            if (sprite != null) {
                g.drawImage(sprite,
                        block.getX(),
                        block.getY(),
                        block.getWidth(),
                        block.getHeight(),
                        null);

                if (showGrid && block.isSolid()) {
                    g.setColor(new Color(255, 0, 0, 100));
                    g.fillRect(block.getX(), block.getY(),
                            CELL_SIZE, CELL_SIZE);
                }
            }
        }

        private void drawWall(Graphics g, Wall wall) {
            Image sprite = wall.getSprite();
            if (sprite != null) {
                g.drawImage(sprite,
                        wall.getX(),
                        wall.getY(),
                        wall.getWidth(),
                        wall.getHeight(),
                        null);

                if (showGrid) {
                    g.setColor(new Color(128, 0, 128, 100));
                    g.fillRect(wall.getX(), wall.getY(),
                            wall.getWidth(), wall.getHeight());
                }
            }
        }

        private void drawFruit(Graphics g, Fruit fruit) {
            Image sprite = fruit.getCurrentSprite();
            if (sprite != null) {
                int fruitDrawX = fruit.getX() + (CELL_SIZE - fruit.getWidth()) / 2;
                int fruitDrawY = fruit.getY() + (CELL_SIZE - fruit.getHeight()) / 2;

                g.drawImage(sprite,
                        fruitDrawX,
                        fruitDrawY,
                        fruit.getWidth(),
                        fruit.getHeight(),
                        null);
            }
        }

        private void drawEnemy(Graphics g, Enemy enemy) {
            Image sprite = enemy.getCurrentSprite();
            if (sprite != null) {
                int enemyDrawX = enemy.getX() + (CELL_SIZE - enemy.getWidth()) / 2;
                int enemyDrawY = enemy.getY() + (CELL_SIZE - enemy.getHeight()) / 2;

                g.drawImage(sprite,
                        enemyDrawX,
                        enemyDrawY,
                        enemy.getWidth(),
                        enemy.getHeight(),
                        null);

                if (showGrid) {
                    g.setColor(new Color(255, 165, 0, 150));
                    g.fillRect(enemy.getX(), enemy.getY(),
                            CELL_SIZE, CELL_SIZE);
                }
            }
        }

        private void drawPlayer(Graphics g) {
            Image sprite = player.getCurrentSprite();
            if (sprite != null) {
                int drawWidth = player.getWidth();
                int drawHeight = player.getHeight();

                int playerDrawX = player.getX() + (CELL_SIZE - drawWidth) / 2;
                int playerDrawY = player.getY() + (CELL_SIZE - drawHeight) / 2;

                if (player.isPerformingPutIceAction()) {
                    playerDrawY -= 15;
                }

                g.drawImage(sprite, playerDrawX, playerDrawY,
                        drawWidth, drawHeight, null);
            }
        }

        private void drawDebugGrid(Graphics g) {
            g.setColor(new Color(255, 0, 0, 150));

            int gridWidth = gameMap.getGrid().getGridWidth();
            int gridHeight = gameMap.getGrid().getGridHeight();

            for (int x = 0; x <= gridWidth; x++) {
                Point p = gameMap.getGrid().gridToPixel(x, 0);
                g.drawLine(p.x, 0, p.x, getHeight());
            }

            for (int y = 0; y <= gridHeight; y++) {
                Point p = gameMap.getGrid().gridToPixel(0, y);
                g.drawLine(0, p.y, getWidth(), p.y);
            }
        }

        private void handleKeyPress(KeyEvent e, boolean pressed) {
            if (player == null) return;

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_ESCAPE && pressed) {
                stopGame();
                screenManager.showScreen("levelSelection");
                return;
            }

            if (playerDead || levelComplete || (pauseButton != null && pauseButton.isPaused())) {
                return;
            }

            if (key == KeyEvent.VK_P && pressed && pauseButton != null) {
                pauseButton.toggle();
                if (pauseButton.isPaused()) {
                    pauseOverlay.show();
                    if (timeLimit != null) {
                        timeLimit.pause();
                    }
                } else {
                    pauseOverlay.hide();
                    if (timeLimit != null) {
                        timeLimit.resume();
                    }
                }
                repaint();
                return;
            }

            if (key == KeyEvent.VK_G && pressed) {
                showGrid = !showGrid;
                System.out.println("Cuadricula de debug: " + (showGrid ? "ON" : "OFF"));
            }

            if (key == KeyEvent.VK_SPACE && pressed && gameMap != null) {
                handleIceAction();
            }

            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                upPressed = pressed;
            }
            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                downPressed = pressed;
            }
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                leftPressed = pressed;
            }
            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                rightPressed = pressed;
            }

            updatePlayerMovement();
        }

        private void handleIceAction() {
            if (!player.canPerformAction()) {
                return;
            }

            if (player.isPerformingAction()) {
                return;
            }

            Player.Direction dir = player.getDirection();
            Point playerGrid = gameMap.getGrid().pixelToGrid(player.getX(), player.getY());
            Point nextGrid = gameMap.getGrid().getNextGridPosition(playerGrid.x, playerGrid.y, dir);

            boolean hasIceInDirection = false;
            if (gameMap.getGrid().isInBounds(nextGrid.x, nextGrid.y)) {
                for (Block block : gameMap.getBlocks()) {
                    Point blockGrid = gameMap.getGrid().pixelToGrid(block.getX(), block.getY());
                    if (blockGrid.x == nextGrid.x && blockGrid.y == nextGrid.y &&
                            block.getType() == Block.BlockType.ICE) {
                        hasIceInDirection = true;
                        break;
                    }
                }
            }

            boolean animationStarted = false;

            if (hasIceInDirection) {
                animationStarted = player.startBreakIceAnimation();
            } else {
                animationStarted = player.startPutIceAnimation();
            }

            if (animationStarted) {
                player.performAction();
            }
        }

        private void updatePlayerMovement() {
            if (player == null || gameMap == null || playerDead || levelComplete) return;

            if (player.isPerformingAction()) {
                return;
            }

            if (player.isOnCooldown()) {
                player.stopMoving();
                return;
            }

            if (upPressed) {
                player.moveUp();
            } else if (downPressed) {
                player.moveDown();
            } else if (leftPressed) {
                player.moveLeft();
            } else if (rightPressed) {
                player.moveRight();
            } else {
                player.stopMoving();
            }
        }
    }
}