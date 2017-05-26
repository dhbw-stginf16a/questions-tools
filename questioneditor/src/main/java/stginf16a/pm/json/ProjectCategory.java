package stginf16a.pm.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import stginf16a.pm.questions.Category;

import java.io.File;

/**
 * Created by Czichotzki on 22.05.2017.
 */
public class ProjectCategory {
    private String categoryName;

    @JsonIgnore
    private String categoryDirectoryName;
    @JsonIgnore
    private Category category;

    public ProjectCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public ProjectCategory(){

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static ProjectCategory fromCategory(Category category){
        return new ProjectCategory(category.getName());
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryDirectoryName() {
        return categoryDirectoryName;
    }

    public void setCategoryDirectoryName(String categoryDirectoryName) {
        this.categoryDirectoryName = categoryDirectoryName;
    }

    public String updateName(){
        String oldName = this.categoryName;
        this.categoryName = this.category.getName();
        if(categoryName.equals(oldName)){
            return null;
        }
        return oldName;
    }
}
