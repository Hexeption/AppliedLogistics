/*
 * LIMITED USE SOFTWARE LICENSE AGREEMENT
 * This Limited Use Software License Agreement (the "Agreement") is a legal agreement between you, the end-user, and the FlatstoneTech Team ("FlatstoneTech"). By downloading or purchasing the software material, which includes source code (the "Source Code"), artwork data, music and software tools (collectively, the "Software"), you are agreeing to be bound by the terms of this Agreement. If you do not agree to the terms of this Agreement, promptly destroy the Software you may have downloaded or copied.
 * FlatstoneTech SOFTWARE LICENSE
 * 1. Grant of License. FlatstoneTech grants to you the right to use the Software. You have no ownership or proprietary rights in or to the Software, or the Trademark. For purposes of this section, "use" means loading the Software into RAM, as well as installation on a hard disk or other storage device. The Software, together with any archive copy thereof, shall be destroyed when no longer used in accordance with this Agreement, or when the right to use the Software is terminated. You agree that the Software will not be shipped, transferred or exported into any country in violation of the U.S. Export Administration Act (or any other law governing such matters) and that you will not utilize, in any other manner, the Software in violation of any applicable law.
 * 2. Permitted Uses. For educational purposes only, you, the end-user, may use portions of the Source Code, such as particular routines, to develop your own software, but may not duplicate the Source Code, except as noted in paragraph 4. The limited right referenced in the preceding sentence is hereinafter referred to as "Educational Use." By so exercising the Educational Use right you shall not obtain any ownership, copyright, proprietary or other interest in or to the Source Code, or any portion of the Source Code. You may dispose of your own software in your sole discretion. With the exception of the Educational Use right, you may not otherwise use the Software, or an portion of the Software, which includes the Source Code, for commercial gain.
 * 3. Prohibited Uses: Under no circumstances shall you, the end-user, be permitted, allowed or authorized to commercially exploit the Software. Neither you nor anyone at your direction shall do any of the following acts with regard to the Software, or any portion thereof:
 *  * Rent;
 *  * Sell;
 *  * Lease;
 *  * Offer on a pay-per-play basis;
 *  * Distribute for money or any other consideration; or
 *  * In any other manner and through any medium whatsoever commercially exploit or use for any commercial purpose.
 *  * Notwithstanding the foregoing prohibitions, you may commercially exploit the software you develop by exercising the Educational Use right, referenced in paragraph 2. hereinabove.
 *  4. Copyright. The Software and all copyrights related thereto (including all characters and other images generated by the Software or depicted in the Software) are owned by FlatstoneTech and is protected by United States copyright laws and international treaty provisions. FlatstoneTech shall retain exclusive ownership and copyright in and to the Software and all portions of the Software and you shall have no ownership or other proprietary interest in such materials. You must treat the Software like any other copyrighted material. You may not otherwise reproduce, copy or disclose to others, in whole or in any part, the Software. You may not copy the written materials accompanying the Software. You agree to use your best efforts to see that any user of the Software licensed hereunder complies with this Agreement.
 *  5. NO WARRANTIES. FLATSTONETECH DISCLAIMS ALL WARRANTIES, BOTH EXPRESS IMPLIED, INCLUDING BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. THIS LIMITED WARRANTY GIVES YOU SPECIFIC LEGAL RIGHTS. YOU MAY HAVE OTHER RIGHTS WHICH VARY FROM JURISDICTION TO JURISDICTION. FlatstoneTech DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR MEET YOUR SPECIFIC REQUIREMENTS. THE WARRANTY SET FORTH ABOVE IS IN LIEU OF ALL OTHER EXPRESS WARRANTIES WHETHER ORAL OR WRITTEN. THE AGENTS, EMPLOYEES, DISTRIBUTORS, AND DEALERS OF FlatstoneTech ARE NOT AUTHORIZED TO MAKE MODIFICATIONS TO THIS WARRANTY, OR ADDITIONAL WARRANTIES ON BEHALF OF FlatstoneTech.
 *  Exclusive Remedies. The Software is being offered to you free of any charge. You agree that you have no remedy against FlatstoneTech, its affiliates, contractors, suppliers, and agents for loss or damage caused by any defect or failure in the Software regardless of the form of action, whether in contract, tort, includinegligence, strict liability or otherwise, with regard to the Software. Copyright and other proprietary matters will be governed by United States laws and international treaties. IN ANY CASE, FlatstoneTech SHALL NOT BE LIABLE FOR LOSS OF DATA, LOSS OF PROFITS, LOST SAVINGS, SPECIAL, INCIDENTAL, CONSEQUENTIAL, INDIRECT OR OTHER SIMILAR DAMAGES ARISING FROM BREACH OF WARRANTY, BREACH OF CONTRACT, NEGLIGENCE, OR OTHER LEGAL THEORY EVEN IF FLATSTONETECH OR ITS AGENT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to you.
 */

package tech.flatstone.appliedlogistics.common.blocks.misc;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tech.flatstone.appliedlogistics.AppliedLogisticsCreativeTabs;
import tech.flatstone.appliedlogistics.ModInfo;
import tech.flatstone.appliedlogistics.client.particles.ParticleCauldronSmokeLarge;
import tech.flatstone.appliedlogistics.client.particles.ParticleCauldronSmokeNormal;
import tech.flatstone.appliedlogistics.client.particles.ParticleCauldronSplash;
import tech.flatstone.appliedlogistics.common.blocks.BlockTileBase;
import tech.flatstone.appliedlogistics.common.tileentities.misc.TileEntityCauldron;
import tech.flatstone.appliedlogistics.common.util.IProvideEvent;
import tech.flatstone.appliedlogistics.common.util.IProvideRecipe;
import tech.flatstone.appliedlogistics.common.util.Platform;
import tech.flatstone.appliedlogistics.common.util.TileHelper;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BlockCauldron extends BlockTileBase implements IProvideRecipe, IProvideEvent {
    public static final AxisAlignedBB AABB_BASE = new AxisAlignedBB(0.125, 0.25, 0.125, 0.875, 0.3125, 0.875);
    public static final AxisAlignedBB AABB_WATER = new AxisAlignedBB(0.1875, 0.3125, 0.1875, 0.8125, 0.8125, 0.8125);
    public static final AxisAlignedBB AABB_PRECIPITATE = new AxisAlignedBB(0.1875, 0.3125, 0.1875, 0.8125, 0.375, 0.8125);
    protected static final AxisAlignedBB AABB_LEG_1_SEGMENT_1 = new AxisAlignedBB(0.125, 0.1875, 0.1875, 0.1875, 0.25, 0.3125);
    protected static final AxisAlignedBB AABB_LEG_1_SEGMENT_2 = new AxisAlignedBB(0.125, 0.1875, 0.125, 0.3125, 0.25, 0.1875);
    protected static final AxisAlignedBB AABB_LEG_2_SEGMENT_1 = new AxisAlignedBB(0.125, 0.1875, 0.6875, 0.1875, 0.25, 0.8125);
    protected static final AxisAlignedBB AABB_LEG_2_SEGMENT_2 = new AxisAlignedBB(0.125, 0.1875, 0.8125, 0.3125, 0.25, 0.875);
    protected static final AxisAlignedBB AABB_LEG_3_SEGMENT_1 = new AxisAlignedBB(0.8125, 0.1875, 0.6875, 0.875, 0.25, 0.8125);
    protected static final AxisAlignedBB AABB_LEG_3_SEGMENT_2 = new AxisAlignedBB(0.6875, 0.1875, 0.8125, 0.875, 0.25, 0.875);
    protected static final AxisAlignedBB AABB_LEG_4_SEGMENT_1 = new AxisAlignedBB(0.8125, 0.1875, 0.1875, 0.875, 0.25, 0.3125);
    protected static final AxisAlignedBB AABB_LEG_4_SEGMENT_2 = new AxisAlignedBB(0.6875, 0.1875, 0.125, 0.875, 0.25, 0.1875);
    protected static final AxisAlignedBB AABB_WALL_1 = new AxisAlignedBB(0.125, 0.3125, 0.1875, 0.1875, 0.875, 0.875);
    protected static final AxisAlignedBB AABB_WALL_2 = new AxisAlignedBB(0.8125, 0.3125, 0.125, 0.875, 0.875, 0.8125);
    protected static final AxisAlignedBB AABB_WALL_3 = new AxisAlignedBB(0.125, 0.3125, 0.125, 0.8125, 0.875, 0.1875);
    protected static final AxisAlignedBB AABB_WALL_4 = new AxisAlignedBB(0.1875, 0.3125, 0.8125, 0.875, 0.875, 0.875);
    protected static final AxisAlignedBB AABB_LOG_1 = new AxisAlignedBB(0.40625, -0.000625, 0.0625, 0.59375, 0.186875, 0.9375);
    protected static final AxisAlignedBB AABB_LOG_3 = new AxisAlignedBB(0.0625, 0.0, 0.375, 0.9375, 0.1875, 0.5625);
    protected static final AxisAlignedBB AABB_WOOD = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.1875, 0.9375);
    protected static final AxisAlignedBB[] BOXES = new AxisAlignedBB[]{AABB_LEG_1_SEGMENT_1, AABB_LEG_1_SEGMENT_2, AABB_LEG_2_SEGMENT_1, AABB_LEG_2_SEGMENT_2, AABB_LEG_3_SEGMENT_1,
            AABB_LEG_3_SEGMENT_2, AABB_LEG_4_SEGMENT_1, AABB_LEG_4_SEGMENT_2, AABB_BASE, AABB_WALL_1, AABB_WALL_2, AABB_WALL_3, AABB_WALL_4, AABB_WOOD};
    protected static final AxisAlignedBB AABB_BOUNDING_BOX = new AxisAlignedBB(0.125, 0.1875, 0.125, 0.875, 0.875, 0.875);

    private static final PropertyBool CAULDRON_LIT = PropertyBool.create("cauldron_lit");

    public BlockCauldron() {
        super(Material.ROCK, "misc/cauldron");
        setTileEntity(TileEntityCauldron.class);
        setCreativeTab(AppliedLogisticsCreativeTabs.GENERAL);
        this.setInternalName("cauldron");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void RegisterRecipes() {
        //GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(Blocks.CAULDRON));
    }

    @Override
    public void registerBlockRenderer() {

    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockItemRenderer() {
        final String resourcePath = String.format("%s:%s", ModInfo.MOD_ID, this.resourcePath);

        NonNullList<ItemStack> subBlocks = NonNullList.create();
        getSubBlocks(Item.getItemFromBlock(this), null, subBlocks);

        //todo: remove this into a class, duplicated code...
        for (ItemStack itemStack : subBlocks) {
            IBlockState blockState = this.getStateFromMeta(itemStack.getItemDamage());
            Map<IProperty<?>, Comparable<?>> properties = new HashMap<>();
            for (Map.Entry<IProperty<?>, Comparable<?>> entry : blockState.getProperties().entrySet()) {
                if (entry.getKey() != FACING && entry.getKey() != CAULDRON_LIT)
                    properties.put(entry.getKey(), entry.getValue());
            }

            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), itemStack.getItemDamage(), new ModelResourceLocation(resourcePath, Platform.getPropertyString(properties)));
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntityCauldron tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityCauldron.class);
        if (tileEntity != null) {
            return state.withProperty(FACING, tileEntity.getForward()).withProperty(CAULDRON_LIT, tileEntity.isFireLit());
        }
        return state.withProperty(FACING, EnumFacing.NORTH).withProperty(CAULDRON_LIT, false);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, CAULDRON_LIT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
        for (int i = 0; i < BOXES.length; i++) {
            AxisAlignedBB box = BOXES[i];
            if (box.equals(AABB_WOOD)) {
                box = box.expand(-0.0625, 0, -0.0625);
            }
            addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
        }
    }

    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        RayTraceResult lookObject = getExtendedRayTraceResult(start, end, pos);
        if (lookObject != null) {
            return new RayTraceResult(lookObject.hitVec.addVector(pos.getX(), pos.getY(), pos.getZ()), lookObject.sideHit, pos);
        }
        return null;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB_BOUNDING_BOX;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
        super.onBlockPlacedBy(world, blockPos, state, placer, itemStack);
        TileEntityCauldron tileEntity = TileHelper.getTileEntity(world, blockPos, TileEntityCauldron.class);
        if (tileEntity != null) {
            tileEntity.liftHandle();
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        worldIn.removeTileEntity(pos);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void drawBlockHighlight(DrawBlockHighlightEvent event) {
        if (!(event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK && event.getPlayer().world.getBlockState(event.getTarget().getBlockPos()).getBlock() instanceof BlockCauldron)) {
            return;
        }
        EntityPlayer player = Minecraft.getMinecraft().player;
        BlockPos pos = event.getTarget().getBlockPos();
        ExtendedRayTraceResult rayTraceResult = getExtendedRayTraceResultFromPlayer(player, pos);
        if (rayTraceResult != null) {
            if (rayTraceResult.isLookingAtLogs) {
                event.setCanceled(true);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GlStateManager.glLineWidth(2.0F);
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

                double partialTicks = event.getPartialTicks();
                double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
                double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
                double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

                RenderGlobal.drawSelectionBoundingBox(AABB_WOOD.offset(pos).expandXyz(0.0020000000949949026D).offset(-d0, -d1, -d2), 0.0F, 0.0F, 0.0F, 0.4F);

                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GlStateManager.disableBlend();
            }
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState blockState = getActualState(getDefaultState(), world, pos);
        return blockState.getValue(CAULDRON_LIT) ? 10 : 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntityCauldron tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityCauldron.class);
        if (tileEntity == null)
            return true;

        ExtendedRayTraceResult lookObject = getExtendedRayTraceResultFromPlayer(playerIn, pos);

        if (tileEntity.isEmpty()) {

        }

        Item item = playerIn.getHeldItem(hand).getItem();

        if (lookObject != null && lookObject.isLookingAtLogs) {
            return interactWithLogs(worldIn, pos, tileEntity, playerIn, hand, playerIn.getHeldItem(hand), item, lookObject);
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    private boolean interactWithLogs(World worldIn, BlockPos pos, TileEntityCauldron tileEntity, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, Item item, ExtendedRayTraceResult lookObject) {
        if (item == Items.WATER_BUCKET) {
            worldIn.playSound(playerIn, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            int n = tileEntity.isFireLit() ? 4 : 3;
            IParticleFactory[] particles = new IParticleFactory[n];
            for (int i = 0; i < 3; i++) {
                particles[i] = new ParticleCauldronSplash.Factory();
            }
            if (tileEntity.isFireLit()) {
                particles[3] = new ParticleCauldronSmokeLarge.Factory();
                worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
                tileEntity.setFireLit(false);
                tileEntity.markDirty();
                tileEntity.markForLightUpdate();
            }
            spawnParticlesForLogs(worldIn, pos, lookObject, 25, particles);
            if (!worldIn.isRemote) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
                }
            }
            return true;
        } else if (item == Items.FLINT_AND_STEEL) {
            worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, worldIn.rand.nextFloat() * 0.4F + 0.8F);
            spawnParticlesForLogs(worldIn, pos, lookObject, 16, new ParticleFlame.Factory(), new ParticleCauldronSmokeNormal.Factory());
            heldItem.damageItem(1, playerIn);
            tileEntity.setFireLit(true);
            tileEntity.markDirty();
            tileEntity.markForLightUpdate();
            return true;
        }
        return false;
    }

    private ExtendedRayTraceResult getExtendedRayTraceResultFromPlayer(EntityPlayer player, BlockPos pos) {
        double reach = 5;
        if (player instanceof EntityPlayerMP) {
            reach = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
        }
        Vec3d start = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d end = start.add(new Vec3d(player.getLookVec().xCoord * reach, player.getLookVec().yCoord * reach, player.getLookVec().zCoord * reach));
        ExtendedRayTraceResult rayTraceResult = getExtendedRayTraceResult(start, end, pos);
        return rayTraceResult;
    }

    private ExtendedRayTraceResult getExtendedRayTraceResult(Vec3d start, Vec3d end, BlockPos pos) {
        RayTraceResult lookObject = null;
        double shortestDistance = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < BOXES.length; i++) {
            RayTraceResult rayTraceResult = BOXES[i].offset(pos).calculateIntercept(start, end);
            if (rayTraceResult != null) {
                double distance = start.distanceTo(rayTraceResult.hitVec);
                if (distance < shortestDistance) {
                    lookObject = rayTraceResult;
                    shortestDistance = distance;
                    index = i;
                }
            }
        }
        boolean isLookingAtPrecipitate = false;
        RayTraceResult rayTraceResult = AABB_PRECIPITATE.offset(pos).calculateIntercept(start, end);
        if (rayTraceResult != null && start.distanceTo(rayTraceResult.hitVec) < shortestDistance) {
            isLookingAtPrecipitate = true;
        }
        return lookObject == null ? null : new ExtendedRayTraceResult(lookObject, index == 13, isLookingAtPrecipitate);
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticlesForLogs(World worldIn, BlockPos pos, ExtendedRayTraceResult lookObject, int particleCount, IParticleFactory... particleFactories) {
        if (!worldIn.isRemote)
            return;
        Vec3d particlePos;
        if (lookObject != null) {
            Vec3d hit = lookObject.hitVec;
            particlePos = new Vec3d(hit.xCoord + Math.random() * 0.02 - 0.01, hit.yCoord + Math.random() * 0.02 - 0.01, hit.zCoord + Math.random() * 0.01);
            for (IParticleFactory particleFactory : particleFactories) {
                Platform.spawnParticle(worldIn, particlePos, particleFactory);
            }
        }
        Vec3d logXZCenter = new Vec3d(0.5, 0, 0.5);
        Vec3d particleXZPos;
        for (int k = 0; k < particleCount; ++k) {
            while (true) {
                particleXZPos = new Vec3d(Math.random(), 0, Math.random());
                if (particleXZPos.distanceTo(logXZCenter) > (AABB_WOOD.maxZ - AABB_WOOD.minZ) / 2)
                    continue;
                AxisAlignedBB boundBox = AABB_WOOD;
                particlePos = new Vec3d(pos.getX() + particleXZPos.xCoord, pos.getY() + Math.random() * (boundBox.contract(0.03125).maxY - boundBox.contract(0.125).minY) + boundBox.contract(0.125).minY, pos.getZ() + particleXZPos.zCoord);
                for (IParticleFactory particleFactory : particleFactories) {
                    Platform.spawnParticle(worldIn, particlePos, particleFactory);
                }
                break;
            }
        }
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        IBlockState blockState = getActualState(getDefaultState(), worldIn, pos);
        if (blockState.getValue(CAULDRON_LIT) && rand.nextDouble() < 0.1D) {
            worldIn.playSound(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
    }

    private static class ExtendedRayTraceResult extends RayTraceResult {
        private boolean isLookingAtLogs, isLookingAtPrecipitate;

        public ExtendedRayTraceResult(RayTraceResult rayTraceResult, boolean isLookingAtLogs, boolean isLookingAtPrecipitate) {
            super(rayTraceResult.hitVec, rayTraceResult.sideHit, rayTraceResult.getBlockPos());
            this.isLookingAtLogs = isLookingAtLogs;
            this.isLookingAtPrecipitate = isLookingAtPrecipitate;
        }
    }
}
