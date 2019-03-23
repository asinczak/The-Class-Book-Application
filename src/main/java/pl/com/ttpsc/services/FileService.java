package pl.com.ttpsc.services;

import pl.com.ttpsc.data.PersonInSchool;
import pl.com.ttpsc.data.Student;

public class FileService {

    private final static FileService fileService = new FileService();

    private FileService () {}

    public static FileService getInstance(){
        return fileService;
    }

    public void writePerson(PersonInSchool personInSchool) {


    }
}
