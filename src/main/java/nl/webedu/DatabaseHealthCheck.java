/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.webedu;
import com.codahale.metrics.health.HealthCheck;

/**
 *
 * @author rezanaser
 */
public class DatabaseHealthCheck extends HealthCheck{

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
    
}
