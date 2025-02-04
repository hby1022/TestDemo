package com.transpeed.park.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Component
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;

    public  String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
          log.error("getLocalHost error:"+e.getMessage());
        }
        assert address != null;
        return "http://" + address.getHostAddress() +":" + this.serverPort;
    }


    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }
}
