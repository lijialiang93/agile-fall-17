import classes.Family;
import classes.Gedcom;
import classes.Individual;
import classes.IndividualRelFamily;
import utils.DBUtils;
import utils.FileUtils;
import utils.PrettyTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        try {
            init(System.getProperty("user.dir") + "/input.ged");

            Individual individual = Individual.findById(16);
            System.out.println(individual.name);

            Family family = Family.findById(5);
            System.out.println(family.members.size());

            System.out.println(Family.all().size());
            
            output();

//            ArrayList<Individual> AllIndividuals = (ArrayList) Individual.all(); //begining of printing individuals
//            System.out.println("Individuals: ");
//
//            for (int i = 0; i < AllIndividuals.size(); i++) {
//
//                String spouse = " ";
//                String child = " ";
//
//                System.out.print(AllIndividuals.get(i).id + " " +
//                        AllIndividuals.get(i).name + " " +
//                        AllIndividuals.get(i).gender + " " +
//                        AllIndividuals.get(i).birthday + " " +
//                        AllIndividuals.get(i).age + " " +
//                        AllIndividuals.get(i).isAlive + " " +
//                        ((AllIndividuals.get(i).death == null) ? "NA" : AllIndividuals.get(i).death) + " ");
//
//                System.out.println();
//            }
//
//
//            // end of printing individuals
//
//            ArrayList<Family> AllFamilies = (ArrayList) Family.all(); // beginning of printing families
//
//            System.out.println("Families: ");
//            for (int i = 0; i < AllFamilies.size(); i++) {
//
//                Family Cfamily = AllFamilies.get(i); // current family
//                System.out.print(Cfamily.id + " " +
//                        ((Cfamily.married == null) ? "NA" : Cfamily.married) + " " +
//
//                        ((Cfamily.divorced == null) ? "NA" : Cfamily.divorced)
//                        + " ");
//
//                ArrayList<IndividualRelFamily> famMembers = (ArrayList) Cfamily.members;
//
//                String hID = "";
//                String wID = "";
//                ArrayList<String> chiID = new ArrayList<String>();
//
//                for (int j = 0; j < famMembers.size(); j++) {
//
//                    if (famMembers.get(j).role.equals("H")) {
//                        hID = famMembers.get(j).individualId;
//
//                    } else if (famMembers.get(j).role.equals("W")) {
//                        wID = famMembers.get(j).individualId;
//
//                    } else if (famMembers.get(j).role.equals("C")) {
//                        chiID.add(famMembers.get(j).individualId);
//                    }
//
//                }
//
//                System.out.print(hID + " " + Individual.findById(hID).name + " " + wID + " " + Individual.findById(wID).name);
//
//                for (int z = 0; z < chiID.size(); z++) {
//
//                    System.out.print(" " + chiID.get(z) + " ");
//                }
//                System.out.println();
//            }

            // end of printing families

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Note: The following code is for the CLI which we may need to implement later
//
//        Scanner scanner = new Scanner(System.in);
//
//        while (true) {
//            System.out.print("gedcom> ");
//
//            String cmd = scanner.nextLine();
//
//            try {
//                switch (cmd) {
//                    case "quit":
//                    case "exit":
//                        return;
//                    case "clean":
//                        clean();
//                        break;
//                    case "init":
//                        System.out.print("enter gedcom file path: ");
//                        String line = scanner.nextLine();
//                        init(line);
//                        break;
//                    default:
//                        System.out.println("command \"" + cmd + "\" not supported");
//                        break;
//                }
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
    }
    
    private static void output() throws Exception {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	
    	ArrayList<Individual> allIndividuals = (ArrayList<Individual>) Individual.all();
    	
    	System.out.println("Individuals: ");
        PrettyTable iTable = new PrettyTable("ID", "Name", "Gender", "Age", "Alive", "Birthday", "Death");
        for (Individual individual: allIndividuals) 
        	iTable.addRow("I" + String.valueOf(individual.id), individual.name, individual.gender, String.valueOf(individual.age), individual.isAlive? "True" : "False", individual.birthday != null? sdf.format(individual.birthday) : "NA", individual.death != null? sdf.format(individual.death): "NA");
        System.out.println(iTable);
        
        ArrayList<Family> allFamilies = (ArrayList<Family>) Family.all();
        
        System.out.println("Families: ");
        PrettyTable fTable = new PrettyTable("ID", "Married", "Divorced");
        for (Family family: allFamilies)
        	fTable.addRow("F" + String.valueOf(family.id), family.married != null? sdf.format(family.married) : "NA", family.divorced != null? sdf.format(family.divorced) : "NA");
        System.out.println(fTable);
    }

    private static void clean() throws Exception {
        DBUtils.update("delete from families where 1;");
        DBUtils.update("delete from individuals where 1;");
    }

    private static void init(String file) throws Exception {
        clean();

        String content = FileUtils.read(file);
        Gedcom gedcom = new Gedcom(content);
    }

}
