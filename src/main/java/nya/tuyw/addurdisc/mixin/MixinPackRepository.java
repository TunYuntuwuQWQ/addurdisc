package nya.tuyw.addurdisc.mixin;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import nya.tuyw.addurdisc.Config.ConfigUtil;
import nya.tuyw.addurdisc.myRepositorySource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.include.com.google.common.collect.ImmutableSet;

import java.util.Set;

@Mixin(PackRepository.class)
public class MixinPackRepository {
	@Shadow @Final @Mutable private Set<RepositorySource> sources;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(CallbackInfo ci) {
		new ConfigUtil();
		RepositorySource mySource = new myRepositorySource(ConfigUtil.getNormalPath(), PackType.CLIENT_RESOURCES,PackSource.BUILT_IN);
		ImmutableSet.Builder<RepositorySource> builder = ImmutableSet.builder();
		builder.addAll(this.sources);
		builder.add(mySource);
		this.sources = builder.build();
	}
}
