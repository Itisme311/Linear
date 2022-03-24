
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author itism
 */
public class Mparser {
    public ArrayList<Double> C;
    public ArrayList<ArrayList<Double>>A;
    public ArrayList<Double>B;
    public String ctype;
    public String vartype;
    public ArrayList<Integer>LB;
    public ArrayList<Integer>UB;
    public boolean max;
    
    public Mparser(File f){
        C = new ArrayList();
        A = new ArrayList();
        B = new ArrayList();
        LB = new ArrayList();
        UB = new ArrayList();
        long tim=System.currentTimeMillis();
        try {
            Scanner scanner = new Scanner(f);
            String tmp=scanner.nextLine();
            String d="[\\[;\\] ]";
            String[]del=tmp.split(d);
            for(int i=1; i<del.length;i++){
                C.add(Double.parseDouble(del[i]));
            }
            while (true) {
                tmp=scanner.nextLine();
                del=tmp.split(d);
                int start=0;
                if(del[0].contains("b"))break;
                if(del[0].contains("a"))start++;
                ArrayList<Double>tmplist=new ArrayList();
                for(int i=start; i<del.length;i++){
                    if(del[i].length()>0)
                        tmplist.add(Double.parseDouble(del[i]));
                }
                A.add(tmplist);
            }
            for(int i=1; i<del.length;i++){
                B.add(Double.parseDouble(del[i]));
            }
            tmp=scanner.nextLine();
            d="\"";
            del=tmp.split(d);
            ctype=del[1];
            tmp=scanner.nextLine();
            del=tmp.split(d);
            vartype=del[1];
            tmp=scanner.nextLine();
            max=tmp.contains("-1");
            tmp=scanner.nextLine();
            d="[\\[\\]]+";
            del=tmp.split(d);
            String[] tint=del[3].split(";");
            for(int i=0; i<tint.length;i++){
                LB.add(Integer.parseInt(tint[i]));
            }
            tint=del[5].split(";");
            for(int i=0; i<tint.length;i++){
                UB.add(Integer.parseInt(tint[i]));
            }
            scanner.close();
            System.out.println("Loading is done in:"+(System.currentTimeMillis()-tim)+"ms");
	} catch (FileNotFoundException e) {
            e.printStackTrace();
        }    
    
    
    
    }
}
