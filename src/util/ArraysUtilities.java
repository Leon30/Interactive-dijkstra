/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import models.Label;

/**
 *
 * @author León
 */
public class ArraysUtilities <T>{
    
    public ArrayList<Label<T>> cloneLabels(ArrayList<Label<T>> al){
        ArrayList<Label<T>> re = new ArrayList();
        for (Label<T> al1 : al) {
            re.add(al1.clone());
        }
        return re;
    }
}
