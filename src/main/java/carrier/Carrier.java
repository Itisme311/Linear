/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrier;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import excel.Excbuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author itism
 */
public class Carrier {
    public List <int[]> depend;
    public List <CarCube> cubes;
    public List <CarCube> orecubes;
    public CarCube[][][] arrcar;
    //public boolean[][] deptable;
    private double dx,dy,dz;
    private TreeSet<Double> OX;
    private TreeSet<Double> OY;
    private TreeSet<Double> OZ;
    public Excbuilder exc;

    public Carrier(Excbuilder exc) {
        this.cubes = new ArrayList<>();
        this.orecubes = new ArrayList<>();
        this.depend = new ArrayList<>();
        this.OX = new TreeSet<>();
        this.OY = new TreeSet<>();
        this.OZ = new TreeSet<>();
        this.exc = exc;
        builddep();
    }
    
    private void builddep(){
        depend.add(new int[]{0,0,1});
        depend.add(new int[]{1,0,1});
        depend.add(new int[]{-1,0,1});
        depend.add(new int[]{0,1,1});
        depend.add(new int[]{0,-1,1});
        /*depend.add(new int[]{1,1,1});
        depend.add(new int[]{-1,-1,1});
        depend.add(new int[]{-1,1,1});
        depend.add(new int[]{1,-1,1});
        depend.add(new int[]{-1,2,1});
        depend.add(new int[]{0,2,1});
        depend.add(new int[]{1,2,1});
        depend.add(new int[]{-1,-2,1});
        depend.add(new int[]{0,-2,1});
        depend.add(new int[]{1,-2,1});
        depend.add(new int[]{2,-1,1});
        depend.add(new int[]{2,0,1});
        depend.add(new int[]{2,1,1});
        depend.add(new int[]{-2,-1,1});
        depend.add(new int[]{-2,0,1});
        depend.add(new int[]{-2,1,1});
        /*depend.add(new int[]{0,0,1});
        depend.add(new int[]{1,0,1});
        depend.add(new int[]{-1,0,1});*/
        
    }
    
    public void addCube(CarCube c){
        cubes.add(c);        
        if(c.Cost>0)orecubes.add(c);
        OX.add(c.X);
        OY.add(c.Y);
        OZ.add(c.Z);
    }
    
    public int [] getind(CarCube ccc){
        int[] ret=new int[3];
            ret[0]=(int)((ccc.X-OX.first())/dx);
            ret[1]=(int)((ccc.Y-OY.first())/dy);
            ret[2]=(int)((ccc.Z-OZ.first())/dz);
        return ret;
    }
    
    
    public int [] getind(double X,double Y, double Z){
        int[] ret=new int[3];
            ret[0]=(int)((X-OX.first())/dx);
            ret[1]=(int)((Y-OY.first())/dy);
            ret[2]=(int)((Z-OZ.first())/dz);
        return ret;
    }
    
    public CarCube getCube(double X, double Y, double Z){
        int[] ret=getind(X,Y,Z);
        return arrcar[ret[0]][ret[1]][ret[2]];
    }
    
    public CarCube getCube(int x,int y,int z){
        CarCube ret;
        try{ret=arrcar[x][y][z];}
        catch(Exception e){ret=null;}
        return ret;
    }
    
/*    private void buildtable(){
        deptable=new boolean[cubes.size()][cubes.size()];
        for(int i=0; i<cubes.size();i++){
            LinearCarCube ccc=cubes.get(i);
            ArrayList<Integer> ilist= new ArrayList();
            int [] ind=getind(ccc.X,ccc.Y,ccc.Z);
            for(int[]delt:depend){
                LinearCarCube tmp=getCube(ind[0]+delt[0],ind[1]+delt[1],ind[2]+delt[2]);
                if(tmp!=null){
                    int j=cubes.indexOf(tmp);
                    deptable[i][j]=true;
                    ilist.add(j);
                }
            }
            for(int ii=0; ii<i; ii++){
                if(deptable[ii][i]){
                    for(int q:ilist){
                        deptable[ii][q]=true;
                    }
                }
            }
        }
    
    }*/
    
    public void builddeps(MPSolver solver, MPVariable [] vars){
        int div=cubes.size();
        for(int i=0; i<div-1;i++){
            CarCube ccc=cubes.get(i);
            int [] ind=getind(ccc.X,ccc.Y,ccc.Z);
            for(int[]delt:depend){
                CarCube tmp=getCube(ind[0]+delt[0],ind[1]+delt[1],ind[2]+delt[2]);
                if(tmp!=null){
                    int j=cubes.indexOf(tmp);
                    for(int y=0; y<exc.years; y++){
                        MPConstraint constr=solver.makeConstraint(0, 1);
                        for(int yy=0; yy<=y;yy++)
                            constr.setCoefficient(vars[j+div*yy], 1);
                        constr.setCoefficient(vars[i+div*y], -1);
                    }
                }
            }
        }
    }

    
    public void carbuild(){
        arrcar=new CarCube[OX.size()][OY.size()][OZ.size()];
        CarCube tmp=cubes.get(0);
        dx=tmp.dX;
        dy=tmp.dY;
        dz=tmp.dZ;
        for (CarCube ccc:cubes){
            int [] ind=getind(ccc.X,ccc.Y,ccc.Z);
            arrcar[ind[0]][ind[1]][ind[2]]=ccc;
        }
        TreeSet<CarCube> no=new TreeSet(new CubeComparator());
        no.addAll(cubes);
        cubes.clear();
        cubes.addAll(no);
        no.clear();
        no.addAll(orecubes);
        orecubes.clear();
        orecubes.addAll(no);
        no.clear();

        //long tim=System.currentTimeMillis();
        //buildtable();
        //System.out.println("Table is build in "+(System.currentTimeMillis()-tim)+"ms");
    }
    
    
    
    public double[] carsummary(){
        double weightcount=0;
        double orecount=0;
        double rubcount=0;
        for(CarCube cub:cubes){
            weightcount+=cub.getVolume();
            if(cub.Cost>0)
                orecount+=cub.getVolume();
            else rubcount+=cub.getVolume();
        }
        System.out.println("Total blocks: "+cubes.size());
        System.out.println("Total weight: "+(weightcount));
        System.out.println("Total ore blocks: "+orecubes.size());
        System.out.println("Total ore: "+(orecount));
        System.out.println("Total rub blocks: "+(cubes.size()-orecubes.size()));
        System.out.println("Total rub: "+(rubcount));
        return new double[]{cubes.size(),orecubes.size()};
    }
class CubeComparator implements Comparator<CarCube>{
    @Override
    public int compare(CarCube a, CarCube b){
        if(a.Z<b.Z)return -1;
        return 1;
    }
    }
}
