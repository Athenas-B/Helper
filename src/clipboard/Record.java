/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clipboard;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author olda9
 */

@Entity
public class Record {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private long id;
    
    private String data;

    public Record(String data) {
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Record{" + "id=" + id + ", data=" + data + '}';
    }
    
}
