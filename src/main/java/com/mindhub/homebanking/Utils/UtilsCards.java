package com.mindhub.homebanking.Utils;

public final class UtilsCards {
    public static int randomNumberC(){
       return (int) (Math.random() * 8999 + 1000);
    }
    public static int randomNumberCvv(){
        return (int) (Math.random() * 899 + 100);
    }
}
