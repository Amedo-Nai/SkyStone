package amedonai.skystone;

import net.minecraft.server.network.ServerPlayerEntity;

public class GolemTracker {
    public static final ThreadLocal<ServerPlayerEntity> PLACER_TRACKER = new ThreadLocal<>();
}