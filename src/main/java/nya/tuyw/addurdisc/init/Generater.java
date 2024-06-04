package nya.tuyw.addurdisc.init;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
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

    public static void GenTexturepng(InputStream inputStream,Path path,ArrayList<String> names){
        try {
            BufferedImage image = ImageIO.read(inputStream);
            names.forEach((name)-> {
                File f = path.resolve("disc_"+name+".png").toFile();
                if (f.exists()){return;}
                try {
                    ImageIO.write(image,"PNG",f);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void GenModelsjson(Path path,ArrayList<String> names){
        names.forEach((name)->{
            File f = path.resolve("disc_"+name+".json").toFile();
            String js = "{\"parent\":\"item/generated\",\"textures\":{\"layer0\":\"addurdisc:item/disc_"+name+"\"}}";
            writein(f.toString(),js);
        });
    }
    public static void GenLangjson(Path path,ArrayList<String> names){
        File f = path.resolve("en_us.json").toFile();
        StringBuilder langjson = new StringBuilder("{");
        if (names != null && !names.isEmpty()) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                langjson.append("\"item.addurdisc.disc_").append(name).append("\":\"MusicDisc-").append(name)
                        .append("\",\"item.addurdisc.disc_").append(name).append(".desc\":\"").append(name).append("--added by 'addurdisc'\"");
                if (i < names.size() - 1) {
                    langjson.append("\n,");
                }
            }
        }
        langjson.append("}");
        writein(f.toString(),langjson.toString());
    }
    public static void GenPackmcmeta(Path path){
        File f = path.resolve("pack.mcmeta").toFile();
        String json = "{\"pack\":{\"pack_format\":15,\"description\":\"addurdisc's resources\"}}";
        writein(f.toString(),json);
    }
    public static void GenSoundsjson(Path path, ArrayList<String> names) {
        File f = path.resolve("sounds.json").toFile();
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
        writein(f.toString(),jsonBuilder.toString());
    }
    public static void GenItemTags(Path path,ArrayList<String> names){
        File f = path.toFile();
        StringBuilder datajson = new StringBuilder("{\"values\":[");
        if (names != null && !names.isEmpty()) {
            for (int i = 0; i < names.size(); i++) {
                String name = names.get(i);
                datajson.append("\"addurdisc:disc_").append(name).append("\"");
                if (i < names.size() - 1) {
                    datajson.append("\n,");
                }
            }
        }
        datajson.append("]}");
        writein(f.toString(),datajson.toString());
    }
}
