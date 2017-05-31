package stginf16a.pm.ui;

import stginf16a.pm.questions.Category;
import stginf16a.pm.wrapper.QuestionWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class RootTreeItem extends AbstractTreeItem {

    Consumer<Category> deleteCategory;
    Consumer<QuestionWrapper> deleteQuestion;
    HashMap<Category, CategoryTreeItem> treeItemHashMap;

    public RootTreeItem(List<Category> categories, Consumer<QuestionWrapper> deleteQuestion, Consumer<Category> deleteCategory) {
        this.deleteCategory = deleteCategory;
        this.deleteQuestion = deleteQuestion;
        this.treeItemHashMap = new HashMap<>();
        for(Category c: categories){
            addChild(c);
        }
    }

    public void deleteCategory(Category category) {
        this.getChildren().remove(treeItemHashMap.get(category));
        this.treeItemHashMap.remove(category);
        this.deleteCategory.accept(category);
    }

    public void addChild(Category category) {
        CategoryTreeItem treeItem = new CategoryTreeItem(category, deleteQuestion, this::deleteCategory);
        treeItemHashMap.put(category, treeItem);
        this.getChildren().add(treeItem);
    }
}
