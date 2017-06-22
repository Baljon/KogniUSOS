package pl.kognitywistyka.app.user;

import pl.kognitywistyka.app.user.User;

import javax.persistence.*;

/**
 * Created by wikto on 19.06.2017.
 */
@Entity
@DiscriminatorValue("POKROPSKI")
public class Pokropski extends User {
    //The only info we need about admins is needed for logging in --> id and password.
    //OK, added the name too, but IMHO we do not need it.

//    @Id
//    @Column(name = "POKROPSKI_ID")
//    private String pokropskiID;
//
//    @Column(name = "POKROPSKI_PASSWORD")
//    private String pokropskiPassword;
//
//    @Column(name = "POKROPSKI_NAME")
//    private String pokropskiName;

    public Pokropski() {}

    public Pokropski(String id, String firstName, String lastName, String password) {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
    }

    @Override
    public String toString() {
        return getId();
    }

    public boolean equals(User user) {
        return getId().equals(user.getId());
    }

//    public String getPokropskiID() {
//        return pokropskiID;
//    }
//
//    public void setPokropskiID(String pokropskiID) {
//        this.pokropskiID = pokropskiID;
//    }
//
//    public String getPokropskiPassword() {
//        return pokropskiPassword;
//    }
//
//    public void setPokropskiPassword(String pokropskiPassword) {
//        this.pokropskiPassword = pokropskiPassword;
//    }
//
//    public String getPokropskiName() {
//        return pokropskiName;
//    }
//
//    public void setPokropskiName(String pokropskiName) {
//        this.pokropskiName = pokropskiName;
//    }
}
