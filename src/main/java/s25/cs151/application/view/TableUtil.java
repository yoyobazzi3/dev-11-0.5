package s25.cs151.application.view;

import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

public final class TableUtil {
    public static <S,T> TableColumn<S,T> col(String title, Function<S,T> mapper) {
        TableColumn<S,T> c = new TableColumn<>(title);
        c.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<>(mapper.apply(p.getValue())));
        return c;
    }
    private TableUtil(){}
}
