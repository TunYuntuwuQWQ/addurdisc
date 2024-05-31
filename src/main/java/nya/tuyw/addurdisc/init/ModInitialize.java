package nya.tuyw.addurdisc.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nya.tuyw.addurdisc.AddurDisc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ModInitialize {
    public static final Path path = Config.getResourceDir().toPath().resolve("/assets/addurdisc/");
    public static final Path sounds = path.resolve("sounds/");
    public static final Path models = path.resolve("models/item/");
    public static final Path textures = path.resolve("textures/items/");
    public static final Path lang = path.resolve("lang/");
    private static final DeferredRegister<SoundEvent> MOD_SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AddurDisc.MODID);
    private static List<String> loadedSounds;


    public static void initializeResourcesPath(){
        checkResourcesPath(path);
        checkResourcesPath(sounds);
        checkResourcesPath(models);
        checkResourcesPath(textures);
        checkResourcesPath(lang);
    }
    public static void initializeSounds() {
        loadedSounds = new ArrayList<>();
        File dir = sounds.toFile();
        File[] files = dir.listFiles();
        if (files != null) {
            AddurDisc.LOGGER.debug("Existing files:");
            for (File f : files) {
                AddurDisc.LOGGER.debug(dir.getName() + "/" + f.getName());
                if (f.getName().endsWith(".ogg") && checkFileName(f.getName())){
                    loadedSounds.add(f.getName().substring(0,f.getName().lastIndexOf('.')));
                }else {
                    if (!f.getName().endsWith(".ogg")){
                        AddurDisc.LOGGER.warn(f.getName() + " is not a valid sound file (not ending with .ogg). Ignoring.");
                    }
                    if (!checkFileName(f.getName())){
                        AddurDisc.LOGGER.warn(f.getName() + " contains illegal characters. Ignoring.");
                    }
                }
            }
            AddurDisc.LOGGER.debug("Loaded files:" + loadedSounds);
        }
        MOD_SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
        registerSounds();
    }


    private static boolean checkFileName(String str) {
        return Pattern.compile("[a-z0-9/._-]").matcher(str).find();
    }
    private static void checkResourcesPath(Path p){
        try {
            if (!Files.exists(p)) {
                Files.createDirectories(p);
            }
        } catch (IOException e) {
            AddurDisc.LOGGER.error("Failed to create directory: " + p, e);
        }
    }
    private static void registerSounds() {
        for (String soundName : loadedSounds) {
            RegistryObject<SoundEvent> soundEvent = MOD_SOUND_EVENTS.register(soundName,
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(AddurDisc.MODID, soundName)));
        }
    }
}
