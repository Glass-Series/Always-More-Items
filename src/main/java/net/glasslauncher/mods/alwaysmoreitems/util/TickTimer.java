package net.glasslauncher.mods.alwaysmoreitems.util;

import net.minecraft.client.Minecraft;

public class TickTimer implements net.glasslauncher.mods.alwaysmoreitems.api.gui.TickTimer {
	private final int ticksPerCycle;
	private final int maxValue;
	private final boolean countDown;

	private long lastUpdateWorldTime = 0;
	private int tickCount = 0;

	public TickTimer(int ticksPerCycle, int maxValue, boolean countDown) {
		this.ticksPerCycle = ticksPerCycle;
		this.maxValue = maxValue;
		this.countDown = countDown;
	}

	@Override
	public int getValue() {
		long worldTime = Minecraft.INSTANCE.world.getTime();
		long ticksPassed = worldTime - lastUpdateWorldTime;
		lastUpdateWorldTime = worldTime;
		tickCount += (int) ticksPassed;
		if (tickCount > ticksPerCycle) {
			tickCount = 0;
		}

		int value = MathUtil.divideCeil(tickCount * maxValue, ticksPerCycle);
		if (countDown) {
			return maxValue - value;
		} else {
			return value;
		}
	}

	@Override
	public int getMaxValue() {
		return maxValue;
	}
}
