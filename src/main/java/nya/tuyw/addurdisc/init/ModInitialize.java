package nya.tuyw.addurdisc.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import nya.tuyw.addurdisc.AddurDisc;
import nya.tuyw.addurdisc.Config.ConfigUtil;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(modid = AddurDisc.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class ModInitialize {
    public static final Path path;
    public static final Path sounds;
    public static final Path models;
    public static final Path textures;
    public static final Path lang;
    public static final Path data;
    public static ArrayList<String> loadedSounds = new ArrayList<>();
    public static ArrayList<SoundEvent> addedSounds;
    public static ArrayList<Item> addedDiscs;
    public static InputStream Stream;


    static {
        Path basePath = ConfigUtil.getResourcePath();

        path = basePath.resolve("assets/addurdisc");
        sounds = path.resolve("sounds");
        models = path.resolve("models/item/");
        textures = path.resolve("textures/item");
        lang = path.resolve("lang");
        data = basePath.resolve("data/minecraft/tags/items");

        initializeResourcesPath();
        initializePackmcmeta();
    }
    public ModInitialize() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        Stream = getClass().getClassLoader().getResourceAsStream("custom.png");
    }

    public static void initializePackmcmeta() {
        Generater.GenPackmcmeta(ConfigUtil.getResourcePath());
        AddurDisc.LOGGER.debug("pack.mcmeta initialization is complete");
    }

    public static void initializeResourcesPath() {
        checkResourcesPath(path);
        checkResourcesPath(sounds);
        checkResourcesPath(models);
        checkResourcesPath(textures);
        checkResourcesPath(lang);
        checkResourcesPath(data);
        AddurDisc.LOGGER.debug("resources path all ready");
    }

    public static void initializeSounds() {
        File dir = sounds.toFile();
        File[] files = dir.listFiles();
        if (files != null) {
            AddurDisc.LOGGER.debug("Existing files:");
            for (File f : files) {
                AddurDisc.LOGGER.debug(dir.getName() + "/" + f.getName());
                if (f.getName().endsWith(".ogg")) {
                    if (checkFileName(f.getName())) {
                        String names = f.getName().substring(0, f.getName().lastIndexOf('.'));
                        loadedSounds.add(names);
                    } else {
                        AddurDisc.LOGGER.warn(f.getName() + " contains illegal characters. Ignoring.");
                    }
                } else {
                    AddurDisc.LOGGER.warn(f.getName() + " is not a valid sound file (not ending with .ogg). Ignoring.");
                }
            }
            Generater.GenSoundsjson(path, loadedSounds);
            Generater.GenTexturepng(Stream, textures, loadedSounds);
            Generater.GenModelsjson(models, loadedSounds);
            if (ConfigUtil.getDorefreshlang()) {
                Generater.GenLangjson(lang, loadedSounds);
            } else {
                AddurDisc.LOGGER.debug("refreshLang:false,will not refresh the en_us.json");
            }
            if (ConfigUtil.getDropbycreeper()) {
                Generater.GenItemTags(data.resolve("creeper_drop_music_discs.json"), loadedSounds);
                deleteFile(data.resolve("music_disc.json").toFile());
            } else {
                Generater.GenItemTags(data.resolve("music_discs.json"), loadedSounds);
                deleteFile(data.resolve("creeper_drop_music_discs.json").toFile());
            }
            AddurDisc.LOGGER.debug("Loaded files:" + loadedSounds);
            AddurDisc.LOGGER.debug("Sounds are all ready");
        }
    }

    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> event) {
        if (!loadedSounds.isEmpty()) {
            for (String name : loadedSounds) {
                ResourceLocation soundLocation = new ResourceLocation(AddurDisc.MODID, name);
                SoundEvent soundEvent = new SoundEvent(soundLocation);
                soundEvent.setRegistryName(soundLocation);
                event.getRegistry().register(soundEvent);
            }
        }
        AddurDisc.LOGGER.debug("onRegisterSounds");
    }

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> event) {
        initializeSounds();
        if (!loadedSounds.isEmpty()) {
            addedDiscs = new ArrayList<>();
            for (String name : loadedSounds) {
                ResourceLocation soundLocation = new ResourceLocation(AddurDisc.MODID, name);
                AddurDisc.LOGGER.debug("Registering RecordItem for: " + name);
                Item recordItem = new RecordItem(15, () -> ForgeRegistries.SOUND_EVENTS.getValue(soundLocation), new Item.Properties().stacksTo(1).rarity(Rarity.RARE).tab(TAB));
                recordItem.setRegistryName(new ResourceLocation(AddurDisc.MODID, "disc_" + name));
                event.getRegistry().register(recordItem);
                addedDiscs.add(recordItem);
            }
        }
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("addurdisc_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.JUKEBOX);
        }
    };


    private static boolean checkFileName(String str) {
        return Pattern.compile("^[a-z0-9._-]+$").matcher(str).find();
    }

    private static void checkResourcesPath(Path p) {
        if (!p.toFile().exists()) {
            p.toFile().mkdirs();
        }
    }

    private static void deleteFile(File f) {
        if (f.exists()) {
            f.delete();
        }
    }
}
