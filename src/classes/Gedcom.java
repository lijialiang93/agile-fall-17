package classes;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Gedcom {

    public List<Individual> individuals;
    public List<Family> families;

    public Gedcom(String input) throws Exception {
        if (validate(input)) {
            parse(input);
        } else {
            throw new GedcomException();
        }
    }

    private boolean validate(String input) {
        // TODO: implement validation
        return true;
    }

    private void parse(String input) throws Exception {
        Map<String, Map<String, String>> map = parseToMap(input);

        for (String key : map.keySet()) {
            if (key.startsWith("@I")) {
                parseIndividual(key, map.get(key));
            } else if (key.startsWith("@F")) {
                parseFamily(key, map.get(key));
            }
        }
    }

    private Map<String, Map<String, String>> parseToMap(String input) {
        String[] lines = input.split("\n");

        Map<String, Map<String, String>> map = new HashMap<>();
        Map<String, String> curMap = null;
        String curId = null;
        String prevTag = null;
        for (String line : lines) {
            String[] fields = line.split(" ");

            int level = Integer.parseInt(fields[0]);
            String tag = fields[1];
            String args = fields.length > 2 ? Arrays.asList(Arrays.copyOfRange(fields, 2, fields.length)).stream().collect(Collectors.joining(" ")) : "";

            if (args != null && (args.equalsIgnoreCase("INDI") || args.equalsIgnoreCase("FAM"))) {
                String tmp = tag;
                tag = args;
                args = tmp;
            }

            if (level == 0 && (tag.equalsIgnoreCase("INDI") || tag.equalsIgnoreCase("FAM"))) {
                if (curId != null && curMap != null) {
                    map.put(curId, curMap);
                }

                curMap = new HashMap<>();
                curId = args;
            } else if (level != 0 && curMap != null) {
                if (tag.equalsIgnoreCase("DATE") && prevTag != null) {
                    curMap.put(prevTag, args);
                } else if (curMap.containsKey(tag)) {
                    args = args + " ||| " + curMap.get(tag);
                    curMap.put(tag, args);
                } else {
                    curMap.put(tag, args);
                }
            }

            prevTag = tag;
        }

        return map;
    }

    private void parseIndividual(String id, Map<String, String> map) throws Exception {
        String name = map.get("NAME");
        String gender = map.get("SEX");
        Date birthday = map.containsKey("BIRT") ? parseDate(map.get("BIRT")) : null;
        Date death = map.containsKey("DEAT") ? parseDate(map.get("DEAT")) : null;

        Individual individual = new Individual(id, name, gender, birthday, death);
        individual.save();

        if (individuals == null) {
            individuals = new ArrayList<>();
        }
        individuals.add(individual);
    }

    private void parseFamily(String id, Map<String, String> map) throws Exception {
        Date married = map.containsKey("MARR") ? parseDate(map.get("MARR")) : null;
        Date divorced = map.containsKey("DIV") ? parseDate(map.get("DIV")) : null;

        Family family = new Family(id, married, divorced);

        if (map.containsKey("WIFE")) {
            IndividualRelFamily rel = new IndividualRelFamily(map.get("WIFE"), id, "W");
            family.addMember(rel);
            rel.save();
        }

        if (map.containsKey("HUSB")) {
            IndividualRelFamily rel = new IndividualRelFamily(map.get("HUSB"), id, "H");
            family.addMember(rel);
            rel.save();
        }

        if (map.containsKey("CHIL")) {
            String[] children = map.get("CHIL").split(" \\|\\|\\| ");
            for (String child : children) {
                IndividualRelFamily rel = new IndividualRelFamily(child, id, "C");
                family.addMember(rel);
                rel.save();
            }
        }

        family.save();

        if (families == null) {
            families = new ArrayList<>();
        }
        families.add(family);
    }

    private Date parseDate(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        return sdf.parse(date);
    }

    public class GedcomException extends Exception {
        public GedcomException() {
            super("Gedcom file contains invalid tags");
        }
    }

}
