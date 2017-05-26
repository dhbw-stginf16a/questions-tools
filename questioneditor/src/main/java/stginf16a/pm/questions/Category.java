package stginf16a.pm.questions;

import stginf16a.pm.wrapper.QuestionWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class Category {

    private String name;
    private List<QuestionWrapper> questions;

    public Category(String name, List<QuestionWrapper> questions){
        this.name = name;
        this.questions = questions;
    }

    public Category(){
        this.questions = new ArrayList<>();
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
        this.questions = questions;
    }
}
