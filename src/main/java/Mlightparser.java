
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
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
public class Mlightparser {
    public int varcount;
    public int constcount;
    public ArrayList<Double> C;
    public ArrayList<Double>B;
    public String ctype;
    public String vartype;
    public ArrayList<Integer>LB;
    public ArrayList<Integer>UB;
    public boolean max;
    File F;
    
    public Mlightparser(File f){
        F=f;
        C = new ArrayList();
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
            varcount=C.size();
            while (true) {
                tmp=scanner.nextLine();
                if(tmp.contains("b"))break;
            }
            del=tmp.split(d);
            for(int i=1; i<del.length;i++){
                B.add(Double.parseDouble(del[i]));
            }
            constcount=B.size();
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
            while(!tmp.contains("["))
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
            System.out.println("preloading is done in:"+(System.currentTimeMillis()-tim)+"ms. Total variables: "+C.size());
	} catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void buildconstr(MPSolver solver, MPVariable [] vars){
        try {
            long tim=System.currentTimeMillis();
            double infinity = java.lang.Double.POSITIVE_INFINITY;
            Scanner scanner = new Scanner(F);
            String tmp=scanner.nextLine();
            String d="[\\[;\\] ]";
            String[]del;
            while(!tmp.contains("a"))
                tmp=scanner.nextLine();
            int i=0;
            while (true) {
                del=tmp.split(d);
                int start=0;
                if(del[0].contains("b"))break;
                if(del[0].contains("a"))start++;
                double ub=0,lb=0;
                if(ctype.charAt(i)=='S'){
                    ub=B.get(i);
                    lb=ub;
                }
                if(ctype.charAt(i)=='U'){
                    ub=B.get(i);
                    lb=-infinity;
                }
                if(ctype.charAt(i)=='L'){
                    lb=B.get(i);
                    ub=infinity;
                }
                MPConstraint constr = solver.makeConstraint(lb, ub);
                int ii=0;
                for(int j=start; j<del.length;j++){
                    if(del[j].length()>0){
                        double c = Double.parseDouble(del[j]);
                        if(c!=0){
                            constr.setCoefficient(vars[ii], c);
                        }
                        ii++;
                    }
                }
                tmp=scanner.nextLine();
                i++;
            }
            scanner.close();
            System.out.println("constraint building is done in:"+(System.currentTimeMillis()-tim)+"ms. Total constraints: "+i);        
	} catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
    
    
    
    }

}
