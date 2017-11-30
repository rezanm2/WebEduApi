package nl.webedu;

import io.dropwizard.Application;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class WebEduApiApplication extends Application<WebEduApiConfiguration> {
    private ConfiguredBundle assetsBundle;
    private String name;
    public static void main(final String[] args) throws Exception {
        new WebEduApiApplication().run(args);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void initialize(final Bootstrap<WebEduApiConfiguration> bootstrap) {
        assetsBundle = (ConfiguredBundle) new ConfiguredAssetsBundle("/assets/", "/client", "index.html");
        
        bootstrap.addBundle(assetsBundle);
    }

    @Override
    public void run(final WebEduApiConfiguration configuration,
                    final Environment environment) {
        name = configuration.getApiName();
    }

}
