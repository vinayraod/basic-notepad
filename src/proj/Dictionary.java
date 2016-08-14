/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package proj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Vinay
 */
public class Dictionary {
    
    private static HashSet<String> dictionary =new HashSet<String>();
    private static ArrayList<String> dictionary2=new ArrayList<String>();
    
    public static void setDictionary(){
        try{
		BufferedReader br = new BufferedReader(new FileReader("dictionary.txt"));
	        String line;
	        while((line = br.readLine()) != null) {
	             // do something with line.
	        	dictionary.add(line);
                dictionary2.add(line);
	        }
	        
			br.close();
		}catch(Exception e){
			System.err.println("Error: " + e.getMessage());
		}
    }
    
    public static HashSet<String> getDictionary(){
        return dictionary;
        
    }
    
    public static ArrayList<String> getDictionary2(){
        return dictionary2;
    }
}
