package ie.brianhenry.veintobrain;

import io.dropwizard.Configuration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VeintobrainConfiguration extends Configuration {


	@JsonProperty
    @NotEmpty
    private String mongohost = "localhost";
 
    @JsonProperty
    @Min(1)
    @Max(65535)
    private int mongoport = 27017;
 
    @JsonProperty
    @NotEmpty
    private String mongodb = "mydb";

	public String getMongohost() {
		return mongohost;
	}

	public int getMongoport() {
		return mongoport;
	}

	public String getMongodb() {
		return mongodb;
	}
    
    
    
}