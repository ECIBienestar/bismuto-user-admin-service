package edu.eci.cvds.users;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserAdminApplication {
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
