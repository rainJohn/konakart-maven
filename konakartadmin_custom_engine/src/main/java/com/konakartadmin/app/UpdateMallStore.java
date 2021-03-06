package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - UpdateMallStore - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class UpdateMallStore
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public UpdateMallStore(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public void updateMallStore(String sessionId, AdminStore store) throws KKAdminException
     {
         kkAdminEng.updateMallStore(sessionId, store);
     }
}
