package ShiftingSpire;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;

import java.util.ArrayList;
import java.util.Iterator;

public class ArmoryChest extends AbstractChest {
    public ArmoryChest() {
        this.img = ImageMaster.loadImage("EquipmentModAssets/chests/armoryChest.png");
        this.openedImg = ImageMaster.loadImage("EquipmentModAssets/chests/armoryChestOpened.png");
        this.hb = new Hitbox(256.0F * Settings.scale, 200.0F * Settings.scale);
        this.hb.move(CHEST_LOC_X, CHEST_LOC_Y - 100.0F * Settings.scale);
    }

    @Override
    public void open(boolean bossChest) {
        AbstractDungeon.overlayMenu.proceedButton.setLabel(TEXT[0]);

        CardCrawlGame.sound.play("CHEST_OPEN");

        ArrayList<AbstractRelic> rewards = new ArrayList<>();
        rewards.add(EquipmentHelper.generate(EquipmentID.LONGBLADE, AbstractDungeon.ascensionLevel));
        rewards.add(EquipmentHelper.generate(EquipmentID.LONGBLADE, AbstractDungeon.ascensionLevel));
        rewards.add(EquipmentHelper.generate(EquipmentID.LONGBLADE, AbstractDungeon.ascensionLevel));

        AbstractDungeon.bossRelicScreen.open(rewards);
    }

    @Override
    public void close() {
        CardCrawlGame.sound.play("CHEST_OPEN");
        this.isOpen = false;
    }
}


