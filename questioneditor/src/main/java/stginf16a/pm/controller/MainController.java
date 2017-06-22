package stginf16a.pm.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import stginf16a.pm.json.Project;
import stginf16a.pm.json.ProjectCategory;
import stginf16a.pm.json.ProjectLoader;
import stginf16a.pm.json.QuestionManager;
import stginf16a.pm.questions.*;
import stginf16a.pm.ui.*;
import stginf16a.pm.util.NestedObjectProperty;
import stginf16a.pm.util.QuestionEditor;
import stginf16a.pm.wrapper.AnswerWrapper;
import stginf16a.pm.wrapper.QuestionWrapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Czichotzki on 21.05.2017.
 */

public class MainController implements Initializable {

    private Stage stage;

    private ObjectProperty<QuestionWrapper> selectedQuestion;
    private ObjectProperty<RootTreeItem> rootTreeItem;
    private ObjectProperty<Project> project;

    @FXML
    private TreeTableView<Object> questionTableTree;
    @FXML
    private TreeTableColumn<Object, QuestionType> typeColumn;
    @FXML
    private TreeTableColumn<Object, QuestionStatus> statusColumn;
    @FXML
    private TreeTableColumn<Object, String> questionColumn;
    @FXML
    private TreeTableColumn<Object, QuestionDifficulty> difficultyColumn;
    @FXML
    private ChoiceBox<QuestionType> typeChoiceBox;
    @FXML
    private ChoiceBox<QuestionDifficulty> difficultyChoiceBox;
    @FXML
    private ChoiceBox<QuestionStatus> statusChoiceBox;
    @FXML
    private TextField correctAnswerTextField;
    @FXML
    private TextArea questionTextArea;
    @FXML
    private TableView<AnswerWrapper> answerTableView;
    @FXML
    private TableColumn<AnswerWrapper, Integer> answerIdColumn;
    @FXML
    private TableColumn<AnswerWrapper, String> answerColumn;
    @FXML
    private Label idLabel;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        DragableTreeTableRow.setController(this);

        rootTreeItem = new SimpleObjectProperty<>();
        project = new SimpleObjectProperty<>();

        project.addListener(((observable, oldValue, newValue) -> {
            RootTreeItem rootTree = new RootTreeItem(newValue.getCategoriesList(), this::deleteQuestion, this::deleteCategory);
            rootTreeItem.setValue(rootTree);
        }));

        project.set(Project.getDefaultProject());

        questionTableTree.rootProperty().bind(rootTreeItem);

        questionColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Object, String> p) -> ((AbstractTreeItem) p.getValue()).questionProperty());

        questionColumn.setCellFactory(param -> new TreeTableCell<Object, String>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                if (!empty && item != null) {
                    item = item.replace("\n", " ");
                }
                setText(item);
                super.updateItem(item, empty);

            }
        });

        statusColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Object, QuestionStatus> p) -> ((AbstractTreeItem) p.getValue()).statusProperty());

        typeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Object, QuestionType> p) -> ((AbstractTreeItem) p.getValue()).typeProperty());

        difficultyColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Object, QuestionDifficulty> p) -> ((AbstractTreeItem) p.getValue()).difficultyProperty());

        questionTableTree.setShowRoot(false);

        //ChoiceBoxValues
        typeChoiceBox.getItems().setAll(QuestionType.values());
        statusChoiceBox.getItems().setAll(QuestionStatus.values());
        difficultyChoiceBox.getItems().setAll(QuestionDifficulty.values());

        //SelectedQuestion
        selectedQuestion = new SimpleObjectProperty<>();

        questionTableTree.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue instanceof QuestionTreeItem) {
                QuestionWrapper wrapper = (QuestionWrapper) newValue.getValue();
                selectedQuestion.setValue(wrapper);
                updateAnswerTableBinding(QuestionType.MULTIPLE_CHOICE, selectedQuestion.getValue().getType());
            }
        }));

        questionTableTree.setRowFactory(param -> new DragableTreeTableRow());

        //questionTableTree.setColumnResizePolicy((param -> true));

        //Binding
        questionTextArea.textProperty().bindBidirectional(new NestedObjectProperty<>(selectedQuestion, QuestionWrapper::questionProperty, true));

        statusChoiceBox.valueProperty().bindBidirectional(new NestedObjectProperty<>(selectedQuestion, QuestionWrapper::statusProperty, true));
        typeChoiceBox.valueProperty().bindBidirectional(new NestedObjectProperty<>(selectedQuestion, QuestionWrapper::typeProperty, true));
        difficultyChoiceBox.valueProperty().bindBidirectional(new NestedObjectProperty<>(selectedQuestion, QuestionWrapper::difficultyProperty, true));

        correctAnswerTextField.textProperty().bindBidirectional(new NestedObjectProperty<>(selectedQuestion, QuestionWrapper::numberOfCorrectAnswersProperty, true), new NumberStringConverter());

        typeChoiceBox.valueProperty().addListener(((observable, oldValue, newValue) -> updateAnswerTableBinding(oldValue, newValue)));

        answerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("answer"));
        answerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        answerColumn.setEditable(true);

        answerColumn.setOnEditCommit(event -> {
            if (event.getNewValue().equals("")) {
                event.getRowValue().setAnswer(event.getOldValue());
            } else {
                event.getRowValue().setAnswer(event.getNewValue());
            }
        });

        answerTableView.setRowFactory(tv -> new AnswerTableRow(selectedQuestion, answerTableView));

        idLabel.textProperty().bind(new NestedObjectProperty<>(selectedQuestion, QuestionWrapper::idProperty, false).asString());
    }

    public void deleteCategory(Category category) {
        project.get().getCategories().remove(category.getProjectCategory());
        QuestionManager manager = new QuestionManager(this.project.get());
        manager.deleteCategory(category.getProjectCategory());
        questionTableTree.refresh();
    }

    public void deleteQuestion(QuestionWrapper question) {
        QuestionManager manager = new QuestionManager(this.project.get());
        manager.deleteQuestion(question);
        questionTableTree.refresh();
    }

    private void updateAnswerTableBinding(QuestionType oldValue, QuestionType newValue) {
        if (newValue == QuestionType.MULTIPLE_CHOICE) {
            answerTableView.itemsProperty().unbind();
            answerTableView.itemsProperty().bind(new NestedObjectProperty<>(selectedQuestion, QuestionWrapper::possibilitiesProperty, true));
            answerTableView.refresh();
        } else if (oldValue == QuestionType.MULTIPLE_CHOICE) {
            answerTableView.itemsProperty().unbind();
            answerTableView.itemsProperty().bind(new NestedObjectProperty<>(selectedQuestion, QuestionWrapper::answersProperty, true));
            answerTableView.refresh();
        }
    }

    public void loadProject(File file) {
        try {
            if (file == null) {
                return;
            }
            Project p = ProjectLoader.loadProject(file);
            QuestionManager manager = new QuestionManager(p);
            manager.loadProject();
            this.project.set(p);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void saveProject(File file) {
        if (file == null) {
            return;
        }

        try {
            ProjectLoader.saveProject(project.get(), file);
            QuestionManager manager = new QuestionManager(project.get());
            manager.saveProject();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void moveQuestionToCategory(QuestionWrapper question, Category category) {
        //Move Tree Item
        //question.getCategory().getTreeItem().getChildren().remove(question.getTreeItem());
        question.getTreeItem().delete();
        category.getTreeItem().getChildren().add(new QuestionTreeItem(question, category.getTreeItem()::deleteQuestion, false));

        //Move in Category
        question.getCategory().getQuestions().remove(question);
        category.getQuestions().add(question);
        question.setCategory(category);

        //Move/Recreate Json File
        QuestionManager manager = new QuestionManager(project.get());
        try {
            manager.moveQuestion(question);
        } catch (IOException e) {
            QuestionEditor.handleException(e);
        }

        questionTableTree.refresh();
    }

    @FXML
    public void onOpen(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Question Editor Project File", "*.qepr");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        File file = fileChooser.showOpenDialog(stage);

        loadProject(file);
    }

    @FXML
    public void onSave(ActionEvent e) {
        if (project.get().hasFile()) {
            saveProject(project.get().getProjectFile());
        } else {
            onSaveAs(e);
        }
    }

    @FXML
    private void onSaveAs(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Question Editor Project File", "*.qepr");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setSelectedExtensionFilter(filter);
        File file = fileChooser.showSaveDialog(stage);

        saveProject(file);
    }

    @FXML
    public void onAddAnswer(ActionEvent e) {
        AnswerWrapper wrapper = new AnswerWrapper(new Answer());
        int max = 0;
        if (selectedQuestion.get().getType() == QuestionType.MULTIPLE_CHOICE) {
            for (AnswerWrapper answerWrapper :
                    selectedQuestion.get().getPossibilities()) {
                max = Math.max(answerWrapper.getId(), max);
            }
            wrapper.setId(max + 1);
            selectedQuestion.get().getPossibilities().add(wrapper);
        } else {
            for (AnswerWrapper answerWrapper :
                    selectedQuestion.get().getAnswers()) {
                max = Math.max(answerWrapper.getId(), max);
            }
            wrapper.setId(max + 1);
            selectedQuestion.get().getAnswers().add(wrapper);
        }
    }

    @FXML
    public void onNewCategory(ActionEvent e) {

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Rename Category");
        dialog.setHeaderText("Enter new name.");
        dialog.showAndWait();
        if (dialog.getResult() != null || dialog.getResult().equals("")) {
            Project p = project.get();
            ProjectCategory pcat = new ProjectCategory(dialog.getResult());
            Category cat = new Category();
            cat.setName(dialog.getResult());
            pcat.setCategory(cat);
            project.get().getCategories().add(pcat);
            rootTreeItem.get().addChild(cat);
        }
    }
}
