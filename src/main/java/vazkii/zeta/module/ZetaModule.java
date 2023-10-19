package vazkii.zeta.module;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.base.module.hint.HintManager;
import vazkii.quark.base.module.hint.HintObject;
import vazkii.zeta.Zeta;
import vazkii.zeta.event.ZGatherHints;
import vazkii.zeta.event.bus.PlayEvent;

public class ZetaModule {
	public ZetaCategory category = null;

	public String displayName = "";
	public String lowercaseName = "";
	public String description = "";

	public Set<String> antiOverlap = Set.of();

	public boolean enabledByDefault = true;
	public boolean missingDep = false;

	//TODO: Can I delete some of these flags? (maybe some sort of "DisabledReason"?)
	// The config should probably be the source of *truth* for this state, but having flags here *is* convenient
	private boolean firstLoad = true;
	public boolean enabled = false;
	public boolean disabledByOverlap = false;
	public boolean configEnabled = false;
	public boolean ignoreAntiOverlap = false;

	private List<HintObject> annotationHints = null;

	public void postConstruct() {
		// NO-OP
	}

	//TODO: tidy
	public final void setEnabled(Zeta z, boolean willEnable) {
		configEnabled = willEnable;

		disabledByOverlap = false;
		if(missingDep)
			willEnable = false;
		else if(!ignoreAntiOverlap && antiOverlap != null && antiOverlap.stream().anyMatch(z::isModLoaded)) {
			disabledByOverlap = true;
			willEnable = false;
		}

		setEnabledAndManageSubscriptions(z, willEnable);
		firstLoad = false;
	}

	public final void setEnabledAndManageSubscriptions(Zeta z, boolean nowEnabled) {
		if(firstLoad || (this.enabled != nowEnabled)) {

			//TODO: Cheap hacks to keep non-Zeta Quark modules on life support.
			// When all the Forge events are removed, this can be removed too.
			boolean actuallySubscribe = LEGACY_doForgeEventBusSubscription(nowEnabled);
			if(actuallySubscribe) {
				if(nowEnabled)
					z.playBus.subscribe(this.getClass()).subscribe(this);
				else
					z.playBus.unsubscribe(this.getClass()).unsubscribe(this);
			}
		}

		this.enabled = nowEnabled;
	}

	@PlayEvent
	public final void addAnnotationHints(ZGatherHints event) {
		if(annotationHints == null)
			annotationHints = HintManager.gatherHintAnnotations(ModuleLoader.INSTANCE.getConfigFlagManager(), this);

		for(HintObject hint : annotationHints)
			hint.apply(event);
	}

	@Deprecated public boolean LEGACY_hasSubscriptions = false;
	@Deprecated public List<Dist> LEGACY_subscriptionTarget = null;

	private boolean LEGACY_doForgeEventBusSubscription(boolean nowEnabled) {
		if(LEGACY_subscriptionTarget == null) return true;

		if(LEGACY_hasSubscriptions && LEGACY_subscriptionTarget.contains(FMLEnvironment.dist)) {
			if(nowEnabled)
				MinecraftForge.EVENT_BUS.register(this);
			else
				MinecraftForge.EVENT_BUS.unregister(this);
		}

		return LEGACY_subscriptionTarget.contains(FMLEnvironment.dist);
	}
}
