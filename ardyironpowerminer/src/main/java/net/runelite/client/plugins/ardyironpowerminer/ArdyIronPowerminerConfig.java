package net.runelite.client.plugins.ardyironpowerminer;

import net.runelite.client.config.*;

@ConfigGroup("ardyironpowerminer")
public interface ArdyIronPowerminerConfig extends Config
{
	@ConfigTitle(
		position = 0,
		keyName = "delaySection",
		name = "Settings",
		description = ""
	)
	String delaySection = "delaySection";

	@ConfigItem(
		keyName = "clickDelayMin",
		name = "Click Delay Min (ms)",
		description = "The minimum delay between clicks (in milliseconds)",
		position = 0,
		title = "delaySection"
	)
	default int clickDelayMin()
	{
		return 20;
	}

	@ConfigItem(
		keyName = "clickDelayMax",
		name = "Click Delay Max (ms)",
		description = "The maximum delay between clicks (in milliseconds)",
		position = 1,
		title = "delaySection"
	)
		default int clickDelayMax()
		{
			return 200;
		}

	@ConfigTitle(
		position = 1,
		keyName = "controlsSection",
		name = "Controls",
		description = ""
	)
	String controlsSection = "controlsSection";

	@ConfigItem(keyName = "startButton",
		name = "Start",
		description = "",
		position = 0,
		title = "controlsSection"
	)
	default Button startButton() { return new Button(); }

	@ConfigItem(keyName = "stopButton",
		name = "Stop",
		description = "",
		position = 1,
		title = "controlsSection"
	)
	default Button stopButton() { return new Button(); }
}