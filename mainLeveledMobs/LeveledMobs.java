package mainLeveledMobs;

import com.sucy.skill.api.PlayerSkills;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.JOCKEY;
import static org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class LeveledMobs extends JavaPlugin
  implements Listener
{
  PlayerSkills player;
  int expGain;
  int expFinal;
  double expMultiplier;
  Boolean elites;
  Boolean giants;
  Boolean Sarmor;
  Boolean Zarmor;
  Boolean dropLegendarys;
  Boolean deathmessage;
  Boolean eliteDrops;
  Boolean constantVisibility;
  Boolean worldGuard = Boolean.valueOf(false);
  Boolean hasSpawned = Boolean.valueOf(false);
  double area;
  double multiplier;
  double eliteSpawnChance;
  BukkitScheduler s;
  BukkitScheduler m;
  ArrayList<String> worlds = new ArrayList();
  ArrayList<String> mobsThatDropLegendarys = new ArrayList();
  ArrayList<ItemStack> legendaryItemList = new ArrayList();
  ArrayList<ItemStack> lequipment = new ArrayList();
  ArrayList<ItemStack> iequipment = new ArrayList();
  ArrayList<ItemStack> gequipment = new ArrayList();
  ArrayList<ItemStack> dequipment = new ArrayList();
  ArrayList<String> eliteMobs = new ArrayList();

  ItemStack lchest = new ItemStack(Material.LEATHER_CHESTPLATE);
  ItemStack lboots = new ItemStack(Material.LEATHER_BOOTS);
  ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET);
  ItemStack llegs = new ItemStack(Material.LEATHER_LEGGINGS);

  ItemStack ichest = new ItemStack(Material.IRON_CHESTPLATE);
  ItemStack iboots = new ItemStack(Material.IRON_BOOTS);
  ItemStack ihelmet = new ItemStack(Material.IRON_HELMET);
  ItemStack ilegs = new ItemStack(Material.IRON_LEGGINGS);

  ItemStack gchest = new ItemStack(Material.GOLD_CHESTPLATE);
  ItemStack gboots = new ItemStack(Material.GOLD_BOOTS);
  ItemStack ghelmet = new ItemStack(Material.GOLD_HELMET);
  ItemStack glegs = new ItemStack(Material.GOLD_LEGGINGS);

  ItemStack dchest = new ItemStack(Material.DIAMOND_CHESTPLATE);
  ItemStack dboots = new ItemStack(Material.DIAMOND_BOOTS);
  ItemStack dhelmet = new ItemStack(Material.DIAMOND_HELMET);
  ItemStack dlegs = new ItemStack(Material.DIAMOND_LEGGINGS);

  public void onEnable()
  {
    Metrics.Graph serversThatUseLeveledMobs;
    try
    {
      Metrics metrics = new Metrics(this);

      serversThatUseLeveledMobs = metrics.createGraph("Servers and their player count");
      Metrics.Graph serversThatUseLeveledMobsIp = metrics.createGraph("Servers  ips and their player count");

      serversThatUseLeveledMobsIp.addPlotter(new Metrics.Plotter(String.valueOf(getIp()))
      {
        public int getValue()
        {
          return Bukkit.getServer().getOnlinePlayers().length;
        }
      });
      serversThatUseLeveledMobs.addPlotter(new Metrics.Plotter(Bukkit.getMotd())
      {
        public int getValue()
        {
          return Bukkit.getServer().getOnlinePlayers().length;
        }
      });
      metrics.start();
    }
    catch (IOException localIOException)
    {
    }

    for (String world : getConfig().getStringList("generalSettings.worlds"))
    {
      this.worlds.add(world);
    }

    this.Zarmor = Boolean.valueOf(getConfig().getBoolean("zombie.armor.enabled"));
    this.Sarmor = Boolean.valueOf(getConfig().getBoolean("skeleton.armor.enabled"));
    this.giants = Boolean.valueOf(getConfig().getBoolean("zombie.giants.enabled"));
    this.dropLegendarys = Boolean.valueOf(getConfig().getBoolean("legendaryItems.enabled"));
    this.elites = Boolean.valueOf(getConfig().getBoolean("eliteMobs.enabled"));
    this.deathmessage = Boolean.valueOf(getConfig().getBoolean("deathMessage"));
    this.eliteDrops = Boolean.valueOf(getConfig().getBoolean("legendaryItems.elites.enabled"));
    this.constantVisibility = Boolean.valueOf(getConfig().getBoolean("constantVisibility"));
    this.hasSpawned = Boolean.valueOf(false);

    for (String world : getConfig().getStringList("generalSettings.worlds"))
    {
      this.worlds.add(world);
    }

    this.s = Bukkit.getServer().getScheduler();

    this.multiplier = getConfig().getDouble("multiplier");
    this.area = getConfig().getDouble("distance");
    getServer().getPluginManager().registerEvents(this, this);

    this.lequipment.add(this.lchest);
    this.lequipment.add(this.lboots);
    this.lequipment.add(this.lhelmet);
    this.lequipment.add(this.llegs);

    this.iequipment.add(this.ichest);
    this.iequipment.add(this.iboots);
    this.iequipment.add(this.ihelmet);
    this.iequipment.add(this.ilegs);

    this.gequipment.add(this.gchest);
    this.gequipment.add(this.gboots);
    this.gequipment.add(this.ghelmet);
    this.gequipment.add(this.glegs);

    this.dequipment.add(this.dchest);
    this.dequipment.add(this.dboots);
    this.dequipment.add(this.dhelmet);
    this.dequipment.add(this.dlegs);

    for (String mobsThatDropLegendarysString : getConfig().getStringList("legendaryItems.normalMobs.mobsThatDropLegendarys"))
    {
      this.mobsThatDropLegendarys.add(mobsThatDropLegendarysString);
    }
    for (String eliteMob : getConfig().getStringList("eliteMobs.includedMobs"))
    {
      this.eliteMobs.add(eliteMob);
    }

    this.s.scheduleSyncRepeatingTask(this, new Runnable()
    {
      public void run()
      {
        if (LeveledMobs.this.getConfig().getBoolean("constantVisibility"))
        {
          for (World w : Bukkit.getServer().getWorlds())
          {
            String World = w.getName().replace("CraftWorld{name=", "");
            World.replace("}", "");
            if (LeveledMobs.this.worlds.contains(World))
            {
              for (Entity ent : w.getEntities())
              {
                if ((ent instanceof LivingEntity))
                {
                  if (((LivingEntity)ent).getCustomName() != null)
                  {
                    int x = 0;
                    for (Entity entity : ent.getNearbyEntities(LeveledMobs.this.getConfig().getDouble("visibilityDistance"), LeveledMobs.this.getConfig().getDouble("visibilityDistance"), LeveledMobs.this.getConfig().getDouble("visibilityDistance")))
                    {
                      if ((entity instanceof Player))
                      {
                        x = 1;
                      }
                    }
                    if (x == 1)
                    {
                      ((LivingEntity)ent).setCustomNameVisible(true);
                      x = 0;
                    }
                    else
                    {
                      ((LivingEntity)ent).setCustomNameVisible(false);
                      x = 0;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    , 0L, 10L);
  }
  
//  @EventHandler(priority=EventPriority.HIGH)
//  public void onExpGain (PlayerExperienceGainEvent event)
//  {
//      if (!event.isCommandExp())
//      {
//          this.player = event.getPlayerData();
//          this.expGain = event.getExp();
//          event.setExp(expFinal);
//      }
//  }
//  
//  @EventHandler(priority=EventPriority.HIGH)
//  public void onKill (EntityDeathEvent event)
//  {
//      if (event.getEntity().getKiller() != null)
//      {
//          if (event.getEntity().getCustomName().indexOf("Lvl") != -1)
//          {
//              String name = event.getEntity().getCustomName();
//              int lvlStart = name.indexOf(": ", 0)+2;
//              int lvlEnd = name.indexOf("]", 0);
//              String temp = name.substring(lvlStart, lvlEnd);
//
//              final int lvl = Integer.parseInt(temp);
//              this.expMultiplier = getConfig().getDouble("expMultiplier");
//              this.expFinal = (int)(this.expGain + this.expGain * lvl * this.expMultiplier);
//          }
//      }
//  }

  @EventHandler
  public void EntityTameEvent(EntityTameEvent e)
  {
    e.getEntity().setCustomName(e.getOwner().getName() + "`s " + e.getEntityType().toString().toLowerCase());
  }

  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
  {
    if (command.getLabel().equalsIgnoreCase("lm"))
    {
      if (args.length > 3)
      {
        if ((sender instanceof Player))
        {
          Player p = (Player)sender;
          p.sendMessage(ChatColor.DARK_RED + "[LeveledMobs]" + ChatColor.GREEN + " Too many arguments, use /lm refreshmobs or /lm setup!");
        }
        else
        {
          ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
          console.sendMessage(ChatColor.DARK_RED + "[LeveledMobs]" + ChatColor.GREEN + " Too few arguments, use /lm refreshmobs or /lm setup!");
        }
      }
      else if (args.length == 0)
      {
        if ((sender instanceof Player))
        {
          Player p = (Player)sender;
          p.sendMessage(ChatColor.DARK_RED + "[LeveledMobs]" + ChatColor.GREEN + " Too many arguments, use /lm refreshmobs or /lm setup!");
        }
        else
        {
          ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
          console.sendMessage(ChatColor.DARK_RED + "[LeveledMobs]" + ChatColor.GREEN + " Too few arguments, use /lm refreshmobs or /lm setup!");
        }
      }
      else if ((args[0].equalsIgnoreCase("refreshmobs")) && (sender.hasPermission("LeveledMobs.refreshmobs")))
      {
        for (World world : Bukkit.getWorlds())
        {
          for (Entity ent : world.getEntities())
          {
          if ((ent instanceof LivingEntity))
          {
            String entWorld = ent.getWorld().getName().replace("CraftWorld{name=", "");
            entWorld.replace("}", "");

            String entityTypeName = ent.getClass().getName().toString().substring(ent.getClass().getName().toString().indexOf(".", 31) + 6, ent.getClass().getName().toString().length()).toLowerCase();
            if (!getConfig().getStringList("generalSettings.worldLocations." + entWorld + ".exemptedMobs").contains(entityTypeName))
            {
              if (!(ent instanceof Player))
              {
                if ((ent instanceof Horse))
                {
                  if (((Horse)ent).getOwner() == null)
                  {
                    ent.remove();
                  }

                }
                else if ((ent instanceof Wolf))
                {
                  if (((Wolf)ent).getOwner() == null)
                  {
                    ent.remove();
                  }

                }
                else if ((ent instanceof Ocelot))
                {
                  if (((Ocelot)ent).getOwner() == null)
                  {
                    ent.remove();
                  }

                }
                else
                {
                  ent.remove();
                }
              }
             }
            }
        }
        }
        if ((sender instanceof Player))
        {
          Player p = (Player)sender;
          p.sendMessage(ChatColor.DARK_RED + "[LeveledMobs]" + ChatColor.GREEN + " All mobs have been reset!");
        }
        else
        {
          ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
          console.sendMessage(ChatColor.DARK_RED + "[LeveledMobs]" + ChatColor.GREEN + " All mobs have been reset!");
        }
       
      }
      else if ((args[0].equalsIgnoreCase("setup")) && (sender.hasPermission("LeveledMobs.setup")))
      {
        saveDefaultConfig();
        if ((sender instanceof Player))
        {
          Player p = (Player)sender;
          p.sendMessage(ChatColor.DARK_RED + "[LeveledMobs]" + ChatColor.GREEN + " LeveledMobs has been Setup!");
        }
        else
        {
          ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
          console.sendMessage(ChatColor.DARK_RED + "[LeveledMobs]" + ChatColor.GREEN + " LeveledMobs has been Setup!");
        }
      }
      else if ((args[0].equalsIgnoreCase("distance")) && (sender.hasPermission("LeveldMobs.distance")))
      {
          if ((sender instanceof Player))
          {
              Player p = (Player)sender;
              String pWorld = p.getWorld().getName().replace("CraftWorld{name=", "");
              Location cregion = getNearestLocation(p, this.worlds, pWorld);
              double dist = cregion.distance(p.getLocation());
              p.sendMessage(ChatColor.DARK_RED + "[LeveledMobs]" + ChatColor.GREEN + "The nearest spawn is " + dist + " blocks away.");
          }
      }
    }
    return false;
  }

  @EventHandler(priority=EventPriority.MONITOR)
  public void EntitySpawnEvent(CreatureSpawnEvent e)
  {
      
    Horse h = null;
    Wolf w = null;
    Ocelot o = null;

    LivingEntity ent = e.getEntity();

    Random r = new Random();

    if ((ent instanceof Horse))
    {
      h = (Horse)ent;
      if (h.getOwner() == null)
      {
        h = null;
      }
      else
      {
        e.getEntity().setCustomName(h.getOwner().getName() + "`s " + e.getEntityType().toString().toLowerCase());
      }
    }
    else if ((ent instanceof Wolf))
    {
      w = (Wolf)ent;
      if (w.getOwner() == null)
      {
        w = null;
      }
      else
      {
        e.getEntity().setCustomName(w.getOwner().getName() + "`s " + e.getEntityType().toString().toLowerCase());
      }
    }
    else if ((ent instanceof Ocelot))
    {
      o = (Ocelot)ent;
      if (o.getOwner() == null)
      {
        o = null;
      }
      else
      {
        e.getEntity().setCustomName(o.getOwner().getName() + "`s " + e.getEntityType().toString().toLowerCase());
      }

    }

    String entityTypeName = ent.getClass().getName().toString().substring(ent.getClass().getName().toString().indexOf(".", 31) + 6, ent.getClass().getName().toString().length()).toLowerCase();
    String entWorld = ent.getWorld().getName().replace("CraftWorld{name=", "");
    entWorld.replace("}", "");

    if (this.worlds.contains(entWorld))
    {
      if (!(ent instanceof Player))
      {
        if (!getConfig().getStringList("generalSettings.worldLocations." + entWorld + ".exemptedMobs").contains(entityTypeName))
        {
          if (h == null)
          {
            if (w == null)
            {
              if (o == null)
              {
                if (!ent.hasMetadata("Npc"))
                {
                  Location cregion = getNearestLocation(ent, e, this.worlds, entWorld);

                  double dist = cregion.distance(ent.getLocation());
                  double trueDistance = Math.ceil(dist / this.area);
                  
                  if (e.getSpawnReason() == SPAWNER_EGG) {
                      return;
                  }
                  
                  if (e.getSpawnReason() == JOCKEY) {
                      return;
                  }

                  if ((ent instanceof Zombie))
                  {
                    Zombie z = (Zombie)ent;

                    if (this.giants.booleanValue())
                    {
                      if (trueDistance > getConfig().getInt("zombie.giants.level"))
                      {
                        int chance = Integer.valueOf(getConfig().getString("zombie.giants.chance").replace("%", "")).intValue();

                        if (r.nextInt(100) <= chance)
                        {
                          ent = (LivingEntity)ent.getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.GIANT);
                        }
                      }
                    }
                    if (this.Zarmor.booleanValue())
                    {
                      if ((ent instanceof Zombie))
                      {
                        double armorLevel = getConfig().getDouble("zombie.armor.level");
                        armorLevel = Math.ceil(trueDistance / armorLevel);
                        int spawnChance = Integer.valueOf(getConfig().getString("zombie.armor.chance").replace("%", "")).intValue();

                        if (r.nextInt(100) <= spawnChance)
                        {
                          if (armorLevel != 1.0D)
                          {
                            if (armorLevel == 2.0D)
                            {
                              ItemStack[] equipment = (ItemStack[])this.lequipment.toArray(new ItemStack[this.lequipment.size()]);
                              z.getEquipment().setArmorContents(equipment);
                              z.getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));
                            }
                            else if (armorLevel == 3.0D)
                            {
                              ItemStack[] equipment = (ItemStack[])this.iequipment.toArray(new ItemStack[this.iequipment.size()]);
                              z.getEquipment().setArmorContents(equipment);
                              z.getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
                            }
                            else if (armorLevel == 4.0D)
                            {
                              ItemStack[] equipment = (ItemStack[])this.gequipment.toArray(new ItemStack[this.gequipment.size()]);
                              z.getEquipment().setArmorContents(equipment);
                              z.getEquipment().setItemInHand(new ItemStack(Material.GOLD_SWORD));
                            }
                            else if (armorLevel == 5.0D)
                            {
                              ItemStack[] equipment = (ItemStack[])this.dequipment.toArray(new ItemStack[this.dequipment.size()]);
                              z.getEquipment().setArmorContents(equipment);
                              z.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                            }
                            else if (armorLevel >= 6.0D)
                            {
                              ItemStack[] equipment = (ItemStack[])this.dequipment.toArray(new ItemStack[this.dequipment.size()]);
                              z.getEquipment().setArmorContents(equipment);
                              z.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                            }
                          }
                        }
                      }
                    }
                  } else if ((ent instanceof Skeleton))
                  {
                    Skeleton s = (Skeleton)ent;

                    if (this.Sarmor.booleanValue())
                    {
                      if ((ent instanceof Skeleton))
                      {
                        double armorLevel = getConfig().getDouble("skeleton.armor.level");
                        armorLevel = Math.ceil(trueDistance / armorLevel);
                        int spawnChance = Integer.valueOf(getConfig().getString("skeleton.armor.chance").replace("%", "")).intValue();

                        if (r.nextInt(100) <= spawnChance)
                        {
                          if (armorLevel != 1.0D)
                          {
                            if (armorLevel == 2.0D)
                            {
                              ItemStack[] equipment = (ItemStack[])this.lequipment.toArray(new ItemStack[this.lequipment.size()]);
                              s.getEquipment().setArmorContents(equipment);
                            }
                            else if (armorLevel == 3.0D)
                            {
                              ItemStack[] equipment = (ItemStack[])this.iequipment.toArray(new ItemStack[this.iequipment.size()]);
                              s.getEquipment().setArmorContents(equipment);
                            }
                            else if (armorLevel == 4.0D)
                            {
                              ItemStack[] equipment = (ItemStack[])this.gequipment.toArray(new ItemStack[this.gequipment.size()]);
                              s.getEquipment().setArmorContents(equipment);
                            }
                            else if (armorLevel == 5.0D)
                            {
                              ItemStack[] equipment = (ItemStack[])this.dequipment.toArray(new ItemStack[this.dequipment.size()]);
                              s.getEquipment().setArmorContents(equipment);
                            }
                            else if (armorLevel >= 6.0D)
                            {
                              ItemStack[] equipment = (ItemStack[])this.dequipment.toArray(new ItemStack[this.dequipment.size()]);
                              s.getEquipment().setArmorContents(equipment);
                            }
                          }
                        }
                      }
                    }
                  }

                  if ((ent instanceof Zombie))
                  {
                    Damageable mob = ent;
                    mob.setMaxHealth(Math.ceil(mob.getHealth() + mob.getHealth() * trueDistance * this.multiplier));
                    mob.setHealth(mob.getMaxHealth());
                    mob = ent;

                    if (ent.getCustomName() != null) ent.setCustomName(ChatColor.GOLD + "[Lvl: " + ChatColor.YELLOW + (int)trueDistance + ChatColor.GOLD + "] " + ChatColor.RESET + ent.getCustomName() /*+ ChatColor.RED + "[" + ChatColor.GREEN + (int)mob.getHealth() + ChatColor.DARK_RED + "❤" + ChatColor.RED + "]"*/);
                    else ent.setCustomName(ChatColor.GOLD + "[Lvl: " + ChatColor.YELLOW + (int)trueDistance + ChatColor.GOLD + "] "/*+ ChatColor.RED + "[" + ChatColor.GREEN + (int)mob.getHealth() + ChatColor.DARK_RED + "❤" + ChatColor.RED + "]"*/);    
                    if (getConfig().getBoolean("constantVisibility")) ent.setCustomNameVisible(true);
                    else if (getConfig().getBoolean("constantVisibility")) ent.setCustomNameVisible(false);
                  }
                  else
                  {
                    Damageable mob = ent;
                    mob.setMaxHealth(Math.ceil(mob.getHealth() + mob.getHealth() * trueDistance * this.multiplier));
                    mob.setHealth(mob.getMaxHealth());
                    mob = ent;

                    if (ent.getCustomName() != null) ent.setCustomName(ChatColor.GOLD + "[Lvl: " + ChatColor.YELLOW + (int)trueDistance + ChatColor.GOLD + "] " + ChatColor.RESET + ent.getCustomName() /*+ ChatColor.RED + "[" + ChatColor.GREEN + (int)mob.getHealth() + ChatColor.DARK_RED + "❤" + ChatColor.RED + "]"*/);
                    else ent.setCustomName(ChatColor.GOLD + "[Lvl: " + ChatColor.YELLOW + (int)trueDistance + ChatColor.GOLD + "] "/*+ ChatColor.RED + "[" + ChatColor.GREEN + (int)mob.getHealth() + ChatColor.DARK_RED + "❤" + ChatColor.RED + "]"*/);    
                    if (getConfig().getBoolean("constantVisibility")) ent.setCustomNameVisible(true);
                    else if (getConfig().getBoolean("constantVisibility")) ent.setCustomNameVisible(false);  }  }  } 
            }
          }
        }
      }
    }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void EntityDamageEvent(EntityDamageEvent e) { if ((e.getEntity() instanceof LivingEntity))
    {
      LivingEntity ent = (LivingEntity)e.getEntity();
      String entWorld = ent.getWorld().getName().replace("CraftWorld{name=", "");
      entWorld.replace("}", "");

      if (this.worlds.contains(entWorld))
      {
        String entityTypeName = ent.getClass().getName().toString().substring(ent.getClass().getName().toString().indexOf(".", 31) + 6, ent.getClass().getName().toString().length()).toLowerCase();

        if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
        {
          EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)e;
          if (!(event.getDamager() instanceof Player))
          {
            if (!(e.getEntity() instanceof Villager))
            {
              if (!getConfig().getStringList("generalSettings.worldLocations." + entWorld + ".exemptedMobs").contains(entityTypeName))
              {
                double damageMultiplier = getConfig().getDouble("damageMultiplier");
                e.setDamage(e.getDamage() + e.getDamage() * (getCreatureLevel((LivingEntity)event.getDamager()) * damageMultiplier));
              }
            }
          }
        } else if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
        {
          EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)e;
          if (!(event.getDamager() instanceof Player))
          {
            if ((event.getDamager() instanceof Arrow))
            {
              Arrow r = (Arrow)event.getDamager();
              if ((r.getShooter() != null) && (!getConfig().getStringList("generalSettings.worldLocations." + entWorld + ".exemptedMobs").contains(entityTypeName)))
              {
                double damageMultiplier = getConfig().getDouble("damageMultiplier");
                e.setDamage(e.getDamage() + e.getDamage() * (getCreatureLevel(r.getShooter()) * damageMultiplier));
              }
            }
          }
        }

        if (!(ent instanceof Player))
        {
          if (!getConfig().getStringList("generalSettings.worldLocations." + entWorld + ".exemptedMobs").contains(entityTypeName))
          {
//            if ((ent instanceof Horse))
//            {
//              if (((Horse)ent).getOwner() == null)
//              {
//                setName(ent, e);
//              }
//            }
//            else if ((ent instanceof Wolf))
//            {
//              if (((Wolf)ent).getOwner() == null)
//              {
//                setName(ent, e);
//              }
//            }
//            else if ((ent instanceof Ocelot))
//            {
//              if (((Ocelot)ent).getOwner() == null)
//              {
//                setName(ent, e);
//              }
//            }
//            else if (!(ent instanceof Villager))
//            {
//              setName(ent, e);
//            }
          }
        }
      }
    } }

  @EventHandler
  public void PlayerDeathEvent(PlayerDeathEvent e) {
    EntityDamageEvent damageEvent = e.getEntity().getLastDamageCause();
    if (this.deathmessage.booleanValue())
    {
      if ((damageEvent instanceof EntityDamageByEntityEvent))
      {
        if ((((EntityDamageByEntityEvent)damageEvent).getDamager() instanceof Arrow))
        {
          Arrow r = (Arrow)((EntityDamageByEntityEvent)damageEvent).getDamager();
          Entity killers = r.getShooter();
          String entityTypeName = killers.getClass().getName().toString().substring(killers.getClass().getName().toString().indexOf(".", 31) + 6, killers.getClass().getName().toString().length());
          if ((killers instanceof LivingEntity))
          {
            if (getCreatureLevel((LivingEntity)killers) != 0)
            {
              e.setDeathMessage(e.getEntity().getName() + " was slain by a " + ChatColor.GOLD + "[" + ChatColor.DARK_RED + "LVL:" + ChatColor.GREEN + getCreatureLevel((LivingEntity)killers) + ChatColor.GOLD + "] " + ChatColor.RESET + entityTypeName + "!");
            }
            else
            {
              e.setDeathMessage(e.getEntity().getName() + " was slain by a " + entityTypeName);
            }
          }
        }
        else if ((((EntityDamageByEntityEvent)damageEvent).getDamager() instanceof LivingEntity))
        {
          LivingEntity killer = (LivingEntity)((EntityDamageByEntityEvent)damageEvent).getDamager();

          if (!(killer instanceof Player))
          {
            if ((killer instanceof Creature))
            {
              String entityTypeName = killer.getClass().getName().toString().substring(killer.getClass().getName().toString().indexOf(".", 31) + 6, killer.getClass().getName().toString().length());
              if (getCreatureLevel(killer) != 0)
              {
                e.setDeathMessage(e.getEntity().getName() + " was slain by a " + ChatColor.GOLD + "[" + ChatColor.DARK_RED + "LVL:" + ChatColor.GREEN + getCreatureLevel(killer) + ChatColor.GOLD + "] " + ChatColor.RESET + entityTypeName + "!");
              }
              else
              {
                e.setDeathMessage(e.getEntity().getName() + " was slain by a " + entityTypeName);
              }
            }
          }
        }
      }
    }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void EntityDeathEvent(EntityDeathEvent e)
  {
    Entity ent = e.getEntity();
    if ((ent instanceof LivingEntity))
    {
      String entWorld = ent.getWorld().getName().replace("CraftWorld{name=", "");
      entWorld.replace("}", "");
      String entityTypeName = ent.getClass().getName().toString().substring(ent.getClass().getName().toString().indexOf(".", 31) + 6, ent.getClass().getName().toString().length()).toLowerCase();
      if (!(ent instanceof Player))
      {
        if (!getConfig().getStringList("generalSettings.worldLocations." + entWorld + ".exemptedMobs").contains(entityTypeName))
        {
          if (this.dropLegendarys.booleanValue())
          {
            if (this.eliteMobs.contains(ent.getUniqueId()))
            {
              if (this.eliteDrops.booleanValue())
              {
                Random r = new Random();
                for (String legendaryItem : getConfig().getStringList("legendaryItems.legendaryNames"))
                {
                  this.legendaryItemList.add(createLegendary(legendaryItem));
                }
                if (this.legendaryItemList.size() >= 1)
                {
                  e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), (ItemStack)this.legendaryItemList.get(r.nextInt(this.legendaryItemList.size())));
                }
              }
              this.eliteMobs.remove(ent.getUniqueId());
            }
            else if (this.mobsThatDropLegendarys.contains(entityTypeName))
            {
              double creatureLevel = getCreatureLevel(e.getEntity());
              int chance = Integer.valueOf(getConfig().getString("legendaryItems.normalMobs.dropChance").replace("%", "")).intValue();
              Random r = new Random();
              if (r.nextInt(100) <= chance)
              {
                for (String legendaryItem : getConfig().getStringList("legendaryItems.legendaryNames"))
                {
                  if (getConfig().getInt("legendaryItems." + legendaryItem + ".dropLevel") <= creatureLevel)
                  {
                    this.legendaryItemList.add(createLegendary(legendaryItem));
                  }
                }
                if (this.legendaryItemList.size() >= 1)
                {
                  e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), (ItemStack)this.legendaryItemList.get(r.nextInt(this.legendaryItemList.size())));
                }
              }
            }
          }
        }
      }
    }
  }

  public void PlayerJoinEvent(PlayerJoinEvent e)
  {
  }

//  public void setName(final LivingEntity ent, final EntityDamageEvent e)
//  {
//    Damageable mob = ent;
//    if (mob.getHealth() > 0.0D)
//    {
//            
//      if (ent.getCustomName().indexOf("]") != -1) {                    
//            {
//              String name = ChatColor.stripColor(ent.getCustomName());
//              int lvlStart = name.indexOf(": ", 0)+2;
//              int lvlEnd = name.indexOf("]", 0);
//              String temp = name.substring(lvlStart, lvlEnd);
//
//              final int lvl = Integer.parseInt(temp);
//              this.s.scheduleSyncDelayedTask(this, new Runnable()
//              {
//                public void run()
//                {
//                  Damageable tempmob = (Damageable)e.getEntity();
//                  ent.setCustomName(ChatColor.GOLD + "[Lvl: " + ChatColor.YELLOW + (int)lvl + ChatColor.GOLD + "] " + ent.getCustomName());
//                }
//              }
//              , 0L);
//            }
//      }
//      else
//      {
//        double dist = ent.getWorld().getSpawnLocation().distance(ent.getLocation());
//        final double trueDistance = Math.ceil(dist / this.area);
//        this.s.scheduleSyncDelayedTask(this, new Runnable()
//        {
//          public void run()
//          {
//            Damageable tempmob = (Damageable)e.getEntity();
//            ent.setCustomName(ChatColor.GOLD + "[Lvl: " + ChatColor.YELLOW + (int)trueDistance + ChatColor.GOLD + "] " + ent.getCustomName());
//          }
//        }
//        , 0L);
//      }
//    }
//  }

  public Location getNearestLocation(Player p, ArrayList<String> worlds, String worldName)
  {
    Location primaryLoc = p.getLocation();
    Location tempLoc = p.getLocation();

    ArrayList centralSpawns = new ArrayList();

    for (String locations : getConfig().getStringList("generalSettings.worldLocations." + worldName + ".centralSpawns"))
    {
      centralSpawns.add(locations);
    }

    primaryLoc.setWorld(p.getWorld());
    primaryLoc.setX(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations.spawn1.x"));
    primaryLoc.setY(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations.spawn1.y"));
    primaryLoc.setZ(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations.spawn1.z"));

    if (centralSpawns.size() > 1)
    {
      for (Object spawn : centralSpawns)
      {
        tempLoc.setX(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations." + spawn + ".x"));
        tempLoc.setY(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations." + spawn + ".y"));
        tempLoc.setZ(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations." + spawn + ".z"));

        if (tempLoc.distance(p.getLocation()) < primaryLoc.distance(p.getLocation()))
        {
          primaryLoc = tempLoc;
        }
      }
    }
    return primaryLoc;
  }
  
  public Location getNearestLocation(LivingEntity ent, CreatureSpawnEvent e, ArrayList<String> worlds, String worldName)
  {
    Location primaryLoc = ent.getLocation();
    Location tempLoc = ent.getLocation();

    ArrayList centralSpawns = new ArrayList();

    for (String locations : getConfig().getStringList("generalSettings.worldLocations." + worldName + ".centralSpawns"))
    {
      centralSpawns.add(locations);
    }

    primaryLoc.setWorld(ent.getWorld());
    primaryLoc.setX(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations.spawn1.x"));
    primaryLoc.setY(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations.spawn1.y"));
    primaryLoc.setZ(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations.spawn1.z"));

    if (centralSpawns.size() > 1)
    {
      for (Object spawn : centralSpawns)
      {
        tempLoc.setX(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations." + spawn + ".x"));
        tempLoc.setY(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations." + spawn + ".y"));
        tempLoc.setZ(getConfig().getDouble("generalSettings.worldLocations." + worldName + ".spawnLocations." + spawn + ".z"));

        if (tempLoc.distance(ent.getLocation()) < primaryLoc.distance(ent.getLocation()))
        {
          primaryLoc = tempLoc;
        }
      }
    }
    return primaryLoc;
  }

  public int getCreatureLevel(LivingEntity ent)
  {
    if (ent.getCustomName() != null)
    {
      String creatureName = ChatColor.stripColor(ent.getCustomName());
      int startLvl = creatureName.indexOf(": ")+2;
      int endLvl = creatureName.indexOf("]");
      
      int lvl = Integer.parseInt(creatureName.substring(startLvl, endLvl));
      return lvl;
    }
    return 0;
  }

  public ItemStack createLegendary(String itemName)
  {
    ArrayList lore = new ArrayList();
    ItemStack legendary = new ItemStack(Material.getMaterial(getConfig().getString("legendaryItems." + itemName + ".material")));
    ItemMeta legendaryMeta = legendary.getItemMeta();
    legendaryMeta.setDisplayName(getConfig().getString("legendaryItems." + itemName + ".name"));
    for (String loretobeadded : getConfig().getStringList("legendaryItems." + itemName + ".lore"))
    {
      lore.add(loretobeadded);
    }
    legendaryMeta.setLore(lore);
    legendary.setItemMeta(legendaryMeta);
    for (String enchantment : getConfig().getStringList("legendaryItems." + itemName + ".enchantments"))
    {
      int enchantLevel = ((Integer)getConfig().getIntegerList("legendaryItems." + itemName + ".enchantmentLevels").get(getConfig().getStringList("legendaryItems." + itemName + ".enchantments").indexOf(enchantment))).intValue();
      legendary.addUnsafeEnchantment(Enchantment.getByName(enchantment), enchantLevel);
    }
    return legendary;
  }

  public String getIp()
  {
    String ip = null;
    try {
      URLConnection connection = new URL(
        "http://api.externalip.net/ip")
        .openConnection();
      Scanner scanner = new Scanner(connection.getInputStream());
      while (scanner.hasNext()) {
        ip = ip + scanner.next() + " ";
      }
      scanner.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ip.replace("null", "");
  }
}