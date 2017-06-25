package pl.kognitywistyka.app.reporting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RJ on 2017-06-25.
 */
public class FACULTY_CONST {

    private static List<String> facultyNames = new ArrayList<>();
    static {
        facultyNames.add("MIM");
        facultyNames.add("PSYCH");
        facultyNames.add("IF");
    }

    public static List<String> getFacultyNames() {
        return facultyNames;
    }
}
