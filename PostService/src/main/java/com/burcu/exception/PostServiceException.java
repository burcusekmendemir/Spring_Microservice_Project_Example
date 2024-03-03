package com.burcu.exception;

import lombok.Getter;

@Getter // errortype erişebilmek için getter ekledik
public class PostServiceException extends RuntimeException {
    /**
     * Kendi Exp sınıfımızı oluşturmak için yapılacaklar;
     * 1- Runtime Exp miras alınır
     * 2- Error.md'ye olası hatalar yazılır.
     * 3- ErrorType enumu yaratılır
     * 4- ErrorMEssage classı yaratılır.
     * 5- controllerda  hata-olustur mapi oluşturulur.
     */

    private final ErrorType errorType;

    public PostServiceException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public PostServiceException(ErrorType errorType, String message){
        super(message);
        this.errorType = errorType;
    }


}
