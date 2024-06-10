package nya.tuyw.addurdisc.Config;

import net.fabricmc.loader.api.FabricLoader;
import nya.tuyw.addurdisc.AddurDisc;

import java.io.File;
import java.nio.file.Path;

public class ConfigUtil {
    private static String path;
    private static String pathname;
    private static boolean dropbycreeper;
    private static boolean refreshlang;
    public ConfigUtil() {
        refreshConfig();
    }
    public static void refreshConfig(){
        Config config = ConfigHandler.getConfig();
        pathname = config.customPathName;
        dropbycreeper = config.canDropByCreeper;
        refreshlang = config.refreshLang;
        if (config.customPath.isEmpty()){
            path = FabricLoader.getInstance().getGameDir().toString();
        }else {
            path = FabricLoader.getInstance().getGameDir().resolve(config.customPath).toString();
        }
        AddurDisc.LOGGER.debug(path.toString());
    }
    public static Path getNormalPath(){
        return new File(path).toPath();
    }
    public static String getResourceName(){
        return pathname;
    }
    public static Path getResourcePath(){
        return getNormalPath().resolve(getResourceName());
    }
    public static boolean getDropbycreeper(){
        return dropbycreeper;
    }
    public static boolean getrefreshlang(){return refreshlang;}
}
