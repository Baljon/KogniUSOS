package pl.kognitywistyka.app.security;

import pl.kognitywistyka.app.user.Pokropski;
import pl.kognitywistyka.app.user.User;

/**
 * Created by wikto on 19.06.2017.
 */
public class AuthenticationService {

    private User currentLoginInfo;

    public static Pokropski getCurrentLoginInfo() {
        //TODO
        return new Pokropski("0","My","Name", "");
    }

    public void setCurrentLoginInfo(User currentLoginInfo) {
        this.currentLoginInfo = currentLoginInfo;
    }

    public static boolean isAuthenticated() {
        //TODO
        return false;
    }

    public static boolean logout() {
        return true;
    }

    public static boolean isAdmin() {
        return true;
    }

    //TODO

}
