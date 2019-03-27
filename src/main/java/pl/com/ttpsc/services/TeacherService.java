package pl.com.ttpsc.services;

import pl.com.ttpsc.data.Roles;

public class TeacherService extends UserService {


    public void createTeacher (){
        UserService.addUserToTheDataBase(Roles.TEACHER);
    }
}
