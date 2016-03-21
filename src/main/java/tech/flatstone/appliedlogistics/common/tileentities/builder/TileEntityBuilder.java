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

package tech.flatstone.appliedlogistics.common.tileentities.builder;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import org.apache.commons.lang3.time.DurationFormatUtils;
import tech.flatstone.appliedlogistics.api.features.IMachinePlan;
import tech.flatstone.appliedlogistics.api.features.TechLevel;
import tech.flatstone.appliedlogistics.api.registries.PlanRegistry;
import tech.flatstone.appliedlogistics.common.integrations.waila.IWailaBodyMessage;
import tech.flatstone.appliedlogistics.common.items.ItemPlanBase;
import tech.flatstone.appliedlogistics.common.tileentities.TileEntityInventoryBase;
import tech.flatstone.appliedlogistics.common.tileentities.inventory.InternalInventory;
import tech.flatstone.appliedlogistics.common.tileentities.inventory.InventoryOperation;
import tech.flatstone.appliedlogistics.common.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class TileEntityBuilder extends TileEntityInventoryBase implements ITickable, INetworkButton, IWailaBodyMessage {
    private InternalInventory inventory = new InternalInventory(this, 56);
    private HashMap<TechLevel, PlanDetails> planDetails = new HashMap<TechLevel, PlanDetails>();
    private String planName = "";
    private ItemStack planItem = null;
    private int selectedTechLevel = -1;
    private int buildingTechLevel = -1;
    private int ticksRemaining = 0;
    private boolean machineWorking = false;

    public int getTotalWeight() {
        int weight = 0;
        int invSlot = 1;

        for (PlanRequiredMaterials material : getPlanDetails().getRequiredMaterialsList()) {
            int materialWeight = material.getItemWeight();
            int itemCount = 0;
            ItemStack itemInSlot = inventory.getStackInSlot(invSlot);
            if (itemInSlot != null)
                itemCount = itemInSlot.stackSize;
            weight += (materialWeight * itemCount);

            invSlot++;
        }

        return weight;
    }

    public boolean isMeetingBuildRequirements() {
        int invSlot = 1;

        if (planDetails == null)
            return false;

        if (inventory.getStackInSlot(28) != null)
            return false;

        if (ticksRemaining > 0)
            return false;

        if (getPlanDetails() == null)
            return false;

        for (PlanRequiredMaterials material : getPlanDetails().getRequiredMaterialsList()) {
            if (material.getMinCount() > 0) {
                ItemStack itemInSlot = inventory.getStackInSlot(invSlot);
                if (itemInSlot == null)
                    return false;

                if (itemInSlot.stackSize < material.getMinCount())
                    return false;
            }

            invSlot++;
        }

        if (planDetails != null && getTotalWeight() > getPlanDetails().getTotalWeight())
            return false;

        return true;
    }

    public int getSelectedTechLevel() {
        return selectedTechLevel;
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }

    public int getBuildingTechLevel() {
        return buildingTechLevel;
    }

    public Set<TechLevel> getTechLevels() {
        return planDetails.keySet();
    }

    public PlanDetails getPlanDetails(TechLevel techLevel) {
        if (planDetails.containsKey(techLevel))
            return planDetails.get(techLevel);

        return null;
    }

    public PlanDetails getPlanDetails() {
        if (planDetails.containsKey(TechLevel.byMeta(selectedTechLevel)))
            return planDetails.get(TechLevel.byMeta(selectedTechLevel));

        return null;
    }

    public ItemStack getPlanItem() {
        return planItem;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        selectedTechLevel = nbtTagCompound.getInteger("selectedTechLevel");
        ticksRemaining = nbtTagCompound.getInteger("ticksRemaining");
        machineWorking = nbtTagCompound.getBoolean("machineWorking");
        buildingTechLevel = nbtTagCompound.getInteger("buildingTechLevel");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        nbtTagCompound.setInteger("selectedTechLevel", selectedTechLevel);
        nbtTagCompound.setInteger("ticksRemaining", ticksRemaining);
        nbtTagCompound.setBoolean("machineWorking", machineWorking);
        nbtTagCompound.setInteger("buildingTechLevel", buildingTechLevel);
    }

    private void updatePlanDetails() {
        ItemStack itemStack = inventory.getStackInSlot(0);

        if (planItem != null && !planItem.isItemEqual(itemStack)) {
            planItem = null;
        }

        if (planItem == null && !planDetails.isEmpty()) {
            planDetails.clear();
            planName = "";
            planChange();
            return;
        }

        if (itemStack == null)
            return;

        if (!planDetails.isEmpty())
            return;

        if (!itemStack.hasTagCompound() || !itemStack.getTagCompound().hasKey("PlanType"))
            return;

        planName = itemStack.getTagCompound().getString("PlanType");
        ItemPlanBase planBase = (ItemPlanBase) PlanRegistry.getPlanAsItem(planName);

        if (planBase == null) {
            // todo: invalidate item
            LogHelper.warn("Plan no longer matches an item, either a mod has changed, or something else bad happened...");
            LogHelper.warn("Plan raw name is: " + planName);
            return;
        }

        if (!(planBase instanceof IMachinePlan))
            return;

        for (int i = getBlockMetadata(); i >= 0; i--) {
            TechLevel techLevel = TechLevel.byMeta(i);
            PlanDetails details = ((IMachinePlan) planBase).getTechLevels(techLevel);
            if (details != null)
                planDetails.put(techLevel, details);
        }

        if (planDetails.isEmpty()) {
            LogHelper.fatal("The plan that was inserted has no techlevel recipes... this is probably not good...");
            LogHelper.fatal("Plan Name: " + planName);
            return;
        }

        planItem = itemStack;
        planChange();
    }

    private void planChange() {
        if (planItem == null && Platform.isServer()) {
            TileHelper.DropItems(this);
        }

        if (planItem == null)
            return;

        if (selectedTechLevel == -1) {
            for (TechLevel techLevel : planDetails.keySet()) {
                if (techLevel.getMeta() > selectedTechLevel)
                    selectedTechLevel = techLevel.getMeta();
            }
        }

        this.markForUpdate();
        this.markDirty();
    }

    @Override
    public IInventory getInternalInventory() {
        return inventory;
    }

    @Override
    public void onChangeInventory(IInventory inv, int slot, InventoryOperation operation, ItemStack removed, ItemStack added) {

    }

    @Override
    public int[] getAccessibleSlotsBySide(EnumFacing side) {
        return new int[0];
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public void update() {
        updatePlanDetails();

        if (getTotalTicks() > 0 && machineWorking) {
            ticksRemaining--;
        }

        if (ticksRemaining <= 0 && machineWorking) {
            machineWorking = false;
            ticksRemaining = 0;
            // Machine is done...
            if (Platform.isClient())
                return;

            if (getPlanDetails() == null)
                return;

            ItemStack outputItem = getPlanDetails(TechLevel.byMeta(buildingTechLevel)).getItemOutput().copy();
            buildingTechLevel = -1;

            NBTTagCompound tagMachineItems = new NBTTagCompound();
            NBTTagCompound tagCompound = new NBTTagCompound();
            int j = 0;
            for (int i = 29; i < 56; i++) {
                NBTTagCompound item = new NBTTagCompound();
                ItemStack itemStack = this.getStackInSlot(i);
                if (itemStack != null) {
                    itemStack.writeToNBT(item);
                    tagCompound.setTag("item_" + j, item);
                    j++;
                }
            }
            tagMachineItems.setTag("MachineItemData", tagCompound);
            outputItem.setTagCompound(tagMachineItems);

            this.inventory.setInventorySlotContents(28, outputItem);

            for (int i = 29; i < 56; i++) {
                inventory.setInventorySlotContents(i, null);
            }

            this.markForUpdate();
            this.markDirty();
        }
    }

    @Override
    public List<String> getWailaBodyToolTip(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        List<String> newTooltip = currentTip;

        if (planDetails == null || getPlanDetails() == null || planItem == null)
            return newTooltip;

        newTooltip.add(String.format("%s: %s",
                LanguageHelper.LABEL.translateMessage("plan"),
                LanguageHelper.NONE.translateMessage(planItem.getUnlocalizedName() + ".name")
        ));

        if (ticksRemaining == 0)
            return newTooltip;

        float timePercent = ((((float) getTotalTicks() - (float) ticksRemaining) / (float) getTotalTicks())) * 100;
        int secondsLeft = (ticksRemaining / 20) * 1000;

        newTooltip.add(String.format("%s: %s (%d%%)",
                LanguageHelper.LABEL.translateMessage("time_left"),
                DurationFormatUtils.formatDuration(secondsLeft, "mm:ss"),
                Math.round(timePercent)
        ));

        return newTooltip;
    }

    @Override
    public void actionPerformed(int buttonID) {
        switch (buttonID) {
            case 0: // Build
                inventoryToInternal();
                buildingTechLevel = selectedTechLevel;
                ticksRemaining = getTotalTicks();
                machineWorking = true;
                this.markForUpdate();
                this.markDirty();
                break;

            case 1: // Previous Tech Level
                changeLevel(getPrevTechLevel());
                break;

            case 2: // Next Tech Level
                changeLevel(getNextTechLevel());
                break;
        }
    }

    public int getNextTechLevel() {
        int nextTechLevel = selectedTechLevel;
        for (int i = selectedTechLevel + 1; i <= this.getBlockMetadata(); i++) {
            if (planDetails.containsKey(TechLevel.byMeta(i)))
                return i;
        }
        return nextTechLevel;
    }

    public int getPrevTechLevel() {
        int prevTechLevel = selectedTechLevel;
        for (int i = selectedTechLevel - 1; i >= 0; i--) {
            if (planDetails.containsKey(TechLevel.byMeta(i)))
                return i;
        }
        return prevTechLevel;
    }

    private void changeLevel(int newTechLevel) {
        selectedTechLevel = newTechLevel;
        TileHelper.DropItems(this, 1, 27);
        this.markForUpdate();
        this.markDirty();
    }

    private void inventoryToInternal() {
        int invSlot = 1;

        for (PlanRequiredMaterials material : getPlanDetails().getRequiredMaterialsList()) {
            ItemStack itemIn = inventory.getStackInSlot(invSlot);
            if (itemIn != null) {
                inventory.setInventorySlotContents(invSlot + 28, itemIn);
                inventory.setInventorySlotContents(invSlot, null);
            }
            invSlot++;
        }
    }

    public int getTotalTicks() {
        int ticks = 0;
        int invSlot = 28;

        if (getPlanDetails() == null)
            return 0;

        for (PlanRequiredMaterials material : getPlanDetails(TechLevel.byMeta(buildingTechLevel)).getRequiredMaterialsList()) {
            int tickTime = material.getAddTime();
            int itemCount = 0;
            ItemStack itemInSlot = inventory.getStackInSlot(invSlot);
            if (itemInSlot != null)
                itemCount = itemInSlot.stackSize;
            ticks += (tickTime * itemCount);

            invSlot++;
        }

        return ticks;
    }

    public int getComparatorOutput() {
        if (planDetails == null)
            return 0;

        if (planItem == null)
            return 0;

        if (getPlanDetails() == null)
            return 0;

        // Check to see if overweight
        if (getTotalWeight() > getPlanDetails().getTotalWeight())
            return 15;

        // Done Building
        if (inventory.getStackInSlot(28) != null)
            return 4;

        // Building...
        if (ticksRemaining > 0)
            return 3;

        // Ok to Build
        if (isMeetingBuildRequirements())
            return 2;

        // Valid Plan
        return 1;
    }

    public String getPlanDetailedDescription() {
        ItemPlanBase planBase = (ItemPlanBase) PlanRegistry.getPlanAsItem(planName);

        if (planBase == null || !(planBase instanceof IMachinePlan))
            return "";

        List<ItemStack> inventory = new ArrayList<ItemStack>();
        for (int i = 1; i < 27; i++) {
            inventory.add(getInternalInventory().getStackInSlot(i));
        }

        return ((IMachinePlan) planBase).getMachineDetails(TechLevel.byMeta(selectedTechLevel), inventory);
    }
}
