/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pack;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author momo and pulak
 */
public class Operator {
    private String name;
    private Constant parameter1;
    private Constant parameter2;
    private List<Predicate> preconditionsList;
    private List<Predicate> deleteList;
    private List<Predicate> addList;
    
    public Operator(){
        
    }
    
    public Operator(String name, Constant p1, Constant p2, List<Predicate> preconditionsList, List<Predicate> deleteList, List<Predicate> addList){
        this.name = name;
        this.parameter1 = p1;
        this.parameter2 = p2;
        this.preconditionsList = preconditionsList;
        this.deleteList = deleteList;
        this.addList = addList;
    }
    

    @Override
    public String toString() { 
       String res = " " + name;
       if(parameter1 != null){
           res += "(" + parameter1.getVal();
       }
       if(parameter2 != null){
           res += "," + parameter2.getVal() + ")";
       }
       else
           res += ") ";
       
       return res;
    }
    
    public static Operator get_Clone(Operator o){
        Operator newOp = new Operator();        
        newOp.setName(new String(o.getName()));

        Constant c1 = null, c2 = null;
        if(o.getParameter1() instanceof ConstantOffice){
            c1 = new ConstantOffice(o.getParameter1().getVal());
        }
        else if(o.getParameter1() instanceof ConstantCoffee){
            c1 = new ConstantCoffee(o.getParameter1().getVal());
        }
        
   
        if(o.getParameter2() instanceof ConstantOffice){
            c2 = new ConstantOffice(o.getParameter2().getVal());
        }
        else if(o.getParameter2() instanceof ConstantCoffee){
            c2 = new ConstantCoffee(o.getParameter2().getVal());
        }
        newOp.setParameter1(c1);
        newOp.setParameter2(c2);
        
        List<Predicate> newpredList = new ArrayList<>();
        List<Predicate> newdelList = new ArrayList<>();
        List<Predicate> newaddList = new ArrayList<>();
        
        for(int i = 0; i < o.getPreconditionsList().size(); i++)
            newpredList.add(i, Predicate.get_Clone(o.getPreconditionsList().get(i)));
        for(int i = 0; i < o.getDeleteList().size(); i++)
            newdelList.add(i, Predicate.get_Clone(o.getDeleteList().get(i)));
        for(int i = 0; i < o.getAddList().size(); i++)
            newaddList.add(i, Predicate.get_Clone(o.getAddList().get(i)));
        
        newOp.setPreconditionsList(newpredList);        
        newOp.setDeleteList(newdelList);
        newOp.setAddList(newaddList);
        
        return newOp;
    }
    
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the parameter1
     */
    public Constant getParameter1() {
        return parameter1;
    }

    /**
     * @param parameter1 the parameter1 to set
     */
    public void setParameter1(Constant parameter1) {
        this.parameter1 = parameter1;
    }

    /**
     * @return the parameter2
     */
    public Constant getParameter2() {
        return parameter2;
    }

    /**
     * @param parameter2 the parameter2 to set
     */
    public void setParameter2(Constant parameter2) {
        this.parameter2 = parameter2;
    }

    /**
     * @return the preconditionsList
     */
    public List<Predicate> getPreconditionsList() {
        return this.preconditionsList;
    }

    /**
     * @param preconditionsList the preconditionsList to set
     */
    public void setPreconditionsList(List<Predicate> preconditionsList) {
        this.preconditionsList = preconditionsList;
    }

    /**
     * @return the deleteList
     */
    public List<Predicate> getDeleteList() {
        return deleteList;
    }

    /**
     * @param deleteList the deleteList to set
     */
    public void setDeleteList(List<Predicate> deleteList) {
        this.deleteList = deleteList;
    }

    /**
     * @return the addList
     */
    public List<Predicate> getAddList() {
        return addList;
    }

    /**
     * @param addList the addList to set
     */
    public void setAddList(List<Predicate> addList) {
        this.addList = addList;
    }
    
    
    
         
}
