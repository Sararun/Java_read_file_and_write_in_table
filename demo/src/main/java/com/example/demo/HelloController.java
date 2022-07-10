//Саросек В.Е. 007сб2
package com.example.demo;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {
    //Объявления переменных, таблицы и столбцов. Также листа с возможностью изменения в нём информации
    private ObservableList<Record> records = FXCollections.observableArrayList();
    @FXML
    private TextField input;
    @FXML
    private TableView<Record> table;
    @FXML
    private TableColumn<Record, Integer> idColumn;
    @FXML
    private TableColumn<Record, String> dataColumn;
    @FXML
    private TableColumn<Record,String> lenColumn;
    @FXML
    int i = 1;
    //чистка
    @FXML
    protected void clearTable() {
        i=1;
        this.table.getItems().clear();
    }

//Заполнение таблицы
    @FXML
    protected void fillCell() {
        this.idColumn.setCellValueFactory(new PropertyValueFactory("id"));
        this.dataColumn.setCellValueFactory(new PropertyValueFactory("chunk"));
        this.lenColumn.setCellValueFactory(new PropertyValueFactory("data"));
        clearTable();
        String path = this.input.getText();

        String[] pa = path.split("\\.");
        String ext = pa[1];
        if(Objects.equals(ext, "png")) {
            getInfoFromFilePNG();
        }
        else {
            getInfoFromFileBMP();
        }

    }
    //Тут методика заполнения
    private void getInfoFromFileBMP(){
        try{
            FileInputStream fin = new FileInputStream(this.input.getText());
            DataInputStream dt = new DataInputStream(fin);
            byte[] header_bm = new byte[2];

            dt.read(header_bm, 0, 2);
            String h = new String(new char[] {(char)header_bm[0], (char)header_bm[1]});
            if(!h.equals("BM")) {
                throw new IOException();
            }
            dt.skipBytes(12);
            //int headerSize = dt.readInt();
            byte[] asd = new byte[4];
            dt.read(asd, 0, 4);
            int headerSize = Byte.toUnsignedInt(asd[0]);
            String version = switch (headerSize) {
                case 12 -> "CORE";
                case 40 -> "V3";
                case 108 -> "V4";
                case 124 -> "V5";
                default -> "unknown";
            };
            table.getItems().add(new Record(i, version, Integer.toString(headerSize)));
            i++;
            byte[] weight=new byte[4];
            dt.read(weight, 0, 4);
            int we = Byte.toUnsignedInt(weight[0]);
            table.getItems().add(new Record(i, "ширина", Integer.toString(we)));
            byte[] height=new byte[4];
            dt.read(height, 0, 4);
            int he = Byte.toUnsignedInt(height[0]);
            table.getItems().add(new Record(i, "высота", Integer.toString(he)));
            dt.skipBytes(24);//Это пропуск для colors in color table
            byte[] colors_in=new byte[4];
            dt.read(colors_in, 0, 4);
            int ci=Byte.toUnsignedInt(colors_in[0]);
            table.getItems().add(new Record(i,"Размер таблицы цветов",Integer.toString(ci)));

//            byte[] table_color=new byte[1000];
//            dt.read(table_color, 0, 1000);
//            int tb = Byte.toUnsignedInt(table_color[0]);
//            table.getItems().add(new Record(i, "высота", Integer.toString(tb)));




            /*if(!Objects.equals(header_bm.toString(), "BM"))
                return;
            byte[] signature=new byte[4];
            String sign;
            int chunkleght;
            dt.read(header_bm, 0,2);
            do{
                chunkleght=readInt();
                dt.read(signature, 0 ,4);
                sign=new String(signature);
                dt.skipBytes()
            }*/

        }catch (IOException var7) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText((String)null);
            alert.setContentText("Ошибка в пути файла!");
            alert.showAndWait();
        }
    }
    private void getInfoFromFilePNG() {
        try {
            FileInputStream fin = new FileInputStream(this.input.getText());
            DataInputStream dt = new DataInputStream(fin);
            byte[] header= new byte[8];
            byte[] signature = new byte[4];
            String sign;

            int chunklegth;
            dt.read(header, 0, 8);
            do{
                chunklegth=dt.readInt();
                dt.read(signature, 0, 4);
                sign=new String(signature);
                dt.skipBytes(chunklegth+4);
                table.getItems().add(new Record(i, sign, Integer.toString(chunklegth)));
                i++;


            }while(!sign.equals("IEND"));

            fin.close();
            dt.close();

        } catch (IOException var7) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("ВНИМАНИЕ!");
            alert.setHeaderText((String)null);
            alert.setContentText("Ошибка в пути файла!");
            alert.showAndWait();
        }
    }
}
