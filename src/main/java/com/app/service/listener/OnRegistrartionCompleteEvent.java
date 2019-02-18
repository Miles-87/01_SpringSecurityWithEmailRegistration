package com.app.service.listener;

import com.app.model.security.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

// klasa ktorej obiekt bedzie przesylany w ramach zdarzenia

@Getter
@Setter
public class OnRegistrartionCompleteEvent extends ApplicationEvent {

    private User user;
    private String url;

    public OnRegistrartionCompleteEvent(User user, String url) {
        super(user);
        this.user = user;
        this.url = url;
    }
}
