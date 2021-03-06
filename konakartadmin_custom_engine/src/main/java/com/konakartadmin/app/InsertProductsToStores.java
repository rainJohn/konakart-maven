package com.konakartadmin.app;

import com.konakartadmin.appif.*;
import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - InsertProductsToStores - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class InsertProductsToStores
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public InsertProductsToStores(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

     public void insertProductsToStores(String sessionId, AdminProductToStore[] productToStores) throws KKAdminException
     {
         kkAdminEng.insertProductsToStores(sessionId, productToStores);
     }
}
