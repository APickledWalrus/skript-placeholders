# skript-placeholders
PlaceholderAPI integration for Skript

This plugin is a fork of Ersatz (by Pikachu - https://github.com/Pikachu920/Ersatz) which has been updated to work with PlaceholderAPI's new PlaceholderExpansion system.

# Links

**Skript:** https://github.com/SkriptLang/Skript

**PlaceholderAPI:** https://www.spigotmc.org/resources/placeholderapi.6245/

**MVdWPlaceholderAPI:** https://www.spigotmc.org/resources/mvdwplaceholderapi.11182/ (Not Required)

**SkUnity Post:** https://forums.skunity.com/resources/skript-placeholders.909/

**Skript Chat Discord:** https://discord.gg/XuMmNbk

# Examples and Documentation

The patterns haven't changed, so documentation is also available at SkriptHub under Ersatz:

https://skripthub.net/docs/?addon=Ersatz

**Getting the Value of a Placeholder**

If you want to get the value of a placeholder, use the Value of Placeholder expression.
```
[the] ([value of] placeholder[s]|placeholder [value] [of]) %strings% [from %players%]

send "Your Ping: %placeholder ""player_ping"" from player%"
```

As of **1.2**, you can get the value of a MVdW placeholder by using curly brackets around the name (e.g. **{NAME}**).

Note: If you don't specify the players, it will default to the player/executor.

**Registering Placeholders**

To register a placeholder, use the Placeholder Request Event.
```
[on] (placeholder[api]|papi) request with [the] prefix %string%
```

In a placeholder, there are two parts: the **prefix** and the **identifier**
The first part is the prefix, and the second part is the identifier (**PREFIX_IDENTIFIER**).

With the event, you declare the prefix, but you still need to set the identifier.
To do that, use the Placeholder Identifier expression.
```
[the] [(placeholder[api]|papi)] identifier
```

In the event, you can check for the identifier by writing:
```
if the identifier is "health":
```

Now that you have a prefix and an identifier, you are ready to set the result.
To set a result, you must use the Placeholder Result expression.
```
[the] [(placeholder[api]|papi)] result
```

Let's say that we want to make a placeholder for the player's health, we could do:

```
on placeholder request with prefix "skript":
	if the identifier is "health": # The placeholder would be skript_health
		set the result to player's health
```

Additionally, you can get the prefix if you ever need to with the Placeholder Prefix expression.
```
[the] [(placeholder[api]|papi)] (prefix|placeholder)
```

If you have any questions, feel free to ask here or in the Skript Chat discord.
