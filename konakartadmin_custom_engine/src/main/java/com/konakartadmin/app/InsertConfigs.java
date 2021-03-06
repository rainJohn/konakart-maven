package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - InsertConfigs - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class InsertConfigs
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public InsertConfigs(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public void insertConfigs(String sessionId, KKConfiguration[] config) throws KKAdminException
     {
         kkAdminEng.insertConfigs(sessionId, config);
     }
}
