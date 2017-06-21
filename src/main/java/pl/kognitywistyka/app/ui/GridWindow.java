package pl.kognitywistyka.app.ui;

import com.vaadin.ui.Grid;
import pl.kognitywistyka.app.course.Course;

/**
 * Created by wikto on 21.06.2017.
 */
public abstract class GridWindow<T> extends CenteredWindow {

    private Grid<T> grid;

    public abstract void updateGrid();

}
