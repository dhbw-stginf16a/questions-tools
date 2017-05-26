package stginf16a.pm.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import stginf16a.pm.questions.Category;
import stginf16a.pm.questions.Changed;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Czichotzki on 22.05.2017.
 */
public class Project implements Changed {
    @JsonIgnore
    String path;
    private List<ProjectCategory> categories;
    @JsonIgnore
    private boolean changed;
    @JsonIgnore
    private File projectFile;

    public static Project getDefaultProject() {
        Project p = new Project();
        ProjectCategory pcat = new ProjectCategory();
        Category cat = new Category();
        cat.setName("New Category");
        pcat.setCategory(cat);
        pcat.setCategoryName("New Category");
        p.setCategories(new ArrayList<>(Collections.singletonList(pcat)));
        return p;
    }

    public List<ProjectCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ProjectCategory> categories) {
        this.categories = categories;
    }

    @JsonIgnore
    public String getPath(ProjectCategory category) {
        return path + "\\" + category.getCategoryName();
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonIgnore
    public List<Category> getCategoriesList() {
        List<Category> result = new ArrayList<>();

        for (ProjectCategory cat :
                categories) {
            result.add(cat.getCategory());
        }

        return result;
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    @Override
    public void changed() {
        setChanged(true);
    }

    @JsonIgnore
    public boolean hasFile() {
        return !(projectFile == null || !projectFile.exists());
    }

    public File getProjectFile() {
        return this.projectFile;
    }

    public void setProjectFile(File projectFile) {
        this.projectFile = projectFile;
    }
}
