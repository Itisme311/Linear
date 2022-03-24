/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carrier;


/**
 *
 * @author User
 */

public class Cube{
    public double X,Y,Z;
    public Cube (double x,double y,double z){
        X=x;
        Y=y;
        Z=z;
    }
    @Override
    public String toString(){
        return "["+X+";"+Y+";"+Z+"]";
    }


}

