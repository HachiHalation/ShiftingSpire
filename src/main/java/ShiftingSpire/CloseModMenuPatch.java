package ShiftingSpire;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz=AbstractDungeon.class,
        method="closeCurrentScreen"
)
public class CloseModMenuPatch {

    public static SpireReturn Prefix() {
        if (ShiftingSpire.closeModScreens())
            return SpireReturn.Return(null);
        return SpireReturn.Continue();
    }
}
