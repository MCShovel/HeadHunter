package com.asiru.headhunter.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.asiru.headhunter.command.sub.AddWorld;
import com.asiru.headhunter.command.sub.BountyAdd;
import com.asiru.headhunter.command.sub.BountyCheck;
import com.asiru.headhunter.command.sub.BountyList;
import com.asiru.headhunter.command.sub.BountyRemove;
import com.asiru.headhunter.command.sub.Help;
import com.asiru.headhunter.command.sub.Reload;
import com.asiru.headhunter.command.sub.RemoveWorld;
import com.asiru.headhunter.command.sub.SellHead;

public class MainExecutor implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("hunter")) {
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("sellhead"))
					SellHead.run(sender, args);
				else if(args[0].equalsIgnoreCase("addworld"))
					AddWorld.run(sender, args);
				else if(args[0].equalsIgnoreCase("removeworld"))
					RemoveWorld.run(sender, args);
				else if(args[0].equalsIgnoreCase("reload"))
					Reload.run(sender, args);
				else if(args[0].equalsIgnoreCase("help"))
					Help.run(sender, args);
				else if(args[0].equalsIgnoreCase("list"))
					BountyList.run(sender, args);
				else if(args[0].equalsIgnoreCase("bounty")) {
					if(args.length > 1) {
						if(args[1].equalsIgnoreCase("add"))
							BountyAdd.run(sender, args);
						else if(args[1].equalsIgnoreCase("remove"))
							BountyRemove.run(sender, args);
						else if(args[1].equalsIgnoreCase("check"))
							BountyCheck.run(sender, args);
						else if(args[1].equalsIgnoreCase("list"))
							BountyList.run(sender, args);
						else
							Help.run(sender, args);
					}
					else
						Help.run(sender, args);
				}
				else
					Help.run(sender, args);
			}
			else
				Help.run(sender, args);
		}
		else if(label.equalsIgnoreCase("sellhead"))
			cmd.execute(sender, "hunter", new String[]{"sellhead"});
		else if(label.equalsIgnoreCase("bounty")) {
			String[] args2 = new String[args.length + 1];
			args2[0] = "bounty";
			for(int i = 0; i < args.length; i++)
				args2[i + 1] = args[i];
			cmd.execute(sender, "hunter", args2);
		}
		return false;
	}
}