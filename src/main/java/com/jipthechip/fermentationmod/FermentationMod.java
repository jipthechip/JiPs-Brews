package com.jipthechip.fermentationmod;

import com.jipthechip.fermentationmod.Blocks.BlockColorList;
import com.jipthechip.fermentationmod.Blocks.BlockList;
import com.jipthechip.fermentationmod.Entities.BlockEntitiesList;
import com.jipthechip.fermentationmod.Events.EventList;
import com.jipthechip.fermentationmod.Items.ItemList;
import com.jipthechip.fermentationmod.Models.FermentableMap;
import net.fabricmc.api.ModInitializer;

public class FermentationMod implements ModInitializer {

    @Override
    public void onInitialize() {
        ItemList.registerItems();
        BlockList.registerBlocks();
        BlockEntitiesList.registerBlockEntities();
        EventList.registerEvents();
        FermentableMap.initializeFermentableMap();
    }
}
