package com.konakartadmin.app;

import com.konakartadmin.bl.KKAdmin;

/**
 *  The KonaKart Custom Engine - UpdateOrderStatus - Generated by CreateKKAdminCustomEng
 */
@SuppressWarnings("all")
public class UpdateOrderStatus
{
    KKAdmin kkAdminEng = null;

    /**
     * Constructor
     */
     public UpdateOrderStatus(KKAdmin _kkAdminEng)
     {
         kkAdminEng = _kkAdminEng;
     }

      @Deprecated
     public void updateOrderStatus(String sessionId, int orderId, int orderStatus, String comments, boolean notifyCustomer) throws KKAdminException
     {
         kkAdminEng.updateOrderStatus(sessionId, orderId, orderStatus, comments, notifyCustomer);
     }
}
