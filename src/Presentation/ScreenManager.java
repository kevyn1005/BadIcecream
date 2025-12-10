package Presentation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

// Administrador que maneja el cambio entre pantallas usando polimorfismo
public class ScreenManager {
    private JFrame frame;
    private Map<String, Screen> screens;
    private Screen currentScreen;
    private GameSessionData sessionData;

    public ScreenManager(JFrame frame) {
        this.frame = frame;
        this.screens = new HashMap<>();
        this.sessionData = new GameSessionData();
        initializeScreens();
    }

    public GameSessionData getSessionData() {
        return sessionData;
    }

    private void initializeScreens() {
        // Registrar todas las pantallas del juego
        screens.put("menu", new MenuScreen(this));
        screens.put("options", new OptionsScreen(this));
        screens.put("game", new GameScreen(this));
        screens.put("gameType", new GameTypeScreen(this));
        screens.put("flavorSelection", new FlavorSelectionScreen(this));
        screens.put("levelSelection", new LevelSelectionScreen(this));

        // Pantallas de 2 jugadores - crear referencia para compartir estado
        TwoPlayerModeScreen twoPlayerMode = new TwoPlayerModeScreen(this);
        screens.put("twoPlayerMode", twoPlayerMode);
        screens.put("twoPlayerFlavorSelection", new TwoPlayerFlavorSelectionScreen(this, twoPlayerMode));
    }

    public void showScreen(String screenName) {
        Screen newScreen = screens.get(screenName);

        if (newScreen == null) {
            System.out.println("Error: Pantalla '" + screenName + "' no existe");
            return;
        }

        // Ocultar pantalla actual
        if (currentScreen != null) {
            currentScreen.onHide();
            frame.remove(currentScreen.getPanel());
        }

        // Mostrar nueva pantalla
        currentScreen = newScreen;
        currentScreen.onShow();
        frame.add(currentScreen.getPanel());
        frame.revalidate();
        frame.repaint();
    }

    public void addScreen(String name, Screen screen) {
        screens.put(name, screen);
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }
}