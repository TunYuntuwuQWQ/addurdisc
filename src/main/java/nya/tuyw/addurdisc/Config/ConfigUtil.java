package nya.tuyw.addurdisc.Config;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ConfigUtil {
    private static Path normalPath = FMLPaths.GAMEDIR.get();
    private static String resourceName = ".addurdisc";
    private static Path resourcePath = null;
    private static Boolean candropbycreeper = Config.canDropbycreeper.get();
    private static Boolean dorefreshlang = Config.doRefreshLang.get();

    public static Path getNormalPath(){
        return normalPath;
    }
    public static void setNormalPath(Path path){
        normalPath = path;
    }
    public static String getResourceName(){
        return resourceName;
    }
    public static void setResourceName(String name){
        resourceName = name;
    }
    public static Path getResourcePath(){
        return resourcePath;
    }
    public static void setResourcePath(Path path){
        resourcePath = path;
    }
    public static Boolean getDropbycreeper(){
        return candropbycreeper;
    }
    public static Boolean getDorefreshlang(){
        return dorefreshlang;
    }
}
