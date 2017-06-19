package stginf16a.pm.ui;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextInputDialog;
import stginf16a.pm.json.QuestionManager;
import stginf16a.pm.questions.Category;
import stginf16a.pm.questions.Question;
import stginf16a.pm.wrapper.QuestionWrapper;

import java.util.function.Consumer;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class CategoryTreeItem extends AbstractTreeItem{

    private final Consumer<QuestionWrapper> parentDeleteQuestion;
    private QuestionManager manager;
    private Category category;

    public CategoryTreeItem(Category category, Consumer<QuestionWrapper> deleteQuestion, Consumer<Category> deleteCategory) {
        super();
        this.category = category;
        this.category.setTreeItem(this);
        this.setValue(category);
        this.manager = manager;

        this.parentDeleteQuestion = deleteQuestion;

        MenuItem deleteMenuItem = new MenuItem("Delete");

        MenuItem newQuestionMenuItem = new MenuItem("New Question");
        MenuItem renameCategory = new MenuItem("Rename");

        menu.setOnShown(event -> {
            deleteMenuItem.setDisable(!this.getChildren().isEmpty());
        });

        newQuestionMenuItem.setOnAction(event -> {
            Question question = new Question();
            question.setCategoryName(category.getName());
            QuestionWrapper wrapper = new QuestionWrapper(question);
            category.getQuestions().add(wrapper);
            QuestionTreeItem treeItem = new QuestionTreeItem(wrapper, this::deleteQuestion, true);
            wrapper.setCategory(category);
            //treeItemHashMap.put(wrapper, treeItem);
            this.getChildren().add(treeItem);
        });

        renameCategory.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog(category.getName());
            dialog.setTitle("Rename Category");
            dialog.setHeaderText("Enter new name.");
            dialog.showAndWait();
            if(dialog.getResult()!=null){
                category.setName(dialog.getResult());
                this.questionProperty().setValue(category.getName());
            }
        });

        deleteMenuItem.setOnAction(event -> {
            deleteCategory.accept(this.category);
        });

        menu.getItems().addAll(deleteMenuItem, renameCategory, new SeparatorMenuItem(), newQuestionMenuItem);

        this.questionProperty().setValue(category.getName());
        for(QuestionWrapper q: category.getQuestions()){
            QuestionTreeItem treeItem = new QuestionTreeItem(q, this::deleteQuestion, false);
            this.getChildren().add(treeItem);
        }
    }

    public void deleteQuestion(QuestionWrapper question) {
        this.getChildren().remove(question.getTreeItem());
        this.parentDeleteQuestion.accept(question);
    }

    public void addCopyQuestion(QuestionWrapper question) {
        QuestionWrapper wrapper = QuestionWrapper.copy(question);
        category.getQuestions().add(wrapper);
        QuestionTreeItem treeItem = new QuestionTreeItem(wrapper, this::deleteQuestion, true);
        wrapper.setCategory(category);
        this.getChildren().add(treeItem);
    }
}
