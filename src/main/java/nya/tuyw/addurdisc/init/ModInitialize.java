package nya.tuyw.addurdisc.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import nya.tuyw.addurdisc.AddurDisc;
import nya.tuyw.addurdisc.Config.ConfigUtil;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ModInitialize {
    public static final Path path;
    public static final Path sounds;
    public static final Path models;
    public static final Path textures;
    public static final Path lang;
    public static final Path data;

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

    public static ArrayList<String> loadedSoundsNames;
    public static ArrayList<Item> addedDiscs;
    public static ArrayList<SoundEvent> loadedSounds;
    public static InputStream Stream;

    private static void initializePackmcmeta() {
        Generater.GenPackmcmeta(ConfigUtil.getResourcePath());
        AddurDisc.LOGGER.debug("pack.mcmeta initialization is complete");
    }

    private static void initializeResourcesPath() {
        checkResourcesPath(path);
        checkResourcesPath(sounds);
        checkResourcesPath(models);
        checkResourcesPath(textures);
        checkResourcesPath(lang);
        checkResourcesPath(data);
        AddurDisc.LOGGER.debug("resources path all ready");
    }

    private static void initializeSounds() {
        loadedSoundsNames = new ArrayList<>();
        File dir = sounds.toFile();
        File[] files = dir.listFiles();
        if (files != null) {
            AddurDisc.LOGGER.debug("Existing files:");
            for (File f : files) {
                AddurDisc.LOGGER.debug(dir.getName() + "/" + f.getName());
                if (f.getName().endsWith(".ogg")) {
                    if (checkFileName(f.getName())){
                        String names = f.getName().substring(0, f.getName().lastIndexOf('.'));
                        loadedSoundsNames.add(names);
                    }else {
                        AddurDisc.LOGGER.warn(f.getName() + " contains illegal characters. Ignoring.");
                    }
                } else {
                    AddurDisc.LOGGER.warn(f.getName() + " is not a valid sound file (not ending with .ogg). Ignoring.");
                }
            }
            Generater.GenSoundsjson(path, loadedSoundsNames);
            Generater.GenTexturepng(Stream,textures, loadedSoundsNames);
            Generater.GenModelsjson(models, loadedSoundsNames);
            if (ConfigUtil.getrefreshlang()){
                Generater.GenLangjson(lang, loadedSoundsNames);
            }else {
                AddurDisc.LOGGER.debug("refreshLang:false,will not refresh the en_us.json");
            }
            if (ConfigUtil.getDropbycreeper()){
                deleteFile(data.resolve("music_disc.json").toFile());
                Generater.GenItemTags(data.resolve("creeper_drop_music_discs.json"), loadedSoundsNames);
            }else {
                deleteFile(data.resolve("creeper_drop_music_discs.json").toFile());
                Generater.GenItemTags(data.resolve("music_discs.json"), loadedSoundsNames);
            }
            AddurDisc.LOGGER.debug("Loaded files:" + loadedSoundsNames);
            AddurDisc.LOGGER.debug("Sounds are all ready");
            RegisterSounds();
            RegisterItems();
            RegisterTabs();
        }
    }

    public ModInitialize() {
        Stream = getClass().getClassLoader().getResourceAsStream("custom.png");
        initializeSounds();
    }

    private static void RegisterSounds() {
        loadedSounds = new ArrayList<>();
        for (String name : loadedSoundsNames) {
            ResourceLocation resourceLocation = new ResourceLocation(AddurDisc.MODID,name);
            SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(resourceLocation);
            Registry.register(BuiltInRegistries.SOUND_EVENT,resourceLocation, soundEvent);
            loadedSounds.add(soundEvent);
        }
    }
    private static void RegisterItems() {
        addedDiscs = new ArrayList<>();
        for (SoundEvent soundEvent : loadedSounds){
            ResourceLocation soundlocation = soundEvent.getLocation();
            String name = soundlocation.getPath();
            Item discitem = new RecordItem(15,soundEvent,new Item.Properties().stacksTo(1).rarity(Rarity.RARE),1728000);
            Registry.register(BuiltInRegistries.ITEM,new ResourceLocation(AddurDisc.MODID,"disc_"+name),discitem);
            addedDiscs.add(discitem);
        }
    }
    private static void RegisterTabs(){
        CreativeModeTab creativeModeTab = CreativeModeTab.builder(CreativeModeTab.Row.TOP,0)
                .icon(()->new ItemStack(Items.JUKEBOX))
                .title(Component.translatable("addurdisc_creativetab"))
                .displayItems((params,output)->{
                    for (Item item:addedDiscs){
                        output.accept(item);
                    }
                })
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,new ResourceLocation(AddurDisc.MODID,"addurdisc_creativetab"),creativeModeTab);
    }

    private static boolean checkFileName(String str) {
        return Pattern.compile("^[a-z0-9._-]+$").matcher(str).find();
    }

    private static void checkResourcesPath(Path p) {
        if (!p.toFile().exists()) {
            p.toFile().mkdirs();
        }
    }
    private static void deleteFile(File f){
        if (f.exists()){
            f.delete();
        }
    }
}