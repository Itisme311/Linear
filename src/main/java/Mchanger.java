
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author itism
 */
public class Mchanger {
    public Mchanger(File f) throws IOException{
        int varcount=0;
        int costrcount=0;
        String newpath=f.getAbsolutePath().replace(".m", ".dzn");
        File file = new File(newpath);
        FileWriter fwr;
        file.createNewFile();
        fwr=new FileWriter(file);
        
        long tim=System.currentTimeMillis();
        try {
            Scanner scanner = new Scanner(f);
            String tmp=scanner.nextLine();
            String d="[\\[;\\] ]";
            String[]del=tmp.split(d);
            varcount=del.length-1;
            while (true) {
                tmp=scanner.nextLine();
                if(tmp.contains("b"))break;
                costrcount++;
            }
            scanner.close();
            DecimalFormat DF= new DecimalFormat("0.0#####",DecimalFormatSymbols.getInstance( Locale.ENGLISH ));
            String out="n="+varcount+";\n"+"m="+costrcount+";\n";
            fwr.append(out);
            fwr.flush();
            scanner = new Scanner(f);
            tmp=scanner.nextLine();
            d="[\\[;\\] ]";
            del=tmp.split(d);
            out=del[0]+"["+DF.format(Double.parseDouble(del[1]));
            for(int i=2; i<del.length;i++){
                out+=","+DF.format(Double.parseDouble(del[i]));
            }
            out+="];\n";
            fwr.append(out);
            fwr.flush();
            out="a=[|";
            for(int j=0; j<costrcount; j++) {
                tmp=scanner.nextLine();
                del=tmp.split(d);
                int start=0;
                if(j==0)start++;
                else out="|";
                out+=del[start];
                for(int i=start+1; i<del.length;i++){
                    if(del[i].length()>0)
                        out+=", "+del[i];
                }
                if(j==costrcount-1)out+="|];";
                out+="\n";
                fwr.append(out);
                fwr.flush();
            }

            tmp=scanner.nextLine();
            del=tmp.split(d);
            out="b=["+del[1];
            for(int i=2; i<del.length;i++){
                out+=","+del[i];
            }
            out+="];\n";
            fwr.append(out);
            fwr.flush();
            tmp=scanner.nextLine();
            d="\"";
            del=tmp.split(d);
            out="ctype=["+del[1].charAt(0);
            for(int i=1; i<del[1].length();i++)
                out+=","+del[1].charAt(i);
            out+="];\n";
            fwr.append(out);
            fwr.flush();
            tmp=scanner.nextLine();
            del=tmp.split(d);
            out="vartype=["+del[1].charAt(0);
            for(int i=1; i<del[1].length();i++)
                out+=","+del[1].charAt(i);
            out+="];\n";
            fwr.append(out);
            fwr.flush();
            tmp=scanner.nextLine();
            while(!tmp.contains("["))
                tmp=scanner.nextLine();
            d="[\\[\\]]+";
            del=tmp.split(d);
            String[] tint=del[3].split(";");
            out="lb=["+tint[0];
            for(int i=1; i<tint.length;i++){
                out+=","+tint[i];
            }
            out+="];\n";
            fwr.append(out);
            fwr.flush();
            
            tint=del[5].split(";");
            out="ub=["+tint[0];
            for(int i=1; i<tint.length;i++){
                out+=","+tint[i];
            }
            out+="];\n";
            fwr.append(out);
            fwr.flush();
            scanner.close();



            System.out.println("Loading is done in:"+(System.currentTimeMillis()-tim)+"ms");
	} catch (FileNotFoundException e) {
            e.printStackTrace();
        }    
    
    
    
    }
}
