package amedonai.ss_celestial_threat.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import amedonai.ss_celestial_threat.screen.SkyStoneFurnaceScreenHandler;

@Environment(EnvType.CLIENT)
public class SkyStoneFurnaceScreen extends HandledScreen<SkyStoneFurnaceScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of("skystone-celestial-threat", "textures/gui/container/sky_stone_furnace.png");

    public SkyStoneFurnaceScreen(SkyStoneFurnaceScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        // Центрируем заголовок в зависимости от длины слова на текущем языке
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // 1. Рисуем задний фон печки
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight, 256, 256);

        // 2. Рисуем огонек (если печь горит)
        if (this.handler.isBurning()) {
            int k = this.handler.getFuelProgress();
            context.drawTexture(TEXTURE, x + 56, y + 36 + 12 - k, 176, 12 - k, 14, k + 1, 256, 256);
        }

        // 3. Рисуем стрелочку прогресса плавки
        int l = this.handler.getCookProgress();
        context.drawTexture(TEXTURE, x + 79, y + 34, 176, 14, l, 16, 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}