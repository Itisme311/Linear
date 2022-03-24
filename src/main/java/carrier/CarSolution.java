/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrier;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class CarSolution {
    public double Cost;
    public long time;
    public List<Journalrecord> record;
    
    public CarSolution(){
    record = new ArrayList<>();}
    

    public void addrecord(Journalrecord a){
        record.add(a);
    }
    
    
    public String getStr(){
        String ret="";
            for(Journalrecord r:record){
                r.recount();
                ret+=r.toString();}
        return ret;
    }
}
