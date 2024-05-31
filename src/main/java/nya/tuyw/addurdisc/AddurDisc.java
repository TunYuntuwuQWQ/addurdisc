package nya.tuyw.addurdisc;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import nya.tuyw.addurdisc.init.Config;
import nya.tuyw.addurdisc.init.ModInitialize;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Mod(AddurDisc.MODID)
public class AddurDisc {
    public static final String MODID = "addurdisc";
    public static final Logger LOGGER = LogUtils.getLogger();
    public AddurDisc() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,Config.CFG);
        ModInitialize.initializeResourcesPath();
    }
}