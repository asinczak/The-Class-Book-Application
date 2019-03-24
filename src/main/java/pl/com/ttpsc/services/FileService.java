package pl.com.ttpsc.services;

import pl.com.ttpsc.data.ListOfPersonsInSchool;
import pl.com.ttpsc.data.PersonInSchool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class FileService {

    private static FileService fileService;

    private FileService () {}

    public static FileService getInstance(){
        if (fileService == null){
            fileService = new FileService();
        }
        return fileService;
    }

    private static final String FILE_LIST_OF_PERSONS = "List_of_persons.xml";
    File file = new File(FILE_LIST_OF_PERSONS);

    public void writeFileListOfPersons () {

//        ClassLoader classLoader = getClass().getClassLoader();
//           File file = new File(classLoader.getResource(FILE_LIST_OF_PERSONS).getFile());


        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ListOfPersonsInSchool.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(ListOfPersonsInSchool.getInstance(), file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public ListOfPersonsInSchool readFileListOfPersons (){
        ListOfPersonsInSchool personsInSchool = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ListOfPersonsInSchool.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            personsInSchool = (ListOfPersonsInSchool) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return personsInSchool;
    }


}
