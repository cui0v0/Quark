package org.violetmoon.zeta.client.event.play;

import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

public interface ZClientTick extends IZetaPlayEvent {
    ZPhase getPhase();

    enum ZPhase {
        START, END;
    }
}
