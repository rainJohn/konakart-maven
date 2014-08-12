package com.konakartadmin.app;

import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - DeleteFile - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class DeleteFile
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public DeleteFile(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public boolean deleteFile(String sessionId, String fileName) throws KKAdminException
     {
         return kkAdminEng.deleteFile(sessionId, fileName);
     }
}