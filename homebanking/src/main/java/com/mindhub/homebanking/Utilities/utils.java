package com.mindhub.homebanking.Utilities;

import com.mindhub.homebanking.Services.ServicesAccount;
import com.mindhub.homebanking.Services.ServicesCard;
import com.mindhub.homebanking.repositories.AccountRepositories;

public class utils {
        //Card Controller
    public static String cvv() {
        int cvvGen = (int) (Math.random() * 999);    //casting en java.
        String cvvGenCompletado = String.format("%03d", cvvGen);
        return cvvGenCompletado;
    }

    public static String seccionDeNumeros() {
        int first = (int) (Math.random() * (9999 - 1000) + 1000);
        int second = (int) (Math.random() * (9999 - 1000) + 1000);
        int third = (int) (Math.random() * (9999 - 1000) + 1000);
        int fourth = (int) (Math.random() * (9999 - 1000) + 1000);

        String seccionDeNumeros = first + "-" + second + "-" + third + "-" + fourth;
        return seccionDeNumeros;
    }
    public static String Number3(ServicesCard servicesCard){
        String Number;
        boolean verifyNumber;
        do {
            Number=seccionDeNumeros();
            verifyNumber=servicesCard.existsByNumber(Number);
        }while(verifyNumber);
        return Number;
    }

    //Card Controller

    //Account Controller
    public static String GenerateNumber(){
        int number1=(int) (Math.random() * (99999999));
        String number ="VIN-"+number1;
        return number;
    }

    public static String Number2(ServicesAccount servicesAccount){
        String Number;
        boolean verifyNumber;
        do {
            Number=GenerateNumber();
            verifyNumber=servicesAccount.existsByNumber(Number);
        }while(verifyNumber);
        return Number;
    }

    //Account Controller

    //Client Controller

    public static String genericNumber(){
        int Numbers= (int) (Math.random()* 99999999);
        String fullNumber = String.format("VIN%03d", Numbers);
        return fullNumber;
    }

    //Client Controller
}
