package pl.kognitywistyka.app.security;

import pl.kognitywistyka.app.user.Student;
import pl.kognitywistyka.app.user.User;

/**
 * Created by wikto on 19.06.2017.
 */
public class AuthenticationService {

    private User currentLoginInfo;

    public static User getCurrentLoginInfo() {
        //TODO
        return null;
    }

    public void setCurrentLoginInfo(User currentLoginInfo) {
        this.currentLoginInfo = currentLoginInfo;
    }

    public static boolean isAuthenticated() {
        //TODO
        return false;
    }

    public static void logout() {
    }

    public static boolean isAdmin() {
        return true;
    }

    //TODO

}
