package com.burcu.utility;

import com.burcu.domain.UserProfile;
import com.burcu.manager.UserManager;
import com.burcu.service.UserProfileService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GetAllData {

    private final UserManager userManager;
    private final UserProfileService userProfileService;


    //@PostConstruct  //tek seferlik çalıştırıldı, önceki kayıtların da eklenmesi sağlandı.
    public void getAllData() {
        List<UserProfile> userProfileList = userManager.findAll().getBody();
        userProfileService.saveAll(Objects.requireNonNull(userProfileList));
    }
}
