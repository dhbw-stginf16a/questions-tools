package stginf16a.pm.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;
import stginf16a.pm.questions.QuestionDifficulty;
import stginf16a.pm.questions.QuestionStatus;
import stginf16a.pm.questions.QuestionType;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public abstract class AbstractTreeItem extends TreeItem<Object>{
    ContextMenu menu;
    private StringProperty question;
    private ObjectProperty<QuestionStatus> status;
    private ObjectProperty<QuestionType> type;
    private ObjectProperty<QuestionDifficulty> difficulty;

    AbstractTreeItem(){
        this.question = new SimpleStringProperty();
        this.status = new SimpleObjectProperty<>();
        this.type = new SimpleObjectProperty<>();
        this.difficulty = new SimpleObjectProperty<>();
        menu = new ContextMenu();
    }

    public String getQuestion() {
        return question.get();
    }

    public StringProperty questionProperty() {
        return question;
    }

    public QuestionStatus getStatus() {
        return status.get();
    }

    public ObjectProperty<QuestionStatus> statusProperty() {
        return status;
    }

    public QuestionType getType() {
        return type.get();
    }

    public ObjectProperty<QuestionType> typeProperty() {
        return type;
    }

    public QuestionDifficulty getDifficulty() {
        return difficulty.get();
    }

    public ObjectProperty<QuestionDifficulty> difficultyProperty() {
        return difficulty;
    }

    public ContextMenu getMenu() {
        return menu;
    }
}
