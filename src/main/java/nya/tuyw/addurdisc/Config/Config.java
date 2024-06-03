package nya.tuyw.addurdisc.Config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

public class Config {
    public static final ForgeConfigSpec CFG;
    public static final ForgeConfigSpec.ConfigValue<String> customPath;
    public static final ForgeConfigSpec.ConfigValue<String> custompathname;

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
    public static void loadmyConfig(ForgeConfigSpec spec, Path path) {

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();
        spec.setConfig(configData);
    }
}
