/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxlibraryassistant.ui.check;

import java.util.ArrayList;

/**
 *
 * @author ChanMyaeOo
 */
public class CheckInfo {
  
    ArrayList<Long> finePerDay = new ArrayList<>();
    public void setFinePerDay(long finePerDay){
        this.finePerDay.add(finePerDay);
    }
    public ArrayList<Long> getFinePerDay(){
        return finePerDay;
    }
   
    
}
