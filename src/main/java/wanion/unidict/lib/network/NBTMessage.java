package wanion.unidict.lib.network;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.unidict.UniDict;
import wanion.unidict.lib.common.INBTMessage;

public class NBTMessage implements IMessage
{
	private int windowId;
	private NBTTagCompound nbtMessage;

	public NBTMessage() {}

	public NBTMessage(final int windowId, final NBTTagCompound nbtMessage)
	{
		this.windowId = windowId;
		this.nbtMessage = nbtMessage;
	}

	public int getWindowId()
	{
		return windowId;
	}

	public NBTTagCompound getNbtMessage()
	{
		return nbtMessage;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.windowId = ByteBufUtils.readVarInt(buf, 5);
		this.nbtMessage = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, windowId, 5);
		ByteBufUtils.writeTag(buf, nbtMessage);
	}

	public static class Handler implements IMessageHandler<NBTMessage, NBTAnswer>
	{
		@Override
		public NBTAnswer onMessage(final NBTMessage nbtMessage, final MessageContext ctx)
		{
			final EntityPlayer entityPlayer = UniDict.proxy.getEntityPlayerFromContext(ctx);
			UniDict.proxy.getThreadListener().addScheduledTask(() -> {
				if (entityPlayer != null && entityPlayer.openContainer.windowId == nbtMessage.getWindowId() && entityPlayer.openContainer instanceof INBTMessage)
					((INBTMessage) entityPlayer.openContainer).receiveNBT(nbtMessage.getNbtMessage());
			});
			return new NBTAnswer(nbtMessage.windowId, nbtMessage.nbtMessage);
		}
	}
}