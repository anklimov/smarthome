/**
 * Copyright (c) 2014,2017 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.smarthome.ui.internal.proxy;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.B64Code;
import org.eclipse.jetty.util.StringUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the {@link ProxyServletService} class.
 *
 * @author Kai Kreuzer - Initial contribution
 *
 */
public class ProxyServletServiceTest {

    static private ProxyServletService service;

    @Before
    public void setUp() {
        service = new ProxyServletService();
    }

    @Test
    public void testMaybeAppendAuthHeaderWithFullCredentials() throws URISyntaxException {
        Request request = mock(Request.class);
        URI uri = new URI("http://testuser:testpassword@127.0.0.1:8080/content");
        service.maybeAppendAuthHeader(uri, request);
        verify(request).header(HttpHeader.AUTHORIZATION,
                "Basic " + B64Code.encode("testuser:testpassword", StringUtil.__ISO_8859_1));
    }

    @Test
    public void testMaybeAppendAuthHeaderWithoutPassword() throws URISyntaxException {
        Request request = mock(Request.class);
        URI uri = new URI("http://testuser@127.0.0.1:8080/content");
        service.maybeAppendAuthHeader(uri, request);
        verify(request).header(HttpHeader.AUTHORIZATION,
                "Basic " + B64Code.encode("testuser:", StringUtil.__ISO_8859_1));
    }

    @Test
    public void testMaybeAppendAuthHeaderWithoutCredentials() throws URISyntaxException {
        Request request = mock(Request.class);
        URI uri = new URI("http://127.0.0.1:8080/content");
        service.maybeAppendAuthHeader(uri, request);
        verify(request, never()).header(any(HttpHeader.class), anyString());
    }
}
