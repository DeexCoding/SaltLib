package me.Deex.SaltLib.Mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.OptionPairWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.resource.language.I18n;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin extends Screen
{
    @Shadow
    private Screen parent;

    @Shadow
    protected String title;

    @Shadow
    private GameOptions options;

    @Shadow
    private EntryListWidget list;

    @Shadow
    @Final
    private static GameOptions.Option[] OPTIONS;

    /* @Overwrite
    public void init() {
        this.title = I18n.translate("options.videoTitle", new Object[0]);
        this.buttons.clear();
        this.buttons.add(new ButtonWidget(200, this.width / 2 - 100, this.height - 27, I18n.translate("gui.done", new Object[0])));
        GameOptions.Option[] options = new GameOptions.Option[OPTIONS.length - 1];
        int i = 0;
        for (GameOptions.Option option : OPTIONS) 
        {
            if (option == GameOptions.Option.USE_VBO) continue; //Bug in the original version? This breaks out ouf the loop when it sees USE_VBO, however, there are options after that, which will not get added
            options[i] = option;
            ++i;
        }
        this.list = new OptionPairWidget(this.client, this.width, this.height, 32, this.height - 32, 25, options);
    }*/
    // This might be improved in future.
}
