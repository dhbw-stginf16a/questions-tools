package stginf16a.pm.wrapper;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import stginf16a.pm.questions.Answer;
import stginf16a.pm.questions.Changed;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class AnswerWrapper implements Changed{

    private Answer a;

    private IntegerProperty id;
    private StringProperty answer;
    private boolean changed;

    public AnswerWrapper(Answer a){
        this.a = a;
        changed = false;
    }

    public Answer getOriginal(){
        return a;
    }

    public int getId() {
        return idProperty().get();
    }

    public IntegerProperty idProperty() {
        if(id == null){
            id = new SimpleIntegerProperty(a.getId());
            id.addListener((observable, oldValue, newValue) -> {a.setId(newValue.intValue()); changed();});
        }
        return id;
    }

    public void setId(int id) {
        this.idProperty().set(id);
    }

    public String getAnswer() {
        return answerProperty().get();
    }

    public StringProperty answerProperty() {
        if(answer == null){
            answer = new SimpleStringProperty(a.getAnswer());
            answer.addListener((observable, oldValue, newValue) -> {a.setAnswer(newValue); changed();});
        }
        return answer;
    }

    public void setAnswer(String answer) {
        this.answerProperty().set(answer);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AnswerWrapper){
            return ((AnswerWrapper) obj).getOriginal().equals(getOriginal());
        }
        return super.equals(obj);
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
}
