package essie.beefchunkus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static essie.beefchunkus.items.BeefChunkusItem.*;
import static essie.beefchunkus.items.MendingAppleItem.mendingApple;

public class BeefChunkusCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.getInventory().addItem(beefChunkus);
                player.getInventory().addItem(mendingApple);
            }

            return true;
        }
}
