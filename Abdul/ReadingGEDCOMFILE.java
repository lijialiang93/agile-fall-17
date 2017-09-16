import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ReadingGEDCOMFILE {
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
	    ArrayList<String> validTags = new ArrayList<>(Arrays.asList("INDI","NAME","SEX","BIRT","DEAT","FAMC", "FAMS","FAM", "MARR","HUSB","WIFE","CHIL",
	    		"DIV", "DATE","HEAD","TRLR", "NOTE"));
	    
	    try{
        File file = new File("/Users/abdulaziz/Desktop/CS_555/My-Family-4-Sep-2017-888.ged");
        
        PrintWriter outputStream = new PrintWriter("output.txt");
        
        FileInputStream fstream = new FileInputStream(file);
            
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(fstream));
        Scanner in = new Scanner(fstream);
    
        String strLine;
        String [] tempStr;
        
        while ((strLine = br.readLine()) != null)   {

            	
        	if(strLine.length() >= 1){
        		System.out.println();
        		System.out.println("--> "+strLine);
        		outputStream.print("--> "+strLine);
        		outputStream.println();
        	}
            
           
            if(!strLine.equals("")){ // this line to ignore empty lines
                
                if(strLine.contains(" ")){
                                                                                
                    tempStr = strLine.split(" ");

                    String valid = " ";
                    
                    for(int i = 0; i < tempStr.length; i++){
                    	                        	                        	
                    	if(i == 0 && tempStr.length > 1){
                    		                    		
                    		if(tempStr[i].equals("0")){
                    			if(tempStr.length == 2){
                    				if(validTags.contains(tempStr[i+1])){
                    					valid = "|Y|";
                    				}else{
                    					valid = "|N|";
                    				}
                    			}else if(tempStr.length == 3){
                        			if(validTags.contains(tempStr[2])){
                        				valid = "|Y|";
                        			}else{
                        				valid = "|N|";
                        			}
                    			}
                		
                    		}// end of if-stmnt for 0
                    		
                    		if(tempStr[i].equals("1") || tempStr[i].equals("2")){
                    			
                    			if (tempStr[i].equals("1")){
                    				
                    				if((tempStr[i+1]).equals("DATE")){
                    					valid = "|N|";                    					
                    				
                    				}else if(validTags.contains(tempStr[i+1])){
                    					
                    					valid = "|Y|";
                    				}else {
                    					valid = "|N|";
                    				}
                    				
                    			}// end of if-stmnt for level 1
                    			
                    			if (tempStr[i].equals("2")){
                    				if((tempStr[i+1]).equals("DATE")){
                    					valid = "|Y|";                    					                    				
                    				}else {
                    					valid = "|N|";
                    				}
                    				
                    			} // end of if-stmnt for level 2

                    		}
                    	} //end of  if(i == 0 && tempStr.length > 1)
                    	
                    	if(i == 0){
                    		
                    		if(tempStr[i].equals("0")){
                    			if (tempStr.length == 3){
                    				
                    				String temp;
                    				temp = tempStr[1];
                    				tempStr[1] = tempStr[2];
                    				tempStr[2] = temp;
                    			}
                    		}
                    	}

                    	if( i == 0){                    		                    		
                    		
                    		System.out.print("<-- " + tempStr[i] + " ");
                    		outputStream.print("<-- " + tempStr[i] + " ");
                    		
                    	}else if (i == 1){
                    		
                    		System.out.print("|"+ tempStr[i] );
                    		System.out.print(valid);
                    		
                    		outputStream.print("|"+ tempStr[i] );
                    		outputStream.print(valid);
                    	
                    	}else {
                    		
                    		System.out.print(tempStr[i] + " " );
                    		outputStream.print(tempStr[i] + " " );
                    	}
                    	
                    	
                    	
                    }// end of foor-loop                                       
                    
                }                    
            }
            outputStream.println();

        }
        outputStream.close();
	    }
        catch(IOException e){
            
            System.out.println("file not found!");
        }
        
        catch(NullPointerException n){
            
            System.out.println("File not found!");
        }
	}
}
	
        
	


