package ShiftingSpire;

import ShiftingSpire.relics.InfectedDagger;
import ShiftingSpire.relics.LongBlade;
import basemod.BaseMod;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BuffHelper implements PostPowerApplySubscriber {

    public BuffHelper(){
        BaseMod.subscribe(this);
    }
    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
        if(target != null && target.isPlayer) {
            if(ShiftingSpire.player == PlayerID.IRONCLAD) {
                Equipment ironclad = ShiftingSpire.inventory.getEquip(PlayerID.IRONCLAD);
                if(ironclad.equipid == EquipmentID.LONGBLADE) {
                        if(abstractPower.ID.equals("Strength") && abstractPower.amount > 0)
                            ((LongBlade) ironclad).applyStrMod();
                        if(abstractPower.ID.equals("Flex"))
                            ((LongBlade) ironclad).appleStrModDown();

                }
            }
        }
        if(target != null) {
            if(ShiftingSpire.player == PlayerID.SILENT) {
                Equipment silent = ShiftingSpire.inventory.getEquip(PlayerID.SILENT);
                if(silent.equipid == EquipmentID.INFECTEDDAGGER){
                    if(abstractPower.ID.equals("Poison")) {
                        abstractPower.amount *= ((InfectedDagger) silent).getPoisonBuff(); //TODO: HOW DO I TELL THE PLAYER HOW MUCH POISON THEY ARE APPLYING????
                    }
                }
            }
        }
    }
}
