/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

/**
 *
 * @author momo and pulak
 */
public class Strips {

    protected List<Predicate> initialState;
    protected List<Predicate> finalState;
    protected List<Predicate> currentState;

    protected List<Operator> plan;
    protected List<Operator> operatorList;

    //Petitions of each office
    Map<Integer, Integer> map_office_coffe = new HashMap<Integer, Integer>();
    //Number of coffe of each machine
    Map<Integer, Integer> map_machine_coffe = new HashMap<Integer, Integer>();
    
    
    protected Stack<Object> stack;

    public Strips() {
        read_file("testcase1.txt");
        CreateOperatorList();
    }
    
    private String printListPredicates(List<Predicate> list, String method){
        String res = method + " List <";
        for(int i = 0; i < list.size(); i++){
            res += list.get(i).toString() + ";";
        }
        
        res += ">";
        return res;
    }

    private void StripsAlgorithm() {
    	long startTime  = System.nanoTime();
        stack = new Stack<>();
        currentState = initialState;
        plan = new ArrayList<>();
        System.out.println();
        System.out.println("Stack: ");
        System.out.println(printListPredicates(finalState, "PUSH"));
        stack.push(finalState);
        
        for (int i = 0; i < finalState.size(); i++) {            
            stack.push(finalState.get(i));
            System.out.println("PUSH Predicate " + finalState.get(i).toString());
        }
        while (!stack.empty()) {
            Object obj = stack.pop();            
            if (obj instanceof Operator) {                
                Operator op = (Operator) obj;
                System.out.println("POP Operator " + op.toString());
                //currentState - del + add
                for (int i = 0; i < op.getDeleteList().size(); i++) {
                    for (int j = 0; j < currentState.size(); j++) {
                        if (currentState.get(j).equals(op.getDeleteList().get(i))) {
                            currentState.remove(j);
                        }
                    }
                }

                for (int j = 0; j < op.getAddList().size(); j++) {
                    currentState.add(op.getAddList().get(j));
                }

                plan.add(op);
            } else if (obj instanceof List<?>) {
                //validate all?
                boolean isPredTrue = false;
                List<Predicate> listPred = (List<Predicate>) obj;
                System.out.println(printListPredicates(listPred, "POP"));
               
                for (int i = 0; i < listPred.size(); i++) {
                    if(listPred.get(i).getPredname().equals("Steps"))
                        continue;
                    for (int j = 0; j < currentState.size(); j++) {
                        if (listPred.get(i).equals(currentState.get(j))) {
                            isPredTrue = true;
                            break;
                        }
                    }
                    if (!isPredTrue) {
                        System.out.println(printListPredicates(listPred, "PUSH"));
                        stack.push(listPred);
                        System.out.println("PUSH Predicate " + listPred.get(i).toString());
                        stack.push(listPred.get(i)); 
                    }
                }
            } else if (obj instanceof Predicate) {
                Predicate pred = (Predicate) obj;
                //totally instanciated
                System.out.println("POP Predicate " + pred.toString());
                if(pred.getPredname().equals("Steps"))
                    continue;
                //pred is false in actual state
                boolean predTrueInCurrentState = false;
                for (int i = 0; i < currentState.size(); i++) {
                    if (pred.equals(currentState.get(i))) {
                        predTrueInCurrentState = true;
                        break;
                    }
                }

                if (!predTrueInCurrentState) {
                    //if I get here is that the predicate is false in the current state
                    //search operator o
                    Operator o = new Operator();
                    o = (Operator)searchOperator(pred);
                                        
                    //stack operator o
                    System.out.println("PUSH Operator " + o.toString());
                    stack.push(o);

                    //stack preconditions of operator o
                    //stack all
                    System.out.println(printListPredicates(o.getPreconditionsList(), "PUSH "));
                    stack.push(o.getPreconditionsList());
                    //stack each precondition by separate
                    for (int i = 0; i < o.getPreconditionsList().size(); i++) {
                        System.out.println("PUSH Predicate " + o.getPreconditionsList().get(i).toString() );
                        stack.push(o.getPreconditionsList().get(i));
                    }
                }

            }
        }

        System.out.println("");
        System.out.println("Plan: ");
        String stringPlan = "";
        for (int i = 0; i < plan.size(); i++) {
            stringPlan += plan.get(i).toString() + ";";
        }
        System.out.println(stringPlan);
        
        System.out.println("");
        
        for(int i = 0; i< currentState.size(); i++){
            if(currentState.get(i).getPredname().equals("Steps")){
                System.out.println("Number of steps: " + currentState.get(i));

            }
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("");
        System.out.println("Duration of the algorith: " + duration + " NANOSECONDS");
    }

    
    private Operator searchOperator(Predicate p) {
        //search the addList to find a predicate with this name
       
        Operator retOp = new Operator();
        Operator op = new Operator();
        List<Predicate> preconditionsList = new ArrayList<>();
        List<Predicate> deleteList = new ArrayList<>();
        List<Predicate> addList = new ArrayList<>();
        
        Constant c1 = null, c2 = null, x = null, xincrement = null;
        for (int i = 0; i < operatorList.size(); i++) {
            for (int j = 0; j < operatorList.get(i).getAddList().size(); j++) {
                Predicate addPred = operatorList.get(i).getAddList().get(j);
                Operator op1 = operatorList.get(i);

                if (p.getPredname().equals(addPred.getPredname())) {
                    //get a copy of the operator for change
                    op = Operator.get_Clone(op1);
                    switch (p.getNumberParams()) {
                        case 0:
                            //res.add(op);
                            break;
                        case 1:
                            if (op.getName().equals("Serve")) {
                                if (p.getParameter1() instanceof ConstantOffice) {
                                    c1 = new ConstantOffice(((ConstantOffice) p.getParameter1()).getVal());
                                    op.setParameter1(c1);
                                    c2 = new ConstantCoffee(map_office_coffe.get(c1.getVal()));
                                    op.setParameter2(c2);
                                }
                            }
                            
                            if (op.getName().equals("Make")) {
                                if (p.getParameter1() instanceof ConstantCoffee) {
                                    c2 = new ConstantCoffee(((ConstantCoffee) p.getParameter1()).getVal());
                                    op.setParameter2(c2);
                                   
                                    c1 = new ConstantOffice(getKeyOfValue(c2.getVal()));
                                    op.setParameter1(c1);

                                }
                            }
                            if (op.getName().equals("Serve") || op.getName().equals("Make")) {
                                //Instanciate Precondition List
                                for (int k = 0; k < op.getPreconditionsList().size(); k++) {
                                    Predicate predPrecond = op.getPreconditionsList().get(k);
                                    if (predPrecond.getParameter1() instanceof ConstantOffice) {
                                        predPrecond.setParameter1(c1);
                                    } else if (predPrecond.getParameter1() instanceof ConstantCoffee && c2 != null) {
                                        predPrecond.setParameter1(c2);
                                    }
                                    if (predPrecond.getParameter2() instanceof ConstantCoffee && c2 != null) {
                                        predPrecond.setParameter2(c2);
                                    }
                                    preconditionsList.add(predPrecond);
                                }

                                //Instanciate Delete List
                                for (int k = 0; k < op.getDeleteList().size(); k++) {
                                    Predicate predDel = op.getDeleteList().get(k);
                                    if (predDel.getParameter1() instanceof ConstantOffice) {
                                        predDel.setParameter1(c1);
                                    } else if (predDel.getParameter1() instanceof ConstantCoffee && c2 != null) {
                                        predDel.setParameter1(c2);
                                    }
                                    if (predDel.getParameter2() instanceof ConstantCoffee && c2 != null) {
                                        predDel.setParameter2(c2);
                                    }
                                    deleteList.add(predDel);
                                }

                                //Instanciate Add List
                                for (int k = 0; k < op.getAddList().size(); k++) {
                                    Predicate predAdd = op.getAddList().get(k);
                                    if (predAdd.getParameter1() instanceof ConstantOffice) {
                                        predAdd.setParameter1(c1);
                                    } else if (predAdd.getParameter1() instanceof ConstantCoffee && c2 != null) {
                                        predAdd.setParameter1(c2);
                                    }
                                    if (predAdd.getParameter2() instanceof ConstantCoffee && c2 != null) {
                                        predAdd.setParameter2(c2);
                                    }
                                    addList.add(predAdd);
                                }

                            }

                            if (op.getName().equals("Move")) {
                                if (p.getParameter1() instanceof ConstantOffice) {
                                    c2 = new ConstantOffice(((ConstantOffice) p.getParameter1()).getVal());
                                    op.setParameter2(c2);
                                    for (int k = 0; k < currentState.size(); k++) {
                                        if (currentState.get(k).getPredname().equals("Robot-location")) {
                                            c1 = new ConstantOffice(((ConstantOffice) (currentState.get(k).getParameter1())).getVal());
                                            continue;
                                        }
                                        
                                        if (currentState.get(k).getPredname().equals("Steps")) {
                                            x = new ConstantDistance(((ConstantDistance) (currentState.get(k).getParameter1())).getVal());
                                            continue;
                                        }
                                    }
                                    op.setParameter1(c1);
                                }
                                //instanciate precondition list
                                for (int k = 0; k < op.getPreconditionsList().size(); k++) {
                                    Predicate predPrecond = op.getPreconditionsList().get(k);
                                    if (predPrecond.getParameter1() instanceof ConstantOffice) {
                                        predPrecond.setParameter1(c1);
                                    }
                                    
                                    if (predPrecond.getParameter1() instanceof ConstantDistance) {
                                        predPrecond.setParameter1(x);
                                    }
                                    preconditionsList.add(predPrecond);
                                }
                                //instanciate delete list 
                                for (int k = 0; k < op.getDeleteList().size(); k++) {
                                    Predicate predDel = op.getDeleteList().get(k);
                                    if (predDel.getParameter1() instanceof ConstantOffice) {
                                        predDel.setParameter1(c1);
                                    }
                                    
                                     if (predDel.getParameter1() instanceof ConstantDistance) {
                                        predDel.setParameter1(x);
                                    }
                                    deleteList.add(predDel);
                                }
                                //instanciate add list
                                for (int k = 0; k < op.getAddList().size(); k++) {
                                    Predicate predAdd = op.getAddList().get(k);
                                    if (predAdd.getParameter1() instanceof ConstantOffice) {
                                        predAdd.setParameter1(c2);
                                    }
                                    
                                    if (predAdd.getParameter1() instanceof ConstantDistance) {
                                        xincrement = new ConstantDistance(x.getVal() + getManhatanDistance(c1.getVal(), c2.getVal()));
                                        predAdd.setParameter1(xincrement);
                                    }
                                    addList.add(predAdd);
                                }
                            }
                    }
                    
                    op.setPreconditionsList(preconditionsList);
                    op.setAddList(addList);
                    op.setDeleteList(deleteList);
                    
                    retOp = op;
                    break;
                }

            }
        }

        return op;
    }

    private int getKeyOfValue(int value) {
        //this vector is used to store office numbers that have machine with same capacity
        Vector<Integer> vectOffices = new Vector<Integer>();
        Integer key = 0,val;
        int robotloc = 1, mindistance = 1000, mhd,nearestofc = 0;
        
        val = value;
        for (Map.Entry entry : map_machine_coffe.entrySet()) {
            if (val.equals(entry.getValue())) {
                key = (Integer) entry.getKey();
                vectOffices.add(key);
            }
        }
        
        //taking current location of robot
        for (int k = 0; k < currentState.size(); k++) {
            if (currentState.get(k).getPredname().equals("Robot-location")) {
                robotloc = currentState.get(k).getParameter1().getVal();
                break;
            }
        }
        
        for(int i = 0; i < vectOffices.size(); i++)
        {
            mhd = getManhatanDistance(robotloc, vectOffices.get(i));
            if(mhd < mindistance)
            {
                mindistance = mhd;
                nearestofc = vectOffices.get(i);
            }
        }
        
        return nearestofc;
    }

    private void read_file(String filename) {
        try {
            InputStream stream = this.getClass().getResourceAsStream(filename);//getClassLoader().
            BufferedReader brFile = new BufferedReader(new InputStreamReader(stream));
            brFile.readLine();
            String initial = brFile.readLine();
            System.out.println("Initial state:");
            System.out.println(initial);
            System.out.println();
            initialState = new ArrayList<>();
            String[] predsini = initial.split(";");
            Predicate p;
            for (int i = 0; i < predsini.length; i++) {
                p = new Predicate();
                String[] eachPred = predsini[i].split(",");
                if (eachPred[0] != null) {
                    p.setPredname(eachPred[0]);
                    p.setNumberParams(0);
                    if (p.getPredname().equals("Machine")) {
                        map_machine_coffe.put(Integer.parseInt(eachPred[1]), Integer.parseInt(eachPred[2]));
                    } else if (p.getPredname().equals("Petition")) {
                        map_office_coffe.put(Integer.parseInt(eachPred[1]), Integer.parseInt(eachPred[2]));
                    }
                }
                if (eachPred.length > 1 && eachPred[1] != null) {
                    if (p.getPredname().equals("Robot_loaded")) {
                        p.setParameter1(new ConstantCoffee(Integer.parseInt(eachPred[1])));
                    } else {
                        p.setParameter1(new ConstantOffice(Integer.parseInt(eachPred[1])));
                    }
                    p.setNumberParams(1);
                }
                if (eachPred.length > 2 && eachPred[2] != null) {
                    p.setParameter2(new ConstantCoffee(Integer.parseInt(eachPred[2])));
                    p.setNumberParams(2);
                }
                initialState.add(p);
            }
            
            
            
            //adding Stepx because it is not given at initial state
            p = new Predicate("Steps", new ConstantDistance());
            p.setParameter1(new ConstantDistance(0));
            initialState.add(p);
            //end adding Stepx
            
            brFile.readLine();
            String goal = brFile.readLine();
            //rearranging goals by using heuristics
            goal = heuristicMethod(initial,goal);
            System.out.println("Final state:");
            System.out.println(goal);
            finalState = new ArrayList<>();
            String[] predsgoal = goal.split(";");
            for (int i = 0; i < predsgoal.length; i++) {
                p = new Predicate();
                String[] eachPred = predsgoal[i].split(",");
                if (eachPred[0] != null) {
                    p.setPredname(eachPred[0]);
                    p.setNumberParams(0);
                }
                if (eachPred[1] != null) {
                    p.setParameter1(new ConstantOffice(Integer.parseInt(eachPred[1])));
                    p.setNumberParams(1);
                }
                if (eachPred.length > 2 && eachPred[2] != null) {
                    p.setParameter2(new ConstantOffice(Integer.parseInt(eachPred[2])));
                    p.setNumberParams(2);
                }
                finalState.add(p);
            }

        } catch (FileNotFoundException fe) {

        } catch (IOException ioe) {

        }
    }

    private void CreateOperatorList() {
        operatorList = new ArrayList<>();

        //Operator1
        List<Predicate> precondList1 = new ArrayList<>();
        precondList1.add(new Predicate("Robot-location", new ConstantOffice()));
        precondList1.add(new Predicate("Robot-free"));
        precondList1.add(new Predicate("Machine", new ConstantOffice(), new ConstantCoffee()));
        List<Predicate> addList1 = new ArrayList<>();
        addList1.add(new Predicate("Robot-loaded", new ConstantCoffee()));
        List<Predicate> delList1 = new ArrayList<>();
        delList1.add(new Predicate("Robot-free"));
        Operator op1 = new Operator("Make", new ConstantOffice(), new ConstantCoffee(), precondList1, delList1, addList1);
        operatorList.add(op1);

        //Operator2
        List<Predicate> preconList2 = new ArrayList<>();
        preconList2.add(new Predicate("Robot-location", new ConstantOffice()));
        preconList2.add(new Predicate("Steps", new ConstantDistance(0)));
        List<Predicate> addList2 = new ArrayList<>();
        addList2.add(new Predicate("Robot-location", new ConstantOffice()));
        addList2.add(new Predicate("Steps", new ConstantDistance(0)));//new Variable("x+distance(x,y)")));
        List<Predicate> delList2 = new ArrayList<>();
        delList2.add(new Predicate("Robot-location", new ConstantOffice()));
        delList2.add(new Predicate("Steps", new ConstantDistance(0)));
        Operator op2 = new Operator("Move", new ConstantOffice(), new ConstantCoffee(), preconList2, delList2, addList2);
        operatorList.add(op2);

        //Operator3
        List<Predicate> preconList3 = new ArrayList<>();
        preconList3.add(new Predicate("Robot-location", new ConstantOffice()));
        preconList3.add(new Predicate("Robot-loaded", new ConstantCoffee()));
        preconList3.add(new Predicate("Petition", new ConstantOffice(), new ConstantCoffee()));
        List<Predicate> addList3 = new ArrayList<>();
        addList3.add(new Predicate("Served", new ConstantOffice()));
        addList3.add(new Predicate("Robot-free"));
        List<Predicate> delList3 = new ArrayList<>();
        delList3.add(new Predicate("Petition", new ConstantOffice(), new ConstantCoffee()));
        delList3.add(new Predicate("Robot-loaded", new ConstantCoffee()));
        Operator op3 = new Operator("Serve", new ConstantOffice(), new ConstantCoffee(), preconList3, delList3, addList3);
        operatorList.add(op3);

    }

    
    public static String heuristicMethod(String initialState, String goalState) {
        // key = office number(o)------value = machine capacity(n)
        Hashtable<Integer, Integer> coff_machine = new Hashtable<Integer, Integer>();
        // key = n-----value = list of office number(o)
        Hashtable<Integer, Vector<Integer>> petition = new Hashtable<Integer, Vector<Integer>>();
        Vector<String> regoalV = new Vector<String>();
        Vector<Integer> v = new Vector<Integer>();
        Vector<Integer> tempV = new Vector<Integer>();
        Vector<Integer> vectuselessM = new Vector<Integer>();
        String finalState = "";
        String[] iniS = null,tempArr = null, goalS = null;
        int robotloc = 1,index = 0;
        Integer key, value, nearest_distance, m_distance, nearest_machine = null, totaldist = 0, machineCap = null,
                ofcNo = null, servedOfcNo = null, k, val;

        iniS = initialState.split(";");

        for (int i = 0; i < iniS.length; i++) {
            if (iniS[i].contains("Robot-location")) {
                tempArr = iniS[i].split(",");
                robotloc = Integer.parseInt(tempArr[1]);
                continue;
            }

            if (iniS[i].contains("Machine")) {
                tempArr = iniS[i].split(",");
                coff_machine.put(Integer.parseInt(tempArr[1]), Integer.parseInt(tempArr[2]));
                continue;
            }

            if (iniS[i].contains("Petition")) {
                tempArr = iniS[i].split(",");
                key = Integer.parseInt(tempArr[2]);
                value = Integer.parseInt(tempArr[1]);
                // same number of petition but in different office
                if (petition.containsKey(key)) {
                    v = new Vector<Integer>();
                    v = petition.get(key);
                    v.addElement(value);
                    petition.put(key, v);
                } else {
                    v = new Vector<Integer>();
                    v.addElement(value);
                    petition.put(key, v);
                }
            }

        }

	
        while (!petition.isEmpty()) {
            // start first step
            Set<Integer> keys = coff_machine.keySet();
            Iterator<Integer> itr = keys.iterator();
            nearest_distance = 1000;
            while (itr.hasNext()) {
                key = itr.next();
                m_distance = getManhatanDistance(key, robotloc);
                if (m_distance < nearest_distance) {
                    nearest_distance = m_distance;
                    nearest_machine = key;
                }
		
            }

            machineCap = coff_machine.get(nearest_machine);
	   // end first step

	     // start 2nd step
            // finding petition location with this capacity
            if (machineCap != null) {
                tempV = petition.get(machineCap);
            }

            if (tempV != null) {
                // if there exists petition only then we are changing robot location
                robotloc = nearest_machine;
                totaldist += nearest_distance;
		// if multiple petition with same capacity then choose the
                // nearest
                nearest_distance = 1000;
                for (int i = 0; i < tempV.size(); i++) {
                    ofcNo = tempV.get(i);
                    m_distance = getManhatanDistance(ofcNo, nearest_machine);
                    if (m_distance < nearest_distance) {
                        nearest_distance = m_distance;
                        index = i;
                        servedOfcNo = ofcNo;
                        //changing robot location
                        robotloc = ofcNo;
                    }
                } // end for

                // adding distance
                totaldist += nearest_distance;

                // that means unique petition...so remove this petition from
                // petition .....
                // because this petition already served
                if (tempV.size() == 1) {
                    petition.remove(machineCap);
                }

                // that means if there is multiple petitions with same
                // quantity.... delete minimum
                // distanced office value from petition
                if (tempV.size() > 1) {
                    tempV.remove(index);
                    petition.put(machineCap, new Vector<Integer>(tempV));
                }

                regoalV.addElement("Served," + servedOfcNo + ";");

            } // If there is no such petition then remove these machines from
            // coff_machine
            else {
                keys = coff_machine.keySet();
                itr = keys.iterator();
                while (itr.hasNext()) {
                    k = itr.next();
                    val = coff_machine.get(k);
                    if (val.equals(machineCap)) {
                        vectuselessM.add(k);
                    }
                }

                for(int i = 0; i < vectuselessM.size(); i++) {
                    coff_machine.remove(vectuselessM.get(i));
                }
                vectuselessM.clear();
            }
            //end 2nd step

        } // end while
        
        //as we use stack so we need to arrage in reverse order 
        tempArr = goalState.split(";");
        
        for(int i = 0; i < tempArr.length; i++)
        {
            if(tempArr[i].contains("Robot-location"))
            {
                finalState += tempArr[i]+";";
            }
        }
        
        for (int i = regoalV.size()-1; i >= 0 ; i--) {
            finalState = finalState + regoalV.get(i);
            
        } 
        
        
        return finalState;
    }

    public static int getManhatanDistance(int k1, int k2) {
        // key = office number and value = int array of size 2 example: 1---->1,1 and 2---->1,2 etc.
        Hashtable<Integer, int[]> offce_coord = new Hashtable<Integer, int[]>();
        
        // setting office coordinate
        int counter = 1,key;
        int[] arr = new int[2];
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                key = counter;
                offce_coord.put(key, new int[]{i, j});
                arr = offce_coord.get(key);
                //System.out.println("key:" + key + "&value" + arr[0] + "," + arr[1]);
                counter++;
            }
        }
        counter = 1;
        // end setting office coordinate
        
        int x1, y1, x2, y2;
        int[] temp = new int[2];
        temp = offce_coord.get(k1);
        x1 = temp[0];
        y1 = temp[1];
        temp = offce_coord.get(k2);
        x2 = temp[0];
        y2 = temp[1];

        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static void main(String[] args) {
        Strips s = new Strips();
        s.StripsAlgorithm();
    }

}
