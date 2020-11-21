package com.jipthechip.fermentationmod.Utils;


public class UtilsList {

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
}
