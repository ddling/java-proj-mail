package com.ddling.server.smtp.mx;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ddling on 2014/12/25.
 */
public class MXExchange {

    public static ArrayList<MXRecord> mxLookup(String server) throws Exception {

        ArrayList<MXRecord> mxServers = new ArrayList<MXRecord>();

        Lookup lookup = new Lookup(server, Type.MX);
        Record[] records = lookup.run();

        for (int i = 0; i < records.length; i++) {
            mxServers.add((MXRecord)records[i]);
        }

        Collections.sort(mxServers, new MXCompare());
        return mxServers;
    }

    public static String getMxServer(String server) throws Exception {

        ArrayList<MXRecord> servers = MXExchange.mxLookup(server);
        if (servers.isEmpty()) {
            return null;
        }

        String serverName = servers.get(0).getTarget().toString();
        return serverName.substring(0, serverName.length() - 1);
    }

    public static void main(String[] args) {
        String serverName = "gmail.com";
        try {
            String name = getMxServer(serverName);
            System.out.print(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
