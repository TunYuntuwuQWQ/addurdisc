package nya.tuyw.addurdisc.init;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class Generater {
    private static void writein(String path,String Content){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            bufferedWriter.write(Content);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private static void checkOrCreateDir(File dir) {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + dir.getAbsolutePath());
            }
        }
    }


    public static void GenPackmcmeta(Path path){
        File f = path.resolve("pack.mcmeta").toFile();
        File p = f.getParentFile();
        checkOrCreateDir(p);
        String json = "{\"pack\":{\"pack_format\":15,\"description\":\"addurdisc's resources\"}}";
        writein(f.toString(),json);
    }
    public static void GenSoundsjson(Path path, ArrayList<String> names) {
        File f = path.resolve("sounds.json").toFile();
        File p = f.getParentFile();
        checkOrCreateDir(p);

        StringBuilder jsonBuilder = new StringBuilder("{");
        if (names != null && !names.isEmpty()) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                jsonBuilder.append("\"").append(name).append("\":{\"category\":\"record\",\"sounds\":[{\"name\":\"addurdisc:").append(name).append("\",\"stream\":true}]}");
                if (i < names.size() - 1) {
                    jsonBuilder.append("\n,");
                }
            }
        }
        jsonBuilder.append("}");

        if (f.exists()) {
            f.delete();
        }
        writein(f.toString(),jsonBuilder.toString());
    }
}
