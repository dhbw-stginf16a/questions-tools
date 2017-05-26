package stginf16a.pm.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import stginf16a.pm.ui.ExceptionAlert;
import stginf16a.pm.util.ioc.GuiceControllerFactory;
import stginf16a.pm.util.ioc.MainIocModule;

import java.util.ResourceBundle;

/**
 * Created by Czichotzki on 21.05.2017.
 */
public class QuestionEditor {

    public static Injector injector;

    public static void init(){
        injector = Guice.createInjector(new MainIocModule());
    }

    public static FXMLLoader getLoader(){
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(new GuiceControllerFactory());
        loader.setResources(injector.getInstance(ResourceBundle.class));
        return loader;
    }

    public static String getLangString(String key){
        return injector.getInstance(ResourceBundle.class).getString(key);
    }

    public static void handleException(Exception e){
        e.printStackTrace();
        if(Platform.isFxApplicationThread()){
            Alert alert = new ExceptionAlert(e);
            alert.showAndWait();
        }
    }
}
