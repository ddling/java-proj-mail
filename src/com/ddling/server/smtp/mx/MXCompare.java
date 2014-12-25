package com.ddling.server.smtp.mx;

import org.xbill.DNS.MXRecord;

import java.util.Comparator;

/**
 * Created by ddling on 2014/12/25.
 */
public class MXCompare implements Comparator {

    @Override
    public int compare(Object arg0, Object arg1) {
        return Integer.compare(((MXRecord) arg0).getPriority(), ((MXRecord) arg1).getPriority());
    }
}
