package stginf16a.pm.ui;

import stginf16a.pm.questions.Category;

import java.util.List;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class RootTreeItem extends AbstractTreeItem {

    public RootTreeItem(List<Category> categories){
        for(Category c: categories){
            this.getChildren().add(new CategoryTreeItem(c));
        }
    }
}
