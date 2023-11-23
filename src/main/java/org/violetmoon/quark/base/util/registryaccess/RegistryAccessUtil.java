package org.violetmoon.quark.base.util.registryaccess;

import net.minecraft.core.RegistryAccess;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.server.ServerLifecycleHooks;

public class RegistryAccessUtil {
    public static RegistryAccess getRegistryAccess() {
        if (FMLEnvironment.dist.isClient()) {
            return ClientUtil.getRegistryAccess();
        } else {
            return ServerLifecycleHooks.getCurrentServer().registryAccess();
        }
    }
}
