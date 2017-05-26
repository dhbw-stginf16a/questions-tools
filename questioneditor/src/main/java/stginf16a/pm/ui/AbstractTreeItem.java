package stginf16a.pm.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TreeItem;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public abstract class AbstractTreeItem extends TreeItem<Object>{
    private StringProperty question;
    private StringProperty status;
    private StringProperty type;

    ContextMenu menu;

    AbstractTreeItem(){
        this.question = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        menu = new ContextMenu();
    }

    public String getQuestion() {
        return question.get();
    }

    public StringProperty questionProperty() {
        return question;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public ContextMenu getMenu() {
        return menu;
    }
}
