package nya.tuyw.addurdisc.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nya.tuyw.addurdisc.AddurDisc;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ModInitialize {

    public static final Path path;
    public static final Path sounds;
    public static final Path models;
    public static final Path textures;
    public static final Path lang;

    static {
        Path basePath = ConfigUtil.getResourcePath();
        if (basePath == null) {
            throw new IllegalStateException("Resource path is null!");
        }
        path = basePath.resolve("assets/addurdisc");
        sounds = path.resolve("sounds");
        models = path.resolve("models/item");
        textures = path.resolve("textures/items");
        lang = path.resolve("lang");

        // 立即初始化资源路径
        initializeResourcesPath();
    }

    private static final DeferredRegister<SoundEvent> MOD_SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AddurDisc.MODID);
    private static ArrayList<String> loadedSounds;

    public static void initializePackmcmeta(){
        Generater.GenPackmcmeta(ConfigUtil.getResourcePath());
        AddurDisc.LOGGER.debug("pack.mcmeta initialization in complete");
    }
    public static void initializeResourcesPath(){

        checkResourcesPath(path);
        checkResourcesPath(sounds);
        checkResourcesPath(models);
        checkResourcesPath(textures);
        checkResourcesPath(lang);
        AddurDisc.LOGGER.debug("resources path all ready");
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
                    String names = f.getName().substring(0,f.getName().lastIndexOf('.'));
                    loadedSounds.add(names);
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
        Generater.GenSoundsjson(path, loadedSounds);
        AddurDisc.LOGGER.debug("Sounds are all ready");
    }


    private static boolean checkFileName(String str) {
        return Pattern.compile("[a-z0-9/._-]").matcher(str).find();
    }
    private static void checkResourcesPath(Path p){
        if (!p.toFile().exists()) {
            p.toFile().mkdirs();
        }
    }
    private static void registerSounds() {
        for (String soundName : loadedSounds) {
            RegistryObject<SoundEvent> SoundEvents = MOD_SOUND_EVENTS.register(soundName,
                    () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(AddurDisc.MODID, soundName)));
        }
    }
}
