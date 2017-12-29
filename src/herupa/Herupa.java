/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herupa;

import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author olda9
 */
public class Herupa extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.setMinHeight(540);
        primaryStage.setMinWidth(920);
        primaryStage.setTitle("Herupa");
        Image icon = new Image(Herupa.class.getResourceAsStream("/data/helper.png"));
        primaryStage.getIcons().add(icon);

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
  
}
