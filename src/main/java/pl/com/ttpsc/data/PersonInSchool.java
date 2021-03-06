package pl.com.ttpsc.data;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Guardian.class})
public class PersonInSchool {

   private String name;
   private String surname;
   private String whoIs;

   @XmlTransient
   private String login;
   @XmlTransient
   private String password;

        public String getName () {
            return name;
        }

        public String getSurname () {
            return surname;
        }

        public String getWhoIs () {
            return whoIs;
        }

        public String getLogin () {
            return login;
        }

        public String getPassword () {
            return password;
        }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname (String surname){
            this.surname = surname;
    }

    public void setWhoIs(String whoIs) {
        this.whoIs = whoIs;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public String toString() {
        return "" +getName()+" "+getSurname()+" "+getWhoIs();
    }
}
