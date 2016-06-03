/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author davtir
 */
public class Media {
    private String _filename;
    private byte[] _data;
    
    public Media(String filename, byte[] data) {
        _filename = filename;
        _data = data;
    }
    
    public byte[] getData() {
        return _data;
    }
    
    public String getFilename() {
        return _filename;
    }
    
    public void setData(byte[] data) {
        _data = data;
    }
    
    public void setFilename(String filename) {
        _filename = filename;
    }
}
