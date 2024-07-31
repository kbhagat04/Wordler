import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LogController {
    @FXML
    private Label guess1;
    @FXML
    private Label guess2;
    @FXML
    private Label guess3;
    @FXML
    private Label guess4;
    @FXML
    private Label guess5;
    @FXML
    private Label guess6;
    @FXML
    private Label status;
    int row = 0;

    public void updateLog(List<String> guesses, boolean won, boolean over) {
        Label[] guessLabels = {guess1, guess2, guess3, guess4, guess5, guess6};
        for(Label guess:guessLabels) {
            guess.setText("");
        }
        for(int i =0; i< guesses.size(); i++) {
            guessLabels[i].setText(guesses.get(i));
        }
        if (over) {
            if (won) {
                status.setText("Won");
            }
            else {
                status.setText("Lost");
            }
        } else {
            status.setText("In progress");
        }
    }
}