import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatsController {
    @FXML
    private Label totalGamesLabel;
    @FXML
    private Label winpercentLabel;
    @FXML
    private Label cstreakLabel;
    @FXML
    private Label mstreakLabel;
    private int totalGames;
    private int totalWins;
    private int currentStreak;
    private int maxStreak;
    private final String statsFile = "stats.txt";
    public void initialize() {
        readStats();
        
    }

    private void readStats() {
        try (Scanner scanner = new Scanner(new File(statsFile))) {
            scanner.nextLine();
            if (scanner.hasNextInt()) {
                totalGames = scanner.nextInt();
            }
            if (scanner.hasNextInt()) {
                totalWins = scanner.nextInt();
            }
            if (scanner.hasNextInt()) {
                currentStreak = scanner.nextInt();
            }
            if (scanner.hasNextInt()) {
                maxStreak = scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
        }
    }

    public void updateStats(boolean gameWon, boolean pressed, List<String> guesses) {
        if (!pressed) {
            totalGames++;
            if (gameWon) {
                totalWins++;
                currentStreak++;
                if (currentStreak > maxStreak) {
                    maxStreak = currentStreak;
                }
            } else {
                currentStreak = 0;
            }
        }
        
        totalGamesLabel.setText(Integer.toString(totalGames));
        winpercentLabel.setText(String.format("%.2f", getWinPercentage()));
        cstreakLabel.setText(Integer.toString(currentStreak));
        mstreakLabel.setText(Integer.toString(maxStreak));
        
        try (PrintWriter writer = new PrintWriter(new File(statsFile))) {
            writer.println("Total Stats:");
            writer.println(totalGames);
            writer.println(totalWins);
            writer.println(currentStreak);
            writer.println(maxStreak);
            writer.println();
            writer.println("Previous guesses:");
            for (String guess : guesses) {
                writer.println(guess);
            }
        } catch (FileNotFoundException e) {
        }
        
    }

    public double getWinPercentage() {
        if (totalGames == 0) {
            return 0;
        }
        return (double) totalWins / totalGames * 100;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public void resetStats() {
        totalGames = 0;
        totalWins = 0;
        currentStreak = 0;
        maxStreak = 0;
    }
}