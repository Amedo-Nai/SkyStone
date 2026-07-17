package amedonai.skystone;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class AdvancementHelper {
    public static void grantAdvancement(ServerPlayerEntity player, String advancementPath) {
        if (player.server == null) return;

        Identifier id = new Identifier("skystone", advancementPath);
        Advancement advancement = player.server.getAdvancementLoader().get(id);

        if (advancement != null) {
            AdvancementProgress progress = player.getAdvancementTracker().getProgress(advancement);
            if (!progress.isDone()) {
                for (String criterion : progress.getUnobtainedCriteria()) {
                    player.getAdvancementTracker().grantCriterion(advancement, criterion);
                }
            }
        }
    }
}