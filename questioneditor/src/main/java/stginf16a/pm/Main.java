package stginf16a.pm;

import javafx.application.Application;
import javafx.stage.Stage;
import stginf16a.pm.util.QuestionEditor;
import stginf16a.pm.view.MainFrame;

import java.io.File;

public class Main extends Application {

    public static void main(String[] args) {
        QuestionEditor.init();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        File file = null;
        if(getParameters().getRaw().size()>0) {
            file = new File(getParameters().getRaw().get(getParameters().getRaw().size()-1));
            if (!file.exists()) {
                file = null;
            }
        }
        MainFrame mainFrame = new MainFrame(primaryStage, file);
        mainFrame.show();
    }
}
