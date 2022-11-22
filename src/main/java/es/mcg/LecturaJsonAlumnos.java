package es.mcg;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class LecturaJsonAlumnos {
    private static final String ENCODE = "UTF-8";
    public static void main(String[] args) {
        File file = null;
        String fileContent;
        try 
        {
            file = new File("alumnos.json");
            fileContent = FileUtils.readFileToString(file, LecturaJsonAlumnos.ENCODE);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
