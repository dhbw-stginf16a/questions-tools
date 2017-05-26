package stginf16a.pm.ui;

import javafx.scene.control.MenuItem;
import stginf16a.pm.wrapper.QuestionWrapper;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class QuestionTreeItem extends AbstractTreeItem{

    QuestionWrapper question;

    public QuestionTreeItem(QuestionWrapper question, boolean newQuestion){
        this.question= question;
        this.setValue(this.question);
        if(newQuestion){
            this.question.changed();
        }
        this.questionProperty().bind(this.question.questionProperty());
        this.statusProperty().bind(this.question.statusProperty().asString());
        this.typeProperty().bind(this.question.typeProperty().asString());

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setDisable(true);//TODO: Implement functionality
        this.menu.getItems().add(deleteMenuItem);
    }

}
