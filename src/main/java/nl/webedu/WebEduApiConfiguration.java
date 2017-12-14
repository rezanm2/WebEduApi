package nl.webedu;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class WebEduApiConfiguration extends Configuration implements AssetsBundleConfiguration{

    @NotEmpty
    @JsonProperty
    private String apiName;

    @Valid
    @NotNull
    @JsonProperty
    private final AssetsConfiguration assets = new AssetsConfiguration();
    
    public String getApiName()
    {
        return apiName;
    }

    public void setApiName(String apiName)
    {
        this.apiName = apiName;
    }

    @Override
    public AssetsConfiguration getAssetsConfiguration()
    {
        return assets;
    }

    @Valid
    @NotNull
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    /**
     * Obtain database connection parameters from the configuration file.
     *
     * @return Data source factory.
     */
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }
}
