package stginf16a.pm.questions;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class Answer {
    @JsonProperty(value = "index")
    private int id;
    @JsonProperty(value = "text")
    private String answer;

    public Answer(int id, String answer){
        this.id = id;
        this.answer = answer;
    }

    public Answer(){

    }

    public static Answer copy(Answer answer) {
        return new Answer(answer.id, answer.answer);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Answer){
            return id == ((Answer) obj).getId();
        }
        else{
            return super.equals(obj);
        }
    }
}
