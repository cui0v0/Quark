package vazkii.quark.client.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.module.Feature;

public class EnchantedBooksShowItems extends Feature {

	private static final List<Pair<ResourceLocation, Integer>> testItemLocs = new ArrayList<>();
	private static final List<ItemStack> testItems = new ArrayList<>();

	private static Multimap<Enchantment, ItemStack> additionalStacks = null;
	private static String[] additionalStacksArr;
	private static boolean loadedConfig;

	private static final Pattern RL_MATCHER = Pattern.compile("^((?:\\w+:)?\\w+)(@\\d+)?$");

	@Override
	public void setupConfig() {
		String[] testItemsArr = loadPropStringList("Items to Test", "", new String[] {
				"minecraft:diamond_sword", "minecraft:diamond_pickaxe", "minecraft:diamond_shovel", "minecraft:diamond_axe", "minecraft:diamond_hoe",
				"minecraft:diamond_helmet", "minecraft:diamond_chestplate", "minecraft:diamond_leggings", "minecraft:diamond_boots",
				"minecraft:shears", "minecraft:bow", "minecraft:fishing_rod", "minecraft:elytra", "quark:pickarang"
		});

		testItemLocs.clear();
		testItems.clear();
		for(String s : testItemsArr) {
			Matcher match = RL_MATCHER.matcher(s);
			if (match.matches()) {
				String metaGroup = match.group(2);
				int meta = metaGroup == null || metaGroup.isEmpty() ? 0 : Integer.parseInt(metaGroup);
				testItemLocs.add(Pair.of(new ResourceLocation(match.group(1)), meta));
			}
		}

		additionalStacksArr = loadPropStringList("Additional Stacks", 
				"A list of additional stacks to display on each enchantment\n"
						+ "The format is as follows:\n"
						+ "enchant_id=item1,item2,item3...\n"
						+ "So to display a carrot on a stick on a mending book, for example, you use:\n"
						+ "minecraft:mending=minecraftcarrot_on_a_stick", 
						new String[0]);

		if(loadedConfig)
			computeAdditionalStacks();
		loadedConfig = true;
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		computeAdditionalStacks();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void makeTooltip(ItemTooltipEvent event) {
		if(Minecraft.getMinecraft().player == null)
			return;

		ItemStack stack = event.getItemStack();
		if(stack.getItem() == Items.ENCHANTED_BOOK) {
			Minecraft mc = Minecraft.getMinecraft();
			List<String> tooltip = event.getToolTip();
			int tooltipIndex = 0;

			List<EnchantmentData> enchants = getEnchantedBookEnchantments(stack);
			for(EnchantmentData ed : enchants) {
				String match = ed.enchantment.getTranslatedName(ed.enchantmentLevel);

				for(; tooltipIndex < tooltip.size(); tooltipIndex++)
					if(tooltip.get(tooltipIndex).equals(match)) {
						List<ItemStack> items = getItemsForEnchantment(ed.enchantment);
						if(!items.isEmpty()) {
							int len = 3 + items.size() * 9;
							String spaces = "";
							while(mc.fontRenderer.getStringWidth(spaces) < len)
								spaces += " ";

							tooltip.add(tooltipIndex + 1, spaces);
						}

						break;
					}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderTooltip(RenderTooltipEvent.PostText event) {
		ItemStack stack = event.getStack();

		if(stack.getItem() == Items.ENCHANTED_BOOK) {
			Minecraft mc = Minecraft.getMinecraft();
			List<String> tooltip = event.getLines();

			GlStateManager.pushMatrix();
			GlStateManager.translate(event.getX(), event.getY() + 12, 0);
			GlStateManager.scale(0.5, 0.5, 1.0);

			List<EnchantmentData> enchants = getEnchantedBookEnchantments(stack);
			for(EnchantmentData ed : enchants) {
				String match = TextFormatting.getTextWithoutFormattingCodes(ed.enchantment.getTranslatedName(ed.enchantmentLevel));
				for(int tooltipIndex = 0; tooltipIndex < tooltip.size(); tooltipIndex++) {
					String line = TextFormatting.getTextWithoutFormattingCodes(tooltip.get(tooltipIndex));
					if(line != null && line.equals(match)) {
						int drawn = 0;

						List<ItemStack> items = getItemsForEnchantment(ed.enchantment);
						for(ItemStack testStack : items) {
							mc.getRenderItem().renderItemIntoGUI(testStack, 6 + drawn * 18, tooltipIndex * 20 - 2);
							drawn++;
						}

						break;
					}
				}
			}
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}

	public static List<ItemStack> getItemsForEnchantment(Enchantment e) {
		List<ItemStack> list = new ArrayList<>();
		if (testItems.isEmpty() && !testItemLocs.isEmpty()) {
			for (Pair<ResourceLocation, Integer> loc : testItemLocs) {
				Item item = Item.REGISTRY.getObject(loc.getKey());
				if (item != null)
					testItems.add(new ItemStack(item, 1, loc.getValue()));
			}
		}

		for(ItemStack stack : testItems)
			if(e.canApply(stack))
				list.add(stack);

		if(additionalStacks.containsKey(e))
			list.addAll(additionalStacks.get(e));

		return list;
	}

	public static List<EnchantmentData> getEnchantedBookEnchantments(ItemStack stack) {
		NBTTagList nbttaglist = ItemEnchantedBook.getEnchantments(stack);
		List<EnchantmentData> retList = new ArrayList<>(nbttaglist.tagCount() + 1);

		for(int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound.getShort("id");
			Enchantment enchantment = Enchantment.getEnchantmentByID(j);
			short level = nbttagcompound.getShort("lvl");

			if (enchantment != null)
				retList.add(new EnchantmentData(enchantment, level));
		}

		return retList;
	}

	private void computeAdditionalStacks() {
		additionalStacks = HashMultimap.create();

		for(String s : additionalStacksArr) {
			System.out.println("Ench: " + s);
			if(!s.contains("="))
				continue;

			String[] toks = s.split("=");
			String left = toks[0];
			String right = toks[1];

			Enchantment ench = Enchantment.REGISTRY.getObject(new ResourceLocation(left));
			System.out.println(ench);
			if(ench != null) {
				toks = right.split(",");

				for(String itemId : toks) {
					System.out.println(itemId);
					Item item = Item.REGISTRY.getObject(new ResourceLocation(itemId));
					System.out.println("Item: " + item);
					if(item != null)
						additionalStacks.put(ench, new ItemStack(item));
				}
			}
		}
	}

}
