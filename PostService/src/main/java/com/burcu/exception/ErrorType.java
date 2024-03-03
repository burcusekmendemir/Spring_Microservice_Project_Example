package com.burcu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorType {

    INTERNAL_SERVER_ERROR(5000, "Sunucu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    LOGIN_ERROR(4110, "Girilen kullanıcı adı ya da parola hatalıdır.", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4111, "Bu kullanıcı adı zaten kayıtlıdır.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_ERROR(4100,"Parametre Hatası", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4112, "Kullanıcı bulunamadı.", HttpStatus.BAD_REQUEST),
    ACTIVATION_CODE_ERROR(4113, "Aktivasyon kod hatası.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4114,"Gecersiz token" ,HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(4115,"Token olusturulamadi" ,HttpStatus.BAD_REQUEST ),
    ACCOUNT_NOT_ACTIVE(4116, "Hesabınız aktif değildir" ,HttpStatus.FORBIDDEN),
    USER_NOT_CREATED(4117,"Kullanıcı olusturulamadi" ,HttpStatus.BAD_REQUEST ),
    POST_NOT_FOUND(4118, "Post bulunamadı" , HttpStatus.BAD_REQUEST ),
    COMMENT_NOT_FOUND(4119,"Yorum bulunamadı" ,HttpStatus.BAD_REQUEST ),
    LIKE_ALREADY_EXISTS(4120, "Zaten beğenildi" , HttpStatus.BAD_REQUEST);


    private int code;
    private String message;
    private HttpStatus httpStatus;


}
