package Presentation;

import Domain.GameLoader;

/**
 * Almacena los datos de la sesion de juego actual
 * Se comparte entre las diferentes pantallas
 */
public class GameSessionData {
    private String player1Name;
    private String player1Flavor;
    private GameLoader gameLoader; // NUEVO

    public GameSessionData() {
        this.player1Name = "Player 1";
        this.player1Flavor = "vanilla";
        this.gameLoader = null; // NUEVO
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String name) {
        this.player1Name = name;
    }

    public String getPlayer1Flavor() {
        return player1Flavor;
    }

    public void setPlayer1Flavor(String flavor) {
        this.player1Flavor = flavor;
    }

    // NUEVOS METODOS PARA EL LOADER
    public GameLoader getGameLoader() {
        return gameLoader;
    }

    public void setGameLoader(GameLoader loader) {
        this.gameLoader = loader;
    }

    public boolean hasGameLoader() {
        return gameLoader != null && gameLoader.hasValidData();
    }

    public void clearGameLoader() {
        this.gameLoader = null;
    }
}