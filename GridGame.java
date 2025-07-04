import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GridGame extends JFrame
{
    private int[][] grid = new int[4][4];
    private JLabel[][] labels = new JLabel[4][4];
    private int targetSum;
    private JLabel targetLabel;
    private JLabel currentSumLabel;
    private JButton newGameButton;
    private JPanel fireworksPanel;

    public GridGame()
    {
        setTitle("Grid Sum Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(4, 4));
        JPanel topButtons = new JPanel(new GridLayout(1, 4));
        JPanel leftButtons = new JPanel(new GridLayout(4, 1));
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Init labels
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                labels[i][j] = new JLabel("0", SwingConstants.CENTER);
                labels[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                labels[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                centerPanel.add(labels[i][j]);
            }
        }

        // Column buttons
        for (int col = 0; col < 4; col++)
        {
            final int c = col;
            JButton btn = new JButton("↓");
            btn.addActionListener(e -> {
                for (int i = 0; i < 4; i++)
                {
                    grid[i][c] = nextValue(grid[i][c]);
                }
                updateGrid();
                checkWin();
            });
            topButtons.add(btn);
        }

        // Row buttons
        for (int row = 0; row < 4; row++)
        {
            final int r = row;
            JButton btn = new JButton("→");
            btn.addActionListener(e -> {
                for (int j = 0; j < 4; j++)
                {
                    grid[r][j] = nextValue(grid[r][j]);
                }
                updateGrid();
                checkWin();
            });
            leftButtons.add(btn);
        }

        // Bottom label + New Game button
        currentSumLabel = new JLabel("Current Sum: 0", SwingConstants.CENTER);
        currentSumLabel.setFont(new Font("Arial", Font.BOLD, 20));

        targetLabel = new JLabel("", SwingConstants.CENTER);
        targetLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel sumPanel = new JPanel(new GridLayout(1, 2));
        sumPanel.add(currentSumLabel);
        sumPanel.add(targetLabel);

        newGameButton = new JButton("New Game");
        newGameButton.setVisible(false);
        newGameButton.addActionListener(e -> newGame());

        fireworksPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if (targetSum == getSum())
                {
                    g.setColor(Color.RED);
                    for (int i = 0; i < 20; i++)
                    {
                        int x = (int)(Math.random() * getWidth());
                        int y = (int)(Math.random() * getHeight());
                        g.fillOval(x, y, 5, 5);
                    }
                }
            }
        };
        fireworksPanel.setPreferredSize(new Dimension(100, 100));
        bottomPanel.add(sumPanel, BorderLayout.NORTH);
        bottomPanel.add(newGameButton, BorderLayout.CENTER);
        bottomPanel.add(fireworksPanel, BorderLayout.SOUTH);

        add(topButtons, BorderLayout.NORTH);
        add(leftButtons, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        newGame();
        setVisible(true);

    }

    private int nextValue(int current)
    {
        return (current + 2) % 3 - 1; // -1 → 0 → 1 → -1
    }

    private void updateGrid()
    {
        int sum = 0;
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                labels[i][j].setText(String.valueOf(grid[i][j]));
                sum += grid[i][j];
            }
        }
        currentSumLabel.setText("Current Sum: " + sum);
    }

    private int getSum()
    {
        int sum = 0;
        for (int[] row : grid)
        {
            for (int val : row)
            {
                sum += val;
            }
        }
        return sum;
    }

    private void checkWin()
    {
        if (getSum() == targetSum)
        {
            newGameButton.setVisible(true);
            repaint(); // triggers fireworks
        }
    }

    private void newGame()
    {
        Random rand = new Random();
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                grid[i][j] = 0;
            }
        }
        targetSum = rand.nextInt(15) + 1; // 1 to 16
        targetLabel.setText("Target Sum: " + targetSum);
        updateGrid();
        newGameButton.setVisible(false);
        repaint();
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(GridGame::new);
    }
}
