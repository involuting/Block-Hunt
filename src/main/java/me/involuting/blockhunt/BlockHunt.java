package me.involuting.blockhunt;

import lombok.Getter;
import me.involuting.blockhunt.command.BlockHuntCommand;
import me.involuting.blockhunt.config.ArenaFile;
import me.involuting.blockhunt.game.disguise.manager.DisguiseManager;
import me.involuting.blockhunt.game.disguise.render.DisguiseRenderer;
import me.involuting.blockhunt.game.disguise.task.SolidifyTask;
import me.involuting.blockhunt.game.npc.listener.NPCListener;
import me.involuting.blockhunt.game.npc.manager.NPCManager;
import me.involuting.blockhunt.game.win.WinCondition;
import me.involuting.blockhunt.listeners.combat.CombatListener;
import me.involuting.blockhunt.listeners.damage.DamageListener;
import me.involuting.blockhunt.listeners.movement.MovementListener;
import me.involuting.blockhunt.listeners.player.InteractListener;
import me.involuting.blockhunt.listeners.player.PlayerListener;
import me.involuting.blockhunt.game.arena.manager.ArenaManager;
import me.involuting.blockhunt.game.manager.GameManager;
import me.involuting.blockhunt.game.player.manager.PlayerManager;
import net.j4c0b3y.api.menu.MenuHandler;
import org.bukkit.plugin.java.JavaPlugin;
@Getter
public final class BlockHunt extends JavaPlugin {
   @Getter
   private static BlockHunt instance;
    private ArenaManager arenaManager;
    private PlayerManager playerManager;
    private GameManager gameManager;
    private DisguiseManager disguiseManager;
    private DisguiseRenderer disguiseRenderer;
    private NPCManager npcManager;
    private ArenaFile arenaFile;
    private MenuHandler menuHandler;
    private WinCondition winCondition;

    public static void setInstance(BlockHunt instance) {
        BlockHunt.instance = instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        registerManagers();
        registerListeners();
        registerTasks();
        registerCommands();

        this.menuHandler = new MenuHandler(this);




    }


    private void registerManagers() {

        this.arenaFile = new ArenaFile(this);

        this.playerManager = new PlayerManager();

        this.disguiseRenderer = new DisguiseRenderer();

        this.disguiseManager = new DisguiseManager(
                playerManager,
                disguiseRenderer
        );

        this.arenaManager = new ArenaManager(arenaFile);

        this.gameManager = new GameManager(
                arenaManager, playerManager, disguiseManager
        );

        this.npcManager = new NPCManager(this, arenaManager);

        arenaManager.loadArenas();
    }



    @Override
    public void onDisable() {

        if (disguiseRenderer != null) {
            disguiseRenderer.removeAll();
        }

        if (arenaManager != null) {
            arenaManager.saveArenas();
        }
    }

    private void registerListeners() {

        getServer().getPluginManager().registerEvents(
                new PlayerListener(playerManager),
                this
        );

        getServer().getPluginManager().registerEvents(
                new MovementListener(
                        playerManager,
                        disguiseManager, arenaManager
                ),
                this
        );

        getServer().getPluginManager().registerEvents(
                new InteractListener(
                        playerManager, disguiseManager
                ),
                this
        );

        getServer().getPluginManager().registerEvents(
                new DamageListener(
                        playerManager
                ),
                this
        );

        getServer().getPluginManager().registerEvents(
                new NPCListener(
                        npcManager,
                        arenaManager, gameManager
                ),
                this
        );

        getServer().getPluginManager().registerEvents(
                new CombatListener(
                        playerManager,
                        arenaManager,disguiseManager, winCondition

                ),
                this
        );
    }

    private void registerTasks() {

        new SolidifyTask(
                playerManager,
                disguiseManager
        ).runTaskTimer(this, 20L, 20L);

    }

    private void registerCommands() {

        getCommand("blockhunt").setExecutor(
                new BlockHuntCommand(
                        arenaManager,
                        gameManager, playerManager, disguiseManager, npcManager
                )
        );
    }

    public static BlockHunt getInstance() {
        return instance;
    }
}