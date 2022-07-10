
//Саросек В.Е. 007сб2
package com.example.demo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
//Тут модификаторы для создания и редактирования записи в стоблцах
public class Record {
    private SimpleIntegerProperty id;
    private SimpleStringProperty chunk;

    private SimpleStringProperty data;

    Record(Integer id, String chunk, String data) {
        this.id = new SimpleIntegerProperty(id);
        this.chunk = new SimpleStringProperty(chunk);
        this.data =new SimpleStringProperty(data);

    }

    public Integer getId() {
        return this.id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getChunk() {
        return this.chunk.get();
    }

    public void setChunk(String chunk) {
        this.chunk.set(chunk);
    }

    public String getData(){return this.data.get();}
    public void setData(String data){this.data.set(data);}
}
