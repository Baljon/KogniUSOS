package pl.kognitywistyka.app.reporting;

import java.util.ArrayList;
import java.util.List;

/**
 * Please do not pay much attention to this class. It was created for testing purposes mostly and will be deprecated.
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
