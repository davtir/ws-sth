/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sthWS;

import entities.Media;
import entities.Device;
import entities.JsonParser;
import entities.TreasureStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.*;

/**
 * REST Web Service
 *
 * @author Alex
 */
@Path("/")
public class DevicesResource {

    private final static Logger LOG = Logger.getLogger(DevicesResource.class.getName());
    
    @Context
    private UriInfo context;

    public static HashMap< String, Device> map = new HashMap<>();
    public static Media _audio;
    public static Media _picture;
    public static String treasureID="";
    public static TreasureStatus treasureStatus = new TreasureStatus(false);
    
    /**
     * Creates a new instance of DevicesResource
     */
    public DevicesResource() {
    }

    /**
     * Retrieves representation of an instance of sthWS.DevicesResource
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/device")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDevice() {
        LOG.info("getDevice() function invoked.");
        Device treasure = map.get(treasureID);
        if(treasure == null) {
            
            LOG.warning("Error: No treasure in map");
            return Response.status(404).entity("No Treasure found").build();           
            
        }
        
        JSONObject jsonTreasure;
        try {
            jsonTreasure = JsonParser.createJsonFromDevice(treasure);
        } catch (JSONException ex) {
            LOG.warning(ex.getMessage());
            return Response.status(404).entity("Error while building jsonTreasure").build();
        }
        
        return Response.status(200).entity(jsonTreasure.toString()).build();
        
        
    }

    /**
     * POST method for creating an instance of DeviceResource
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Path("/device")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDevice(InputStream content) {
        LOG.info("postDevice() function invoked.");
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(content));
            String line = null;
            while ( (line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch(IOException e) {
            LOG.warning("Error while parsing JSON stream");
            return Response.status(404).entity(sb.toString()).build();
        }
        
        JSONObject jsonDevice;       
        try {
            
            jsonDevice = new JSONObject(sb.toString());            
            Device device = JsonParser.createDeviceFromJson(jsonDevice);
            
            if (device.getRole().equals("T"))
                treasureID = device.getMACAddress();
            map.put(device.getMACAddress(), device);           
        } catch (JSONException ex) {
            LOG.warning(ex.getMessage());
            return Response.status(404).entity(sb.toString()).build();
        }
        return Response.status(200).entity("Post succeded").build();
    }

    
     /**
     * Retrieves info about the discovery status of the treasure
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/endgame")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFound() {
        LOG.info("getFound() function invoked.");
        JSONObject jsonEndgame;
        try {
            jsonEndgame = JsonParser.createJsonFromTreasureStatus(treasureStatus);
        } catch (JSONException ex) {
            LOG.warning(ex.getMessage());
            return Response.status(404).entity("Error while building jsonTreasure").build();
        }
        
        return Response.status(200).entity(jsonEndgame.toString()).build();   
    }
    
    /**
     * Post info about the discovery status of the treasure
     * @return an instance of java.lang.String
     */
    @POST
    @Path("/endgame")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postFound(InputStream incoming) {
        LOG.info("postFound() function invoked.");
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(incoming));
            String line = null;
            while ( (line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch(IOException e) {
            LOG.warning("Error while parsing JSON stream");
            return Response.status(404).entity(sb.toString()).build();
        }
        
        JSONObject jsonEndgame;
        try {
            jsonEndgame = new JSONObject(sb.toString());
            treasureStatus = JsonParser.createTresureStatusFromJson(jsonEndgame);
        } catch (JSONException e) {
            LOG.warning(e.getMessage());
            return Response.status(404).entity(sb.toString()).build();           
        }
        
        return Response.status(200).entity("Post succeded").build();
    }
    
    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public DeviceResource getDeviceResource(@PathParam("id") String id) {
        return DeviceResource.getInstance(id);
    }
    
    @POST
    @Path("/delete")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postDelete(String id) {
        LOG.info("postDelete() function invoked.");
        DeviceResource dev = getDeviceResource(id);
        dev.delete();
        
        return Response.status(200).entity("Delete succeded").build();
    }
    
    @POST
    @Path("/audio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postAudio(InputStream incoming) {
        LOG.info("postAudio() function invoked.");
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(incoming));
            String line = null;
            while ( (line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch(IOException e) {
            LOG.warning("Error while parsing JSON stream");
            return Response.status(404).entity(sb.toString()).build();
        }
        
        JSONObject jsonAudio;
        try {
            jsonAudio = new JSONObject(sb.toString());
            _audio = JsonParser.createAudioMediaFromJson(jsonAudio);
        } catch (JSONException e) {
            LOG.warning(e.getMessage());
            return Response.status(404).entity(sb.toString()).build();           
        }
        
        return Response.status(200).entity("Post succeded").build();
    }
    
    @GET
    @Path("/audio")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAudio() {
        LOG.info("getAudio() function invoked.");
        if( _audio == null ) {
            LOG.warning("No audio uploaded");
            return Response.status(404).entity("No audio uploaded").build();           
        }
        
        JSONObject jsonAudio;
        try {
            jsonAudio = JsonParser.createJsonFromAudioMedia(_audio);
        } catch (JSONException ex) {
            LOG.warning(ex.getMessage());
            return Response.status(404).entity("Error while building json").build();
        }
        
        return Response.status(200).entity(jsonAudio.toString()).build();        
    } 
    
    @POST
    @Path("/picture")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPicture(InputStream incoming) {
        LOG.info("postPicture() function invoked.");
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(incoming));
            String line = null;
            while ( (line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch(IOException e) {
            LOG.warning("Error while parsing JSON stream");
            return Response.status(404).entity(sb.toString()).build();
        }
        
        JSONObject jsonPicture;
        try {
            jsonPicture = new JSONObject(sb.toString());            
            _picture = JsonParser.createPictureMediaFromJson(jsonPicture);
        } catch (JSONException e) {
            LOG.warning(e.getMessage());
            return Response.status(404).entity(sb.toString()).build();           
        }
        
        return Response.status(200).entity("Post succeded").build();
    }   
    
    @GET
    @Path("/picture")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPicture() {
        LOG.info("getPicture() function invoked.");
        if( _picture == null ) {
            LOG.warning("No picture uploaded");
            return Response.status(404).entity("No picture uploaded").build();           
        }   
        
        JSONObject jsonPicture;
        try {
           jsonPicture = JsonParser.createJsonFromPictureMedia(_picture);
        } catch (JSONException ex) {
            LOG.warning(ex.getMessage());
            return Response.status(404).entity("Error while building jsonTreasure").build();
        }
        
        return Response.status(200).entity(jsonPicture.toString()).build();        
    }
}
