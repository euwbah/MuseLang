import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.*;

import java.util.Collection;
import java.util.Collections;

public class MuseLangEnvironment extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private CodeArea codeArea;
    private StyleClassedTextArea console;

    private StyleSpansBuilder<Collection<String>> styleSpansBuilder;

    private EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if(event.isAltDown() && event.getCode() == KeyCode.R) {
                evalCode();
                event.consume();
            }
        }
    };

    @Override
    public void start(Stage primaryStage) {

        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        codeArea.setStyle("-fx-background-color: rgba(255, 255, 255, 0)");

        console = new StyleClassedTextArea();
        console.setEditable(false);

        console.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

        codeArea.richChanges().subscribe(change -> {
            
        });
        //codeArea.setStyleClass(from, to, "css style class");//use this


        SplitPane splitPane = new SplitPane();
        VirtualizedScrollPane<CodeArea> codePane = new VirtualizedScrollPane<>(codeArea);
        VirtualizedScrollPane<StyleClassedTextArea> consolePane = new VirtualizedScrollPane<>(console);
        splitPane.getItems().addAll(codePane, consolePane);

        codePane.setStyle("-fx-background-color: rgba(0, 0, 0, 0)");

        splitPane.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.4);" +
                        "-fx-effect: dropshadow(gaussian, white, 20, 0, 0, 0);" +
                        "-fx-background-insets: 40;"
        );

        splitPane.setDividerPositions(0.5);

        Scene scene = new Scene(splitPane, 800, 480);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("syntax-colours.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("MuseLang v0.01-alpha");
        primaryStage.show();
    }

    private void evalCode() {
        if(codeArea.getSelection() == null) {
            //Just the caret
            codeArea.getCaretPosition();
        }
        else {
            //Has selection
            codeArea.getSelection();
        }
    }
}
