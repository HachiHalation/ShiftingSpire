package ShiftingSpire;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.ConfirmButton;

import java.util.ArrayList;

public class InventoryScreen {
    public static final int RELICS_PER_LINE = 10;
    private static float startX = Settings.WIDTH * 0.1F;
    private static float startY = Settings.HEIGHT * 0.75F;
    private static float padX = (Settings.WIDTH * 0.8F)/9.0F;
    private static float padY = AbstractRelic.RAW_W;

    private Equipment hovered;
    private Equipment clicked;

    private Inventory inventory;

    private ConfirmButton equipButtton;
    private static final float buttonX = (Settings.WIDTH * 0.8F);
    private static final float buttonY = (Settings.HEIGHT * 0.66F);

    public boolean open;

    public InventoryScreen(Inventory inventory) {
        this.inventory = inventory;
        equipButtton = new ConfirmButton("Equip");
        open = false;
    }
    
    public void update() {
        updatePositions();
        updateClick();

        equipButtton.update();
    }

    public void open() {
        AbstractDungeon.player.releaseCard();
        CardCrawlGame.sound.play("CHEST_OPEN");
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
        inventory.drawInventory(startX, startY, padX, padY, RELICS_PER_LINE);
        AbstractDungeon.overlayMenu.proceedButton.hide();
        AbstractDungeon.overlayMenu.hideCombatPanels();
        AbstractDungeon.overlayMenu.showBlackScreen();
        AbstractDungeon.overlayMenu.cancelButton.show("Return");

        if(AbstractDungeon.getCurrRoom() instanceof NeowRoom) equipButtton.show();
        open = true;
    }

    private void updatePositions() {
        hovered = inventory.updatePositions(startX, startY, padX, padY, RELICS_PER_LINE);
    }

    private void updateClick() {
        if(hovered != null && hovered.hb.clickStarted) {
            if(clicked != null) clicked.stopPulse();
            clicked = hovered;
            clicked.beginLongPulse();
            equipButtton.isDisabled = false;
        }

        if(!equipButtton.isDisabled && equipButtton.isHovered && equipButtton.hb.clicked) {
            inventory.equip(clicked, ShiftingSpire.player);
            close();
        }
    }

    public void close() {
        open = false;
        hovered = null;
        if(clicked != null) {
            clicked.stopPulse();
            equipButtton.isDisabled = true;
        }
        equipButtton.hb.clicked = false;
        clicked = null;
        AbstractDungeon.overlayMenu.cancelButton.hide();
        equipButtton.hide();

        if(AbstractDungeon.previousScreen == null) {
            AbstractDungeon.overlayMenu.hideBlackScreen();
            AbstractDungeon.isScreenUp = false;
        }

        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead)
            AbstractDungeon.overlayMenu.showCombatPanels();
        if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMPLETE)
            AbstractDungeon.overlayMenu.proceedButton.show();
    }

    public void render(SpriteBatch sb) {
        equipButtton.render(sb);
        inventory.render(sb);
    }

}
