package net.runelite.client.plugins.pktools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.SpriteID;
import net.runelite.api.kit.KitType;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import com.openosrs.client.util.WeaponMap;
import com.openosrs.client.util.WeaponStyle;

@Singleton
public class PkToolsOverlay extends Overlay
{
	private final Client client;
	private final PkToolsPlugin pkToolsPlugin;
	private final PkToolsConfig config;
	private final SpriteManager spriteManager;
	private final PanelComponent imagePanelComponent = new PanelComponent();

	private static final Color NOT_ACTIVATED_BACKGROUND_COLOR = new Color(150, 0, 0, 150);

	Dimension panel_size = new Dimension(27, 40);

	public static Point lastEnemyLocation;

	@Inject
	private PkToolsOverlay(Client client, PkToolsPlugin plugin, SpriteManager spriteManager, PkToolsConfig config)
	{
		this.client = client;
		this.pkToolsPlugin = plugin;
		this.spriteManager = spriteManager;
		this.config = config;

		this.setPosition(OverlayPosition.BOTTOM_RIGHT);
		this.setPriority(OverlayPriority.HIGH);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (this.client.getGameState() != GameState.LOGGED_IN)
		{
			return null;
		}

		Player lastEnemy = pkToolsPlugin.lastEnemy;

		ImageComponent PROTECT_MELEE_IMG = new ImageComponent(spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MELEE, 0));
		ImageComponent PROTECT_MISSILES_IMG = new ImageComponent(spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MISSILES, 0));
		ImageComponent PROTECT_MAGIC_IMG = new ImageComponent(spriteManager.getSprite(SpriteID.PRAYER_PROTECT_FROM_MAGIC, 0));

		imagePanelComponent.getChildren().clear();
		imagePanelComponent.getChildren().add(TitleComponent.builder().text("PK").color(config.autoPrayerSwitcherEnabled() ? Color.GREEN : Color.red).build());
		imagePanelComponent.setBackgroundColor(ComponentConstants.STANDARD_BACKGROUND_COLOR);
		imagePanelComponent.setPreferredSize(panel_size);

		if (lastEnemy != null)
		{
			int WEAPON_INT = Objects.requireNonNull(lastEnemy.getPlayerComposition()).getEquipmentId(KitType.WEAPON);

			WeaponStyle style = WeaponMap.StyleMap.getOrDefault(WEAPON_INT, null);

			if (style == null)
			{
				return imagePanelComponent.render(graphics);
			}

			if (config.prayerHelper())
			{
				switch (style)
				{
					case MELEE:
						imagePanelComponent.getChildren().add(PROTECT_MELEE_IMG);

						if (!client.isPrayerActive(Prayer.PROTECT_FROM_MELEE))
						{
							imagePanelComponent.setBackgroundColor(PkToolsOverlay.NOT_ACTIVATED_BACKGROUND_COLOR);
						}
						break;
					case RANGE:
						imagePanelComponent.getChildren().add(PROTECT_MISSILES_IMG);

						if (!client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES))
						{
							imagePanelComponent.setBackgroundColor(PkToolsOverlay.NOT_ACTIVATED_BACKGROUND_COLOR);
						}
						break;
					case MAGIC:
						imagePanelComponent.getChildren().add(PROTECT_MAGIC_IMG);

						if (!client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
						{
							imagePanelComponent.setBackgroundColor(PkToolsOverlay.NOT_ACTIVATED_BACKGROUND_COLOR);
						}
						break;
					default:
						break;
				}
			}
		}
		else
		{
			PkToolsOverlay.lastEnemyLocation = null;
		}

		return imagePanelComponent.render(graphics);
	}
}