package pl.kognitywistyka.app.user;

import pl.kognitywistyka.app.user.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by wikto on 19.06.2017.
 */
@Entity
@DiscriminatorValue("POKROPSKI")
public class Pokropski extends User {

    public Pokropski() {

    }

}
