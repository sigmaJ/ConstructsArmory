/*
 * Copyright (c) 2018-2020 C4
 *
 * This file is part of Construct's Armory, a mod made for Minecraft.
 *
 * Construct's Armory is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Construct's Armory is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Construct's Armory.  If not, see <https://www.gnu.org/licenses/>.
 */

package c4.conarm.lib.book.content;

import c4.conarm.common.ConstructsRegistry;
import c4.conarm.lib.ArmoryRegistry;
import c4.conarm.lib.armor.ArmorCore;
import c4.conarm.lib.armor.ArmorPart;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.element.ImageData;
import slimeknights.mantle.client.book.data.element.TextData;
import slimeknights.mantle.client.gui.book.GuiBook;
import slimeknights.mantle.client.gui.book.element.BookElement;
import slimeknights.mantle.client.gui.book.element.ElementImage;
import slimeknights.mantle.client.gui.book.element.ElementText;
import slimeknights.mantle.util.ItemStackList;
import slimeknights.tconstruct.library.book.TinkerPage;
import slimeknights.tconstruct.library.book.elements.ElementTinkerItem;
import slimeknights.tconstruct.library.materials.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static slimeknights.tconstruct.library.book.content.ContentTool.*;

public class ContentArmor extends TinkerPage {

    public static final transient String ID = "armor";

    private transient List<ArmorCore> armor;
    private transient List<ArmorPart> coreParts;

    public TextData[] text = new TextData[0];
    public String[] properties = new String[0];

    public ContentArmor() {
    }

    public ContentArmor(List<ArmorCore> armor) {
        this.armor = armor;
    }

    @Override
    public void load() {
        if(armor == null) {
            armor = new ArrayList<>();
            armor = ArmoryRegistry.getArmor().stream()
                    .filter(armorCore -> armorCore.getAppearanceName().equals("classic"))
                    .collect(Collectors.toList());
        }
        if(coreParts == null) {
            coreParts = new ArrayList<>();
            coreParts.addAll(Lists.newArrayList(ConstructsRegistry.helmetCore,
                    ConstructsRegistry.chestCore,
                    ConstructsRegistry.leggingsCore,
                    ConstructsRegistry.bootsCore));
        }
    }

    @Override
    public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
        addTitle(list, parent.translate("armor.title"));

        int padding = 5;

        int h = GuiBook.PAGE_WIDTH / 3 - 10;
        int y = 16;
        list.add(new ElementText(padding, y, GuiBook.PAGE_WIDTH - padding*2, h, text));

        ImageData img = IMG_SLOTS;
        int imgX = GuiBook.PAGE_WIDTH - img.width - 8;
        int imgY = GuiBook.PAGE_HEIGHT - img.height - 16;

        int armorX = imgX + (img.width - 16) / 2;
        int armorY = imgY + 28;

        y = imgY - 6;

        if(properties.length > 0) {
            TextData head = new TextData(parent.translate("armor.properties"));
            head.underlined = true;
            list.add(new ElementText(padding, y, 86 - padding, GuiBook.PAGE_HEIGHT - h - 20, head));

            List<TextData> effectData = Lists.newArrayList();
            for(String e : properties) {
                effectData.add(new TextData("\u25CF "));
                effectData.add(new TextData(e));
                effectData.add(new TextData("\n"));
            }

            y += 10;
            list.add(new ElementText(padding, y, GuiBook.PAGE_WIDTH / 2 + 5, GuiBook.PAGE_HEIGHT - h - 20, effectData));
        }



        int[] slotX = new int[]{-21, -25,   0, 25, 21};
        int[] slotY = new int[]{ 22,  -4, -25, -4, 22};

        list.add(new ElementImage(imgX + (img.width - IMG_TABLE.width) / 2, imgY + 28, -1, -1, IMG_TABLE));
        list.add(new ElementImage(imgX, imgY, -1, -1, img, book.appearance.slotColor));

        ItemStackList demo = getDemoArmor();

        ElementTinkerItem toolItem = new ElementTinkerItem(armorX, armorY, 1f, demo);
        toolItem.noTooltip = true;

        list.add(toolItem);
        list.add(new ElementImage(armorX - 3, armorY - 3, -1, -1, IMG_SLOT_1, 0xffffff));

        Material material = armor.get(0).getMaterialForPartForGuiRendering(0);
        ItemStackList stacks = ItemStackList.withSize(coreParts.size());
        for (int i = 0; i < coreParts.size(); i++) {
            stacks.set(i, coreParts.get(i).getItemstackWithMaterial(material));
        }
        ElementTinkerItem partItem = new ElementTinkerItem(armorX + slotX[0], armorY + slotY[0], 1f, stacks);
        partItem.noTooltip = true;
        list.add(partItem);
        ElementTinkerItem plateItem = new ElementTinkerItem(armorX + slotX[1], armorY + slotY[1], 1f, ConstructsRegistry.armorPlate.getItemstackWithMaterial(armor.get(0).getMaterialForPartForGuiRendering(1)));
        plateItem.noTooltip = true;
        list.add(plateItem);
        ElementTinkerItem trimItem = new ElementTinkerItem(armorX + slotX[2], armorY + slotY[2], 1f, ConstructsRegistry.armorTrim.getItemstackWithMaterial(armor.get(0).getMaterialForPartForGuiRendering(2)));
        trimItem.noTooltip = true;
        list.add(trimItem);
    }

    protected ItemStackList getDemoArmor() {
        ItemStackList demo = ItemStackList.withSize(armor.size());

        for(int i = 0; i < armor.size(); i++) {
            if(armor.get(i) != null) {
                demo.set(i, ((armor.get(i)).buildItemForRenderingInGui()));
            }
            else if(armor != null) {
                demo.set(i, new ItemStack(armor.get(i)));
            }
        }

        return demo;
    }
}
