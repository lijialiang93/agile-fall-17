import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Verify_GEDCOM {
	
	public static final List<String> Level = Arrays.asList("0", "1", "2");
	
	//tag
	public static final List<String> level0_2 = Arrays.asList("NOTE");
	public static final List<String> level0_2_noarg = Arrays.asList("HEAD", "TRLR");
	public static final List<String> level0_3 = Arrays.asList("INDI", "FAM");
	public static final List<String> level1 = Arrays.asList("NAME", "SEX", "FAMC", "FAMS", 
								"HUSB", "WIFE", "CHIL");
	public static final List<String> level1_noarg = Arrays.asList("BIRT", "DEAT", "MARR", "DIV");
	public static final List<String> level2 = Arrays.asList("DATE");
	
	public static final List<String> month = Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY",
								"JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
	
	public static List<String> readGEDCOMFileIntoArrList (String filePath) {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			Reader reader = new FileReader(filePath);
			br = new BufferedReader(reader);
			String currentLine = null;
			while ((currentLine = br.readLine()) != null) {
				list.add(currentLine);
			}
		} catch (Exception e) {
			System.out.println("No such file or directory!");
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return list;
	}
	
	public static void writeToFile (String filename, List<String> output) throws IOException {
		BufferedWriter outputWriter = null;
		outputWriter = new BufferedWriter(new FileWriter(filename));
		for (int i = 0; i < output.size(); i++) {
			outputWriter.write(output.get(i));
			if (i != output.size() - 1)
				outputWriter.newLine();
		}
		outputWriter.flush();
		outputWriter.close();
	}
	
	public static String verifyGEDCOM (String line) {
		String afterSplit[] = line.split(" ", 3);
		String result = null;
		
		String level = afterSplit[0];
		String tag = null;
		String argument = null;
		String valid = null;
		
		
		if (Level.contains(level)) {
			
			switch (afterSplit[0]) {
			
			case "0":
				if (afterSplit.length == 2) {
					if (level0_2_noarg.contains(afterSplit[1])) {
						tag = afterSplit[1];
						valid = "Y";
						argument = "";
					}
					else {
						tag = afterSplit[1];
						valid = "N";
						argument = "";
					}
				}
				else if (afterSplit.length == 3) {
					if (level0_2.contains(afterSplit[1])) {
						tag = afterSplit[1];
						valid = "Y";
						argument = afterSplit[2];
					}
					else if (level0_3.contains(afterSplit[2])) {
						tag = afterSplit[2];
						valid = "Y";
						argument = afterSplit[1];
					}
					else if (level0_2_noarg.contains(afterSplit[1])) {
						tag = afterSplit[1];
						valid = "Y";
						argument = "";
					}
					else {
						tag = afterSplit[1];
						valid = "N";
						if (afterSplit.length == 2) {
							argument = "";
						}
						else {
							argument = afterSplit[2];
						}
					}
				}
				else {
					tag = "";
					valid = "N";
					argument = "";
				}
				result = "<-- " + level + "|" + tag + "|" + valid + "|" + argument;
				break;
				
			case "1":
				if (afterSplit.length == 2) {
					if (level1_noarg.contains(afterSplit[1])) {
						tag = afterSplit[1];
						valid = "Y";
						argument = "";
					}
					else {
						tag = afterSplit[1];
						valid = "N";
						argument = "";
					}
				}
				else if (afterSplit.length == 3) {
					if (level1.contains(afterSplit[1])) {
						tag = afterSplit[1];
						valid = "Y";
						argument = afterSplit[2];
						if (tag.equals("SEX")) {
							if (argument.equals("M") || argument.equals("F")) {
								valid = "Y";
							}
							else
								valid = "N";
						}
					}
					else if (level1_noarg.contains(afterSplit[1])) {
						tag = afterSplit[1];
						valid = "Y";
						argument = "";
					}
					else {
						tag = afterSplit[1];
						valid = "N";
						argument = afterSplit[2];
					}
				}
				else {
					tag = "";
					valid = "N";
					argument = "";
				}
				result = "<-- " + level + "|" + tag + "|" + valid + "|" + argument;
				break;
				
			case "2":
				if (afterSplit.length == 2) {
					tag = afterSplit[1];
					valid = "N";
					argument = "";
				}
				else if (afterSplit.length == 3) {
					if (level2.contains(afterSplit[1])) {
						String date[] = afterSplit[2].split(" ", 3);
						int daysMaxInMonth = 0;
						if (date.length == 3) {
							if (month.contains(date[1])) {
								switch (date[1]) {
								
								case "JAN":
								case "MAR":
								case "MAY":
								case "JUL":
								case "AUG":
								case "OCT":
								case "DEC":
									daysMaxInMonth = 31;
									break;
									
								case "FEB":
									int year = Integer.parseInt(date[2]);
									if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
										daysMaxInMonth = 29;
									}
									else
										daysMaxInMonth = 28;
									break;
									
								default:
									daysMaxInMonth = 30;
									break;
								}
								
								int day = Integer.parseInt(date[0]);
								if (day > 0 && day <= daysMaxInMonth) {
									tag = afterSplit[1];
									valid = "Y";
									argument = afterSplit[2];
								}
								else {
									tag = afterSplit[1];
									valid = "N";
									argument = afterSplit[2];
								}
							}
						}
					}
					else {
						tag = afterSplit[1];
						valid = "N";
						argument = afterSplit[2];
					}
				}
				else {
					tag = afterSplit[1];
					valid = "N";
					argument = afterSplit[2];
				}
				result = "<-- " + level + "|" + tag + "|" + valid + "|" + argument;
				break;
			
			default:
				if (afterSplit.length == 2) {
					tag = afterSplit[1];
					valid = "N";
					argument = "";
				}
				else if (afterSplit.length == 3) {
					tag = afterSplit[1];
					valid = "N";
					argument = afterSplit[2];
				}
				else {
					tag = "";
					valid = "N";
					argument = "";
				}
				result = "<-- " + level + "|" + tag + "|" + valid + "|" + argument;
				break;
			}
		}
		else {
			tag = afterSplit[1];
			valid = "N";
			argument = afterSplit[2];
			result = "<-- " + level + "|" + tag + "|" + valid + "|" + argument;
		}
		return result;
	}

	public static void main(String[] args) {
		List<String> list = readGEDCOMFileIntoArrList("proj02test.ged");
		List<String> outputList = new ArrayList<String>();
		String outputFilename = "result.ged";
		
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				String currentLine = list.get(i);
				outputList.add("--> " + currentLine);
				outputList.add(verifyGEDCOM(currentLine));
				//System.out.println("--> " + currentLine);
				//System.out.println(verifyGEDCOM(currentLine));
			}
			try {
				writeToFile(outputFilename, outputList);
				System.out.println("The result is output to filename: " + outputFilename);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
