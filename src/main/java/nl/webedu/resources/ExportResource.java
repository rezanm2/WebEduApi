/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.auth.Auth;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import nl.webedu.models.CSVModel;
import nl.webedu.models.EmployeeModel;
import nl.webedu.services.ExportService;

/**
 *
 * @author RdenBlaauwen
 */
@Path("/export")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExportResource {
    private ExportService exportService;
    public ExportResource(){
        this.exportService=new ExportService();
    }
    
    @GET
    @JsonProperty
    @Produces(MediaType.APPLICATION_JSON)
    public CSVModel read(@Auth EmployeeModel employeeModel){
        System.out.println(this.getClass().toString()+": read "+employeeModel.getEmployeeRole());
        if(employeeModel.getEmployeeRole().equals("employee")){
            return null;
        }
        return this.exportService.read();
    }
}
