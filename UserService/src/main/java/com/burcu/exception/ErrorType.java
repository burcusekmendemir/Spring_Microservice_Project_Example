package com.burcu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorType {

    INTERNAL_SERVER_ERROR(5200, "Sunucu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_DUPLICATE(4211, "Bu kullanıcı adı zaten kayıtlıdır.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(4200,"Parametre Hatası", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4212, "Kullanıcı bulunamadı.", HttpStatus.BAD_REQUEST),
    USER_NOT_CREATED(4213,"Kullanıcı oluşturulamadı" ,HttpStatus.BAD_REQUEST ),
    UPDATE_ERROR(4214,"Kullanıcı bilgileri güncellenemedi" ,HttpStatus.BAD_REQUEST ),
    INVALID_TOKEN(4214,"Gecersiz token" ,HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(4215,"Token olusturulamadi" ,HttpStatus.BAD_REQUEST ),
    ACCOUNT_NOT_ACTIVE(4216, "Hesabınız aktif değildir" ,HttpStatus.FORBIDDEN),
    USER_NOT_ACTIVE(4217, "Hesabınız aktif değildir" , HttpStatus.BAD_REQUEST),
    USER_IS_ALREADY_FOLLOWED(4218,"Kullanıcı zaten takip ediliyor" ,HttpStatus.BAD_REQUEST),
    FOLLOW_ALREADY_EXIST(4219, "Kullanıcı zaten takip ediliyor" , HttpStatus.BAD_REQUEST);



    private int code;
    private String message;
    private HttpStatus httpStatus;


}
