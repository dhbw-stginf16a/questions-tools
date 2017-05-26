package stginf16a.pm.questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by Czichotzki on 20.05.2017.
 */
public enum QuestionType {
    MULTIPLE_CHOICE("multipleChoice"),LIST("list"),WILDCARD("wildcard"),FILL_IN("fillIn");
    private String name;

    QuestionType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName(){
        return name;
    }

    @JsonCreator
    public static QuestionType fromString(String name){
        for (QuestionType type :
                QuestionType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
