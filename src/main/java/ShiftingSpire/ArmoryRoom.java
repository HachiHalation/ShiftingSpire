package ShiftingSpire;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;

public class ArmoryRoom extends TreasureRoomBoss {

    public ArmoryRoom() {
        super();
    }

    @Override
    public void onPlayerEntry() {
        CardCrawlGame.music.silenceBGM();
        if (AbstractDungeon.actNum < 4) {
            AbstractDungeon.overlayMenu.proceedButton.setLabel(TEXT[0]);
        }

        this.playBGM("SHRINE");

        this.chest = new ArmoryChest();
    }
}
