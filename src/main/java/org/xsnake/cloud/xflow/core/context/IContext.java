package org.xsnake.cloud.xflow.core.context;

public interface IContext {

	Object getAttribute(final String id);

    void setAttribute(final String id, final Object obj);

    Object removeAttribute(final String id);

    void clear();
	
}
