package com.cinemamod.downloader;

public final class Resource {

    public static final String BASE_URL = "https://minecraftbibliotek.blob.core.windows.net/minecraft/jcef/";
    //public static final String BASE_URL = "https://cinemamod-libraries.ewr1.vultrobjects.com/";
    public static final String CINEMAMOD_VERSIONS_URL = BASE_URL + "versions.txt";
    public static final String CINEMAMOD_JCEF_URL_FORMAT = BASE_URL + "jcef/%s/%s";
    public static final String CINEMAMOD_JCEF_PATCHES_URL_FORMAT = BASE_URL + "jcef-patches/%s/%s";

    public static String getJcefUrl(String cefBranch, String platform) {
        return CINEMAMOD_JCEF_URL_FORMAT.formatted(cefBranch, platform);
    }

    public static String getJcefPatchesUrl(String cefBranch, String platform) {
        return CINEMAMOD_JCEF_PATCHES_URL_FORMAT.formatted(cefBranch, platform);
    }

}
