package pl.com.ttpsc.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "Student")
@XmlAccessorType (XmlAccessType.FIELD)
public class Student extends PersonInSchool {
}
