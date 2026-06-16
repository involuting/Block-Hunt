package me.involuting.blockhunt.game.disguise.type;

import org.bukkit.Material;

public enum BlockType {

    // Nature
    GRASS_BLOCK(Material.GRASS_BLOCK),
    DIRT(Material.DIRT),
    COARSE_DIRT(Material.COARSE_DIRT),
    PODZOL(Material.PODZOL),
    MOSS_BLOCK(Material.MOSS_BLOCK),

    // Wood
    OAK_LOG(Material.OAK_LOG),
    OAK_PLANKS(Material.OAK_PLANKS),
    SPRUCE_LOG(Material.SPRUCE_LOG),
    SPRUCE_PLANKS(Material.SPRUCE_PLANKS),

    // Farm
    HAY_BLOCK(Material.HAY_BLOCK),
    MELON(Material.MELON),
    PUMPKIN(Material.PUMPKIN),

    // Village
    BARREL(Material.BARREL),
    CRAFTING_TABLE(Material.CRAFTING_TABLE),
    FLETCHING_TABLE(Material.FLETCHING_TABLE),
    CARTOGRAPHY_TABLE(Material.CARTOGRAPHY_TABLE),
    LECTERN(Material.LECTERN),

    // Storage
    CHEST(Material.CHEST),
    BOOKSHELF(Material.BOOKSHELF),

    // Stone
    STONE(Material.STONE),
    COBBLESTONE(Material.COBBLESTONE),
    STONE_BRICKS(Material.STONE_BRICKS),
    MOSSY_STONE_BRICKS(Material.MOSSY_STONE_BRICKS),

    // Decoration
    BRICKS(Material.BRICKS),
    TNT(Material.TNT),
    CAKE(Material.CAKE),

    // Nether
    NETHERRACK(Material.NETHERRACK),
    SOUL_SAND(Material.SOUL_SAND),

    // Misc
    FURNACE(Material.FURNACE),
    ANVIL(Material.ANVIL),
    NOTE_BLOCK(Material.NOTE_BLOCK);

    private final Material material;

    BlockType(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}