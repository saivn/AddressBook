package controll;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.File;
import controll.Controller;

/**
 * Created by sasha on 04.07.2017.
 */
public class MenuController {
    Controller controller;

    File OpenFile() {
        String[] fileMask ={".txt",".csv"};
        String titleWindow = "Открытие базы данных из файла";
        String filter = "All text files";
        File fileName = FileDate.openFileDialog(fileMask, titleWindow, filter, "openFile");
        return fileName;
    }

    void WriteFile() {
        String[] fileMask ={"txt",""};
        String titleWindow = "Запись базы в файл";
        String filter = "All txt file";

        File fileName = FileDate.openFileDialog(fileMask, titleWindow, filter, "writeFile");
        FileDate.writeFile(controller.getHumanData(), fileName.getName());
    }

    void FileExit() {
        Platform.exit();
        System.exit(0);
    }

    void AboutMy() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе");
        alert.setHeaderText("Контакты");
        alert.setContentText("Куку");
        alert.showAndWait();
    }
}
