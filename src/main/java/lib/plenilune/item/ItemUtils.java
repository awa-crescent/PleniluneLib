package lib.plenilune.item;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import lib.lunar.jvm.Manipulator;
import lib.lunar.nativemc.Version;
import lib.plenilune.core.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class ItemUtils {
	private static final int version_case;

	static {
		if (Version.this_version.between("1_20_R0", "1_21_R0")) {
			version_case = 0;
		} else if (Version.this_version.newerOrEqual("1_21_R0")) {
			version_case = 1;
		} else
			version_case = -1;
	}

	public static class EnchantmentLevel {
		public final String enchantment_id;
		public final int level;

		public EnchantmentLevel(ResourceKey<Enchantment> resourceKey, int level) {
			this.enchantment_id = getEnchantmentId(resourceKey);
			this.level = level;
		}

		public EnchantmentLevel(Enchantment enchantment, int level) {
			this.enchantment_id = getEnchantmentId(enchantment);
			this.level = level;
		}

		public EnchantmentLevel(String enchantment_id, int level) {
			this.enchantment_id = enchantment_id;
			this.level = level;
		}

		public static String getEnchantmentId(ResourceKey<Enchantment> resourceKey) {
			if (resourceKey == null)
				return null;
			else {
				String key = resourceKey.toString();
				return key.substring("ResourceKey[minecraft:enchantment / ".length(), key.length() - 1);
			}
		}

		@SuppressWarnings("unchecked")
		public static String getEnchantmentId(Enchantment enchantment) {
			ResourceKey<Enchantment> resourceKey = null;
			Registry<Enchantment> registry = null;
			switch (version_case) {
			case 0: {
				// BuiltInRegistries.ENCHANTMENT.getResourceKey(enchantment);
				registry = (Registry<Enchantment>) Manipulator.access(BuiltInRegistries.class, "field_41176");
				resourceKey = registry.getResourceKey(enchantment).orElse(null);
			}
				break;
			case 1: {
				registry = RegistryManager.enchantment;
				resourceKey = registry.getResourceKey(enchantment).orElse(null);
			}
				break;
			}
			return getEnchantmentId(resourceKey);
		}

		@Override
		public String toString() {
			return "{enchantment_id=" + enchantment_id + ", level=" + level + '}';
		}

	}

	@SuppressWarnings("unchecked")
	public static ArrayList<EnchantmentLevel> getAllEnchantmentsProperties(ItemStack stack) {
		ArrayList<EnchantmentLevel> list = new ArrayList<>();
		switch (version_case) {
		case 0: {
			// EnchantmentHelper.getEnchantments(ItemStack arg0)
			Map<Enchantment, Integer> map = (Map<Enchantment, Integer>) Manipulator.invoke(EnchantmentHelper.class, "method_8222", new Class<?>[] { ItemStack.class }, stack);
			Set<Entry<Enchantment, Integer>> enchantments_set = map.entrySet();// 获取物品所有附魔
			for (Entry<Enchantment, Integer> entry : enchantments_set) {
				list.add(new EnchantmentLevel(entry.getKey(), entry.getValue()));
			}
		}
			break;
		case 1: {
			// Set<it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<Holder<Enchantment>>> enchantments_set = stack.getEnchantments().entrySet();// 获取物品所有附魔
			Object ItemEnchantments = Manipulator.invoke(stack, "method_58657", null);
			Set<Object2IntMap.Entry<Holder<Enchantment>>> enchantments_set = (Set<Object2IntMap.Entry<Holder<Enchantment>>>) Manipulator.invoke(ItemEnchantments, "method_57539", null);
			for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments_set) {
				list.add(new EnchantmentLevel(entry.getKey().unwrapKey().orElse(null), entry.getIntValue()));
			}
		}
			break;
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	public static String getID(Item item) {
		String key = item.builtInRegistryHolder().key().toString();
		return key.substring("ResourceKey[minecraft:item / ".length(), key.length() - 1);
	}

}
