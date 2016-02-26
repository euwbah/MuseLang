import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.*;

import javax.xml.soap.Node;
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.regex.Matcher;

public class MuseLangEnvironment extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private CodeArea codeArea;
    private StyleClassedTextArea console;

    private StyleSpansBuilder<Collection<String>> styleSpansBuilder;

    @Override
    public void start(Stage primaryStage) {

        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        codeArea.setStyle("-fx-background-color: rgba(255, 255, 255, 0.4)");

        console = new StyleClassedTextArea();
        console.setEditable(false);

        console.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

        codeArea.richChanges().subscribe(change -> {
            int charposition = change.getPosition();//Use this
        });

        codeArea.getSelection().getStart();//use this
        codeArea.getSelection().getEnd();//use this


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

    private void startStylingFrom(int charCount, StyleSpansBuilder<Collection<String>> spansBuilder) {
        spansBuilder.add(Collections.emptyList(), charCount);
    }
    private void styleAs(String cssStyleClass, StyleSpansBuilder<Collection<String>> spansBuilder, int forHowManyChars) {
        spansBuilder.add(Collections.singleton(cssStyleClass), forHowManyChars);
    }
    private StyleSpans<Collection<String>> createStyle(StyleSpansBuilder<Collection<String>> spansBuilder) {
        return spansBuilder.create();
    }
}
