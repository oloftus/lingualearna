package com.lingualearna.web.filter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AddHeadersFilterTest {

    private static final String BLANK = null;
    private static final String INIT_PARAM_KEY = "Headers";
    private static final String HEADER_1_KEY = "header1Key";
    private static final String HEADER_1_VALUE = "header1Value";
    private static final String HEADER_2_KEY = "header2Key";
    private static final String HEADER_2_VALUE = "header2Value";
    private static final String INVALID_HEADER = "invalidHeader";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private FilterConfig filterConfig;

    AddHeadersFilter filter = new AddHeadersFilter();

    private void givenACorrectWebXml() {

        when(filterConfig.getInitParameter(INIT_PARAM_KEY))
                .thenReturn(String.format("%s = %s; %s=%s", HEADER_1_KEY, HEADER_1_VALUE, HEADER_2_KEY, HEADER_2_VALUE));
    }

    private void givenAnInvalidWebXml() {

        when(filterConfig.getInitParameter(INIT_PARAM_KEY)).thenReturn(INVALID_HEADER);
    }

    private void givenAnUnconfiguredWebXml() {

        when(filterConfig.getInitParameter(INIT_PARAM_KEY)).thenReturn(BLANK);
    }

    @Test
    public void testFilterPassesThroughWhenNotConfigured() throws Exception {

        givenAnUnconfiguredWebXml();
        whenICallTheFilter();
        thenItPassesThrough();
    }

    @Test
    public void testFilterSetsUpHeadersCorrectly() throws Exception {

        givenACorrectWebXml();
        whenICallTheFilter();
        thenTheHeadersAreCorrectlySet();
    }

    @Test(expected = ServletException.class)
    public void testFilterThrowsExceptionOnInValidWebXml() throws Exception {

        givenAnInvalidWebXml();
        whenICallTheFilter();
    }

    private void thenItPassesThrough() {

        verify(response, never()).setHeader(any(String.class), any(String.class));
    }

    private void thenTheHeadersAreCorrectlySet() throws IOException, ServletException {

        verify(response).setHeader(HEADER_1_KEY, HEADER_1_VALUE);
        verify(response).setHeader(HEADER_2_KEY, HEADER_2_VALUE);
        verify(chain).doFilter(request, response);
    }

    private void whenICallTheFilter() throws ServletException, IOException {

        filter.init(filterConfig);
        filter.doFilter(request, response, chain);
    }
}
