package ShiftingSpire;

import ShiftingSpire.relics.LongBlade;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;

@SpirePatch(
        clz = VulnerablePower.class,
        method = "atDamageReceive"
)

public class VulnerableDamagePatch
{
    @SpireInsertPatch (
        rloc = 5
    )
    public static SpireReturn<Float> Insert(VulnerablePower __instance, float damage, DamageInfo.DamageType type){
        if (__instance.owner != null && __instance.owner.isPlayer) {
            return SpireReturn.Return(damage * 1.5F);
        } else {
            float mod = (AbstractDungeon.player.hasRelic("Paper Frog")) ? 1.75F : 1.5F;
            if (AbstractDungeon.player.hasRelic("shiftingspire:LongBlade")) {
                LongBlade blade = (LongBlade) AbstractDungeon.player.getRelic("shiftingspire:LongBlade");
                float bonus = blade.getVulnBuff()/100F;
                mod += bonus;
            }
            return SpireReturn.Return(damage * mod);
        }
    }

}
