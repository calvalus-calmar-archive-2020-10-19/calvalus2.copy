package com.bc.calvalus.portal.client;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple dialog component.
 *
 * @author Norman Fomferra
 */
public class Dialog {
    private final String title;
    private final Widget dialogContents;
    private final ButtonType[] buttonTypes;
    private ButtonType selectedButtonType;
    private int selectedButtonIndex;
    private DialogBox dialogBox;
    private Button[] buttons;

    public enum ButtonType {
        OK("OK"),
        CANCEL("Cancel"),
        YES("Yes"),
        NO("No"),
        CLOSE("Close"),;

        private final String label;

        ButtonType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    public Dialog(String title, Widget dialogContents, ButtonType... buttonTypes) {
        this.title = title;
        this.dialogContents = dialogContents;
        this.buttonTypes = buttonTypes;
    }

    public static void ask(String title, Widget question, final Runnable action) {
        Dialog dialog = new Dialog(title, question, ButtonType.YES, ButtonType.NO) {
            @Override
            protected void onYes() {
                action.run();
                hide();
            }
        };
        dialog.show();
    }

    public static void info(String title, String htmlMessage) {
        new Dialog(title, new HTML(htmlMessage), ButtonType.CLOSE).show();
    }

    public static void info(String title, Widget... widgets) {
        if (widgets.length == 1) {
            new Dialog(title, widgets[0], ButtonType.CLOSE).show();
        } else {
            new Dialog(title, UIUtils.createVerticalPanel(widgets), ButtonType.CLOSE).show();
        }
    }

    public static void error(String title, String htmlMessage) {
        error(title, new HTML(htmlMessage));
    }

    public static void error(String title, Widget... widgets) {
        // use error logo here
        info(title, widgets);
    }

    public void show() {
        if (dialogBox == null) {
            initButtons();
            this.dialogBox = createDialogBox();
            dialogBox.setWidget(createMainPanel());
        }
        dialogBox.center();
        onShow();
    }

    public void hide() {
        onHide();
        dialogBox.hide();
    }

    public int getSelectedButtonIndex() {
        return selectedButtonIndex;
    }

    public ButtonType getSelectedButtonType() {
        return selectedButtonType;
    }

    public Button getButton(int buttonIndex) {
        return buttons[buttonIndex];
    }

    private void onButtonClicked(ButtonType buttonType, int buttonIndex) {
        selectedButtonType = buttonType;
        selectedButtonIndex = buttonIndex;
        if (buttonType == ButtonType.OK) {
            onOk();
        } else if (buttonType == ButtonType.CANCEL) {
            onCancel();
        } else if (buttonType == ButtonType.CLOSE) {
            onClose();
        } else if (buttonType == ButtonType.YES) {
            onYes();
        } else if (buttonType == ButtonType.NO) {
            onNo();
        }
    }

    protected void onOk() {
        hide();
    }

    protected void onClose() {
        hide();
    }

    protected void onCancel() {
        hide();
    }

    protected void onYes() {
        hide();
    }

    protected void onNo() {
        hide();
    }

    protected void onShow() {
    }

    protected void onHide() {
    }

    protected DialogBox createDialogBox() {
        final DialogBox dialogBox = new DialogBox();
        dialogBox.ensureDebugId("cwDialogBox");
        dialogBox.setText(title);
        dialogBox.setGlassEnabled(true);
        dialogBox.setAnimationEnabled(true);
        return dialogBox;
    }

    private VerticalPanel createMainPanel() {
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setSpacing(4);

        mainPanel.add(dialogContents);
        mainPanel.setCellHorizontalAlignment(dialogContents, HasHorizontalAlignment.ALIGN_CENTER);

        HorizontalPanel buttonRow = createButtonRow();
        mainPanel.add(buttonRow);
        mainPanel.setCellHorizontalAlignment(buttonRow, HasHorizontalAlignment.ALIGN_RIGHT);
        return mainPanel;
    }

    private HorizontalPanel createButtonRow() {
        final HorizontalPanel buttonRow = new HorizontalPanel();
        buttonRow.setSpacing(2);
        for (int i = 0; i < buttons.length; i++) {
            Button button = buttons[i];
            buttonRow.add(button);
            buttonRow.setCellHorizontalAlignment(button, HasHorizontalAlignment.ALIGN_RIGHT);
        }
        return buttonRow;
    }

    private void initButtons() {
        buttons = new Button[buttonTypes.length];
        for (int i = 0; i < buttonTypes.length; i++) {
            final ButtonType buttonType = buttonTypes[i];
            final int buttonIndex = i;
            final Button button = new Button(buttonType.getLabel(),
                                             new ClickHandler() {
                                                 public void onClick(ClickEvent event) {
                                                     onButtonClicked(buttonType, buttonIndex);
                                                 }
                                             });
            buttons[i] = button;
        }
    }
}
