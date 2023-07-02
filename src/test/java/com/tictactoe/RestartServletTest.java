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

import static org.junit.jupiter.api.Assertions.*;

class RestartServletTest extends Mockito {
    @Mock
    HttpServletRequest requestMock;
    @Mock
    HttpServletResponse responseMock;
    @Mock
    HttpSession currentSessionMock;
    @Spy
    RestartServlet restartServlet;

    AutoCloseable closeable;

    @BeforeEach
    void initServlet() throws ServletException {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(requestMock.getSession()).thenReturn(currentSessionMock);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void doPost_Should_InvalidateSession() throws IOException {
        restartServlet.doPost(requestMock, responseMock);

        Mockito.verify(currentSessionMock).invalidate();
    }

    @Test
    void doPost_Should_RedirectToStartJsp() throws IOException, ServletException {
        restartServlet.doPost(requestMock, responseMock);

        Mockito.verify(responseMock).sendRedirect("/start");
    }
}