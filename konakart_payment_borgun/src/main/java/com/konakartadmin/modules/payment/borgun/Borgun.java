package com.konakartadmin.modules.payment.borgun;

import com.konakart.util.Utils;
import com.konakartadmin.app.KKConfiguration;
import com.konakartadmin.bl.KKAdminBase;
import com.konakartadmin.modules.PaymentModule;

import java.util.Date;

/**
 * Borgun payment module
 * <p/>
 * These definitions are used to allow the Administration Application to define the payment module's
 * configuration parameters.
 */
public class Borgun extends PaymentModule {
    /**
     * @return the configuration key stub
     */
    public String getConfigKeyStub() {
        if (configKeyStub == null) {
            setConfigKeyStub(super.getConfigKeyStub() + "_BORGUN");
        }
        return configKeyStub;
    }

    public String getModuleTitle() {
        return getMsgs().getString("MODULE_PAYMENT_BORGUN_TEXT_TITLE");
    }

    /**
     * @return the implementation filename. (For osCommerce compatibility you can use the php
     * version for these names)
     */
    public String getImplementationFileName() {
        return "borgun.php";
    }

    /**
     * @return the module code
     */
    public String getModuleCode() {
        return "borgun";
    }

    /**
     * @return an array of configuration values for this payment module
     */
    public KKConfiguration[] getConfigs() {
        if (configs == null) {
            configs = new KKConfiguration[7];
        }

        if (configs[0] != null && !Utils.isBlank(configs[0].getConfigurationKey())) {
            return configs;
        }

        Date now = KKAdminBase.getKonakartTimeStampDate();

        int i = 0;
        int groupId = 6;

        configs[i++] = new KKConfiguration(
        /* title */"Enable Borgun Module",
        /* key */"MODULE_PAYMENT_BORGUN_STATUS",
        /* value */"true",
        /* description */"Do you want to accept Borgun payments? ('true' or 'false')",
        /* groupId */groupId,
        /* sort Order */i,
        /* useFun */"",
        /* setFun */"tep_cfg_select_option(array('true', 'false'), ",
        /* dateAdd */now);

        configs[i++] = new KKConfiguration(
        /* title */"Sort order of display.",
        /* key */"MODULE_PAYMENT_BORGUN_SORT_ORDER",
        /* value */"0",
        /* description */"Sort order of display. Lowest is displayed first.",
        /* groupId */groupId,
        /* sort Order */i,
        /* useFun */"",
        /* setFun */"",
        /* dateAdd */now);

        configs[i++] = new KKConfiguration(
        /* title */"Payment Zone",
        /* key */"MODULE_PAYMENT_BORGUN_ZONE",
        /* value */"0",
        /* description */"If a zone is selected, only enable this payment method for that zone.",
        /* groupId */groupId,
        /* sort Order */i,
        /* useFun */"tep_get_zone_class_title",
        /* setFun */"tep_cfg_pull_down_zone_classes(",
        /* dateAdd */now);

        configs[i++] = new KKConfiguration(
        /* title */"Borgun Username",
        /* key */"MODULE_PAYMENT_BORGUN_USERNAME",
        /* value */"pj-ql-01",
        /* description */"The username used to access the Borgun service",
        /* groupId */groupId,
        /* sort Order */i,
        /* useFun */"",
        /* setFun */"",
        /* dateAdd */now);

        configs[i++] = new KKConfiguration(
        /* title */"Borgun Password",
        /* key */"MODULE_PAYMENT_BORGUN_PASSWORD",
        /* value */"pj-ql-01p",
        /* description */"The password used to access the Borgun service",
        /* groupId */groupId,
        /* sort Order */i,
        /* useFun */"",
        /* setFun */"password",
        /* dateAdd */now);

        configs[i++] = new KKConfiguration(
        /* title */"Payment Server URL",
        /* key */"MODULE_PAYMENT_BORGUN_URL",
        /* value */"https://borgunlabs.com/quick_link",
        /* description */"URL used by KonaKart to send the transaction details",
        /* groupId */groupId,
        /* sortO */i,
        /* useFun */"",
        /* setFun */"",
        /* dateAdd */now);

        configs[i++] = new KKConfiguration(
                /* title */"Security Options",
                /* key */"MODULE_PAYMENT_BORGUN_SECURITY",
                /* value */"AWZ|M|false|true|false",
                /* description */"Security Options for Borgun - refer to Borgun documentation for details",
                /* groupId */groupId,
                /* sortO */i,
                /* useFun */"",
                /* setFun */"",
                /* dateAdd */now);

        return configs;
    }
}