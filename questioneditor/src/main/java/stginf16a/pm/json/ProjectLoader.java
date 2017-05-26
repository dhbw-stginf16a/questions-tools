package stginf16a.pm.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Created by Czichotzki on 23.05.2017.
 */
public class ProjectLoader {

    public static Project loadProject(File projectFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Project p = mapper.readValue(projectFile, Project.class);
        p.setPath(projectFile.getParent());
        p.setProjectFile(projectFile);
        return p;
    }

    public static void saveProject(Project project, File projectFile) throws IOException {
        project.setPath(projectFile.getParent());
        project.setProjectFile(projectFile);

        createFolders(project);

        for (ProjectCategory cat :
                project.getCategories()) {
            String oldName = cat.updateName();
            if(oldName!=null){
                updateDirectoryName(project, oldName, cat);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(projectFile, project);
    }

    private static void updateDirectoryName(Project project, String oldName, ProjectCategory cat) {
        File dir = new File(project.path + File.separator + oldName);
        dir.renameTo(new File(project.getPath(cat)));
    }

    public static boolean checkFolders(Project p) {
        boolean result = true;

        for (ProjectCategory c :
                p.getCategories()) {
            if(!checkCategoryFolder(p,c)){
                result = false;
            }
        }
        return result;
    }

    private static boolean checkCategoryFolder(Project p, ProjectCategory c){
        File directory = new File(p.path);

        boolean result = false;

        File[] dir = directory.listFiles(File::isDirectory);

        for (File d :
                dir != null ? dir : new File[0]) {
            if(d.getAbsolutePath().equals(p.getPath(c))){
                result = true;
            }

        }
        return result;
    }

    public static void createFolders(Project p){
        for (ProjectCategory c :
                p.getCategories()) {
            createCategoryFolder(p, c);
        }
    }

    public static void createCategoryFolder(Project p , ProjectCategory c){
        if(!checkCategoryFolder(p, c)){
            File dir = new File(p.getPath(c));
            dir.mkdir();
        }
    }
}
