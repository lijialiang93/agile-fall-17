
import classes.Family;
import classes.Gedcom;
import classes.Individual;
import classes.IndividualRelFamily;
import utils.DBUtils;
import utils.FileUtils;
import utils.PrettyTable;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

	public static void main(String[] args) {
		try {
			clean();
			init(System.getProperty("user.dir") + "/input.ged");
			printTable();
			//
			// Individual individual = Individual.findById(16);
			// System.out.println(individual.name);
			//
			// Family family = Family.findById(5);
			// System.out.println(family.members.size());
			//
			// System.out.println(Family.all().size());
			//
			// //output();
			//
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// PrettyTable table = new PrettyTable("ID", "Name", "Gender",
			// "Birthday", "Death");
			// table.addRow(""+individual.id, individual.name,
			// individual.gender, sdf.format(individual.birthday),
			// individual.death != null? sdf.format(individual.death): "");
			// //System.out.println(table);
			//
			// print();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Note: The following code is for the CLI which we may need to
		// implement later
		//
		// Scanner scanner = new Scanner(System.in);
		//
		// while (true) {
		// System.out.print("gedcom> ");
		//
		// String cmd = scanner.nextLine();
		//
		// try {
		// switch (cmd) {
		// case "quit":
		// case "exit":
		// return;
		// case "clean":
		// clean();
		// break;
		// case "init":
		// System.out.print("enter gedcom file path: ");
		// String line = scanner.nextLine();
		// init(line);
		// break;
		// default:
		// System.out.println("command \"" + cmd + "\" not supported");
		// break;
		// }
		// } catch (Exception e) {
		// System.out.println(e.getMessage());
		// }
		// }
	}

	private static void printTable() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		List<Individual> indiList;

		indiList = Individual.all();

		List<Family> famList = Family.all();

		PrettyTable table1 = new PrettyTable("ID", "Name", "Gender", "Birthday", "Age", "Alive", "Death", "Child",
				"Spouse");
		PrettyTable table2 = new PrettyTable("ID", "Married", "Divorced", "Husband ID", "Husband Name", "Wife ID",
				"Wife Name", "Children");

		for (int i = 0; i < indiList.size(); i++) {
			Individual indi = indiList.get(i);
			List<IndividualRelFamily> irfList = IndividualRelFamily.findBy("individual_id", String.valueOf(indi.id));
			IndividualRelFamily irf = null;
			String id = "I";
			String cFamily = "{'";
			String sFamily = "{'";
			String role = "";
			id += String.valueOf(indi.id);
			if (irfList.size() != 0) {
				for (int j = 0; j < irfList.size(); j++) {
					irf = irfList.get(j);
					if (irf.role.equals("C")) {
						cFamily += "F" + String.valueOf(irf.familyId) + "',";
					} else if (irf.role.equals("W") || irf.role.equals("H")) {
						sFamily += "F" + String.valueOf(irf.familyId) + "',";
					}
					role += irf.role;
				}

			}
			cFamily = cFamily.substring(0, cFamily.length() - 2) + "'}";
			sFamily = sFamily.substring(0, sFamily.length() - 2) + "'}";
			table1.addRow(id, indi.name, indi.gender, sdf.format(indi.birthday), String.valueOf(indi.age),
					String.valueOf(indi.isAlive), indi.death != null ? sdf.format(indi.death) : "N/A",
					role.contains("C") ? cFamily : "N/A", role.contains("W") || role.contains("H") ? sFamily : "N/A");

		}

		for (int i = 0; i < famList.size(); i++) {
			Family fam = famList.get(i);
			int famId = fam.id;
			String married = null;
			String divorced = null;
			if (fam.married != null) {
				married = sdf.format(fam.married);
			}
			if (fam.divorced != null) {
				divorced = sdf.format(fam.divorced);
			}

			List<IndividualRelFamily> irfList = fam.members;
			String wName = null;
			String hName = null;
			String wId = null;
			String hId = null;
			String cId = "{";
			for (int j = 0; j < irfList.size(); j++) {
				IndividualRelFamily irf = irfList.get(j);
				if (irf.role.equals("W")) {
					wId = String.valueOf(irf.individualId);
					wName = Individual.findById(Integer.parseInt(wId)).name;
				} else if (irf.role.equals("H")) {
					hId = String.valueOf(irf.individualId);
					hName = Individual.findById(Integer.parseInt(hId)).name;
				} else if (irf.role.equals("C")) {
					cId += "'I" + irf.individualId + "',";
				}
			}
			hId = "I" + hId;
			wId = "I" + wId;
			cId = cId.substring(0, cId.length() - 1);
			cId += "}";

			table2.addRow(String.valueOf(famId), married != null ? married : "N/A", divorced != null ? divorced : "N/A",
					hId != null ? hId : "N/A", hName != null ? hName : "N/A", wId != null ? wId : "N/A",
					wName != null ? wName : "N/A", cId != "{" ? cId : "N/A");

		}
		System.out.println(table1);
		System.out.println(table2);

	}

	// private static void output() throws Exception {
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//
	// ArrayList<Individual> allIndividuals = (ArrayList<Individual>)
	// Individual.all();
	//
	// System.out.println("Individuals: ");
	// PrettyTable iTable = new PrettyTable("ID", "Name", "Gender", "Age",
	// "Alive", "Birthday", "Death");
	// for (Individual individual : allIndividuals)
	// iTable.addRow("I" + String.valueOf(individual.id), individual.name,
	// individual.gender,
	// String.valueOf(individual.age), individual.isAlive ? "True" : "False",
	// individual.birthday != null ? sdf.format(individual.birthday) : "NA",
	// individual.death != null ? sdf.format(individual.death) : "NA");
	// System.out.println(iTable);
	//
	// ArrayList<Family> allFamilies = (ArrayList<Family>) Family.all();
	//
	// System.out.println("Families: ");
	// PrettyTable fTable = new PrettyTable("ID", "Married", "Divorced");
	// for (Family family : allFamilies)
	// fTable.addRow("F" + String.valueOf(family.id), family.married != null ?
	// sdf.format(family.married) : "NA",
	// family.divorced != null ? sdf.format(family.divorced) : "NA");
	// System.out.println(fTable);
	// }

	private static void clean() throws Exception {
		DBUtils.update("delete from families where 1;");
		DBUtils.update("delete from individuals where 1;");
	}

	private static void init(String file) throws Exception {
		clean();

		String content = FileUtils.read(file);
		Gedcom gedcom = new Gedcom(content);
	}

	// private static void print() throws Exception {
	//
	// PrintWriter outputStream = new PrintWriter("output.txt");
	//
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//
	// // start here:
	// ArrayList<Individual> AllIndividuals = (ArrayList) Individual.all();
	// ArrayList<Family> AllFamilies = (ArrayList) Family.all();
	//
	// // begining of printing individuals
	// System.out.println("Individuals:");
	// outputStream.println("Individuals:");
	//
	// PrettyTable Itable = new PrettyTable("ID", "Name", "Gender", "Birthday",
	// "Age", "Alive", "Death", "Child",
	// "Spouse");
	//
	// for (int i = 0; i < AllIndividuals.size(); i++) {
	//
	// ArrayList<String> spouse = new ArrayList<String>();
	// ArrayList<String> children = new ArrayList<String>();
	// String spList = "{";
	// String chList = "{";
	//
	// for (int j = 0; j < AllFamilies.size(); j++) {
	// ArrayList<IndividualRelFamily> famMembers = (ArrayList)
	// AllFamilies.get(j).members;
	//
	// for (int z = 0; z < famMembers.size(); z++) {
	//
	// if (famMembers.get(z).individualId == AllIndividuals.get(i).id) {
	//
	// if (famMembers.get(z).role.equals("H") ||
	// famMembers.get(z).role.equals("W")) {
	// spouse.add("" + AllFamilies.get(j).id);
	//
	// } else if (famMembers.get(z).role.equals("C")) {
	// children.add("" + AllFamilies.get(j).id);
	// }
	// }
	// }
	//
	// }
	//
	// for (int z = 0; z < spouse.size(); z++) {
	//
	// // getting list of all souses in this family
	// spList += spouse.get(z);
	//
	// if (z != spouse.size() - 1) {
	// spList += ", ";
	// }
	// }
	// spList += "}";
	//
	// for (int z = 0; z < children.size(); z++) {
	//
	// // getting list of all children in this family
	// chList += children.get(z);
	//
	// if (z != children.size() - 1) {
	// chList += ", ";
	// }
	// }
	// chList += "}";
	//
	// Itable.addRow("" + AllIndividuals.get(i).id, AllIndividuals.get(i).name,
	// AllIndividuals.get(i).gender,
	// sdf.format(AllIndividuals.get(i).birthday), "" +
	// AllIndividuals.get(i).age,
	// "" + AllIndividuals.get(i).isAlive,
	// ((AllIndividuals.get(i).death == null) ? "NA" :
	// sdf.format(AllIndividuals.get(i).death)), chList,
	// spList);
	//
	// }
	// System.out.println(Itable);
	// outputStream.println(Itable);
	//
	// // end of printing individuals
	//
	// System.out.println("Families: ");
	// outputStream.println("Families:");
	// // beginning of printing families
	// PrettyTable Ftable = new PrettyTable("ID", "Married", "Divorced",
	// "Husband ID", "Husband Name", "Wife ID",
	// "Wife Name", "Children");
	//
	// for (int i = 0; i < AllFamilies.size(); i++) {
	//
	// Family Cfamily = AllFamilies.get(i); // current family
	//
	// ArrayList<IndividualRelFamily> famMembers = (ArrayList) Cfamily.members;
	//
	// String hID = "";
	// String wID = "";
	// String ChList = "{";
	//
	// ArrayList<String> chiID = new ArrayList<String>();
	//
	// for (int j = 0; j < famMembers.size(); j++) {
	//
	// if (famMembers.get(j).role.equals("H")) {
	// hID = "" + famMembers.get(j).individualId;
	//
	// } else if (famMembers.get(j).role.equals("W")) {
	// wID = "" + famMembers.get(j).individualId;
	//
	// } else if (famMembers.get(j).role.equals("C")) {
	// chiID.add("" + famMembers.get(j).individualId);
	// }
	//
	// }
	//
	// for (int z = 0; z < chiID.size(); z++) {
	//
	// // getting list of all children in this family
	// ChList += chiID.get(z);
	//
	// if (z != chiID.size() - 1) {
	// ChList += ", ";
	// }
	// }
	// ChList += "}";
	//
	// Ftable.addRow("" + Cfamily.id, Cfamily.married != null ?
	// sdf.format(Cfamily.married) : "NA",
	// Cfamily.divorced != null ? sdf.format(Cfamily.divorced) : "NA", hID,
	// Individual.findById(Integer.parseInt(hID)).name, wID,
	// Individual.findById(Integer.parseInt(wID)).name, ChList);
	// // System.out.println();
	// }
	//
	// System.out.println(Ftable);
	// outputStream.println(Ftable);
	// // end of printing families
	// outputStream.close();
	// }

}