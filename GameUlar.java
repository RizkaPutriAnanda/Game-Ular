import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GameUlar {

    private JFrame frame;
    private GamePanel gamePanel;
    private Timer timer;
    private int snakeSize;
    private int[] snakeX, snakeY;
    private int foodX, foodY;
    private boolean isGameOver;
    private Direction direction;

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    public GameUlar() {
        frame = new JFrame("Game Ular");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        showMainMenu();
    }

    private void showMainMenu() {
        JPanel mainMenuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Tambahkan gambar latar belakang di sini
                ImageIcon background = new ImageIcon("C:\\Users\\ASUS\\Documents\\game.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Selamat datang di Game Ular!");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);  // Tambahkan warna teks
        mainMenuPanel.add(titleLabel);

        JButton startButton = new JButton("Mulai Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setBackground(new Color(0, 128, 0));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                frame.revalidate();
                frame.repaint();
                initializeGame();
            }
        });
        mainMenuPanel.add(startButton);

        JButton exitButton = new JButton("Keluar");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setBackground(new Color(178, 34, 34));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        mainMenuPanel.add(exitButton);

        frame.add(mainMenuPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void initializeGame() {
        gamePanel = new GamePanel();
        gamePanel.setBackground(Color.BLACK);
        frame.add(gamePanel, BorderLayout.CENTER);

        snakeSize = 3;
        snakeX = new int[300];
        snakeY = new int[300];
        direction = Direction.RIGHT;

        frame.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != Direction.DOWN)
                            direction = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != Direction.UP)
                            direction = Direction.DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != Direction.RIGHT)
                            direction = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != Direction.LEFT)
                            direction = Direction.RIGHT;
                        break;
                }
            }

            public void keyReleased(KeyEvent e) {}
        });

        frame.setFocusable(true);
        frame.revalidate();
        frame.repaint();
        startGame();
    }

    private void startGame() {
        isGameOver = false;
        snakeSize = 3;
        for (int i = 0; i < snakeSize; i++) {
            snakeX[i] = 50 - i * 10;
            snakeY[i] = 50;
        }

        spawnFood();

        timer = new Timer(150, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    moveSnake();
                    checkCollision();
                    checkFood();
                    gamePanel.repaint();
                }
            }
        });

        timer.start();
    }

    private void moveSnake() {
        for (int i = snakeSize - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case UP:
                snakeY[0] -= 10;
                break;
            case DOWN:
                snakeY[0] += 10;
                break;
            case LEFT:
                snakeX[0] -= 10;
                break;
            case RIGHT:
                snakeX[0] += 10;
                break;
        }
    }

    private void checkCollision() {
        if (snakeX[0] < 0 || snakeX[0] >= 300 || snakeY[0] < 0 || snakeY[0] >= 300) {
            gameOver();
        }

        for (int i = 1; i < snakeSize; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                gameOver();
            }
        }
    }

    private void checkFood() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeSize++;
            spawnFood();
        }
    }

    private void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(29) * 10;
        foodY = random.nextInt(29) * 10;
    }

    private void gameOver() {
        isGameOver = true;
        timer.stop();
        JOptionPane.showMessageDialog(null, "Game Over! Skor Anda: " + (snakeSize - 3));
        resetGame();
    }

    private void resetGame() {
        int choice = JOptionPane.showConfirmDialog(null, "Apakah Anda ingin bermain lagi?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            frame.getContentPane().removeAll();
            frame.revalidate();
            frame.repaint();
            showMainMenu();
        } else {
            System.exit(0);
        }
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (int i = 0; i < snakeSize; i++) {
            g.fillRect(snakeX[i], snakeY[i], 10, 10);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(foodX, foodY, 10, 10);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameUlar());
    }

    class GamePanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawSnake(g);
            drawFood(g);
        }
    }
}