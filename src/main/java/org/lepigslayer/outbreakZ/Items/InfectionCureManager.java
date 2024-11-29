package org.lepigslayer.outbreakZ.Items;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.lepigslayer.outbreakZ.Infection.InfectionState;
import org.lepigslayer.outbreakZ.Infection.InfectionSystem;
import org.lepigslayer.outbreakZ.OutbreakZ;

public class InfectionCureManager implements Listener {
    private static final String INFECTION_CURE_NAME = "§3Infection Cure";

    public InfectionCureManager(OutbreakZ plugin) {
        NamespacedKey key = new NamespacedKey(plugin,"infection_cure");
        ShapedRecipe recipe = new ShapedRecipe(key,getNewCure());
        recipe.shape(" E ","BAB","EEE");
        recipe.setIngredient('E', Material.DIAMOND_BLOCK);
        recipe.setIngredient('B', Material.NETHERITE_SCRAP);
        recipe.setIngredient('A', Material.GLASS_BOTTLE);
        Bukkit.addRecipe(recipe);
    }

    public ItemStack getNewCure() {
        ItemStack i = new ItemStack(Material.POTION,1);
        PotionMeta meta = ((PotionMeta) i.getItemMeta());
        meta.setColor(Color.AQUA);
        meta.setDisplayName(INFECTION_CURE_NAME);
        i.setItemMeta(meta);
        return i;
    }

    private boolean checkMatch(ItemStack i){
        ItemMeta meta = i.getItemMeta();
        if(!meta.hasDisplayName()) return false;
        return meta.getDisplayName().equals(INFECTION_CURE_NAME);
    }

    @EventHandler
    private void anvilUse(PrepareAnvilEvent e){
        if(checkMatch(e.getInventory().getItem(0))){
            e.setResult(null);
        }
    }

    @EventHandler
    private void drinkCure(PlayerItemConsumeEvent e){
        if(!checkMatch(e.getItem())) return;
        if(!OutbreakZ.getSession().isOngoing()){
            e.setCancelled(true);
            return;
        }

        if(InfectionSystem.isInfected(e.getPlayer())){
            e.getPlayer().sendMessage("§c§lThe cure had no effect on you");
            return;
        }

        e.getPlayer().sendMessage("§3§lYou consume the infection cure. Its soothing");
        InfectionSystem.applyCure(e.getPlayer());
    }
}
