package io.github.betterclient.htmlutil.testing;

import io.github.betterclient.htmlutil.api.DocumentScreenOptions;
import io.github.betterclient.htmlutil.api.elements.HTMLBrElement;
import io.github.betterclient.htmlutil.api.elements.HTMLButtonElement;
import io.github.betterclient.htmlutil.api.elements.HTMLDocument;
import io.github.betterclient.htmlutil.api.elements.HTMLSpanElement;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {
    private MixinTitleScreen() {
        super(Text.literal(""));
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Test HTML"), (button) -> {
            start();
        }).dimensions(this.width / 2 + 150, this.height / 4 + 48, 98, 20).build());
    }

    @Unique
    private void start() {
        HTMLDocument document = new HTMLDocument("/testing.html");
        document.addStyleSheet("/testing.css");

        HTMLBrElement br = new HTMLBrElement(document);
        document.appendElement(br);

        HTMLSpanElement element = new HTMLSpanElement(document, "Appended element");
        element.getStyle().put("color", "green");
        element.getStyle().put("background-color", "blue");
        document.appendElement(element);

        HTMLButtonElement button = document.getElementById("mybutton");
        button.onMouseUp((event) -> {
            HTMLSpanElement element1 = document.getElementById("clickcounter");

            element1.setText(
                    (Integer.parseInt(element1.getText()) + 1) + ""
            );
        });

        document.openAsScreen(new DocumentScreenOptions(true, true));
    }
}
