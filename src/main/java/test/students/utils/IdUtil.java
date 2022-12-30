/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students.utils;

import java.util.Random;
import java.util.UUID;


public class IdUtil {
    
    public static final String generate() {

        return UUID.randomUUID().toString();
    }
    
    
}
