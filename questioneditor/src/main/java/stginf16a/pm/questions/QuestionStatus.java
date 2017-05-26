package stginf16a.pm.questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public enum QuestionStatus {
    DRAFT("draft"),REVIEW("review"),APPROVED("approved");

    private String name;

    QuestionStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return name;
    }

    @JsonCreator
    public static QuestionStatus fromString(String name){
        for (QuestionStatus status :
                QuestionStatus.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        return null;
    }
}
