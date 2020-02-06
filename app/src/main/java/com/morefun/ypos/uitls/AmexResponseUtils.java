package com.morefun.ypos.uitls;

public class AmexResponseUtils {
    /**
     * We found that there are only two situations,
     * one is approve,
     * the other is other values，
     * and approve that include reponse code that 000、107、001、002、900、400、800
     */
    public String convertAmexValues(String code){

        return "53";
    }
}
