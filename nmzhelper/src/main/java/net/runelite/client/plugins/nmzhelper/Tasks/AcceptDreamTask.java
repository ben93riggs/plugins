package net.runelite.client.plugins.nmzhelper.Tasks;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.nmzhelper.NMZHelperConfig;
import net.runelite.client.plugins.nmzhelper.NMZHelperPlugin;
import net.runelite.client.plugins.nmzhelper.Task;

public class AcceptDreamTask extends Task
{
	public AcceptDreamTask(NMZHelperPlugin plugin, Client client, NMZHelperConfig config)
	{
		super(plugin, client, config);
	}

	@Override
	public boolean validate()
	{
		//nmz dream accept button
		Widget acceptWidget = client.getWidget(129, 6);

		return acceptWidget != null && !acceptWidget.isHidden();
	}

	@Override
	public String getTaskDescription()
	{
		return "Accepting Dream";
	}
	
	@Override
	public void onGameTick(GameTick event)
	{
		entry = new MenuEntry("Continue", "", 0, MenuAction.WIDGET_TYPE_6.getId(), -1, 8454150, false);
		click();
	}
}
