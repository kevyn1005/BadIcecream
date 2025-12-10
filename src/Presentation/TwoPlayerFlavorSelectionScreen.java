package Presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Pantalla de seleccion de sabor para 2 jugadores
 * Permite elegir sabor y nombre/dificultad segun el modo de juego
 */
public class TwoPlayerFlavorSelectionScreen implements Screen {
    private JPanel panel;
    private JLabel backgroundLabel;
    private final ScreenManager screenManager;

    // Referencias a otras pantallas para obtener el modo
    private TwoPlayerModeScreen modeScreen;

    // Campos de entrada para jugadores
    private JTextField player1NameField;
    private JTextField player2NameField;
    private JComboBox<String> player2DifficultyBox;

    // Sabores seleccionados
    private String player1Flavor = "";
    private String player2Flavor = "";

    public TwoPlayerFlavorSelectionScreen(ScreenManager screenManager, TwoPlayerModeScreen modeScreen) {
        this.screenManager = screenManager;
        this.modeScreen = modeScreen;
        initializePanel();
    }

    private void initializePanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(770, 790));
        panel.setBackground(Color.BLACK);

        loadGifBackground();
    }

    private void loadGifBackground() {
        try {
            String path = "Images/Sabor2Helados.gif";
            ImageIcon gifIcon = new ImageIcon(path);

            if (gifIcon.getIconWidth() > 0) {
                System.out.println("GIF de seleccion de sabores 2 jugadores cargado desde: " + path);

                Image scaledImage = gifIcon.getImage().getScaledInstance(
                        756, 752, Image.SCALE_DEFAULT
                );
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                backgroundLabel = new JLabel(scaledIcon);
                backgroundLabel.setLayout(null);
                backgroundLabel.setPreferredSize(new Dimension(756, 752));

                panel.add(backgroundLabel, BorderLayout.CENTER);
            } else {
                System.out.println("No se pudo cargar el GIF de seleccion de sabores 2 jugadores");
            }
        } catch (Exception e) {
            System.out.println("Error cargando GIF de seleccion de sabores 2 jugadores: " + e.getMessage());
        }
    }

    private void createJ1VSJ2Fields() {
        if (backgroundLabel == null) {
            System.out.println("No se pueden crear campos sin fondo");
            return;
        }

        System.out.println("Creando campos de nombre para J1 VS J2");

        // Campo de nombre para jugador 1 (izquierda)
        player1NameField = new JTextField("Player 1");
        player1NameField.setBounds(115, 118, 200, 40);
        player1NameField.setFont(new Font("Arial", Font.BOLD, 18));
        player1NameField.setHorizontalAlignment(JTextField.CENTER);
        player1NameField.setForeground(new Color(139, 69, 19));
        player1NameField.setBackground(new Color(255, 248, 220));
        player1NameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(139, 69, 19), 3),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Comportamiento del placeholder jugador 1
        player1NameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (player1NameField.getText().equals("Player 1")) {
                    player1NameField.setText("");
                    player1NameField.setForeground(new Color(0, 0, 0));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (player1NameField.getText().trim().isEmpty()) {
                    player1NameField.setText("Player 1");
                    player1NameField.setForeground(new Color(139, 69, 19));
                }
            }
        });

        // Campo de nombre para jugador 2 (derecha)
        player2NameField = new JTextField("Player 2");
        player2NameField.setBounds(450, 118, 200, 40);
        player2NameField.setFont(new Font("Arial", Font.BOLD, 18));
        player2NameField.setHorizontalAlignment(JTextField.CENTER);
        player2NameField.setForeground(new Color(139, 69, 19));
        player2NameField.setBackground(new Color(255, 248, 220));
        player2NameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(139, 69, 19), 3),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Comportamiento del placeholder jugador 2
        player2NameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (player2NameField.getText().equals("Player 2")) {
                    player2NameField.setText("");
                    player2NameField.setForeground(new Color(0, 0, 0));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (player2NameField.getText().trim().isEmpty()) {
                    player2NameField.setText("Player 2");
                    player2NameField.setForeground(new Color(139, 69, 19));
                }
            }
        });

        backgroundLabel.add(player1NameField);
        backgroundLabel.add(player2NameField);

        System.out.println("Campos agregados al backgroundLabel");
    }

    private void createJ1VSIAFields() {
        if (backgroundLabel == null) {
            System.out.println("No se pueden crear campos sin fondo");
            return;
        }

        System.out.println("Creando campos para J1 VS IA");

        // Campo de nombre para jugador 1 (izquierda)
        player1NameField = new JTextField("Player 1");
        player1NameField.setBounds(115, 118, 200, 40);
        player1NameField.setFont(new Font("Arial", Font.BOLD, 18));
        player1NameField.setHorizontalAlignment(JTextField.CENTER);
        player1NameField.setForeground(new Color(139, 69, 19));
        player1NameField.setBackground(new Color(255, 248, 220));
        player1NameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(139, 69, 19), 3),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Comportamiento del placeholder jugador 1
        player1NameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (player1NameField.getText().equals("Player 1")) {
                    player1NameField.setText("");
                    player1NameField.setForeground(new Color(0, 0, 0));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (player1NameField.getText().trim().isEmpty()) {
                    player1NameField.setText("Player 1");
                    player1NameField.setForeground(new Color(139, 69, 19));
                }
            }
        });

        // Selector de dificultad para IA (derecha)
        String[] difficulties = {"hungry", "fearful", "expert"};
        player2DifficultyBox = new JComboBox<>(difficulties);
        player2DifficultyBox.setBounds(450, 118, 200, 40);
        player2DifficultyBox.setFont(new Font("Arial", Font.BOLD, 18));
        player2DifficultyBox.setForeground(new Color(139, 69, 19));
        player2DifficultyBox.setBackground(new Color(255, 248, 220));
        player2DifficultyBox.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 3));

        backgroundLabel.add(player1NameField);
        backgroundLabel.add(player2DifficultyBox);

        System.out.println("Campo de nombre y selector de IA agregados");
    }

    private void createButtons() {
        if (backgroundLabel == null) {
            System.out.println("No se pueden crear botones sin fondo");
            return;
        }

        // Botones de sabor para jugador 1 (izquierda)
        createPlayer1FlavorButtons();

        // Botones de sabor para jugador 2 (derecha)
        createPlayer2FlavorButtons();

        // Boton BACK
        JButton backButton = createInvisibleButton(300, 600, 155, 50);
        backButton.addActionListener(e -> {
            System.out.println("Volver a seleccion de modo 2 jugadores");
            screenManager.showScreen("twoPlayerMode");
        });
        backgroundLabel.add(backButton);
    }

    private void createPlayer1FlavorButtons() {
        // Boton marron jugador 1
        JButton brownButton = createInvisibleButton(123, 225, 45, 110);
        brownButton.addActionListener(e -> {
            player1Flavor = "brown";
            System.out.println("Jugador 1 selecciono sabor marron");
            checkIfBothSelected();
        });

        // Boton vainilla jugador 1
        JButton vanillaButton = createInvisibleButton(175, 230, 55, 105);
        vanillaButton.addActionListener(e -> {
            player1Flavor = "vanilla";
            System.out.println("Jugador 1 selecciono sabor vainilla");
            checkIfBothSelected();
        });

        // Boton rosa jugador 1
        JButton pinkButton = createInvisibleButton(235, 225, 45, 110);
        pinkButton.addActionListener(e -> {
            player1Flavor = "pink";
            System.out.println("Jugador 1 selecciono sabor rosa");
            checkIfBothSelected();
        });

        backgroundLabel.add(brownButton);
        backgroundLabel.add(vanillaButton);
        backgroundLabel.add(pinkButton);
    }

    private void createPlayer2FlavorButtons() {
        // Boton marron jugador 2
        JButton brownButton = createInvisibleButton(413, 225, 45, 110);
        brownButton.addActionListener(e -> {
            player2Flavor = "brown";
            System.out.println("Jugador 2 selecciono sabor marron");
            checkIfBothSelected();
        });

        // Boton vainilla jugador 2
        JButton vanillaButton = createInvisibleButton(465, 230, 55, 105);
        vanillaButton.addActionListener(e -> {
            player2Flavor = "vanilla";
            System.out.println("Jugador 2 selecciono sabor vainilla");
            checkIfBothSelected();
        });

        // Boton rosa jugador 2
        JButton pinkButton = createInvisibleButton(525, 225, 45, 110);
        pinkButton.addActionListener(e -> {
            player2Flavor = "pink";
            System.out.println("Jugador 2 selecciono sabor rosa");
            checkIfBothSelected();
        });

        backgroundLabel.add(brownButton);
        backgroundLabel.add(vanillaButton);
        backgroundLabel.add(pinkButton);
    }

    private void checkIfBothSelected() {
        if (!player1Flavor.isEmpty() && !player2Flavor.isEmpty()) {
            System.out.println("Ambos jugadores seleccionaron sabor, avanzando...");
            System.out.println("Jugador 1: " + getPlayer1Name() + " - " + player1Flavor);
            System.out.println("Jugador 2: " + getPlayer2Name() + " - " + player2Flavor);
            screenManager.showScreen("levelSelection");
        }
    }

    private JButton createInvisibleButton(int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    /**
     * Obtiene el nombre del jugador 1
     */
    private String getPlayer1Name() {
        if (player1NameField != null) {
            String name = player1NameField.getText().trim();
            return (name.isEmpty() || name.equals("Player 1")) ? "Player 1" : name;
        }
        return "Player 1";
    }

    /**
     * Obtiene el nombre del jugador 2 o dificultad de IA
     */
    private String getPlayer2Name() {
        if (player2NameField != null) {
            String name = player2NameField.getText().trim();
            return (name.isEmpty() || name.equals("Player 2")) ? "Player 2" : name;
        } else if (player2DifficultyBox != null) {
            return "IA-" + player2DifficultyBox.getSelectedItem();
        }
        return "Player 2";
    }

    /**
     * Obtiene el sabor del jugador 1
     */
    public String getPlayer1Flavor() {
        return player1Flavor;
    }

    /**
     * Obtiene el sabor del jugador 2
     */
    public String getPlayer2Flavor() {
        return player2Flavor;
    }

    /**
     * Obtiene el modo de juego actual
     */
    public String getGameMode() {
        return modeScreen.getSelectedMode();
    }

    /**
     * Obtiene la dificultad de la IA del jugador 2 (solo para J1VSIA e IAVSIA)
     */
    public String getPlayer2Difficulty() {
        if (player2DifficultyBox != null) {
            return (String) player2DifficultyBox.getSelectedItem();
        }
        return null;
    }

    /**
     * Obtiene el nombre real del jugador 1 (publico para otras pantallas)
     */
    public String getPlayer1NamePublic() {
        return getPlayer1Name();
    }

    /**
     * Obtiene el nombre/dificultad real del jugador 2 (publico para otras pantallas)
     */
    public String getPlayer2NamePublic() {
        return getPlayer2Name();
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void onShow() {
        System.out.println("Mostrando pantalla de seleccion de sabores 2 jugadores");
        String mode = modeScreen.getSelectedMode();
        System.out.println("Modo actual: " + mode);

        player1Flavor = "";
        player2Flavor = "";

        // Crear campos segun el modo
        if ("J1VSJ2".equals(mode)) {
            createJ1VSJ2Fields();
            createButtons();
        } else if ("J1VSIA".equals(mode)) {
            createJ1VSIAFields();
            createButtons();
        } else {
            System.out.println("Modo " + mode + " aun no implementado");
        }
    }

    @Override
    public void onHide() {
        System.out.println("Ocultando pantalla de seleccion de sabores 2 jugadores");

        // Limpiar campos al ocultar
        if (backgroundLabel != null) {
            backgroundLabel.removeAll();
        }
    }
}