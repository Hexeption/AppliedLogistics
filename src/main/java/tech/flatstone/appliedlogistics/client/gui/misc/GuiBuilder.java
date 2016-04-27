/*
 *
 * LIMITED USE SOFTWARE LICENSE AGREEMENT
 * This Limited Use Software License Agreement (the "Agreement") is a legal agreement between you, the end-user, and the FlatstoneTech Team ("FlatstoneTech"). By downloading or purchasing the software material, which includes source code (the "Source Code"), artwork data, music and software tools (collectively, the "Software"), you are agreeing to be bound by the terms of this Agreement. If you do not agree to the terms of this Agreement, promptly destroy the Software you may have downloaded or copied.
 * FlatstoneTech SOFTWARE LICENSE
 * 1. Grant of License. FlatstoneTech grants to you the right to use the Software. You have no ownership or proprietary rights in or to the Software, or the Trademark. For purposes of this section, "use" means loading the Software into RAM, as well as installation on a hard disk or other storage device. The Software, together with any archive copy thereof, shall be destroyed when no longer used in accordance with this Agreement, or when the right to use the Software is terminated. You agree that the Software will not be shipped, transferred or exported into any country in violation of the U.S. Export Administration Act (or any other law governing such matters) and that you will not utilize, in any other manner, the Software in violation of any applicable law.
 * 2. Permitted Uses. For educational purposes only, you, the end-user, may use portions of the Source Code, such as particular routines, to develop your own software, but may not duplicate the Source Code, except as noted in paragraph 4. The limited right referenced in the preceding sentence is hereinafter referred to as "Educational Use." By so exercising the Educational Use right you shall not obtain any ownership, copyright, proprietary or other interest in or to the Source Code, or any portion of the Source Code. You may dispose of your own software in your sole discretion. With the exception of the Educational Use right, you may not otherwise use the Software, or an portion of the Software, which includes the Source Code, for commercial gain.
 * 3. Prohibited Uses: Under no circumstances shall you, the end-user, be permitted, allowed or authorized to commercially exploit the Software. Neither you nor anyone at your direction shall do any of the following acts with regard to the Software, or any portion thereof:
 * Rent;
 * Sell;
 * Lease;
 * Offer on a pay-per-play basis;
 * Distribute for money or any other consideration; or
 * In any other manner and through any medium whatsoever commercially exploit or use for any commercial purpose.
 * Notwithstanding the foregoing prohibitions, you may commercially exploit the software you develop by exercising the Educational Use right, referenced in paragraph 2. hereinabove.
 * 4. Copyright. The Software and all copyrights related thereto (including all characters and other images generated by the Software or depicted in the Software) are owned by FlatstoneTech and is protected by United States copyright laws and international treaty provisions. FlatstoneTech shall retain exclusive ownership and copyright in and to the Software and all portions of the Software and you shall have no ownership or other proprietary interest in such materials. You must treat the Software like any other copyrighted material. You may not otherwise reproduce, copy or disclose to others, in whole or in any part, the Software. You may not copy the written materials accompanying the Software. You agree to use your best efforts to see that any user of the Software licensed hereunder complies with this Agreement.
 * 5. NO WARRANTIES. FLATSTONETECH DISCLAIMS ALL WARRANTIES, BOTH EXPRESS IMPLIED, INCLUDING BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. THIS LIMITED WARRANTY GIVES YOU SPECIFIC LEGAL RIGHTS. YOU MAY HAVE OTHER RIGHTS WHICH VARY FROM JURISDICTION TO JURISDICTION. FlatstoneTech DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR MEET YOUR SPECIFIC REQUIREMENTS. THE WARRANTY SET FORTH ABOVE IS IN LIEU OF ALL OTHER EXPRESS WARRANTIES WHETHER ORAL OR WRITTEN. THE AGENTS, EMPLOYEES, DISTRIBUTORS, AND DEALERS OF FlatstoneTech ARE NOT AUTHORIZED TO MAKE MODIFICATIONS TO THIS WARRANTY, OR ADDITIONAL WARRANTIES ON BEHALF OF FlatstoneTech.
 * Exclusive Remedies. The Software is being offered to you free of any charge. You agree that you have no remedy against FlatstoneTech, its affiliates, contractors, suppliers, and agents for loss or damage caused by any defect or failure in the Software regardless of the form of action, whether in contract, tort, includinegligence, strict liability or otherwise, with regard to the Software. Copyright and other proprietary matters will be governed by United States laws and international treaties. IN ANY CASE, FlatstoneTech SHALL NOT BE LIABLE FOR LOSS OF DATA, LOSS OF PROFITS, LOST SAVINGS, SPECIAL, INCIDENTAL, CONSEQUENTIAL, INDIRECT OR OTHER SIMILAR DAMAGES ARISING FROM BREACH OF WARRANTY, BREACH OF CONTRACT, NEGLIGENCE, OR OTHER LEGAL THEORY EVEN IF FLATSTONETECH OR ITS AGENT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to you.
 */

package tech.flatstone.appliedlogistics.client.gui.misc;

import mezz.jei.Internal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.lwjgl.opengl.GL11;
import tech.flatstone.appliedlogistics.api.features.TechLevel;
import tech.flatstone.appliedlogistics.client.gui.GuiBase;
import tech.flatstone.appliedlogistics.common.container.misc.ContainerBuilder;
import tech.flatstone.appliedlogistics.common.container.slot.SlotBuilderInventory;
import tech.flatstone.appliedlogistics.common.network.PacketHandler;
import tech.flatstone.appliedlogistics.common.network.messages.PacketButtonClick;
import tech.flatstone.appliedlogistics.common.tileentities.misc.TileEntityBuilder;
import tech.flatstone.appliedlogistics.common.util.GuiHelper;
import tech.flatstone.appliedlogistics.common.util.LanguageHelper;
import tech.flatstone.appliedlogistics.common.util.PlanDetails;
import tech.flatstone.appliedlogistics.common.util.PlanRequiredMaterials;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GuiBuilder extends GuiBase {
    TileEntityBuilder tileEntity;
    GuiHelper guiHelper;
    private GuiButton btnStartBuilder;
    private GuiButton btnSelectTechLevel;


    public GuiBuilder(InventoryPlayer inventoryPlayer, TileEntityBuilder tileEntity) {
        super(new ContainerBuilder(inventoryPlayer, tileEntity));
        guiHelper = new GuiHelper();
        this.xSize = 256;
        this.ySize = 222;
        this.tileEntity = tileEntity;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.btnStartBuilder = new GuiButton(0, guiLeft + 185, guiTop + 117, 64, 20, LanguageHelper.LABEL.translateMessage("build"));
        this.btnSelectTechLevel = new GuiButton(1, guiLeft + 185, guiTop + 139, 64, 20, "");

        this.buttonList.clear();
        this.buttonList.add(btnStartBuilder);

        if (tileEntity.getBlockMetadata() > 0) {
            this.buttonList.add(btnSelectTechLevel);
        }

        this.btnStartBuilder.enabled = false;
    }

    @Override
    public void drawBG(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        bindTexture("gui/machines/builder.png");
        drawTexturedModalRect(paramInt1, paramInt2, 0, 0, this.xSize, this.ySize);

        PlanDetails planDetails = tileEntity.getPlanDetails();
        if (planDetails != null) {
            List<PlanRequiredMaterials> requiredMaterials = planDetails.getRequiredMaterialsList();
            int slotID = 0;
            for (PlanRequiredMaterials materials : requiredMaterials) {
                Slot slot = this.inventorySlots.getSlot(slotID);
                ItemStack stack = materials.getRequiredMaterials().get(0);
                //todo: Make icon change...
//                if (materials.getRequiredMaterials().size() > 1) {
//                    stack = materials.getRequiredMaterials().get(1);
//                }
                this.drawTransparentIconEmpty(slot, stack);
                slotID++;
            }
        }
    }

    @Override
    public void drawFG(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        /**
         * Titles
         */
        this.fontRendererObj.drawString(tileEntity.hasCustomName() ? tileEntity.getCustomName() : LanguageHelper.NONE.translateMessage(tileEntity.getUnlocalizedName()), 8, 6, 4210752);
        this.fontRendererObj.drawString(LanguageHelper.NONE.translateMessage("container.inventory"), 8, 129, 4210752);

        ItemStack itemPlan = tileEntity.getInternalInventory().getStackInSlot(0);

        if (tileEntity.getPlanItem() == null) {
            this.fontRendererObj.drawString(EnumChatFormatting.RED + LanguageHelper.MESSAGE.translateMessage("plan.insert"), 36, 26, 4210752);
        } else {
            this.fontRendererObj.drawString(LanguageHelper.NONE.translateMessage(itemPlan.getUnlocalizedName() + ".name"), 8, 48, 4210752);
        }

        /**
         * Description of things...
         */
        GL11.glPushMatrix();
        GL11.glScalef(0.75f, 0.75f, 0.75f);
        String machineDescription = tileEntity.getPlanDetailedDescription();
        String[] description = machineDescription.split("\\n");
        int messageY = 12;
        for (String message : description) {
            guiHelper.drawStringWithShadow(233, messageY, message, colorFont);
            messageY += 9;
        }
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();

        /**
         * Progress Bars
         */
        if (tileEntity.getPlanItem() != null && tileEntity.getTicksRemaining() == 0 && tileEntity.getBlockMetadata() != TechLevel.CREATIVE.getMeta()) {
            int weightMax = tileEntity.getPlanDetails().getTotalWeight();
            int weightTotal = tileEntity.getTotalWeight();
            int weightProgressColor = colorProgressBackground;

            float weightPercent = (((float) weightTotal) / (float) weightMax) * 100;

            if (tileEntity.isMeetingBuildRequirements())
                weightProgressColor = colorProgressBackgroundGood;

            if (weightPercent > 100) {
                weightPercent = 100;
                weightProgressColor = colorProgressBackgroundBad;
            }

            guiHelper.drawHorizontalProgressBar(40, 26, 126, 8, Math.round(weightPercent), colorBackground, colorBorder, weightProgressColor);
            String weightLabel = String.format("%s: %dkg",
                    LanguageHelper.LABEL.translateMessage("weight_left"),
                    weightMax - weightTotal
            );
            guiHelper.drawCenteredStringWithShadow(40, 26, 126, weightLabel, colorFont);
        }

        if (tileEntity.getPlanItem() != null && tileEntity.getTicksRemaining() > 0 && tileEntity.getBlockMetadata() != TechLevel.CREATIVE.getMeta()) {
            int timeMax = tileEntity.getTotalTicks();
            int timeCurrent = tileEntity.getTicksRemaining();
            int timeProgressColor = colorProgressBackground;

            float timePercent = (((float) timeMax - (float) timeCurrent) / (float) timeMax) * 100;

            int secondsLeft = (timeCurrent / 20) * 1000;

            guiHelper.drawHorizontalProgressBar(40, 26, 126, 8, Math.round(timePercent), colorBackground, colorBorder, timeProgressColor);
            String timeLabel = String.format("%s: %s (%d%%)",
                    LanguageHelper.LABEL.translateMessage("time_left"),
                    DurationFormatUtils.formatDuration(secondsLeft, "mm:ss"),
                    Math.round(timePercent)
            );
            guiHelper.drawCenteredStringWithShadow(40, 26, 126, timeLabel, colorFont);
        }

        guiHelper.drawMachineUpgradeIcons(322, 12, tileEntity);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        btnStartBuilder.enabled = tileEntity.isMeetingBuildRequirements();

        if (tileEntity.getBlockMetadata() > 0) {
            btnSelectTechLevel.displayString = LanguageHelper.NONE.translateMessage(TechLevel.byMeta(tileEntity.getSelectedTechLevel()).getUnlocalizedName());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float btn) {
        super.drawScreen(mouseX, mouseY, btn);

        Slot slot = getSlotUnderMouse();
        if (slot == null)
            return;

        PlanDetails planDetails = tileEntity.getPlanDetails();
        if (planDetails != null) {
            List<PlanRequiredMaterials> requiredMaterials = planDetails.getRequiredMaterialsList();
            if (slot instanceof SlotBuilderInventory && slot.getSlotIndex() > 0 && slot.getSlotIndex() <= requiredMaterials.size() && !slot.getHasStack()) {
                renderItemStackToolTip(requiredMaterials.get(slot.getSlotIndex() - 1), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        UUID playerUUID = Minecraft.getMinecraft().thePlayer.getUniqueID();
        PacketButtonClick packetButtonClick = new PacketButtonClick(button.id, tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), playerUUID);
        PacketHandler.INSTANCE.sendToServer(packetButtonClick);
    }
}
