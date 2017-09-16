package utils;

import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONUtils {

    public static Object parse(String string) {
        JSONTokener jsonTokener = new JSONTokener(string);
        return jsonTokener.nextValue();
    }

}
