package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;


public class Controller implements Initializable {
    @FXML
    public TextField inputText;
    public Button clickbutton;
    public TextArea outputText;
    public ImageView editbutton;
    public Menu openrecent;
    public ListView dictList;
    public Button add;
    public ImageView deletebutton;

    public  TreeMap<String,String> dictionary;
    public String dictname;


    public void addWordScene(ActionEvent event) throws IOException {
        Parent addParent = FXMLLoader.load(getClass().getResource("addWord.fxml"));
        Scene addScene =new Scene(addParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(addScene);
        window.setWidth(960);
        window.setHeight(600);
        window.show();
    }


    // working on it
    public void addWord(String word, String meaning){
        dictionary.put(word,meaning);
        updateListView();
        updateToFile();

    }

    // demo addword
    // demo sucessfully xD
    public void demo(ActionEvent event){
        dictionary.put(" demohere", "lua dao day dung tin");
        // đúng ra là phải dùng ObservableList để setItem cho ListView, do hàm getItem.add chỉ là copy dữ liệu
        // nếu dùng ObservableList thì chỉ cần addItem là ListView sẽ tự update khi OL thay đổi
        updateListView();
    }

    //done
    // set editable for the word's meaning
    public void edit(MouseEvent mouseEvent){
        if(outputText.isEditable()){
            outputText.setEditable(false);
            editMeaning();
        }else {
            outputText.setEditable(true);
        }
    }

    //done
    public void delete(MouseEvent event){
        String word;
        word = " " + inputText.getText();
        dictionary.remove(word);
        outputText.clear();
        inputText.clear();
        updateListView();
        updateToFile();

    }

    //done
    private void updateListView() {
        dictList.getItems().clear();
        for (Map.Entry<String,String> entry: dictionary.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            dictList.getItems().add(key);
        }
    }

    //done
    public void editMeaning(){
        String word;
        String meaning;
        word = " " + inputText.getText();
        meaning = outputText.getText();
        dictionary.replace(word,meaning);
        updateToFile();
    }

    //done
    private void updateToFile() {
        try {
            FileWriter writer = new FileWriter(getfinalpath("src/sample/listDictionary/textfield"));
            BufferedWriter buffer = new BufferedWriter(writer);
            buffer.write(" # " + dictname + " # "); // follow the format
            for (Map.Entry<String,String> entry: dictionary.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                buffer.write(" { " + key + " ; " + value + " } "); // follow the format
                buffer.newLine();
            }
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //done // find the meaning and show it on right textarea
    public void searching(ActionEvent event){
        String input = inputText.getText();
        String meaning;
        meaning = getMeaning(input);
        outputText.setEditable(true);
        outputText.setText(meaning);
        outputText.setEditable(false);
    }

    //done
    public String getMeaning(String word){
        String notfound = "Word not found!";
        word = " " + word;
        Set<String> keys = dictionary.keySet();
        for (String key : keys ){
            if (key.equals(word)) return dictionary.get(key);
        }
        return notfound;
    }

    //initialize
    public void getDict(){
        try {
            Dict dict = new Dict();
            Scanner scanner = new Scanner(new File(getfinalpath("src/sample/listDictionary/textfield")));
            dictionary = dict.read(new File(getfinalpath("src/sample/listDictionary/textfield")));
            dictname = dict.getdictname(scanner);
            for (Map.Entry<String,String> entry: dictionary.entrySet()) {
                String key   = entry.getKey();
                String value = entry.getValue();
                dictList.getItems().add(key);
//                content.add(key);
            }

        }catch (FileNotFoundException e){
            System.out.println("File not found . . . ");
            e.printStackTrace();
        }
    }


    // still working on it, but have no idea :(
    // choicebox / choicedialog ?
    public void openFile(Event event){
        String filename = "da";
        System.out.println("select file : " + filename );
    }

    //done
    //list all file in folder listDictionary
    public void listFile(File dir) {
        int i = 0;
        String getname;
        File[] files = dir.listFiles();
        for (File file: files) {
            getname = file.getName();
            openrecent.getItems().add(new MenuItem(getname));
            i++;
        }
    }

    public String getfinalpath(String getfile) {
        File file = new File(getfile);
        String pathname = file.getAbsolutePath();

        String out = "";
        for (int i = 0 ; i < getfile.length() ; i++){
            if (getfile.charAt(i) ==  '\\'){
                out = out + "/";
            }else out = out + getfile.charAt(i);
        }
        return  out;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getDict();
        listFile(new File(getfinalpath("src/sample/listDictionary/")));

    }

}
