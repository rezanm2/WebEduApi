package nl.webedu;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.bundles.assets.AssetsBundleConfiguration;
import io.dropwizard.bundles.assets.AssetsConfiguration;
import javax.validation.Valid;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

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
}
