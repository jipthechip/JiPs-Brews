package com.jipthechip.fermentationmod.Utils;


import com.jipthechip.fermentationmod.Particles.ParticleList;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class UtilList {

    public static float roundFloat(float number, int places){
        if(places >= 7) places = 7;
        float pow = (float)Math.pow(10, places);
        return Math.round(number * pow) / pow;
    }

    public static boolean [] byteArrayToBoolean(byte [] arr){
        int length = arr.length;
        boolean [] newArr = new boolean[length];
        for(int i = 0; i < length; i++) newArr[i] = arr[i] > 0;
        return newArr;
    }

    public static byte [] booleanArrayToByte(boolean [] arr){
        int length = arr.length;
        byte [] newArr = new byte[length];
        for(int i = 0; i < length; i++) newArr[i] = arr[i] ? (byte)1 : (byte)0;
        return newArr;
    }

    public static int [] floatArrayToInt(float [] arr){
        int length = arr.length;
        int [] newArr = new int[length];
        for(int i = 0; i < length; i++) newArr[i] = Float.floatToIntBits(arr[i]);
        return newArr;
    }
    public static float [] intArrayToFloat(int [] arr){
        int length = arr.length;
        float [] newArr = new float[length];
        for(int i = 0; i < length; i++) newArr[i] = Float.intBitsToFloat(arr[i]);
        return newArr;
    }

    public static long [] doubleArrayToLong(double [] arr){
        int length = arr.length;
        long [] newArr = new long[length];
        for(int i = 0; i < length; i++) newArr[i] = Double.doubleToLongBits(arr[i]);
        return newArr;
    }

    public static double [] longArrayToDouble(long [] arr){
        int length = arr.length;
        double [] newArr = new double[length];
        for(int i = 0; i < length; i++) newArr[i] = Double.longBitsToDouble(arr[i]);
        return newArr;
    }

    public static Vec3d doubleArrayToVec3d(double [] arr){
        if(arr.length < 3) return null;
        return new Vec3d(arr[0], arr[1], arr[2]);
    }

    public static double[] vec3dToDoubleArray(Vec3d vec3d){
        return new double[]{vec3d.getX(), vec3d.getY(), vec3d.getZ()};
    }

    public static Identifier identifierFromString(String str){
        String[] arr = str.split(":");
        return new Identifier(arr[0], arr[1]);
    }

    public static void sendParticlePacket(World world, BlockPos pos, double particleX, double particleY, double particleZ, Identifier identifier){
        if(world.isClient()) return;
        Stream<PlayerEntity> watchingPlayers = PlayerStream.watching(world,pos);


        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeBlockPos(pos);
        passedData.writeDouble(particleX);
        passedData.writeDouble(particleY);
        passedData.writeDouble(particleZ);
        passedData.writeIdentifier(identifier);

        watchingPlayers.forEach(player ->
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ParticleList.PLAY_BLOCK_PARTICLE_PACKET_ID, passedData));
    }
}
