package proj;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vinay
 */
public class Corrector {
    private HashSet<String> wlist2 = new HashSet<String>();
    private HashSet<String> wlist1 = new HashSet<String>();
    private HashSet<String> wlist3 = new HashSet<String>();
    
    public Corrector(){
        Dictionary.setDictionary();
        wlist2=Dictionary.getDictionary();
    }
    
    public HashSet<String> listCorrections(String word){
        wlist3.clear();
        this.permute(word);
        this.insert(word);
        this.replace(word);
        this.delete(word);
	for(String str : wlist1){
		
		if(wlist2.contains(str)){
			wlist3.add(str);
                        System.out.println(str);
		}
			
	}
	//System.out.println("...........");
	//System.out.println(wlist1.size());
	//System.out.println(wlist2.size());
	//System.out.println(i);
	wlist1.clear();
	wlist2.clear();
        return wlist3;
    }
    
    public void permute( String input){
    
    int inputLength = input.length();
    boolean[ ] used = new boolean[ inputLength ];
    StringBuffer outputString = new StringBuffer();
    char[ ] in = input.toCharArray( );
  
    doPermute ( in, outputString, used, inputLength, 0 );

}

    public void doPermute ( char[ ] in, StringBuffer outputString, 
                    boolean[ ] used, int inputlength, int level){
     if( level == inputlength) {
     //System.out.println ( outputString.toString()); 
        wlist1.add(outputString.toString());
        return;
     }

    for( int i = 0; i < inputlength; ++i ) {       

       if( used[i] ) 
           continue;

       outputString.append( in[i] );      
       used[i] = true;       
       doPermute( in,   outputString, used, inputlength, level + 1 );       
       used[i] = false;       
       outputString.setLength(   outputString.length() - 1 );   
    }
 }
    public void insert(String word){
      
	  int length = word.length();
          HashSet<String> alphabets = new HashSet<String>();
          String x=null;
          String y=null;
	  for(int i=0;i<length;i++){
              
		  x = word.substring(0,i);
		  y = word.substring(i);
		  
		  try{
                    BufferedReader br = new BufferedReader(new FileReader("letters.txt"));
                    String line;
                    while((line = br.readLine()) != null) {
	             // do something with line.
	        	alphabets.add(line);
                    }
			br.close();
                  }catch(Exception e){
			System.err.println("Error: " + e.getMessage());
                  }
			
                  for(String a : alphabets){
			  String m = x+a+y;
			  //System.out.println(m);
			  wlist1.add(m);
		  }
               }
		  
		  
	  }
    
    public void delete(String word){
	  int length = word.length();
	  for(int i=0;i<length;i++){
		  String x=word.substring(0,i);
		  String y=word.substring(i+1);
		  String m = x+y;
		  //System.out.println(m);
		  wlist1.add(m);
	  }
  }
    
    public void replace(String word){
	  int length = word.length();
          HashSet<String> alphabets = new HashSet<String>();
          //String x=null;
          //String y=null;
	  for(int i=0;i<length;i++){
              
		  String x = word.substring(0,i);
		  String y = word.substring(i+1);
		  
		  try{
                    BufferedReader br = new BufferedReader(new FileReader("letters.txt"));
                    String line;
                    while((line = br.readLine()) != null) {
	             // do something with line.
	        	alphabets.add(line);
                    }
			br.close();
                  }catch(Exception e){
			System.err.println("Error: " + e.getMessage());
                  }
                  
                  for(String a : alphabets){
			  String m = x+a+y;
			  //System.out.println(m);
			  wlist1.add(m);
		  }
				
               }
		  
  }
}
