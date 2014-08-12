//
// (c) 2006 DS Data Systems UK Ltd, All rights reserved.
//
// DS Data Systems and KonaKart and their respective logos, are 
// trademarks of DS Data Systems UK Ltd. All rights reserved.
//
// The information in this document is the proprietary property of
// DS Data Systems UK Ltd. and is protected by English copyright law,
// the laws of foreign jurisdictions, and international treaties,
// as applicable. No part of this document may be reproduced,
// transmitted, transcribed, transferred, modified, published, or
// translated into any language, in any form or by any means, for
// any purpose other than expressly permitted by DS Data Systems UK Ltd.
// in writing.
//
package com.konakartadmin.bl;

/**
 * Defines the interface for the AdminOrderIntegrationMgr
 */
public interface AdminOrderIntegrationMgrInterface
{    
    /**
     * Called just after an order status change
     * 
     * @param orderId
     * @param currentStatus
     * @param newStatus
     */
    public void changeOrderStatus(int orderId, int currentStatus, int newStatus);
}
