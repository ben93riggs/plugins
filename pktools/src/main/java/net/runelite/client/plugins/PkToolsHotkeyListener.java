package net.runelite.client.plugins.pktools;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyListener;
import net.runelite.client.plugins.inventorytags.InventoryTagsConfig;
import net.runelite.client.plugins.pktools.ScriptCommand.ScriptCommandFactory;

import javax.inject.Inject;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class PkToolsHotkeyListener extends MouseAdapter implements KeyListener
{

	private final Client client;
	private static final int PAUSE = KeyEvent.VK_PAUSE;

	private boolean disabled;

	static boolean prayer_hotkey;

	private Instant lastPress;

	@Inject
	private PkToolsPlugin plugin;

	@Inject
	private PkToolsConfig config;

	@Inject
	private PkToolsOverlay overlay;

	@Inject
	private ConfigManager configManager;

	private final ExecutorService executor = Executors.newCachedThreadPool();
	private final ReentrantLock lock = new ReentrantLock();

	@Inject
	private PkToolsHotkeyListener(final Client client, final PkToolsConfig config, final PkToolsPlugin plugin, final PkToolsOverlay overlay)
	{
		this.client = client;
		this.config = config;
		this.plugin = plugin;
		this.overlay = overlay;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{

		if (this.client.getGameState() != GameState.LOGGED_IN)
			return;

		if (e.getKeyCode() == this.config.prayerKey().getKeyCode())
			PkToolsHotkeyListener.prayer_hotkey = true;

		executor.submit(() -> {
			try
			{
				lock.lock();

				if (this.lastPress != null && Duration.between(this.lastPress, Instant.now()).getNano() > 1000)
				{
					this.lastPress = null;
				}

				if (this.lastPress != null)
				{
					return;
				}

				final int key_code = e.getKeyCode();

				if (key_code == PkToolsHotkeyListener.PAUSE)
				{
					this.disabled = !this.disabled;
				}

				if (this.disabled)
				{
					return;
				}

				if (key_code == this.config.key1().getKeyCode())
				{
					this.processCommands(this.config.key1_script(), this.client, this.config, this.plugin, this.overlay, this.configManager);
				}
				else if (key_code == this.config.key2().getKeyCode())
				{
					this.processCommands(this.config.key2_script(), this.client, this.config, this.plugin, this.overlay, this.configManager);
				}
				else if (key_code == this.config.key3().getKeyCode())
				{
					this.processCommands(this.config.key3_script(), this.client, this.config, this.plugin, this.overlay, this.configManager);
				}
				else if (key_code == this.config.key4().getKeyCode())
				{
					this.processCommands(this.config.key4_script(), this.client, this.config, this.plugin, this.overlay, this.configManager);
				}
				else if (key_code == this.config.key5().getKeyCode())
				{
					this.processCommands(this.config.key5_script(), this.client, this.config, this.plugin, this.overlay, this.configManager);
				}

			}
			catch (final Throwable ex)
			{
				ex.printStackTrace();
			}
			finally
			{
				lock.unlock();
			}
		});
	}

	private void processCommands(final String command, final Client client, final PkToolsConfig config, final PkToolsPlugin plugin, final PkToolsOverlay overlay, final ConfigManager configManager)
	{
		for (final String c : command.split("\\s*\n\\s*"))
		{
			ScriptCommandFactory.builder(c).execute(client, config, plugin, overlay, configManager);
		}
	}

	public static String getTag(final ConfigManager configManager, final int itemId)
	{
		final String tag = configManager.getConfiguration(InventoryTagsConfig.GROUP, "item_" + itemId);
		if (tag == null || tag.isEmpty())
		{
			return null;
		}

		return tag;
	}

	@Override
	public void keyTyped(final KeyEvent e)
	{
	}

	@Override
	public void keyReleased(final KeyEvent e)
	{
		if (e.getKeyCode() == this.config.prayerKey().getKeyCode())
			PkToolsHotkeyListener.prayer_hotkey = false;
	}
}
