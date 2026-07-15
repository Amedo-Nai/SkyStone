package amedonai.skystone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.List;

public class TooltipHelper {

    @Environment(EnvType.CLIENT)
    public static void addTooltipLines(List<Text> tooltip, String baseKey, Formatting formatting) {
        int i = 1;
        while (true) {
            String key = baseKey + i;
            boolean has = TranslationStorage.getInstance().hasTranslation(key);
            if (!has) break;
            tooltip.add(new TranslatableText(key).formatted(formatting));
            i++;
        }
    }
}