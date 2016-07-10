
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sthWS;

import entities.Device;
import java.util.logging.Logger;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * REST Web Service
 *
 * @author Alex
 */
public class DeviceResource {

    private final static Logger LOG = Logger.getLogger(DeviceResource.class.getName());
    
    private String id;

    /**
     * Creates a new instance of DeviceResource
     */
    private DeviceResource(String id) {
        
        LOG.info("Constructor created");
        
        this.id = id;
    }

    /**
     * Get instance of the DeviceResource
     */
    public static DeviceResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of DeviceResource class.
        
        LOG.info("getInstance invoked");
        return new DeviceResource(id);
    }

    /**
     * Retrieves representation of an instance of sthWS.DeviceResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
        //TODO return proper representation object
        LOG.info("getText invoked");
        
        Device device = DevicesResource.map.get(id);
        if (device == null) {
            LOG.warning("Device " + id + " not found");
            return Response.status(404).entity("Device " + id + " not found").build();
        }    
        JSONObject jsonDevice = new JSONObject();
        try {
            
            jsonDevice.put("ID", device.getMACAddress());
            jsonDevice.put("NAME", device.getName());
            jsonDevice.put("ROLE", device.getRole());
            jsonDevice.put("LATITUDE", device.getLatitude());
            jsonDevice.put("LONGITUDE", device.getLongitude());
            jsonDevice.put("LUMINOSITY", device.getLuminosity());
            jsonDevice.put("TEMPERATURE", device.getTemperature());            
            jsonDevice.put("ACCELERATION", device.getAcceleration());
            jsonDevice.put("ROTATION", device.getRotation());
            
        } catch (JSONException ex) {
            LOG.warning("Error while building jsonDevice");
            return Response.status(404).entity("Error while building jsonDevice").build();
        }

        return Response.status(200).entity(jsonDevice.toString()).build();
        
    }

    /**
     * PUT method for updating or creating an instance of DeviceResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    /**
     * DELETE method for resource DeviceResource
     */
    @DELETE
    public void delete() {
        
        LOG.info("delete invoked");
        
        DevicesResource.map.remove(id);
    }
}
