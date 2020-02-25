package net.runelite.client.plugins.pktools;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Title;

import java.awt.event.KeyEvent;

@ConfigGroup("pktools")
public interface PkToolsConfig extends Config
{

	String commands = "protectitem, rigour, augury, piety, " +
			"incrediblereflexes, ultimatestrength, steelskin, eagleeye, mysticmight, " +
			"freeze, vengeance, teleblock, entangle, " +
			"spec, doublespec, wait, clickenemy, " +
			"group1, group2, group3, group4";

	@ConfigItem(
			keyName = "label1",
			name = "Hotkeys",
			description = "",
			position = 1
	)
	default Title label1()
	{
		return new Title();
	}

	@ConfigItem(
			keyName = "key1",
			name = "Hotkey 1",
			description = "Activates script for this key.",
			position = 2
	)
	default Keybind key1()
	{
		return new Keybind(KeyEvent.VK_1, 0);
	}

	@ConfigItem(
			keyName = "key2",
			name = "Hotkey 2",
			description = "Activates script for this key.",
			position = 3
	)
	default Keybind key2()
	{
		return new Keybind(KeyEvent.VK_2, 0);
	}

	@ConfigItem(
			keyName = "key3",
			name = "Hotkey 3",
			description = "Activates script for this key.",
			position = 4
	)
	default Keybind key3()
	{
		return new Keybind(KeyEvent.VK_3, 0);
	}

	@ConfigItem(
			keyName = "key4",
			name = "Hotkey 4",
			description = "Activates script for this key.",
			position = 5
	)
	default Keybind key4()
	{
		return new Keybind(KeyEvent.VK_4, 0);
	}

	@ConfigItem(
			keyName = "key5",
			name = "Hotkey 5",
			description = "Activates script for this key.",
			position = 6
	)
	default Keybind key5()
	{
		return new Keybind(KeyEvent.VK_5, 0);
	}

	@ConfigItem(
			position = 7,
			keyName = "key1_script",
			name = "Key 1 Script",
			description = PkToolsConfig.commands
	)
	default String key1_script()
	{
		return "group1\nwait\npiety";
	}

	@ConfigItem(
			position = 8,
			keyName = "key2_script",
			name = "Key 2 Script",
			description = PkToolsConfig.commands
	)
	default String key2_script()
	{
		return "group2\nwait\nrigour";
	}

	@ConfigItem(
			position = 9,
			keyName = "key3_script",
			name = "Key 3 Script",
			description = PkToolsConfig.commands
	)
	default String key3_script()
	{
		return "group3\nwait\naugury";
	}

	@ConfigItem(
			position = 10,
			keyName = "key4_script",
			name = "Key 4 Script",
			description = PkToolsConfig.commands
	)
	default String key4_script()
	{
		return "group4\npiety\nspec\nclickenemy";
	}

	@ConfigItem(
			position = 11,
			keyName = "key5_script",
			name = "Key 5 Script",
			description = PkToolsConfig.commands
	)
	default String key5_script()
	{
		return "freeze\nclickenemy";
	}

	@ConfigItem(
			keyName = "label2",
			name = "Prayer",
			description = "",
			position = 12
	)
	default Title label2()
	{
		return new Title();
	}

	@ConfigItem(
			position = 13,
			keyName = "autoprayerswitcher",
			name = "Auto Prayer Switcher",
			description = "Automatic Prayer Switching mode using group hotkeys"
	)
	default PkToolsAutoPrayerModes autoPrayerSwitcher()
	{
		return PkToolsAutoPrayerModes.OFF;
	}

	@ConfigItem(
			position = 14,
			keyName = "autoPrayerSwitcherHotkey",
			name = "Prayer Switch Hotkey",
			description = "Hotkey to swap to the appropriate prayer"
	)
	default Keybind prayerKey()
	{
		return new Keybind(KeyEvent.VK_6, 0);
	}

	@ConfigItem(
			position = 15,
			keyName = "prayerHelper",
			name = "Prayer Helper",
			description = "Draws icons to suggest proper prayer switches"
	)
	default boolean prayerHelper()
	{
		return true;
	}

	@ConfigItem(
			keyName = "label3",
			name = "Setup (required)",
			description = "",
			position = 16
	)
	default Title label3()
	{
		return new Title();
	}

	@ConfigItem(
			position = 17,
			keyName = "inventoryTabKey",
			name = "Inventory Tab Key",
			description = "Hotkey in settings that activates the inventory tab"
	)
	default PkToolsHotkeys inventoryTabKey()
	{
		return PkToolsHotkeys.ESC;
	}

	@ConfigItem(
			position = 18,
			keyName = "combatTabKey",
			name = "Combat Tab Key",
			description = "Hotkey in settings that activates the combat tab"
	)
	default PkToolsHotkeys combatTabKey()
	{
		return PkToolsHotkeys.F1;
	}

	@ConfigItem(
			position = 19,
			keyName = "prayerTabKey",
			name = "Prayer Tab Key",
			description = "Hotkey in settings that activates the prayer tab"
	)
	default PkToolsHotkeys prayerTabKey()
	{
		return PkToolsHotkeys.F5;
	}

	@ConfigItem(
			position = 20,
			keyName = "spellbookTabKey",
			name = "Spellbook Tab Key",
			description = "Hotkey in settings that activates the spellbook tab"
	)
	default PkToolsHotkeys spellbookTabKey()
	{
		return PkToolsHotkeys.F6;
	}

	@ConfigItem(
			position = 21,
			keyName = "clickDelay",
			name = "Click Delay",
			description = "Sets the delay between clicks"
	)
	default int clickDelay()
	{
		return 30;
	}
}
