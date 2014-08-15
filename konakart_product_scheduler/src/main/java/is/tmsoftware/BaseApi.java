package is.tmsoftware;

import com.konakart.app.EngineConfig;
import com.konakart.appif.KKEngIf;
import com.konakart.util.KKConstants;
import com.konakartadmin.app.AdminEngineConfig;
import com.konakartadmin.app.KKAdminException;
import com.konakartadmin.appif.KKAdminIf;
import com.konakartadmin.ws.KKAdminEngineMgr;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author bjarnig
 */
public class BaseApi {
    
    protected static String DEFAULT_USERNAME = "admin@konakart.com";

    protected static String DEFAULT_PASSWORD = "12345678";

    protected static final String DEFAULT_STD_KKADMIN_ENG_CLASS = "com.konakartadmin.bl.KKAdmin";

    protected static final String KKEngName = "com.konakart.app.KKEng";

    protected static final String DEFAULT_WS_KKADMIN_ENG_CLASS = "com.konakartadmin.ws.KKWSAdmin";

    protected static final String DEFAULT_RMI_KKADMIN_ENG_CLASS = "com.konakartadmin.rmi.KKRMIAdminEng";

    protected static final String DEFAULT_CUSTOM_KKADMIN_ENG_CLASS = "com.konakartadmin.bl.KKCustomAdminService";

    protected static final String DEFAULT_CUSTOM_KKWSADMIN_ENG_CLASS = "com.konakartadmin.ws.KKWSCustomAdminService";

    protected static final String DEF_STORE_ID = KKConstants.KONAKART_DEFAULT_STORE_ID;

    protected static KKAdminIf eng;

    protected static KKEngIf kkEng;

    /** StoreId - Used in Multi-Store Installations to identify the store */
    private static String storeId = KKConstants.KONAKART_DEFAULT_STORE_ID;

    /** engClassName - Name of the engine to use */
    private static String engClassName = DEFAULT_STD_KKADMIN_ENG_CLASS;

    /** POJO Custom Store Service Engine */
    protected static final String kkCustomAdminEngName = DEFAULT_CUSTOM_KKADMIN_ENG_CLASS;

    /** SOAP Custom Store Service Engine */
    protected static final String kkWSCustomAdminEngName = DEFAULT_CUSTOM_KKWSADMIN_ENG_CLASS;

    /** Engine Mode - 0 = Single Store 1 = Multi-Store Multiple DBs, 2 = Multi-Store Single DB */
    private static int engineMode = KKConstants.MODE_SINGLE_STORE;

    /** debug flag */
    private static boolean debug = false;

    /** customersShared flag */
    private static boolean customersShared = false;

    /** productsShared flag */
    private static boolean productsShared = false;

    /** categoriesShared flag */
    private static boolean categoriesShared = false;

    /** username */
    private static String username = DEFAULT_USERNAME;

    /** password */
    private static String password = DEFAULT_PASSWORD;

    /**
     * The session id returned by a successful login
     */
    protected static String sessionId;

    /**
     * Initialise a KonaKart Admin engine instance by name and perform a login to get a session id.
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws KKAdminException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalArgumentException
     */
    static protected void init(int engMode, String sId, String engineClass, boolean custShared,
            boolean prodShared, boolean catShared, String[] properties)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            KKAdminException, IllegalArgumentException, InvocationTargetException
    {
        /*
         * Instantiate a KonaKart Admin Engine instance by name
         */
        KKAdminEngineMgr kkAdminEngMgr = new KKAdminEngineMgr();

        AdminEngineConfig adEngConf = new AdminEngineConfig();
        adEngConf.setMode(engMode);
        adEngConf.setPropertiesFileName(KKConstants.KONAKARTADMIN_PROPERTIES_FILE);
        adEngConf.setAxisClientFileName(KKConstants.KONAKARTADMIN_WS_CLIENT_PROPERTIES_FILE);
        adEngConf.setStoreId(sId);
        adEngConf.setCustomersShared(custShared);
        adEngConf.setProductsShared(prodShared);
        adEngConf.setCategoriesShared(catShared);
        adEngConf.setProperties(properties);

        EngineConfig engConf = new EngineConfig();
        engConf.setMode(getEngineMode());
        engConf.setStoreId(getStoreId());
        engConf.setCustomersShared(isCustomersShared());
        engConf.setProductsShared(isProductsShared());
        engConf.setCategoriesShared(isCategoriesShared());

        /*
         * This creates a KonaKart Admin Engine by name using the constructor that requires an
         * AdminEngineConfig object. This is the recommended approach.
         */
        setEng(kkAdminEngMgr.getKKAdminByName(engineClass, adEngConf));

        // Set the KK Engine
        setEng(getKKEngByName(KKEngName, engConf));

        /*
         * Login with default credentials
         */
        sessionId = getEng().login(getUsername(), getPassword());
    }

    public static KKAdminIf getEng()
    {
        return eng;
    }

    public static void setEng(KKAdminIf eng)
    {
        BaseApi.eng = eng;
    }

    public static void setEng(KKEngIf kkEng)
    {
        BaseApi.kkEng = kkEng;
    }

    public static String getStoreId()
    {
        return storeId;
    }

    public static void setStoreId(String storeId)
    {
        BaseApi.storeId = storeId;
    }

    public static int getEngineMode()
    {
        return engineMode;
    }

    public static void setEngineMode(int engineMode)
    {
        BaseApi.engineMode = engineMode;
    }

    public static boolean isDebug()
    {
        return debug;
    }

    public static void setDebug(boolean debug)
    {
        BaseApi.debug = debug;
    }

    public static boolean isCustomersShared()
    {
        return customersShared;
    }

    public static void setCustomersShared(boolean customersShared)
    {
        BaseApi.customersShared = customersShared;
    }

    public static boolean isProductsShared()
    {
        return productsShared;
    }

    public static void setProductsShared(boolean productsShared)
    {
        BaseApi.productsShared = productsShared;
    }

    public static String getSessionId()
    {
        return sessionId;
    }

    public static void setSessionId(String sessionId)
    {
        BaseApi.sessionId = sessionId;
    }

    public static String getEngClassName()
    {
        return engClassName;
    }

    public static void setEngClassName(String engClassName)
    {
        BaseApi.engClassName = engClassName;
    }

    public static boolean isCategoriesShared()
    {
        return categoriesShared;
    }

    public static void setCategoriesShared(boolean categoriesShared)
    {
        BaseApi.categoriesShared = categoriesShared;
    }

    public static String getUsername()
    {
        return username;
    }

    public static void setUsername(String username)
    {
        BaseApi.username = username;
    }

    public static String getPassword()
    {
        return password;
    }

    public static void setPassword(String password)
    {
        BaseApi.password = password;
    }


    protected static KKEngIf getKKEngByName(String engineClassName, EngineConfig config)
            throws IllegalArgumentException, InstantiationException, IllegalAccessException,
            InvocationTargetException, ClassNotFoundException {
        Class<?> engineClass = Class.forName(engineClassName);
        KKEngIf kkeng = null;
        Constructor<?>[] constructors = engineClass.getConstructors();
        Constructor<?> engConstructor = null;
        if (constructors != null && constructors.length > 0) {
            for (int i = 0; i < constructors.length; i++) {
                Constructor<?> constructor = constructors[i];
                Class<?>[] parmTypes = constructor.getParameterTypes();
                if (parmTypes != null && parmTypes.length == 1) {
                    String parmName = parmTypes[0].getName();
                    if (parmName != null && parmName.equals("com.konakart.appif.EngineConfigIf")) {
                        engConstructor = constructor;
                    }
                }
            }
        }

        if (engConstructor != null) {
            kkeng = (KKEngIf) engConstructor.newInstance(config);
        }

        return kkeng;
    }
}
