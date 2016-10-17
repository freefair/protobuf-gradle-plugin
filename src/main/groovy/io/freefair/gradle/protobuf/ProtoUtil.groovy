package io.freefair.gradle.protobuf

/**
 * Created by larsgrefer on 11.10.16.
 */
class ProtoUtil {

    static String getProtocZipLink(String version) {
        String protocArch = getProtocArch()

        return getProtocZipLink(version, protocArch)
    }

    static String getProtocZipLink(String version, String protocArch) {
        return "https://github.com/google/protobuf/releases/download/v$version/protoc-$version-${protocArch}.zip"
    }

    public static String getProtocArch() {
        String osName = System.getProperty("os.name").toLowerCase();
        String osArch = System.getProperty("os.arch").toLowerCase();
        String dataModel = System.getProperty("sun.arch.data.model");

        String arch = dataModel != null ? dataModel : (osArch.endsWith("64") ? "64" : "32");

        String protocArch;

        if (osName.contains("mac")) {
            protocArch = "osx-x86_$arch";
        } else if (osName.contains("win")) {
            protocArch = "win32";
        } else {
            protocArch = "linux-x86_$arch";
        }
        return protocArch
    }
}
