package com.jipthechip.fermentationmod.Sounds;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class SoundList {

    public static final Identifier CORK_OPEN_ID = new Identifier("fermentationmod", "cork_open");
    public static final SoundEvent CORK_OPEN_EVENT = new SoundEvent(CORK_OPEN_ID);

    public static final Identifier CORK_CLOSE_ID = new Identifier("fermentationmod", "cork_close");
    public static final SoundEvent CORK_CLOSE_EVENT = new SoundEvent(CORK_CLOSE_ID);

    public static final Identifier ADD_TO_MASHER_ID = new Identifier("fermentationmod", "add_to_masher");
    public static final SoundEvent ADD_TO_MASHER_EVENT = new SoundEvent(ADD_TO_MASHER_ID);

    public static void registerSounds(){
        Registry.register(Registry.SOUND_EVENT, CORK_OPEN_ID, CORK_OPEN_EVENT);
        Registry.register(Registry.SOUND_EVENT, CORK_CLOSE_ID, CORK_CLOSE_EVENT);
        Registry.register(Registry.SOUND_EVENT, ADD_TO_MASHER_ID, ADD_TO_MASHER_EVENT);
    }
}
