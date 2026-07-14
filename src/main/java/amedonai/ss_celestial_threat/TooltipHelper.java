package amedonai.ss_celestial_threat;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Language;
import java.util.List;

public class TooltipHelper {

    public static void addTooltipLines(List<Text> tooltip, String baseKey, Formatting formatting) {
        int i = 1;
        while (true) {
            String key = baseKey + i;
            // Безопасно проверяем наличие перевода как на клиенте, так и на сервере
            boolean has = Language.getInstance().hasTranslation(key);
            if (!has) break;

            tooltip.add(Text.translatable(key).formatted(formatting));
            i++;
        }
    }
}