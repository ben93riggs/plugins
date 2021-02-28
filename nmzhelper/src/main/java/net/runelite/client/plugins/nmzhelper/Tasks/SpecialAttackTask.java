package net.runelite.client.plugins.nmzhelper.Tasks;

import java.util.Random;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuAction;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.nmzhelper.MiscUtils;
import net.runelite.client.plugins.nmzhelper.NMZHelperConfig;
import net.runelite.client.plugins.nmzhelper.NMZHelperPlugin;
import net.runelite.client.plugins.nmzhelper.Task;

public class SpecialAttackTask extends Task
{
	private final Random r = new Random();

	int nextSpecialValue = r.nextInt(config.specialAttackMax() - config.specialAttackMin()) + config.specialAttackMin();

	public SpecialAttackTask(NMZHelperPlugin plugin, Client client, NMZHelperConfig config)
	{
		super(plugin, client, config);
	}

	@Override
	public boolean validate()
	{
		//option is disabled in config
		if (!config.useSpecialAttack())
		{
			return false;
		}

		//not in the nightmare zone
		if (!MiscUtils.isInNightmareZone(client))
		{
			return false;
		}

		//spec already enabled
		if (client.getVar(VarPlayer.SPECIAL_ATTACK_ENABLED) == 1)
		{
			return false;
		}

		//value returns 1000 for 100% spec, 500 for 50%, etc
		if (client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) < nextSpecialValue * 10)
		{
			return false;
		}

		Widget specialOrb = client.getWidget(160, 30);

		if (specialOrb == null || specialOrb.isHidden())
		{
			return false;
		}

		return true;
	}

	@Override
	public String getTaskDescription()
	{
		return "Use Special Attack";
	}

	@Override
	public void onGameTick(GameTick event)
	{
		entry = new MenuEntry("Use <col=00ff00>Special Attack</col>", "", 1, MenuAction.CC_OP.getId(), -1, 38862884, false);
		click();

		nextSpecialValue = r.nextInt(config.specialAttackMax() - config.specialAttackMin()) + config.specialAttackMin();
	}
}
