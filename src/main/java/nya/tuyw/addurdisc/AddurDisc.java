package nya.tuyw.addurdisc;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import nya.tuyw.addurdisc.Config.Config;
import nya.tuyw.addurdisc.Config.ConfigUtil;
import nya.tuyw.addurdisc.init.ModInitialize;
import org.slf4j.Logger;

import java.io.File;

@Mod(AddurDisc.MODID)
public class AddurDisc {
    public static final String MODID = "addurdisc";
    public static final Logger LOGGER = LogUtils.getLogger();
    public AddurDisc() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,Config.CFG);
        Config.loadmyConfig(Config.CFG, FMLPaths.CONFIGDIR.get().resolve("addurdisc-common.toml"));

        if (!Config.customPath.get().isEmpty()) {
            ConfigUtil.setNormalPath(new File((Config.customPath.get())+"/").toPath());
        }
        LOGGER.debug("custom path now :"+ConfigUtil.getNormalPath().toString());
        ConfigUtil.setResourceName(Config.custompathname.get());
        ConfigUtil.setResourcePath(ConfigUtil.getNormalPath().resolve(ConfigUtil.getResourceName()));

        new ModInitialize();

//        ModInitialize.initializeSounds();
//        ModInitialize.initializeDiscItems();

//        BufferedImage image = null;
//        try {
//            image = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("custom.png")));
//            LOGGER.debug("readdded"+image.toString());
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        try {
//            ImageIO.write(image,"PNG",ModInitialize.textures.resolve("cous.png").toFile());
//            LOGGER.debug("writeeeen");
//        }catch (IOException e){
//            e.printStackTrace();
//        }
    }
}