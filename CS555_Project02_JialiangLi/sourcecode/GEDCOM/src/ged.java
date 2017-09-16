/**
 * 
 * @author Jialiang Li
 *
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ged {
	public static HashMap<String, Integer> map = new HashMap<String, Integer>();
	public static final String[] TAGS = { "INDI-0", "NAME-1", "SEX-1", "BIRT-1", "DEAT-1", "FAMC-1", "FAMS-1", "FAM-0",
			"MARR-1", "HUSB-1", "WIFE-1", "CHIL-1", "DIV-1", "DATE-2", "HEAD-0", "TRLR-0", "NOTE-0" };

	public static void main(String[] args) {
		createMap(TAGS);
		File in = new File("lib/Jialiang-Li-Family-5-Sep-2017-688.ged");
		File out = new File("lib/output.txt");
		if(!out.exists()){
			try {
				out.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		    
		readAndPrint(in,out);
	}
	
	//create hashmap
	public static void createMap(String[] tags) {

		for (int i = 0; i < tags.length; i++) {
			String[] tagAndLevel = tags[i].split("-");
			String tag = tagAndLevel[0];
			int level = Integer.parseInt(tagAndLevel[1]);
			map.put(tag, level);
		}
	}
	
	
	//read and print
	public static void readAndPrint(File in,File out) {
		FileReader fr;
		FileWriter fw;
		try {
			fr = new FileReader(in);
			fw = new FileWriter(out);
			BufferedReader br = new BufferedReader(fr);
			BufferedWriter bw = new BufferedWriter(fw);
			String line;
			while ((line = br.readLine()) != null) {
		
				String level;
				String tag = "";
				String argument= "";
				String valid ="N";
				int argPos;
				boolean check = true;
				
				
				//display input
				String output1 = "-->" + line;
				bw.write(output1);
				bw.newLine();
				bw.flush();
				System.out.println(output1);
				
				String[] strs = line.split(" ");
				//get level
				level = strs[0];


				// 0 <id> INDI
				// 0 <id> FAM
				if (level.equals("0") && strs.length == 3) {
					// tag is the third token
					if (strs[2].equals("INDI") || strs[2].equals("FAM")) {
						//valid situation
						tag = strs[2];
						argPos = 1;
					}
					else if (strs[1].equals("INDI") || strs[1].equals("FAM")){
						//invalid situation
						tag = strs[1];
						argPos = 2;
						check = false;
					}
					else{
						//other 3 tokens valid situation in level 0
						tag = strs[1];
						argPos = 2;
					}
				} 
				else {
					//tag is the second token
					tag = strs[1];
					argPos = 2;
				}
				
				//check validity
				if (map.get(tag) != null) {
					if (map.get(tag).toString().equals(level)&&check==true) {
						valid = "Y";
					}
				}
				
				//argument behind tag
				if (argPos == 2) {
					for (int i = argPos; i < strs.length; i++) {
						argument += strs[i] + " ";
					}
				} 
				//tag behind <id>
				else if(argPos==1){
					argument = strs[argPos];
				}
				
				//output
				String output2 = "<--" + level + "|" + tag + "|" + valid + "|" + argument;
				bw.write(output2);
				bw.newLine();
				bw.flush();
				System.out.println(output2);
				
			}
			br.close();
			bw.close();
			fr.close();
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
