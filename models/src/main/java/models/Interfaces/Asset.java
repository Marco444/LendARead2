package models.Interfaces;

import sun.security.util.ByteArrays;

public interface Asset {
     int getId();

     String getName();


    byte[] getPhoto();

    String display();

    String type();
}
