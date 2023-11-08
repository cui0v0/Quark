package org.violetmoon.zeta.network;

import java.io.Serializable;

public interface IZetaMessage extends Serializable {
	boolean receive(IZetaNetworkEventContext context);
}
