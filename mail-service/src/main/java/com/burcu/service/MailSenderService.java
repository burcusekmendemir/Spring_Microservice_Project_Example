package com.burcu.service;

import com.burcu.rabbitmq.model.RegisterMailModel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    //bu maile cevap verilemez tarzı br sistem kuracağız.
    private final JavaMailSender javaMailSender;

//    @EventListener(ApplicationReadyEvent.class) //Uygulama ayaga kalktiginda metodun bir kerelik calistirilmasi icin. (deneme asamasinda kullandik)
    public void sendMail(RegisterMailModel model){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom("burcusekmen6@gmail.com");
        mailMessage.setTo("burcusekmen6@gmail.com"); //kendime mail göderiyorum şimdilik,  burasi development tamamlandiginda kullanici mailiyle degistirilecek.
        mailMessage.setSubject("AKTİVASYON KODUNUZ...");
        mailMessage.setText(model.getUsername()+" Aramıza hoşgeldin!\n"+"Hesabınızı doğrulamak için aktivasyon kodunuz: "+model.getActivationCode()); // 5 haneli aktivasyon kodu örneği
        javaMailSender.send(mailMessage);

    }
}
