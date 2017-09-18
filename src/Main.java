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

            Individual individual = Individual.findById("@I16@");
            System.out.println(individual.name);

            Family family = Family.findById("@F5@");
            System.out.println(family.members.size());

            System.out.println(Family.all().size());
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            PrettyTable table = new PrettyTable("ID", "Name", "Gender", "Birthday", "Death");
            table.addRow(individual.id, individual.name, individual.gender, sdf.format(individual.birthday), individual.death != null? sdf.format(individual.death): "");
            System.out.println(table);


            ArrayList<Individual> AllIndividuals = (ArrayList) Individual.all();
            ArrayList<Family> AllFamilies = (ArrayList) Family.all();
            
          //begining of printing individuals            
            PrettyTable Itable = new PrettyTable("ID", "Name", "Gender", "Birthday", "Age"
            		,"Alive", "Death", "Child", "Spouse");
            
            for (int i = 0; i < AllIndividuals.size(); i++) {

            	ArrayList<String> spouse = new ArrayList<String>();
            	ArrayList<String> children = new ArrayList<String>();
            	String spList = "{";
            	String chList = "{";
            	
                for(int j = 0; j < AllFamilies.size(); j++){
                	ArrayList<IndividualRelFamily> famMembers = (ArrayList) AllFamilies.get(j).members;
                	
                	for(int z = 0; z < famMembers.size(); z++){
                		
                		if(famMembers.get(z).individualId == AllIndividuals.get(i).id){
                            
                			if (famMembers.get(z).role.equals("H") || famMembers.get(z).role.equals("W")) {
                            	spouse.add(AllFamilies.get(j).id);

                            } else if (famMembers.get(z).role.equals("C")) {
                            	children.add(AllFamilies.get(j).id);
                            }
                		}
                	}
                	
                }
                
                for (int z = 0; z < spouse.size(); z++) {

                	// getting list of all souses in this family
                    spList += spouse.get(z);
                    
                    if(z != spouse.size() -1){
                    	spList += ", ";
                    }
                }
                spList += "}";
                
                for (int z = 0; z < children.size(); z++) {

                	// getting list of all children in this family
                    chList += children.get(z);
                    
                    if(z != children.size() -1){
                    	chList += ", ";
                    }
                }
                chList += "}";
                
                Itable.addRow(AllIndividuals.get(i).id, AllIndividuals.get(i).name, AllIndividuals.get(i).gender,
                		sdf.format(AllIndividuals.get(i).birthday),
                		""+AllIndividuals.get(i).age, ""+AllIndividuals.get(i).isAlive,
                		((AllIndividuals.get(i).death == null) ? "NA" : sdf.format(AllIndividuals.get(i).death)),
                		chList, spList);
                
//                System.out.print(AllIndividuals.get(i).id + " " +
//                        AllIndividuals.get(i).name + " " +
//                        AllIndividuals.get(i).gender + " " +
//                        AllIndividuals.get(i).birthday + " " +
//                        AllIndividuals.get(i).age + " " +
//                        AllIndividuals.get(i).isAlive + " " +
//                        ((AllIndividuals.get(i).death == null) ? "NA" : AllIndividuals.get(i).death) + " ");
//
//                System.out.println();
                
            }
         // end of printing individuals
                       
            System.out.println("Families: ");
         // beginning of printing families
            PrettyTable Ftable = new PrettyTable("ID", "Married", "Divorced", "Husband ID", "Husband Name"
            		,"Wife ID", "Wife Name", "Children");
            
            for (int i = 0; i < AllFamilies.size(); i++) {

                Family Cfamily = AllFamilies.get(i); // current family

                ArrayList<IndividualRelFamily> famMembers = (ArrayList) Cfamily.members;

                String hID = "";
                String wID = "";
                String ChList = "{";
                
                ArrayList<String> chiID = new ArrayList<String>();

                for (int j = 0; j < famMembers.size(); j++) {

                    if (famMembers.get(j).role.equals("H")) {
                        hID = famMembers.get(j).individualId;

                    } else if (famMembers.get(j).role.equals("W")) {
                        wID = famMembers.get(j).individualId;

                    } else if (famMembers.get(j).role.equals("C")) {
                        chiID.add(famMembers.get(j).individualId);
                    }

                }

                for (int z = 0; z < chiID.size(); z++) {

                	// getting list of all children in this family
                    ChList += chiID.get(z);
                    
                    if(z != chiID.size() -1){
                    	ChList += ", ";
                    }
                }
                ChList += "}";
                
                Ftable.addRow(Cfamily.id, 
                		Cfamily.married != null? sdf.format(Cfamily.married): "NA",
                		Cfamily.divorced != null? sdf.format(Cfamily.divorced): "NA", 
                		 hID,Individual.findById(hID).name,
                		wID, Individual.findById(wID).name,
                		ChList);
                //System.out.println();
            }
            System.out.println(Ftable);

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
