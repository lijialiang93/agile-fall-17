package classes;

import java.util.*;
import java.util.stream.Collectors;

public class Gedcom {

    public Gedcom(String input) throws GedcomException {
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

    private void parse(String input) {
        Map<String, Map<String, String>> map = parseToMap(input);
        System.out.println(map);
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

    public class GedcomException extends Exception {
        public GedcomException() {
            super("Gedcom file contains invalid tags");
        }
    }

}
