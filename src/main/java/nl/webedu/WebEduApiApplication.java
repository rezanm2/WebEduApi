package nl.webedu;

import io.dropwizard.Application;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.net.InetAddress;
import java.net.ServerSocket;
import nl.webedu.auth.Auth;
import nl.webedu.healthchecks.DatabaseHealthCheck;
import nl.webedu.models.EmployeeModel;
import nl.webedu.resources.ProjectResource;
import nl.webedu.resources.UserResource;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import nl.webedu.resources.*;
import nl.webedu.helpers.*;

/**
 * Deze klasse is de startpunt van de api
 * @author rezanaser
 */
public class WebEduApiApplication extends Application<WebEduApiConfiguration> {
    private ConfiguredBundle assetsBundle;
    private String name;
    public static void main(final String[] args) throws Exception {
//        DateHelper dateHelper = new DateHelper();
//        System.out.println(dateHelper.parseTime("17:51:05", "HH:mm:ss"));
//        InetAddress locIP = InetAddress.getByName("192.168.1.68");
//        ServerSocket serverSocket = new ServerSocket(9999, 0, locIP);
        new WebEduApiApplication().run(args);
    }
    /**
     * Deze methode returnt de apiName
     * De apiName krijgt hij van e WebEduApiConfiguration Klasse
     * @return apiName
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void initialize(final Bootstrap<WebEduApiConfiguration> bootstrap) {
        /**
         * Hier wordt er een assetsBundle aangemaakt en wordt de er gezegd index.html moet zoeken in de angular directory
         * De path van de angular directory wordt meegeven in het config.yml bestand in de home directory.
         * @author rezanaser
         */
        assetsBundle = (ConfiguredBundle) new ConfiguredAssetsBundle("/assets/", "/client", "index.html");
        
        bootstrap.addBundle(assetsBundle);
    }

    @Override
    public void run(final WebEduApiConfiguration configuration,
                    final Environment environment) {
        /**
         * Load resources into environment
         */
        environment.healthChecks().register("database", new DatabaseHealthCheck());//Voert healthcheck uit(controlleer de connectie met de database)
        environment.jersey().register(new UserResource());
        environment.jersey().register(new ProjectResource());
        environment.jersey().register(new EntryResource());
        environment.jersey().register(new SprintResource());
        environment.jersey().register(new CustomerResource());
        environment.jersey().register(AuthFactory.binder(
                new BasicAuthFactory<>(
                        new Auth(),
                        "Security realm",
                        EmployeeModel.class
                )));


        /**
         * ALLOW ALL CONTENT FOR DEV PURPOSES WILL DELETE THIS ON PRODUCTION TIME
         */
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

}
