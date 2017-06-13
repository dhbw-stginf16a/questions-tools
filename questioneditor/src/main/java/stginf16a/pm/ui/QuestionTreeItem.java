package stginf16a.pm.ui;

import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import stginf16a.pm.json.QuestionManager;
import stginf16a.pm.wrapper.QuestionWrapper;

import java.util.function.Consumer;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class QuestionTreeItem extends AbstractTreeItem{

    QuestionWrapper question;
    QuestionManager manager;

    public QuestionTreeItem(QuestionWrapper question, Consumer<QuestionWrapper> deleteQuestion, boolean newQuestion) {
        this.question= question;
        this.manager = manager;
        this.setValue(this.question);
        if(newQuestion){
            this.question.changed();
        }
        this.questionProperty().bind(this.question.questionProperty());
        this.statusProperty().bind(this.question.statusProperty().asString());
        this.typeProperty().bind(this.question.typeProperty().asString());

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(event -> {
            deleteQuestion.accept(this.question);
        });
        MenuItem duplicateMenuItem = new MenuItem("Duplicate");
        duplicateMenuItem.setOnAction(event -> {
            TreeItem<?> treeItem = this.getParent();
            if (treeItem instanceof CategoryTreeItem) {
                ((CategoryTreeItem) treeItem).addCopyQuestion(this.question);
            }
        });
        this.menu.getItems().addAll(deleteMenuItem, duplicateMenuItem);
    }

}
