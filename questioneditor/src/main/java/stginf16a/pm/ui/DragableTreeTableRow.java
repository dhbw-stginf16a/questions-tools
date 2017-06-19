package stginf16a.pm.ui;

import javafx.scene.control.TreeTableRow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import stginf16a.pm.controller.MainController;
import stginf16a.pm.questions.Category;
import stginf16a.pm.wrapper.QuestionWrapper;


/**
 * Created by Czichotzki on 16.06.2017.
 */
public class DragableTreeTableRow extends TreeTableRow<Object> {

    private static QuestionWrapper dragContainer;
    private static MainController controller;

    public DragableTreeTableRow() {

        this.setOnDragDetected(event -> {
            if (!this.isEmpty() && (this.getItem() instanceof QuestionWrapper)) {
                Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
                db.setDragView(this.snapshot(null, null));
                ClipboardContent cc = new ClipboardContent();
                cc.put(DataFormat.PLAIN_TEXT, "");
                dragContainer = (QuestionWrapper) this.getItem();
                db.setContent(cc);
                event.consume();
            }
        });

        this.setOnDragOver(event -> {
            if (!this.isEmpty() && (this.getItem() instanceof Category)) {
                if (this.getItem() != dragContainer.getCategory()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        this.setOnDragDone(event -> {
            dragContainer = null;
            event.consume();
        });

        this.setOnDragDropped(event -> {
            if (!this.isEmpty() && (this.getItem() instanceof Category)) {
                Dragboard db = event.getDragboard();
                controller.moveQuestionToCategory(dragContainer, (Category) this.getItem());
                event.consume();
            }
        });
    }

    public static MainController getController() {
        return controller;
    }

    public static void setController(MainController controller) {
        DragableTreeTableRow.controller = controller;
    }

    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            this.setContextMenu(((AbstractTreeItem) getTreeItem()).getMenu());
        }
    }
}
