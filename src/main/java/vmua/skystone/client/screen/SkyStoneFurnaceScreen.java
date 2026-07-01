package vmua.skystone.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import vmua.skystone.screen.SkyStoneFurnaceScreenHandler;

@Environment(EnvType.CLIENT)
public class SkyStoneFurnaceScreen extends HandledScreen<SkyStoneFurnaceScreenHandler> {
    // Путь к текстуре GUI: assets/skystone/textures/gui/container/sky_stone_furnace.png
    private static final Identifier TEXTURE = new Identifier("skystone", "textures/gui/container/sky_stone_furnace.png");

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
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.client != null) {
            this.client.getTextureManager().bindTexture(TEXTURE);
        }
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // 1. Рисуем задний фон печки
        this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        // 2. Рисуем огонек (если печь горит)
        if (this.handler.isBurning()) {
            int k = this.handler.getFuelProgress();
            // Координаты ванильного огонька: x + 56, y + 36. В текстуре он обычно на 176, 12
            this.drawTexture(matrices, x + 56, y + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        // 3. Рисуем стрелочку прогресса плавки
        int l = this.handler.getCookProgress();
        // Координаты ванильной стрелочки: x + 79, y + 34. В текстуре она на 176, 14
        this.drawTexture(matrices, x + 79, y + 34, 176, 14, l, 16);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}