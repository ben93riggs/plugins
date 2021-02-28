package net.runelite.client.plugins.continueclicker;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuAction;
import net.runelite.api.Point;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import org.pf4j.Extension;

@Singleton
@Extension
@PluginDescriptor(
	name = "Continue Clicker",
	description = "APresses continue on dialogue when available",
	tags = {"continue", "chat", "dialogue", "clicker"},
	enabledByDefault = false
)
public class ContinueClickerPlugin extends Plugin
{
	@Inject
	public Client client;

	public MenuEntry entry;

	@Override
	protected void startUp() throws Exception { }

	@Override
	protected void shutDown() throws Exception { }

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Widget widget = getContinueWidget();

		if (widget == null || widget.isHidden())
		{
			return;
		}

		entry = new MenuEntry("Continue", "", 0, MenuAction.WIDGET_TYPE_6.getId(), -1, widget.getId(), false);
		click();
	}

	public Widget getContinueWidget()
	{
		Widget widget = client.getWidget(WidgetInfo.DIALOG_NPC_CONTINUE);

		if (widget != null && !widget.isHidden())
		{
			return widget;
		}

		widget = client.getWidget(WidgetInfo.DIALOG_PLAYER_CONTINUE);

		if (widget != null && !widget.isHidden())
		{
			return widget;
		}

		widget = client.getWidget(WidgetInfo.DIALOG2_SPRITE_CONTINUE);

		if (widget != null && !widget.isHidden())
		{
			return widget;
		}

		widget = client.getWidget(WidgetInfo.DIALOG_NOTIFICATION_CONTINUE);

		if (widget != null && !widget.isHidden())
		{
			return widget;
		}

		return null;
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (entry != null)
		{
			event.setMenuEntry(entry);
		}

		entry = null;
	}

	public void click()
	{
		Point pos = client.getMouseCanvasPosition();

		if (client.isStretchedEnabled())
		{
			final Dimension stretched = client.getStretchedDimensions();
			final Dimension real = client.getRealDimensions();
			final double width = (stretched.width / real.getWidth());
			final double height = (stretched.height / real.getHeight());
			final Point point = new Point((int) (pos.getX() * width), (int) (pos.getY() * height));
			client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), 501, System.currentTimeMillis(), 0, point.getX(), point.getY(), 1, false, 1));
			client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), 502, System.currentTimeMillis(), 0, point.getX(), point.getY(), 1, false, 1));
			client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), 500, System.currentTimeMillis(), 0, point.getX(), point.getY(), 1, false, 1));
			return;
		}

		client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), 501, System.currentTimeMillis(), 0, pos.getX(), pos.getY(), 1, false, 1));
		client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), 502, System.currentTimeMillis(), 0, pos.getX(), pos.getY(), 1, false, 1));
		client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), 500, System.currentTimeMillis(), 0, pos.getX(), pos.getY(), 1, false, 1));
	}
}