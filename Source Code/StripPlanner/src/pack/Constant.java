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
public class Constant {
    private int val;
    
    public Constant(int val){
        this.val = val;
    }
    
    public Constant(){
        
    }
         
    @Override
    public boolean equals(Object obj){
        if (obj == null) 
            return false;
        
        final Constant other = (Constant) obj;
        if (!Constant.class.isAssignableFrom(obj.getClass())) 
            return false;
            
        return this.getVal() == other.getVal();
    }

    /**
     * @return the val
     */
    public int getVal() {
        return val;
    }

    /**
     * @param val the val to set
     */
    public void setVal(int val) {
        this.val = val;
    }
}
