package tech.flatstone.appliedlogistics.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tech.flatstone.appliedlogistics.common.util.IRotatable;

public class PacketBlockRotated implements IMessage, IMessageHandler<PacketBlockRotated, IMessage> {
    public BlockPos blockPos;

    public PacketBlockRotated() {

    }

    public PacketBlockRotated(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(final PacketBlockRotated message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
            @SideOnly(Side.CLIENT)
            @Override
            public void run() {
                TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(message.blockPos);
                if (tileEntity != null && tileEntity instanceof IRotatable) {
                    ((IRotatable) tileEntity).onRotated();
                }
            }
        });

        return null;
    }
}