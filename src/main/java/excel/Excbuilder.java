/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;

import carrier.CarCube;
import carrier.Carrier;
import carrier.CarSolution;
import carrier.Cube;
import carrier.Journalrecord;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author User
 */
public class Excbuilder {
    Workbook workbook;
    Font fontBold;
    public int years; 
    public double dore,dwei, totalcost;
    public Carrier car;
    public long sttime;
    public String path;


    public Workbook getWorkbook() {
        return workbook;
    }
    
    private String celltoStr(Cell cl){
        String ret="";
        if(cl!=null){
        DataFormatter formatter = new DataFormatter();
        ret= formatter.formatCellValue(cl);}
        return ret;
    }
    
  /*  private void paintCube(Sheet sh, Cube c, int dx, int dy, CellStyle st){
            Row r=sh.getRow(c.Y+dy);
            if(r==null)r=sh.createRow(c.Y+dy);
            Cell cl = r.getCell(c.X+dx);
            if(cl==null)cl=r.createCell(c.X+dx);
            if(c.Weight>0){
                XSSFRichTextString outs= new XSSFRichTextString(((double)c.Weight)/multiplier+" "+((double)c.Ore)/multiplier);
                if(st!=null)cl.setCellStyle(st);
                if(c.Ore>0)outs.applyFont(fontBold);
                cl.setCellValue(outs);
            }
            else cl.setCellValue("");
    }*/
    
    
    /*private void writestat(Sheet sh,Journalrecord jr, int dy, CellStyle cs){
        int rowd=(jr.year-1)*4+dy;
            Row r=sh.getRow(rowd);
            if(r==null)r=sh.createRow(rowd);
            Cell cl = r.createCell(0);
            if(cs!=null)cl.setCellStyle(cs);
            cl.setCellValue("Year:");
            cl=r.createCell(1);
            if(cs!=null)cl.setCellStyle(cs);
            cl.setCellValue(jr.year);
            r=sh.getRow(rowd+1);
            if(r==null)r=sh.createRow(rowd+1);
            cl = r.createCell(0);
            //if(cs!=null)cl.setCellStyle(cs);
            cl.setCellValue("Weight:");
            cl=r.createCell(1);
            //if(cs!=null)cl.setCellStyle(cs);
            cl.setCellValue(((double)jr.yearweight)/multiplier);
            r=sh.getRow(rowd+2);
            if(r==null)r=sh.createRow(rowd+2);
            cl = r.createCell(0);
           // if(cs!=null)cl.setCellStyle(cs);
            cl.setCellValue("Ore:");
            cl=r.createCell(1);
            //if(cs!=null)cl.setCellStyle(cs);
            cl.setCellValue(((double)jr.yearore)/multiplier);
    }
    
    private void addSheet(Workbook wb, CarSolution j, int i, List<CellStyle> CS){
        int dx=3;
        int dy=2;
        Sheet sh=wb.createSheet("sol "+i);
        sh.setDefaultColumnWidth(5);
        sh.setColumnWidth(0, 10*256);
        sh.setDefaultRowHeight((short)600);
        paintcar(sh,dx,dy);
        for(Journalrecord jr:j.record){
            CellStyle stl=CS.get(jr.year-1);
            writestat(sh,jr,dy,stl);
            //for(Cube cub:jr.cubes)
                //paintCube(sh,cub, dx,dy,stl);
         }
    }
    
    
    public void exsols(String path, List<CarSolution> sols) throws FileNotFoundException, IOException{
        Workbook tmp;
        FileOutputStream fileOut = new FileOutputStream(path);
        tmp = new XSSFWorkbook();
        List<CellStyle>CS=new ArrayList<>();
        short step = (short) (255/years*10);
        for(short i=0; i<years; i++){
            CellStyle style = tmp.createCellStyle();
            //style.setFillBackgroundColor((short)(i+10));
            
            style.setFillForegroundColor((short)(i+10));
            style.setFillPattern(FillPatternType.SPARSE_DOTS);
            CS.add(style);
        }         
        int i=0;
        for(CarSolution cl:sols){
            addSheet(tmp,cl,i,CS);
            i++;
        }
    
        tmp.write(fileOut);
        fileOut.close();
    
    }
*/
    public void exsol3d(String path, CarSolution sol, int count) throws FileNotFoundException, IOException, InvalidFormatException{
        if(count==1) workbook=new XSSFWorkbook();
        else {          
            FileInputStream  fil=new FileInputStream (path);
            workbook=new XSSFWorkbook(fil);
            fil.close();
                }
        FileOutputStream fileOut = new FileOutputStream(path);
        
        Sheet sh=workbook.createSheet("Solution "+count);
        Row r=sh.createRow(0);
        int i=1;
        r.createCell(0).setCellValue("X");
        r.createCell(1).setCellValue("Y");
        r.createCell(2).setCellValue("Z");
        r.createCell(3).setCellValue("dX");
        r.createCell(4).setCellValue("dY");
        r.createCell(5).setCellValue("dZ");
        r.createCell(6).setCellValue("Value");
        //r.createCell(7).setCellValue("P2O5");
        r.createCell(7).setCellValue("Year");
        for(Journalrecord rcd:sol.record){
            for(CarCube c:rcd.cubes){
                r=sh.createRow(i);
                r.createCell(0).setCellValue(c.X);
                r.createCell(1).setCellValue(c.Y);
                r.createCell(2).setCellValue(c.Z);
                r.createCell(3).setCellValue(c.dX);
                r.createCell(4).setCellValue(c.dY);
                r.createCell(5).setCellValue(c.dZ);
                //r.createCell(6).setCellValue(c.Orepart);
                r.createCell(6).setCellValue(c.Cost);
                r.createCell(7).setCellValue(rcd.year);
                i++;
            }
        }
        workbook.write(fileOut);
        fileOut.close();
    
    }
    
    public void cubestoExcel(String path, List <? extends CarCube> cubes) throws FileNotFoundException, IOException{
        Workbook tmp;
        FileOutputStream fileOut = new FileOutputStream(path);
        tmp = new XSSFWorkbook();
        Sheet sh=tmp.createSheet();
        Row r=sh.createRow(0);
        int i=1;
        r.createCell(0).setCellValue("X");
        r.createCell(1).setCellValue("Y");
        r.createCell(2).setCellValue("Z");
        r.createCell(3).setCellValue("dX");
        r.createCell(4).setCellValue("dY");
        r.createCell(5).setCellValue("dZ");
        r.createCell(6).setCellValue("Value");
        r.createCell(7).setCellValue("P2O5");
        r.createCell(8).setCellValue("Year");
            for(CarCube c:cubes){
                r=sh.createRow(i);
                cubetoex(r,c);
                /*r.createCell(0).setCellValue(c.X);
                r.createCell(1).setCellValue(c.Y);
                r.createCell(2).setCellValue(c.Z);
                r.createCell(3).setCellValue(c.dX);
                r.createCell(4).setCellValue(c.dY);
                r.createCell(5).setCellValue(c.dZ);
                r.createCell(6).setCellValue(c.Cost);
                r.createCell(7).setCellValue(c.Orepart);*/
                i++;
            }
        tmp.write(fileOut);
        fileOut.close();
    }
    
    
    
    private void cubetoex(Row r, CarCube c){
            r.createCell(0).setCellValue(c.X);
            r.createCell(1).setCellValue(c.Y);
            r.createCell(2).setCellValue(c.Z);
            r.createCell(3).setCellValue(c.dX);
            r.createCell(4).setCellValue(c.dY);
            r.createCell(5).setCellValue(c.dZ);
            r.createCell(6).setCellValue(c.Cost);
            r.createCell(7).setCellValue(c.Orepart);
    }
    

    
    
    
    public Excbuilder(String pth, int yrs,double dor, double dw) throws IOException, InvalidFormatException{
        dore=dor;
        dwei=dw;
        path=pth+".xlsx";
        totalcost=0;
        car= new Carrier(this);
        workbook = null;
        years=yrs;
        sttime=System.currentTimeMillis();
        workbook = new XSSFWorkbook(new File(path));
        fontBold= workbook.createFont();
        fontBold.setBold(true);
        double cx,cy,cz,cdx,cdy,cdz,co=0,cc;
        String chk;
        int index=1;
        int sheet=0;
        Row row = workbook.getSheetAt(sheet).getRow(index);
        while (row!=null){
            int y;
            int ii=0;
            cx=Double.parseDouble(celltoStr(row.getCell(ii))); ii++;
            cy=Double.parseDouble(celltoStr(row.getCell(ii)));ii++;
            cz=Double.parseDouble(celltoStr(row.getCell(ii)));ii++;
            cdx=Double.parseDouble(celltoStr(row.getCell(ii)));ii++;
            cdy=Double.parseDouble(celltoStr(row.getCell(ii)));ii++;
            cdz=Double.parseDouble(celltoStr(row.getCell(ii)));ii++;
            co=Double.parseDouble(celltoStr(row.getCell(ii)));ii++;
            cc=Double.parseDouble(celltoStr(row.getCell(ii)));ii++;
            chk=celltoStr(row.getCell(ii)); ii++;
            try{co=Double.parseDouble(chk);}
            catch(Exception e){
            }
            try{y=Integer.parseInt(celltoStr(row.getCell(ii)));}
            catch(Exception e){y=-1;}
            totalcost+=cc;
            CarCube ncube = new CarCube(cx,cy,cz,cdx,cdy,cdz,co,cc,car);
            ncube.year=y;
            car.addCube(ncube);
            index++;
            row = workbook.getSheetAt(sheet).getRow(index);
        }
        car.carbuild();

    }
    
}
