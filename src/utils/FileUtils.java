package utils;

import java.io.*;

public class FileUtils {

    public static String read(String path) throws Exception {
        File file = new File(path);
        InputStream is = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append("\n");
        }

        reader.close();
        is.close();

        return buffer.toString();
    }

    public static void write(String path, String content) throws Exception {
        File file = new File(path);
        file.createNewFile();

        PrintWriter printWriter = new PrintWriter(path);
        printWriter.println(content);
        printWriter.flush();
        printWriter.close();
    }

}
