package stginf16a.pm.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;
import stginf16a.pm.Main;
import stginf16a.pm.controller.MainController;
import stginf16a.pm.util.QuestionEditor;

import java.io.File;
import java.io.IOException;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class MainFrame {
    private Stage primaryStage;

    public MainFrame(Stage primaryStage, File file){
        this.primaryStage = primaryStage;
        try {
            Scene mainScene;
            FXMLLoader loader = QuestionEditor.getLoader();
            mainScene = new Scene(loader.load(this.getClass().getResource("mainFrame.fxml").openStream()), 900, 600,
                    true, SceneAntialiasing.BALANCED);
            String css = Main.class.getResource("style/default.css").toExternalForm();
            MainController controller = loader.getController();
            mainScene.getStylesheets().add(css);
            primaryStage.setTitle(QuestionEditor.getLangString("application.name"));
            primaryStage.setScene(mainScene);
            primaryStage.setMinHeight(600.0);
            primaryStage.setMinWidth(900.0);
            controller.setStage(primaryStage);
            controller.loadProject(file);
        } catch (IOException e) {
            QuestionEditor.handleException(e);
        }
    }

    public void show() {
        primaryStage.show();
    }

}
