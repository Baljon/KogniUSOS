package pl.kognitywistyka.app.user;

import javax.persistence.*;

/**
 * Created by wikto on 19.06.2017.
 */

//This class should supposedly be deprecated. We will hopefully never need a common denominator for students and admins.
@Entity
@Inheritance
@DiscriminatorColumn(name = "TYPE")
@Table(name = "USER")
public abstract class User {

    @Id
    @Column(name = "USER_ID", nullable = false)
    private String id; //should be PESEL number or album number or USOS id

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "PASSWORD", nullable = false)
    private String password;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public abstract String toString();

    public boolean equals(User user) {
        return getId().equals(user.getId());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean confirmPassword(String hashedPassword) {
        if(password.equals(hashedPassword)) return true;
        else return false;
    }
}
