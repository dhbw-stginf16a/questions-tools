package stginf16a.pm.questions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public enum QuestionDifficulty {
    EASY(1),NORMAL(2),HARD(3);
    private int id;

    QuestionDifficulty(int id) {
        this.id = id;
    }

    @JsonValue
    public int getId(){
        return id;
    }

    @JsonCreator
    public static QuestionDifficulty fromId(int id){
        for (QuestionDifficulty difficulty :
                QuestionDifficulty.values()) {
            if (difficulty.getId() == id) {
                return difficulty;
            }
        }
        return null;
    }
}
