/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pack;

/**
 *
 * @author momo and pulak
 */
public class Predicate {
    private String predname;
    private Constant parameter1;
    private Constant parameter2;
    private int numberParams;
    
    public Predicate(){   
    }
    
    public Predicate(String predname, Constant parameter1, Constant parameter2){
        this.predname = predname;
        this.parameter1 = parameter1;
        this.parameter2 = parameter2;
        this.numberParams = 2;
    }
   
    @Override
    public String toString() { 
       String res = " " + predname;
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
    
    public static Predicate get_Clone(Predicate p){
        Predicate newP = new Predicate();
        newP.setPredname(new String(p.getPredname()));
        newP.numberParams = p.numberParams;
        Constant c1 = null, c2 = null,x = null;
        if(p.getParameter1() instanceof ConstantOffice){
            c1 = new ConstantOffice(p.getParameter1().getVal());
        }
        else if(p.getParameter1() instanceof ConstantCoffee){
            c1 = new ConstantCoffee(p.getParameter1().getVal());
        }
        else if(p.getParameter1() instanceof ConstantDistance){
            c1 = new ConstantDistance(p.getParameter1().getVal());
        }
        
        if(p.getParameter2() instanceof ConstantOffice){
            c2 = new ConstantOffice(p.getParameter2().getVal());
        }
        else if(p.getParameter2() instanceof ConstantCoffee){
            c2 = new ConstantCoffee(p.getParameter2().getVal());
        }
        newP.setParameter1(c1);
        newP.setParameter2(c2);
        
        return newP;
    }
    
    
    @Override
    public boolean equals(Object obj){
        if (obj == null) 
            return false;
        
        final Predicate other = (Predicate) obj;
        if (!Predicate.class.isAssignableFrom(obj.getClass())) 
            return false;
        
        switch (other.getNumberParams()) {
            case 0:
                if(this.predname.equals(other.predname))
                    return true;
                break;
            case 1:
                if(this.predname.equals(other.predname) && this.parameter1.equals(other.parameter1))
                    return true;
                break;
            case 2:
                if(this.predname.equals(other.predname) && this.parameter1.equals(other.parameter1) && this.parameter2.equals(other.parameter2))
                    return true;
                break;
            default:
                return false;
        }
        return false;
    }
    
    public Predicate(String predname){
        this.predname = predname;
        this.numberParams = 0;
    }
    
    public Predicate(String predname, Constant parameter1){
        this.predname = predname;
        this.parameter1 = parameter1;
        this.numberParams = 1;
    }

    /**
     * @return the predname
     */
    public String getPredname() {
        return predname;
    }

    /**
     * @param predname the predname to set
     */
    public void setPredname(String predname) {
        this.predname = predname;
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
     * @return the numberParams
     */
    public int getNumberParams() {
        return numberParams;
    }

    /**
     * @param numberParams the numberParams to set
     */
    public void setNumberParams(int numberParams) {
        this.numberParams = numberParams;
    }
    
    
}
