package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

/**
 * <pre>
 * Spring boot admin server Application
 * </pre>
 * 
 * @since May 23, 2019
 * @author Sunkist Wang
 * @version
 *          <ul>
 *          <li>May 23, 2019,Sunkist Wang,new
 *          </ul>
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class AdminServer {

    public static void main(String[] args) {
        SpringApplication.run(AdminServer.class, args);
    }

}
