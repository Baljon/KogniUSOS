package pl.kognitywistyka.app.ui;

import com.vaadin.ui.*;

/**
 * Created by wikto on 21.06.2017.
 */
public class TextWindow extends ItemWindow {

    private VerticalLayout middleLayer;
    private CssLayout nameLayout;
    private Label nameLabel;

    private String text;
    public TextWindow(String object) {
        setText(object);
        init();
    }
    private void init() {
        setSizeFull();

        //Initializing layout
        middleLayer = new VerticalLayout();
        middleLayer.setSizeFull();
        middleLayer.addStyleName("login-panel");

        init(middleLayer);

        middleLayer.setWidth("600px");
        middleLayer.setHeight("600px");

        //Initializing name
        nameLayout = new CssLayout();
        nameLayout.setSizeFull();

        nameLabel = new Label(getText());

        nameLayout.addComponent(nameLabel);
        middleLayer.addComponent(nameLayout);
        middleLayer.setComponentAlignment(nameLayout, Alignment.TOP_LEFT);
        middleLayer.setExpandRatio(nameLayout, 0.1f);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
