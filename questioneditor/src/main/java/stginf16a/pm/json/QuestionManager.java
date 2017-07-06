package stginf16a.pm.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import stginf16a.pm.questions.Category;
import stginf16a.pm.questions.Question;
import stginf16a.pm.questions.QuestionType;
import stginf16a.pm.wrapper.AnswerWrapper;
import stginf16a.pm.wrapper.QuestionWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Czichotzki on 23.05.2017.
 */
public class QuestionManager {
    private Project project;
    private ObjectMapper mapper;

    public QuestionManager(Project project) {
        this.project = project;
        this.mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    public Category loadCategory(ProjectCategory category) throws IOException {
        Category cat = new Category(category.getCategoryName(), loadQuestions(category));
        category.setCategory(cat);
        return cat;
    }

    public void loadProject() throws IOException {
        for (ProjectCategory category :
                project.getCategories()) {
            loadCategory(category);
        }
    }

    public void saveProject() throws IOException {
        for(ProjectCategory category: project.getCategories()){
            saveCategory(category);
        }
    }

    public void saveCategory(ProjectCategory category) throws IOException {
        File f = new File(project.getPath(category));

        for (QuestionWrapper q :
                category.getCategory().getQuestions()) {
            if(q.isChanged()) {
                saveQuestion(q);
                q.setChanged(false);
            }
        }
    }

    private List<QuestionWrapper> loadQuestions(ProjectCategory category) throws IOException {
        List<QuestionWrapper> result = new ArrayList<>();
        File dir = new File(project.getPath(category));
        File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        for (File file :
                files != null ? files : new File[0]) {
            QuestionWrapper q = loadQuestion(file);
            q.setOldCategory(category);
            q.getOriginal().setCategoryName(category.getCategoryName());
            result.add(q);
        }
        return result;
    }

    private QuestionWrapper loadQuestion(File file) throws IOException {
        Question question = mapper.readValue(file, Question.class);
        String id = file.getName().replace(".json", "");
        question.setId(UUID.fromString(id));
        QuestionWrapper wrapper = new QuestionWrapper(question);
        wrapper.setQuestionHash(ProjectLoader.calcHash(file));
        fixAnswers(wrapper);
        return wrapper;
    }

    private void fixAnswers(QuestionWrapper question) {
        if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
            List<AnswerWrapper> newAnswers = new ArrayList<>();
            Iterator<AnswerWrapper> answers = question.getAnswers().iterator();
            while (answers.hasNext()) {
                AnswerWrapper wrapper = answers.next();
                Iterator<AnswerWrapper> possibilities = question.getPossibilities().iterator();
                while (possibilities.hasNext()) {
                    AnswerWrapper pos = possibilities.next();
                    if (pos.equals(wrapper)) {
                        if (!wrapper.getAnswer().equals(pos.getAnswer())) {
                            question.changed();
                        }
                        answers.remove();
                        newAnswers.add(pos);
                        break;
                    }
                }
            }
            question.getAnswers().addAll(newAnswers);
        }
    }

    private void saveQuestion(QuestionWrapper question, File file) throws IOException {
        File f = new File(file.getAbsolutePath() + File.separator + question.getOriginal().getId().toString() + ".json");
        mapper.writeValue(f, question.getOriginal());
        question.setQuestionHash(ProjectLoader.calcHash(f));
    }

    public void saveQuestion(QuestionWrapper question) throws IOException {
        if (question.isChanged() || question.isMoved()) {
            File f = new File(project.getPath(question.getCategory().getProjectCategory()));
            saveQuestion(question, f);
            question.setChanged(false);
            question.setMoved(false);
            question.setOldCategory(question.getCategory().getProjectCategory());
        }
    }

    public void moveQuestion(QuestionWrapper question) throws IOException {
        if (question.isMoved()) {
            question.setOldCategory(question.getCategory().getProjectCategory());
            question.setMoved(false);
            File f = new File(project.getPath(question.getCategory().getProjectCategory()));
            if (f.exists()) {
                saveQuestion(question, f);
                question.setChanged(false);
            } else {
                question.setChanged(true);
            }
        }
    }

    public void deleteQuestion(QuestionWrapper question, File file) {
        File f = new File(file.getAbsolutePath() + File.separator + question.getOriginal().getId().toString() + ".json");
        if (f.exists()) {
            f.delete();
        }
    }

    public void deleteQuestion(QuestionWrapper question) {
        File f = new File(project.getPath(question.getCategory().getProjectCategory()));
        deleteQuestion(question, f);
        question.getCategory().getQuestions().remove(question);
    }

    public void deleteCategory(ProjectCategory category) {
        File f = new File(project.getPath(category));
        if (f.exists()) {
            f.delete();
        }
    }

}
