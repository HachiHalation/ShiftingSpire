package ShiftingSpire;

import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InventoryTopBar extends TopPanelItem {
    private static String path = "EquipmentModAssets/chests/topBarChest.png";
    private static String ID = "shiftingspire:inventory";
    private InventoryScreen screen;

    public InventoryTopBar(InventoryScreen screen) {
        super(new Texture(path), ID);
        this.screen = screen;
    }

    @Override
    protected void onClick() {

        if (screen.open) {
            screen.close();
        } else {
            if (AbstractDungeon.isScreenUp && AbstractDungeon.previousScreen == null)
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            else
                AbstractDungeon.previousScreen = null;
            screen.open();
        }

    }
}

