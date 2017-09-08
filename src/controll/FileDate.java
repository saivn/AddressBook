package controll;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Human;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.readAllLines;

/**
 * Created by sasha on 03.07.2017.
 */
public class FileDate {


    public static File openFileDialog(String[] maskFile, String titleStr, String filterStr, String orOpenSave) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titleStr);
        fileChooser.getExtensionFilters().addAll(                       //показал расширения
                new FileChooser.ExtensionFilter(filterStr, "*.*"),
                new FileChooser.ExtensionFilter(maskFile[0].toUpperCase(), "*." + maskFile[0]),
                new FileChooser.ExtensionFilter(maskFile[0].toUpperCase(), "*." + maskFile[1])
        );


        Stage stage = new Stage();                                 //подготовил текущую директорию
        String userDirectoryString = System.getProperty("user.dir") + File.separator;

        System.out.println(userDirectoryString);

        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(userDirectory);

        File file = (orOpenSave.equals("openFile"))? fileChooser.showOpenDialog(stage) ://окно открытия файла
                                                     fileChooser.showSaveDialog(stage); //

        return file;
    }




    public static ObservableList<Human> readFile(File fileName) {
        ObservableList<Human> humanData = FXCollections.observableArrayList();

        List<String> lines = null;
        try {
            lines = readAllLines(Paths.get(fileName.getPath()), Charset.forName("windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        for (String line : lines) {
            String[] records = line.split(" ", 4);
            humanData.add(new Human(records[0], records[1], records[2], records[3]));
        }

        return humanData;
    }


    public static void writeFile(ObservableList<Human> humanData, String fileName) {
        ArrayList<String> linesRecord = new ArrayList<String>();
        Charset charset = Charset.forName("windows-1251");
        for (int i = 0; i < humanData.size(); i++) {
            String str = humanData.get(i).getFamyli() + " ";
            str += humanData.get(i).getName() + " ";
            str += humanData.get(i).getPatron() + " ";
            str += humanData.get(i).getImageFace();
            linesRecord.add(str);
        }

        try {
            Files.write(Paths.get(fileName), linesRecord, charset, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println(e);
        }

    }

}
