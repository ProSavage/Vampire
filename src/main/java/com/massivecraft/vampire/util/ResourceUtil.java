package com.massivecraft.vampire.util;

import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.vampire.XMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class ResourceUtil
{
	public static boolean playerHas(Player player, ItemStack stack)
	{
		Material requiredType = stack.getType();
		short requiredDamage = stack.getDurability();
		int requiredAmount = stack.getAmount();
		
		int actualAmount = 0;
		for (ItemStack pstack : player.getInventory().getContents())
		{
			if (pstack == null) continue;
			if (pstack.getType() != requiredType) continue;
			if (pstack.getDurability() != requiredDamage) continue;
			actualAmount += pstack.getAmount();
		}
		
		return actualAmount >= requiredAmount;
	}
	
	public static boolean playerHas(Player player, Collection<? extends ItemStack> stacks)
	{
		for (ItemStack stack : stacks)
		{
			if ( ! playerHas(player, stack)) return false;
		}
		return true;
	}
	
	public static void playerRemove(Player player, Collection<? extends ItemStack> stacks)
	{
		playerRemove(player, stacks.toArray(new ItemStack[0]));
	}
	
	public static void playerRemove(Player player, ItemStack... stacks)
	{
		player.getInventory().removeItem(stacks);
		player.updateInventory();
	}
	
	public static void playerAdd(Player player, Collection<? extends ItemStack> stacks)
	{
		Inventory inventory = player.getInventory();
		inventory.addItem(stacks.toArray(new ItemStack[0]));
		player.updateInventory();
	}
	
	public static void playerAdd(Player player, ItemStack stack)
	{
		Inventory inventory = player.getInventory();
		inventory.addItem(stack);
		player.updateInventory();
	}
	
	public static String describe(Collection<? extends ItemStack> stacks)
	{
		ArrayList<String> lines = new ArrayList<>();
		for (ItemStack stack : stacks)
		{
			String desc = describe(stack.getType(), stack.getDurability());
			lines.add(Txt.parse("<h>%d <p>%s", stack.getAmount(), desc));
					
		}
		return Txt.implode(lines, Txt.parse("<i>, "));
	}
	
	public static String describe(Material type, short damage)
	{
		if (type == XMaterial.POTION.parseMaterial() && damage == 0) return "Water Bottle";
		if (type == XMaterial.INK_SAC.parseMaterial() && damage == 4 ) return "Lapis Lazuli Dye";
		if (type == XMaterial.COAL.parseMaterial() && damage == 1 ) return "Charcoal";
		
		return Txt.getMaterialName(type);
	}
	
	public static boolean playerRemoveAttempt(Player player, Collection<? extends ItemStack> stacks, String success, String fail)
	{
		if ( ! playerHas(player, stacks))
		{
			MixinMessage.get().messageOne(player, Txt.parse(fail));
			MixinMessage.get().messageOne(player, describe(stacks));
			return false;
		}

		playerRemove(player, stacks);
		
		MixinMessage.get().messageOne(player, Txt.parse(success));
		MixinMessage.get().messageOne(player, describe(stacks));
		
		return true;
	}
	
}
