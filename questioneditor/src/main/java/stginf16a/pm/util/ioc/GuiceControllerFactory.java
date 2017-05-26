package stginf16a.pm.util.ioc;

import javafx.util.Callback;
import stginf16a.pm.util.QuestionEditor;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class GuiceControllerFactory implements Callback<Class<?>,Object> {
    @Override
    public Object call(Class<?> param) {
        return QuestionEditor.injector.getInstance(param);
    }
}
