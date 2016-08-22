/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static sthWS.DevicesResource._audio;
import static sthWS.DevicesResource.treasureStatus;

/**
 *
 * @author davtir
 */
public class JsonParser {
    
    public static Device createDeviceFromJson(JSONObject json) throws JSONException {
        if ( json == null )
            throw new JSONException("Invalid input json");
        
        String id = (String) json.get("ID");
        String name = (String) json.get("NAME");      
        String role = (String) json.get("ROLE");
        double lat = json.getDouble("LATITUDE");
        double lon = json.getDouble("LONGITUDE");
        double lux = json.getDouble("LUMINOSITY");
        double temperature = json.getDouble("TEMPERATURE");
        double[] acceleration = new double[3];

        JSONArray jArr = json.getJSONArray("ACCELERATION");
        if(jArr.length() != 3) {
            throw new JSONException("Invalid acceleration length");
        }
        acceleration[0] = jArr.getDouble(0);
        acceleration[1] = jArr.getDouble(1);
        acceleration[2] = jArr.getDouble(2);
        
        return new Device(id, name, role, lat, lon, lux, temperature, acceleration);
    }
    
    public static JSONObject createJsonFromDevice(Device device) throws JSONException {
        if ( device == null )
            throw new JSONException("Invalid input device");
        JSONObject jsonDevice = new JSONObject();       
        jsonDevice.put("ID", device.getMACAddress());
        jsonDevice.put("NAME",device.getName());
        jsonDevice.put("ROLE",device.getRole());
        jsonDevice.put("LATITUDE", device.getLatitude());
        jsonDevice.put("LONGITUDE", device.getLongitude());
        jsonDevice.put("LUMINOSITY", device.getLuminosity());
        jsonDevice.put("TEMPERATURE", device.getTemperature());            
        jsonDevice.put("ACCELERATION", device.getAcceleration());           
        return jsonDevice;
    }
    
    public static Media createPictureMediaFromJson(JSONObject json) throws JSONException {
        if ( json == null )
            throw new JSONException("Invalid input json");
        
        String filename = json.getString("PIC_NAME");         
        String data = json.getString("PIC_DATA");
            
        return new Media(filename, Base64.decodeBase64(data));
    }
    
    public static JSONObject createJsonFromPictureMedia(Media pic) throws JSONException {
        if ( pic == null )
            throw new JSONException("Invalid audio media in input");
        
        JSONObject jsonPic = new JSONObject();
        jsonPic.put("PIC_NAME", pic.getFilename());            
        jsonPic.put("PIC_DATA", Base64.encodeBase64String(pic.getData()));
        
        return jsonPic;
    }
    
    public static Media createAudioMediaFromJson(JSONObject json) throws JSONException {
        if ( json == null )
            throw new JSONException("Invalid input json");
        
        String filename = json.getString("AUDIO_NAME");         
        String data = json.getString("AUDIO_DATA");
            
        return new Media(filename, Base64.decodeBase64(data));
    }
    
    public static JSONObject createJsonFromAudioMedia(Media audio) throws JSONException {
        if ( audio == null )
            throw new JSONException("Invalid audio media in input");
        
        JSONObject jsonAudio = new JSONObject();
        jsonAudio.put("AUDIO_NAME", audio.getFilename());            
        jsonAudio.put("AUDIO_DATA", Base64.encodeBase64String(audio.getData()));
        
        return jsonAudio;
    }
    
    public static TreasureStatus createTresureStatusFromJson(JSONObject json) throws JSONException {
        if ( json == null )
            throw new JSONException("Invalid input json");
        
        Boolean status = Boolean.parseBoolean(json.getString("STATUS"));
        String filename = json.getString("PIC_NAME");         
        String data = json.getString("PIC_DATA");

        Media winner = new Media(filename, Base64.decodeBase64(data));

        TreasureStatus t_status = new TreasureStatus(status);
        t_status.setWinner(winner);
        return t_status;
        
    }
    
    public static JSONObject createJsonFromTreasureStatus(TreasureStatus status) throws JSONException {
        if ( status == null )
            throw new JSONException("Invalid input status");
        
        JSONObject jsonEndgame = new JSONObject();
        Media winner = treasureStatus.getWinner();   
        String filename = "", data = "";
        if ( winner != null && winner.getData() != null ) {
            filename = winner.getFilename();
            data = Base64.encodeBase64String(winner.getData());
        }
        
        jsonEndgame.put("STATUS", treasureStatus.isFound());
        jsonEndgame.put("PIC_NAME", filename);            
        jsonEndgame.put("PIC_DATA", data);
        
        return jsonEndgame;
    }
    
}
