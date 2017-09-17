import java.text.SimpleDateFormat;

import classes.Family;
import classes.Gedcom;
import classes.Individual;
import utils.DBUtils;
import utils.FileUtils;
import utils.PrettyTable;

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
