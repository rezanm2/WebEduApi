package nl.webedu;

import io.dropwizard.Application;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.webedu.auth.Auth;
import nl.webedu.models.EmployeeModel;
import nl.webedu.resources.ProjectResource;
import nl.webedu.resources.UserResource;
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
        environment.jersey().register(AuthFactory.binder(
                new BasicAuthFactory<>(
                        new Auth(),
                        "Security realm",
                        EmployeeModel.class
                )));
        

        /**
         * Hier krijgt de variabele 'naam' de apiName van de WebEduApiConfiguration
         * @author rezanaser
         */
        name = configuration.getApiName();
    }

}
