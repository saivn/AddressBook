package controll;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import model.Human;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Optional;

import static java.lang.Character.toUpperCase;

public class Controller {

    private boolean flagYesSearch = false;
    private int indxLastTabHumans = 0;
    public static final String alp = "абвгдежзиклмнопрстуфхцчшщэюя";
    private StringBuffer searchString = new StringBuffer();
    private ObservableList<Human> humanData = FXCollections.observableArrayList();
    private String stylesearch;
    private String stylesearchImg;
    private String stylesTabHumans;
    private String styletab, stylePaneView;
    private boolean flagAddRecord = false;
    @FXML
    private CustomTextField famTextEdit, namTextEdit, parTextEdit;
    @FXML
    private Label infFam, infNam, infParnt;
    @FXML
    private Pane infImgHuman, paneCloseEditRecord;
    @FXML
    private TextField textSearch;
    @FXML
    private TableColumn<Human, String> familyTableColumn,
            nameTableColumn, parentTableColumn;
    @FXML
    private TableView<Human> tableHumans;
    @FXML
    private TabPane tabHumans;
    @FXML
    private Pane paneView;

    MenuController menuController;
    AlertControll alertControll;
    ImageView imageView;

    @FXML
    private void initialize() {
        menuController = new MenuController();
        alertControll = new AlertControll();
    }

    public void openAdressBook(File file) {

        setHumanData(FileDate.readFile(file));
        setCellValueFactory();
        setStylesAdressBook();
        setInf(0);

        tableHumans.getSelectionModel().select(0);  //подсветил первую строчку по умолчанию
        infImgHuman.setCursor(Cursor.DEFAULT);

        setupClearButtonField(famTextEdit); //делаем чтобы в поле ввода появился крестик очистки текста
        setupClearButtonField(namTextEdit); //делаем чтобы в поле ввода появился крестик очистки текста
        setupClearButtonField(parTextEdit); //делаем чтобы в поле ввода появился крестик очистки текста
    }

    private void setCellValueFactory() {
        familyTableColumn.setCellValueFactory(new PropertyValueFactory<Human, String>("famyli"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Human, String>("name"));
        parentTableColumn.setCellValueFactory(new PropertyValueFactory<Human, String>("patron"));

        setDataTabHumans(0);  //формируем tableview
    }

    private void setStylesAdressBook() {
        stylesearchImg = imageView.getStyle();
        stylePaneView = paneView.getStyle();
        stylesearch = getTextSearch().getStyle();
        stylesTabHumans = tabHumans.getSelectionModel().getSelectedItem().getStyle();
    }

    private void setupClearButtonField(CustomTextField customTextField) {
        try {
            Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
            m.setAccessible(true);
            m.invoke(null, customTextField, customTextField.rightProperty());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSelection(Event event) {
        int i = tabHumans.getSelectionModel().getSelectedIndex();
        System.out.println("onSelect " + i);
        setDataTabHumans(i);
    }

    @FXML
    public void onMouseSelectionTableView(MouseEvent mouseEvent) {
        int i = tableHumans.getSelectionModel().getSelectedIndex();
        setInf(i);
    }

    public void onKeyPressedTableView(KeyEvent keyEvent) {

        if ((keyEvent.getCode() == KeyCode.UP) || (keyEvent.getCode() == KeyCode.DOWN)) {
            int i = tableHumans.getSelectionModel().getSelectedIndex();
            i = (keyEvent.getCode() == KeyCode.UP) ? i - 1 : i + 1;           //другой индекс чем у onMouseSelectionTableView
            if (i >= 0 && i <= tableHumans.getItems().size() - 1) setInf(i);    //ограничил чтобы не вылез эксепшен
        }
    }


    @FXML
    public void onSearchReleased(KeyEvent keyEvent) {
        famTextEdit.setVisible(false);
        namTextEdit.setVisible(false);
        parTextEdit.setVisible(false);

        searchString.append(keyEvent.getText()); //склеиваем строку поиска
        KeyCode key = keyEvent.getCode();

        if ((key == KeyCode.BACK_SPACE) || (key == KeyCode.DELETE)) {  //если удаляем
            int leng = searchString.length();
            if (leng != 0) {
                searchString.deleteCharAt(leng - 1);
            }
        }
        System.out.println("onSearchReleas " + keyEvent.getText());
        searchInTabPane(searchString);

    }

    public void searchInTabPane(StringBuffer searchString) {

        String str = searchString.toString().toLowerCase().replaceAll("[\\x00-\\x1f]", ""); //удалил управляющие символы типа Ентер
        flagYesSearch = false;
        tabHumans.getSelectionModel().select(indxLastTabHumans);
        tabHumans.getSelectionModel().getSelectedItem().setStyle(stylesTabHumans);
        System.out.println("searchInTab " + str);


        for (int i = 0; i < humanData.size(); i++) {         //перебор всех фамилий

            if (humanData.get(i).getFamyli().toLowerCase().startsWith(str.toLowerCase())) {
                System.out.println("searchintabpane Yes!!");
                indxLastTabHumans = alp.indexOf(str.toLowerCase().charAt(0));
                tabHumans.getSelectionModel().select(indxLastTabHumans);
                tabHumans.getSelectionModel().getSelectedItem().setStyle(
                        "-fx-background-color: #8DB600;" + //apple green;" +
                                "-fx-rotate: 90;"
                );

                System.out.println("search " + indxLastTabHumans);
                setDataTabHumans(indxLastTabHumans);                    //формируем tableview
                searchInTableHumans(searchString);         //и теперь ищем внутри tableview
                flagYesSearch = true;
                break;
            }

        }

        if (!flagYesSearch) {       //не нашел поменял цвет фона

            getTextSearch().setStyle("-fx-background-color: LightPink;" +
                    "-fx-background-radius: 20;");
            imageView.setStyle("-fx-image: url('images/circle.png');");

        } else {
            getTextSearch().setStyle(stylesearch); //восстановил стиль
            if (stylesearchImg != null) imageView.setStyle(stylesearchImg);
        }
    }

    public void searchInTableHumans(StringBuffer searchString) {
        ObservableList<Human> humanDataSearch = tableHumans.getItems();
        String str = searchString.toString().toLowerCase();
        System.out.println("! " + str);

        for (int i = 0; i < humanDataSearch.size(); i++) {         //перебор всех фамилий
            if (humanDataSearch.get(i).getFamyli().toLowerCase().startsWith(str)) {
                System.out.println("Yes!!");
                tableHumans.getSelectionModel().select(i);  //выделили строку
                tableHumans.scrollTo(i);                    //прокрутил
                setInf(i);
                flagYesSearch = true;
                break;
            }
        }

        if (!flagYesSearch) {       //не нашел поменял цвет фона
            getTextSearch().setStyle("-fx-background-color: LightPink;" +
                    "-fx-background-radius: 20;");
        } else {
            getTextSearch().setStyle(stylesearch); //восстановил стиль
        }

    }


    private void setInf(int indx) {
        infFam.setText(tableHumans.getItems().get(indx).getFamyli());
        infNam.setText(tableHumans.getItems().get(indx).getName());
        infParnt.setText(tableHumans.getItems().get(indx).getPatron());

        String styleImgFace = "-fx-background-image: url('images/" +
                tableHumans.getItems().get(indx).getImageFace() + "');";
        infImgHuman.setStyle(styleImgFace);

    }

    private void setDataTabHumans(int selectIndx) {
        ObservableList<Human> humanDataTemp = FXCollections.observableArrayList();
        char famBeginChar = alp.charAt(selectIndx);
        int sizeHumanData = humanData.size();

        for (int i = 0; i < sizeHumanData; i++) {
            char famBeginCharHuman = humanData.get(i).getFamyli().substring(0).charAt(0);
            if (toUpperCase(famBeginChar) == toUpperCase(famBeginCharHuman)) {
                humanDataTemp.add(humanData.get(i));
            }
        }
        tableHumans.getItems().clear();
        tableHumans.setItems(humanDataTemp);  //формирую viewTab
    }


    public void onMouseImageView(MouseEvent mouseEvent) { //тык на лупу
        System.out.println("Tis!" + flagYesSearch);

        if (!flagYesSearch) {               // если нет такой строки, то удалим строку поиска
            System.out.println("eto " + searchString.toString() + "d " + searchString.length());
            searchString.delete(0, searchString.length());
            textSearch.setText(searchString.toString());

        }
    }


    public void onAddRecord(ActionEvent actionEvent) {
        clearStringSearch();

        System.out.println("add rec " + searchString.toString());
        paneCloseEditRecord.setVisible(true);
        getPaneView().setStyle("-fx-background-color: #CCFF00");  //желто-зеленый
        famTextEdit.setVisible(true);
        namTextEdit.setVisible(true);
        parTextEdit.setVisible(true);
        flagAddRecord = true;

        String styleImgFace = "-fx-background-image: url('images/zero.png');";
        infImgHuman.setStyle(styleImgFace);
        infImgHuman.setCursor(Cursor.OPEN_HAND);
    }

    public void onDeleteRecord(ActionEvent actionEvent) {

        if (tableHumans.getSelectionModel().getSelectedIndex() >= 0) {
            int i = tableHumans.getSelectionModel().getSelectedIndex();
            if (alertControll.alertYesDeletRecord(this, i)) {
                DeletRecord();
            }
        } else {
            alertControll.alertMarkRecord();
        }
    }

    public void DeletRecord() {
        clearStringSearch();

        famTextEdit.setVisible(false);
        namTextEdit.setVisible(false);
        parTextEdit.setVisible(false);

        int i = tableHumans.getSelectionModel().getSelectedIndex();
        System.out.println("--->" + tableHumans.getItems().get(i).getFamyli());
        String fam = tableHumans.getItems().get(i).getFamyli();
        String nam = tableHumans.getItems().get(i).getName();
        String pat = tableHumans.getItems().get(i).getPatron();
        tableHumans.getItems().remove(i);
        deletRecordHumanData(fam, nam, pat);
    }

    public void deletRecordHumanData(String fam, String nam, String pat) {
        for (int i = 0; i < humanData.size(); i++) {         //перебор всех фамилий
            if (humanData.get(i).getFamyli().equals(fam))
                if (humanData.get(i).getName().equals(nam))
                    if (humanData.get(i).getPatron().equals(pat)) {
                        System.out.println("Yes delet record!!");
                        humanData.remove(i);

                        break;
                    }
        }
    }

    public void onChangRecord(ActionEvent actionEvent) {
        clearStringSearch();

        if (tableHumans.getSelectionModel().getSelectedIndex() >= 0) {

            paneCloseEditRecord.setVisible(true);
            infImgHuman.setCursor(Cursor.OPEN_HAND);
            getPaneView().setStyle("-fx-background-color: #CCFF00");  //желто-зеленый
            setRecords();
        } else {
            alertControll.alertMarkRecord();
        }

    }

    public void setRecords() {
        int i = tableHumans.getSelectionModel().getSelectedIndex();
        System.out.println("--->" + tableHumans.getItems().get(i).getFamyli());
        String fam = tableHumans.getItems().get(i).getFamyli();
        String nam = tableHumans.getItems().get(i).getName();
        String pat = tableHumans.getItems().get(i).getPatron();

        System.out.println("Chang!");
        famTextEdit.setVisible(true);
        namTextEdit.setVisible(true);
        parTextEdit.setVisible(true);
        famTextEdit.setText(fam);
        namTextEdit.setText(nam);
        parTextEdit.setText(pat);

    }


    public void closeEditRecord() {
        if (!flagAddRecord) {
            String fam = infFam.getText();
            String nam = infNam.getText();
            String pat = infParnt.getText();
            deletRecordHumanData(fam, nam, pat);
        }

        famTextEdit.setVisible(false);       //скрыл поле редактирования
        namTextEdit.setVisible(false);       //скрыл поле редактирования
        parTextEdit.setVisible(false);       //скрыл поле редактирования

        infFam.setText(famTextEdit.getText());   //присвоил поле редактиррования
        infNam.setText(namTextEdit.getText());   //присвоил поле редактиррования
        infParnt.setText(parTextEdit.getText());   //присвоил поле редактиррования

        String[] imgFace = infImgHuman.getStyle().split("'", 3); //вычленил название файла - оно в одинарных кавычках
        String[] imgFace1 = imgFace[1].split("/", 2);
        humanData.add(new Human(infFam.getText(), infNam.getText(), infParnt.getText(), imgFace1[1])); //поменял данные

        indxLastTabHumans = alp.indexOf(infFam.getText().toLowerCase().charAt(0));  //вывел таблицу с нужной страницы

        System.out.println("indxLastTabHumans" + indxLastTabHumans);
        setDataTabHumans(indxLastTabHumans);
        searchString.delete(0, 1);

        String str = infFam.getText();
        searchString.append(str);
        searchInTabPane(searchString);

        flagAddRecord = false;
        getPaneView().setStyle("-fx-background-color: #CCFF99");
        paneCloseEditRecord.setVisible(false);
        infImgHuman.setCursor(Cursor.DEFAULT);
    }


    @FXML
    public void onImgFaceOpenFileDialog(MouseEvent mouseEvent) {
        String[] fileMask = {"jpg", "png"};
        String titleWindow = "Открытие фото для паспорта";
        String filter = "All image";
        File file = FileDate.openFileDialog(fileMask, titleWindow, filter, "openFile");
        System.out.println("fiiile " + file.getAbsolutePath());
        String styleImgFace = "-fx-background-image: url('images/" + file.getName() + "');";  //сменил

        infImgHuman.setStyle(styleImgFace);
    }

    @FXML
    public void noClosedEditRecord(MouseEvent mouseEvent) {
        setRecords();
        closeEditRecord();
    }

    public Pane getPaneView() {
        return paneView;
    }

    public void onDubleClikedForChangRecord(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            ActionEvent actionEvent = null;
            onChangRecord(actionEvent);
        }
    }


    void clearStringSearch() {
        searchString.delete(0, searchString.length());
        textSearch.setText("");
        getTextSearch().setStyle(stylesearch); //восстановил стиль
        if (stylesearchImg != null)
            imageView.setStyle(stylesearchImg);
    }


    //--------------------Menu---------------------------
    @FXML
    public void onAboutMy(ActionEvent actionEvent) {
        menuController.AboutMy();
    }

    @FXML
    public void onFileExit(ActionEvent actionEvent) {
        menuController.FileExit();
    }

    @FXML
    public void onWriteFile(ActionEvent actionEvent) {
        menuController.WriteFile();
    }

    @FXML
    public void onOpenFile(ActionEvent actionEvent) {
        openAdressBook(menuController.OpenFile());
    }
    //-----------------endMenu---------------------------

    public TableView<Human> getTableHumans() {
        return tableHumans;
    }

    public ObservableList<Human> getHumanData() {
        return humanData;
    }

    public void setHumanData(ObservableList<Human> humanData) {
        this.humanData = humanData;
    }

    public TextField getTextSearch() {
        return textSearch;
    }

    public String getStyletab() {
        return styletab;
    }

    public void onSearch(ActionEvent actionEvent) {
    }
}
