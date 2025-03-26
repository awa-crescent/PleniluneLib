package lib.plenilune.client.render.item;

import net.minecraft.world.item.ItemStack;

public interface ItemStackFunc {
	default public boolean hasFoil(ItemStack itemStack) {
		return itemStack.hasFoil();
	}

	ItemStackFunc hasFoil_Default = new ItemStackFunc() {

	};

	ItemStackFunc hasFoil_Always_True = new ItemStackFunc() {
		@Override
		public boolean hasFoil(ItemStack itemStack) {
			return true;
		}
	};

	ItemStackFunc hasFoil_Always_False = new ItemStackFunc() {
		@Override
		public boolean hasFoil(ItemStack itemStack) {
			return false;
		}
	};

}
