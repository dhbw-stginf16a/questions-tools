package stginf16a.pm.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import stginf16a.pm.util.QuestionEditor;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class ExceptionAlert extends Alert{

    public ExceptionAlert(Exception exception) {
        super(AlertType.ERROR);
        Exception exception1 = exception;

        this.setTitle(QuestionEditor.getLangString("errorDialog.title"));
        this.setHeaderText(exception.getClass().getSimpleName());
        this.setContentText(exception.getLocalizedMessage());

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception1.printStackTrace(pw);

        TextArea textArea = new TextArea(sw.toString());
        textArea.setEditable(false);
        textArea.setWrapText(false);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        this.getDialogPane().setExpandableContent(expContent);
    }
}
