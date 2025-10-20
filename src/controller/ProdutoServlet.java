package controller;

import dao.ProdutoDAO;
import model.Produto;
import view.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/ProdutoServlet")
public class ProdutoServlet extends HttpServlet {
    private ProdutoDAO produtoDAO;

    @Override
    public void init() throws ServletException {
        produtoDAO = new ProdutoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    View.render(request, response, "/produtoForm.jsp");
                    break;
                case "edit":
                    int idEdit = safeParseInt(request.getParameter("id"), 0);
                    if (idEdit > 0) {
                        Produto produtoEdit = produtoDAO.getById(idEdit);
                        if (produtoEdit != null) {
                            request.setAttribute("produto", produtoEdit);
                            View.render(request, response, "/produtoForm.jsp");
                        } else {
                            View.redirect(response, "ProdutoServlet?action=list");
                        }
                    } else {
                        View.redirect(response, "ProdutoServlet?action=list");
                    }
                    break;
                case "delete":
                    int idDelete = safeParseInt(request.getParameter("id"), 0);
                    if (idDelete > 0) produtoDAO.delete(idDelete);
                    View.redirect(response, "ProdutoServlet?action=list");
                    break;
                case "list":
                default:
                    List<Produto> produtos = produtoDAO.getAll();
                    request.setAttribute("produtos", produtos);
                    View.render(request, response, "/produtoList.jsp");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Produto produto = new Produto();
            produto.setId(safeParseInt(request.getParameter("id"), 0));
            produto.setNome(request.getParameter("nome"));
            produto.setDescricao(request.getParameter("descricao"));
            produto.setCodigoBarras(request.getParameter("codigoBarras"));
            produto.setFabricante(request.getParameter("fabricante"));
            produto.setCategoria(request.getParameter("categoria"));
            produto.setPrecoCompra(safeParseDouble(request.getParameter("precoCompra"), 0.0));
            produto.setPrecoVenda(safeParseDouble(request.getParameter("precoVenda"), 0.0));
            produto.setEstoque(safeParseInt(request.getParameter("estoque"), 0));
            produto.setControlado("on".equals(request.getParameter("controlado")));

            if (produto.getId() > 0) {
                produtoDAO.update(produto);
            } else {
                produtoDAO.insert(produto);
            }
            View.redirect(response, "ProdutoServlet?action=list");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private int safeParseInt(String value, int defaultVal) {
        try { return Integer.parseInt(value); } catch (Exception e) { return defaultVal; }
    }

    private double safeParseDouble(String value, double defaultVal) {
        try { return Double.parseDouble(value); } catch (Exception e) { return defaultVal; }
    }
}
