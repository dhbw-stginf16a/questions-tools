package stginf16a.pm.questions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Czichotzki on 20.05.2017.
 */
public class Question {

    @JsonIgnore
    private UUID id;

    private QuestionType type;
    private QuestionDifficulty difficulty;
    private QuestionStatus status;
    private String question;
    @JsonProperty(value = "requiredAnswers")
    private int numberOfCorrectAnswers;
    private List<Answer> answers;
    private List<Answer> possibilities;
    @JsonProperty(value = "category")
    private String categoryName;


    public Question(QuestionType type){
        this.type=type;
        this.status=QuestionStatus.DRAFT;
        this.difficulty=QuestionDifficulty.EASY;
        this.answers = new ArrayList<>();
        this.possibilities = new ArrayList<>();
    }

    public Question(){
        this(QuestionType.WILDCARD);
    }

    @JsonIgnore
    public static Question copy(Question original) {
        Question q = new Question();
        q.answers = copyAnswerList(original.answers);
        q.categoryName = original.categoryName;
        q.difficulty = original.difficulty;
        q.numberOfCorrectAnswers = original.numberOfCorrectAnswers;
        q.possibilities = copyAnswerList(original.possibilities);
        q.question = original.question;
        q.type = original.type;
        return q;
    }

    private static List<Answer> copyAnswerList(List<Answer> original) {
        List<Answer> result = new ArrayList<>(original.size());
        for (Answer answer : original) {
            result.add(Answer.copy(answer));
        }
        return result;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public UUID getId() {
        if(id==null){
            id= UUID.randomUUID();
        }
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public QuestionDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(QuestionDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public QuestionStatus getStatus() {
        return status;
    }

    public void setStatus(QuestionStatus status) {
        this.status = status;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getNumberOfCorrectAnswers() {
        return numberOfCorrectAnswers;
    }

    public void setNumberOfCorrectAnswers(int numberOfCorrectAnswers) {
        this.numberOfCorrectAnswers = numberOfCorrectAnswers;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getPossibilities() {
        return possibilities;
    }

    public void setPossibilities(List<Answer> possibilities) {
        this.possibilities = possibilities;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || id == null){
            return false;
        }
        if(obj instanceof Question){
            return ((Question) obj).getId().equals(id);
        }
        return super.equals(obj);
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    //@JsonIgnore
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
