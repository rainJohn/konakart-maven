package com.konakartadmin.app;

import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - SetConfigurationValue - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class SetConfigurationValue
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public SetConfigurationValue(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public boolean setConfigurationValue(String sessionId, String key, String value) throws KKAdminException
     {
         return kkAdminEng.setConfigurationValue(sessionId, key, value);
     }
}
