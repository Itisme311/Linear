/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author itism
 */
public class Logger {
    File file;
    FileWriter fwr;
    public Logger (String path) throws IOException{
        file = new File(path);
        System.out.println(path);
        file.createNewFile();
        fwr=new FileWriter(file);
    }
    public Logger () {
        file=null;
    }
    public void write(String text) throws IOException{
        System.out.println(text);
        if(file!=null){fwr.append(text);
        fwr.flush();}
    }
    public void close() throws IOException{
        if(file!=null)fwr.close();
    }
}
