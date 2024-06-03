package nya.tuyw.addurdisc.PackFinder;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nya.tuyw.addurdisc.AddurDisc;
import nya.tuyw.addurdisc.Config.ConfigUtil;

@Mod.EventBusSubscriber(modid = AddurDisc.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class addPackFinder {
    @SubscribeEvent
    public static void newpackfinder(AddPackFindersEvent event){
        event.addRepositorySource(new myRepositorySource(ConfigUtil.getNormalPath(), PackType.CLIENT_RESOURCES, PackSource.BUILT_IN));
        AddurDisc.LOGGER.debug("addurdisc's resourcespath added");
    }
}
