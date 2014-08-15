//
// (c) 2006 DS Data Systems UK Ltd, All rights reserved.
//
// DS Data Systems and KonaKart and their respective logos, are 
// trademarks of DS Data Systems UK Ltd. All rights reserved.
//
// The information in this document is free software; you can redistribute 
// it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This software is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package com.konakartadmin.modules.payment.valitor;

import com.konakart.util.Utils;
import com.konakartadmin.app.KKConfiguration;
import com.konakartadmin.bl.KKAdminBase;
import com.konakartadmin.modules.PaymentModule;

import java.util.Date;

/**
 * VALITOR payment module
 * 
 */
public class Valitor extends PaymentModule
{
    /**
     * @return the config key stub
     */
    public String getConfigKeyStub()
    {
        if (configKeyStub == null)
        {
            setConfigKeyStub(super.getConfigKeyStub() + "_VALITOR");
        }
        return configKeyStub;
    }

    public String getModuleTitle()
    {
        return getMsgs().getString("MODULE_PAYMENT_VALITOR_TEXT_TITLE");
    }

    /**
     * @return the implementation filename
     */
    public String getImplementationFileName()
    {
        return "VALITOR";
    }

    /**
     * @return the module code
     */
    public String getModuleCode()
    {
        return "VALITOR";
    }

    /**
     * @return an array of configuration values for this payment module
     */
    public KKConfiguration[] getConfigs()
    {
        if (configs == null)
        {
            configs = new KKConfiguration[11];
        }

        if (configs[0] != null && !Utils.isBlank(configs[0].getConfigurationKey()))
        {
            return configs;
        }

        Date now = KKAdminBase.getKonakartTimeStampDate();

        int i = 0;
        int groupId = 6;

        configs[i] = new KKConfiguration(
        /* title */"VALITOR Status",
        /* key */"MODULE_PAYMENT_VALITOR_STATUS",
        /* value */"true",
        /* description */"If set to false, the VALITOR module will be unavailable",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"",
        /* setFun */"tep_cfg_select_option(array('true', 'false'), ",
        /* dateAdd */now);

        configs[i] = new KKConfiguration(
        /* title */"Sort order of display",
        /* key */"MODULE_PAYMENT_VALITOR_SORT_ORDER",
        /* value */"0",
        /* description */"Sort Order of VALITOR module on the UI. Lowest is displayed first.",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"",
        /* setFun */"",
        /* dateAdd */now);

        configs[i] = new KKConfiguration(
        /* title */"VALITOR Payment Zone",
        /* key */"MODULE_PAYMENT_VALITOR_ZONE",
        /* value */"0",
        /* description */"Zone where VALITOR module can be used. Otherwise it is disabled.",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"tep_get_zone_class_title",
        /* setFun */"tep_cfg_pull_down_zone_classes(",
        /* dateAdd */now);

        configs[i] = new KKConfiguration(
        /* title */"Callback username",
        /* key */"MODULE_PAYMENT_VALITOR_CALLBACK_USERNAME",
        /* value */"myuser",
        /* description */"Valid username for KonaKart. Used by the callback"
                + " code to log into KonaKart in order to make an engine call",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"",
        /* setFun */"",
        /* dateAdd */now);

        configs[i] = new KKConfiguration(
        /* title */"Callback Password",
        /* key */"MODULE_PAYMENT_VALITOR_CALLBACK_PASSWORD",
        /* value */"mypassword",
        /* description */"Valid password for KonaKart. Used by the callback"
                + " code to log into KonaKart in order to make an engine call",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"",
        /* setFun */"password",
        /* dateAdd */now);

        configs[i] = new KKConfiguration(
        /* title */"Callback URL",
        /* key */"MODULE_PAYMENT_VALITOR_CALLBACK_URL",
        /* value */"http://host:port/konakart/VALITORCallback.action",
        /* description */"URL used by VALITOR to callback into KonaKart."
                + " This would typically be HTTPS",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"",
        /* setFun */"",
        /* dateAdd */now);

        configs[i] = new KKConfiguration(
        /* title */"Return URL",
        /* key */"MODULE_PAYMENT_VALITOR_RETURN_URL",
        /* value */"http://host:port/konakart/CheckoutFinished.action",
        /* description */"URL to return to when leaving VALITOR web"
                + " site after a successful transaction",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"",
        /* setFun */"",
        /* dateAdd */now);

        configs[i] = new KKConfiguration(
        /* title */"Cancel URL",
        /* key */"MODULE_PAYMENT_VALITOR_CANCEL_URL",
        /* value */"http://host:port/konakart/CatalogCheckoutExternalPaymentErrorPage.action",
        /* description */"URL to return to when leaving VALITOR web"
                + " site after an unsuccesful transaction",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"",
        /* setFun */"",
        /* dateAdd */now);

        configs[i] = new KKConfiguration(
        /* title */"Test Mode",
        /* key */"MODULE_PAYMENT_VALITOR_TEST_MODE",
        /* value */"true",
        /* description */"Forces KonaKart to use the VALITOR Sandbox",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"",
        /* setFun */"tep_cfg_select_option(array('true', 'false'), ",
        /* dateAdd */now);

        configs[i] = new KKConfiguration(
        /* title */"VALITOR Id",
        /* key */"MODULE_PAYMENT_VALITOR_ID",
        /* value */"VALITORId",
        /* description */"The merchant VALITOR Id",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"",
        /* setFun */"",
        /* dateAdd */now);

        configs[i] = new KKConfiguration(
        /* title */"Transaction Currency",
        /* key */"MODULE_PAYMENT_VALITOR_CURRENCY",
        /* value */"Selected Currency",
        /* description */"Currency to use for VALITOR transaction",
        /* groupId */groupId,
        /* sortO */i++,
        /* useFun */"",
        /* setFun */"",
        /* dateAdd */now);

        return configs;
    }
}