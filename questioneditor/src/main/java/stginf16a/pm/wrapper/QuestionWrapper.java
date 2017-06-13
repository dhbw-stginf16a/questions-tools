package stginf16a.pm.wrapper;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import stginf16a.pm.questions.*;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class QuestionWrapper implements Changed{

    private Question q;

    private boolean changed;

    private ObjectProperty<QuestionType> type = null;
    private ObjectProperty<QuestionDifficulty> difficulty = null;
    private ObjectProperty<QuestionStatus> status = null;
    private ObjectProperty<UUID> id = null;

    private StringProperty question = null;
    private IntegerProperty numberOfCorrectAnswers = null;
    private ListProperty<AnswerWrapper> answers = null;
    private ListProperty<AnswerWrapper> possibilities = null;

    private Category category;

    public QuestionWrapper(Question question){
        this.q = question;
        this.changed = false;
    }

    public static QuestionWrapper copy(QuestionWrapper question) {
        Question newQ = Question.copy(question.getOriginal());
        return new QuestionWrapper(newQ);
    }

    public Question getOriginal(){
        return q;
    }

    public QuestionType getType() {
        return typeProperty().get();
    }

    public void setType(QuestionType type) {
        this.typeProperty().set(type);
    }

    public ObjectProperty<QuestionType> typeProperty() {
        if(type == null){
            type = new SimpleObjectProperty<>(q.getType());
            type.addListener(((observable, oldValue, newValue) -> {
                q.setType(newValue);
                changed();

                if(oldValue != newValue && oldValue != null) {
                    if (newValue == QuestionType.MULTIPLE_CHOICE) {
                        System.out.println("ToPos");
                        switchAnswersToPossibilities();
                    } else if(oldValue == QuestionType.MULTIPLE_CHOICE){
                        System.out.println("ToAns");
                        switchPossibilitiesToAnswer();
                    }
                }
            }));
        }
        return type;
    }

    public QuestionDifficulty getDifficulty() {
        return difficultyProperty().get();
    }

    public void setDifficulty(QuestionDifficulty difficulty) {
        this.difficultyProperty().set(difficulty);
    }

    public ObjectProperty<QuestionDifficulty> difficultyProperty() {
        if(difficulty == null){
            difficulty = new SimpleObjectProperty<>(q.getDifficulty());
            difficulty.addListener((observable, oldValue, newValue) -> {q.setDifficulty(newValue); changed();});
        }
        return difficulty;
    }

    public QuestionStatus getStatus() {
        return statusProperty().get();
    }

    public void setStatus(QuestionStatus status) {
        this.statusProperty().set(status);
    }

    public ObjectProperty<QuestionStatus> statusProperty() {
        if(status == null){
            status = new SimpleObjectProperty<>(q.getStatus());
            status.addListener((observable, oldValue, newValue) -> {q.setStatus(newValue); changed();});
        }
        return status;
    }

    public String getQuestion() {
        return questionProperty().get();
    }

    public void setQuestion(String question) {
        this.questionProperty().set(question);
    }

    public StringProperty questionProperty() {
        if(question == null){
            question = new SimpleStringProperty(q.getQuestion());
            question.addListener((observable, oldValue, newValue) -> {q.setQuestion(newValue); changed();});
        }
        return question;
    }

    public int getNumberOfCorrectAnswers() {
        return numberOfCorrectAnswersProperty().get();
    }

    public void setNumberOfCorrectAnswers(int numberOfCorrectAnswers) {
        this.numberOfCorrectAnswersProperty().set(numberOfCorrectAnswers);
    }

    public IntegerProperty numberOfCorrectAnswersProperty() {
        if(numberOfCorrectAnswers == null){
            numberOfCorrectAnswers = new SimpleIntegerProperty(q.getNumberOfCorrectAnswers());
            numberOfCorrectAnswers.addListener((observable, oldValue, newValue) -> {q.setNumberOfCorrectAnswers(newValue.intValue()); changed();});
        }
        return numberOfCorrectAnswers;
    }

    public ObservableList<AnswerWrapper> getAnswers() {
        return answersProperty().get();
    }

    public void setAnswers(ObservableList<AnswerWrapper> answers) {
        this.answersProperty().set(answers);
    }

    public ListProperty<AnswerWrapper> answersProperty() {
        if(answers == null){
            ObservableList<AnswerWrapper> wrapperList = FXCollections.observableList(new ArrayList<>());
            for(Answer a: q.getAnswers()){
                wrapperList.add(new AnswerWrapper(a));
            }
            answers = new SimpleListProperty(wrapperList);
            answers.addListener((ListChangeListener.Change<? extends AnswerWrapper> c)->{
                while(c.next()){
                    if(c.wasAdded()){
                        for (AnswerWrapper a: c.getAddedSubList()
                                ) {
                            q.getAnswers().add(a.getOriginal());
                            changed();
                        }
                    }else if(c.wasRemoved()){
                        for (AnswerWrapper a: c.getRemoved()
                                ) {
                            q.getAnswers().remove(a.getOriginal());
                            changed();
                        }
                    }
                }
            });
        }
        return answers;
    }

    public ObservableList<AnswerWrapper> getPossibilities() {
        return possibilitiesProperty().get();
    }

    public void setPossibilities(ObservableList<AnswerWrapper> possibilities) {
        this.possibilitiesProperty().set(possibilities);
    }

    public ListProperty<AnswerWrapper> possibilitiesProperty() {
        if(possibilities == null){
            ObservableList<AnswerWrapper> wrapperList = FXCollections.observableList(new ArrayList<>());
            for(Answer a: q.getPossibilities()){
                wrapperList.add(new AnswerWrapper(a));
            }
            possibilities = new SimpleListProperty(wrapperList);
            possibilities.addListener((ListChangeListener.Change<? extends AnswerWrapper> c)->{
                while(c.next()){
                    if(c.wasAdded()){
                        for (AnswerWrapper a: c.getAddedSubList()
                             ) {
                            q.getPossibilities().add(a.getOriginal());
                            changed();
                        }
                    }else if(c.wasRemoved()){
                        for (AnswerWrapper a: c.getRemoved()
                                ) {
                            q.getPossibilities().remove(a.getOriginal());
                            changed();
                        }
                    }
                }
            });
        }
        return possibilities;
    }

    public UUID getId() {
        return id.get();
    }

    public void setId(UUID id) {
        this.id.set(id);
    }

    public ObjectProperty<UUID> idProperty() {
        if(id==null){
            id = new SimpleObjectProperty<>(q.getId());
        }
        return id;
    }

    private void switchAnswersToPossibilities(){
        possibilitiesProperty().clear();
        for (AnswerWrapper a :
                answersProperty()) {
            possibilitiesProperty().add(a);
        }
        answers.clear();
    }

    private void switchPossibilitiesToAnswer(){
        answersProperty().clear();
        for (AnswerWrapper a :
                possibilitiesProperty()) {
            answersProperty().add(a);
        }
        possibilitiesProperty().clear();
    }

    public boolean isChanged() {
        return changed || answersChanged();
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    private boolean answersChanged() {
        for (AnswerWrapper answer :
                answersProperty().get()) {
            if(answer.isChanged()){
                return true;
            }
        }

        for (AnswerWrapper answer :
                possibilitiesProperty().get()) {
            if (answer.isChanged()) {
                return true;
            }
            }

        return false;
    }

    public void changed(){
        setChanged(true);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
