package nya.tuyw.addurdisc.init;

import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class Config {
    public static final ForgeConfigSpec CFG;
    private static final ForgeConfigSpec.ConfigValue<String> customPath;
    private static final ForgeConfigSpec.ConfigValue<String> custompathname;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("AddurDisc Settings").push("common");
        customPath = builder.comment("You can customize the resource path(in game directory)",
                "example1: .minecraft/config/ = \"config\"",
                "example2: .minecraft/logs/addurdisc/ = \"logs/addurdisc\"").define("CustomPath","");
        custompathname = builder.comment("You can customize the resource path name",
                "default : .addurdisc").define("CustomPathName",".addurdisc");
        builder.pop();
        CFG = builder.build();
    }

    private static final File normalDir = new File(customPath.get());
    private static final String resourceName = custompathname.get();
    private static final File resourceDir = normalDir.toPath().resolve(resourceName).toFile();

    public static File getNormalDir(){
        return normalDir;
    }
    public static String getResourceName(){
        return resourceName;
    }
    public static File getResourceDir(){
        return resourceDir;
    }
}
