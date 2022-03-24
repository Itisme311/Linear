/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import carrier.CarCube;
import carrier.CarSolution;
import carrier.Journalrecord;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import excel.Excbuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.pow;
import java.util.Locale;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * Linear programming example that shows how to use the API.
 */
public class Linear {
  private static void runLinearProgrammingExample(String in,int ycount,double dore, double dwei, String type) throws IOException, InvalidFormatException {
    /*solver_id is case insensitive, and the following names are supported:
   *   - CLP_LINEAR_PROGRAMMING or CLP
   *   - CBC_MIXED_INTEGER_PROGRAMMING or CBC
   *   - GLOP_LINEAR_PROGRAMMING or GLOP
   *   - BOP_INTEGER_PROGRAMMING or BOP
   *   - SAT_INTEGER_PROGRAMMING or SAT or CP_SAT
   *   - SCIP_MIXED_INTEGER_PROGRAMMING or SCIP
   *   - GUROBI_LINEAR_PROGRAMMING or GUROBI_LP
   *   - GUROBI_MIXED_INTEGER_PROGRAMMING or GUROBI or GUROBI_MIP
   *   - CPLEX_LINEAR_PROGRAMMING or CPLEX_LP
   *   - CPLEX_MIXED_INTEGER_PROGRAMMING or CPLEX or CPLEX_MIP
   *   - XPRESS_LINEAR_PROGRAMMING or XPRESS_LP
   *   - XPRESS_MIXED_INTEGER_PROGRAMMING or XPRESS or XPRESS_MIP
   *   - GLPK_LINEAR_PROGRAMMING or GLPK_LP
   *   - GLPK_MIXED_INTEGER_PROGRAMMING or GLPK or GLPK_MIP  
   */   
      
    String slvr=type;
    System.out.println("----------------------------------------\nSOLVING WITH "+slvr);
    MPSolver solver = MPSolver.createSolver(slvr);
    if (solver == null) {
      System.out.println("Could not create solver");
      return;
    }
    long tim;
    Excbuilder ecar=new Excbuilder(in,ycount,dore,dwei);
    
    long stop=System.currentTimeMillis();
    System.out.println("Carrier is loaded in "+(stop-ecar.sttime)+" ms(Total "+(stop-ecar.sttime)+" ms)");
    double[] sum=ecar.car.carsummary();
    double weightnorm=(sum[0]-sum[1])/ecar.years;
    double orenorm=sum[1]/ecar.years;
    double Dweight =weightnorm*ecar.dwei;
    double Dore=orenorm*ecar.dore;
    double upornorm=orenorm+Dore;
    double downornorm=orenorm-Dore;
    double upwnorm=weightnorm+Dweight;
    double downwnorm=weightnorm-Dweight;
    /*
    upornorm=360;
    downornorm=300;
    upwnorm=1400;
    downwnorm=0;
*/
    System.out.println("years: "+ecar.years);
    System.out.println("orenorm: "+downornorm+" "+upornorm);
    System.out.println("wnorm: "+downwnorm+" "+upwnorm);
    System.out.println("totnorm: "+(sum[0]/ecar.years)+" "+((sum[0]/ecar.years)*(1-ecar.dwei))+" "+((sum[0]/ecar.years)*(1+ecar.dwei)));
    
    tim=System.currentTimeMillis();
    //ссоздание переменных для кубов и ограничений на единственный забор куба.
    MPVariable [] vars=new MPVariable[ecar.car.cubes.size()*ecar.years];
    for(int i=0; i<ecar.car.cubes.size(); i++){
        MPConstraint constr=solver.makeConstraint(1, 1);
        for(int y=0; y<ecar.years; y++){
            vars[i+y*ecar.car.cubes.size()]=solver.makeBoolVar(ecar.car.cubes.get(i).toString()+" y"+(y+1));
            constr.setCoefficient(vars[i+y*ecar.car.cubes.size()], 1);
        }
    }
    stop=System.currentTimeMillis();
    System.out.println("Year costraints are built in "+(stop-tim)+" ms(Total "+(stop-ecar.sttime)+" ms)");

    tim=System.currentTimeMillis();
    //ограничения на объемы выимки и формирование целевой функции.
    MPObjective objective = solver.objective();
    for(int y=0; y<ecar.years; y++){
        MPConstraint oreconstr = solver.makeConstraint(downornorm, upornorm);
        MPConstraint rubconstr = solver.makeConstraint(downwnorm, upwnorm);
        for(int i=0; i<ecar.car.cubes.size(); i++){
            double cost=ecar.car.cubes.get(i).Cost*pow(0.9,y);
            if(cost>0)oreconstr.setCoefficient(vars[i+y*ecar.car.cubes.size()], 1);
            else rubconstr.setCoefficient(vars[i+y*ecar.car.cubes.size()], 1);
            objective.setCoefficient(vars[i+y*ecar.car.cubes.size()], cost);
        }
    }
    objective.setMaximization();

    stop=System.currentTimeMillis();
    System.out.println("Objective and norms are built in "+(stop-tim)+" ms(Total "+(stop-ecar.sttime)+" ms)");

    tim=System.currentTimeMillis();
    ecar.car.builddeps(solver, vars);
    stop=System.currentTimeMillis();
    System.out.println("Dependensies are built in "+(stop-tim)+" ms(Total "+(stop-ecar.sttime)+" ms)");
    //String model = solver.exportModelAsLpFormat();
    //System.out.println(model);

/*    for(MPVariable vr:solver.variables())
        System.out.println(vr.name()+" ["+vr.lb()+";"+vr.ub()+"]");*/
    /*
    for(int i=0; i<ecar.car.cubes.size();i++){
        String out=ecar.car.cubes.get(i).toString();
        for(int y=0; y<ecar.years; y++)
            out+=vars[i+(y*ecar.car.cubes.size())].name()+" ";
        System.out.println(out);
    }
    
    */
    solver.setTimeLimit(12*60*60000);
    System.out.println("!!! "+solver.constraints().length+" "+solver.variables().length);
    tim=System.currentTimeMillis();
    final MPSolver.ResultStatus resultStatus = solver.solve();
    tim=System.currentTimeMillis()-tim;
    System.out.println("Solved in: "+tim+"ms");
    // Check that the problem has an optimal solution.
    if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
      System.err.println("The problem does not have an optimal solution!");
      //return;
    }
    if (resultStatus == MPSolver.ResultStatus.NOT_SOLVED) {
      System.err.println("The problem does not solved!");
      return;
    }

    // Verify that the solution satisfies all constraints (when using solvers
    // others than GLOP_LINEAR_PROGRAMMING, this is highly recommended!).
    if (!solver.verifySolution(/*tolerance=*/1e-7, /* log_errors= */ true)) {
      System.err.println("The solution returned by the solver violated the"
          + " problem constraints by at least 1e-7");
      return;
    }

    System.out.println("Problem solved in " + solver.wallTime() + " milliseconds");

    System.out.println("Optimal objective value = " + solver.objective().value());

    File file = new File(in+"_"+ecar.sttime+"_"+slvr+".txt");
    CarSolution CS=new CarSolution();
    CS.time=tim;
    CS.Cost=solver.objective().value();
    Journalrecord []JR=new Journalrecord[ecar.years];
    for(int i=0; i<ecar.years; i++){
        JR[i]=new Journalrecord(i+1);
        CS.record.add(JR[i]);
    }

    FileWriter fwr;
    file.createNewFile();
    fwr=new FileWriter(file);
    fwr.append("Objective value: "+solver.objective().value()+"\n"+"Solved in "+tim+" ms\n");
    fwr.append("Years: "+ecar.years+" orenorm: "+downornorm+" "+upornorm+" wnorm: "+downwnorm+" "+upwnorm+"\n");
    fwr.append("Solved with: "+slvr+"\n");
    for (int i=0; i<vars.length/ecar.years; i++){
        String out="";
        int yr=0;
        for(int y=0; y<ecar.years;y++){
            yr+=(y+1)*(int)vars[i+ecar.car.cubes.size()*y].solutionValue();
            out+=vars[i+ecar.car.cubes.size()*y].name()+"= "+(int)vars[i+ecar.car.cubes.size()*y].solutionValue()+" ";
        }
        fwr.append(out+"\n");
        JR[yr-1].cubes.add(ecar.car.cubes.get(i));
    }
    fwr.flush();
    fwr.close();
    
    ecar.exsol3d(in+"_"+ecar.sttime+"_"+slvr+".xlsx", CS, 1);
        
  }

  public static void main(String[] args) throws Exception {
    Locale.setDefault(new Locale("en", "US"));
    Loader.loadNativeLibraries();
    runLinearProgrammingExample("d:\\work\\NBprojects\\Nodes_withoutsort\\carrier\\ideal\\4000", 3,0.05,0.05,"SCIP");
    /*String[] str={"CLP","CBC","GLOP","BOP","SAT","SCIP","GUROBI_LP","GUROBI","CPLEX_LP","CPLEX","XPRESS_LP","XPRESS","GLPK_LP","GLPK"};
    for(String s:str)
        runLinearProgrammingExample("d:\\work\\NBprojects\\Nodes_withoutsort\\carrier\\ideal\\4000", 3,0.1,0.5,s);*/
  }
}
