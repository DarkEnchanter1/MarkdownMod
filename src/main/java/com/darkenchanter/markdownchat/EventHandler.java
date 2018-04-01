package com.darkenchanter.markdownchat;

import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventHandler {
	private static Pattern pattern = Pattern.compile("(<.*?> §r).*");
	@SubscribeEvent
	public void chatReceived(ClientChatReceivedEvent chatEvent) {
		String message = chatEvent.getMessage().getFormattedText();
		String usrId = null;
		Main.logger.info(message);
		try {
			Matcher match = pattern.matcher(message);
			match.matches();
			usrId = match.group(1);
		} catch (Exception e) {
			Main.logger.info("Failed to match somehow...");
		}
		String[] split = message.split("<.*?> §r");
		if (split.length > 1)
			message = split[1]; //Makes sure that this is a player chat message. Bukkit servers will be hell to support (so I'll probably try and make a GUI
		// to allow the user to manually choose the splitting string)
		if (message.startsWith(">")) {
			message = "§a" + message; //Appends the greentext value to a message at the start of the message itself, not the nametag.
			// TODO: Make a config option
		}
		message = formatAll(message, "***", "§o§l", false);
		message = formatAll(message, "*", "§o", true);
		message = formatAll(message, "**", "§l", false);
		message = formatAll(message, "_", "§o", true);
		message = formatAll(message, "__", "§n", false);
		int i = -1;
		while ((i = message.indexOf('\\', i + 1)) >= 0) {
			message = message.substring(0, i) + message.substring(i + 1);
			if (message.charAt(i + 1) == '\\')
				i += 2;
		}

		/* * * * * * * * * * * * END OF EDITS TO MESSAGE, THIS SECTION NEEDS TO HAPPEN LAST * * * * * * * * * * * */
		Main.logger.info(chatEvent.getMessage().getSiblings().size());
		chatEvent.setMessage(new TextComponentString((usrId != null ? usrId : "") + message));
	}
	public static String formatAll(String message, String ch, String replaceVal, boolean hasDoubleVariant) {
		int i = -1;
		while ((i = message.indexOf(ch, i + 1)) >= 0) {
			Main.logger.info("Check for " + ch + " at index " + i);
			if (isNotEscaped(message, i)) {
				Main.logger.info("First check succeeded");
				if (!message.substring(i + ch.length(), i + ch.length() * 2).equals(ch)) {
					int end = message.indexOf(ch, i + ch.length());
					Main.logger.info("First and a halfth check succeeded, end is " + end);
					if (end != -1 && end != i - 1 && isNotEscaped(message, end) && (!hasDoubleVariant ||
							!message.substring(end + ch.length(), end + ch.length() * 2).equals(ch))) {
						Main.logger.info("Second check succeeded");
						message = message.substring(0, i) + replaceVal + message.substring(i + ch.length(), end) + "§r" + message.substring(end + ch.length());
					}
				}
			}
		}
		return message;
	}
	public static boolean isNotEscaped(String message, int i) {
		if (i == 0) return true;
		if (i == 1) return message.charAt(i - 1) != '\\';
		return !(message.charAt(i - 1) == '\\' && message.charAt(i - 2) != '\\');
	}
}
