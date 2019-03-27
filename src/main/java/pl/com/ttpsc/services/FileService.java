package pl.com.ttpsc.services;


public class FileService {

    private static FileService fileService;

    private FileService () {}

    public static FileService getInstance(){
        if (fileService == null){
            fileService = new FileService();
        }
        return fileService;
    }


}
