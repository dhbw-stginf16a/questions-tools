package stginf16a.pm.ui;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import stginf16a.pm.questions.Category;
import stginf16a.pm.questions.Question;
import stginf16a.pm.wrapper.QuestionWrapper;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class CategoryTreeItem extends AbstractTreeItem{

    public CategoryTreeItem(Category category){
        super();

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setDisable(true);//TODO: Implement functionality
        MenuItem newQuestionMenuItem = new MenuItem("New Question");
        MenuItem renameCategory = new MenuItem("Rename");

        newQuestionMenuItem.setOnAction(event -> {
            Question question = new Question();
            question.setCategoryName(category.getName());
            QuestionWrapper wrapper = new QuestionWrapper(question);
            category.getQuestions().add(wrapper);
            this.getChildren().add(new QuestionTreeItem(wrapper, true));
        });

        renameCategory.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog(category.getName());
            dialog.setTitle("Rename Category");
            dialog.setHeaderText("Enter new name.");
            dialog.showAndWait();
            if(dialog.getResult()!=null){
                category.setName(dialog.getResult());
                this.setValue(dialog.getResult());
                this.questionProperty().setValue(category.getName());
            }
        });

        menu.getItems().addAll(deleteMenuItem, renameCategory, new SeparatorMenuItem(), newQuestionMenuItem);

        this.setValue(category.getName());
        this.questionProperty().setValue(category.getName());
        for(QuestionWrapper q: category.getQuestions()){
            this.getChildren().add(new QuestionTreeItem(q, false));
        }
    }
}
