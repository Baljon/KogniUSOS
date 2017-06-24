package pl.kognitywistyka.app.security;

import pl.kognitywistyka.app.service.StudentService;
import pl.kognitywistyka.app.user.Pokropski;
import pl.kognitywistyka.app.user.Student;
import pl.kognitywistyka.app.user.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by wikto on 19.06.2017.
 */
public class AuthenticationService {

    private static AuthenticationService instance;

    private User currentLoginInfo;

    public AuthenticationService() {
    }

    public boolean authenticate(String id, String password) {
        StudentService studentService = StudentService.getInstance();
//        List<User> retrievedUser = studentService.findById(id);
        List<User> retrievedUser = studentService.findById(id);
        if(!retrievedUser.isEmpty() && retrievedUser != null) {
            String hashedPassword = sha512(password);
            if(retrievedUser.get(0).confirmPassword(hashedPassword)) {
               setCurrentLoginInfo(retrievedUser.get(0));
               return true;
            }
            else return false;
        } else return false;
    }

    public User getCurrentLoginInfo() {
        //TODO
        return currentLoginInfo;
    }

    public void setCurrentLoginInfo(User currentLoginInfo) {
        this.currentLoginInfo = currentLoginInfo;
    }

    public boolean isAuthenticated() {
        return getCurrentLoginInfo() != null;
    }

    public boolean logout() {
        setCurrentLoginInfo(null);
        return true;
    }

    public boolean isAdmin() {
        if (getCurrentLoginInfo().getClass().equals(Student.class)) return false;
        else return true;
    }

    public static AuthenticationService getInstance() {
        if (instance == null) instance = new AuthenticationService();
        return instance;
    }

    /***
     * Credits to ilintar.
     * @param toHash
     * @return
     */
    public static String sha512(String toHash) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("SHA-512 hash not available.");
        }

        byte[] digest = md.digest(toHash.getBytes());

        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i< digest.length; i++) {
            hexString.append(Integer.toHexString(0xFF & digest[i]));
        }

        return hexString.toString();
    }
}
