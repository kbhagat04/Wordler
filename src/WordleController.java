import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class WordleController {
    @FXML
    private TextField guessField;
    @FXML
    private String answerWord;
    @FXML
    private TilePane tilePane;
    @FXML
    private FlowPane keyboard;
    @FXML
    private Label answer;
    @FXML
    private Button reset;
    @FXML
    private Button stats;
    private List<String> guesses = new ArrayList<>();
    private List<String> currentGuesses = new ArrayList<>();
    private Map<String, Button> letterButtons = new HashMap<>();
    private boolean won = false;
    boolean gameOver = false;
    int row = 0;
    public void initialize() {
        System.out.println();
        System.out.println("Game started");
        currentGuesses.clear();
        Image resetImage = new Image(getClass().getResourceAsStream("/reset.png"));
        Image statsImage = new Image(getClass().getResourceAsStream("/stats.png"));
        ImageView resetImageView = new ImageView(resetImage);
        ImageView statsImageView = new ImageView(statsImage);
        statsImageView.setFitWidth(25);
        statsImageView.setFitHeight(25);
        resetImageView.setFitWidth(25);
        resetImageView.setFitHeight(25); 
        reset.setGraphic(resetImageView);
        stats.setGraphic(statsImageView);
        gameOver= false;
        won = false;
        for (Node node : keyboard.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                letterButtons.put(button.getText(), button);
            }
        }
        guessField.textProperty().addListener((observable, oldValue, newValue) -> {
            guessField.setText(newValue.toUpperCase());
            if (newValue.length() > 5) {
                answer.setVisible(true);
                answer.setText("Too many letters!");
                for (int i = row * 5; i < (row * 5) + 5; i++) {
                    Node node = tilePane.getChildren().get(i);
                    if (node instanceof Label) {
                        Label label = (Label) node;
            
                        TranslateTransition tt1 = new TranslateTransition(Duration.millis(40), label);
                        tt1.setFromX(0f);
                        tt1.setToX(-4f);
                        tt1.setCycleCount(1);
                        tt1.setAutoReverse(false);

                        TranslateTransition tt2 = new TranslateTransition(Duration.millis(40), label);
                        tt2.setFromX(-4f);
                        tt2.setToX(0f);
                        tt2.setCycleCount(1);
                        tt2.setAutoReverse(false);

                        SequentialTransition st = new SequentialTransition(tt1, tt2);
                        st.setCycleCount(3);
                        st.setAutoReverse(true);

                        st.playFromStart();
                    }
                }
                FadeTransition fadeIn = new FadeTransition(Duration.millis(200), answer);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.setCycleCount(1);

                PauseTransition stayOn = new PauseTransition(Duration.seconds(2.5));

                FadeTransition fadeOut = new FadeTransition(Duration.millis(200), answer);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setCycleCount(1);

                SequentialTransition st = new SequentialTransition(fadeIn, stayOn, fadeOut);
                st.play();
                guessField.setText(newValue.substring(0, 5)); //truncate to 5 characters
            }  else {
                if (!newValue.isEmpty() && newValue.length() > oldValue.length() && gameOver == false) {
                    int lastIndex = newValue.length() - 1;
                    char guessChar = newValue.charAt(lastIndex);
                    int labelIndex2 = row * 5 + lastIndex;
                    Label label = (Label) tilePane.getChildren().get(labelIndex2);
                    label.setText(String.valueOf(guessChar));
                    //animation
                    ScaleTransition growShrink = new ScaleTransition(Duration.millis(100), label);
                    growShrink.setByX(0.1);
                    growShrink.setByY(0.1);
                    growShrink.setCycleCount(2); 
                    growShrink.setAutoReverse(true);
                    growShrink.play();
                } 
            } 
        
    });
        guessField.requestFocus();
        answerWord = pickRandomWord();
        System.out.println(answerWord);
        answer.setText(answerWord);
        answer.setVisible(false);
        guessField.setOnKeyPressed(this::handleKeyPressed);
    }
    
    private String pickRandomWord() {
        List<String> words = new ArrayList<>();
        try (Scanner scanner = new Scanner(getClass().getResourceAsStream("/words.txt"))) { 
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                words.add(line);
            }
            scanner.close();
        } catch (Exception e){
            System.out.println("Error: Words file not found!");
            e.printStackTrace();
        }
        int randomIndex = new Random().nextInt(words.size());
        return words.get(randomIndex).toUpperCase();
    }
    
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if(gameOver){
            event.consume();
            return;
        }
        if (event.getCode() == KeyCode.ENTER) {
            if(guessField.getText().length() != 5) {
                for (int i = row * 5; i < (row * 5) + 5; i++) {
                    Node node = tilePane.getChildren().get(i);
                    if (node instanceof Label) {
                        Label label = (Label) node;
            
                        TranslateTransition tt1 = new TranslateTransition(Duration.millis(40), label);
                        tt1.setFromX(0f);
                        tt1.setToX(-4f);
                        tt1.setCycleCount(1);
                        tt1.setAutoReverse(false);

                        TranslateTransition tt2 = new TranslateTransition(Duration.millis(40), label);
                        tt2.setFromX(-4f);
                        tt2.setToX(0f);
                        tt2.setCycleCount(1);
                        tt2.setAutoReverse(false);

                        SequentialTransition st = new SequentialTransition(tt1, tt2);
                        st.setCycleCount(3);
                        st.setAutoReverse(true);

                        st.playFromStart();
                    }
                }
                
                answer.setText("Not enough letters!");
                answer.setMaxWidth(Double.MAX_VALUE);
                answer.setVisible(true);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(200), answer);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.setCycleCount(1);

                PauseTransition stayOn = new PauseTransition(Duration.seconds(2.5));

                FadeTransition fadeOut = new FadeTransition(Duration.millis(200), answer);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setCycleCount(1);

                SequentialTransition st = new SequentialTransition(fadeIn, stayOn, fadeOut);
                st.play();
                event.consume();
                return;
            }
            checkGuess(guessField.getText());
            row++;
            guessField.clear();
        }else if (event.getCode() == KeyCode.BACK_SPACE || (event.getText().length() < 5 && event.getText().matches("[a-zA-Z]"))) {
            Platform.runLater(() -> updateWordleLabels(guessField.getText()));
            
          }
    }

    public void updateWordleLabels(String currentText) {   
        int startIndex = row * 5;
        for (int i = 0; i < 5; i++) {
            int labelIndex = startIndex + i;
            if (i < currentText.length()) {
                char currentChar = currentText.charAt(i);
                Label label = (Label) tilePane.getChildren().get(labelIndex);
                label.setText(String.valueOf(currentChar));
            } else {
                ((Label) tilePane.getChildren().get(labelIndex)).setText("");
            }
        }
      }
      
    @FXML
    private void handleButtonClicked(ActionEvent event) {
        if (won) {
            return;
        }
        Button button = (Button) event.getSource();
        String currentText = guessField.getText();
        if (currentText.length() < 5) {
            guessField.setText(currentText + button.getText());
            Platform.runLater(() -> updateWordleLabels(guessField.getText()));
        } else {
            answer.setVisible(true);
            answer.setText("Too many letters!");
            for (int i = row * 5; i < (row * 5) + 5; i++) {
                Node node = tilePane.getChildren().get(i);
                if (node instanceof Label) {
                    Label label = (Label) node;
        
                    TranslateTransition tt1 = new TranslateTransition(Duration.millis(40), label);
                    tt1.setFromX(0f);
                    tt1.setToX(-4f);
                    tt1.setCycleCount(1);
                    tt1.setAutoReverse(false);

                    TranslateTransition tt2 = new TranslateTransition(Duration.millis(40), label);
                    tt2.setFromX(-4f);
                    tt2.setToX(0f);
                    tt2.setCycleCount(1);
                    tt2.setAutoReverse(false);

                    SequentialTransition st = new SequentialTransition(tt1, tt2);
                    st.setCycleCount(3);
                    st.setAutoReverse(true);

                    st.playFromStart();
                }
            }
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), answer);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.setCycleCount(1);

            PauseTransition stayOn = new PauseTransition(Duration.seconds(2.5));

            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), answer);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setCycleCount(1);

            SequentialTransition st = new SequentialTransition(fadeIn, stayOn, fadeOut);
            st.play();
        }
    }

    @FXML
    private void handleEnterClicked(ActionEvent event) {
        if (won) {
            return;
        }
        String guess = guessField.getText();
        if (guess.length() == 5) {
            checkGuess(guess);
            row++;
            guessField.clear(); 
        } else {
            for (int i = row * 5; i < (row * 5) + 5; i++) {
                Node node = tilePane.getChildren().get(i);
                if (node instanceof Label) {
                    Label label = (Label) node;
        
                    TranslateTransition tt1 = new TranslateTransition(Duration.millis(40), label);
                    tt1.setFromX(0f);
                    tt1.setToX(-4f);
                    tt1.setCycleCount(1);
                    tt1.setAutoReverse(false);

                    TranslateTransition tt2 = new TranslateTransition(Duration.millis(40), label);
                    tt2.setFromX(-4f);
                    tt2.setToX(0f);
                    tt2.setCycleCount(1);
                    tt2.setAutoReverse(false);

                    SequentialTransition st = new SequentialTransition(tt1, tt2);
                    st.setCycleCount(3);
                    st.setAutoReverse(true);

                    st.playFromStart();
                }
            }
            
                answer.setText("Not enough letters!");
                answer.setMaxWidth(Double.MAX_VALUE);
                answer.setVisible(true);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(200), answer);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.setCycleCount(1);

                PauseTransition stayOn = new PauseTransition(Duration.seconds(3));

                FadeTransition fadeOut = new FadeTransition(Duration.millis(200), answer);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setCycleCount(1);

                SequentialTransition st = new SequentialTransition(fadeIn, stayOn, fadeOut);
                st.play();
                event.consume();
        }
    }

    @FXML
    private void handleDeleteClicked(ActionEvent event) {
        if (won) {
            return;
        }
        String currentText = guessField.getText();
        if (!currentText.isEmpty()) {
            guessField.setText(currentText.substring(0, currentText.length() - 1));
            Platform.runLater(() -> updateWordleLabels(guessField.getText()));
        }
    }

    private void checkGuess(String guess) {
        currentGuesses.add(guess);
        char[] answerChars = answerWord.toCharArray();
        guesses.add(guess); 
        if (guess.length() != 5) {
            
            return;
        }
        int labelIndex = 0;
        for (int i = 0; i < 5; i++) {
            char guessChar = guess.charAt(i);
            Button button = letterButtons.get(String.valueOf(guessChar));
            char answerChar = answerWord.charAt(i);
            labelIndex = row * 5 + i;
            Label label = (Label) tilePane.getChildren().get(labelIndex);
    
            // green for match
            if (guessChar == answerChar) {
                label.setStyle("-fx-border-color: grey; -fx-background-color: #598c52; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
                button.setStyle("-fx-background-color: #598c52; -fx-text-fill: white; -fx-font-weight: bold;");
            } else {
                boolean existsInAnswer = false;
                for (char c : answerChars) {
                    if (guessChar == c) {
                        existsInAnswer = true;
                        break;
                    }
                }
                // yellow for letter existing but in wrong position
                if (existsInAnswer) {
                    label.setStyle("-fx-border-color: grey; -fx-background-color: #b49f47; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
                    button.setStyle("-fx-background-color: #b49f47; -fx-text-fill: white; -fx-font-weight: bold;");
                } else {
                    // grey for wrong letter
                    label.setStyle("-fx-border-color: grey; -fx-background-color: #3a3a3c; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
                    button.disableProperty().set(true);
                    button.setStyle("-fx-background-color: #3a3a3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-opacity: 1;");
                }
            }
            
        }
        if (guess.equals(answerWord)) {
            won = true;
            gameOver = true;
            answer.setMaxWidth(Region.USE_PREF_SIZE);
            answer.setVisible(true);
            answer.setText("Congratulations, you win!");
            FadeTransition ft = new FadeTransition(Duration.millis(500), answer);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.play();
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> displayStats());
            pause.play();
            return;
        } else if (labelIndex == 29){
            gameOver = true;
            System.out.println("Game Over");
            if(won == false){
                answer.setMaxWidth(Region.USE_PREF_SIZE);
                answer.setVisible(true);
                answer.setText(answerWord);
                FadeTransition ft = new FadeTransition(Duration.millis(500), answer);
                ft.setFromValue(0.0);
                ft.setToValue(1.0);
                ft.play();
                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(event -> displayStats());
                pause.play();
                return; 
            }
        }    
    }

    @FXML
    private void handleResetClicked(ActionEvent event) {
        
        guessField.clear();
        currentGuesses.clear();
        row = 0;
        won = false;
        for (Button button : letterButtons.values()) {
            button.disableProperty().set(false);
            button.setStyle("-fx-background-color: rgb(211, 215, 217); -fx-text-fill: black; -fx-font-weight: bold;");
        }
        for (Node node : tilePane.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                label.setText("");
                label.setStyle("-fx-border-color: grey; -fx-background-color: white; -fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");
            }
        }
       initialize();
    }

    @FXML
    private void handleStatsClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Stats.fxml"));
            Parent statsRoot = loader.load();
            StatsController statsController = loader.getController();
            statsController.updateStats(won, true, currentGuesses);
            Stage statsStage = new Stage();
            statsStage.setScene(new Scene(statsRoot));
            statsStage.setTitle("Stats");
            statsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Log.fxml"));
            Parent logRoot = loader.load();
            LogController logController = loader.getController();
            logController.updateLog(currentGuesses, won, gameOver);
            Stage logStage = new Stage();
            logStage.setScene(new Scene(logRoot));
            logStage.setTitle("Log");
            logStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayStats() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Stats.fxml"));
            Parent statsRoot = loader.load();
            StatsController statsController = loader.getController();
            statsController.updateStats(won, false, guesses);
            Stage statsStage = new Stage();
            statsStage.setScene(new Scene(statsRoot));
            statsStage.setTitle("Stats");
            statsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}