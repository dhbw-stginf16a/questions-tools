package stginf16a.pm.ui;

import javafx.beans.binding.When;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.control.*;
import stginf16a.pm.questions.QuestionType;
import stginf16a.pm.util.NestedObjectProperty;
import stginf16a.pm.wrapper.AnswerWrapper;
import stginf16a.pm.wrapper.QuestionWrapper;

/**
 * Created by Czichotzki on 22.05.2017.
 */
public class AnswerTableRow extends TableRow<AnswerWrapper> {

    private NestedObjectProperty<ObservableList<AnswerWrapper>,QuestionWrapper> selectedQuestionAnswerList;
    private NestedObjectProperty<QuestionType,QuestionWrapper> selectedQuestionType;

    private TableView<AnswerWrapper> answerTableView;
    private boolean active;

    private static final PseudoClass CORRECT_PSEUDO_CLASS = PseudoClass.getPseudoClass("correct");

    @Override
    protected void updateItem(AnswerWrapper item, boolean empty) {
        super.updateItem(item, empty);
        if(selectedQuestionAnswerList.getValue().contains(item) && selectedQuestionType.getValue().equals(QuestionType.MULTIPLE_CHOICE)){
            pseudoClassStateChanged(CORRECT_PSEUDO_CLASS,true);
            active=true;
        }else{
            pseudoClassStateChanged(CORRECT_PSEUDO_CLASS, false);
            active=false;
        }
    }

    public AnswerTableRow(ObjectProperty<QuestionWrapper> selectedQuestion, TableView<AnswerWrapper> answerTableView) {
        super();
        getStyleClass().add("answerRow");

        this.setEditable(true);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem checkMenuItem = new MenuItem("Toggle Correct Answer");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(checkMenuItem, deleteMenuItem);
        this.setContextMenu(contextMenu);

        this.answerTableView = answerTableView;

        //ObjectProperty<QuestionWrapper> selectedQuestion1 = selectedQuestion;
        this.selectedQuestionAnswerList = new NestedObjectProperty<>(selectedQuestion, QuestionWrapper::answersProperty, false);
        this.selectedQuestionType = new NestedObjectProperty<>(selectedQuestion, QuestionWrapper::typeProperty,false);

        checkMenuItem.visibleProperty().bind(new When(this.selectedQuestionType.isEqualTo(QuestionType.MULTIPLE_CHOICE)).then(true).otherwise(false));

        checkMenuItem.setOnAction(event -> {
            if(this.active){
                selectedQuestionAnswerList.getValue().remove(this.getItem());
                this.answerTableView.refresh();
                active = false;
            }
            else{
                if (!selectedQuestionAnswerList.get().contains(this.getItem())) {
                    selectedQuestionAnswerList.getValue().add(this.getItem());
                    this.answerTableView.refresh();
                }
                active = false;
            }
        });

        deleteMenuItem.setOnAction(event -> {
            selectedQuestion.get().getAnswers().remove(this.getItem());
            selectedQuestion.get().getPossibilities().remove(this.getItem());
            answerTableView.refresh();
        });
    }
}
