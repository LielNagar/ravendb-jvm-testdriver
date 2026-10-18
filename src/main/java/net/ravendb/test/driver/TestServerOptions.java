package net.ravendb.test.driver;

import net.ravendb.embedded.ServerOptions;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Server options for the test driver. Unlike plain {@link ServerOptions}, a test server fails to start
 * when no valid license is available.
 */
public final class TestServerOptions extends ServerOptions {

    public TestServerOptions() {
        getLicensing().setThrowOnInvalidOrMissingLicense(true);
    }

    /**
     * Binds the server to the machine name instead of localhost, so Fiddler captures its traffic
     * without any client configuration.
     *
     * @return options of a server whose traffic Fiddler can capture
     */
    public static TestServerOptions useFiddler() {
        TestServerOptions options = new TestServerOptions();
        options.setServerUrl("http://" + getMachineName() + ":0");
        options.getCommandLineArgs().add("--Security.UnsecuredAccessAllowed=PrivateNetwork");
        return options;
    }

    /**
     * The equivalent of .NET's Environment.MachineName, which on Windows is the COMPUTERNAME variable.
     */
    static String getMachineName() {
        String computerName = System.getenv("COMPUTERNAME");
        if (StringUtils.isNotBlank(computerName)) {
            return computerName;
        }

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new IllegalStateException("Unable to determine the machine name: " + e.getMessage(), e);
        }
    }
}
