package nya.tuyw.addurdisc.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
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

    public static ArrayList<String> loadedSounds;
    public static ArrayList<Item> addedDiscs;
    public static InputStream Stream;

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
        loadedSounds = new ArrayList<>();
        File dir = sounds.toFile();
        File[] files = dir.listFiles();
        if (files != null) {
            AddurDisc.LOGGER.debug("Existing files:");
            for (File f : files) {
                AddurDisc.LOGGER.debug(dir.getName() + "/" + f.getName());
                if (f.getName().endsWith(".ogg") && checkFileName(f.getName())) {
                    String names = f.getName().substring(0, f.getName().lastIndexOf('.'));
                    loadedSounds.add(names);
                } else {
                    if (!f.getName().endsWith(".ogg")) {
                        AddurDisc.LOGGER.warn(f.getName() + " is not a valid sound file (not ending with .ogg). Ignoring.");
                    }
                    if (!checkFileName(f.getName())) {
                        AddurDisc.LOGGER.warn(f.getName() + " contains illegal characters. Ignoring.");
                    }
                }
            }
            Generater.GenSoundsjson(path, loadedSounds);
            Generater.GenLangjson(lang,loadedSounds);
            Generater.GenTexturepng(Stream,textures,loadedSounds);
            Generater.GenModelsjson(models,loadedSounds);
            if (ConfigUtil.getDropbycreeper()){
                Generater.GenItemTags(data.resolve("creeper_drop_music_discs.json"),loadedSounds);
            }else {
                Generater.GenItemTags(data.resolve("music_discs.json"),loadedSounds);
            }
            AddurDisc.LOGGER.debug("Loaded files:" + loadedSounds);
            AddurDisc.LOGGER.debug("Sounds are all ready");
        }
    }

    @SubscribeEvent
    public static void onRegisterSounds(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.SOUND_EVENTS.getRegistryKey())) {
            initializeSounds();
            for (String name : loadedSounds) {
                event.register(ForgeRegistries.SOUND_EVENTS.getRegistryKey(),
                        new ResourceLocation(AddurDisc.MODID,name),
                        () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(AddurDisc.MODID,name)));
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterItems(RegisterEvent event) {
        addedDiscs = new ArrayList<>();
        if (event.getRegistryKey().equals(ForgeRegistries.ITEMS.getRegistryKey())) {
            for (String name : loadedSounds) {
                ResourceLocation soundLocation = new ResourceLocation(AddurDisc.MODID,name);
                SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(soundLocation);
                if (soundEvent != null) {
                    AddurDisc.LOGGER.debug("Registering RecordItem for: " + name);
                    Item recordItem = new RecordItem(15, () -> soundEvent, new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 1728000);
                    event.register(ForgeRegistries.ITEMS.getRegistryKey(),
                            new ResourceLocation(AddurDisc.MODID, "disc_" + name),
                            () -> recordItem);
                    addedDiscs.add(recordItem);
                } else {
                    AddurDisc.LOGGER.warn("Sound event not found for: " + name);
                }
            }
        }
    }


    @SubscribeEvent
    public static void onRegisterTabs(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.CREATIVE_MODE_TAB)) {
            event.register(Registries.CREATIVE_MODE_TAB,
                    new ResourceLocation(AddurDisc.MODID, "addurdisc_creativetab"),
                    () -> CreativeModeTab.builder()
                            .icon(() -> new ItemStack(Items.JUKEBOX))
                            .title(Component.translatable("addurdisc_creativetab"))
                            .displayItems((pParameters, pOutput) -> loadedSounds.forEach(name -> {
                                ResourceLocation itemLocation = new ResourceLocation(AddurDisc.MODID, "disc_" + name);
                                Item item = ForgeRegistries.ITEMS.getValue(itemLocation);
                                if (item != null) {
                                    pOutput.accept(new ItemStack(item));
                                } else {
                                    AddurDisc.LOGGER.warn("Item not found for: " + name);
                                }
                            }))
                            .build());
        }
    }

    private static boolean checkFileName(String str) {
        return Pattern.compile("[a-z0-9/._-]").matcher(str).find();
    }

    private static void checkResourcesPath(Path p) {
        if (!p.toFile().exists()) {
            p.toFile().mkdirs();
        }
    }
}