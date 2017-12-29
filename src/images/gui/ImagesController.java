/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package images.gui;

import images.ThreadManager;
import images.Url;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author olda9
 */
public class ImagesController implements Initializable {

    private ThreadManager tManager;

    @FXML
    private TextField url;
    @FXML
    private CheckBox stayOnDomain;
    @FXML
    private Spinner<Integer> maxLevel;
    @FXML
    private ListView<?> listPictures;
    @FXML
    private ListView<?> listURL;
    @FXML
    private Spinner<Integer> maxThreads;
    @FXML
    private ListView<?> listChecked;
    @FXML
    private ListView<?> listError;
    @FXML
    private ProgressBar statusBar;
    @FXML
    private Label statusLabel;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        maxLevel.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0));
        maxThreads.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 4));
        tManager = null;
        //Platform.setImplicitExit(false);
        //listPictures.setse
    }

    @FXML
    private void addAndSearch(ActionEvent event) {
        if (tManager != null && tManager.isAlive()) {
            tManager.stopNow();
        }

        tManager = new ThreadManager(new Url(url.getText(), 0));
        tManager.setMaxLevel(maxLevel.getValue());
        tManager.setMaxThreads(1);
        tManager.setStayOnDomain(stayOnDomain.isSelected());
        //tManager.setName("JavaFX Application Thread");
        //tManager.start();
        //tManager.setDaemon(true);
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //         tManager.start();
                Platform.runLater(tManager);
                return null;
            }
        };
        new Thread(task).start();
        // Platform.runLater(tManager);
        listPictures.setItems((ObservableList) tManager.getIMAGES());
        listURL.setItems((ObservableList) tManager.getLINKS());

    }

    @FXML
    private void justAdd(ActionEvent event) {
    }

    @FXML
    private void addALot(ActionEvent event) {
    }

    @FXML
    private void searchFromQueue(ActionEvent event) {
    }

    @FXML
    private void download(ActionEvent event) throws MalformedURLException, FileNotFoundException, IOException {
        System.out.println("download");
        String folderPath = "./img/";
        String folder = null;
        int c=0;
        for (Url url2 : tManager.getIMAGES()) {
            String src = url2.getUrl();
            System.out.println("download:" + ++c);

            //Exctract the name of the image from the src attribute
            int indexname = src.lastIndexOf("/");

            if (indexname == src.length()) {
                src = src.substring(1, indexname);
            }

            indexname = src.lastIndexOf("/");
            String name = src.substring(indexname, src.length());

            System.out.println(name);

            //Open a URL Stream
            URL url = new URL(src);
            InputStream in = url.openStream();

            OutputStream out = new BufferedOutputStream(new FileOutputStream(folderPath + name));

            for (int b; (b = in.read()) != -1;) {
                out.write(b);
            }
            out.close();
            in.close();

        }
    }

}
