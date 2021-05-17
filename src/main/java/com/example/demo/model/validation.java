package com.example.demo.model;

import java.util.HashMap;

public class validation {
    public static String asignarType(String number) {
        HashMap<String, String> types = new HashMap<>();
        types.put("03", "MasterCard");
        types.put("06", "VISA");
        types.put("12", "PRIME");
        String type = types.get(number.substring(0, 2));
        if(type == null){
            throw new IllegalArgumentException("El codigo de la tarjeta no es valido");}
        return type;
    }
}
