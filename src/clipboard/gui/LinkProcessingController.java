/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clipboard.gui;

import clipboard.Record;
import global.database.Databaze;
import gui.ClipBoardListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;

/**
 * FXML Controller class
 *
 * @author olda9
 */
public class LinkProcessingController implements Initializable {

    private Databaze db;
    ObservableList<Record> data;
    ClipBoardListener clipboard;

    @FXML
    private ListView<Record> list;
    @FXML
    private ToggleButton watchClipboard;
    @FXML
    private Label status;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = Databaze.getInstance();
        List vratVsechny = db.vratVsechny();
        data = FXCollections.synchronizedObservableList(FXCollections.observableList(vratVsechny));
        clipboard = new ClipBoardListener();

        clipboard.setData(data);
        list.setItems(data);

        status.textProperty().bind(Bindings.size(data).asString());
    }

    @FXML
    private void watchClipboard(ActionEvent event) {
        boolean selected = watchClipboard.isSelected();
        if (selected) {
            watchClipboard.setText("Stop Watching");
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    clipboard.run();
//                }
//            });
            clipboard.setDaemon(false);
            clipboard.start();

        } else {
            watchClipboard.setText("Watch clipboard");
            //clipboard.stop();
            clipboard.setPleseDie(true);
        }
    }

    @FXML
    private void saveToDb(ActionEvent event) {
        for (Object ob : data.toArray()) {
            db.uloz(ob);
        }
    }

    @FXML
    private void deleteSelected(ActionEvent event) {
        Record selected = list.getSelectionModel().getSelectedItem();
        data.remove(selected);
        db.smaz(selected);

    }

}
