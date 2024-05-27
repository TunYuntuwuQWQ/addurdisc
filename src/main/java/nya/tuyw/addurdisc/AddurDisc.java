package nya.tuyw.addurdisc;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Mod(AddurDisc.MODID)
public class AddurDisc {
    public static final String MODID = "addurdisc";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AddurDisc.MODID);
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");
    private static List<File> sounds;
    private static List<String> loadedSounds;

    public static boolean containsChinese(String str) {
        return CHINESE_PATTERN.matcher(str).find();
    }
    private static void registerSounds() {
        for (String soundName : loadedSounds) {
            RegistryObject<SoundEvent> soundEvent = SOUND_EVENTS.register(soundName,
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, soundName)));
        }
    }
    private static void initializeSounds() {
        sounds = new ArrayList<>();
        loadedSounds = new ArrayList<>();
        File dir = FMLPaths.GAMEDIR.get().resolve("addurdisc").toFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                LOGGER.error("Failed to create directory: " + dir.getAbsolutePath());
                return;
            }
        }
        File[] files = dir.listFiles();
        if (files != null) {
            LOGGER.debug("Existing files:");
            for (File f : files) {
                LOGGER.debug(dir.getName() + "/" + f.getName());
                if (f.getName().endsWith(".ogg") && !containsChinese(f.getName())){
                    sounds.add(f);
                    loadedSounds.add(f.getName().substring(0,f.getName().lastIndexOf('.')));
                }else {
                    if (!f.getName().endsWith(".ogg")){
                        LOGGER.warn(f.getName() + " is not a valid sound file (not ending with .ogg). Ignoring.");
                    }
                    if (containsChinese(f.getName())){
                        LOGGER.warn(f.getName() + " contains Chinese characters. Ignoring.");
                    }
                }
            }
            LOGGER.debug("Loaded files:" + loadedSounds);
        }
        SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        registerSounds();
    }
    public AddurDisc() {
        initializeSounds();
    }
}