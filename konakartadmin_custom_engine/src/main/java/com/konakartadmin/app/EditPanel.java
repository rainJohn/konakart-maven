package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - EditPanel - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class EditPanel
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public EditPanel(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public void editPanel(String sessionId, AdminPanel panel) throws KKAdminException
     {
         kkAdminEng.editPanel(sessionId, panel);
     }
}
