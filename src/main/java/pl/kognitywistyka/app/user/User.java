package pl.kognitywistyka.app.user;

import javax.persistence.*;

/**
 * Basic entity class representing users. Extended by Student and Pokropski.
 * DB table user (single-table inheritance, discriminator column TYPE, values {student, pokropski}).
 */

@Entity
@Inheritance
@DiscriminatorColumn(name = "TYPE")
@Table(name = "user")
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
