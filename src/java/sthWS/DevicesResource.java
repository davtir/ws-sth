/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sthWS;

import entities.Media;
import entities.Device;
import entities.TreasureStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
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
import org.apache.tomcat.util.codec.binary.Base64;
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
        
        JSONObject jsonTreasure = new JSONObject();
        Device treasure = map.get(treasureID);
        if(treasure == null) {
            
            LOG.warning("Error: No treasure in map");
            return Response.status(404).entity("No Treasure found").build();           
            
        }    
        try {
            
            jsonTreasure.put("ID", treasure.getMACAddress());
            jsonTreasure.put("NAME", treasure.getName());
            jsonTreasure.put("ROLE", treasure.getRole());
            jsonTreasure.put("LATITUDE", treasure.getLatitude());
            jsonTreasure.put("LONGITUDE", treasure.getLongitude());
            jsonTreasure.put("LUMINOSITY", treasure.getLuminosity());
            jsonTreasure.put("TEMPERATURE", treasure.getTemperature());
            jsonTreasure.put("ACCELERATION", treasure.getAcceleration());
            
        } catch (JSONException ex) {
            LOG.warning("Error while building jsonTreasure");
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
            String id = (String) jsonDevice.get("ID");
            String name = (String) jsonDevice.get("NAME");      
            String role = (String) jsonDevice.get("ROLE");
            double latitude = jsonDevice.getDouble("LATITUDE");
            double longitude = jsonDevice.getDouble("LONGITUDE");
            double luminosity = jsonDevice.getDouble("LUMINOSITY");
            double temperature = jsonDevice.getDouble("TEMPERATURE");
            double[] acceleration = new double[3];
            
            JSONArray jArr = jsonDevice.getJSONArray("ACCELERATION");
            if(jArr.length() != 3) {
                LOG.warning("Invalid acceleration length");
                return Response.status(404).entity(sb.toString()).build();
            }
                
            acceleration[0] = jArr.getDouble(0);
            acceleration[1] = jArr.getDouble(1);
            acceleration[2] = jArr.getDouble(2);
            
            Device device = new Device(id, name, role, latitude, longitude, luminosity, temperature, acceleration);
            
            if (role.equals("T"))
                treasureID=id;
            map.put(id, device);
            
            
        } catch (JSONException ex) {
            ex.printStackTrace();
            LOG.warning("Error while parsing JSONObject");
            return Response.status(404).entity(sb.toString()).build();
        }
        

        return Response.status(200).entity("Post succeded").build();
        //return Response.created(context.getAbsolutePath()).build();
    }

    
     /**
     * Retrieves info about the discovery status of the treasure
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/endgame")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFound() {
    
        JSONObject jsonEndgame = new JSONObject();
        Media winner = treasureStatus.getWinner();
        
        String filename = "", data = "";
        if ( winner != null && winner.getData() != null ) {
            filename = winner.getFilename();
            data = Base64.encodeBase64String(winner.getData());
        }
        
        try {
            jsonEndgame.put("STATUS", treasureStatus.isFound());
            jsonEndgame.put("PIC_NAME", filename);            
            jsonEndgame.put("PIC_DATA", data);
        } catch (JSONException ex) {
            LOG.warning("Error while building jsonTreasure");
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
            Boolean status = Boolean.parseBoolean(jsonEndgame.getString("STATUS"));
            String filename = jsonEndgame.getString("PIC_NAME");         
            String data = jsonEndgame.getString("PIC_DATA");
            
            Media winner = new Media(filename, Base64.decodeBase64(data));
            
            treasureStatus.setFound(status);
            treasureStatus.setWinner(winner);
        } catch (JSONException e) {
            e.printStackTrace();
            LOG.warning("Error while parsing JSONObject");
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
        DeviceResource dev = getDeviceResource(id);
        dev.delete();
        
        return Response.status(200).entity("Delete succeded").build();
    }
    
    @POST
    @Path("/audio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postAudio(InputStream incoming) {
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
            String filename = jsonAudio.getString("AUDIO_NAME");         
            String data = jsonAudio.getString("AUDIO_DATA");
            
            _audio = new Media(filename, Base64.decodeBase64(data));
        } catch (JSONException e) {
            e.printStackTrace();
            LOG.warning("Error while parsing JSONObject");
            return Response.status(404).entity(sb.toString()).build();           
        }
        
        return Response.status(200).entity("Post succeded").build();
    }
    
    @GET
    @Path("/audio")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAudio() {
        JSONObject jsonAudio = new JSONObject();
        if( _audio == null ) {
            
            LOG.warning("No audio uploaded");
            return Response.status(404).entity("No audio uploaded").build();           
            
        }   
        
        String filename = _audio.getFilename();
        String data = Base64.encodeBase64String(_audio.getData());
        try {
            
            jsonAudio.put("AUDIO_NAME", filename);            
            jsonAudio.put("AUDIO_DATA", data);
        } catch (JSONException ex) {
            LOG.warning("Error while building jsonTreasure");
            return Response.status(404).entity("Error while building jsonTreasure").build();
        }
        
        return Response.status(200).entity(jsonAudio.toString()).build();        
    } 
    
    @POST
    @Path("/picture")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPicture(InputStream incoming) {
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
            String filename = jsonPicture.getString("PIC_NAME");         
            String data = jsonPicture.getString("PIC_DATA");
            
            _picture = new Media(filename, Base64.decodeBase64(data));
        } catch (JSONException e) {
            e.printStackTrace();
            LOG.warning("Error while parsing JSONObject");
            return Response.status(404).entity(sb.toString()).build();           
        }
        
        return Response.status(200).entity("Post succeded").build();
    }   
    
    @GET
    @Path("/picture")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPicture() {
        JSONObject jsonPicture = new JSONObject();
        if( _picture == null ) {
            
            LOG.warning("No picture uploaded");
            return Response.status(404).entity("No picture uploaded").build();           
            
        }   
        
        String filename = _picture.getFilename();
        String data = Base64.encodeBase64String(_picture.getData());
        try {
            
            jsonPicture.put("PIC_NAME", filename);            
            jsonPicture.put("PIC_DATA", data);
        } catch (JSONException ex) {
            LOG.warning("Error while building jsonTreasure");
            return Response.status(404).entity("Error while building jsonTreasure").build();
        }
        
        return Response.status(200).entity(jsonPicture.toString()).build();        
    }
}
