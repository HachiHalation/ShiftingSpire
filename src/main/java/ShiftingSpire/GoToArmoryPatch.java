package ShiftingSpire;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import sun.security.provider.ConfigFile;

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
        AbstractRoom dest = null;
        int x = -1;
        int y = 15;
        if (currentRoom instanceof MonsterRoomBoss) {
            dest = new ArmoryRoom();
        }
        else if (currentRoom instanceof ArmoryRoom && !AbstractDungeon.id.equals("TheBeyond") && !AbstractDungeon.id.equals("TheEnding")){
            dest = new TreasureRoomBoss();
        }
        else if (currentRoom instanceof ArmoryRoom) {
            if (AbstractDungeon.id.equals("TheBeyond")) {
                if (AbstractDungeon.ascensionLevel >= 20 && AbstractDungeon.bossList.size() == 2) {
                    AbstractDungeon.bossKey = (String)AbstractDungeon.bossList.get(0);
                    dest = new MonsterRoomBoss();
                } else if (!Settings.isEndless) {
                    dest = new VictoryRoom(VictoryRoom.EventType.HEART);
                }
            } else if (AbstractDungeon.id.equals("TheEnding")) {
                dest = new TrueVictoryRoom();
                x = 3;
                y = 4;
            }
        }
        if (dest != null){
            redirectRoom(__instance, x, y, dest);
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }


    private static void redirectRoom(ProceedButton button, int x, int y, AbstractRoom dest) {
        CardCrawlGame.music.fadeOutBGM();
        CardCrawlGame.music.fadeOutTempBGM();
        MapRoomNode node = new MapRoomNode(x, y);
        node.room = dest;
        AbstractDungeon.nextRoom = node;
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.nextRoomTransitionStart();
        button.hide();
    }


}
