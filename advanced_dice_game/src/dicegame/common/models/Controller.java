package dicegame.common.models;

import dicegame.GameProperties;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {

    protected GameProperties properties;
    protected Logger logger;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    /**
     * Rolls the dice
     */
    public void rollDice() {}

    /**
     * Starts a game
     */
    public void startGame() {}

    /**
     * Updates the Log List View.
     */
    protected void updateLog(ListView listView, String newLog)
    {
        try {
            listView.getItems().clear();

            logger.addToLog(newLog);
            for (String log: this.logger.getLog()) {
                listView.getItems().add(log);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
