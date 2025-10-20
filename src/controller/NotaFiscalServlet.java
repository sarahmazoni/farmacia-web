package controller;

import dao.NotaFiscalDAO;
import model.NotaFiscal;
import view.View;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/NotaFiscalServlet")
public class NotaFiscalServlet extends HttpServlet {

    private NotaFiscalDAO notaFiscalDAO;

    @Override
    public void init() throws ServletException {
        notaFiscalDAO = new NotaFiscalDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "view":
                    try {
                        int pedidoId = Integer.parseInt(request.getParameter("pedidoId"));
                        NotaFiscal nf = notaFiscalDAO.buscarPorPedidoId(pedidoId);
                        if (nf != null) {
                            request.setAttribute("nota", nf);
                            View.render(request, response, "/notaFiscalView.jsp");
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Nota Fiscal não encontrada");
                        }
                    } catch (NumberFormatException e) {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de pedido inválido");
                    }
                    break;

                case "list":
                default:
                    List<NotaFiscal> notas = notaFiscalDAO.listarTodos();
                    request.setAttribute("notas", notas);
                    View.render(request, response, "/notaFiscalList.jsp");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
