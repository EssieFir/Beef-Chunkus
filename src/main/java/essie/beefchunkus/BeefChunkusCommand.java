package essie.beefchunkus;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BeefChunkusCommand implements CommandExecutor {

        // This method is called, when somebody uses our command
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.getInventory().addItem(BeefChunkusItem.beefChunkus);
            }

            return true;
        }
}
