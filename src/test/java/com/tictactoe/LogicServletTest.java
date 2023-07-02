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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LogicServletTest extends Mockito {

    Field field;
    @Mock
    HttpServletRequest requestMock;
    @Mock
    HttpServletResponse responseMock;
    @Mock
    HttpSession currentSessionMock;
    @Mock
    AutoCloseable closeable;

    @BeforeEach
    void initServlet() {
        field = spy(Field.class);
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(requestMock.getSession()).thenReturn(currentSessionMock);
        Mockito.when(currentSessionMock.getAttribute("field")).thenReturn(field);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6,7,8})
    void doGet_Should_PutCrossOnCorrectIndex(Integer integer) throws ServletException, IOException {
        Mockito.when(requestMock.getParameter("click")).thenReturn(String.valueOf(integer));

        new LogicServlet().doGet(requestMock, responseMock);

        List<Sign> data = field.getFieldData();
        assertEquals(data.get(integer), Sign.CROSS);
    }

    @ParameterizedTest
    @ValueSource(strings={"CROSS", "NOUGHT"})
    void doGet_SouldNot_PutCrossWhenGameIsFinished(String sign) throws ServletException, IOException {
        field.getField().put(0, Sign.valueOf(sign));
        field.getField().put(1, Sign.valueOf(sign));
        field.getField().put(2, Sign.valueOf(sign));
        Mockito.when(requestMock.getParameter("click")).thenReturn("3");

        new LogicServlet().doGet(requestMock, responseMock);

        List<Sign> data = field.getFieldData();
        assertEquals(data.get(3), Sign.EMPTY);
    }

    @Test
    void doGet_Should_RedirectWhenCellIsNotEmpty() throws ServletException, IOException {
        RequestDispatcher dispatcherMock = mock(RequestDispatcher.class);
        ServletConfig configMock = mock(ServletConfig.class);
        ServletContext servletContextMock = mock(ServletContext.class);
        LogicServlet logicServletSpy = Mockito.spy(new LogicServlet());
        logicServletSpy.init(configMock);
        Mockito.when(configMock.getServletContext()).thenReturn(servletContextMock);
        Mockito.when(logicServletSpy.getServletContext().getRequestDispatcher("/index.jsp"))
                .thenReturn(dispatcherMock);
        field.getField().put(3, Sign.CROSS);
        Mockito.when(requestMock.getParameter("click")).thenReturn("3");

        logicServletSpy.doGet(requestMock, responseMock);

        Mockito.verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @ParameterizedTest
    @ValueSource(strings={"CROSS", "NOUGHT"})
    void doGet_Should_ReturnCorrectWinner(String sign) throws ServletException, IOException {
        field.getField().put(0, Sign.valueOf(sign));
        field.getField().put(1, Sign.valueOf(sign));
        field.getField().put(2, Sign.valueOf(sign));

        new LogicServlet().doGet(requestMock, responseMock);

        verify(currentSessionMock).setAttribute("winner", Sign.valueOf(sign));
    }

    @Test
    void doGet_Should_SetAttributeDrawWhenNeeded() throws ServletException, IOException {
        field.getField().put(0, Sign.NOUGHT);
        field.getField().put(1, Sign.CROSS);
        field.getField().put(2, Sign.NOUGHT);
        field.getField().put(3, Sign.NOUGHT);
        field.getField().put(4, Sign.CROSS);
        field.getField().put(5, Sign.CROSS);
        field.getField().put(6, Sign.CROSS);
        field.getField().put(7, Sign.NOUGHT);
        Mockito.when(requestMock.getParameter("click")).thenReturn("8");

        new LogicServlet().doGet(requestMock, responseMock);

        verify(currentSessionMock).setAttribute("draw", true);
    }

}