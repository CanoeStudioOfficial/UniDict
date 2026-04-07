package wanion.unidict.proxy;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import wanion.unidict.UniDict;

import javax.annotation.Nonnull;

public class ClientProxy extends CommonProxy {
	@Override
	public void postInit(final FMLPostInitializationEvent event)
	{
		super.postInit(event);

		try {
			RecipeBookClient.rebuildTable();
			UniDict.getLogger().info("Fixed the Recipe Book");
		}
		catch (Exception e){
			UniDict.getLogger().error("Failed to fix Recipe Book");
			e.printStackTrace();
		}
	}

	@Override
	public EntityPlayer getEntityPlayerFromContext(@Nonnull final MessageContext messageContext)
	{
		return messageContext.side.isClient() ? Minecraft.getMinecraft().player : super.getEntityPlayerFromContext(messageContext);
	}

	@Override
	public IThreadListener getThreadListener()
	{
		return Minecraft.getMinecraft();
	}
}