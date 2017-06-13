package stginf16a.pm.questions;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import stginf16a.pm.json.Project;
import stginf16a.pm.json.ProjectCategory;
import stginf16a.pm.wrapper.QuestionWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class Category {

    private String name;
    private ObservableList<QuestionWrapper> questions;
    private ProjectCategory projectCategory;

    public Category(String name, List<QuestionWrapper> questions){
        this.name = name;
        for (QuestionWrapper q : questions
                ) {
            q.setCategory(this);
        }
        this.questions = FXCollections.observableArrayList(questions);
        this.questions.addListener((ListChangeListener.Change<? extends QuestionWrapper> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (QuestionWrapper q :
                            c.getAddedSubList()) {
                        q.setCategory(this);
                    }
                }
            }
        });
    }

    public Category(){
        this.questions = FXCollections.observableArrayList(new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        for (QuestionWrapper q :
                questions) {
            q.getOriginal().setCategoryName(name);
        }
    }

    public List<QuestionWrapper> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionWrapper> questions) {
        this.questions = FXCollections.observableArrayList(questions);
    }

    public ProjectCategory getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(ProjectCategory projectCategory) {
        this.projectCategory = projectCategory;
    }

    public boolean checkHash(Project project) throws IOException {
        for (QuestionWrapper questionWrapper : this.getQuestions()) {
            if (!questionWrapper.checkHash(project)) {
                return false;
            }
        }
        return true;
    }
}
