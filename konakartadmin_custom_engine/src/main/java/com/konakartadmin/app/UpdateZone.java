package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - UpdateZone - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class UpdateZone
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public UpdateZone(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public int updateZone(String sessionId, AdminZone updateObj) throws KKAdminException
     {
         return kkAdminEng.updateZone(sessionId, updateObj);
     }
}
