package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Roles;

public class StudentService extends UserService {

    public void addStudentToDataBase () {
        UserService.addUserToTheDataBase(Roles.STUDENT);
    }


    public void addStudentToClass() {

    }
}
