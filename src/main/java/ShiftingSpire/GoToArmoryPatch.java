package ShiftingSpire;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;

@SpirePatch(
        clz = ProceedButton.class,
        method = "update"

)

public class GoToArmoryPatch {
    @SpireInsertPatch(
            rloc = 23,
            localvars = {"currentRoom"}
    )
    public static SpireReturn Insert(ProceedButton __instance, AbstractRoom currentRoom) {
        if (currentRoom instanceof ArmoryRoom){
            myGoToTreasure(__instance);
            return SpireReturn.Return(null);
        }
        if (currentRoom instanceof MonsterRoomBoss)
            goToArmory(__instance);
        return SpireReturn.Continue();
    }


    // TODO: Try not to steal code :/
    private static void myGoToTreasure(ProceedButton button){
        CardCrawlGame.music.fadeOutTempBGM();
        MapRoomNode node = new MapRoomNode(-1, 15);
        node.room = new TreasureRoomBoss();
        AbstractDungeon.nextRoom = node;
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.nextRoomTransitionStart();
        button.hide();
    }

    private static void goToArmory(ProceedButton button){
        CardCrawlGame.music.fadeOutTempBGM();
        MapRoomNode node = new MapRoomNode(-1, 15);
        node.room = new ArmoryRoom();
        AbstractDungeon.nextRoom = node;
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.nextRoomTransitionStart();
        button.hide();
    }

}
