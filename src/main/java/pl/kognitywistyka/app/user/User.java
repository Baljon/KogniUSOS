package pl.kognitywistyka.app.user;

import javax.persistence.*;

/**
 * Created by wikto on 19.06.2017.
 */

//This class should supposedly be deprecated. We will hopefully never need a common denominator for students and admins.
@Entity
@Inheritance
@DiscriminatorColumn(name = "USER_TYPE")
@Table(name = "USERS")
public abstract class User {

    @Id
    @Column(name = "ID", nullable = false)
    private String id; //should be PESEL number or album number or USOS id

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;


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

    public abstract boolean equals(User user);
}
