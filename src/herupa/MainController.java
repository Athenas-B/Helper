/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herupa;

import global.database.Databaze;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * FXML Controller class
 *
 * @author olda9
 */
public class MainController implements Initializable {

    @FXML
    private Pane content;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                boolean close = Databaze.closeInstance();
                if (close) {
                    System.out.println("otevřená db uzavřena");
                }
                System.out.println("konec");
            }
        });
        linkProcessing(new ActionEvent());
    }

    private void changeContent(String resource) {
        try {
            content.getChildren().clear();
            Region region = (Region) FXMLLoader.load(getClass().getResource(resource));
            content.getChildren().setAll(region);
            region.prefWidthProperty().bind(content.widthProperty());
            region.prefHeightProperty().bind(content.heightProperty());
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void close(ActionEvent event) {
        Platform.exit();
        System.exit(0);

    }

    @FXML
    private void searchPictures(ActionEvent event) {
        changeContent("/images/gui/Images.fxml");
    }

    @FXML
    private void linkProcessing(ActionEvent event) {
        changeContent("/clipboard/gui/LinkProcessing.fxml");
    }

    @FXML
    private void about(ActionEvent event) {
    }

}
