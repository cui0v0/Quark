package org.violetmoon.quark.base.util.registryaccess;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ClientUtil {
    public static RegistryAccess getRegistryAccess() {
        if (EffectiveSide.get().isServer()) return ServerLifecycleHooks.getCurrentServer().registryAccess();
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) return connection.registryAccess();
        return RegistryAccess.EMPTY;
    }
}
