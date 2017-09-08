package controll;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Created by sasha on 04.07.2017.
 */
public class AlertControll {


    public void alertMarkRecord(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Последнее китайское предупреждение");
        alert.setHeaderText("Внимание!");
        alert.setContentText("Отметте запись в таблице");

        alert.showAndWait();
    }

    public boolean alertYesDeletRecord(Controller controller, int i){
        String  headerText = "Хотите удалить запись: \n";
        headerText += controller.getTableHumans().getItems().get(i).getFamyli() + " ";
        headerText += controller.getTableHumans().getItems().get(i).getName();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Окно удаления");
        alert.setHeaderText(headerText);
        alert.setContentText("Выберите кнопу");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        }
        return false;
    }
}
