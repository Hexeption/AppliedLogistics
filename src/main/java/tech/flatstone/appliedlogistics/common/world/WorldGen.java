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

package tech.flatstone.appliedlogistics.common.world;

import com.google.common.collect.ArrayListMultimap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import tech.flatstone.appliedlogistics.common.util.LogHelper;
import tech.flatstone.appliedlogistics.common.util.WorldInfoHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGen implements IWorldGenerator {
    protected static ArrayList<OreGen> oreSpawnList = new ArrayList();
    protected static ArrayList<Integer> oreDimBlackList = new ArrayList();
    protected static ArrayListMultimap<Integer, ChunkCoordIntPair> retrogenChunks = ArrayListMultimap.create();
    private int numChunks = 2;

    public static OreGen addOreGen(String name, IBlockState block, int maxVeinSize, int minY, int maxY, int chunkOccurrence, int weight) {
        OreGen oreGen = new OreGen(name, block, maxVeinSize, Blocks.stone, minY, maxY, chunkOccurrence, weight);
        oreSpawnList.add(oreGen);
        return oreGen;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (!oreDimBlackList.contains(world.provider.getDimensionId()))
            for (OreGen oreGen : oreSpawnList)
                oreGen.generate(world, random, chunkX * 16, chunkZ * 16);
    }

    public void generateOres(Random random, int chunkX, int chunkZ, World world, boolean newGeneration) {
        if (!oreDimBlackList.contains(world.provider.getDimensionId())) {
            for (OreGen gen : oreSpawnList) {
                if ((newGeneration) || (retroGenEnabled(gen.name))) {
                    gen.generate(world, random, chunkX * 16, chunkZ * 16);
                }
            }
        }
    }

    private boolean retrogenEnabled() {
        /*for (OreGen gen : oreSpawnList) {

        }*/

        return true;
    }

    private boolean retroGenEnabled(String oreName) {
        return true;
    }

    @SubscribeEvent
    public void chunkSave(ChunkDataEvent.Save event) {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        event.getData().setTag("AppliedLogistics", nbtTagCompound);
        nbtTagCompound.setBoolean("DEFAULT", true);
    }

    @SubscribeEvent
    public void chunkLoad(ChunkDataEvent.Load event) {
        int dimID = event.world.provider.getDimensionId();
        if ((!event.getData().getCompoundTag("AppliedLogistics").hasKey("DEFAULT")) && retrogenEnabled()) {
            LogHelper.info("Chunk " + event.getChunk().getChunkCoordIntPair() + " has been flagged for Ore RetroGen by Applied Logistics");
            retrogenChunks.put(dimID, event.getChunk().getChunkCoordIntPair());
        }
    }

    @SubscribeEvent
    public void serverWorldTick(TickEvent.WorldTickEvent event) {
        if ((event.side == Side.CLIENT) || (event.phase == TickEvent.Phase.START))
            return;

        int dimID = event.world.provider.getDimensionId();
        int counter = 0;

        List<ChunkCoordIntPair> chunks = retrogenChunks.get(dimID);

        if ((chunks != null) && (!chunks.isEmpty())) {
            if (WorldInfoHelper.getTps() >= 20){
                numChunks++;
            } else {
                numChunks = Math.max(2,numChunks-1);
            }

            for (int i = 1; i <= numChunks; i++) {
                int index = chunks.size() - i;
                if (index < 0)
                    return;

                counter++;

                ChunkCoordIntPair chunkCoordIntPair = chunks.get(index);
                long worldSeed = event.world.getSeed();
                Random fmlRandom = new Random(worldSeed);
                long xSeed = fmlRandom.nextLong() >> 3;
                long zSeed = fmlRandom.nextLong() >> 3;
                fmlRandom.setSeed(xSeed * chunkCoordIntPair.chunkXPos + zSeed * chunkCoordIntPair.chunkZPos ^ worldSeed);
                generateOres(fmlRandom, chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos, event.world, false);
                chunks.remove(index);
            }
        }

        if (counter > 0)
            LogHelper.info("Retrogen was performed on " + counter + " Chunks, " + Math.max(0, chunks.size()) + " chunks remaining");
    }

    public static class OreGen {
        WorldGenMinable worldGenMinable;
        int minY;
        int maxY;
        int chunkOccurrence;
        int weight;
        String name;

        public OreGen(String name, IBlockState block, int maxVeinSize, Block replaceTarget, int minY, int maxY, int chunkOccurrence, int weight) {
            this.name = name;
            this.worldGenMinable = new WorldGenMinable(block, maxVeinSize, BlockHelper.forBlock(replaceTarget));
            this.minY = minY;
            this.maxY = maxY;
            this.chunkOccurrence = chunkOccurrence;
            this.weight = weight;
        }

        public void generate(World world, Random random, int x, int z) {
            for (int i = 0; i < chunkOccurrence; i++) {
                if (random.nextInt(100) < this.weight) {
                    BlockPos blockPos = new BlockPos(x + random.nextInt(16), this.minY + random.nextInt(this.maxY - this.minY), z + random.nextInt(16));
                    this.worldGenMinable.generate(world, random, blockPos);
                }
            }
        }
    }
}
