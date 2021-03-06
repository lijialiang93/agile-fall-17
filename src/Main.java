
import classes.Family;
import classes.Gedcom;
import classes.Individual;
import classes.IndividualRelFamily;
import utils.DBUtils;
import utils.FileUtils;
import utils.PrettyTable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Main {

	public static void main(String[] args) throws Exception {
		try {
            String dir = System.getProperty("user.dir");

			clean();
            init(dir + "/input.ged");

            PrettyTable individualTable = createIndividualTable(Individual.all());
            PrettyTable borninLast30DaysTable = createIndividualTable(Individual.borninLast30Days(Individual.all()));
            PrettyTable familyTable = createFamilyTable();

            StringBuilder sb = new StringBuilder();
            sb.append("Individuals:\r\n");
            sb.append(individualTable.toString());
            sb.append("\r\nFamilies:\r\n");
            sb.append(familyTable.toString());
            
            sb.append("Individuals born in the last 30 days:\r\n");
            sb.append(borninLast30DaysTable.toString());
            FileUtils.write(dir + "/output.txt", sb.toString());

            System.out.println(sb.toString());
        } catch (Exception e) {
			e.printStackTrace();
		}

		//Individual.borninLast30Days(Individual.all());
		
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

    private static PrettyTable createIndividualTable(List<Individual> indiList) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        //List<Individual> indiList = Individual.all(); 
        PrettyTable res = new PrettyTable("ID", "Name", "Gender", "Birthday", "Age", "Alive", "Death", "Child", "Spouse");

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
            res.addRow(id, indi.name, indi.gender, sdf.format(indi.birthday), String.valueOf(indi.age),
                    indi.isAlive ? "True" : "False", indi.death != null ? sdf.format(indi.death) : "N/A",
                    role.contains("C") ? cFamily : "N/A", role.contains("W") || role.contains("H") ? sFamily : "N/A");

        }

        return res;
    }

    private static PrettyTable createFamilyTable() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        List<Family> famList = Family.all();
        PrettyTable res = new PrettyTable("ID", "Married", "Divorced", "Husband ID", "Husband Name", "Wife ID",
                "Wife Name", "Children");

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
                    cId += "'I" + irf.individualId + "', ";
                }
            }
            hId = "I" + hId;
            wId = "I" + wId;
            cId = cId.substring(0, cId.length() - 2);
            cId += "}";

            res.addRow("F" + famId, married != null ? married : "N/A", divorced != null ? divorced : "N/A",
                    hId != null ? hId : "N/A", hName != null ? hName : "N/A", wId != null ? wId : "N/A",
                    wName != null ? wName : "N/A", cId != "{" ? cId : "N/A");

        }

        return res;
    }

	private static void clean() throws Exception {
		DBUtils.update("delete from families where 1;");
		DBUtils.update("delete from individuals where 1;");
		DBUtils.update("delete from individual_rel_family where 1;");
	}

	private static void init(String file) throws Exception {
		clean();

		String content = FileUtils.read(file);
		Gedcom gedcom = new Gedcom(content);
	}

}