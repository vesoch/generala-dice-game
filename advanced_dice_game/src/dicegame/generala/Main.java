package dicegame.generala;

import dicegame.Helper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage)
    {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("generala.fxml"));
            primaryStage.setTitle("Generala");
            primaryStage.getIcons().add(Helper.getImage("dice_icon", "png"));
            primaryStage.setScene(new Scene(root, 1100, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
