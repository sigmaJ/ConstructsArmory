package c4.conarm.common.items.armor;

import c4.conarm.lib.armor.ArmorCore;
import c4.conarm.common.ConstructsRegistry;
import c4.conarm.lib.materials.ArmorMaterialType;
import net.minecraft.inventory.EntityEquipmentSlot;

public class Helmet extends ArmorCore {

    public Helmet(String appearanceName) {
        super(EntityEquipmentSlot.HEAD, appearanceName, ArmorMaterialType.core(ConstructsRegistry.helmetCore));
    }

}