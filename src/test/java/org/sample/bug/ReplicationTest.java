package org.sample.bug;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicLong;
import org.apache.ignite.IgniteAtomicSequence;
import org.apache.ignite.Ignition;
import org.gridkit.nanocloud.Cloud;
import org.gridkit.nanocloud.CloudFactory;
import org.gridkit.nanocloud.VX;
import org.gridkit.vicluster.ViNode;
import org.gridkit.vicluster.ViProps;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReplicationTest {
    @Test
    public void testReplication() throws Exception {

        final Cloud cluster = CloudFactory.createCloud();
        ViProps.at(cluster.node("**")).setLocalType();
        cluster.node("**").setProp("IGNITE_QUIET", "false");

        // start data node in separate JVM
        final ViNode localNode = cluster.node("local-node");
//        localNode.x(VX.PROCESS).addJvmArg("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005"); // uncomment for debug
        final CacheRemote localClusterCache = localNode.exec(new Callable<CacheRemote>() {
            public CacheRemote call() throws Exception {
                return Starter.startLocalCluster();
            }
        });
        
        // start data node in separate JVM
        final ViNode remoteNode = cluster.node("remote-node");
        final CacheRemote remoteClusterCache = remoteNode.exec(new Callable<CacheRemote>() {
            public CacheRemote call() throws Exception {
                return Starter.startRemoteCluster();
            }
        });

        Thread.sleep(20000);

        localClusterCache.put("sample.key", "sample.value");

        Thread.sleep(20000);

        assertEquals("sample.value", localClusterCache.get("sample.key"));
        assertEquals("sample.value", remoteClusterCache.get("sample.key"));

    }

}
