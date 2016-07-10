
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.ArrayList;
import javax.ws.rs.core.Response;

/**
 *
 * @author Alex
 */
public class Device {
    private final String mac_address_;
    private String name_;
    private String role_;
    private double latitude_;
    private double longitude_;
    private double luminosity_;
    private double temperature_;
    private double[] acceleration_;
    private double[] rotation_;
    
    public Device(String mac_address, String name, String role, double lat, double lon, double lux, double temp, double[] acc, double[] rot) throws RuntimeException {
        if ( mac_address == null || name == null || acc == null || rot == null)  {
            throw new RuntimeException("Invalid mac address or name or acceleration or rotation.");
        }
        
        mac_address_ = mac_address;
        name_ = name;
        role_ = role;
        latitude_ = lat;
        longitude_ = lon;
        luminosity_ = lux;
        temperature_ = temp;
        acceleration_ = acc;
        rotation_ = rot;
    }

    public double getLatitude() {
        return latitude_;
    }

    public double getLongitude() {
        return longitude_;
    }

    public double getLuminosity() {
        return luminosity_;
    }

    public double getTemperature() {
        return temperature_;
    }

    public double[] getAcceleration() {
        return acceleration_;
    }

    public double[] getRotation() {
        return rotation_;
    }
    
    public String getMACAddress() {
        return mac_address_;
    }
    
    public String getName() {
        return name_;
    }
    
    public String getRole() {
        return role_;
    }

    @Override
    public String toString() {
        return "Device{" + "mac_address_=" + mac_address_ + ", name_=" + name_ + ", role_=" + role_ + ", latitude_=" + latitude_ + ", longitude_=" + longitude_ + ", luminosity_=" + luminosity_ + ", temperature_=" + temperature_ + ", acceleration_=" + acceleration_ + ", rotation_=" + rotation_ + '}';
    }
    

}
