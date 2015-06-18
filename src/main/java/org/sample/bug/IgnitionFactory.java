package org.sample.bug;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.springframework.stereotype.Component;

@Component
public class IgnitionFactory {

    public Ignite ignite(){
        return Ignition.start(IgnitionFactory.class.getClassLoader().getResource("gridgain-config-context.xml"));
    }

}
