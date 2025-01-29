package com.collector.howare_you_api.auth.service.imple;

import com.collector.howare_you_api.auth.entity.UserEntity;
import com.collector.howare_you_api.auth.repository.UserRepository;
import com.collector.howare_you_api.auth.service.CustomOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository repository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName();
        log.info("oauthClientName ---> {}",oauthClientName);
        try {
            log.info("{} ..",new ObjectMapper().writeValueAsString(oAuth2User.getAuthorities()));
        }catch (Exception e){
            e.printStackTrace();
        }

        UserEntity userEntity = null;
        String userId = null;
        String email = "eamil@email.com";

        if (oauthClientName.equals("kakao")){
            userId = "kakao_"+oAuth2User.getAttributes().get("id");
            userEntity = new UserEntity(userId,email,"kakao");
        }

        if (oauthClientName.equals("naver")){
        Map<String,String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
        userId = "naver_"+responseMap.get("id").substring(0,14);
        email = responseMap.get("email");
        userEntity = new UserEntity(userId,email,"naver");

        }
        // 아직 디비 연결안해놔서 로그로 찍음

        log.info(" --->{}",userEntity);
        //repository.save(userEntity);

        return new CustomOAuth2User(userId);
    }
}
