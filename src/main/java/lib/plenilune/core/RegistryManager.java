package lib.plenilune.core;

import java.util.IdentityHashMap;
import java.util.Optional;

import lib.lunar.jvm.Manipulator;
import lib.lunar.nativemc.Version;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class RegistryManager {
	public static final RegistryAccess registryAccess;

	public static final Registry<Enchantment> enchantment;
	public static final Registry<Item> item;
	public static final Registry<Level> dimension;
	public static final Registry<LevelStem> level_stem;
	public static final Registry<DimensionType> dimension_type;
	public static final Registry<Biome> biome;

	static {
		registryAccess = Minecraft.getInstance().getConnection().registryAccess();
		enchantment = getRegistry(Registries.ENCHANTMENT);
		item = getRegistry(Registries.ITEM);
		dimension = getRegistry(Registries.DIMENSION);
		dimension_type = getRegistry(Registries.DIMENSION_TYPE);
		level_stem = getRegistry(Registries.LEVEL_STEM);
		biome = getRegistry(Registries.BIOME);
	}

	/**
	 * 获取注册表
	 * 
	 * @param <T>          ResourceKey类型
	 * @param resource_key ResourceKey参数定义于net.minecraft.core.registries.Registries
	 * @return 返回注册表实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> Registry<T> getRegistry(ResourceKey<? extends Registry<T>> resource_key) {
		Registry<T> registry = null;
		if (Version.this_version.between("1_20_R0", "1_21_R2"))
			registry = (Registry<T>) (((Optional<Registry<T>>) (Manipulator.invoke(registryAccess, "method_33310)", new Class<?>[] { resource_key.getClass() }, resource_key))).orElseThrow());// 1.21.0为registry()
		else if (Version.this_version.newerOrEqual("1_21_R2"))
			registry = (Registry<T>) (((Optional<Registry<T>>) (Manipulator.invoke(registryAccess, "method_46759)", new Class<?>[] { resource_key.getClass() }, resource_key))).orElseThrow());// 1.21.3及以上为lookup()
		if (registry == null)
			System.err.println("NMS cannot get Registry of " + resource_key);
		return registry;
	}

	/**
	 * @param <T>
	 * @param reg          注册表条目
	 * @param resource_key 该条目下的注册表键值对的key
	 * @return 对应resource_key的值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRegistryValue(Registry<T> reg, ResourceKey<T> resource_key) {
		T reg_value = (T) (Manipulator.invoke(reg, "method_29107", new Class<?>[] { resource_key.getClass() }, resource_key));// 1.21.0为get() 1.21.3为getValue()
		if (reg_value == null)
			System.err.println("NMS cannot get Registry value of " + resource_key);
		return reg_value;
	}

	/**
	 * @param <T>
	 * @param reg          注册表条目
	 * @param resource_key 该条目下的注册表键值对的key
	 * @return 对应resource_key的值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRegistryValue(Registry<T> reg, ResourceLocation resource_loc) {
		T reg_value = (T) (Manipulator.invoke(reg, "method_63535", new Class<?>[] { resource_loc.getClass() }, resource_loc));// 1.21.0为get() 1.21.3为getValue()
		if (reg_value == null)
			System.err.println("NMS cannot get Registry value of " + resource_loc);
		return reg_value;
	}

	public static <T> T getRegistryValue(Registry<T> reg, ResourceKey<? extends Registry<T>> registries_entry, String reg_key) {
		return getRegistryValue(reg, ResourceLocationBuilder.getResourceKey(registries_entry, reg_key));
	}

	@SuppressWarnings("unchecked")
	public static <T> Holder.Reference<T> getRegistryHolder(Registry<T> reg, ResourceLocation resource_loc) {
		Holder.Reference<T> holder = null;
		if (Version.this_version.between("1_20_R0", "1_21_R2"))
			holder = ((Optional<Holder.Reference<T>>) (Manipulator.invoke(reg, "method_40264", new Class<?>[] { resource_loc.getClass() }, resource_loc))).orElse(null);// 1.21.0为getHolder()
		else if (Version.this_version.newerOrEqual("1_21_R2"))
			holder = ((Optional<Holder.Reference<T>>) (Manipulator.invoke(reg, "method_10223)", new Class<?>[] { resource_loc.getClass() }, resource_loc))).orElse(null);// 1.21.3及以上为get()
		if (holder == null)
			System.err.println("NMS cannot get Registry value of " + resource_loc);
		return holder;
	}

	public static <T> boolean isFrozen(ResourceKey<? extends Registry<T>> resource_key) {
		return (boolean) Manipulator.access(getRegistry(resource_key), "net.minecraft.core.MappedRegistry.frozen");
	}

	/**
	 * 解冻注册表冻结以注册，不记录入is_registrise_frozen，因此尽量不要使用
	 * 
	 * @return 操作是否成功
	 */
	public static <T> boolean unfreezeRegistry(Registry<T> registry) {
		return Manipulator.setBoolean(registry, "net.minecraft.core.MappedRegistry.frozen", false) && Manipulator.setObject(registry, "net.minecraft.core.MappedRegistry.unregisteredIntrusiveHolders", new IdentityHashMap<>());
	}

	/**
	 * 解冻注册表冻结以注册，并记录入is_registrise_frozen，应当总是使用该方法解冻注册表
	 * 
	 * @return 操作是否成功
	 */
	public static <T> boolean unfreezeRegistry(ResourceKey<? extends Registry<T>> resource_key) {
		return unfreezeRegistry(getRegistry(resource_key));
	}

	/**
	 * 冻结注册表，并记录入is_registrise_frozen
	 * 
	 * @return
	 */
	public static <T> void freezeRegistry(Registry<T> registry) {
		if (Version.this_version.between("1_20_R0", "1_21_R2"))
			registry.freeze();
	}

	public static <T> void freezeRegistry(ResourceKey<? extends Registry<T>> resource_key) {
		freezeRegistry(getRegistry(resource_key));
	}
}
