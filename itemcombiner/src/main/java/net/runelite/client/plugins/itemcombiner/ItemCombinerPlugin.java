package net.runelite.client.plugins.itemcombiner;

import com.google.inject.Provides;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.Instant;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuAction;
import net.runelite.api.Point;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.HotkeyListener;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "Item Combiner",
	description = "Automatically uses items on another item",
	tags = {"skilling", "item", "object", "combiner"},
	enabledByDefault = false
)
public class ItemCombinerPlugin extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ConfigManager configManager;

	@Inject
	ItemCombinerConfig config;

	@Inject
	private ItemManager itemManager;

	@Inject
	private KeyManager keyManager;

	private Instant lastPress;

	Random r = new Random();

	MenuEntry waitEntry = new MenuEntry();
	public Queue<MenuEntry> entryList = new ConcurrentLinkedQueue<>();

	@Provides
	ItemCombinerConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(ItemCombinerConfig.class);
	}

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.useItemsKeybind())
	{
		@Override
		public void hotkeyPressed()
		{
			clientThread.invokeLater(() -> {
				if (client.getGameState() != GameState.LOGGED_IN)
				{
					return;
				}

				if (lastPress != null && Duration.between(lastPress, Instant.now()).getNano() > 1000)
				{
					lastPress = null;
				}

				if (lastPress != null)
				{
					return;
				}

				entryList.clear();

				try {
					final int itemId = config.itemId();
					final int itemId2 = config.itemId2();
					final Widget inventory = client.getWidget(WidgetInfo.INVENTORY);

					if (inventory == null)
					{
						return;
					}

					for (int i = config.iterations(); i > 0; i--)
					{

						final WidgetItem firstItem = inventory.getWidgetItems().stream().filter(inventoryItem -> inventoryItem.getId() == itemId).findFirst().orElse(null);

						if (firstItem == null)
						{
							return;
						}

						final WidgetItem secondItem = inventory.getWidgetItems().stream().filter(inventoryItem -> inventoryItem.getId() == itemId2).findFirst().orElse(null);

						if (secondItem == null)
						{
							return;
						}

						entryList.add(new MenuEntry("Use", "<col=ff9040>" + itemManager.getItemComposition(firstItem.getId()).getName(), firstItem.getId(), MenuAction.ITEM_USE.getId(), firstItem.getIndex(), WidgetInfo.INVENTORY.getId(), false));

						int randDelay = r.nextInt(config.waitMax() - config.waitMin()) + config.waitMin();

						for (int j = 0; j < randDelay; j++)
						{
							entryList.add(waitEntry);
						}

						entryList.add(new MenuEntry("Use", "<col=ff9040>" + itemManager.getItemComposition(firstItem.getId()).getName() + "<col=ffffff> -> <col=ff9040>" + itemManager.getItemComposition(secondItem.getId()).getName(), secondItem.getId(), MenuAction.ITEM_USE_ON_WIDGET_ITEM.getId(), secondItem.getIndex(), WidgetInfo.INVENTORY.getId(), false));

						randDelay = r.nextInt(config.waitMax() - config.waitMin()) + config.waitMin();

						for (int j = 0; j < randDelay; j++)
						{
							entryList.add(waitEntry);
						}
					}
					click();
				}
				catch (final Exception e)
				{
					e.printStackTrace();
				}
			});
		}
	};

	@Override
	protected void startUp() throws Exception
	{
		this.keyManager.registerKeyListener(hotkeyListener);
		entryList.clear();
	}

	@Override
	protected void shutDown() throws Exception
	{
		this.keyManager.unregisterKeyListener(hotkeyListener);
		entryList.clear();
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (entryList != null && !entryList.isEmpty())
		{
			event.setMenuEntry(entryList.poll());

			if (entryList == null || entryList.isEmpty())
			{
				return;
			}

			click();
		}
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
