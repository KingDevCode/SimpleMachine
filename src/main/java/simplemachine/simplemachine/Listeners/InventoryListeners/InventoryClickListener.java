package simplemachine.simplemachine.Listeners.InventoryListeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import simplemachine.simplemachine.Components.Machine;
import simplemachine.simplemachine.GUI.CollectorInventory;
import simplemachine.simplemachine.GUI.Handlers.NavigationHandler;
import simplemachine.simplemachine.GUI.ItemGeneratorInventory;
import simplemachine.simplemachine.GUI.ItemGeneratorSpeedModifyInventory;
import simplemachine.simplemachine.Tools.Matrix;

import static simplemachine.simplemachine.SimpleMachine.navigationHandlerHashMap;
import static simplemachine.simplemachine.Tools.Functies.*;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null)return;

        if (e.getInventory().getTitle().equals("§7§l| §eItem Generator §7§l|")){
            e.setCancelled(true);
            if (e.getClickedInventory() instanceof PlayerInventory)return;

            Machine machine = navigationHandlerHashMap.get(player).getMachine();

            if (e.getSlot() == 4){
                if (machine.getItemGenerator().isEnabled()){
                    player.sendMessage(getMessage("Item Generator On"));
                    return;
                }
                ItemGeneratorInventory itemGeneratorInventory = new ItemGeneratorInventory(player);
                itemGeneratorInventory.openProductConfiguration();
                return;
            }
            switch (clickedItem.getType()){
                case LAVA_BUCKET:
                    player.sendMessage(getMessage("Current Fuel Level"));
                    break;
                case FEATHER:
                    if (player.hasPermission("SMachine.itemgenerator.change")){
                        ItemGeneratorSpeedModifyInventory itemGeneratorSpeedModifyInventory = new ItemGeneratorSpeedModifyInventory(player, machine);
                        itemGeneratorSpeedModifyInventory.openInventory();
                    }
                    break;
                case ARROW:
                    player.closeInventory();
                    break;
                case GREEN_SHULKER_BOX:
                    if (machine.getItemGenerator().getProduct().getType() == Material.BARRIER){
                        player.sendMessage(getMessage("Invalid Product"));
                        return;
                    }
                    if (machine.getCollector().getStorage().size() + machine.getItemGenerator().getItemsActive().size() >= machine.getCollector().maxCollectorSize){
                        player.sendMessage(getMessage("Collector Full"));
                        return;
                    }
                    machine.getItemGenerator().start();
                    player.sendMessage(getMessage("Item Generator Started"));
                    player.closeInventory();
                    break;
                case RED_SHULKER_BOX:
                    machine.getItemGenerator().stop();
                    player.sendMessage(getMessage("Item Generator Stopped"));
                    player.closeInventory();
                    break;
            }
        }else if (e.getInventory().getTitle().equals("§7§l| §7§lCollector §7- §8Main menu §7§l|")){
            e.setCancelled(true);
            if (e.getClickedInventory() instanceof PlayerInventory)return;

            Machine machine = navigationHandlerHashMap.get(player).getMachine();

            switch (clickedItem.getType()){
                case CHEST:
                    CollectorInventory collectorInventory = new CollectorInventory(player);
                    collectorInventory.inventory();
                    break;
                case ARROW:
                    player.closeInventory();
                    break;
            }
        }else if (e.getInventory().getTitle().equals("§7§l| §7§lCollector §7- §8Inventory §7§l|")){
            e.setCancelled(true);
            if (e.getClickedInventory() instanceof PlayerInventory)return;

            Matrix anvils = new Matrix(27, 34);
            if (anvils.fromInventory(e.getClickedInventory()).contains(e.getSlot()))return;

            Machine machine = navigationHandlerHashMap.get(player).getMachine();
            CollectorInventory collectorInventory = new CollectorInventory(player);

            if (e.getSlot() == 35) collectorInventory.mainmenu();
            else {
                giveItemToPlayer(player, clickedItem);
                clickedItem.setAmount(0);
                Matrix matrix = new Matrix(0, 26);
                machine.getCollector().setStorage(matrix.getInventoryItems(e.getClickedInventory()));
                collectorInventory.inventory();
            }
            if (clickedItem.getType() == Material.ARROW)
                collectorInventory.mainmenu();
        }else if (e.getInventory().getTitle().equals("§7§l| §eItem Gen. §7- §8Product Config §7§l|")){
            e.setCancelled(true);

            Machine machine = navigationHandlerHashMap.get(player).getMachine();
            ItemGeneratorInventory itemGeneratorInventory = new ItemGeneratorInventory(player);
            if (e.getClickedInventory() instanceof PlayerInventory) {
                machine.getItemGenerator().setProduct(clickedItem);
                itemGeneratorInventory.openProductConfiguration();
                return;
            }

            if (e.getSlot() == 4){
                if(e.isLeftClick() && e.isShiftClick()) {
                    machine.getItemGenerator().setProduct(null);
                    itemGeneratorInventory.openProductConfiguration();
                }else if (e.isRightClick())
                    player.getInventory().addItem(machine.getItemGenerator().getProduct());


                return;
            }
            if (clickedItem.getType() == Material.ARROW){
                itemGeneratorInventory.openItemGeneratorMenu();
            }
        }
    }
}
