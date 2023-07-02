package com.tictactoe;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;

class InitServletTest extends Mockito{
    @Mock
    HttpServletRequest requestMock;
    @Mock
    HttpServletResponse responseMock;
    @Mock
    HttpSession currentSessionMock;
    @Mock
    RequestDispatcher dispatcherMock;
    @Mock
    ServletConfig configMock;
    @Mock
    ServletContext servletContextMock;
    @Spy
    InitServlet initServletSpy;

    AutoCloseable closeable;

    @BeforeEach
    void initServlet() throws ServletException {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(requestMock.getSession(true)).thenReturn(currentSessionMock);
        initServletSpy.init(configMock);
        Mockito.when(configMock.getServletContext()).thenReturn(servletContextMock);
        Mockito.when(initServletSpy.getServletContext().getRequestDispatcher("/index.jsp"))
                .thenReturn(dispatcherMock);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void doGet_Should_CreateEmptyField() throws ServletException, IOException {
        List<Sign> data = Arrays.asList(Sign.EMPTY, Sign.EMPTY, Sign.EMPTY, Sign.EMPTY, Sign.EMPTY, Sign.EMPTY, Sign.EMPTY, Sign.EMPTY, Sign.EMPTY);

        initServletSpy.doGet(requestMock, responseMock);

        verify(currentSessionMock).setAttribute("data", data);
    }

    @Test
    void doGet_Should_ForwardToIndexJsp() throws ServletException, IOException {
        Mockito.when(initServletSpy.getServletContext().getRequestDispatcher("/index.jsp"))
                .thenReturn(dispatcherMock);

        initServletSpy.doGet(requestMock, responseMock);

        Mockito.verify(dispatcherMock).forward(requestMock, responseMock);
    }

}