package lib.plenilune.item;

import java.util.ArrayList;
import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class ItemUtils {
	public static class EnchantmentLevel {
		public final String enchantment_id;
		public final int level;

		public EnchantmentLevel(ResourceKey<Enchantment> resourceKey, int level) {
			if (resourceKey == null)
				this.enchantment_id = null;
			else {
				String key = resourceKey.toString();
				this.enchantment_id = key.substring("ResourceKey[minecraft:enchantment / ".length(), key.length() - 1);
			}
			this.level = level;
		}

		public EnchantmentLevel(String enchantment_id, int level) {
			this.enchantment_id = enchantment_id;
			this.level = level;
		}

		@Override
		public String toString() {
			return "{enchantment_id=" + enchantment_id + ", level=" + level + '}';
		}
	}

	public static ArrayList<EnchantmentLevel> getAllEnchantmentsProperties(ItemStack stack) {
		ArrayList<EnchantmentLevel> list = new ArrayList<>();
		Set<it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<Holder<Enchantment>>> enchantments_set = stack.getEnchantments().entrySet();// 获取物品所有附魔
		for (it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments_set) {
			list.add(new EnchantmentLevel(entry.getKey().unwrapKey().orElse(null), entry.getIntValue()));
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	public static String getID(Item item) {
		String key = item.builtInRegistryHolder().key().toString();
		return key.substring("ResourceKey[minecraft:item / ".length(), key.length() - 1);
	}

}
