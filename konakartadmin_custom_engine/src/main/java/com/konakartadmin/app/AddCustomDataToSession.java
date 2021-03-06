package com.konakartadmin.app;

import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - AddCustomDataToSession - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class AddCustomDataToSession
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public AddCustomDataToSession(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public void addCustomDataToSession(String sessionId, String data, int position) throws KKAdminException
     {
         kkAdminEng.addCustomDataToSession(sessionId, data, position);
     }
}
